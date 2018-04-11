package com.next.optimus.common.common;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


/**
 * GateWayFactoryManager
 *
 * @author qindaorong
 * @date 2017/10/16
 */
public class GateWayFactoryManager {

	/** 消息配置文件 */
	private static Properties serverProperties = null;

	/**  */
	private static GateWayFactoryManager propertiesManager = null;
	
	/** 服务配置文件 */
	private static Map<String, String> serverMap = null;

	 static {
		// 加载属性文件
		try {
			// 消息配置文件
			InputStream inputStreamServer = MessageManager.class.getClassLoader()
					.getResourceAsStream(
						CommonConstants.PRO_FILE_SERVER_PROPERTIES);
			try {

				//
				// 服务配置文件
				//
				serverProperties = new Properties();
				serverProperties.load(inputStreamServer);
				serverMap = new HashMap<String, String>((Map) serverProperties);
				// 记录数据库连接设置文件加载成功
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				inputStreamServer.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 构造方法
	 */
	private GateWayFactoryManager() {
	}

	/**
	 * 初始化Manager
	 */
	public static GateWayFactoryManager getMessageManager() {
		GateWayFactoryManager manager = null;
		if (propertiesManager == null) {
			manager = new GateWayFactoryManager();
		} else {
			manager = propertiesManager;
		}

		return manager;

	}

	public Map<String, String> getPropertiesMap() {
		return serverMap;
	}
}
