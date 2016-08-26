package com.jci.storage.service;

import java.net.URI;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.jci.storage.PLMStrorageApplication;
import com.jci.storage.dao.PLMStorageDao;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class PLMStorageServiceImpl implements PLMStorageService {
	
	private static final Logger LOG = LoggerFactory.getLogger(PLMStorageServiceImpl.class);

	 @Bean
	  RestTemplate restTemplate(){
	    return new RestTemplate();
	  }
	@Autowired
	RestTemplate restTemplate;

	@Autowired
	PLMStorageDao plmStorageDao;
	
	public String PutXmlPartBom(HashMap<String, Object> xml) {
		
		LOG.info("Storage Service Implementation Execute");
		plmStorageDao.PutXmlBom(xml);	
		return null;

	}

	/*public String PutJsonBom(String json) {
		
		plmStorageDao.PutjsonBom(json);
		
		return null;
	}*/

	public String setEntity() {
		
		LOG.info("Flow Inside the Storage Implemention setEntity()");
		
		
	return plmStorageDao.setEntity();
		
	}
	
	
	@Override
	@HystrixCommand(fallbackMethod = "error")
	public String hystrixCircuitBreaker() {
		System.out.println("Inseide() call before fallBack");												

		return "Successfully execute";	}

	public String error() {
		System.out.println("fall back is call");
		return "fall back is call ";

	}
	
	
	
	}
