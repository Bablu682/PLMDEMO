package com.jci.partbom;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.jci.partbom.service.PLMPartBomService;

@SpringBootApplication
@EnableDiscoveryClient
@EnableEurekaClient
@EnableFeignClients
@RestController
@EnableHystrix
@EnableHystrixDashboard
@EnableCircuitBreaker

public class PLMPartBomApplication {
	public static void main(String[] args) {

		SpringApplication.run(PLMPartBomApplication.class, args);

	}
	
	
	@Autowired
	RestTemplate resttemplate;
	
	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}
	

		@Autowired
		private DiscoveryClient discoveryClient;
		@Autowired
		RestTemplate restTemplate;
		
		
		@RequestMapping("/service-instances/{applicationName}")
		public List<ServiceInstance> serviceInstancesByApplicationName(@PathVariable String applicationName) {
			return this.discoveryClient.getInstances(applicationName);
		} 


	@Autowired
	private PLMPartBomService partbomservice;

	@RequestMapping(value = "/bomcall")
	public String bomApiCallInApigee() {

		String name = partbomservice.bomApiCallInApigee();
		return name;

	}

	@RequestMapping(value = "/partcall")
	public String partApiCallInApigee() {

		String name = partbomservice.partApiCallInApigee();
		return name;

	}

	@RequestMapping(value = "/receiveJson", method = { RequestMethod.POST })
	public String jsonRecieveAndSend(@RequestBody HashMap<String, Object> jsonXml) throws Exception {

		System.out.println("Data reach at Bom ms from subcriber ms");
		System.out.println("===================PART=======================");
		System.out.println(jsonXml.get("part"));
		System.out.println("===================BOM=======================");
		System.out.println(jsonXml.get("bom"));
		System.out.println("===================JSON=======================");
		System.out.println(jsonXml.get("json"));

		 partbomservice.jsonSendToStorage(jsonXml);

		return " Successs fully send data to Storage Ms ";

	}
	@RequestMapping(value = "/fallBack")
	@ResponseBody
	public String hystrixCircuitBreaker(){
	
	String value=	partbomservice.hystrixCircuitBreaker();
	System.out.println("-------->Part Bom Get the Return from fallback    "+value);
	return "Successssss";
	}
	
	
	
}
