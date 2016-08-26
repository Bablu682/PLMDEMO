package com.jci.storage.dao;

import java.util.HashMap;

public interface PLMStorageDao {
	
	
	String PutXmlBom(HashMap<String, Object> xml);

	public String setEntity() ;
	public String PutjsonBom(HashMap<String, Object> jsonXml);


//	String PutjsonBom(String json);

	
}
