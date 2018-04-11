package com.next.optimus.common.util;

/**
 * StringUtil
 *
 * @author qindaorong
 * @date 2017/10/16
 */
public class StringUtil {
	/**
	 * 判定对象是否为空
	 * 
	 * @param object1
	 *            对象1
	 * @param object2
	 *            对象2
	 * @return boolean
	 */
	public static boolean equals(String object1, String object2) {
		if (object1 == null) {
			return false;
		}

		return object1.equals(object2);
	}
	
	/**
	 * 判定对象是否为空
	 *
	 * @param object
	 * @return true...null或者空 / false...not null或者非空
	 */
	public static boolean isEmpty(String object) {
		if (object == null) {
			return true;
		}
		
		if (object instanceof String) {
			if ("".equals(((String) object))) {
				return true;
			}
		}
		return false;
	}
}
