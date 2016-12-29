package com.siegler.Common.communication.response;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.siegler.Common.chat.Chat;
import com.siegler.Common.utils.CommonConstants;
import com.siegler.Server.server.Server;

public class UserInformationResponse extends Response {

	private static final long serialVersionUID = 5354749320462181086L;
	
	private static final ResponseType responseType = ResponseType.USER_INFORMATION_RESPONSE;
	
	private final UserInformationResponseType answer;
	
	private final List<String> friends;
	
	private final List<Chat> chats;

	public UserInformationResponse(Server from, UserInformationResponseType answer, List<String> friends, List<Chat> chats) {
		
		super(from);
		
		this.answer = answer;
		
		this.friends = friends;
		
		this.chats = chats;
		
		this.metadata = generateMetadata();

	}
	
	@Override
	protected JsonObject generateMetadata(){
		
		metadata = super.generateMetadata();
				
		if(getAnswer() != null){
			
			metadata.addProperty(CommonConstants.USER_INFORMATION_TYPE_TAG, getAnswer().toString());

			metadata.addProperty(CommonConstants.FRIENDS_TAG, new Gson().toJson(friends));
			
			JsonArray chatsArr = new JsonArray();
			
			for(Chat c : chats){
				
				chatsArr.add(c.toJson());
				
			}
			
			metadata.add(CommonConstants.CHATS_TAG, chatsArr);
						
		}
				
		return metadata;
		
	}
	
	public UserInformationResponseType getAnswer(){
		
		return answer;
		
	}

	@Override
	public ResponseType getResponseType() {

		return responseType;
		
	}

}
