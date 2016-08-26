package com.jci.partbom.service;

import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.URIException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.jci.partbom.PLMPartBomApplication;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class PLMPartBomServiceImpl implements PLMPartBomService {
	
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

	public String bomApiCallInApigee() {
		
		// Apigee API Url
		String uri = "http://johnsoncontroll-test.apigee.net/v1/hello_policies";
		RestTemplate restTemplate = new RestTemplate();

		String result = restTemplate.getForObject(uri, String.class);

		System.out.println(result.getBytes());

		System.out.println(result);
		return result;


	}

	public String partApiCallInApigee() {
		
		/*//String uri2 = "http://apidev1.jci.com:9055/jcibe/v1/suppliercollaboration/purchaseorders?erpname=SYMIX&region=ASIA&plant=RY1&ordernumber=**&ordercreationdate=**";
		LOG.info("PartApiCall() Execute");
		System.out.println("Call given");
		String uri = "http://johnsoncontroll-test.apigee.net/v1/hello_policies";
		RestTemplate restTemplate = new RestTemplate();

		JSONObject result2 = restTemplate.getForObject(uri, JSONObject.class);
	//	JSONObject result2 = restTemplate.getForObject(uri2, JSONObject.class);
		System.out.println(result2);

		System.out.println(result2.get("code"));
		System.out.println(result2.get("status"));
		System.out.println(result2.get("message"));
		System.out.println(result2.get("date")); 	
		System.out.println(result2.get("city"));
		return null;
	//	return result2.toString();
*/
	
		// we write at the controller insted
		try {
		
		JSONParser parser = new JSONParser();
			URL git = new URL("http://apidev1.jci.com:9055/jcibe/v1/suppliercollaboration/purchaseorders?erpname=SYMIX&region=ASIA&plant=RY1&ordernumber=**&ordercreationdate=**");
			Object obj=parser.parse(new InputStreamReader(git.openStream()));
			 JSONObject jsonObj = new JSONObject(obj.toString());
			System.out.println(jsonObj.get("code"));
			System.out.println(jsonObj.get("status"));
			System.out.println(jsonObj.get("message"));
			System.out.println(jsonObj.get("date")); 		
		
		} catch (Exception e) {
			
			e.printStackTrace();
		
		}
		
		return "success";
		
	}

	

	@Override
	public String jsonSendToStorage(HashMap<String, Object> jsonXml) {

		LOG.info("PART-BOM ms Service Is executing");
		
		//sending to storage- ms
		
					String storageUri;
					try 
					{
					//http://apidev1.jci.com:9055/jcibe/v1/suppliercollaboration/purchaseorders?erpname=SYMIX&region=ASIA&plant=RY1&ordernumber=**&ordercreationdate=**
					List<ServiceInstance> serviceInstance = discoveryClient.getInstances("plm-storage-ms");
					ServiceInstance bomInstance = serviceInstance.get(0);
					storageUri = "http://" + bomInstance.getHost() + ":" + Integer.toString(bomInstance.getPort())
					+ "/sendJsonStorage";
					 Map result = restTemplate.postForObject( storageUri, jsonXml , Map.class); 
				LOG.info("JSON Is send to Storage MS");	 
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}

		
		return null;
	}

	@Override
	@HystrixCommand(fallbackMethod = "error")
	public String hystrixCircuitBreaker() {
					System.out.println("Inseide() call before fallBack");												

		return "Successfully execute";
	}

	public String error() {
		System.out.println("fall back is call");
		return "fall back is call ";

	}

}
