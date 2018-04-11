package com.next.websocketcenter.core.controller;

import com.next.optimus.common.dataresponse.ServerHttpResponse;
import com.next.websocketcenter.api.WebsocketCenterEnum;
import com.next.websocketcenter.api.dto.BroadCastMessageInfo;
import com.next.websocketcenter.api.dto.NotificationMessageInfo;
import com.next.websocketcenter.api.dto.UserMessageInfo;
import com.next.websocketcenter.api.dto.WebSocketMessage;
import com.next.websocketcenter.core.interceptor.BackUserWebSocketHandler;
import com.next.websocketcenter.core.interceptor.SystemWebSocketHandler;
import com.next.websocketcenter.core.util.WebsocketSessionUtils;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONObject;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.TextMessage;

/**
 * NextWebSocketController
 *
 * @author qindaorong
 * @create 2017-11-14 11:51 AM
 **/
@RestController
public class NextWebSocketController {
    
    private static final Logger logger = LoggerFactory.getLogger(NextWebSocketController.class);
    
    /**
     * 用户单发通道
     * @param userMessageInfo
     * @return
     */
    @RequestMapping(value = "/singleUserChat", method = RequestMethod.POST)
    public ServerHttpResponse singleUserChat(@RequestBody UserMessageInfo userMessageInfo){

        ServerHttpResponse serverHttpResponse;

        String userId = userMessageInfo.getUserId();
        logger.info("start to singleUserChat ,message getter name userId is [{}].....",userId);
        
        try {
            WebSocketMessage webSocketMessage = new WebSocketMessage();
            webSocketMessage.setEvent(userMessageInfo.getEvent());
            webSocketMessage.setExtensions(userMessageInfo.getExtensions());
            
            String msg = JSONObject.fromObject(webSocketMessage).toString();

            SystemWebSocketHandler.sendMessageToSingleUser(userId,new TextMessage(msg));
            serverHttpResponse = new ServerHttpResponse(HttpStatus.SC_OK, WebsocketCenterEnum.WEBSOCKET_CENTER_SEND_SUC.getCode());
        } catch (Exception e) {
            e.printStackTrace();
            serverHttpResponse = new ServerHttpResponse(HttpStatus.SC_INTERNAL_SERVER_ERROR, WebsocketCenterEnum.WEBSOCKET_CENTER_SEND_FAIL.getCode());
        }
        logger.info("singleUserChat is over and success !");
        return serverHttpResponse;
    }
    
    /**
     * 消息通知
     * @param notificationMessageInfo
     * @return
     */
    @RequestMapping(value = "/notificationChat", method = RequestMethod.POST)
    public ServerHttpResponse notificationChat(@RequestBody NotificationMessageInfo notificationMessageInfo){
        
        ServerHttpResponse serverHttpResponse;
        logger.info("start to singleUserChat ,message getter name userId is [{}].....");
        
        try {
            WebSocketMessage webSocketMessage = new WebSocketMessage();
            webSocketMessage.setEvent(notificationMessageInfo.getEvent());
            webSocketMessage.setExtensions(notificationMessageInfo.getExtensions());
            
            List<Long> userIdList = notificationMessageInfo.getUsers();
            
            String msg = JSONObject.fromObject(webSocketMessage).toString();
            
            SystemWebSocketHandler.sendMessageToMultipleUser(userIdList,new TextMessage(msg));
            serverHttpResponse = new ServerHttpResponse(HttpStatus.SC_OK, WebsocketCenterEnum.WEBSOCKET_CENTER_SEND_SUC.getCode());
        } catch (Exception e) {
            e.printStackTrace();
            serverHttpResponse = new ServerHttpResponse(HttpStatus.SC_INTERNAL_SERVER_ERROR, WebsocketCenterEnum.WEBSOCKET_CENTER_SEND_FAIL.getCode());
        }
        logger.info("singleUserChat is over and success !");
        return serverHttpResponse;
    }
    
    
    /**
     * 用户群发通道
     * @param userMessageInfoMap
     * @return
     */
    @RequestMapping(value = "/singleUserChatBatch", method = RequestMethod.POST)
    public ServerHttpResponse singleUserChatBatch(@RequestBody Map<String,List<UserMessageInfo>> userMessageInfoMap){
        
        ServerHttpResponse serverHttpResponse;
        logger.info("start to send singleUserChatBatch");
        
        try {
            List<UserMessageInfo> userMessageInfoList = userMessageInfoMap.get("list");
            
            SystemWebSocketHandler.sendMessageToMultipleUser(userMessageInfoList);
            
            serverHttpResponse = new ServerHttpResponse(HttpStatus.SC_OK, WebsocketCenterEnum.WEBSOCKET_CENTER_SEND_SUC.getCode());
        } catch (IOException e) {
            e.printStackTrace();
            serverHttpResponse = new ServerHttpResponse(HttpStatus.SC_INTERNAL_SERVER_ERROR, WebsocketCenterEnum.WEBSOCKET_CENTER_SEND_FAIL.getCode());
        }
        logger.info("singleUserChatBatch is over and success !");
        return serverHttpResponse;
    }
    
    /**
     * 后台广播通道
     * @param broadCastMessageInfo
     * @return
     */
    @RequestMapping(value = "/broadCastUserChat", method = RequestMethod.POST)
    public ServerHttpResponse broadCastUserChat(@RequestBody BroadCastMessageInfo broadCastMessageInfo){
        
        ServerHttpResponse serverHttpResponse;
        
        logger.info("start to send chatBroadCastUserMessage ");
        try {
            WebSocketMessage webSocketMessage = new WebSocketMessage();
            webSocketMessage.setEvent(broadCastMessageInfo.getEvent());
            webSocketMessage.setExtensions(broadCastMessageInfo.getExtensions());
            
            String msg = JSONObject.fromObject(webSocketMessage).toString();

            BackUserWebSocketHandler.sendMessageToAll(new TextMessage(msg));
            serverHttpResponse = new ServerHttpResponse(HttpStatus.SC_OK, WebsocketCenterEnum.WEBSOCKET_CENTER_SEND_SUC.getCode());
        } catch (IOException e) {
            e.printStackTrace();
            serverHttpResponse = new ServerHttpResponse(HttpStatus.SC_INTERNAL_SERVER_ERROR, WebsocketCenterEnum.WEBSOCKET_CENTER_SEND_FAIL.getCode());
        }
        logger.info("chatBroadCastUserMessage is over and success !");
        return serverHttpResponse;
    }
    
    
    @RequestMapping(value = "/userList", method = RequestMethod.POST)
    public ServerHttpResponse userList() {

        ServerHttpResponse serverHttpResponse;
        List<String> userNameList =  WebsocketSessionUtils.listUserName();
        serverHttpResponse = new ServerHttpResponse(HttpStatus.SC_OK, WebsocketCenterEnum.WEBSOCKET_CENTER_SEND_SUC.getCode());
        serverHttpResponse.setMapAttribute("userNameList",userNameList);
        return serverHttpResponse;
    }
}
