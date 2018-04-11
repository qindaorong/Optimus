package com.next.websocketcenter.api;

import com.next.optimus.common.dataresponse.ServerHttpResponse;
import org.apache.http.HttpStatus;

/**
 * WebsocketCenterEnum
 *
 * @author qindaorong
 * @date 2017/11/16
 */
public enum WebsocketCenterEnum {
    
    /**
     *  WEBSOCKET_CENTER消息发送成功
     */
    WEBSOCKET_CENTER_SEND_SUC("010001"),
    /**
     *  WEBSOCKET_CENTER消息发送失败
     */
    WEBSOCKET_CENTER_SEND_FAIL("010002");
    
    private String code;
    
    WebsocketCenterEnum(String code) {
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
