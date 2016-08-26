package com.jci.payloadprocess;

import java.util.HashMap;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.jci.payloadprocess.service.PLMProcessPayloadService;


@SpringBootApplication
@EnableAutoConfiguration
@RestController
@EnableHystrix
@EnableHystrixDashboard
@EnableCircuitBreaker
@EnableDiscoveryClient
@EnableEurekaClient
public class PLMPayloadProcessMsApplication {

	private static final Logger LOG = LoggerFactory.getLogger(PLMPayloadProcessMsApplication.class);
	
	public static void main(String[] args) {
		
		SpringApplication.run(PLMPayloadProcessMsApplication.class, args);
	}
	@Autowired
	PLMProcessPayloadService process;
	
	@Autowired
	RestTemplate resttemplate;
	
	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}
	@Autowired
	RestTemplate restTemplate;
	

		@Autowired
		private DiscoveryClient discoveryClient;
		
		
		@RequestMapping("/service-instances/{applicationName}")
		public List<ServiceInstance> serviceInstancesByApplicationName(@PathVariable String applicationName) {
			return this.discoveryClient.getInstances(applicationName);
		} 

	
	//the below method is called from subscriber ms 
			@RequestMapping(value = "/receiveXml", method = { RequestMethod.POST })
		    
			public  String  processPayload(@RequestBody String xmlPayload, HttpServletRequest request) 
			{
				LOG.info("Payload Process MS Controller is Executing");
				
				String xml = request.getParameter("xml");
				String ecnNo="1234";
				LOG.info("EcnNo is "+ecnNo);
				LOG.info("XML At Payload Process MS");
				LOG.info("========================================");
				LOG.info(xml);
				try
				{
					process.processPayload(xml, ecnNo);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				return "Success Node js";
			}
	
	
	
	//the below method is called from the UI (It would be a rest call)
		@RequestMapping(value = "/reprocess", method = { RequestMethod.POST })
		public  String  reprocessPayload(@RequestBody HashMap<String, String> hashMap) 
		{
		try
			{
			
				LOG.info("Payload Process MS Controller is Executing");
				String completeXml=hashMap.get("completeXml");
				String ecnNo=hashMap.get("ecnNo");
				LOG.info("EcnNo is "+ecnNo);
				LOG.info("XML At Payload Process MS");
				LOG.info("========================================");
				LOG.info(completeXml);
				
				
				process.processPayload(completeXml, ecnNo);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			return null;
		}
		@RequestMapping(value = "/fallBack")
		public String hystrixCircuitBreaker(){
		
		String value=	process.hystrixCircuitBreaker();
		
			return "Success";
		}
				
}
