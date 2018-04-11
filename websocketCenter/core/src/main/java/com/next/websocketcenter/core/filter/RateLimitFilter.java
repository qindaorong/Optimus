package com.next.websocketcenter.core.filter;

import com.next.optimus.common.trafficthrottling.service.BlackWhiteListService;
import com.next.optimus.common.trafficthrottling.service.RateLimitService;
import java.io.IOException;
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

/**
 * Created by Michael on 12/11/2017.
 * @author Michael
 */
@WebFilter(filterName="RateLimitFilter",urlPatterns={"/ws/external/*"})
public class RateLimitFilter implements Filter {
    
    private static final Logger log = LoggerFactory.getLogger(RateLimitFilter.class);
    
    
    private static BlackWhiteListService blackWhiteListService = new BlackWhiteListService();
    
    
    private static RateLimitService rateLimitService = new RateLimitService();
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("RateLimitFilter init......");
    }
    
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
        throws IOException, ServletException {
    
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String ip = getRequestIp(request);
        if(blackWhiteListService.isInBlackList(ip)){
            response.sendError(HttpServletResponse.SC_FORBIDDEN,"exceed request frequency limitation");
            log.warn("[RateLimitFilter|inBlackList] ip = {}",ip);
            return;
        }
        if(!blackWhiteListService.isInWhiteList(ip)){
            if(!rateLimitService.canPass(ip)){
                response.sendError(HttpServletResponse.SC_FORBIDDEN,"exceed request frequency limitation");
                log.warn("[RateLimitFilter|cannotPass] ip = {}",ip);
                return;
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
    
    private String getRequestIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
    
    @Override
    public void destroy() {
        log.info("RateLimitFilter destroy.....");
    }
}
