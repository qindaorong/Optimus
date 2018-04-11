package com.next.optimus.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * EurekaApplication
 *
 * @author qindaorong
 * @date 2017/10/16
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaApplication extends SpringBootServletInitializer {
	
	@Override
	protected SpringApplicationBuilder configure(
		SpringApplicationBuilder application) {
		return application.sources(EurekaApplication.class);
	}
	
	public static void main(String[] args) {
		SpringApplication.run(EurekaApplication.class, args);
	}
}
