package com.siegler.Common.chat.message;

import com.google.gson.JsonObject;
import com.siegler.Common.utils.CommonConstants;
import com.siegler.Server.user.User;

public class Message {

	private User sender;
	
	private String messageContents;
	
	public Message(User s, String m){
		
		this.sender = s;
		
		this.messageContents = m;
		
	}
	
	public Message(JsonObject messageObject){
		
		this.sender = new User(messageObject.get(CommonConstants.SENDER_TAG).getAsJsonObject());
		
		this.messageContents = messageObject.get(CommonConstants.MESSAGE_CONTENT_TAG).toString();
		
	}
	
	public JsonObject toJson(){
		
		JsonObject messageObject = new JsonObject();
		
		messageObject.add(CommonConstants.SENDER_TAG, sender.toJson());
		
		messageObject.addProperty(CommonConstants.MESSAGE_CONTENT_TAG, messageContents);
		
		return messageObject;
		
	}
	
	public User getSender() {
		
		return sender;
		
	}

	public void setSender(User sender) {
		
		this.sender = sender;
		
	}

	public String getMessageContents() {
		
		return messageContents;
		
	}

	public void setMessageContents(String message) {
		
		this.messageContents = message;
		
	}

}
