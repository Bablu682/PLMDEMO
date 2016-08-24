package com.jci.subscriber.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name="plm-process-ms",url="http://10.109.218.6:9090")
public interface PLMSubscriberMSFeignClient {
	
	@RequestMapping(method = RequestMethod.POST, value="/receiveXml")
	public String sendData(@RequestBody  String xmlPayload);

}
