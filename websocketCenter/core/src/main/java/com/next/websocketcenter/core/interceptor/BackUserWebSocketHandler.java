package com.next.websocketcenter.core.interceptor;

import com.next.optimus.common.util.StringUtil;
import com.next.websocketcenter.core.controller.NextWebSocketController;
import com.next.websocketcenter.core.util.BroadCastSessionUtils;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class BackUserWebSocketHandler implements WebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(NextWebSocketController.class);
    
    private static String WELCOME_WORDS = "welcome to nextTruckingBack";
    private static String CHECK_WORDS_RESPONSE = "session alived";
    private static String UNAVAILABLE_SESSION = "unavailable session";
    
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        
        String userId = (String) session.getAttributes().get("userId");
        
        //帐号首次登录标志
        boolean onlineFlag = true;
        
        if(userId !=null){
            if(BroadCastSessionUtils.isOnline(userId)){
                onlineFlag = false;
            }
            BroadCastSessionUtils.addUser(userId,session);
        }

        if(onlineFlag){
            session.sendMessage(new TextMessage(WELCOME_WORDS));
        }

        logger.info("create connection ,user is [{}]", userId);
    }
    
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
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
        BroadCastSessionUtils.removeUserSession(userId,session);
    
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
     * 给后台所有用户发送消息
     *
     * @param message
     */
    public static void  sendMessageToAll(TextMessage message) throws IOException {
        try{
            ConcurrentHashMap<String,CopyOnWriteArrayList<WebSocketSession>> sessionMap =  BroadCastSessionUtils.getWebSocketSessionMap();
            
            for (Map.Entry<String,CopyOnWriteArrayList<WebSocketSession>> entry : sessionMap.entrySet()) {
                
                String backgroundUserId = entry.getKey();
                CopyOnWriteArrayList<WebSocketSession> webSocketSessionList = entry.getValue();
                for(WebSocketSession webSocketSession : webSocketSessionList){
                    try {
                        webSocketSession.sendMessage(message);
                    }catch (Exception e){
                        BroadCastSessionUtils.removeUserSession(backgroundUserId,webSocketSession);
                    }
                }
            }
            logger.info("send message to back user");
        }catch (NullPointerException e ){
            logger.info("send message to back user");
        }
    }
}
