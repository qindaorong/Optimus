package com.next.websocketcenter.api.facade;

import com.next.optimus.common.dataresponse.ServerHttpResponse;
import com.next.websocketcenter.api.dto.BroadCastMessageInfo;
import com.next.websocketcenter.api.dto.NotificationMessageInfo;
import com.next.websocketcenter.api.dto.UserMessageInfo;
import java.util.List;
import java.util.Map;

/**
 * WebsocketClientFacade
 *
 * @author qindaorong
 * @create 2017-11-14 2:15 PM
 **/
public interface WebsocketClientFacade {
    
    /**
     * 获得在线用户列表
     * @param headerMap
     * @return
     */
    ServerHttpResponse listUserName(Map<String,String> headerMap);
    
    
    /**
     * 向用户发送消息
     * @param userMessageInfo
     * @param headerMap
     * @return
     */
    ServerHttpResponse singleUserChat(UserMessageInfo userMessageInfo,Map<String,String> headerMap);
    
    
    /**
     * 向多个用户发送消息
     * @param userMessageInfoMap
     * @param headerMap
     * @return
     */
    ServerHttpResponse singleUserChatBatch(Map<String,List<UserMessageInfo>> userMessageInfoMap,Map<String,String> headerMap);
    
    /**
     * 消息通知
     * @param notificationMessageInfo
     * @param headerMap
     * @return
     */
    ServerHttpResponse notificationChat(NotificationMessageInfo notificationMessageInfo,Map<String,String> headerMap);
    
    
    /**
     * 向后台用户发送消息
     * @param broadCastMessageInfo
     * @param headerMap
     * @return
     */
    ServerHttpResponse broadCastUserChat(BroadCastMessageInfo broadCastMessageInfo,Map<String,String> headerMap);
}
