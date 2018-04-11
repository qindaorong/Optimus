package com.next.kafkaconsumer.core.elasticsearch.util;

import com.google.common.collect.Lists;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author : Donald
 * @description :
 * @date : 2017/9/25 9:59.
 */
public class ResourceUtil {
    
    /**
     * 获取指定配置文件中所以的数据
     * @param propertyName
     *        调用方式：
     *            1.配置文件放在resource源包下，不用加后缀
     *              PropertiesUtil.getAllMessage("message");
     *            2.放在包里面的
     *              PropertiesUtil.getAllMessage("com.test.message");
     * @return
     */
    public static List<String> getAllMessage(String propertyName) {
        // 获得资源包
        ResourceBundle rb = ResourceBundle.getBundle(propertyName.trim());
        // 通过资源包拿到所有的key
        Enumeration<String> allKey = rb.getKeys();
        // 遍历key 得到 value
        List<String> valList = Lists.newArrayList();
        while (allKey.hasMoreElements()) {
            String key = allKey.nextElement();
            String value = (String) rb.getString(key);
            valList.add(value);
        }
        return valList;
    }
}
