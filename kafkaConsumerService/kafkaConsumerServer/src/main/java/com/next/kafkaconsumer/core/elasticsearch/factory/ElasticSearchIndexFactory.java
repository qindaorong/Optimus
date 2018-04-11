package com.next.kafkaconsumer.core.elasticsearch.factory;


import com.next.kafkaconsumer.core.elasticsearch.builder.ElasticSearchIndexBuilder;
import org.elasticsearch.client.transport.TransportClient;

/**
 * @author Donald
 * @date 2017/9/20.
 */
public class ElasticSearchIndexFactory {
    
    public ElasticSearchIndexFactory() {
    }
    
    public static ElasticSearchIndexBuilder createIndexBuilder(TransportClient client) {
        return new ElasticSearchIndexBuilder(client);
    }

}
