package com.siegler.Common.communication.request;

import com.google.gson.JsonObject;
import com.siegler.Client.client.Client;
import com.siegler.Common.utils.CommonConstants;

public class RegisterNewUserRequest extends Request {

	private static final long serialVersionUID = -4338011313810090737L;
	
	private static final RequestType requestType = RequestType.REGISTER_NEW_USER;
	
	private String username;
	
	private char[] password;
	
	public RegisterNewUserRequest(Client from, String username, char[] pass){
		
		super(from);
				
		this.username = username;
		
		this.password = pass;
				
		this.metadata = generateMetadata();
				
	}

	@Override
	protected JsonObject generateMetadata(){
		
		metadata = super.generateMetadata();
		
		metadata.addProperty(CommonConstants.USERNAME_TAG, username);
		
		if(password != null){
			
			metadata.addProperty(CommonConstants.PASSWORD_TAG, new String(password));
			
		}
				
		return metadata;
		
	}

	@Override
	public RequestType getRequestType() {

		return requestType;
		
	}

}
