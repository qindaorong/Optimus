package com.next.consumer.core.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.next.consumer.core.constants.FrameworkConstants;
import com.next.consumer.core.manage.KafkaMessageManager;
import com.next.consumer.core.sinker.AbstractMessageSinker;
import com.next.consumer.core.worker.CallConsumerWorker;
import com.next.optimus.common.util.StringUtil;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ConsumerThreadHandler
 *
 * @author qindaorong
 * @date 2017/10/16
 */
public class ConsumerThreadHandlerSync implements Runnable {
    
    protected final Logger logger = LoggerFactory.getLogger(ConsumerThreadHandlerSync.class);
    
    /**
     * 线程池排队序列最大长度
     */
    private static Integer MAX_CAPACITY = 1024 * 1024;
    
    /**
     * 关闭线程池等待时间
     */
    private static Integer AWAIT_TERMINATION_TIME = 3;
    
    private AtomicInteger atomicNumber = new AtomicInteger(0);
    
    private ExecutorService executorService;
    private final KafkaConsumer<String, String> consumer;
    
    private final Map<TopicPartition, OffsetAndMetadata> offsetsMap = new HashMap<>();
    
    
    private final ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("consumerThread-%d").build();
    
    private AbstractMessageSinker sinker;
    private int partition;
    private String topic;
    
    private boolean shutdownFlag = true;
    
    public ConsumerThreadHandlerSync(String topic, String groupId, AbstractMessageSinker sinker) {
        
        this.sinker = sinker;
        this.topic = topic;
        consumer = KafkaConsumerConfig.createKafkaConsumer(groupId);
        
        //注入topicPartitionNumber
        KafkaMessageManager manager = KafkaMessageManager.getMessageManager();
        
        if (!StringUtil.isEmpty(manager.getMessageValue(FrameworkConstants.TOPIC_PARTITION_NUMBER))) {
            String topicPartitionNumber = manager.getMessageValue(FrameworkConstants.TOPIC_PARTITION_NUMBER);
            try {
                partition = Integer.valueOf(topicPartitionNumber);
                logger.warn("load [topic.partition.number .properties value is [{}].", partition);
            } catch (NumberFormatException e) {
                logger.warn("[topic.partition.number] value can not cast to Integer ,use default value '0'.properties value is [{}].",
                    topicPartitionNumber);
            }
        }
    
        //程调用运行该任务的 execute 本身。此策略提供简单的反馈控制机制，能够减缓新任务的提交速度。
        executorService = new ThreadPoolExecutor(partition, partition,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(MAX_CAPACITY), namedThreadFactory, new ThreadPoolExecutor.CallerRunsPolicy());
    }
    
    /**
     * 消费主方法
     */
    @Override
    public void run() {

        //kafka Consumer是非线程安全的,所以需要每个线程建立一个consumer
        consumer.subscribe(Arrays.asList(topic),
            new ConsumerRebalanceListener() {
                @Override
                public void onPartitionsRevoked(
                    Collection<TopicPartition> partitions) {
                    logger.info("threadId = {}, onPartitionsRevoked.", Thread.currentThread().getId());
                    consumer.commitSync(offsetsMap);
                }
                
                @Override
                public void onPartitionsAssigned(
                    Collection<TopicPartition> partitions) {
                    logger.info("threadId = {}, onPartitionsAssigned.", Thread.currentThread().getId());
                    offsetsMap.clear();
                }
            });
        
        //接收kafka消息
        while (shutdownFlag) {
            try {
                //使用100ms作为获取超时时间
                ConsumerRecords<String, String> records = consumer.poll(1000);

                if (records.count() > 0) {
                    logger.debug("poll records size: " + records.count());
                    logger.info("threadId = {}, on consumer poll Record", Thread.currentThread().getId());

                    List<Callable<Boolean>> tasks = new Vector<>();
    
                    int recordsCount =  records.count();
    
                    for (final ConsumerRecord<String, String> record : records) {
    
                        //记录当前 TopicPartition和OffsetAndMetadata
                        TopicPartition topicPartition = new TopicPartition(record.topic(), record.partition());
                        OffsetAndMetadata offset = new OffsetAndMetadata(record.offset());
                        offsetsMap.put(topicPartition, offset);

                        //加入统一执行集合
                        tasks.add(new CallConsumerWorker(record.topic(), record.value(), sinker,
                            atomicNumber));
                    }
    
                    //统一执行获得返回值
                    List<Future<Boolean>> results = executorService.invokeAll(tasks);
    
                    ConcurrentHashMap<Integer,Boolean> map = new ConcurrentHashMap(recordsCount);
                    
                    for (int i = 0; i < results.size(); i++){
                        map.put(i,results.get(i).get());
                    }
    
                    if(map.contains(Boolean.TRUE)){
                        //提交TopicPartition位移偏移量
                        consumer.commitSync(offsetsMap);
                    }
                    
                    //TopicPartition位移偏移量清空
                    offsetsMap.clear();
                }

            } catch (WakeupException e) {
                logger.warn("ConsumerRecords has an WakeupException massage is [{}].", e.getMessage());
            } catch (Exception e ){
                e.printStackTrace();
                logger.error("ConsumerRecords has an Exception massage is [{}].",  e.getMessage());
            }
        }
    }
    
    
    
    /**
     * 安全关闭线程池方法
     */
    public void shutDown() {
    
        logger.info("[" + Thread.currentThread().getName() + "] shutDown is work!");
        
        shutdownFlag = false;
        
        //关闭每个线程topic开启的worker线程
        if (!offsetsMap.isEmpty()) {
            consumer.commitSync(offsetsMap);
            consumer.close();
        }
    
        executorService.shutdown();
        
        try {
            if (!executorService.awaitTermination(AWAIT_TERMINATION_TIME, TimeUnit.SECONDS)) {
                logger.warn("Timeout.... Ignore for this case");
            }
        } catch (InterruptedException ignored) {
            logger.warn("Other thread interrupted this shutdown, ignore for this case.");
            Thread.currentThread().interrupt();
        }
        
        logger.info("[" + Thread.currentThread().getName() + "] has bean shutDown!");
    }
}