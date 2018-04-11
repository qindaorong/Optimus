package com.next.kafkaconsumer.core.elasticsearch.builder;

import java.util.Objects;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * @author Donald
 * @date 2017/9/20.
 */
public final class ElasticSearchIndexBuilder {
    
    private TransportClient client;
    private String index;
    private String id;
    private String type;
    private String source;
    
    private Logger log = LoggerFactory.getLogger(ElasticSearchIndexBuilder.class);
    
    public ElasticSearchIndexBuilder(TransportClient client) {
        Assert.notNull(client, "Client cannot be null.");
        this.client = client;
    }
    
    public ElasticSearchIndexBuilder index(String index) {
        ensureNameNoBlank(index, "index");
        this.index = index;
        return this;
    }
    
    public ElasticSearchIndexBuilder type(String type) {
        ensureNameNoBlank(index, "type");
        this.type = type;
        return this;
    }
    
    public ElasticSearchIndexBuilder id(String id) {
        ensureNameNoBlank(index, "id");
        this.id = id;
        return this;
    }
    
    public ElasticSearchIndexBuilder addJsonDataSource(String source) {
        ensureNameNoBlank(index, "source");
        this.source = source;
        return this;
    }
    
    public IndexResponse build() {
        IndexResponse indexResponse = null;
        if (Objects.isNull(id)) {
            indexResponse = client.prepareIndex(index, type)
                .setSource(source, XContentType.JSON)
                .get();
        } else {
            indexResponse = client.prepareIndex(index, type, id)
                .setSource(source, XContentType.JSON)
                .get();
        }
        return indexResponse;
    }
    
    public static void ensureNameNoBlank(String data, String type) {
        if(StringUtils.isBlank(data)) {
            throw new IllegalArgumentException("Field " + type + " cannot be blank.");
        }
    }
}
