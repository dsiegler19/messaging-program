package com.siegler.Common.communication.request;

import com.google.gson.JsonObject;
import com.siegler.Common.communication.CommunicationType;
import com.siegler.Common.utils.CommonConstants;

public enum RequestType {
	
	LOGIN,
	REGISTER_NEW_USER,
	USER_INFORMATION_REQUEST;
	
	public static RequestType getRequestType(JsonObject meta){
		
		if(CommunicationType.getCommunicationType(meta) != CommunicationType.REQUEST){
			
			throw new IllegalArgumentException("The metadata passed is not a response!");
			
		}
		
		String declaredType = meta.get(CommonConstants.REQUEST_TYPE_TAG).getAsString();
		
		return RequestType.valueOf(declaredType);
		
	}

}
