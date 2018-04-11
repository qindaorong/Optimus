package com.next.optimus.common.trafficthrottling.service;

import com.next.optimus.common.enums.ConfigKeyEnums;
import com.next.optimus.common.util.ConfigurationManager;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Michael on 12/11/2017.
 * @author Michael
 */
public class RateLimitService {
    private static final Logger log = LoggerFactory.getLogger(RateLimitService.class);
    
    private Integer COUNT_LIMIT;
    
    /** <timestamp,<ip/key,count>>**/
    private Map<Long,ConcurrentHashMap<String,AtomicInteger>>
        ipRequestCounter = new HashMap<>();
    
    private Properties properties;
    
    private int interval;
    private int intervalCount;
    
    private int monitorInterval;
    
    public RateLimitService(){
        Thread t = new Thread(new RateLimitMonitorThread(),"rate_limit_thread");
        t.setDaemon(true);
        t.start();
        properties = ConfigurationManager.getInstance().getConfigInfo();
        interval = Integer.valueOf(properties.getProperty(ConfigKeyEnums.INTERVAL.getKey()));
        intervalCount = Integer.valueOf(properties.getProperty(ConfigKeyEnums.INTERVAL_COUNT.getKey()));
        COUNT_LIMIT = interval * intervalCount;
        monitorInterval = interval;
    }
    
    public boolean canPass(String ip){
        long currentTime = System.currentTimeMillis()/1000;
        boolean canPass = true;
        log.info("[canPass] start,and ip = {},and currentTime = {}",ip,currentTime);
        ConcurrentHashMap<String,AtomicInteger> counter = this.get(currentTime);
        if(Objects.nonNull(counter)){
            AtomicInteger count = counter.get(ip);
            if (Objects.isNull(count)) {
                counter.put(ip, new AtomicInteger(0));
                count = counter.get(ip);
            }
            count.incrementAndGet();
            int sum;
            synchronized (RateLimitService.class) {
                sum = this.calculateRequestTimeForInterval(currentTime, ip);
                log.info("[canPass] sum = {},ip = {}", sum, ip);
                if (sum > COUNT_LIMIT) {
                    log.info("[canPass] request hit limit and sum = {} and ip = {} and currentTime = {}", sum, ip, currentTime);
                    canPass = false;
                }
            }
        }else{
            log.error("[canPass] did not create concurrent map.");
        }
        return canPass;
    }
    
    private int calculateRequestTimeForInterval(long currentTime,String ip){
        //AtomicInteger atomicSum = new AtomicInteger(0);
        int sum = 0;
        Integer duration = interval;
        while(duration > 0){
            ConcurrentHashMap<String,AtomicInteger> counter = get(currentTime);
            if(Objects.nonNull(counter) && Objects.nonNull(counter.get(ip))){
                int count = counter.get(ip).intValue();
                sum += count;
            }
            currentTime --;
            duration --;
        }
        return sum;
    }
    
    private ConcurrentHashMap<String,AtomicInteger> get(long currentTime){
        ConcurrentHashMap<String,AtomicInteger> counter = ipRequestCounter.get(currentTime);
        return counter;
    }
    
    class RateLimitMonitorThread implements Runnable{
        /**time unit second**/
        
        @Override
        public void run() {
            while(true){
                Long currentTime = System.currentTimeMillis()/1000;
                log.info("[RateLimitMonitorThread] currentTime = {}",currentTime);
                Integer interval = monitorInterval * 2;
                openTimeUnit(interval,currentTime);
                eraseTimeUnit(interval,currentTime);
                try {
                    TimeUnit.SECONDS.sleep(monitorInterval);
                } catch (InterruptedException e) {
                    log.error(e.getMessage(), e);
                } catch(Exception e1){
                    log.error(e1.getMessage(),e1);
                }
            }
        }
        
        /**
         *
         * @param interval time unit second
         * @param currentTime time unit second
         */
        private void openTimeUnit(int interval,Long currentTime){
            log.info("[RateLimitMonitorThread.openTimeUnit] interval = {},currentTime = {}", interval, currentTime);
            Long currentTimeForOpen;
            currentTimeForOpen = currentTime;
            while(interval > 0){
                if(Objects.isNull(ipRequestCounter.get(currentTimeForOpen))){
                    ConcurrentHashMap<String, AtomicInteger> counter = new ConcurrentHashMap<>();
                    ipRequestCounter.put(currentTimeForOpen, counter);
                    log.info("[RateLimitMonitorThread.openTimeUnit] currentTimeForOpen = {}",currentTimeForOpen);
                }
                currentTimeForOpen++;
                interval--;
            }
            if(ipRequestCounter.size() > 5 * monitorInterval){
                log.error("[RateLimitMonitorThread.eraseTimeUnit] ipRequestCounter size > {} and ipRequestCounter = {},and this may be like memory leak.",ipRequestCounter.size(),ipRequestCounter);
            }else{
                log.info("[RateLimitMonitorThread.eraseTimeUnit] ipRequestCounter size = {}",ipRequestCounter.size());
            }
        }
        
        
        private void eraseTimeUnit(int interval,Long currentTime){
            log.info("[RateLimitMonitorThread.eraseTimeUnit] interval = {},currentTime = {}",interval,currentTime);
            Long currentTimeForErase = currentTime - interval - 1;
            while(interval > 0){
                Integer limit = 4 * monitorInterval;
                if((ipRequestCounter.size() > limit) &&(Objects.nonNull(ipRequestCounter.get(currentTimeForErase)))){
                    ipRequestCounter.remove(currentTimeForErase);
                    log.info("[RateLimitMonitorThread.eraseTimeUnit] Erase Object currentTimeForErase = {}",currentTimeForErase);
                }
                currentTimeForErase--;
                interval--;
            }
            if(ipRequestCounter.size() > 5 * monitorInterval){
                log.error("[RateLimitMonitorThread.eraseTimeUnit] ipRequestCounter size > {} and ipRequestCounter = {},and this may be like memory leak.",ipRequestCounter.size(),ipRequestCounter.toString());
            }else{
                log.info("[RateLimitMonitorThread.eraseTimeUnit] ipRequestCounter size = {}",ipRequestCounter.size(),ipRequestCounter.toString());
            }
        }
    }
}
