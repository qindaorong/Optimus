package com.next.websocketcenter.api;

import com.next.optimus.common.dataresponse.ServerHttpResponse;
import com.next.optimus.common.exception.NextHttpException;
import com.next.optimus.common.exception.ServerAliveException;
import com.next.optimus.common.util.httputil.BaseClient;
import com.next.websocketcenter.api.constants.FrameworkConstants;
import com.next.websocketcenter.api.dto.BroadCastMessageInfo;
import com.next.websocketcenter.api.dto.NotificationMessageInfo;
import com.next.websocketcenter.api.dto.UserMessageInfo;
import com.next.websocketcenter.api.facade.WebsocketClientFacade;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * WebSocketClient
 *
 * @author qindaorong
 * @create 2017-11-14 2:14 PM
 **/
public class WebSocketClient extends BaseClient implements WebsocketClientFacade {

    public WebSocketClient() {
        super(FrameworkConstants.SERVER_NAME);
    }
    
    @Override
    public ServerHttpResponse listUserName(Map<String,String> headerMap) {
        //封装参数名称
        String[] keyArray = new String[]{};
        //封装参数
        Map<String, Object> params = this.createParamMap(keyArray);
        ServerHttpResponse serverHttpResponse = null;
        try {
            serverHttpResponse = this.sendHttpRequest(FrameworkConstants.USER_LIST, params,headerMap,BaseClient.HTTP_METHODS_POST);
        } catch (ServerAliveException e) {
            serverHttpResponse = ServerAliveException.getServerAliveExceptionResponse();
        } catch (NextHttpException e) {
            serverHttpResponse = NextHttpException.getNextHttpExceptionResponse();
        }
        return serverHttpResponse;
    }
    
    @Override
    public ServerHttpResponse singleUserChat(UserMessageInfo userMessageInfo,Map<String,String> headerMap) {
        //封装参数
        Map<String, Object> params = this.transBean2Map(userMessageInfo);
        
        ServerHttpResponse serverHttpResponse = null;
        try {
            serverHttpResponse = this.sendHttpRequest(FrameworkConstants.CHAT_SINGLEUSER, params,headerMap,BaseClient.HTTP_METHODS_POST);
        } catch (ServerAliveException e) {
            serverHttpResponse = ServerAliveException.getServerAliveExceptionResponse();
        } catch (NextHttpException e) {
            serverHttpResponse = NextHttpException.getNextHttpExceptionResponse();
        }
        return serverHttpResponse;
    }

    @Override
    public ServerHttpResponse singleUserChatBatch(Map<String,List<UserMessageInfo>> userMessageInfoMap,Map<String,String> headerMap) {
    
        //封装参数
        Map<String, Object> params = new HashMap<>(userMessageInfoMap.size());
        params.putAll(userMessageInfoMap);

        ServerHttpResponse serverHttpResponse = null;
        try {
            serverHttpResponse = this.sendHttpRequest(FrameworkConstants.CHAT_SINGLEUSERCHATBATCH, params,headerMap,BaseClient.HTTP_METHODS_POST);
        } catch (ServerAliveException e) {
            serverHttpResponse = ServerAliveException.getServerAliveExceptionResponse();
        } catch (NextHttpException e) {
            serverHttpResponse = NextHttpException.getNextHttpExceptionResponse();
        }
        return serverHttpResponse;
    }
    
    @Override
    public ServerHttpResponse notificationChat(NotificationMessageInfo notificationMessageInfo,Map<String,String> headerMap) {
        //封装参数
        Map<String, Object> params = this.transBean2Map(notificationMessageInfo);
        
        ServerHttpResponse serverHttpResponse = null;
        try {
            serverHttpResponse = this.sendHttpRequest(FrameworkConstants.CHAT_NOTIFICATIONCHAT, params,headerMap,BaseClient.HTTP_METHODS_POST);
        } catch (ServerAliveException e) {
            serverHttpResponse = ServerAliveException.getServerAliveExceptionResponse();
        } catch (NextHttpException e) {
            serverHttpResponse = NextHttpException.getNextHttpExceptionResponse();
        }
        return serverHttpResponse;
    }
    
    @Override
    public ServerHttpResponse broadCastUserChat(BroadCastMessageInfo broadCastMessageInfo,Map<String,String> headerMap) {
        //封装参数
        Map<String, Object> params = this.transBean2Map(broadCastMessageInfo);
        
        ServerHttpResponse serverHttpResponse = null;
        try {
            serverHttpResponse = this.sendHttpRequest(FrameworkConstants.CHAT_BROADCASTUSERCHAT, params,headerMap,BaseClient.HTTP_METHODS_POST);
        } catch (ServerAliveException e) {
            serverHttpResponse = ServerAliveException.getServerAliveExceptionResponse();
        } catch (NextHttpException e) {
            serverHttpResponse = NextHttpException.getNextHttpExceptionResponse();
        }
        return serverHttpResponse;
    }
    
    
    
    /**
     * bean 转map
     * @param obj
     * @return
     */
    private  Map<String, Object> transBean2Map(Object obj) {
        if(obj == null){
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                // 过滤class属性
                if (!key.equals("class")) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);
                    
                    map.put(key, value);
                }
                
            }
        } catch (Exception e) {
            System.out.println("transBean2Map Error " + e);
        }
        return map;
    }
}
