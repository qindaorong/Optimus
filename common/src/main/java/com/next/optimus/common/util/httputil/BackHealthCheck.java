package com.next.optimus.common.util.httputil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

/**
 * BackHealthCheck
 *
 * @author qindaorong
 * @date 2017/10/16
 */
public class BackHealthCheck implements Runnable,Serializable {
    
    private static final long serialVersionUID = 811201120421934287L;
    
    
    private static final String DEFAULT_CHARSET = "UTF-8";
    /**
     * 心跳检查访问地址
     */
    private String serverInfo;

    /**
     * 监控工作线程池
     */
    private CopyOnWriteArrayList<String> workingServerList;
    
    public BackHealthCheck(String serverInfo,CopyOnWriteArrayList<String> workingServerList){
        this.serverInfo = serverInfo;
        this.workingServerList = workingServerList;
    }
    
    @Override
    public void run(){
        Map<String,Object> map = new HashMap<String,Object>();
        while(true){

            String returnValue = "";
            try {
                HttpResponse response = HttpClientUtil.get("http://"+serverInfo+"/healthCheck",map);
    
                if(response !=null ){
                    HttpEntity responseObj = response.getEntity();
                    returnValue = EntityUtils.toString(responseObj,DEFAULT_CHARSET);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            
            if("0".equals(returnValue)){
                if(!workingServerList.contains(serverInfo)){
                    //在工作线程池中添加数据
                    workingServerList.add(serverInfo);
                }
            }else{
                //检查在工作线程池中是否存在
                if(workingServerList.contains(serverInfo)){
                    //从工作线程池中移除服务
                    workingServerList.remove(serverInfo);
                }
            }
    
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
