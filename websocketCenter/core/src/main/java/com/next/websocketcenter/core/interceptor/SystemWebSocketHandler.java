package com.next.websocketcenter.core.interceptor;

import com.next.optimus.common.util.StringUtil;
import com.next.websocketcenter.api.dto.UserMessageInfo;
import com.next.websocketcenter.core.controller.NextWebSocketController;
import com.next.websocketcenter.core.util.BroadCastSessionUtils;
import com.next.websocketcenter.core.util.WebsocketSessionUtils;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * SystemWebSocketHandler
 *
 * @author qindaorong
 * @create 2017-11-15 5:34 PM
 **/
public class SystemWebSocketHandler implements WebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(NextWebSocketController.class);
    
    private static String WELCOME_WORDS = "welcome to nextTrucking";
    private static String CHECK_WORDS_RESPONSE = "session alived";
    private static String UNAVAILABLE_SESSION = "unavailable session";
    
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        
        String userId = (String) session.getAttributes().get("userId");
        
        //帐号首次登录标志
        boolean onlineFlag = true;
        
        if(userId !=null){
            if(WebsocketSessionUtils.isOnline(userId)){
                onlineFlag = false;
            }
            WebsocketSessionUtils.addUser(userId,session);
        }

        if(onlineFlag){
            session.sendMessage(new TextMessage(WELCOME_WORDS));
        }

        logger.info("create connection ,user is [{}]", userId);
    }
    
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        session.sendMessage(new TextMessage(CHECK_WORDS_RESPONSE));
    
        String userId = (String) session.getAttributes().get("userId");
    
        if(!StringUtil.isEmpty(userId)){
            session.sendMessage(new TextMessage(CHECK_WORDS_RESPONSE));
        }else{
            session.sendMessage(new TextMessage(UNAVAILABLE_SESSION));
            session.close();
        }
        
        
    }
    
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        
        String userId = (String) session.getAttributes().get("userId");
        WebsocketSessionUtils.removeUserSession(userId,session);

        logger.info("transport error connection closed ,user is [{}]", userId);
    }
    
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        String userId = (String) session.getAttributes().get("userId");
        logger.info("connection is shut down ,user is [{}]", userId);
    
        //WebsocketSessionUtils.removeUser(userId);
    }
    
    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
    
    /**
     * 给某个用户发送消息
     *
     * @param userId
     * @param message
     */
    public static void  sendMessageToSingleUser(String userId, TextMessage message) throws IOException {
        try{
            List<WebSocketSession> sessionList= WebsocketSessionUtils.getUserWebSocketSession(userId);

            for(WebSocketSession userSession: sessionList){
                try {
                    userSession.sendMessage(message);
                }catch (Exception e){
                    WebsocketSessionUtils.removeUserSession(userId,userSession);
                }
            }
            
            logger.info("send message to user ,get message user is [{}]", userId);
        }catch (NullPointerException e ){
            System.out.println("用户"+userId +"不在线！");
        }

    }
    
    
    /**
     * 给指定群发消息用户
     *
     * @param userIdList
     * @param msg
     */
    public static void  sendMessageToMultipleUser(List<Long> userIdList,TextMessage msg)
        throws IOException {
        
        try{
            for(Long userId :userIdList){
                String userIdStr = String.valueOf(userId);
                boolean onlineFlag = WebsocketSessionUtils.isOnline(userIdStr);
        
                if(onlineFlag){
                    CopyOnWriteArrayList<WebSocketSession> webSocketSessionList  = WebsocketSessionUtils.getUserWebSocketSession(userIdStr);
                    for(WebSocketSession webSocketSession : webSocketSessionList){
                        try {
                            webSocketSession.sendMessage(msg);
                        }catch (Exception e){
                            WebsocketSessionUtils.removeUserSession(userIdStr,webSocketSession);
                        }
                    }
                }
            }
            logger.info("send message to back user");
        }catch (NullPointerException e ){
            logger.info("send message to back user");
        }
    }
    
    /**
     * 给指定群发消息用户
     *
     * @param userMessageInfoList
     */
    public static void  sendMessageToMultipleUser(List<UserMessageInfo> userMessageInfoList)
        throws IOException {
        try{
            for(UserMessageInfo userMessageInfo :userMessageInfoList){
                
                String userId = userMessageInfo.getUserId();
                boolean onlineFlag = WebsocketSessionUtils.isOnline(userId);
                
                if(onlineFlag){
                    CopyOnWriteArrayList<WebSocketSession> webSocketSessionList  = WebsocketSessionUtils.getUserWebSocketSession(userId);

                    com.next.websocketcenter.api.dto.WebSocketMessage webSocketMessage = new com.next.websocketcenter.api.dto.WebSocketMessage();
                    webSocketMessage.setEvent(userMessageInfo.getEvent());
                    webSocketMessage.setExtensions(userMessageInfo.getExtensions());
                    String msg = JSONObject.fromObject(webSocketMessage).toString();

                    for(WebSocketSession webSocketSession : webSocketSessionList){
                        try {
                            webSocketSession.sendMessage(new TextMessage(msg));
                        }catch (Exception e){
                            WebsocketSessionUtils.removeUserSession(userId,webSocketSession);
                        }
                    }
                }
            }
            logger.info("send message to back user");
        }catch (NullPointerException e ){
            logger.info("send message to back user");
        }
    }
    
}