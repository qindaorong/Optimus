package com.next.kafkaconsumer.core.elasticsearch.config;

import com.next.kafkaconsumer.core.elasticsearch.service.ElasticSearchTemplate;
import org.elasticsearch.client.transport.TransportClient;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Donald
 * @date 2017/9/20.
 */
@Configuration
@EnableAutoConfiguration
@Import(ElasticSearchClientConfig.class)
public class ElasticSearchTemplateConfig {
    
    @Bean(name = "elasticSearchTemplate")
    public ElasticSearchTemplate elasticSearchTemplate(TransportClient client) {
        /*
         * create ElasticSearchTemplate
         */
        ElasticSearchTemplate elasticSearchTemplate = new ElasticSearchTemplate(client);
        return elasticSearchTemplate;
    }
}
