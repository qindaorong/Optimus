package com.next.optimus.messagecenterservice.api.hystric;

import com.next.optimus.common.dataresponse.ServerHttpResponse;
import com.next.optimus.messagecenterservice.api.MessageCenterCloudClient;
import com.next.optimus.messagecenterservice.api.MessageCenterEnum;
import com.next.optimus.messagecenterservice.api.dto.KafkaDTO;
import org.springframework.stereotype.Component;

/**
 * MessageCenterHystric
 *
 * @author qindaorong
 * @date 2017/10/16
 */
@Component
public class MessageCenterHystric implements MessageCenterCloudClient {
    
    @Override
    public ServerHttpResponse messageCenterSendMsg(KafkaDTO kafkaDTO) {
        System.out.println("--------------------------消息中心SendMsg断路器  发送消息方法进入断路器--------------------------");
        ServerHttpResponse serverHttpResponse = MessageCenterEnum.getHystricResponse();
        System.out.println("--------------------------消息中心SendMsg断路器 发送消息方法断路器处理完毕--------------------------");
        return serverHttpResponse;
    }
}
