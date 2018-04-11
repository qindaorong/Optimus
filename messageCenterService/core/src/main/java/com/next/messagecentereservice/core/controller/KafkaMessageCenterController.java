package com.next.messagecentereservice.core.controller;

import com.next.messagecentereservice.core.service.KafkaProducerService;
import com.next.optimus.common.dataresponse.ServerHttpResponse;
import com.next.optimus.common.exception.NextHttpException;
import com.next.optimus.messagecenterservice.api.MessageCenterEnum;
import com.next.optimus.messagecenterservice.api.dto.KafkaDTO;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.kafka.common.errors.AuthorizationException;
import org.apache.kafka.common.errors.OutOfOrderSequenceException;
import org.apache.kafka.common.errors.ProducerFencedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * KafkaMessageCenterController
 *
 * @author qindaorong
 * @date 2017/10/16
 */
@RestController
public class KafkaMessageCenterController {
    
    private static final Logger logger = LoggerFactory.getLogger(KafkaMessageCenterController.class);

    @Autowired
    private KafkaProducerService kafkaProducerService;
    

    @RequestMapping(value = "/sendMessage", method = RequestMethod.POST)
    public ServerHttpResponse sendMessage(@Valid @RequestBody KafkaDTO kafkaDTO,HttpServletResponse response) {
      ServerHttpResponse serverHttpResponse;
        try{
            kafkaProducerService.sendMessage(kafkaDTO);
    
            serverHttpResponse = new ServerHttpResponse(HttpStatus.SC_OK, MessageCenterEnum.MESSSAGE_CENTER_SEND_SUC.getCode());

        }catch (InterruptedException e) {
            logger.info("kafka message fail to send ,Exception is [InterruptedException] , Exception message is [{}] ", e.getMessage());
            serverHttpResponse = new ServerHttpResponse(HttpStatus.SC_INTERNAL_SERVER_ERROR, MessageCenterEnum.MESSSAGE_CENTER_SEND_FAIL.getCode());
        } catch (ExecutionException e) {
            logger.info("kafka message fail to send ,Exception is [ExecutionException] , Exception message is [{}] ", e.getMessage());
            serverHttpResponse = new ServerHttpResponse(HttpStatus.SC_INTERNAL_SERVER_ERROR, MessageCenterEnum.MESSSAGE_CENTER_SEND_FAIL.getCode());
        }catch (NextHttpException e) {
            logger.info("kafka message fail to send ,Exception is [NextHttpException], Exception message is [{}] ", e.getMessage());
            serverHttpResponse = NextHttpException.getNextHttpExceptionResponse();
        }
        return serverHttpResponse;
    }
    
    
    @RequestMapping(value = "/sendMessageCollection", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    public ServerHttpResponse sendMessageCollection(@Valid @RequestBody KafkaDTO kafkaDTO) {

        ServerHttpResponse serverHttpResponse;
        
        try{
            kafkaProducerService.sendMessageCollection(kafkaDTO);
            
            serverHttpResponse = new ServerHttpResponse(HttpStatus.SC_OK, MessageCenterEnum.MESSSAGE_CENTER_SEND_SUC.getCode());
        }catch (ProducerFencedException e) {
            logger.info("kafka message fail to send ,Exception is [ProducerFencedException] , Exception message is [{}] ", e.getMessage());
            serverHttpResponse = new ServerHttpResponse(HttpStatus.SC_INTERNAL_SERVER_ERROR, MessageCenterEnum.MESSSAGE_CENTER_SEND_FAIL.getCode());
        } catch (OutOfOrderSequenceException e) {
            logger.info("kafka message fail to send ,Exception is [OutOfOrderSequenceException] , Exception message is [{}] ", e.getMessage());
            serverHttpResponse = new ServerHttpResponse(HttpStatus.SC_INTERNAL_SERVER_ERROR, MessageCenterEnum.MESSSAGE_CENTER_SEND_FAIL.getCode());
        } catch (AuthorizationException e) {
            logger.info("kafka message fail to send ,Exception is [AuthorizationException] , Exception message is [{}] ", e.getMessage());
            serverHttpResponse = new ServerHttpResponse(HttpStatus.SC_INTERNAL_SERVER_ERROR, MessageCenterEnum.MESSSAGE_CENTER_SEND_FAIL.getCode());
        } catch (NextHttpException e) {
            logger.info("kafka message fail to send ,Exception is [NextHttpException], Exception message is [{}] ", e.getMessage());
            serverHttpResponse = NextHttpException.getNextHttpExceptionResponse();
        }
        return serverHttpResponse;
    }
}
