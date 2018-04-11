package com.next.websocketcenter.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


/**
 * WebsocketCenterCoreApplication
 *
 * @author qindaorong
 * @date 2017/11/14
 */
@SpringBootApplication(scanBasePackages = "com.next.websocketcenter.core")
@EnableDiscoveryClient
@ServletComponentScan
public class WebsocketCenterCoreApplication  extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(WebsocketCenterCoreApplication.class, args);
    }
    
    
    @Override
    protected SpringApplicationBuilder configure(
        SpringApplicationBuilder application) {
        return application.sources(WebsocketCenterCoreApplication.class);
    }
    
}
