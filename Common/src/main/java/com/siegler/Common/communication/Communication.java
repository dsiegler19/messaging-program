package com.siegler.Common.communication;

import java.io.Serializable;
import java.util.Date;

import com.google.gson.JsonObject;
import com.siegler.Common.utils.CommonConstants;

public abstract class Communication implements Serializable{
	
	private static final long serialVersionUID = -586943602294749607L;
		
	protected Date timeSent;
	
	protected JsonObject metadata;
	
	protected String senderIp;
		
	public Communication(String senderIp){
		
		this.senderIp = senderIp;
		
		metadata = generateMetadata();
		
	}
	
	protected JsonObject generateMetadata(){
		
		metadata = new JsonObject();
		
		Date date = new Date();
		
		this.timeSent = date;
		
		metadata.addProperty(CommonConstants.DATE_TAG, CommonConstants.DATE_FORMAT.format(date));
		metadata.addProperty(CommonConstants.IP_TAG, getSenderIp());
		metadata.addProperty(CommonConstants.COMMUNICATION_TYPE_TAG, getCommunicationType().toString());
		
		return metadata;
		
	}
	
	public String getSenderIp(){
		
		return senderIp;
		
	}
	
	public abstract JsonObject getMetadata();
	public abstract CommunicationType getCommunicationType();
	public abstract Date getTimeSent();

}
