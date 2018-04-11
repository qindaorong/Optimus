package com.next.optimus.messagecenterservice.api;

import com.next.optimus.common.dataresponse.ServerHttpResponse;
import com.next.optimus.common.exception.NextHttpException;
import com.next.optimus.common.exception.ServerAliveException;
import com.next.optimus.common.util.httputil.BaseClient;
import com.next.optimus.messagecenterservice.api.content.MessageCenterURL;
import com.next.optimus.messagecenterservice.api.dto.KafkaDTO;
import com.next.optimus.messagecenterservice.api.facade.MessageCenterFacade;
import java.util.Map;

/**
 * MessageCenterLBClient 工程调用客户端
 *
 * @author qindaorong
 * @date 2017/10/16
 */
public class MessageCenterLBClient extends BaseClient implements MessageCenterFacade {
    
    
    public MessageCenterLBClient() {
        super(MessageCenterURL.SERVER_NAME);
    }
    
    @Override
    public ServerHttpResponse sendMessage( KafkaDTO kafkaDTO) {
        //封装参数名称
        String[] keyArray = new String[]{"topicId", "content"};
        //封装参数
        Map<String, Object> params = this.createParamMap(keyArray, kafkaDTO.getTopicId(), kafkaDTO.getContent());
        
        ServerHttpResponse<String> serverHttpResponse = null;

        try {
            serverHttpResponse = this.sendHttpRequest(MessageCenterURL.SEND_MESSAGE, params, BaseClient.HTTP_METHODS_POST);
        } catch (ServerAliveException e) {
            serverHttpResponse = ServerAliveException.getServerAliveExceptionResponse();
        } catch (NextHttpException e) {
            serverHttpResponse = NextHttpException.getNextHttpExceptionResponse();
        }

        return serverHttpResponse;
    }
}
