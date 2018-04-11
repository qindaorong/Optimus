package com.next.optimus.messagecenterservice.api;

import com.next.optimus.common.dataresponse.ServerHttpResponse;
import org.apache.http.HttpStatus;

/**
 * MessageCenterEnum
 *
 * @author qindaorong
 * @date 2017/10/16
 */
public enum MessageCenterEnum {
    
    /**
     * messageCenter消息发送成功
     */
    MESSSAGE_CENTER_SEND_SUC("010001"),
    /**
     * messageCenter消息发送失败
     */
    MESSSAGE_CENTER_SEND_FAIL("010002");
    
    private String code;
    
    MessageCenterEnum(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return code;
    }
    
    
    /**
     * ServerAliveException触发时返回实体
     * @return
     */
    public static ServerHttpResponse getHystricResponse(){
        ServerHttpResponse serverHttpResponse = new ServerHttpResponse(HttpStatus.SC_NOT_FOUND);
        serverHttpResponse.setBizCode(HYSTRIC_ERROR_CODE);
        serverHttpResponse.setBizDescribe(HYSTRIC_ERROR_MSG);
        return serverHttpResponse;
    }
    
    
    /**
     * HYSTRIC错误代码
     */
    public static String HYSTRIC_ERROR_CODE = "090001";
    
    /**
     * HYSTRIC错误信息提示
     */
    public static String HYSTRIC_ERROR_MSG = "request has into hystric, server is down!";
}
