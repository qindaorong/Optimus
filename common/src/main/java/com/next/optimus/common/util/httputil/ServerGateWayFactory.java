package com.next.optimus.common.util.httputil;

import com.next.optimus.common.common.GateWayFactoryManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ServerGateWayFactory class
 *
 * @author qindaorong
 * @date 2017/10/16
 */
public class ServerGateWayFactory {
    
    
    /**
     * 初始化原始线程池
     */
    private static ConcurrentHashMap<String,String> serverPools = new ConcurrentHashMap<String,String>();
    
    /**
     * 初始化原始线程池
     */
    private static ConcurrentHashMap<String,GroupBean> serverGroupBeans = new ConcurrentHashMap<String,GroupBean>();
    
    
    private static ServerGateWayFactory serverGateWayFactory = null;
    
    private ServerGateWayFactory(Map<String, String> map){
        serverPools.putAll(map);

        //工作线程组封装
        createThreadGroup();
    }
    
    public static ServerGateWayFactory getInstance(){
        if(serverGateWayFactory == null ){
            GateWayFactoryManager messageManager = GateWayFactoryManager.getMessageManager();
            serverGateWayFactory = new ServerGateWayFactory(messageManager.getPropertiesMap());
        }
        return serverGateWayFactory;
    }
    
    
    public GroupBean getWatchingGroup(String threadGroupName){
        if(serverGroupBeans.containsKey(threadGroupName)){
            GroupBean groupBean = serverGroupBeans.get(threadGroupName);
            return groupBean;
        }else {
            return null;
        }
    }
    
    
    /**
     * 组装需要实例化的线程
     * @return
     */
    private static void createThreadGroup(){
        
        Map<String,List<String>> map = new HashMap<>();
        
        //修改后的IP
        String serverIP = "";
        
        //服务IP分组
        for(Map.Entry<String, String> entry: serverPools.entrySet()) {
    
            serverIP = entry.getKey().replace("_",":");
            
            //如果在MAP中已经存在
            if(map.containsKey(entry.getValue())){
                map.get(entry.getValue()).add(serverIP);
            }
            //Map中不存在新做一个组
            else{
                List<String> allServerList = new ArrayList<String>();
                allServerList.add(serverIP);
                map.put(entry.getValue(),allServerList);
            }
        }
        
        //监控线程数据注入
        for( Map.Entry<String ,List<String>> entry: map.entrySet()){
            String serverName = entry.getKey();
            //实例化并且开启服务检查
            GroupBean groupBean = new GroupBean(serverName,entry.getValue());
            serverGroupBeans.put(entry.getKey(),groupBean);
        }
        
    }
}
