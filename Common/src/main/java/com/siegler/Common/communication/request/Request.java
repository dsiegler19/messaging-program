package com.siegler.Common.communication.request;

import java.util.Date;

import com.google.gson.JsonObject;
import com.siegler.Client.client.Client;
import com.siegler.Common.communication.Communication;
import com.siegler.Common.communication.CommunicationType;
import com.siegler.Common.utils.CommonConstants;

public abstract class Request extends Communication {
	
	private static final long serialVersionUID = -474813983867799180L;
			
	protected static final CommunicationType messageType = CommunicationType.REQUEST;
		
	public Request(Client from){
		
		super(from.getIp());
				
		this.metadata = generateMetadata();
		
	}

	@Override
	protected JsonObject generateMetadata() {

		metadata = super.generateMetadata();
		
		metadata.addProperty(CommonConstants.REQUEST_TYPE_TAG, getRequestType().toString());
		
		return metadata;
		
	}

	@Override
	public JsonObject getMetadata() {

		return metadata;
		
	}

	@Override
	public CommunicationType getCommunicationType() {

		return messageType;
		
	}

	@Override
	public Date getTimeSent() {
		
		return timeSent;
		
	}
	
	public abstract RequestType getRequestType();

}
