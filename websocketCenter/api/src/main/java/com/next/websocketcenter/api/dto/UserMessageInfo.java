package com.next.websocketcenter.api.dto;

/**
 * UserMessageInfo
 *
 * @author qindaorong
 * @create 2017-12-27 10:07 AM
 **/
public class UserMessageInfo extends WebSocketMessage {
    
    /**
     * 单发消息接收用户ID
     */
    private String userId;
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
}
