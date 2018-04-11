package com.next.websocketcenter.core.config;

import com.next.optimus.common.trafficthrottling.service.BlackWhiteListService;
import com.next.optimus.common.trafficthrottling.service.RateLimitService;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Michael on 12/11/2017.
 * @author Michael
 */
@Configuration
@EnableAutoConfiguration
public class RateLimitConfiguration {
    
    @Bean
    public RateLimitService rateLimitService(){
        return new RateLimitService();
    }
    
    @Bean
    public BlackWhiteListService blackWhiteListService(){
        return new BlackWhiteListService();
    }
    
}
