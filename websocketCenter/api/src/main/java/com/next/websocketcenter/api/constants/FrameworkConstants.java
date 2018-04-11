package com.next.websocketcenter.api.constants;

/**
 * FrameworkConstants
 *
 * @author qindaorong
 * @date 2017/10/16
 */
public class FrameworkConstants {
    
    /**
     * 服务名称
     */
    public final static String SERVER_NAME = "websocketCenter";
    
    
    /**
     * 获得用户列表
     */
    public final static String USER_LIST = "/userList";
    
    
    /**
     * 向用户发送消息
     */
    public final static String CHAT_SINGLEUSER= "/singleUserChat";
    
    /**
     * 消息通知
     */
    public final static String CHAT_NOTIFICATIONCHAT= "/notificationChat";
    
    /**
     * 批量消息通知
     */
    public final static String CHAT_SINGLEUSERCHATBATCH= "/singleUserChatBatch";
    
    /**
     * 向后台广播
     */
    public final static String CHAT_BROADCASTUSERCHAT= "/broadCastUserChat";
    
    //--------------------------------------------------------------------
    
    /**
     * message
     */
    public static final String WEBSOCKET_PROPERTIES = "websocket.properties";
    
    /**
     * 验证用户名称
     */
    public static String ACCESS_KEY_ID ="access_key_id";
    
    /**
     * 验证用户密码
     */
    public static String SECRET_ACCESS_KEY ="secret_access_key";



    
}
