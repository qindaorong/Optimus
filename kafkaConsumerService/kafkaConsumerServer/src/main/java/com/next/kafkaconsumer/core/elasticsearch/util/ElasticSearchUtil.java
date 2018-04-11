package com.next.kafkaconsumer.core.elasticsearch.util;

import java.util.concurrent.TimeUnit;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Donald
 * @description :
 * @date : 2017/9/22 11:13.
 */
public class ElasticSearchUtil {
    
    private static final Logger log = LoggerFactory.getLogger(ElasticSearchUtil.class);
    
    public static void checkNodeHealth(TransportClient client) {
        try {
            log.info(" >>>>>>>>>>>>>>> [ElasticSearchConfig] Getting ElasticSearch cluster health... <<<<<<<<<<<<< ");
            ActionFuture<ClusterHealthResponse> healthFuture = client.admin().cluster().health(Requests.clusterHealthRequest());
            final ClusterHealthResponse clusterHealth = healthFuture.get(5, TimeUnit.SECONDS);
            if (clusterHealth.isTimedOut()) {
                log.warn(">>>>>>>>>>>>>>> [ElasticSearchConfig] ElasticSearch cluster health timed out");
            } else {
                log.info(" >>>>>>>>>>>>>>> [ElasticSearchConfig] ElasticSearch cluster health: Status {} ; {} nodes; {} active shards.",
                    clusterHealth.status().name(), clusterHealth.getNumberOfNodes(), clusterHealth.getActiveShards());
            }
        } catch (Throwable t) {
            log.error(" >>>>>>>>>>>>>>> [ElasticSearchConfig] Unable to get cluster health response: [{}]. <<<<<<<<<<<<< ", t.getMessage());
        }
    }
}
