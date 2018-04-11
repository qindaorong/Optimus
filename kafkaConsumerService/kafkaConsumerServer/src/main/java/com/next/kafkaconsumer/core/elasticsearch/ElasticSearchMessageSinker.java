package com.next.kafkaconsumer.core.elasticsearch;


import com.next.consumer.core.sinker.AbstractMessageSinker;
import com.next.kafkaconsumer.core.elasticsearch.builder.BuildElasticSearchDataSource;
import com.next.kafkaconsumer.core.elasticsearch.manager.ElasticSearchConsumerPropManager;
import com.next.kafkaconsumer.core.elasticsearch.service.ElasticSearchTemplate;
import com.next.optimus.common.messagebean.Message;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author Donald
 * @date 2017/9/21
 */
@Component
@Scope("prototype")
@DependsOn("elasticSearchTemplate")
public class ElasticSearchMessageSinker extends AbstractMessageSinker {
    
    private final Logger log = LoggerFactory.getLogger(ElasticSearchMessageSinker.class);
    
    @Autowired
    private ElasticSearchTemplate elasticSearchTemplate;
    

    
    @Override
    public boolean sink(Message message) {
    
        log.info("[ElasticSearchMessageSinker].sink start ！Thread name is [{}]",Thread.currentThread().getName());
        String content = message.getContent();
    
    
        //log.info("[ElasticSearchMessageSinker] content is [{}]",content);
        
        JSONObject contentJsonObject = JSONObject.fromObject( content );

        Map<String, String> esMapping = ElasticSearchConsumerPropManager.getMappingByTopic(this.consumerId);
        
        
        
        String source = BuildElasticSearchDataSource.getSourceByTopic(this.consumerId);
        JSONObject sourceJsonObject = JSONObject.fromObject( source );

        for(String esKey : esMapping.keySet()){
            if(!"type".equals(esKey)) {
                sourceJsonObject.put(esMapping.get(esKey), contentJsonObject.get(esKey));
            }
        }
        source = sourceJsonObject.toString();
        String id = null;
        if (contentJsonObject.containsKey("id")) {
            contentJsonObject.getString("id");
        }
    
        boolean addFlag = elasticSearchTemplate.addDocument(this.consumerId, esMapping.get("type"), id ,source);
    
        log.info("[ElasticSearchMessageSinker] sink end!");
        return addFlag;
    }
    
    @Override
    public void shutdownSink() {
        elasticSearchTemplate.closeClient();
    }
    
    @Override
    public void initSink() {
        List<String> topics = ElasticSearchConsumerPropManager.getTopics();
        for (String topic : topics) {
            Map<String, String> mapping = ElasticSearchConsumerPropManager.getMappingByTopic(topic);
            log.info("[ApplicationStartup] checkIndexOrCreateIndexForES : topic:{},type:{}", topic, mapping.get("type"));
            elasticSearchTemplate.checkOrCreateIndex(topic, mapping.get("type"));
        }
    }
    
    
    
}
