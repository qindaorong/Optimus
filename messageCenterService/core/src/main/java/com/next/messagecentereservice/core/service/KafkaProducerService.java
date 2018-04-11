package com.next.messagecentereservice.core.service;

import com.next.messagecentereservice.core.constant.kafkamanage.KafkaManager;
import com.next.messagecentereservice.core.kafkaconfig.ProducerConfig;
import com.next.optimus.common.exception.NextHttpException;
import com.next.optimus.common.messagebean.Message;
import com.next.optimus.messagecenterservice.api.dto.KafkaDTO;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.errors.AuthorizationException;
import org.apache.kafka.common.errors.OutOfOrderSequenceException;
import org.apache.kafka.common.errors.ProducerFencedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * KafkaProducerServer
 *
 * @author qindaorong
 * @date 2017/10/16
 */
@Service
public class KafkaProducerService {
    
    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);
    
    @Autowired
    private Producer<String, String> producer;
    
    /**
     * 向kafka压入一条数据
     * @param kafkaDTO
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws NextHttpException
     */
    public void sendMessage(KafkaDTO kafkaDTO) throws InterruptedException ,ExecutionException,NextHttpException{
    
        KafkaManager manager = KafkaManager.getMessageManager();
        Map<String, String> topicMap = manager.getTopicProperties();
    
        if (topicMap.containsKey(kafkaDTO.getTopicId())) {
            String topic = topicMap.get(kafkaDTO.getTopicId());
    
            Message msg = new Message();
            msg.setMessageType(topicMap.get(kafkaDTO.getTopicId()));
            msg.setContent(kafkaDTO.getContent());
            msg.setMessageNumber(String.valueOf(0));
    
            String timeStamp = "[Producer send time id " + String.valueOf(System.currentTimeMillis()) + "]";
    
            try {
                //如果没有key默认随机随机分区
                RecordMetadata recordMetadata = producer.send(new ProducerRecord<String, String>(topic, timeStamp, msg.toString())).get();
                String partition = String.valueOf(recordMetadata.partition());
                String topicName = recordMetadata.topic();
                logger.info("kafka message already send , partition is [{}] , topic name is [{}] ,message is [{}] ", partition, topicName,
                    msg.toString());
            } catch (InterruptedException e) {
                throw e;
            } catch (ExecutionException e) {
                throw e;
            }
        }else{
            throw new NextHttpException();
        }
    }
    
    
    /**
     * 向KAFKA压入一组数据
     * @param kafkaDTO
     * @throws ProducerFencedException
     * @throws OutOfOrderSequenceException
     * @throws AuthorizationException
     * @throws NextHttpException
     */
    public void sendMessageCollection(KafkaDTO kafkaDTO) throws ProducerFencedException ,OutOfOrderSequenceException,AuthorizationException,NextHttpException{
        
        KafkaManager manager = KafkaManager.getMessageManager();
        Map<String, String> topicMap = manager.getTopicProperties();
        
        if (topicMap.containsKey(kafkaDTO.getTopicId()) && !kafkaDTO.getStringList().isEmpty()) {
    
            //创建一个特殊带有事物的Producer
            Producer<String, String> transactionalProducer = ProducerConfig.getTransactionalProducerConfig();
    
            //初始化Transactions
            transactionalProducer.initTransactions();
    
            String topic = topicMap.get(kafkaDTO.getTopicId());
            
            try{
                //开启Transactions
                transactionalProducer.beginTransaction();

                for (String messageString : kafkaDTO.getStringList()){
                    String timeStamp = "[Producer send time id " + String.valueOf(System.currentTimeMillis()) + "]";
    
                    Message msg = new Message();
                    msg.setMessageType(topicMap.get(kafkaDTO.getTopicId()));
                    msg.setContent(messageString);
                    msg.setMessageNumber(String.valueOf(0));
    
                    transactionalProducer.send(new ProducerRecord<>(topic, timeStamp, msg.toString()));
                }
                //提交Transactions
                transactionalProducer.commitTransaction();
                
            }catch (ProducerFencedException | OutOfOrderSequenceException | AuthorizationException e) {
                // We can't recover from these exceptions, so our only option is to close the producer and exit.
                transactionalProducer.close();
                throw e;
            } catch (KafkaException e) {
                // For all other exceptions, just abort the transaction and try again.
                transactionalProducer.abortTransaction();
                throw e;
            }finally {
                //关闭Producer
                transactionalProducer.close();
            }
        }
    }
}
