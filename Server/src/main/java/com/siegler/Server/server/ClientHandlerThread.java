package com.siegler.Server.server;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.siegler.Common.communication.Communication;
import com.siegler.Common.communication.CommunicationType;
import com.siegler.Common.communication.InitializationCommunication;
import com.siegler.Common.communication.request.RequestType;
import com.siegler.Common.communication.response.LoginResponse;
import com.siegler.Common.communication.response.RegisterNewUserResponse;
import com.siegler.Common.communication.response.UserInformationResponse;
import com.siegler.Common.connection.Connection;
import com.siegler.Common.logging.Logger;
import com.siegler.Common.logging.LoggingSeverity;
import com.siegler.Common.utils.CommonConstants;

public class ClientHandlerThread extends Thread {
	
	private Connection connection;
	
	private Server server;
	
	public ClientHandlerThread(Connection c, Server s){
		
		this.connection = c;
		
		this.server = s;
		
	}
	
	@Override
	public void run(){
		
		super.run();
		
		initConnection();

		listenForRequests();
		
	}
	
	private void initConnection(){
		
		Communication m = new InitializationCommunication(this.server);

		connection.send(m.getMetadata());
		
	}
	
	private void listenForRequests(){
		
		while(true){
			
			Logger.log("Listening for requests...");
			
			String strReceived = connection.read();
			
			if(strReceived == null){
				
				return;
				
			}
					
			Logger.log("Received the string " + strReceived);
			
			JsonParser parser = new JsonParser();
			
			JsonObject received = parser.parse(strReceived).getAsJsonObject();
							
			if(CommunicationType.getCommunicationType(received) == CommunicationType.REQUEST){
				
				dispatchRequest(received);
				
			}
			
			else{
				
				Logger.log("The client should not be sending the server a message that isn't a request!", LoggingSeverity.WARNING);
				
			}
			
		}
		
	}
	
	private void dispatchRequest(JsonObject request){
		
		RequestType requestType = RequestType.getRequestType(request);
		
		String username;
		String password;
		
		switch(requestType){
		
		case LOGIN:
			Logger.log("Login request received");
			
			username = request.get(CommonConstants.USERNAME_TAG).getAsString();
			password = request.get(CommonConstants.PASSWORD_TAG).getAsString();
			
			LoginResponse loginResponse = server.loginUser(username, password);
						
			connection.send(loginResponse);
			
			break;
			
		case REGISTER_NEW_USER:
			Logger.log("Register new user request received");
			
			username = request.get(CommonConstants.USERNAME_TAG).getAsString();
			password = request.get(CommonConstants.PASSWORD_TAG).getAsString();
			
			RegisterNewUserResponse registerNewUserResponse = server.registerNewUser(username, password);
						
			connection.send(registerNewUserResponse);
			
			break;
			
		case USER_INFORMATION_REQUEST:
			Logger.log("User information request received");
			
			username = request.get(CommonConstants.USERNAME_TAG).getAsString();
			password = request.get(CommonConstants.PASSWORD_TAG).getAsString();
			
			UserInformationResponse userInformationResponse = server.getUserInformation(username, password);
			
			connection.send(userInformationResponse);
			
			break;
		
		default:
			Logger.log("Unknown request type received!", LoggingSeverity.WARNING);
			break;
		
		}
		
	}

}
