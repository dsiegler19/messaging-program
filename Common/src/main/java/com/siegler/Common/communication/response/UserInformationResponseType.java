package com.siegler.Common.communication.response;

import com.google.gson.JsonObject;
import com.siegler.Common.communication.CommunicationType;
import com.siegler.Common.utils.CommonConstants;

public enum UserInformationResponseType {
	
	ACCEPTED,
	INVALID_USERNAME_OR_PASSWORD;
	
	public static UserInformationResponseType getUserInformationResponseType(JsonObject meta){
		
		if(CommunicationType.getCommunicationType(meta) != CommunicationType.RESPONSE || ResponseType.getResponseType(meta) != ResponseType.LOGIN_RESPONSE){
			
			throw new IllegalArgumentException("The metadata passed is not a login response!");
			
		}
				
		String declaredType = meta.get(CommonConstants.USER_INFORMATION_TYPE_TAG).getAsString();
		
		return UserInformationResponseType.valueOf(declaredType);
		
	}

}
