package com.next.optimus.common.common;

import com.next.optimus.common.util.StringUtil;
import java.io.InputStream;
import java.util.Properties;

/**
 * Message文件取得用工具类
 * 
 * @version V1.0
 * @author qindaorong
 * @date 2017/8/23
 */
public class MessageManager {
	
	public static int MAP_MIN_SIZE = 16;
	
	/**
	 * 读取配置文件
	 * @param propertiesName
	 * @return
	 */
	public static InputStream loadProperties(String propertiesName){
		InputStream inputStreamServer = null;
		// 加载属性文件
		try {
			inputStreamServer = MessageManager.class.getClassLoader().getResourceAsStream(propertiesName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return inputStreamServer;
	}
	
	
	/**
	 * 根据KEY获得读取的配置文件的value
	 * @param key
	 * @param properties
	 * @return
	 */
	protected static String getMessage(String key,Properties properties){
		String message = "";
		
		if (StringUtil.isEmpty(key)) {
			return "";
		}

		if(properties != null ){
			// 信息内容
			message = properties.getProperty(key);
			
			if (message == null) {
				// 将KEY设置成值
				message = key;
			}
		}
		return message;
	}
	
}
