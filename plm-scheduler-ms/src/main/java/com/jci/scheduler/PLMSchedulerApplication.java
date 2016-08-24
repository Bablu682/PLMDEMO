/**
 * 
 */
package com.jci.scheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RestController;

@EnableAutoConfiguration
@ComponentScan
@EnableEurekaClient
@SpringBootApplication
@EnableDiscoveryClient

@RestController
public class PLMSchedulerApplication {

	public static void main(String[] args) {

		SpringApplication.run(PLMSchedulerApplication.class, args);

	}
}
