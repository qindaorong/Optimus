package com.next.optimus.common.dataresponse;


import com.next.optimus.common.common.ErrorCodeManager;
import com.next.optimus.common.util.StringUtil;
import java.util.HashMap;
import java.util.Map;

/**
 * ServerHttpResponse<T>
 *
 * @author qindaorong
 * @date 2017/10/16
 */
public class ServerHttpResponse<T> extends BaseResponseDTO{
    
    /**
     * 业务返回码
     */
    private String bizCode;
    
    /**
     * 业务描述
     */
    private String bizDescribe;

    /**
     * 业务返回数据
     */
    private Map<String,Object> returnMap;

    public ServerHttpResponse() {
    }
    
    /**
     * 系统异常抛出时使用
     * @param httpCode
     */
    public ServerHttpResponse(int httpCode) {
        this.httpStatus = httpCode;
    }
    
    
    /**
     * 业务异常抛出时使用
     * @param httpCode
     * @param bizCode
     */
    public ServerHttpResponse(int httpCode,String bizCode) {
        this.httpStatus = httpCode;
        this.bizCode = bizCode;
        returnMap = new HashMap<>();
        
        ErrorCodeManager errorCodeManager = ErrorCodeManager.getErrorCodeManager();
        this.bizDescribe = errorCodeManager.getPropertiesValue(bizCode);
    }
    
    
    public String getBizCode() {
        return bizCode;
    }
    
    public void setBizCode(String bizCode) {
        this.bizCode = bizCode;
    }
    
    public String getBizDescribe() {
        return bizDescribe;
    }
    
    public void setBizDescribe(String bizDescribe) {
        this.bizDescribe = bizDescribe;
    }
    
    public void setMapAttribute(String key ,Object o){
        if(!StringUtil.isEmpty(key)){
        this.returnMap.put(key,o);
        }
    }
    
    public Map<String, Object> getReturnMap() {
        return returnMap;
    }
    
    public void setReturnMap(Map<String, Object> returnMap) {
        this.returnMap = returnMap;
    }
    
    @Override
    public String toString() {
        return super.toString();
    }
}
