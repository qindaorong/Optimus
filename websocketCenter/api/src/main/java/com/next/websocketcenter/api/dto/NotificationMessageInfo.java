package com.next.websocketcenter.api.dto;

import java.util.List;

/**
 * NotificationMessageInfo
 *
 * @author qindaorong
 * @create 2017-12-27 10:07 AM
 **/
public class NotificationMessageInfo extends WebSocketMessage {
    
    /**
     * 单发消息接收用户ID
     */
    private List<Long> users;
    
    public List<Long> getUsers() {
        return users;
    }
    
    public void setUsers(List<Long> users) {
        this.users = users;
    }
}
