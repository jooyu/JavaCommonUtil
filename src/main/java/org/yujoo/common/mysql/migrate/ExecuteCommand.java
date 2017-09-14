package org.yujoo.common.mysql.migrate;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public abstract class ExecuteCommand {

	static Map<String, ExecuteCommand> commands = new HashMap<>();

	public abstract String name();

	public abstract void execute(String[] args);

	@PostConstruct
	public void init() {
		commands.put(name(), this);
	}

	public static ExecuteCommand get(String name) {
		return commands.get(name);
	} 
	
	protected JdbcTemplate getJdbc(String ip, String userName, String password, String dbName) {
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName("com.mysql.jdbc.Driver");
		ds.setInitialSize(20);
		ds.setMaxActive(20);
		ds.setMaxIdle(10);
		ds.setMinIdle(10);
		ds.setTimeBetweenEvictionRunsMillis(90000);
		ds.setTestOnBorrow(false);
		ds.setTestWhileIdle(true);
		ds.setUsername(userName);
		ds.setPassword(password);
		String url = "jdbc:mysql://%s/%s?useUnicode=true&amp;characterEncoding=utf-8&amp;autoReconnect=true;useCursorFetch=true";
		ds.setUrl(String.format(url, ip, dbName));

		return new JdbcTemplate(ds);
	}
}
