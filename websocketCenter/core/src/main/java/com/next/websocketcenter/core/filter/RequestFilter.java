package com.next.websocketcenter.core.filter;

import com.next.optimus.common.util.StringUtil;
import com.next.websocketcenter.api.constants.FrameworkConstants;
import com.next.websocketcenter.api.util.WebSocketManager;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

/**
 * RequestFilter
 *
 * @author qindaorong
 * @create 2017-12-12 2:22 PM
 **/
@Order(9)
@WebFilter(filterName="RequestFilter",urlPatterns={"/singleUserChat","/multipleUsersChat","/chatBroadCastUserMessage"})
public class RequestFilter implements Filter {
    
    private static final Logger log = LoggerFactory.getLogger(RequestFilter.class);
    
    
    private final static String SECURITY_MSG = "the security code is incorrect";
    
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("RequestFilter init......");
    }
    
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
        throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        
        Map<String,String> headerMap = this.getHeaderMap(request);
        if(!checkVerifyCode(headerMap)){
            response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED,SECURITY_MSG);
            return ;
        }
    
        filterChain.doFilter(servletRequest, servletResponse);
    }
    
    @Override
    public void destroy() {
        log.info("RequestFilter destroy.....");
    }
    
    
    /**
     * 推送校验
     * @param headerMap
     * @return
     */
    private boolean checkVerifyCode(Map<String,String> headerMap){
        
        WebSocketManager webSocketManager = WebSocketManager.getWebSocketManager();
        String keyId = webSocketManager.getMessageValue(FrameworkConstants.ACCESS_KEY_ID);
        String accessKey = webSocketManager.getMessageValue(FrameworkConstants.SECRET_ACCESS_KEY);
        
        if(StringUtil.equals(keyId,headerMap.get(FrameworkConstants.ACCESS_KEY_ID)) && StringUtil.equals(accessKey,headerMap.get(FrameworkConstants.SECRET_ACCESS_KEY))){
            return true;
        }else{
            return false;
        }
    }
    
    
    /**
     * 获得用户验证信息
     * @param request
     * @return
     */
    private Map<String,String> getHeaderMap(HttpServletRequest request){
        Map<String,String> headerMap = new HashMap<>(2);
        
        String headerKey = request.getHeader(FrameworkConstants.ACCESS_KEY_ID);
        String headerValue = request.getHeader(FrameworkConstants.SECRET_ACCESS_KEY);
        
        headerMap.put(FrameworkConstants.ACCESS_KEY_ID,headerKey);
        headerMap.put(FrameworkConstants.SECRET_ACCESS_KEY,headerValue);
        
        return headerMap;
    }
}
