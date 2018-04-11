package com.next.optimus.messagecenterservice.api.content;

/**
 * MessageCenterURL
 *
 * @author qindaorong
 * @date 2017/10/16
 */
public class MessageCenterURL {
    
    /**
     * 服务名称
     */
    public final static String SERVER_NAME = "messageCenter";
    
    /**
     * 向MQ中添加数据
     */
    public final static String SEND_MESSAGE = "/sendMessage";
    
    /**
     * 从服务中获得名称
     */
    public final static String GET_NAME = "/getName";
}