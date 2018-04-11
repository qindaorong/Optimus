package com.next.websocketcenter.api.util;

import com.next.optimus.common.common.MessageManager;
import com.next.websocketcenter.api.constants.FrameworkConstants;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * WebSocketManager
 *
 * @author qindaorong
 * @create 2017-11-14 2:04 PM
 **/
public class WebSocketManager extends MessageManager {
    
    public static WebSocketManager propertiesManager = null;
    
    /** 系统配置文件 */
    private static Properties messageInfo = null;
    
    /**
     * 初始化Manager
     */
    public static WebSocketManager getWebSocketManager() {
        if (propertiesManager == null) {
            propertiesManager = new WebSocketManager();
            
            InputStream messageStreamServer = loadProperties(FrameworkConstants.WEBSOCKET_PROPERTIES);
            
            try {
                messageInfo = new Properties();
                messageInfo.load(messageStreamServer);
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    messageStreamServer.close();
                    
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

}
