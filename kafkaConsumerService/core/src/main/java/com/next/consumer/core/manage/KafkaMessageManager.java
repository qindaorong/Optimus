package com.next.consumer.core.manage;

import com.next.consumer.core.constants.FrameworkConstants;
import com.next.optimus.common.common.MessageManager;

import com.next.optimus.common.util.StringUtil;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;


/**
 * KafkaMessageManager
 *
 * @author qindaorong
 * @date 2017/10/16
 */
public class KafkaMessageManager extends MessageManager {

    public static KafkaMessageManager propertiesManager = null;
    
    /** messageConsumer_sinkerSafety 对应的sink安全启动 */
    public static String SINK_SAFETY = "messageConsumer_sinkerSafety";
    
    /** topic配置文件 */
    private static Map<String, String> topicMap = null;
    /** topic配置文件 */
    private static Properties topicProperties = null;
    /** 系统配置文件 */
    private static Properties messageInfo = null;
    
    /**
     * 初始化Manager
     */
    public static KafkaMessageManager getMessageManager() {
        if (propertiesManager == null) {
            propertiesManager = new KafkaMessageManager();
            
            InputStream messageStreamServer = loadProperties(FrameworkConstants.PRO_FILE_MESSAGE_PROPERTIES);
    
            InputStream topicStreamServer = loadProperties(FrameworkConstants.PRO_FILE_TOPIC_SETTING);
            
            try {
                messageInfo = new Properties();
                messageInfo.load(messageStreamServer);
    
                topicProperties = new Properties();
                topicProperties.load(topicStreamServer);
                topicMap = new HashMap<String, String>((Map)topicProperties);
                
                
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                
                try {
                    messageStreamServer.close();
                    topicStreamServer.close();
                    
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        return propertiesManager;
    }

    /**
     * 根据KEY获得系统配置文件message
     * @param messageKey
     * @return
     */
    public String getMessageValue(String messageKey) {
        return  getMessage(messageKey,messageInfo);
    }
    
    
    /**
     * 获得topicMap
     * @return
     */
    public String getTopicValue(String topicKey) {
    
        String topicValue = "";
        
        if (StringUtil.isEmpty(topicKey)) {
            return topicValue;
        }
    
        if(topicMap != null ){
            if (topicMap.containsKey(topicKey)) {
                // 将KEY设置成值
                topicValue = topicMap.get(topicKey);
            }
        }
        return topicValue;
    }
    
    
    
    /**
     * 获得topicMap
     * @return
     */
    public Map<String,String> getKeyByValue(String value) {
    
        Map<String,String> map = new HashMap(MessageManager.MAP_MIN_SIZE);

        if (StringUtil.isEmpty(value)) {
            return map;
        }
        
        if(value != null ){
            if (messageInfo.containsValue(value)) {
                Iterator it = messageInfo.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry entry = (Entry) it.next();
                    Object obj = entry.getValue();
                    if (obj != null && obj.equals(value)) {
                        String mapKey = (String) entry.getKey();
                        map.put(mapKey, value);
                    }
                }
            }
        }
        return map;
    }
}
