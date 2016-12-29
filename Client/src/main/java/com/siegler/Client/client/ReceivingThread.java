package com.siegler.Client.client;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.siegler.Common.logging.Logger;

public class ReceivingThread extends Thread {
	
	Client client;
	
	public ReceivingThread(Client c){
		
		super();
		
		this.client = c;
		
	}
	
	@Override
	public void run(){
		
		while(true){
			
			JsonObject meta = receiveMetadata();
			client.processMetadata(meta);
			
		}
		
	}
	
	private JsonObject receiveMetadata(){
		
		Logger.log("Receiving metadata...");
		
		client.checkIfConnected();
		
		String received = null;
					
		received = client.getConnection().read();
		
		if(received != null){
			
			JsonParser parser = new JsonParser();
			
			JsonObject meta = parser.parse(received).getAsJsonObject();
			
			Logger.log("Metadata " + meta.toString() + " received");
			
			return meta;
			
		}
		
		return null;
		
	}

}
