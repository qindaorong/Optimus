package com.next.websocketcenter.core.util;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;

/**
 * WebsocketSessionUtils
 *
 * @author qindaorong
 * @create 2017-11-21 2:11 PM
 **/
public class BroadCastSessionUtils extends CommonWebSessionUtils {

    private static final Logger log = LoggerFactory.getLogger(BroadCastSessionUtils.class);

    /**
     * WebSocket用户注册集合
     */
    private static ConcurrentHashMap<String,CopyOnWriteArrayList<WebSocketSession>> sessionMap = new ConcurrentHashMap();

    private static final Integer queueMaxSize = 10;

    /**
     * 获得在线用户集合
     * @return
     */
    public static List<String> listUserName(){
        log.info(" user method [listUserName] ....");
        return listUserName(sessionMap);
    }

    /**
     * 检查用户是否在线
     * @param userId
     * @return
     */
    public static boolean isOnline(String userId){
        log.info(" user method [isOnline] ....");
        return sessionMap.containsKey(userId);
    }

    /**
     * 添加在线用户
     * @param userId
     * @param session
     */
    public static void addUser(String userId,WebSocketSession session){
        log.info(" user method [addUser] ,user is [{}]",userId);

        addUser(sessionMap,session,userId);
    }

    /**
     * 移除登出用户
     * @param userId
     */
    public static void removeUser(String userId){
        log.info(" user method [removeUser] ,user is [{}]",userId);
        if(isOnline(userId)){
            sessionMap.remove(userId);
        }
    }


    /**
     * 移除登出用户Session
     * @param userId
     * @param session
     */
    public static void removeUserSession(String userId,WebSocketSession session){
        log.info(" user method [removeUserSession] ,user is [{}]",userId);
        removeUserSession(userId,session,sessionMap);
    }

    /**
     * 获得用户WebSocketSession集合
     * @return
     */
    public static CopyOnWriteArrayList<WebSocketSession> getUserWebSocketSession(String userId){
        log.info(" user method [getUserWebSocketSession] ");
        return sessionMap.get(userId);

    }


    /**
     * 获得用户后台用户WebSocketSession集合
     * @return
     */
    public static ConcurrentHashMap<String,CopyOnWriteArrayList<WebSocketSession>> getWebSocketSessionMap(){
        log.info(" user method [getWebSocketSessionMap] ");
        return sessionMap;

    }

}
