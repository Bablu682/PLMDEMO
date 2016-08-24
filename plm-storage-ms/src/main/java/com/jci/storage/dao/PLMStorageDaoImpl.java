package com.jci.storage.dao;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;

import org.springframework.stereotype.Repository;

import com.jci.storage.domain.PLMPayload;
import com.microsoft.windowsazure.services.blob.client.CloudBlobClient;
import com.microsoft.windowsazure.services.blob.client.CloudBlobContainer;
import com.microsoft.windowsazure.services.blob.client.CloudBlockBlob;
import com.microsoft.windowsazure.services.core.storage.CloudStorageAccount;
import com.microsoft.windowsazure.services.table.client.CloudTable;
import com.microsoft.windowsazure.services.table.client.CloudTableClient;
import com.microsoft.windowsazure.services.table.client.TableOperation;
@Repository
public class PLMStorageDaoImpl implements PLMStorageDao {
	
	public static final String storageConnectionString = "DefaultEndpointsProtocol=http;" + "AccountName=erpconnsample;"
			+ "AccountKey=GQZDOpTxJwebJU7n3kjT2VZP1mXCY6QXzVoCZGIsCdvU6rX7E8M5S24+Ki4aYqD2AwK1DnUh6ivlbaVKR7NOTQ==";

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

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/*@Override
	public String PutjsonBom(String json) {
		
		try {

			CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);
			// Create the table client.
			CloudTableClient tableClient = storageAccount.createCloudTableClient();

			// insert the json into azure table

			// Create the table if it doesn't exist.
			String tableName = "BomJsonStore";
			CloudTable cloudTable = tableClient.getTableReference(tableName);
			cloudTable.createIfNotExist();

			cloudTable = tableClient.getTableReference("BOMJSON1");

			PLMPayload header = new PLMPayload("ecnNo", "ecnName");
			header.setPayload(json);

			TableOperation insertCustomer1 = TableOperation.insertOrReplace(header);

			tableClient.execute(tableName, insertCustomer1);

		} catch (Exception e) {
			e.printStackTrace();
		}

		
		
		return null;
	}*/

	
}
