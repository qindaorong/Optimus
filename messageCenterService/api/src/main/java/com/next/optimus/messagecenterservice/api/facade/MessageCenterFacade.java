package com.next.optimus.messagecenterservice.api.facade;

import com.next.optimus.common.dataresponse.ServerHttpResponse;
import com.next.optimus.messagecenterservice.api.dto.KafkaDTO;


/**
 * MessageCenterFacade
 *
 * @author qindaorong
 * @date 2017/10/16
 */
public interface MessageCenterFacade {
    
    /**
     * 向kafka发送消息
     * @param kafkaDTO
     * @return
     */
    public ServerHttpResponse sendMessage(KafkaDTO kafkaDTO);
}
