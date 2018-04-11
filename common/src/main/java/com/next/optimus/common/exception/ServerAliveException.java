package com.next.optimus.common.exception;

import com.next.optimus.common.dataresponse.ServerHttpResponse;
import org.apache.http.HttpStatus;

/**
 * ServerAliveException
 * 调用异常
 * @author qindaorong
 * @date 2017/10/16
 */
public class ServerAliveException extends Exception{
    
    public static String ERROR_CODE="020001";
    
    public static String ALIVE_CODE_MSG="no server alive!";

    public ServerAliveException(){
        super(ALIVE_CODE_MSG);
    }
    
    /**
     * ServerAliveException触发时返回实体
     * @return
     */
    public static ServerHttpResponse getServerAliveExceptionResponse(){
        ServerHttpResponse serverHttpResponse = new ServerHttpResponse(HttpStatus.SC_NOT_FOUND);
        serverHttpResponse.setBizCode(ERROR_CODE);
        serverHttpResponse.setBizDescribe(ALIVE_CODE_MSG);
        return serverHttpResponse;
    }
    
    
}
