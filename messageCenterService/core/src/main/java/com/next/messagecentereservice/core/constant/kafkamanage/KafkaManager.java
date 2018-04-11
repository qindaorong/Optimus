package com.next.messagecentereservice.core.constant.kafkamanage;

import com.next.optimus.common.util.StringUtil;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 * KafkaManager
 *
 * @author qindaorong
 * @date 2017/10/16
 */
public class KafkaManager {
    
    /**
     * 日志文件生成器
     */
    private static Logger log = Logger.getLogger(KafkaManager.class);
    
    /**
     * 消息配置文件
     */
    private static Properties messageProperties = null;
    
    /**  */
    private static KafkaManager propertiesManager = null;
    
    /**
     * topic配置文件
     */
    private static Map<String, String> topicMap = null;
    /**
     * topic配置文件
     */
    private static Properties topicProperties = null;
    
    
    static {
        // 加载属性文件
        try {
            // 消息配置文件
            InputStream inputStreamMsg = KafkaManager.class.getClassLoader()
                .getResourceAsStream(
                    KafkaConstant.PRO_FILE_MESSAGE_PROPERTIES);
            
            // 数据库连接配置文件
            InputStream inputStreamTopic = KafkaManager.class.getClassLoader()
                .getResourceAsStream(KafkaConstant.PRO_FILE_TOPIC_SETTING);
            
            try {
                //
                // 消息配置文件
                //
                messageProperties = new Properties();
                messageProperties.load(inputStreamMsg);
                // 记录系统设置文件加载成功
                log.info("Secure " + KafkaConstant.PRO_FILE_MESSAGE_PROPERTIES
                    + " is loaded.");
                
                //
                // 数据库连接配置文件
                //
                topicProperties = new Properties();
                topicProperties.load(inputStreamTopic);
                topicMap = new HashMap<String, String>((Map) topicProperties);
                // 记录数据库连接设置文件加载成功
                log.info("Secure " + KafkaConstant.PRO_FILE_TOPIC_SETTING
                    + " is loaded.");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                inputStreamMsg.close();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
    
    /**
     * 构造方法
     */
    public KafkaManager() {
    }
    
    /**
     * 初始化Manager
     */
    public static KafkaManager getMessageManager() {
        if (propertiesManager == null) {
            propertiesManager = new KafkaManager();
        }
        return propertiesManager;
        
    }
    
    /**
     * 根据KEY获得message
     */
    public String getMessage(String messageKey) {
        
        // 信息内容
        String message = "";
        
        if (StringUtil.isEmpty(messageKey)) {
            return "";
        }
        
        // 信息内容
        message = messageProperties.getProperty(messageKey);
        
        if (message == null) {
            // 将KEY设置成值
            message = messageKey;
        }
        
        // 返回信息内容
        return message;
    }
    
    
    public Map<String, String> getTopicProperties() {
        return topicMap;
    }
}
