package com.next.kafkaconsumer.core.elasticsearch.builder;

import com.google.common.collect.Maps;
import com.next.kafkaconsumer.core.elasticsearch.manager.ElasticSearchConsumerPropManager;
import com.next.optimus.common.util.StringUtil;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.PostConstruct;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * @author : Donald
 * @description :
 * @date : 2017/10/13 17:19.
 */
@Component
@DependsOn("elasticSearchConsumerPropManager")
public class BuildElasticSearchDataSource {
    
    private static Logger log = LoggerFactory.getLogger(BuildElasticSearchDataSource.class);
    
    private static Map<String, String> dataSourceMap = null;
    
    private static String getSource(String topicId) {
        String jsonData;
        try {
            Map<String, String> esMapping = ElasticSearchConsumerPropManager.getMappingByTopic(topicId);
            // ElasticSearchMappingBean esMapping = getMappingByTopic(this.consumerId);
            log.info(">>>>> elasticSearchMapping :  <<<<<：" + esMapping);
    
    
            XContentBuilder builder = XContentFactory.jsonBuilder().startObject();
            
            for(String key : esMapping.keySet()) {
                log.info(">>>>> es[key] : {} <<<<<" , key );
                if( !"type".equals(key) ) {
                    // String esKey= esMapping.get(key);
                    // log.info(">>>>> es[key] : {} <<<<<" ,  esKey);
                    //String esValue = contentJsonObject.getString(key);
                    //log.info(">>>>> es[value] : {} <<<<<" , esValue );
                    builder.field(esMapping.get(key), "");
                }
                


//                Object o1 = contentJsonObject.get(esKey);
                
                // if(StringUtil.isEmpty(esKey)) {
                //     throw new IllegalArgumentException("Cannot found " + topicId + "." + key +  ",please check " + CommonConstants
                //         .PRO_FILE_ELASTICSEARCH_CONSUMER_SETTING );
                // } else {
                //     builder.field(esKey, esValue);
                // }
            }
            
            jsonData  = builder.endObject().string();
            
        } catch (IOException e) {
            log.error("Error:" +  e.getMessage());
            throw new RuntimeException("Error:" +  e.getMessage());
        }
        
        log.info(">>>>> jsonData : {} <<<<<", jsonData);
        return jsonData;
    }
    
    @PostConstruct
    public static void getAllSource() {
        List<String> topics = ElasticSearchConsumerPropManager.getTopics();
        String source;
        for (String topic : topics) {
            source = getSource(topic);
            if (!StringUtil.isEmpty(source)) {
                if(Objects.isNull(dataSourceMap)) {
                    dataSourceMap = Maps.newConcurrentMap();
                }
                dataSourceMap.put(topic, source);
            }
        }
    }
    
    public static String getSourceByTopic(String topicId) {
        if(Objects.isNull(dataSourceMap)) {
            Assert.notNull(dataSourceMap,"BuildElasticSearchDataSource.dataSourceMap can not be null.");
        }
        return dataSourceMap.containsKey(topicId) ? dataSourceMap.get(topicId) : "";
    }
}
