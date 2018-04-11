package com.next.messagecentereservice.core.kafkaconfig;

import com.next.messagecentereservice.core.constant.kafkamanage.KafkaConstant;
import com.next.messagecentereservice.core.constant.kafkamanage.KafkaManager;
import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.common.serialization.StringSerializer;


/**
 * ProducerConfig
 *
 * @author qindaorong
 * @date 2017/10/16
 */
public class ProducerConfig {
    
    /**
     * 获得一般Producer
     * @return
     */
    public static Producer<String, String> getProducerConfig(){
        // 注入Manager
        KafkaManager manager = KafkaManager.getMessageManager();

        Properties props = new Properties();
        props.put("bootstrap.servers", manager.getMessage(KafkaConstant.BOOTSTRAP_SERVERS));
        props.put("acks", manager.getMessage(KafkaConstant.ACKS));
        props.put("retries", manager.getMessage(KafkaConstant.RETRIES));
        props.put("batch.size", manager.getMessage(KafkaConstant.BATCH_SIZE));
        props.put("linger.ms", manager.getMessage(KafkaConstant.LINGER_MS));
        props.put("buffer.memory", manager.getMessage(KafkaConstant.BUFFER_MEMORY));
        props.put("key.serializer", manager.getMessage(KafkaConstant.KEY_SERIALIZER));
        props.put("value.serializer", manager.getMessage(KafkaConstant.VALUE_SERIALIZER));
        props.put("num.partitions", 6);

        Producer<String, String> producer = new KafkaProducer<>(props);
        return producer;
    }
    
    
    /**
     * 有带有事物的Producer
     * @return
     */
    public static Producer<String, String> getTransactionalProducerConfig(){
        // 注入Manager
        KafkaManager manager = KafkaManager.getMessageManager();

        Properties props = new Properties();
        props.put("bootstrap.servers", manager.getMessage(KafkaConstant.BOOTSTRAP_SERVERS));
        props.put("transactional.id", manager.getMessage(KafkaConstant.TRANSACTIONAL_ID));
        
        Producer<String, String> producer = new KafkaProducer<>(props, new StringSerializer(), new StringSerializer());
        
        return producer;
    }
    
    
}
