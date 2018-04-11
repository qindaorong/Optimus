package com.next.optimus.test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * WebConfig
 *
 * @author qindaorong
 * @create 2017-11-16 10:20 AM
 **/
@Configuration
public class WebConfig extends WebMvcConfigurationSupport {
    @Override
    @Bean
    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
        RequestMappingHandlerMapping handlerMapping = new CustomRequestMappingHandlerMapping();
        handlerMapping.setOrder(0);//拦截顺序
        handlerMapping.setInterceptors(getInterceptors());//添加springMVC默认拦截器
        return handlerMapping;
    }
}
