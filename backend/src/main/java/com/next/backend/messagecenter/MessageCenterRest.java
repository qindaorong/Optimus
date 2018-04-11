package com.next.backend.messagecenter;

import com.next.optimus.common.dataresponse.ServerHttpResponse;
import com.next.optimus.messagecenterservice.api.MessageCenterLBClient;
import com.next.optimus.messagecenterservice.api.dto.KafkaDTO;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * MessageCenterRest
 *
 * @author qindaorong
 * @date 2017/10/16
 */
@RestController
public class MessageCenterRest{
    
    MessageCenterLBClient messageCenterLBClient = new MessageCenterLBClient();

    @RequestMapping(value = "/sendMessage")
    public ServerHttpResponse sendMessage(@Valid @RequestBody KafkaDTO kafkaDTO) {
        return messageCenterLBClient.sendMessage(kafkaDTO);
    }
}
