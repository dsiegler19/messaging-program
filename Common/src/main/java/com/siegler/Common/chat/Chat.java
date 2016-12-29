package com.siegler.Common.chat;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.siegler.Common.chat.message.Message;
import com.siegler.Common.utils.CommonConstants;
import com.siegler.Server.user.User;

public class Chat {
	
	private List<User> usersInChat;
	
	private final long UUID;
	
	private LinkedList<Message> messages; 
	
	public Chat(List<User> usersInChat){
		
		this.usersInChat = usersInChat;
		
		this.UUID = java.util.UUID.randomUUID().getMostSignificantBits();
		
		this.messages = new LinkedList<Message>();
		
	}
	
	public Chat(JsonObject chatObject){
		
		this.usersInChat = new ArrayList<>();
		
		this.messages = new LinkedList<Message>();
		
		JsonArray usersInChatArr = chatObject.get(CommonConstants.USERS_IN_CHAT_TAG).getAsJsonArray();
		
		for(JsonElement user : usersInChatArr){
			
			User u = new User(user.getAsJsonObject());
			
			usersInChat.add(u);
			
		}
		
		JsonArray messagesArr = chatObject.get(CommonConstants.MESSAGES_IN_CHAT_TAG).getAsJsonArray();
		
		for(JsonElement message : messagesArr){
			
			Message m = new Message(message.getAsJsonObject());
			
			messages.add(m);
			
		}
		
		this.UUID = java.util.UUID.randomUUID().getMostSignificantBits();
		
	}
	
	public JsonObject toJson(){
		
		JsonObject json = new JsonObject();
		
		json.addProperty(CommonConstants.USERS_IN_CHAT_TAG, new Gson().toJson(usersInChat));
		
		JsonArray messagesArr = new JsonArray();
		
		for(Message m : messages){
			
			messagesArr.add(m.toJson());
			
		}
		
		json.add(CommonConstants.MESSAGES_IN_CHAT_TAG, messagesArr);
		
		json.addProperty(CommonConstants.UUID_TAG, UUID);
		
		return json;
		
	}
	
	public void addMessage(Message m){
		
		messages.addFirst(m);
		
	}
	
	public LinkedList<Message> getMessages(){
		
		return messages;
		
	}
	
	public void removeUser(User u){
		
		usersInChat.remove(u);
		
	}
	
	public void addUser(User u){
		
		usersInChat.add(u);
		
	}
	
	public boolean isInChat(User u){
		
		return usersInChat.contains(u);
		
	}

}
