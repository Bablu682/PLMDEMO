package com.jci.partbom;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;

import java.util.List;

import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RestController
@EnableHystrix
@EnableHystrixDashboard
@EnableCircuitBreaker

public class PLMPartBomApplication {
	
	private static final Logger LOG = LoggerFactory.getLogger(PLMPartBomApplication.class);	
	
	
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
		LOG.info("Part-Bom Ms Controller is Executing");

		System.out.println("Data reach at Bom ms from subcriber ms");
		System.out.println("===================PART=======================");
		System.out.println(jsonXml.get("part"));
		System.out.println("===================BOM========================");
		System.out.println(jsonXml.get("bom"));
		System.out.println("===================JSON=======================");
		System.out.println(jsonXml.get("json"));
		
		LOG.info("JSON Received at PART-BOM MS");
		LOG.info("=========================================================");
		LOG.info("PART JSON"+jsonXml.get("part"));
		
		LOG.info("=========================================================");
		LOG.info("BOM JSON"+jsonXml.get("bom"));
		
		LOG.info("=========================================================");
		LOG.info("INFO JSON"+jsonXml.get("json"));
		
		
		JSONParser parser = new JSONParser();
		URL git = new URL("http://apidev1.jci.com:9055/jcibe/v1/suppliercollaboration/purchaseorders?erpname=SYMIX&region=ASIA&plant=RY1&ordernumber=**&ordercreationdate=**");
		Object obj=parser.parse(new InputStreamReader(git.openStream()));
		 JSONObject jsonObj = new JSONObject(obj.toString());
		System.out.println(jsonObj.get("code"));
		System.out.println(jsonObj.get("status"));
		System.out.println(jsonObj.get("message"));
		System.out.println(jsonObj.get("date")); 
		
		jsonXml.put("code", jsonObj.get("code"));
		jsonXml.put("status", jsonObj.get("status"));
		jsonXml.put("message", jsonObj.get("message"));
		jsonXml.put("date", jsonObj.get("date"));
		
//		 partbomservice.partApiCallInApigee();
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
	
	
	
	public static void main(String[] args) {
		
		SpringApplication.run(PLMPartBomApplication.class, args);
		
	}
}
