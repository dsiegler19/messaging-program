package com.siegler.Common.communication.request;

import com.google.gson.JsonObject;
import com.siegler.Client.client.Client;
import com.siegler.Common.utils.CommonConstants;

public class UserInformationRequest extends Request {

	private static final long serialVersionUID = 3461619737554398315L;
	
	private static final RequestType requestType = RequestType.USER_INFORMATION_REQUEST;
	
	private final String username;
	
	private final String password;
	
	public UserInformationRequest(Client from, String username, String password) {
		
		super(from);
				
		this.username = username;
		
		this.password = password;
		
		this.metadata = generateMetadata();

	}
	
	@Override
	protected JsonObject generateMetadata(){
		
		metadata = super.generateMetadata();
		
		if(username != null && password != null){
		
			metadata.addProperty(CommonConstants.USERNAME_TAG, username);	
			metadata.addProperty(CommonConstants.PASSWORD_TAG, new String(password));
			
		}
				
		return metadata;
		
	}
	
	public String getUsername(){
		
		return username;
		
	}
	
	public String getPassword(){
		
		return password;
		
	}

	@Override
	public RequestType getRequestType() {

		return requestType;
		
	}

}
