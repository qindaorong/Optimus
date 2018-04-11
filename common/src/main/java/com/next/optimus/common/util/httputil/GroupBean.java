package com.next.optimus.common.util.httputil;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * GroupBean
 *
 * @author qindaorong
 * @date 2017/10/16
 */
public class GroupBean implements Serializable {
    
    private static final long serialVersionUID = 6105813848187153894L;
    
    private static AtomicLong visitCount = new AtomicLong(0);
    
    private ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().build();
    
    
    private final int THREAD_NUMER = 512;
    /**
     * 监控线程池
     */
    private ExecutorService executorService;
    
    /**
     * 监控线程集合SET
     */
    private CopyOnWriteArrayList<String> allServerList = new CopyOnWriteArrayList();
    
    /**
     * 工作中集合SET
     */
    private CopyOnWriteArrayList<String> workingServerList = new CopyOnWriteArrayList();
    
    private GroupBean() {
    
    }
    
    public GroupBean(String serverName ,List<String> allServerArray) {
    
        //线程工厂创建
        namedThreadFactory = new ThreadFactoryBuilder().setNameFormat(serverName+"-%d").build();
        
        executorService =   new ThreadPoolExecutor(allServerArray.size(), allServerArray.size(),
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(THREAD_NUMER), namedThreadFactory, new AbortPolicy());
        
        allServerList.addAll(allServerArray);
        
        //开启线程监控
        this.createHealthCheckThread();
    }
    
    /**
     * 开启监控线程
     */
    private void createHealthCheckThread() {
        for (String serverInfo : allServerList) {
            BackHealthCheck backHealthCheck = new BackHealthCheck(serverInfo, workingServerList);
            executorService.submit(backHealthCheck);
        }
    }
    
    /**
     * 获得正在工作的线程
     */
    public String getWorkingServerIP() {
        String serverInfo = "";
        Long indexNumber = 0L;
        
        if (workingServerList.size() > 0) {
            //AtomicLong数组越界处理
            long countNumber = 1L;
            try {
                countNumber = visitCount.incrementAndGet();
            } catch (Exception e) {
                visitCount = new AtomicLong(1);
            }
            
            indexNumber = countNumber % workingServerList.size();
            serverInfo = workingServerList.get(indexNumber.intValue());
        }
        return serverInfo;
    }
}
