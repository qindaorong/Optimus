package com.next.kafkaconsumer.core.elasticsearch.manager;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.next.consumer.core.constants.FrameworkConstants;
import com.next.kafkaconsumer.core.elasticsearch.constants.CommonConstants;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;

/**
 * @author : Donald
 * @description :
 * @date : 2017/9/21 15:37.
 */
@Configuration
@PropertySource(value = {
    "classpath:/" + CommonConstants.PRO_FILE_ELASTICSEARCH_CONSUMER_SETTING,
    "classpath:/" + CommonConstants.PRO_FILE_MESSAGE_PROPERTIES
})
public class ElasticSearchConsumerPropManager implements EnvironmentAware{
    
    private static Environment env;
    
    private static final Logger log = LoggerFactory.getLogger(ElasticSearchConsumerPropManager.class);
    
    private static List<String> topics;
    
    private static Map<String,Map<String, String>> elasticSearchMappings;
    
    private static Map<String,Map<String, String>> elasticSearchTypeMappings;
    
    private static final List<String> AVAILABLE_FILED_DATA_TYPES = Lists.newArrayList("text", "keyword", "long", "integer", "short",
        "byte", "double", "float", "half_float", "scaled_float", "boolean", "date", "binary", "ip");
    
    @Override
    public void setEnvironment(Environment environment) {
        env = environment;
    }
    
    @PostConstruct
    private static Map<String,Map<String, String>> getElasticSearchMappings() {
        if(CollectionUtils.isEmpty(topics)) {
            
            topics = env.getRequiredProperty(FrameworkConstants.SINKER_CONSUMER_TOPIC, ArrayList.class);
            
            // fetch resource
            ResourceBundle rb = ResourceBundle.getBundle("elasticsearch_consumer");
            // get all keys
            Enumeration<String> allKey = rb.getKeys();
            while (allKey.hasMoreElements()) {
                
                String key = allKey.nextElement();
                
                // now you have name and value
                for(String topic : topics) {
                    
                    // filter keys
                    if (key.startsWith(topic)) {
                        
                        String postFixOfKey = StringUtils.substring(key, topic.length() + 1, key.length() );

                        String valueAndType = rb.getString(key);
                        String[] valueAndTypeArray = valueAndType.split(":");
                        String value = valueAndTypeArray[0];
                        
                        if(Objects.isNull(elasticSearchMappings)) {
                            elasticSearchMappings = Maps.newHashMap();
                            elasticSearchTypeMappings = Maps.newHashMap();
                        }
                        
                        if (elasticSearchMappings.keySet().contains(topic)) {
                            elasticSearchMappings.get(topic).put(postFixOfKey, value);
                        } else {
                            Map mappings = Maps.newHashMap();
                            mappings.put(postFixOfKey, value);
                            elasticSearchMappings.put(topic,mappings);
                        }
    
                        String valueType = "";
                        if (valueAndTypeArray.length == 2) {
                            valueType = valueAndTypeArray[1];
                            if(!AVAILABLE_FILED_DATA_TYPES.contains(valueType)) {
                                throw new IllegalArgumentException("Type configuration error about filed '" +  key
                                    + "',please check configuration : " + CommonConstants.PRO_FILE_ELASTICSEARCH_CONSUMER_SETTING );
                            }
                        } else if ("type".equals(postFixOfKey)){
                            continue;
                        } else {
                            throw new IllegalArgumentException("Type configuration error about filed '" +  key
                                + "',please check configuration : " + CommonConstants.PRO_FILE_ELASTICSEARCH_CONSUMER_SETTING );
                        }
                        
                        if (elasticSearchTypeMappings.keySet().contains(topic)) {
                            elasticSearchTypeMappings.get(topic).put(postFixOfKey, valueType);
                        } else {
                            Map mappings = Maps.newHashMap();
                            mappings.put(value, valueType);
                            elasticSearchTypeMappings.put(topic,mappings);
                        }
                        
                    }
                }
            }
        }
        return elasticSearchMappings;
    }
    
    public static Map<String, String> getMappingByTopic(String topic) {
        if (Objects.isNull(elasticSearchMappings)) {
            getElasticSearchMappings();
        }
        Map<String, String> esMapping = elasticSearchMappings.get(topic);
        Assert.notNull(esMapping, "Cannot find mapping by topic : { " + topic + "}");
        return esMapping;
    }
    
    public static Map<String, String> getTypeMappingByTopic(String topic) {
        if (Objects.isNull(elasticSearchTypeMappings)) {
            Assert.notNull(elasticSearchTypeMappings, "ElasticSearchConsumerPropManager.elasticSearchTypeMappings cannot be nell.");
        }
        Map<String, String> esTypeMapping = elasticSearchTypeMappings.get(topic);
        Assert.notNull(esTypeMapping, "Cannot find esType mapping by topic : { " + topic + "}");
        return esTypeMapping;
    }
    
    public static List<String> getTopics() {
        return topics;
    }
}
