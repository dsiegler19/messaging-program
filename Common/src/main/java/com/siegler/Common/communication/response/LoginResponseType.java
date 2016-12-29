package com.siegler.Common.communication.response;

import com.google.gson.JsonObject;
import com.siegler.Common.communication.CommunicationType;
import com.siegler.Common.utils.CommonConstants;

public enum LoginResponseType {
	
	ACCEPTED,
	INVALID_USERNAME_OR_PASSWORD;
	
	public static LoginResponseType getLoginResponseType(JsonObject meta){
		
		if(CommunicationType.getCommunicationType(meta) != CommunicationType.RESPONSE || ResponseType.getResponseType(meta) != ResponseType.LOGIN_RESPONSE){
			
			throw new IllegalArgumentException("The metadata passed is not a login response!");
			
		}
				
		String declaredType = meta.get(CommonConstants.LOGIN_RESPONSE_TYPE_TAG).getAsString();
		
		return LoginResponseType.valueOf(declaredType);
		
	}

}
