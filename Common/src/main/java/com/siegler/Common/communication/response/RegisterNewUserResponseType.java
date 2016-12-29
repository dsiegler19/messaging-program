package com.siegler.Common.communication.response;

import com.google.gson.JsonObject;
import com.siegler.Common.communication.CommunicationType;
import com.siegler.Common.utils.CommonConstants;

public enum RegisterNewUserResponseType {
	
	USER_REGISTERED,
	USERNAME_TAKEN,
	INVALID_PASSWORD;
	
	public static RegisterNewUserResponseType getRegisterNewUserResponseType(JsonObject meta){
		
		if(CommunicationType.getCommunicationType(meta) != CommunicationType.RESPONSE || ResponseType.getResponseType(meta) != ResponseType.REGISTER_NEW_USER_RESPONSE){
			
			throw new IllegalArgumentException("The metadata passed is not a register new user response!");
			
		}
		
		String declaredType = meta.get(CommonConstants.REGISTER_NEW_USER_TYPE_TAG).getAsString();
		
		return RegisterNewUserResponseType.valueOf(declaredType);
		
	}
	
}
