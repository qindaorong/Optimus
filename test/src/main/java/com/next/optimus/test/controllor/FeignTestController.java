package com.next.optimus.test.controllor;

import com.next.optimus.common.dataresponse.ServerHttpResponse;
import com.next.optimus.messagecenterservice.api.MessageCenterCloudClient;
import com.next.optimus.messagecenterservice.api.dto.KafkaDTO;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * FeignTestController
 *
 * @author qindaorong
 * @date 2017/10/16
 */
@RestController
public class FeignTestController {
    
    private static final Logger logger = LoggerFactory.getLogger(FeignTestController.class);
    
    @Autowired
    private MessageCenterCloudClient messageCenterCloudClient;
    
    @RequestMapping(value = "/feignSendMessage",method = RequestMethod.POST)
    public ServerHttpResponse feignSendMessage(
        @Valid @RequestBody KafkaDTO kafkaDTO
    ){
        logger.info("FeignTestController get the request");
        ServerHttpResponse serverHttpResponse = messageCenterCloudClient.messageCenterSendMsg(kafkaDTO);
        logger.info("FeignTestController get the response from messageCenter");
        return serverHttpResponse;
    }
    
}
