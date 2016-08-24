package com.jci.storage.domain;

import com.microsoft.windowsazure.services.table.client.TableServiceEntity;

public class PLMPayload extends TableServiceEntity{
	
	
	
	private String payload;
	
		public String getPayload() {
		return payload;
	}
	public void setPayload(String payload) {
		this.payload = payload;
	}
		public PLMPayload(String ecnNo, String ecnName) {
        this.partitionKey = ecnNo;
        this.rowKey = ecnName;
    }
	public PLMPayload() { }
}
