package com.next.optimus.messagecenterservice.api;

import com.next.optimus.common.dataresponse.ServerHttpResponse;
import com.next.optimus.messagecenterservice.api.dto.KafkaDTO;
import com.next.optimus.messagecenterservice.api.hystric.MessageCenterHystric;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * MessageCenterCloudClient
 *
 * @author qindaorong
 * @date 2017/10/16
 */
@FeignClient(value = "MESSAGECENTER", fallback = MessageCenterHystric.class)
public interface MessageCenterCloudClient {
    
    /**
     * 调用messageCenter方法
     * @param kafkaDTO
     * @return
     */
    @RequestMapping(value = "/sendMessage", method = RequestMethod.GET)
    ServerHttpResponse messageCenterSendMsg(KafkaDTO kafkaDTO);
    
}
