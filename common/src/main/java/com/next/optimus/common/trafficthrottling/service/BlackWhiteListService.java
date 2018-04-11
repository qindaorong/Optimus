package com.next.optimus.common.trafficthrottling.service;

import com.next.optimus.common.enums.ConfigKeyEnums;
import com.next.optimus.common.util.ConfigurationManager;
import com.next.optimus.common.util.httputil.CacheServiceClient;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Michael on 12/11/2017.
 * @author Michael
 */
public class BlackWhiteListService {
    
    private static final Logger log = LoggerFactory.getLogger(BlackWhiteListService.class);
    
    private Map<String,Set<String>> blackAndWhiteMap;
    
    private static final String blackIPKey = "auth_key_black_list";
    
    private static final String whiteIPKey = "auth_key_white_list";
    
    private static final String blackKeyPrefix = "b:";
    
    private static final String whiteKeyPrefix = "w:";
    
    private static final Integer MONITOR_THREAD_SLEEP = 10;
    
    public BlackWhiteListService(){
        Thread syncThread = new Thread(new SyncBlackAndWhiteList(),"sync_black_white_list");
        syncThread.setDaemon(true);
        syncThread.start();
    }
    
    public boolean isInBlackList(String ip){
        boolean isInBlackList = false;
        if(Objects.nonNull(blackAndWhiteMap)){
            log.info("[isInBlackList] blackAndWhiteMap = {}",blackAndWhiteMap);
            Set<String> blackSet = new HashSet<>(blackAndWhiteMap.get(blackIPKey));
            if(Objects.nonNull(blackSet) && blackSet.contains(blackKeyPrefix+ip)){
                isInBlackList = true;
            }
        }
        return isInBlackList;
    }
    
    public boolean isInWhiteList(String ip){
        boolean isInWhiteList = false;
        log.info("[isInWhiteList] blackAndWhiteMap = {}",blackAndWhiteMap);
        if(Objects.nonNull(blackAndWhiteMap)){
            Set<String> whiteSet = new HashSet<>(blackAndWhiteMap.get(whiteIPKey));
            if(Objects.nonNull(whiteSet) && whiteSet.contains(whiteKeyPrefix+ip)){
                isInWhiteList = true;
            }
        }
        return isInWhiteList;
    }
    
    class SyncBlackAndWhiteList implements Runnable{
        @Override
        public void run() {
            CacheServiceClient client = new CacheServiceClient();
            ConfigurationManager configurationManager = ConfigurationManager.getInstance();
            Properties config = configurationManager.getConfigInfo();
            while(true){
                blackAndWhiteMap = client.getBlackOrWhiteListFromCache(config.getProperty(ConfigKeyEnums.CACHE_SERVICE_URL_KEY.getKey()));
                if(Objects.isNull(blackAndWhiteMap)){
                    blackAndWhiteMap = new HashMap<>();
                }
                try {
                    TimeUnit.SECONDS.sleep(MONITOR_THREAD_SLEEP);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
}
