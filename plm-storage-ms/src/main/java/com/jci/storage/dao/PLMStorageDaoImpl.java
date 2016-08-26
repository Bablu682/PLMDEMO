package com.jci.storage.dao;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.jci.storage.domain.PLMPayload;
import com.jci.storage.domain.PlmStorageEntity;
import com.jci.storage.service.PLMStorageServiceImpl;
import com.microsoft.windowsazure.services.blob.client.CloudBlobClient;
import com.microsoft.windowsazure.services.blob.client.CloudBlobContainer;
import com.microsoft.windowsazure.services.blob.client.CloudBlockBlob;
import com.microsoft.windowsazure.services.core.storage.CloudStorageAccount;
import com.microsoft.windowsazure.services.core.storage.StorageException;
import com.microsoft.windowsazure.services.table.client.CloudTable;
import com.microsoft.windowsazure.services.table.client.CloudTableClient;
import com.microsoft.windowsazure.services.table.client.TableOperation;
@Repository
public class PLMStorageDaoImpl implements PLMStorageDao {
	
	private static final Logger LOG = LoggerFactory.getLogger(PLMStorageDaoImpl.class);

	CloudBlobContainer blobContainer = null;
	CloudTableClient tableClient = null;

	
	public static final String storageConnectionString = "DefaultEndpointsProtocol=http;" + "AccountName=erpconnsample;"
			+ "AccountKey=GQZDOpTxJwebJU7n3kjT2VZP1mXCY6QXzVoCZGIsCdvU6rX7E8M5S24+Ki4aYqD2AwK1DnUh6ivlbaVKR7NOTQ==";

	public CloudTableClient getTableClientReference()
			throws RuntimeException, IOException, IllegalArgumentException, URISyntaxException, InvalidKeyException {

		// Retrieve the connection string
		Properties prop = new Properties();
		try {
			InputStream propertyStream = PLMStorageDaoImpl.class.getClassLoader()
					.getResourceAsStream("config.properties");
			if (propertyStream != null) {
				prop.load(propertyStream);
			} else {
				throw new RuntimeException();
			}
		} catch (RuntimeException | IOException e) {
			System.out.println("\nFailed to load config.properties file.");
			throw e;
		}

		CloudStorageAccount storageAccount;
		try {
			storageAccount = CloudStorageAccount.parse(prop.getProperty("azureStorageTableConnectionString"));
		} catch (IllegalArgumentException | URISyntaxException e) {
			System.out.println("\nConnection string specifies an invalid URI.");
			System.out.println("Please confirm the connection string is in the Azure connection string format.");
			throw e;
		} catch (InvalidKeyException e) {
			System.out.println("\nConnection string specifies an invalid key.");
			System.out.println("Please confirm the AccountName and AccountKey in the connection string are valid.");
			throw e;
		}

		return storageAccount.createCloudTableClient();
	}



	
	
	
	
	
	@Override
	public String PutXmlBom(HashMap<String, Object> xml) {
		
		
		
		
		try {

			File file = new File("PayloadForBlob.xml");
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
		
			BufferedWriter bw = new BufferedWriter(fw);
			
		
			bw.write(xml.get("xml").toString());
		
			bw.close();

			CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);
			storageAccount.createCloudTableClient();

			CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
			CloudBlobContainer container = blobClient.getContainerReference("plmcontainer2");
			container.createIfNotExist();
			String filePath = "PayloadForBlob.xml";
			CloudBlockBlob blob = container.getBlockBlobReference("PayloadForBlob.xml");
			java.io.File source = new java.io.File(filePath);
			java.io.FileInputStream fileInputStream = new java.io.FileInputStream(source);
			blob.upload(fileInputStream, source.length());
			
			LOG.info("XML File Stored In Azure Blob");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Xml File Successfully Stored in Azure Blob";
	}

	
	
	
	
	
	
