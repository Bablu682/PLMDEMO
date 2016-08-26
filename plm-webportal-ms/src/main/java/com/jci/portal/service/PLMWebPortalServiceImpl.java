package com.jci.portal.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.jci.portal.PLMWebPortalApplication;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@Service
public class PLMWebPortalServiceImpl implements PLMWebportalService {

	private static final Logger LOG = LoggerFactory.getLogger(PLMWebPortalServiceImpl.class);

	// @LoadBalanced
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

	@Override
	public String reprocessRequest(String ecnNo, String completeXml) {

		// sending to payload-process ms
		URI partbomUri;
		try {
			LOG.info("Web Portal MS Service is Executing");

			List<ServiceInstance> serviceInstance = discoveryClient.getInstances("plm-payloadprocess-ms");
			ServiceInstance bomInstance = serviceInstance.get(0);
			String urlString = "http://" + bomInstance.getHost() + ":" + Integer.toString(bomInstance.getPort())
					+ "/reprocess";
			HashMap<String, String> mvm = new HashMap<String, String>();
			mvm.put("ecnNo", ecnNo.toString());
			mvm.put("completeXml", completeXml);
			LOG.info("XMl is Sent to Payload Process MS");

			Map result = restTemplate.postForObject(urlString, mvm, Map.class);

		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return null;
	}

	@Override
	@HystrixCommand(fallbackMethod = "error")
	public String hystrixCircuitBreaker() {
		System.out.println("Inseide() call before fallBack");
		LOG.info("Flow inside Web microservice service impl");
		return "Web Microservice Successfully execute";
	}

	public String error() {
		System.out.println("fall back is call");
		return "fall back is call ";

	}

}
