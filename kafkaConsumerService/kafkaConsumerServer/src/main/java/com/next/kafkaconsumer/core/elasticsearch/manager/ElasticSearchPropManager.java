package com.next.kafkaconsumer.core.elasticsearch.manager;

import com.google.common.collect.Maps;
import com.next.kafkaconsumer.core.elasticsearch.constants.CommonConstants;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 * @author : Donald
 * @description :
 * @date : 2017/9/22 10:19.
 */
@Configuration
@PropertySource( value = { "classpath:/" + CommonConstants.PRO_FILE_ELASTICSEARCH_SETTING ,
    "classpath:/" + CommonConstants.PRO_FILE_MESSAGE_PROPERTIES })
public class ElasticSearchPropManager implements EnvironmentAware {
    
    private static Environment env;
    
    private static Logger log  = LoggerFactory.getLogger(ElasticSearchPropManager.class);
    
    private static String transportTcpPort;
    
    private static List<String> topics;
    
    private static List<String> discoveryZenPingUnicastHosts;
    
    private static Map<String,String> elasticSearchConf;
    
    @Override
    public void setEnvironment(Environment environment) {
        env = environment;
    }
    
    public static Map<String,String> getElasticSearchClientConf() {
        if (Objects.isNull(elasticSearchConf)) {
            elasticSearchConf = Maps.newHashMap();
            elasticSearchConf.put(CommonConstants.ES_CLUSTER_NAME, env.getRequiredProperty(CommonConstants.ES_CLUSTER_NAME));
            elasticSearchConf.put(CommonConstants.ES_XPACK_SECURITY_USER, env.getRequiredProperty(CommonConstants.ES_XPACK_SECURITY_USER));
            elasticSearchConf.put(CommonConstants.ES_CLIENT_TRANSPORT_SNIFF, env.getRequiredProperty(CommonConstants.ES_CLIENT_TRANSPORT_SNIFF));
            elasticSearchConf.put(CommonConstants.ES_TRANSPORT_PING_SCHEDULE, env.getRequiredProperty(CommonConstants.ES_TRANSPORT_PING_SCHEDULE));
            elasticSearchConf.put(CommonConstants.ES_XPACK_SECURITY_TRANSPORT_SSL_ENABLED, env.getRequiredProperty(CommonConstants.ES_XPACK_SECURITY_TRANSPORT_SSL_ENABLED));
        }
        return elasticSearchConf;
    }
    
    public static String getTransportTcpPort() {
        if(Objects.isNull(transportTcpPort)) {
            transportTcpPort = env.getProperty(CommonConstants.ES_TRANSPORT_TCP_PORT);
        }
        return transportTcpPort;
    }
    
    public static List<String> getDiscoveryZenPingUnicastHosts() {
        if(Objects.isNull(discoveryZenPingUnicastHosts)) {
            discoveryZenPingUnicastHosts = env.getProperty(CommonConstants.ES_DISCOVERY_ZEN_PING_UNICAST_HOSTS, ArrayList.class);
        }
        return discoveryZenPingUnicastHosts;
    }
}
