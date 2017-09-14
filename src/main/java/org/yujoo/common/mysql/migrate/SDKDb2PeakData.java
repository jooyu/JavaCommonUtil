package org.yujoo.common.mysql.migrate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.peak.core.db.sql.impl.SelectStatement;
import com.peak.core.spring.SpringContext;

@Component
public class SDKDb2PeakData extends ExecuteCommand {
	private static final Logger LOGGER = LoggerFactory.getLogger(SDKDb2PeakData.class);

	static SelectStatement selectSql = new SelectStatement();

	SDKDb2PeakDataConfig config;

	Map<String, Integer> identifierMaps = new HashMap<>();
	JdbcTemplate peakDataJdbc;
	JdbcTemplate peakDealJdbc;

	public String name() {
		return "sdk_db_2_peak_data";
	}

	public void execute(String[] args) {
		try {
			loadInfo();
			
			if (identifierMaps.isEmpty()) {
				LOGGER.info("identifier map is emtpy.");
				return;
			}

			LOGGER.info("are you sure?(Y/N)");
			byte[] buffer = new byte[512];
			System.in.read(buffer);
			
			String isSure = new String(buffer).trim();
			if (!isSure.toLowerCase().equals("y")) {
				LOGGER.info("bye!");
				return;
			}

			// 汇聚数据到 user_jieo表
			LOGGER.info("import `union db` to user_jieo table.");
			moveData();

			// 划分数据到每款游戏表
			LOGGER.info("assign to game table.");
			initTableByGameId();

			LOGGER.info("bye!");
		} catch (Exception ex) {
			LOGGER.error("{}", ex);
		}
	}

	private void loadInfo() {
		config = SpringContext.getBean(SDKDb2PeakDataConfig.class);

		LOGGER.info("config info:");
		LOGGER.info("---------------------------------------------------------");
		for (Entry<String, Object> entry : config.getFieldMpas().entrySet()) {
			LOGGER.info(entry.getKey() + " = " + entry.getValue());
		}
		LOGGER.info("---------------------------------------------------------");

		this.peakDataJdbc = getJdbc(config.peak_data_db_ip, config.peak_data_user_name, config.peak_data_password, config.peak_data_db);
		this.peakDealJdbc = getJdbc(config.peak_data_db_ip, config.peak_data_user_name, config.peak_data_password, config.peak_deal_db);

		this.identifierMaps = getChannelIdentifierList(config);

		LOGGER.info("identifier load complete! {} size.", identifierMaps.size());
	}

	/**
	 * @param config
	 */
	public void moveData() {

		// TRUNCATE TABLE user_jieo
		peakDataJdbc.execute("TRUNCATE TABLE `peak_data_01`.`user_jieo`");

		int count = 0;
		for (int i = config.union_db_hash_min; i <= config.union_db_hash_max; i++) {
			String dbName = config.union_db_prefix + i;
			JdbcTemplate jdbc = getJdbc(config.union_db_ip, config.union_user_name, config.union_password, dbName);
			int pageSize = 500;
			for (int t = config.union_table_hash_min; t <= config.union_table_hash_max; t++) {
				int pageIndex = 0;
				while (true) {
					String tableName = config.union_table_prefix + t;
					String sql = String.format("select * from %s limit %s,%s", tableName, pageIndex * pageSize, pageSize);

					List<Map<String, Object>> list = jdbc.queryForList(sql);
					if (list == null || list.size() < 1) {
						break;
					}
					count += list.size();

					List<OpenIdPlayerIds> openIdList = new ArrayList<>();
					for (Map<String, Object> item : list) {

						if (isFilterOpenId(item)) {
							continue;
						}

						OpenIdPlayerIds openIdItem = new OpenIdPlayerIds(item);
						openIdList.add(openIdItem);

						if (openIdItem.channel_openid.equals("55d9b8cd1f5ae95fee938c82d82f1075")) {
							System.out.println(sql);
						}
					}
					moveData2PeakData(openIdList);
					pageIndex++;
				}
			}
		}
		LOGGER.info("execute complete! {} total records.", count);
	}
	
	private boolean isFilterOpenId(Map<String, Object> item) {
		String openId = item.get("openid").toString();
		return config.filter_open_id_list.contains(openId);
	}

	public void moveData2PeakData(List<OpenIdPlayerIds> openIdList) {
		if (openIdList.isEmpty()) {
			return;
		}

		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO `user_jieo` (`game_id`,`open_id`,`channel_id`,`channel_uid`,`ledou_player_id`) VALUES ");
		openIdList.forEach(item -> {
			Integer channelId = identifierMaps.get(item.channel_id);
			if (channelId == null) {
				LOGGER.warn("channelId not found... channel_identifier:{}", item.channel_id);
			} else {
				sb.append(String.format("('%s','%s','%s','%s','%s'),", item.game_id, item.openid, channelId, item.channel_openid, item.player_id));
			}
		});
		sb.deleteCharAt(sb.length() - 1);

		peakDataJdbc.update(sb.toString());

		LOGGER.info("import data complete! {} record.", openIdList.size());
	}

	public Map<String, Integer> getChannelIdentifierList(SDKDb2PeakDataConfig config) {
		Map<String, Integer> identifierList = new HashMap<>();

		String sql = "select * from channel_package";
		List<Map<String, Object>> list = peakDealJdbc.queryForList(sql);
		for (Map<String, Object> item : list) {
			String identifier = item.get("channel_identifier").toString();
			int channelId = Integer.valueOf(item.get("channel_id").toString());
			identifierList.put(identifier, channelId);
		}

		return identifierList;
	}

	// 根据去重的gameid初始化表
	private void initTableByGameId() {
		String gameidSql = String.format("select distinct game_id from user_jieo;");

		List<Map<String, Object>> list = peakDataJdbc.queryForList(gameidSql);
		for (Map<String, Object> item : list) {
			String gameId = item.get("game_id").toString();
			copyTable(Integer.valueOf(gameId));
		}
	}

	private void copyTable(int gameId) {
		try {

			LOGGER.info("create user_{},waiting...", gameId);
			String sqlUser = String.format("CREATE TABLE IF NOT EXISTS user_%s LIKE _user_template ", gameId);
			peakDataJdbc.execute(sqlUser);

			LOGGER.info("insert user_{},waiting...", gameId);
			String sqlData = String.format("replace into user_%s select * from user_jieo where game_id=%s;", gameId, gameId);
			peakDataJdbc.execute(sqlData);

			String sqlOrder = String.format("CREATE TABLE IF NOT EXISTS order_%s LIKE _order_template ", gameId);
			peakDataJdbc.execute(sqlOrder);

		} catch (Exception e) {
			LOGGER.warn("e.getMessage()!");
		}
		LOGGER.info(gameId + " copy finish...");
	}
}
