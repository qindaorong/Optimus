package com.next.consumer.core.config;

import com.next.consumer.core.constants.FrameworkConstants;
import com.next.consumer.core.manage.KafkaMessageManager;
import java.util.Properties;
import org.apache.kafka.clients.consumer.KafkaConsumer;

/**
 * KafkaConsumerConfig
 *
 * @author qindaorong
 * @date 2017/10/16
 */
public class KafkaConsumerConfig {

    /**
     * 组装kafka配置
     */
    public static KafkaConsumer<String, String> createKafkaConsumer(String groupId) {
        
        //注入Manager
        KafkaMessageManager manager = KafkaMessageManager.getMessageManager();
        
        Properties props = new Properties();
        props.put("bootstrap.servers",manager.getMessageValue(FrameworkConstants.BOOTSTRAP_SERVERS));
        props.put("group.id", groupId);
        props.put("enable.auto.commit", "false");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", manager.getMessageValue(FrameworkConstants.SESSION_TIMEOUT_MS));
        props.put("max.poll.records", manager.getMessageValue(FrameworkConstants.MAX_POLL_RECORDS));
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("auto.offset.reset", manager.getMessageValue(FrameworkConstants.AUTO_OFFSET_RESET));
        props.put("max.poll.records", 60 * 1000);
        
        return new KafkaConsumer(props);
    }

}
