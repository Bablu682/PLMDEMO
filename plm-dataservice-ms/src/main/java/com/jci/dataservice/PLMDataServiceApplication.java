package com.jci.dataservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jci.dataservice.service.PLMDataServiceMSService;
@EnableAutoConfiguration
@ComponentScan
//@EnableEurekaClient
@EnableDiscoveryClient
@SpringBootApplication
@EnableHystrix
@EnableHystrixDashboard
@EnableCircuitBreaker


@RestController
public class PLMDataServiceApplication {

	public static void main(String[] args) {
		
		SpringApplication.run(PLMDataServiceApplication.class, args);
		
		System.out.println("### Ending config.Application.main ####");
		
	}
	@Autowired
	PLMDataServiceMSService dataService;
	
	@RequestMapping(value = "/fallBack")
	public String hystrixCircuitBreaker(){
	
	dataService.hystrixCircuitBreaker();
		return "Success Execute Circuit Breaker in Data servce MS";
	}
	
}
