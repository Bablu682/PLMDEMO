/**
 * 
 */
package com.jci.storage;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jci.storage.dao.PLMStorageDao;
import com.jci.storage.service.PLMStorageService;

@SpringBootApplication
@RestController
@EnableHystrix
@EnableHystrixDashboard
@EnableCircuitBreaker
@EnableDiscoveryClient
@EnableEurekaClient
public class PLMStrorageApplication {

	private static final Logger LOG = LoggerFactory.getLogger(PLMStrorageApplication.class);
	// given by subcriber ms
	@Autowired
	private PLMStorageService storageservice;
	@Autowired
	private PLMStorageDao storagedao;

	@RequestMapping(value = "/receiveXml", method = { RequestMethod.POST })
	public String recievedXmlFromSubscriber(@RequestBody HashMap<String, Object> s1) throws Exception {
		LOG.info("Storage MS Controller is Executing");
		LOG.info("XML From Payload Process MS");
		LOG.info("===================================================================");
		LOG.info(s1.get("xml").toString());
		System.out.println("Data from Payload Process recived at storage");
		System.out.println("-------------------------------------------------------------");
		System.out.println(s1.get("xml"));
		return storageservice.PutXmlPartBom(s1);

	}

	// given by PART-BOM ms
	@RequestMapping(value = "/sendJsonStorage", method = { RequestMethod.POST })
	public String recievedJson(@RequestBody HashMap<String, Object> jsonXml) throws Exception {

		LOG.info("Storage MS Controller is Executing");

		System.out.println("Storage receiveFromPBMS triggered");

		LOG.info("JSON at Storage MS");
		LOG.info("=========================================================");
		LOG.info("Part       " + jsonXml.get("part").toString());
		LOG.info("Bom        " + jsonXml.get("bom").toString());
		LOG.info("Info Json  " + jsonXml.get("json").toString());
		System.out.println("Data reach at Storage ms from PART-BOM ms");
		System.out.println("===================PART=======================");
		System.out.println(jsonXml.get("part"));
		System.out.println("===================BOM=======================");
		System.out.println(jsonXml.get("bom"));
		System.out.println("===================JSON=======================");
		System.out.println(jsonXml.get("json"));
		
		System.out.println("===================Apigee Data=======================");
		System.out.println(jsonXml.get("code"));
		System.out.println(jsonXml.get("status"));
		System.out.println(jsonXml.get("message"));
		System.out.println(jsonXml.get("date"));
		storagedao.PutjsonBom(jsonXml);

		return null;

	}

	@RequestMapping(value = "/fallBack")
	public String hystrixCircuitBreaker() {
		storageservice.hystrixCircuitBreaker();
		return "Success";
	}

	@RequestMapping(value = "/insertData")
	public String insertDataInAzureTable() {
		storageservice.setEntity();
		return "successfully inserted data";
	}

	public static void main(String[] args) {

		SpringApplication.run(PLMStrorageApplication.class, args);

	}
}
