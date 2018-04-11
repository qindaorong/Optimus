package com.next.websocketcenter.core.config;

import com.next.websocketcenter.core.interceptor.BackUserWebSocketHandler;
import com.next.websocketcenter.core.interceptor.SystemWebSocketHandler;
import com.next.websocketcenter.core.interceptor.WebSocketHandshakeInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * NextWebsSocketConfig
 *
 * @author qindaorong
 * @create 2017-11-15 4:46 PM
 **/
@Configuration
@EnableWebMvc
@EnableWebSocket
public class NextWebsSocketConfig  extends WebMvcConfigurerAdapter implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        //setAllowedOrigins("*") 解决跨域问题
        registry.addHandler(systemWebSocketHandler(),"/userEndpoint") .addInterceptors(new
            WebSocketHandshakeInterceptor())
            .setAllowedOrigins("*");
    
    
        registry.addHandler(backUserWebSocketHandler(),"/broadcastEndpoint").addInterceptors(new WebSocketHandshakeInterceptor())
            .setAllowedOrigins("*");
    }
    
    @Bean
    public WebSocketHandler systemWebSocketHandler(){
        return new SystemWebSocketHandler();
    }
    
    @Bean
    public BackUserWebSocketHandler backUserWebSocketHandler(){
        return new BackUserWebSocketHandler();
    }

}
