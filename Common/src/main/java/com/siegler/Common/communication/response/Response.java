package com.siegler.Common.communication.response;

import java.util.Date;

import com.google.gson.JsonObject;
import com.siegler.Common.communication.Communication;
import com.siegler.Common.communication.CommunicationType;
import com.siegler.Common.utils.CommonConstants;
import com.siegler.Server.server.Server;

public abstract class Response extends Communication{

	private static final long serialVersionUID = -1198907995707592509L;	
		
	private static CommunicationType messageType = CommunicationType.RESPONSE;
		
	protected Server from;
	
	public Response(Server from){
		
		super(from.getIp());
		
		this.from = from;
		
		this.metadata = generateMetadata();
		
	}
	
	@Override
	protected JsonObject generateMetadata(){
		
		metadata = super.generateMetadata();
		
		metadata.addProperty(CommonConstants.RESPONSE_TYPE_TAG, getResponseType().toString());
		
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
	
	public abstract ResponseType getResponseType();
	
}
