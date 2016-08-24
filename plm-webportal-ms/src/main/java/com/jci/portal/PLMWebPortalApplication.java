/**
 * 
 */
package com.jci.portal;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URI;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.jci.portal.service.PLMWebportalService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@EnableDiscoveryClient
@EnableEurekaClient
@EnableFeignClients
@SpringBootApplication
@EnableHystrix
@EnableCircuitBreaker
@EnableHystrixDashboard
@RestController
public class PLMWebPortalApplication {


	@Autowired
	PLMWebportalService ui;

	
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

	
	
	// here need to confirmed about the transaction number
	@RequestMapping(value = "/ErrorTriggered", method = { RequestMethod.GET })
	public String reprocessRequest() throws Exception {
		String ecnNo;
		System.out.println("Webmicroservice controller ");
		// here implemented temporary XML from local
		// here we need to take a XML from azure storage to re-process
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		InputStream inputStream = new FileInputStream(new File("Payload.xml"));
		org.w3c.dom.Document doc = documentBuilderFactory.newDocumentBuilder().parse(inputStream);
		StringWriter stw = new StringWriter();
		Transformer serializer = TransformerFactory.newInstance().newTransformer();
		serializer.transform(new DOMSource(doc), new StreamResult(stw));
		// here implementing temporary Hard coded enNo
		ecnNo = "1234";

		ui.reprocessRequest(ecnNo, stw.toString());
		return "ecn-nu";
	}

	@RequestMapping(value ="fallBack")
	public String hystrixCircuitBreaker() {

		String value = ui.hystrixCircuitBreaker();
		return "Circuit Breaker success in WebPortal Microservice";
	}

	public static void main(String[] args) {

		SpringApplication.run(PLMWebPortalApplication.class, args);

	}

}
