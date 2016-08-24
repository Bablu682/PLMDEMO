var http = require('http');
var loggy = require('C:/Program Files/nodejs/node_modules/npm/node_modules/winston');
var request = require('C:/Program Files/nodejs/node_modules/npm/node_modules/request');
var azure = require('C:/Program Files/nodejs/node_modules/npm/node_modules/azure');

var connectionString = "Endpoint=sb://sbplmdev.servicebus.windows.net/;SharedAccessKeyName=RootManageSharedAccessKey;SharedAccessKey=Y3X4ESQe5yP6ZOmz7zB3rbTgRUSEPlvRyLy9LgTbGls="; 


loggy.info("Service Bus connection begins.");
var serviceBusService = azure.createServiceBusService(connectionString);
loggy.info("Service Bus connection done.");

loggy.info("Receive queue messages begins.");
serviceBusService.receiveQueueMessage('testqueue',{ isPeekLock: true },  function(error, lockedMessage){
    if(!error && lockedMessage != undefined){
    	xmlMessage = lockedMessage.body;
    	loggy.info("Xml data received for service bus: "+JSON.stringify(xmlMessage));
    	
    	loggy.info("Sending xml data to microservice begins.");
    	request.post(
    		    'http://10.229.190.41:9090/receiveXml',
    		    { form: { 'xml': xmlMessage } },
    		    function (error, response, body) {
    		        if (!error && response.statusCode == 200) {
    		        	loggy.info("Xml data sent successfully to microservice.");
    		        	loggy.info("Response body: "+body);
    		        }else{
    		        	loggy.error("Xml data sending failed with status code: "+response.statusCode);
    		        }
    		    }
    		);
    	
    }else{
    	loggy.error("Xml data sending failed "+error);
    }
});