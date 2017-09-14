package org.yujoo.common.mysql.migrate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.peak.core.spring.SpringContext;

/**
 * 1.所有流程一体化，数据复制到数据迁移
 * 2.共同的部分抽象
 * 3.编写脚本执行
 * @author eaves.zhu
 *
 */
public class CLILuancher {
	private static final Logger LOGGER = LoggerFactory.getLogger(CLILuancher.class);

	public static void main(String[] args) {		
		SpringContext.run();
		
		if (args == null || args.length < 1) {
			LOGGER.info("please configure command parameters!");
			LOGGER.info("eg. java -jar peak-cli.jar sdk_db_2_peak_data");
			return;
		}
		
		String actionName = "sdk_db_2_peak_data";
		if (args.length > 0) {
			actionName = args[0];
		}
		
		ExecuteCommand command =  ExecuteCommand.get(actionName);
		if (command == null) {
			System.out.println(String.format("`%s` command not found", actionName));
			return;
		}
		command.execute(args);
	}
}