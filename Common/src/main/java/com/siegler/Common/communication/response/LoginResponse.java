package com.siegler.Common.communication.response;

import java.util.List;

import com.google.gson.JsonObject;
import com.siegler.Common.utils.CommonConstants;
import com.siegler.Server.server.Server;

public class LoginResponse extends Response {

	private static final long serialVersionUID = -45134098342089234L;
			
	private static final ResponseType responseType = ResponseType.LOGIN_RESPONSE;	
	
	private final LoginResponseType answer;
		
	public LoginResponse(Server from, LoginResponseType answer, List<String> friends) {
		
		super(from);
		
		this.answer = answer;
		
		this.metadata = generateMetadata();
		
	}
	
	@Override
	protected JsonObject generateMetadata(){
		
		metadata = super.generateMetadata();
				
		if(getAnswer() != null){
			
			metadata.addProperty(CommonConstants.LOGIN_RESPONSE_TYPE_TAG, getAnswer().toString());
			
		}
				
		return metadata;
		
	}

	public LoginResponseType getAnswer(){
		
		return answer;
		
	}
	
	@Override
	public ResponseType getResponseType() {

		return responseType;
		
	}

}
