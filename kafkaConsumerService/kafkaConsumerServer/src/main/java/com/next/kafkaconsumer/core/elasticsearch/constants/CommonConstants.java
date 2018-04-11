package com.next.kafkaconsumer.core.elasticsearch.constants;

/**
 * CommonConstants
 *
 * @author qindaorong
 * @date 2017/10/16
 */
public class CommonConstants {
    
    /**
     * message
     */
    public static final String PRO_FILE_MESSAGE_PROPERTIES = "message.properties";
    /**
     * elasticsearch_consumer
     */
    public static final String PRO_FILE_ELASTICSEARCH_CONSUMER_SETTING = "elasticsearch_consumer.properties";
    /**
     * elasticsearch
     */
    public static final String PRO_FILE_ELASTICSEARCH_SETTING = "elasticsearch.properties";
    
    
    /**
     * ElasticSearch CommonConstants
      */
    public static final String ES_CLUSTER_NAME="cluster.name";
    public static final String ES_XPACK_SECURITY_USER="xpack.security.user";
    public static final String ES_CLIENT_TRANSPORT_SNIFF="client.transport.sniff";
    public static final String ES_TRANSPORT_PING_SCHEDULE="transport.ping_schedule";
    public static final String ES_XPACK_SECURITY_TRANSPORT_SSL_ENABLED="xpack.security.transport.ssl.enabled";
    
    public static final String ES_TRANSPORT_TCP_PORT="transport.tcp.port";
    public static final String ES_DISCOVERY_ZEN_PING_UNICAST_HOSTS="discovery.zen.ping.unicast.hosts";
    
}
