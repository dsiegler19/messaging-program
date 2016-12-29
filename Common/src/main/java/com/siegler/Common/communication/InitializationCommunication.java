package com.siegler.Common.communication;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Date;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.siegler.Common.logging.Logger;
import com.siegler.Common.utils.CommonConstants;
import com.siegler.Server.server.Server;
import com.siegler.Server.utils.ServerConfig;

public class InitializationCommunication extends Communication {

	private static final long serialVersionUID = -45675456890L;
			
	protected static final CommunicationType messageType = CommunicationType.INITILIZATION;
		
	public InitializationCommunication(Server from){
			
		super(from.getIp());
				
		metadata = generateMetadata();
		
	}
	
	@Override
	protected JsonObject generateMetadata() {
		
		metadata = super.generateMetadata();
		
		metadata.addProperty(CommonConstants.GREETING_TAG, "Welcome to Dylan's Chatting Program");
		
		JsonParser parser = new JsonParser();
		JsonElement updates = null;
		
		try {
			
			updates = parser.parse(new FileReader(ServerConfig.getConfig().getUpdatesFile()));
			
		} catch (FileNotFoundException e) {

			Logger.log("Error reading the updates file at " + ServerConfig.getConfig().getUpdatesFile().toString() + "!", e);
			
		}
				
		metadata.add(CommonConstants.UPDATES_TAG, updates);

		return metadata;
		
	}
	
	@Override
	public JsonObject getMetadata(){
		
		return metadata;
		
	}

	@Override
	public CommunicationType getCommunicationType() {

		return messageType;
		
	}

	@Override
	public Date getTimeSent() {

		return this.timeSent;
		
	}

}
