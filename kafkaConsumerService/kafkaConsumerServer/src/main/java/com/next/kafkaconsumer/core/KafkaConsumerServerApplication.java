package com.next.kafkaconsumer.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * KafkaConsumerServerApplication
 *
 * @author qindaorong
 * @date 2017/10/16
 */
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages={"com.next.consumer.core","com.next.kafkaconsumer.core"})
public class KafkaConsumerServerApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(KafkaConsumerServerApplication.class);
		application.run(args);
	}
	
	@Override
	protected SpringApplicationBuilder configure(
		SpringApplicationBuilder application) {
		return application.sources(KafkaConsumerServerApplication.class);
	}
}
