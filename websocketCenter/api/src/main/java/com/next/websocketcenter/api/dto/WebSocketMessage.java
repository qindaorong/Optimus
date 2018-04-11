package com.next.websocketcenter.api.dto;

import java.util.Map;

/**
 * WebSocketMessage
 *
 * @author qindaorong
 * @create 2017-11-14 11:54 AM
 **/
public class WebSocketMessage {
    
    /**
     * 消息事件
     */
    private String event;
    
    /**
     * 消息实体
     */
    private Map<String,Object> extensions;
    
    public String getEvent() {
        return event;
    }
    
    public void setEvent(String event) {
        this.event = event;
    }
    
    public Map<String, Object> getExtensions() {
        return extensions;
    }
    
    public void setExtensions(Map<String, Object> extensions) {
        this.extensions = extensions;
    }
}
