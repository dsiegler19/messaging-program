package com.siegler.Common.communication.response;

import com.google.gson.JsonObject;
import com.siegler.Common.communication.CommunicationType;
import com.siegler.Common.utils.CommonConstants;

public enum ResponseType {
	
	LOGIN_RESPONSE,
	REGISTER_NEW_USER_RESPONSE,
	USER_INFORMATION_RESPONSE;
	
	public static ResponseType getResponseType(JsonObject meta){
		
		if(CommunicationType.getCommunicationType(meta) != CommunicationType.RESPONSE){
			
			throw new IllegalArgumentException("The metadata passed is not a response!");
			
		}
		
		String declaredType = meta.get(CommonConstants.RESPONSE_TYPE_TAG).getAsString();
		
		return ResponseType.valueOf(declaredType);
		
	}
	
}
