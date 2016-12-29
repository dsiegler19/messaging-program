package com.siegler.Common.communication.response;

import com.google.gson.JsonObject;
import com.siegler.Common.utils.CommonConstants;
import com.siegler.Server.server.Server;

public class RegisterNewUserResponse extends Response {

	private static final long serialVersionUID = -7292415125731774497L;

	private static final ResponseType responseType = ResponseType.REGISTER_NEW_USER_RESPONSE;	
	
	private final RegisterNewUserResponseType answer;
	
	public RegisterNewUserResponse(Server from, RegisterNewUserResponseType answer) {
		
		super(from);
		
		this.answer = answer;
		
		this.metadata = generateMetadata();

	}
	
	@Override
	protected JsonObject generateMetadata(){
		
		metadata = super.generateMetadata();
		
		if(getAnswer() != null){
			
			metadata.addProperty(CommonConstants.REGISTER_NEW_USER_TYPE_TAG, getAnswer().toString());
			
		}
				
		return metadata;
		
	}

	public RegisterNewUserResponseType getAnswer(){
		
		return answer;
		
	}
	
	@Override
	public ResponseType getResponseType() {

		return responseType;
		
	}

}