		@SuppressWarnings("null")
	public boolean createAzureTableIfNotExists(CloudTableClient tableClient, String azureStorageTableName) {
		CloudTable table = null;
		try {
			table = tableClient.getTableReference(azureStorageTableName);
		} catch (URISyntaxException | StorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (table == null) {
			System.out.println("Created new table since it exist");
			try {
				table.createIfNotExist();
				return true;
			} catch (StorageException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		} else {
			System.out.println("Table already exists");
		}
		return true;

	}
	public boolean insertReplaceAzureTableEntity(CloudTableClient tableClient, String tableName) {

		try {
			/*JSONParser jp = new JSONParser();
			Object object = jp.parse(new FileReader("C:\\BOM.json"));
			JSONObject jso = (JSONObject) object;
			System.out.println(jso.toJSONString());*/
			PlmStorageEntity entity=new PlmStorageEntity("SAP_PO","0001");
			//BOM entity = new BOM("SAP_PO", "0001");
			
			entity.setErrorMsg("errorMessage");
			entity.setIsErrored(true);
			entity.setIsProcessed(true);
			entity.setIsProcessedBy("admin");
			entity.setCreatedDate(new Date());
			entity.setModifiedDate(new Date());
			entity.setProcessDate(new Date());
			entity.setECNNumber("1111");
			entity.setDescription("CUMMINS PART NPI");
			entity.setPlant("Reynosa");
			entity.setStatus(3);
			entity.setERPName("SAP");
			entity.setECNRequestor("Administrator");
			entity.setError("401");
			entity.setPtcAck(true);
			entity.setPtcAckmsg("success");
			entity.setPtcAckSentDate(new Date());
			entity.setRegion("Reynosa");
			entity.setTxnID(7);
			entity.setUIReprocessing("uIReprocessing");
			entity.setUIReprocessingDate(new Date());
			entity.setInputXMLEtag("cccbbdcbwe");
			entity.setOutputXMLEtag("cccbbdcghr");
			entity.setBomError(true);
			entity.setBomErrorMsg("error");
			entity.setBomPayloadProcessed(false);
			entity.setBomPayloadProcessedDate(new Date());
			entity.setPartError(true);
			entity.setPartErrorMsg("error");
			entity.setPartPayloadProcessed(false);
			entity.setPartPayloadProcessedDate(new Date());
			// entity.setPayload(jso.toJSONString());
			//entity.setPayload(jso.toJSONString());

			TableOperation insertSample = TableOperation.insertOrReplace(entity);
			// Call execute method on table client
			// so as to perform the operation

			tableClient.execute(tableName, insertSample);
			return true;
		} catch (StorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}
	
	@Override
	public String setEntity() {
		PlmStorageEntity sampleEntity = null;
		CloudTableClient tableClient = null;
		try {
			tableClient = getTableClientReference();

			if (tableClient != null) {

				boolean createTable = createAzureTableIfNotExists(tableClient, "ControllesPLMTable");
				if (createTable) {
					// return azureSBCService.azureMessageSubscriber(service);
					// Adding a entity to table
					insertReplaceAzureTableEntity(tableClient, "ControllesPLMTable");
					System.out.println("table entities read successfully");
				} else {
					// return "Enity addition to table " + "sample" + "failed";
					// System.out.println("entity creation failed");
					System.out.println("table entities read failed");
				}

			} else {
				System.out.println("Table client returned to controller was null");
				return "Table client returned to controller was null";
			}
		} catch (InvalidKeyException | RuntimeException | URISyntaxException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "check logs";

	}
	
	
	public String PutjsonBom(HashMap<String, Object> jsonXml) {
		
		try {
			
			System.out.println("reach to dao of storage at putjsonBom");

			CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);
			// Create the table client.
			CloudTableClient tableClient = storageAccount.createCloudTableClient();

			// insert the json into azure table

			// Create the table if it doesn't exist.
			String tableName = "ApigeeDataPayload";
			CloudTable cloudTable = tableClient.getTableReference(tableName);
			cloudTable.createIfNotExist();

			cloudTable = tableClient.getTableReference("BOMJSON1");

			//Setting the entity for azure table
			PLMPayload header = new PLMPayload("ecnNo", "ecnName");
			header.setCode(jsonXml.get("code").toString());
			header.setStatus(jsonXml.get("status").toString());
			header.setDate(jsonXml.get("date").toString());
			header.setMessage(jsonXml.get("message").toString());
			/*if(jsonXml.get("code").toString()=="200")
			{
				header.setIsErrored(false);
			}
			else
			{
				header.setIsErrored(true);
			}
			*/
			header.setERPName(jsonXml.get("erp").toString());
			header.setRegion(jsonXml.get("region").toString());
			header.setPlant(jsonXml.get("plant").toString());
			
			Date date = new Date();
			header.setProcessDate(date);

			TableOperation insertCustomer1 = TableOperation.insertOrReplace(header);

			tableClient.execute(tableName, insertCustomer1);

		} catch (Exception e) {
			e.printStackTrace();
		}

		
		
		return null;
	}

	
}
