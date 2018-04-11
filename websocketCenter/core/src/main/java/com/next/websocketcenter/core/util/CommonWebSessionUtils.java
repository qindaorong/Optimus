package com.next.websocketcenter.core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.web.socket.WebSocketSession;

/**
 * CommonWebSessionUtil
 *
 * @author qindaorong
 * @create 2017-12-22 9:51 AM
 **/
public class CommonWebSessionUtils {
    
    private static final Integer queueMaxSize = 10;
    
    /**
     * 获得在线用户列表
     * @return
     */
    public static List<String> listUserName(ConcurrentHashMap<String,CopyOnWriteArrayList<WebSocketSession>> sessionMap){
        Set<String> userIdSet= sessionMap.keySet();
        List<String> userIdList = new ArrayList<>();
        userIdList.addAll(userIdSet);
        return userIdList;
    }
    
    
    /**
     * 判断用户是否在线
     * @param userId
     * @return
     */
    public static boolean isOnline(ConcurrentHashMap<String,CopyOnWriteArrayList<WebSocketSession>> sessionMap,String userId){
        return sessionMap.containsKey(userId);
    }
    
    
    /**
     * 向websession集合中添加websession
     * @param sessionMap
     * @param session
     * @param userId
     */
    public static void addUser(ConcurrentHashMap<String,CopyOnWriteArrayList<WebSocketSession>> sessionMap,WebSocketSession session,String
        userId){
        if(sessionMap.containsKey(userId)){
            CopyOnWriteArrayList<WebSocketSession> sessionList =sessionMap.get(userId);
            if(sessionList.size() >= queueMaxSize){
                int number = sessionList.size()-queueMaxSize + 1;
                for(int i = 0 ;i< number ;i++ ){
                    sessionList.remove(0);
                }
                
            }
            sessionList.add(session);
            sessionMap.put(userId,sessionList);
        }else{
            CopyOnWriteArrayList<WebSocketSession> sessionList=new CopyOnWriteArrayList();
            sessionList.add(session);
            
            sessionMap.put(userId,sessionList);
        }
    }
    
    /**
     * 移除用户所有的websession
     * @param userId
     */
    public static void removeUser(ConcurrentHashMap<String,CopyOnWriteArrayList<WebSocketSession>> sessionMap,String userId){
        if(sessionMap.containsKey(userId)){
            sessionMap.remove(userId);
        }
    }
    
    
    /**
     * 移除用户所有的websession
     * @param userId        用户ID
     * @param session       移除的用户 session
     * @param sessionMap   websession保存集合
     */
    public static void removeUserSession(String userId,WebSocketSession session,ConcurrentHashMap<String,CopyOnWriteArrayList<WebSocketSession>>
        sessionMap){
        if(sessionMap.containsKey(userId)){
            CopyOnWriteArrayList<WebSocketSession> sessionList = sessionMap.get(userId);
            if(sessionList.contains(session)){
                sessionList.remove(session);
                sessionMap.put(userId,sessionList);
            }
            if(sessionList.isEmpty()){
                sessionMap.remove(userId);
            }
        }
    }
}
