package com.next.messagecentereservice.core.constant.kafkamanage;

/**
 * KafkaConstant
 *
 * @author qindaorong
 * @date 2017/10/16
 */
public class KafkaConstant {
    
    /**
     * message读取文件名称
     */
    public static String PRO_FILE_MESSAGE_PROPERTIES = "message.properties";
    
    /**
     * message读取文件名称
     */
    public static String PRO_FILE_TOPIC_SETTING = "topic.properties";
    
    public static String BOOTSTRAP_SERVERS = "bootstrap.servers";
    public static String ACKS = "acks";
    public static String RETRIES = "retries";
    public static String BATCH_SIZE= "batch.size";
    public static String LINGER_MS= "linger.ms";
    public static String BUFFER_MEMORY= "buffer.memory";
    public static String KEY_SERIALIZER= "key.serializer";
    public static String VALUE_SERIALIZER= "value.serializer";
    public static String NUM_PARTITIONS= "num.partitions";
    
    public static String TRANSACTIONAL_ID= "transactional.id";
    
}
