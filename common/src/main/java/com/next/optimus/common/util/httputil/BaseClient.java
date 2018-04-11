package com.next.optimus.common.util.httputil;

import com.next.optimus.common.dataresponse.ServerHttpResponse;
import com.next.optimus.common.exception.NextHttpException;
import com.next.optimus.common.exception.ServerAliveException;
import com.next.optimus.common.util.StringUtil;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import net.sf.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;

/**
 * BaseClient
 *
 * @author qindaorong
 * @date 2017/10/16
 */
public abstract class BaseClient{
    
    /**
     * 监控对象类
     */
    private GroupBean groupBean;
    
    protected String serverName ;
    
    public  BaseClient(String serverName){
        this.serverName = serverName;
        groupBean = ServerGateWayFactory.getInstance().getWatchingGroup(serverName);
    }

    /**
     * 发送方法GET
     */
    public static String HTTP_METHODS_GET ="GET";
    
    /**
     * 发送方法POST
     */
    public static String HTTP_METHODS_POST ="POST";
    
    /**
     * 处理发送请求Rest请求
     * @param requestMapping    方法映射
     * @param params            参数集合
     * @param methods           处理方法
     * @return
     */
    public ServerHttpResponse sendHttpRequest(
        String requestMapping,
        Map<String ,Object> params,
        String methods
    ) throws ServerAliveException, NextHttpException {
        ServerHttpResponse serverHttpResponse = null;
        //当前存活的线程
        String serverPrefix = groupBean.getWorkingServerIP();
        String url = "http://"+ serverPrefix + requestMapping;
        
        if(!StringUtil.isEmpty(serverPrefix)){
            HttpResponse httpResponse = null;
            if(StringUtil.equals(BaseClient.HTTP_METHODS_GET,methods)){
                httpResponse = HttpClientUtil.get(url,params);
            }
            if(StringUtil.equals(BaseClient.HTTP_METHODS_POST,methods)) {
                
                JSONObject json = JSONObject.fromObject(params);
                httpResponse = HttpClientUtil.requestPost(url,json.toString(),null);
            }
    
            serverHttpResponse = this. httpResponseBack(httpResponse);
            
        }else{
            throw new ServerAliveException();
        }
        return serverHttpResponse;
    }
    

    /**
     * 处理发送请求Rest请求
     * @param requestMapping    方法映射
     * @param params            参数集合
     * @param headerParams      head参数
     * @param methods           处理方法
     * @return
     * @throws ServerAliveException
     * @throws NextHttpException
     */
    public ServerHttpResponse sendHttpRequest(
        String requestMapping,
        Map<String ,Object> params,
        Map<String ,String> headerParams,
        String methods
    ) throws ServerAliveException, NextHttpException {
        ServerHttpResponse serverHttpResponse = null;
        //当前存活的线程
        String serverPrefix = groupBean.getWorkingServerIP();
        String url = "http://"+ serverPrefix + requestMapping;
        
        if(!StringUtil.isEmpty(serverPrefix)){
            HttpResponse httpResponse = null;
            if(StringUtil.equals(BaseClient.HTTP_METHODS_GET,methods)){
                if(headerParams != null ){
                    httpResponse = HttpClientUtil.sendGetRequest(url,params,headerParams,"UTF-8");
                }else{
                    httpResponse = HttpClientUtil.get(url,params);
                }
            }
            if(StringUtil.equals(BaseClient.HTTP_METHODS_POST,methods)) {
                JSONObject json = JSONObject.fromObject(params);
                if(headerParams != null ){
                    httpResponse = HttpClientUtil.requestPost(url,json.toString(),headerParams);
                }else{
                    httpResponse = HttpClientUtil.requestPost(url,json.toString(),null);
                }
            }
    
            serverHttpResponse = this. httpResponseBack(httpResponse);

        }else{
            throw new ServerAliveException();
        }
        return serverHttpResponse;
    }
    
    private ServerHttpResponse httpResponseBack(HttpResponse httpResponse) throws NextHttpException {
        //HTTP级别返回状态码
        int httpCode = httpResponse.getStatusLine().getStatusCode();
    
        ServerHttpResponse serverHttpResponse = null;
    
        if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
            //返回成功
            String resultStr = null;
            try {
                resultStr = EntityUtils.toString(httpResponse.getEntity());
            }catch (IOException e){
                throw  new NextHttpException();
            }
        
            JSONObject jsonObject = JSONObject.fromObject(resultStr);
            serverHttpResponse = (ServerHttpResponse)JSONObject.toBean(jsonObject,ServerHttpResponse.class);
        }else{
            //返回失败
            serverHttpResponse  = new ServerHttpResponse(httpCode);
        }
        return serverHttpResponse;
    }

    /**
     * 封装参数Map
     * @param keyArray
     * @param params
     * @return
     */
    public Map<String,Object> createParamMap(String[] keyArray,String ...params){
        Map<String, Object> paramMap = new HashMap<>();
        
        for(int i = 0 ; i < keyArray.length;i++ ){
            paramMap.put(keyArray[i],params[i]);
        }
        return paramMap;
    }
}