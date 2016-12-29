package com.siegler.Common.communication;

import com.google.gson.JsonObject;
import com.siegler.Common.utils.CommonConstants;

public enum CommunicationType {
	
	INITILIZATION,
	COMMAND,
	TEXT,
	IMAGE,
	REQUEST,
	RESPONSE;
	
	public static CommunicationType getCommunicationType(JsonObject meta){

		String declaredType = meta.get(CommonConstants.COMMUNICATION_TYPE_TAG).getAsString();
		
		return CommunicationType.valueOf(declaredType);
		
	}

}
