package com.next.optimus.common.exception;

import com.next.optimus.common.dataresponse.ServerHttpResponse;
import java.io.IOException;
import org.apache.http.HttpStatus;

/**
 * NextHttpException
 * HttpParameterException调用异常
 * @author qindaorong
 * @date 2017/10/16
 */
public class NextHttpException extends IOException {
    
    public static String ERROR_CODE="020002";
    
    public static String CODE_MSG="Next Http Exception!";

    public NextHttpException(){
        super(CODE_MSG);
    }

    /**
     * NextHttpException触发时返回实体
     * @return
     */
    public static ServerHttpResponse getNextHttpExceptionResponse(){
        ServerHttpResponse serverHttpResponse = new ServerHttpResponse(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        serverHttpResponse.setBizCode(ERROR_CODE);
        serverHttpResponse.setBizDescribe(CODE_MSG);
        return serverHttpResponse;
    }
}