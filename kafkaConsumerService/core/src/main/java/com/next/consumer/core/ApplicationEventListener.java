package com.next.consumer.core;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.next.consumer.core.config.ConsumerThreadHandlerSync;
import com.next.consumer.core.constants.FrameworkConstants;
import com.next.consumer.core.manage.KafkaMessageManager;
import com.next.consumer.core.sinker.AbstractMessageSinker;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.stereotype.Component;

/**
 * springboot启动Listener
 *
 * @author QDR
 */
@Component
public class ApplicationEventListener implements ApplicationListener{
    
    protected final Logger logger = LoggerFactory.getLogger(ApplicationEventListener.class);

    private final ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("TopicListenerThread-%d").build();
    
    /**
     * 线程池排队序列最大长度
     */
    private static Integer MAX_CAPACITY = 1024;
    
    private ThreadPoolExecutor executor;
    
    private final List< ConsumerThreadHandlerSync> topicListenerThreadList = new ArrayList<>();

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
    
        if(event instanceof ApplicationPreparedEvent){
            logger.info("开始初始化");
            // 初始化完成
            this.initKafkaConsumerGroup();
            logger.info("结束初始化");
        }
        
        if(event instanceof ContextStoppedEvent ){
            System.out.println("应用停止");
        }
        if(event instanceof ContextClosedEvent){
            this.shutDownApplication();
        }
    }
    
    /**
     * 初始化kafkaConsumer监控线程
     */
    private void initKafkaConsumerGroup(){
        logger.info("---------------------------------initKafkaConsumerGroup start ---------------------------------");
        KafkaMessageManager manager = KafkaMessageManager.getMessageManager();
        
        //检查安全启动环境
        initSinkSafety(manager);
        
        String topicStringArray = manager.getMessageValue(FrameworkConstants.SINKER_CONSUMER_TOPIC);
        //开启的线程数目
        String[] topicArray = topicStringArray.split(",");
        
        logger.info("开启线程为：" + topicStringArray);
        
        //根据配置文件类型开启相应线程监听
        executor = new ThreadPoolExecutor(topicArray.length, topicArray.length,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(MAX_CAPACITY), namedThreadFactory, new AbortPolicy());
        
        logger.info("开启线程数为：" + topicArray.length + "个");

        //消费节点循环
        for (int i = 0; i < topicArray.length; i++) {
            
            //通道
            String topicId = manager.getMessageValue(topicArray[i] + ".topicId");
            //数据分组
            String groupId = manager.getMessageValue(topicArray[i] + ".groupId");
            //实现类
            String className = manager.getMessageValue(topicArray[i] + ".task");
            String topicValue = "";
            //容器注入实现类
            AbstractMessageSinker sinker = (AbstractMessageSinker)SpringContextUtil.getBean(className);
            
            if (sinker != null) {
                //获得topicValue
                topicValue = manager.getTopicValue(topicId);
                sinker.setConsumerId(topicArray[i]);
                
                ConsumerThreadHandlerSync consumerThreadHandler = new ConsumerThreadHandlerSync(topicValue, groupId, sinker);
                
                executor.submit(consumerThreadHandler);
                topicListenerThreadList.add(consumerThreadHandler);
                
                logger.info(
                    "第" + Integer.valueOf(i + 1) + "个线程，监控topic为：" + topicId + ",线程处理实现类为" + className + "启动成功！开始监控！");
            } else {
                logger.info("没有获取到线程处理实现类，请检查配置文件!");
            }
            
        }
        logger.info("---------------------------------initKafkaConsumerGroup end---------------------------------");
    }
    
    
    /**
     * 关闭线程调用程序
     */
    private void shutDownApplication(){
        logger.info("---------------------------------shutDownApplication start ---------------------------------");
        
        for(Runnable runner :topicListenerThreadList){
            ConsumerThreadHandlerSync consumerThreadHandler = (ConsumerThreadHandlerSync)runner;
            consumerThreadHandler.shutDown();
        }
    
        executor.shutdown();
        logger.info("---------------------------------shutDownApplication end ---------------------------------");
    }
    
    
    /**
     * 初始化检查
     * @param manager
     */
    private void initSinkSafety(KafkaMessageManager manager){
        Map<String,String > sinkSafetyMap = manager.getKeyByValue(KafkaMessageManager.SINK_SAFETY);
        Iterator it = sinkSafetyMap.keySet().iterator();
        while(it.hasNext()){
            String sinkerSafetyClass = (String) it.next();
            AbstractMessageSinker sinker = (AbstractMessageSinker)SpringContextUtil.getBean(sinkerSafetyClass);
    
            sinker.initSink();
        }
    }
}