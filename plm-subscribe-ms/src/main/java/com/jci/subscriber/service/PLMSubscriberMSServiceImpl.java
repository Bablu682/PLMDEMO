package com.jci.subscriber.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.microsoft.windowsazure.Configuration;
import com.microsoft.windowsazure.exception.ServiceException;
import com.microsoft.windowsazure.services.serviceBus.models.ReceiveMessageOptions;
import com.microsoft.windowsazure.services.serviceBus.models.ReceiveMode;
import com.microsoft.windowsazure.services.servicebus.ServiceBusConfiguration;
import com.microsoft.windowsazure.services.servicebus.ServiceBusContract;
import com.microsoft.windowsazure.services.servicebus.ServiceBusService;
import com.microsoft.windowsazure.services.servicebus.models.BrokeredMessage;
import com.microsoft.windowsazure.services.servicebus.models.ReceiveQueueMessageResult;

@Service
public class PLMSubscriberMSServiceImpl implements PLMSubscriberMSService {


	@LoadBalanced
	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Autowired
	RestTemplate restTemplate;
	@Autowired
	PLMSubscriberMSFeignClient process;
	
	

	private static String namespace = "";
	private static String sasPolicyKeyName = "";
	private static String sasPolicyKey = "";
	private static String serviceBusRootUri = "";
	private static String queueName = "";
	public static String completeXml = "";
	public static String payLoadType = "";
	public static String ecnNo="";
	public static int reProcess;

	public ServiceBusContract azureConnectionSetup() {
		Properties prop = new Properties();
		try {
			InputStream propertyStream = PLMSubscriberMSServiceImpl.class.getClassLoader()
					.getResourceAsStream("config.properties");
			if (propertyStream != null) {
				prop.load(propertyStream);
			} else {
				throw new RuntimeException();
			}
		} catch (IOException e) {
			System.out.println("\nFailed to load config.properties file.");
		} catch (RuntimeException e) {
			System.out.println("\nFailed to load config.properties file.");
			throw e;
		}
		namespace = prop.getProperty("namespace") == null ? "sbplmdev" : prop.getProperty("namespace");
		sasPolicyKeyName = prop.getProperty("sasPolicyKeyName") == null ? "RootManageSharedAccessKey"
				: prop.getProperty("sasPolicyKeyName");
		sasPolicyKey = prop.getProperty("sasPolicyKey") == null ? "Y3X4ESQe5yP6ZOmz7zB3rbTgRUSEPlvRyLy9LgTbGls="
				: prop.getProperty("sasPolicyKey");
		serviceBusRootUri = prop.getProperty("serviceBusRootUri") == null ? ".servicebus.windows.net"
				: prop.getProperty("serviceBusRootUri");
		queueName = prop.getProperty("queueName") == null ? "sbqplmdev" : prop.getProperty("queueName");
		Configuration config = ServiceBusConfiguration.configureWithSASAuthentication(namespace, sasPolicyKeyName,
				sasPolicyKey, serviceBusRootUri);
		ServiceBusContract service = ServiceBusService.create(config);
		return service;
	}

	public boolean azureMessagePublisher(ServiceBusContract service, String message) {

		try {
			BrokeredMessage brokeredMessage = new BrokeredMessage(message);
			service.sendQueueMessage(queueName, brokeredMessage);
		} catch (ServiceException e) {
			System.out.print("ServiceException encountered: ");
			System.out.println(e.getMessage());
			System.exit(-1);
		}
		return true;

	}
	
	
	//receiving code with resttemplate
	/*@RequestMapping(value = "/receiver" , method = { RequestMethod. POST })
    public String processUploadFile(
           @RequestParam (value = "param1" ) String param1){

         System. out .println("Hello from " + param1 );


       return null ;
   }
*/

	public String azureMessageSubscriber(ServiceBusContract service) {

		try {

			ReceiveMessageOptions opts = ReceiveMessageOptions.DEFAULT;
			opts.setReceiveMode(ReceiveMode.PEEK_LOCK);
			while (true) {
				ReceiveQueueMessageResult resultQM = service.receiveQueueMessage(queueName);
				BrokeredMessage message = resultQM.getValue();
				if (message != null && message.getMessageId() != null) {
					System.out.println("MessageID: " + message.getMessageId());

					System.out.print("From queue: ");
					byte[] b = new byte[90000];
					String s = null;
					int numRead = message.getBody().read(b);
					while (-1 != numRead) {
						s = new String(b);
						s = s.trim();

						numRead = message.getBody().read(b);
						completeXml = completeXml + s;
						
						s = null;
					}

					int index = completeXml.lastIndexOf("</COLLECTION>");
					index = index + 13;
					completeXml = completeXml.substring(0, index);
					
					
					/*File file = new File("Payload.xml");

					if (!file.exists()) {
						file.createNewFile();
					}

					FileWriter fw = new FileWriter(file.getAbsoluteFile());
					BufferedWriter bw = new BufferedWriter(fw);
					bw.write(completeXml);
					bw.close();
*/
					
					//from here need to send the completeXml to payloadprocess
					
					process.sendData(completeXml);
					
					
					
					
					/*File file = new File("Payload.xml");

					if (!file.exists()) {
						file.createNewFile();
					}

					FileWriter fw = new FileWriter(file.getAbsoluteFile());
					BufferedWriter bw = new BufferedWriter(fw);
					bw.write(completeXml);
					bw.close();
					TransformerFactory tFactory = TransformerFactory.newInstance();
					Transformer transformer = tFactory
							.newTransformer(new javax.xml.transform.stream.StreamSource("input.xsl"));
					transformer.transform(new javax.xml.transform.stream.StreamSource("Payload.xml"),
							new javax.xml.transform.stream.StreamResult(new FileOutputStream("Payload_XSLT_Out.xml")));

					DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
					InputStream inputStream = new FileInputStream(new File("Payload_XSLT_Out.xml"));
					org.w3c.dom.Document doc = documentBuilderFactory.newDocumentBuilder().parse(inputStream);
					StringWriter stw = new StringWriter();
					Transformer serializer = TransformerFactory.newInstance().newTransformer();
					serializer.transform(new DOMSource(doc), new StreamResult(stw));*/
					
					/*//sending xml to Storage client
					sfClient.sendData(stw.toString());

					System.out.println("The json is formed");
					org.json.JSONObject payloadJsonXml = XML.toJSONObject(stw.toString()); 

					if (stw.toString().contains("BOMHeader")) {
						payLoadType = "BOM";
					} else {
						payLoadType = "PART";
					}

					System.out.println("Implementing feign client");
					
					//sending to bom
					HashMap<String , String> hashMap = new HashMap<>();
					hashMap.put("payloadJsonXml", payloadJsonXml.toString());
					client.sendData2(hashMap);*/
					
					
					
					/*URI uri = new URI("http://BOM-SERVICE/receiver" ); 
                    MultiValueMap<String, Object> mvm = new LinkedMultiValueMap<String, Object>();
                 mvm.add( "param1" , "TestParameter" );
                 Map result = restTemplate .postForObject(uri , mvm , Map.class );*/

					
					
					System.out.println("Custom Property: " + message.getProperty("MyProperty"));
					
				} else {
					System.out.println("Finishing up - no more messages.");
					break;
					// Added to handle no more messages.
					// Could instead wait for more messages to be added.
				}
			}
			return "success in retreiving the message from SB";

		} catch (Exception e) {
			e.printStackTrace();
			System.out.print("Generic exception encountered: ");
			System.out.println(e.getMessage());
			return "failure in retreiving the message from SB";
		}

	}
	
	

}

