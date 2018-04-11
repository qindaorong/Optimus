package com.next.optimus.test.config;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.mvc.condition.RequestCondition;

/**
 * ApiVesrsionCondition
 *
 * @author qindaorong
 * @create 2017-11-16 10:18 AM
 **/
public class ApiVesrsionCondition implements RequestCondition<ApiVesrsionCondition> {
    /**
     * 使用标签传入的版本格式“/v[1-9]/”
     */
    private final static Pattern VERSION_PREFIX_PATTERN = Pattern.compile("v(\\d+)/");
    
    private int apiVersion;
    
    /**
     * 获得版本信息
     * @param apiVersion
     */
    public ApiVesrsionCondition(int apiVersion){
        this.apiVersion = apiVersion;
    }
    
    
    /**
     *
     * @param other
     * @return
     */
    @Override
    public ApiVesrsionCondition combine(ApiVesrsionCondition other) {
        // 采用最后定义优先原则，则方法上的定义覆盖类上面的定义
        return new ApiVesrsionCondition(other.getApiVersion());
    }
    
    @Override
    public ApiVesrsionCondition getMatchingCondition(HttpServletRequest request) {
        //检查URL中是否含有版本号
        Matcher m = VERSION_PREFIX_PATTERN.matcher(request.getRequestURL());
        if(m.find()){
            Integer version = Integer.valueOf(m.group(1));
            // 如果请求的版本号大于配置版本号， 则满足
            if(version >= this.apiVersion) {
                return this;
            }
        }
        return null;
    }
    
    /**
     * 比较传入版本号和当前版本号
     * @param other
     * @param request
     * @return
     */
    @Override
    public int compareTo(ApiVesrsionCondition other, HttpServletRequest request) {
        // 优先匹配最新的版本号
        return other.getApiVersion() - this.apiVersion;
    }
    
    /**
     * 传入的版本号
     * @return
     */
    public int getApiVersion() {
        return apiVersion;
    }
    
}
