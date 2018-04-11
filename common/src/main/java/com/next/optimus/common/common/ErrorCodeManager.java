package com.next.optimus.common.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * ErrorCodeManager
 *
 * @author qindaorong
 * @date 2017/10/16
 */
public class ErrorCodeManager{

	/** 消息配置文件 */
	private static Properties errorCodeProperties = null;
	/**  */
	private static ErrorCodeManager propertiesManager = null;

	/**
	 * 构造方法
	 */
	private ErrorCodeManager() {
	}

	/**
	 * 初始化Manager
	 */
	public static ErrorCodeManager getErrorCodeManager() {
		if (propertiesManager == null) {
			propertiesManager = new ErrorCodeManager();
			
			try {
				InputStream inputErrorCode = MessageManager.loadProperties(CommonConstants.SERVER_ERROR_CODE_PROPERTIES);
				errorCodeProperties = new Properties();
				errorCodeProperties.load(inputErrorCode);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return propertiesManager;

	}
	
	/**
	 * 根据Key获得数据error对应value
	 * @param code
	 * @return
	 */
	public String getPropertiesValue(String code ) {
		return MessageManager.getMessage(code,errorCodeProperties);
	}
}
