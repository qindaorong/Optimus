package clientTest;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Donald on 2017/9/15.
 */
public class ElasticSearchTemplateTest {
    private static final Logger log = LoggerFactory.getLogger(ElasticSearchTemplateTest.class);
    
    private TransportClient client;
    
    /*@Before
    private void initClient() {
        // on startup
        try {
            // Build the settings for our template.
            Settings settings = Settings.builder()
                .put("cluster.name", "next_dev")
                .put("xpack.security.user", "elastic:changeme")
                .put("xpack.security.transport.ssl.enabled", false)
                .put("transport.ping_schedule", "5s")
                .put("client.transport.sniff", "true")            // In order to enable sniffing, set template.transport.sniff to true
                .build();
            // Instantiate a TransportClient and add the cluster to the list of addresses to connect to.
            //client = new PreBuiltTransportClient(settings)
            client = new PreBuiltXPackTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("devsvc01"), 9300))
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("devsvc02"), 9300))
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("devsvc03"), 9300));
        } catch (UnknownHostException e) {
            log.debug("[ElasticSearchClient] Initial ElasticSearchClient Error: Unable to get the host.");
        }
        log.debug("[ElasticSearchClient] Initial ElasticSearchClient Success.");
    }
    
    @Test
    public static void main(String[] args)  {
        ElasticSearchTemplateTest elasticSearchClientTest = new ElasticSearchTemplateTest();
        elasticSearchClientTest.initClient();
        elasticSearchClientTest.healthCheck();
        //elasticSearchClientTest.createIndex();
        //elasticSearchClientTest.search();
        
    }
    
    private void search() {
        // String token = basicAuthHeaderValue("elastic", new SecuredString("changeme".toCharArray()));
        // template.filterWithHeader(Collections.singletonMap("Authorization", token))
        //     .prepareSearch().get();
    }
    
    private void createIndex() {
        try {
            XContentBuilder builder = jsonBuilder()
                .startObject()
                .field("user", "kimchy")
                .field("postDate", new Date())
                .field("message", "trying out Elasticsearch")
                .endObject();
            log.debug("[ElasticSearchClient] builder = {}.", builder.toString());
            // index document
            IndexResponse response = client.prepareIndex("twitter", "tweet", "2")
                .setSource(builder.string(),  XContentType.JSON)
                .get();
            // Index name
            String _index = response.getIndex();
            log.debug("[ElasticSearchClient] response.index = {}.", _index);
            // Type name
            String _type = response.getType();
            log.debug("[ElasticSearchClient] response.type = {}.", _type);
            // Document ID (generated or not)
            String _id = response.getId();
            log.debug("[ElasticSearchClient] response.id = {}.", _id);
            // Version (if it's the first time you index this document, you will get: 1)
            long _version = response.getVersion();
            log.debug("[ElasticSearchClient] response.version = {}.", _version);
            // status has stored current instance statement.
            RestStatus status = response.status();
            log.debug("[ElasticSearchClient] response.status = {}.", status);
            
        } catch (IOException e) {
            log.debug("[ElasticSearchClient] jsonBuilder error.");
        }
    }
    
    private void healthCheck() {

        while (true) {
            try {
                log.info("Getting cluster health...... ");
                ActionFuture<ClusterHealthResponse> healthFuture = client.admin().cluster().health(Requests.clusterHealthRequest());
                ClusterHealthResponse healthResponse = healthFuture.get(5, TimeUnit.SECONDS);
                log.info("Got cluster health response: [{}]", healthResponse.getStatus());
            } catch (Throwable t) {
                log.error("Unable to get cluster health response: [{}]", t.getMessage());
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ie) {
                log.debug(ie.getMessage());
            }
        }
    }
    
    
    
    @After
    private void destroy() {
        // on shutdown
        client.close();
    }*/
}
