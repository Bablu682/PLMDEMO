package com.jci.portal.service;


public interface PLMWebportalService {
	
	public String reprocessRequest(String ecnNo,String completeXml);

	public String hystrixCircuitBreaker();
}
