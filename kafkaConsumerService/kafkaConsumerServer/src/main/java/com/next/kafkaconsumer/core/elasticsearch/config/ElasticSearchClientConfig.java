package com.next.kafkaconsumer.core.elasticsearch.config;

import com.next.kafkaconsumer.core.elasticsearch.constants.CommonConstants;
import com.next.kafkaconsumer.core.elasticsearch.manager.ElasticSearchPropManager;
import com.next.kafkaconsumer.core.elasticsearch.util.ElasticSearchUtil;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * @author Donald
 * @date 2017/9/20.
 */
@Configuration
@EnableAutoConfiguration
@DependsOn("elasticSearchPropManager")
public class ElasticSearchClientConfig {
    
    private static final Logger log = LoggerFactory.getLogger(ElasticSearchClientConfig.class);
    
    @Bean(name = "client")
    public TransportClient elasticSearchClient() {
        TransportClient client = null;
        try {
            log.info(" >>>>>>>>>>>>>>> [ElasticSearchConfig] Initial ElasticSearch TransportClient Start... <<<<<<<<<<<<< ");
            // init TransportClient
            client = initClient();
            // check node health
            ElasticSearchUtil.checkNodeHealth(client);
            log.info(" >>>>>>>>>>>>>>> [ElasticSearchConfig] Initial ElasticSearch TransportClient End. <<<<<<<<<<<<< ");
        } catch (UnknownHostException e) {
            log.error(" >>>>>>>>>>>>>>> [ElasticSearchConfig] Initial ElasticSearch TransportClient Error: Unable to get the host. <<<<<<<<<<<<< ");
        }
        return client;
    }
    
    private TransportClient initClient() throws UnknownHostException {
        // Build the settings for our template.
        Map<String, String> clientConfigs = ElasticSearchPropManager.getElasticSearchClientConf();
        Settings settings = Settings.builder()
            .put(clientConfigs)
            .build();
        
        log.info(" >>>>>>>>>>>>>>> [ElasticSearchConfig] Elasticsearch Cluster Name: {}.",
            clientConfigs.get(CommonConstants.ES_CLUSTER_NAME));
        
        // Fetch transport.tcp.port value.
        String transportTcpPortStr = ElasticSearchPropManager.getTransportTcpPort();
        final int transportTcpPort = Integer.valueOf(transportTcpPortStr);
        
        // Fetch discovery.zen.ping.unicast.hosts value.
        List<String> hosts = ElasticSearchPropManager.getDiscoveryZenPingUnicastHosts();
        
        
        // Instantiate a TransportClient and add the cluster to the list of addresses to connect to.
        TransportClient client = new PreBuiltXPackTransportClient(settings);
        for (String host : hosts) {
            log.info(" >>>>>>>>>>>>>>> [ElasticSearchConfig] Add {}:{} into ElasticSearch Cluster. <<<<<<<<<<<<< ", host, transportTcpPort);
            client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), transportTcpPort));
        }
        return client;
    }
}
