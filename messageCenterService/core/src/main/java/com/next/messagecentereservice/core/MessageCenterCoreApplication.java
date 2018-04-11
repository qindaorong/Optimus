package com.next.messagecentereservice.core;

import com.next.messagecentereservice.core.kafkaconfig.ProducerConfig;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


/**
 * MessageCenterCoreApplication
 *
 * @author qindaorong
 * @date 2017/10/16
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableHystrix
@EnableHystrixDashboard
public class MessageCenterCoreApplication  extends SpringBootServletInitializer {
    
    
    @Override
    protected SpringApplicationBuilder configure(
        SpringApplicationBuilder application) {
        return application.sources(MessageCenterCoreApplication.class);
    }
    
    @Bean
    public Producer<String, String> getProducer(){
        return ProducerConfig.getProducerConfig();
    }
    
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                    .allowedOrigins("*")
                    .allowedMethods("PUT", "DELETE","GET","POST")
                    .allowedHeaders("*")
                    .exposedHeaders("access-control-allow-headers",
                        "access-control-allow-methods",
                        "access-control-allow-origin",
                        "access-control-max-age",
                        "X-Frame-Options")
                    .allowCredentials(false).maxAge(3600);
            }
        };
        
    }
    
    
    
    public static void main(String[] args) {
        SpringApplication.run(MessageCenterCoreApplication.class, args);
    }
}
