package com.next.consumer.core.worker;

import com.next.consumer.core.sinker.AbstractMessageSinker;
import com.next.optimus.common.messagebean.Message;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ConsumerWorker
 *
 * @author qindaorong
 * @date 2017/10/16
 */
public class CallConsumerWorker implements Callable<Boolean> {
    
    private Logger logger = LoggerFactory.getLogger(CallConsumerWorker.class);
    
    private String topic;
    private String messageJson;
    private AbstractMessageSinker sinker;
    private AtomicInteger atomicNumber;
    
    Boolean sinkerResBoolean = false;
    
    public CallConsumerWorker(
        String topic,
        String messageJson,
        AbstractMessageSinker sinker,
        AtomicInteger atomicNumber
    ) {
        this.topic = topic;
        this.messageJson = messageJson;
        this.sinker = sinker;
        this.atomicNumber = atomicNumber;
    }
    
    @Override
    public Boolean call() {
        try {
            Message message = new Message();
            //String 转 josn
            JSONObject jsonObject = JSONObject.fromObject(messageJson);
            message.setMessageType(jsonObject.get("messageType").toString());
            message.setMessageNumber(jsonObject.get("messageNumber").toString());
            message.setContent(jsonObject.get("content").toString());

            sinkerResBoolean = sinker.sink(message);
        
            int workNumber = atomicNumber.incrementAndGet();
            logger.info("kafka atomicNumber is [{}]", workNumber);
            
        } catch (Exception e) {
            logger.error("ParseKafkaLogJob run error. ", e);
        }
        logger.info("sink return value [{}]", sinkerResBoolean);
        
        return sinkerResBoolean;
    }
    
    public void shutDownWorker(){
        if(sinker != null ){
            sinker.shutdownSink();
        }
    }
    
}
