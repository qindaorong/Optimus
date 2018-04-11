package com.next.kafkaconsumer.core.elasticsearch.service;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import com.next.kafkaconsumer.core.elasticsearch.factory.ElasticSearchIndexFactory;
import com.next.kafkaconsumer.core.elasticsearch.manager.ElasticSearchConsumerPropManager;
import com.next.optimus.common.util.StringUtil;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Map;
import org.elasticsearch.action.admin.indices.cache.clear.ClearIndicesCacheResponse;
import org.elasticsearch.action.admin.indices.close.CloseIndexResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.exists.types.TypesExistsResponse;
import org.elasticsearch.action.admin.indices.open.OpenIndexResponse;
import org.elasticsearch.action.admin.indices.recovery.RecoveryResponse;
import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 * @date 2017/9/15.
 */
@Component
public class ElasticSearchTemplate {
    
    private static final Logger log = LoggerFactory.getLogger(ElasticSearchTemplate.class);
    
    private TransportClient client;
    
    private static final int NUMBER_OF_SHARDS = 6;
    
    private static final int NUMBER_OF_REPLICAS = 1;
    
    
    public TransportClient getClient() {
        return client;
    }
    
    
    public ElasticSearchTemplate(TransportClient client) {
        this.client = client;
    }
    
    /**
     *
     * @param indexName
     * @param type
     * @param id
     * @param source
     * @return
     */
    public boolean addDocument(String indexName, String type, String id, String source) {
        //checkOrCreateIndex(indexName, type);
        IndexResponse index = ElasticSearchIndexFactory.createIndexBuilder(client).index(indexName).type(type).id(id)
            .addJsonDataSource(source).build();
        log.info("[ElasticSearchTemplate] Add document: _index: [{}] ,_type: [{}], _id: [{}] ,_version: [{}] , status: [{}] ,id: [{}]."
            ,index,type,id,index.getVersion(),index.status(),id);

        if( RestStatus.OK.equals(index.status()) || RestStatus.CREATED.equals(index.status()) ){
            return true;
        }else{
            return false;
        }
    }
    
    /**
     *
     * @param indexName
     */
    public void checkOrCreateIndex(String indexName, String type) {
        try {
            if(!isExistsIndex(indexName)) {
                log.info("[ElasticSearchTemplate] Index for key {} didn't exist, now going to CREATE new index: {}.", indexName, indexName);
                createIndex(indexName, type);
            }
        } catch (IOException e) {
            log.error("[ElasticSearchTemplate] Index for key '{}' check error : {}.", indexName, e.getMessage());
            
        }
    }
    
    /**
     *
     * @param indexName
     */
    private void checkOrCreateIndexWithType(String indexName,String type) {
        try {
            if(!isExistsType(indexName, type)) {
                log.info("[ElasticSearchTemplate]  Index for key '{}' didn't exist, now going to CREATE new index : .", indexName, indexName);
                createSimpleIndex(indexName);
            }
        } catch (UnknownHostException e) {
            log.error("[ElasticSearchTemplate] Index for key '{}' check error : {}.", indexName, e.getMessage());
        }
    }
    
    /**
     *
     * @param indexName
     * @return
     * @throws UnknownHostException
     */
    private boolean isExistsIndex(String indexName) throws UnknownHostException {
        enSureIndexNameNotBlank(indexName);
        IndicesExistsResponse response =
            client.admin().indices().prepareExists(indexName).execute().actionGet();
        if(response.isExists()) {
            log.info("[ElasticSearchTemplate] Index for key {} already exists.", indexName);
        }
        return response.isExists();
    }
    
    /**
     *
     * @param indexName
     * @param type
     * @return
     * @throws UnknownHostException
     */
    private boolean isExistsType(String indexName, String type) throws UnknownHostException {
        enSureIndexNameNotBlank(indexName);
        enSureTypeNotBlank(indexName);
        if( isExistsIndex(indexName) ) {
            return true;
        }
        TypesExistsResponse response =
            client.admin().indices().prepareTypesExists(indexName).setTypes(type).execute().actionGet();
        return response.isExists();
    }
    
    /**
     *
     * @param indexName
     */
    private void enSureIndexNameNotBlank(String indexName) {
        enSureStringNotBlank(indexName, "index");
    }
    
    /**
     *
     * @param type
     */
    private void enSureTypeNotBlank(String type) {
        enSureStringNotBlank(type, "type");
    }
    
    /**
     *
     * @param value
     * @param type
     */
    private void enSureStringNotBlank(String value, String type) {
        if (StringUtil.isEmpty(type)) {
            throw new IllegalArgumentException(new StringBuilder("[ElasticSearchTemplate] ")
                .append(value).append(" for key '")
                .append(type).append("' is empty.").toString()
            );
        }
    }
    
    /**
     * @function Create an index with all default settings and no mapping.
     *
     * @param indexName
     */
    private void createSimpleIndex(String indexName) {
        CreateIndexResponse created = client.admin().indices().prepareCreate(indexName).execute().actionGet();
        if (!created.isAcknowledged()) {
            throw new RuntimeException("[ElasticSearchTemplate] Could not create index '" + indexName + "' !");
        }
    }
    
    
    /**
     * @function Created can have specific settings associated with it.
     *
     * @param indexName
     */
    private void createSimpleIndex(String indexName, int shards, int replicas) {
        CreateIndexResponse created = client.admin().indices()
            .prepareCreate(indexName)
            .setSettings(Settings.builder()
                .put("index.number_of_shards", shards)
                .put("index.number_of_replicas", replicas)
            )
            .execute()
            .actionGet();
        if (!created.isAcknowledged()) {
            throw new RuntimeException("[ElasticSearchTemplate] Could not create index '" + indexName + "' !");
        }
    }
    
