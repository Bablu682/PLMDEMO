package com.jci.storage.domain;

import java.util.Date;

import com.microsoft.windowsazure.services.table.client.TableServiceEntity;

public class PlmStorageEntity extends TableServiceEntity {

	private Boolean IsProcessed;
	private String ErrorMsg;
	// private Integer errorCode;
	private Boolean IsErrored;
	private Date ProcessDate;
	private Date CreatedDate;
	private Date ModifiedDate;
	private String IsProcessedBy;
	// private String Payload;
	private String ECNNumber;
	private String Description;
	private String Plant;
	private Integer Status;
	private String ERPName;
	private String ECNRequestor;
	private String Error;
	private Boolean PtcAck;
	private String PtcAckmsg;
	private Date PtcAckSentDate;
	private String Region;
	private long TxnID;
	private String UIReprocessing;
	private Date UIReprocessingDate;
	private String InputXMLEtag;
	private String OutputXMLEtag;
	private Boolean BomError;
	private String BomErrorMsg;
	private Boolean BomPayloadProcessed;
	private Date BomPayloadProcessedDate;
	private Boolean PartError;
	private String PartErrorMsg;
	private Boolean PartPayloadProcessed;
	private Date PartPayloadProcessedDate;
	
	
	
	public PlmStorageEntity(String partitionKey, String rowKey) {
		this.partitionKey = "SAP_PO";
		this.rowKey = rowKey;

	}

	public long getTxnID() {
		return TxnID;
	}

	public void setTxnID(long txnID) {
		TxnID = txnID;
	}

	public Boolean getBomError() {
		return BomError;
	}

	public void setBomError(Boolean bomError) {
		BomError = bomError;
	}

	public String getBomErrorMsg() {
		return BomErrorMsg;
	}

	public void setBomErrorMsg(String bomErrorMsg) {
		BomErrorMsg = bomErrorMsg;
	}

	public Boolean getBomPayloadProcessed() {
		return BomPayloadProcessed;
	}

	public void setBomPayloadProcessed(Boolean bomPayloadProcessed) {
		BomPayloadProcessed = bomPayloadProcessed;
	}

	public Date getBomPayloadProcessedDate() {
		return BomPayloadProcessedDate;
	}

	public void setBomPayloadProcessedDate(Date bomPayloadProcessedDate) {
		BomPayloadProcessedDate = bomPayloadProcessedDate;
	}

	public Boolean getPartError() {
		return PartError;
	}

	public void setPartError(Boolean partError) {
		PartError = partError;
	}

	public String getPartErrorMsg() {
		return PartErrorMsg;
	}

	public void setPartErrorMsg(String partErrorMsg) {
		PartErrorMsg = partErrorMsg;
	}

	public Boolean getPartPayloadProcessed() {
		return PartPayloadProcessed;
	}

	public void setPartPayloadProcessed(Boolean partPayloadProcessed) {
		PartPayloadProcessed = partPayloadProcessed;
	}

	public Date getPartPayloadProcessedDate() {
		return PartPayloadProcessedDate;
	}

	public void setPartPayloadProcessedDate(Date partPayloadProcessedDate) {
		PartPayloadProcessedDate = partPayloadProcessedDate;
	}

	public PlmStorageEntity() {

	}

	public Boolean getIsProcessed() {
		return IsProcessed;
	}

	public void setIsProcessed(Boolean isProcessed) {
		IsProcessed = isProcessed;
	}

	public String getErrorMsg() {
		return ErrorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		ErrorMsg = errorMsg;
	}

	public Boolean getIsErrored() {
		return IsErrored;
	}

	public void setIsErrored(Boolean isErrored) {
		IsErrored = isErrored;
	}

	public Date getProcessDate() {
		return ProcessDate;
	}

	public void setProcessDate(Date processDate) {
		ProcessDate = processDate;
	}

	public Date getCreatedDate() {
		return CreatedDate;
	}

	public void setCreatedDate(Date createdDate) {
		CreatedDate = createdDate;
	}

	public Date getModifiedDate() {
		return ModifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		ModifiedDate = modifiedDate;
	}

	public String getIsProcessedBy() {
		return IsProcessedBy;
	}

	public void setIsProcessedBy(String isProcessedBy) {
		IsProcessedBy = isProcessedBy;
	}

	public String getECNNumber() {
		return ECNNumber;
	}

	public void setECNNumber(String eCNNumber) {
		ECNNumber = eCNNumber;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getPlant() {
		return Plant;
	}

	public void setPlant(String plant) {
		Plant = plant;
	}

	public Integer getStatus() {
		return Status;
	}

	public void setStatus(Integer status) {
		Status = status;
	}

	public String getERPName() {
		return ERPName;
	}

	public void setERPName(String eRPName) {
		ERPName = eRPName;
	}

	public String getECNRequestor() {
		return ECNRequestor;
	}

	public void setECNRequestor(String eCNRequestor) {
		ECNRequestor = eCNRequestor;
	}

	public String getError() {
		return Error;
	}

	public void setError(String error) {
		Error = error;
	}

	public Boolean getPtcAck() {
		return PtcAck;
	}

	public void setPtcAck(Boolean ptcAck) {
		PtcAck = ptcAck;
	}

	public String getPtcAckmsg() {
		return PtcAckmsg;
	}

	public void setPtcAckmsg(String ptcAckmsg) {
		PtcAckmsg = ptcAckmsg;
	}

	public Date getPtcAckSentDate() {
		return PtcAckSentDate;
	}

	public void setPtcAckSentDate(Date ptcAckSentDate) {
		PtcAckSentDate = ptcAckSentDate;
	}

	public String getRegion() {
		return Region;
	}

	public void setRegion(String region) {
		Region = region;
	}

	

	public String getUIReprocessing() {
		return UIReprocessing;
	}

	public void setUIReprocessing(String uIReprocessing) {
		UIReprocessing = uIReprocessing;
	}

	public Date getUIReprocessingDate() {
		return UIReprocessingDate;
	}

	public void setUIReprocessingDate(Date uIReprocessingDate) {
		UIReprocessingDate = uIReprocessingDate;
	}

	public String getInputXMLEtag() {
		return InputXMLEtag;
	}

	public void setInputXMLEtag(String inputXMLEtag) {
		InputXMLEtag = inputXMLEtag;
	}

	public String getOutputXMLEtag() {
		return OutputXMLEtag;
	}

	public void setOutputXMLEtag(String outputXMLEtag) {
		OutputXMLEtag = outputXMLEtag;
	}

}
