package com.next.optimus.common.messagebean;

import java.io.Serializable;

/**
 * Message
 * 浏览器向服务器发送的消息使用此类接受
 * @author qindaorong
 * @date 2017/10/16
 */
public class Message implements Serializable {
    
    
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * 消息类型 010:推送通知 020：车队job分发 030：S修改 ，031：C修改，032：ANTO修改 040:司机GPS上报
     */
    private String messageType;
    
    /**
     * 消息传递实体
     */
    private String content;
    
    /**
     * 测试number
     */
    private String messageNumber;
    
    
    public String getMessageType() {
        return messageType;
    }
    
    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public String getMessageNumber() {
        return messageNumber;
    }
    
    public void setMessageNumber(String messageNumber) {
        this.messageNumber = messageNumber;
    }
    
    @Override
    public String toString(){
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        sb.append("\"messageType\": \""+messageType +"\",");
        sb.append("\"messageNumber\": \""+messageNumber +"\",");
        sb.append("\"content\": "+content);
        sb.append("}");
        return sb.toString();
    }
    
}