    /**
     * @function Add a new type while creating an index with mapping.
     *
     * @param indexName
     */
    private void createIndex(String indexName,String type) throws IOException {
    
        // read type mapping from configuration
        Map<String, String> typeMappings = ElasticSearchConsumerPropManager.getTypeMappingByTopic(indexName);
        
        // create Index Request Builder
        CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate(indexName);
        
        // add mapping
        createIndexRequestBuilder.addMapping( type, createMapping(indexName, type, typeMappings), XContentType.JSON );
        
        // set setting
        createIndexRequestBuilder.setSettings( createSetting() );
        
        // execute request
        CreateIndexResponse created = createIndexRequestBuilder.execute().actionGet();
    
        if (!created.isAcknowledged()) {
            throw new RuntimeException("[ElasticSearchTemplate] Could not create index '" + indexName + "' !");
        }
        log.debug("[ElasticSearchTemplate] Successfully CREATE new index : {}.", indexName);
    }
    
    
    
    public String createMapping(String indexName, String type,  Map<String, String> typeMappings) throws IOException {
        Date date = new Date();
        XContentBuilder builder = jsonBuilder()
            .startObject()
            .startObject(type)
            .startObject("properties");
        
        for(String key : typeMappings.keySet()) {
            if("date".equals(typeMappings.get(key))) {
                builder.startObject(key)
                    .field("type", "date")
                    .field("format","date")
                    .field("index", "not_analyzed")
                    .endObject();
            } else {
                builder.startObject(key)
                    .field("type", typeMappings.get(key))
                    .field("index", "not_analyzed")
                    .endObject();
            }
        }
        
        builder
            .endObject()
            .endObject()
            .endObject();
        return builder.string();
    }
    
    /**
     * @function create Settings for index.
     *
     * @return
     */
    public static Settings createSetting() {
        Settings settings = Settings.builder()
            .put("number_of_shards", NUMBER_OF_SHARDS)
            .put("number_of_replicas", NUMBER_OF_REPLICAS)
            .build();
        return settings;
    }
    
    public void deleteIndex(String indexName) {
        DeleteIndexResponse deleteIndexResponse = client.admin().indices().prepareDelete(indexName,"","").execute().actionGet();
        if (!deleteIndexResponse.isAcknowledged()) {
            throw new RuntimeException("[ElasticSearchTemplate] Could not delete index '" + indexName + "' !");
        }
        log.info("[ElasticSearchTemplate] Delete index ok.");
    }
    
    public void recoveryIndex(String indexName) {
        RecoveryResponse recoveryResponse = client.admin().indices().prepareRecoveries(indexName).execute().actionGet();
        if (!recoveryResponse.hasRecoveries()) {
            throw new RuntimeException("[ElasticSearchTemplate] Could not recovery index '" + indexName + "' !");
        }
        log.info("[ElasticSearchTemplate] Recovery index ok.");
    }
    
    public void closeIndex(String indexName) {
        CloseIndexResponse closeIndexResponse = client.admin().indices().prepareClose(indexName).execute().actionGet();
        if (!closeIndexResponse.isAcknowledged()) {
            throw new RuntimeException("[ElasticSearchTemplate] Could not close index '" + indexName + "' !");
        }
        log.info("[ElasticSearchTemplate] Close index ok.");
    }
    
    public void openIndex(String indexName) {
        OpenIndexResponse openIndexResponse = client.admin().indices().prepareOpen(indexName).execute().actionGet();
        if (!openIndexResponse.isAcknowledged()) {
            throw new RuntimeException("[ElasticSearchTemplate] Could not open index '" + indexName + "' !");
        }
        log.info("[ElasticSearchTemplate] Open index ok.");
    }
    
    public void clearCacheIndex(String indexName) {
        ClearIndicesCacheResponse clearIndicesCacheResponse = client.admin().indices().prepareClearCache(indexName).execute().actionGet();
        if (null == clearIndicesCacheResponse ){
            throw new RuntimeException("[ElasticSearchTemplate] Could not ClearCache index '" + indexName + "' !");
        }
        log.info("[ElasticSearchTemplate] Clear Cache index ok.");
    }
    
    public void deleteDocument(String indexName, String type, String id) {
        DeleteResponse deleteResponse = client.prepareDelete(indexName, type, id).get();
        if (!RestStatus.OK.equals(deleteResponse.status())) {
            throw new RuntimeException("[ElasticSearchTemplate] Could not delete document '" + indexName + "," + type + "," + id + "' !");
        }
        log.info("[ElasticSearchTemplate] Delete Document ok.");
    }
    
    public void defaultSetting(String indexName) {
        UpdateSettingsResponse updateSettingsResponse = client.admin().indices().prepareUpdateSettings(indexName)
            .setSettings(
                Settings.builder()
                    //.put("number_of_shards", NUMBER_OF_SHARDS)
                    .put("number_of_replicas", 0)
                    .build()
            
            ).execute().actionGet();
        if(updateSettingsResponse.isAcknowledged()) {
            throw new RuntimeException("[ElasticSearchTemplate] Could not reset default setting : " + indexName);
        }
        log.info("[ElasticSearchTemplate] Delete reset default setting ok.");
    }
    
    public void getSetting(String indexName) {
    
    }
    
    public void closeClient() {
        client.close();
    }
}
