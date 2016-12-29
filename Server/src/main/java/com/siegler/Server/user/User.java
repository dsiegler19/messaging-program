package com.siegler.Server.user;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.siegler.Common.logging.Logger;
import com.siegler.Common.logging.LoggingSeverity;
import com.siegler.Common.utils.CommonConstants;
import com.siegler.Server.utils.ServerConstants;

public class User {
	
	private String username;

	private String password;
	
	private long UUID;
	
	private Date dateJoined;
	
	private List<String> friends;
	
	public User(String username, String password, long UUID, Date dateJoined){
		
		this.username = username;
		this.password = password;
		
		this.UUID = UUID;
		
		this.dateJoined = dateJoined;
		
		this.friends = new ArrayList<String>();
		
	}
	
	public User(String username, String password, Date dateJoined) {

		this(username, password, java.util.UUID.randomUUID().getMostSignificantBits(), dateJoined);
			
	}
	
	public User(JsonObject userObject){
		
		this.username = userObject.get(CommonConstants.USERNAME_TAG).getAsString();
		this.password = userObject.get(CommonConstants.PASSWORD_TAG).getAsString();
		
		this.UUID = userObject.get(CommonConstants.UUID_TAG).getAsLong();
		
		String dateJoinedStr = userObject.get(ServerConstants.DATE_TAG).getAsString();

		this.dateJoined = new Date();
		
		try {
			
			this.dateJoined = CommonConstants.DATE_FORMAT.parse(dateJoinedStr);
			
		} catch (ParseException e) {

			Logger.log("Error parsing the date", e, LoggingSeverity.WARNING);
			
		}
		
		this.friends = new ArrayList<String>();
		
		JsonElement friendsJson = userObject.get(CommonConstants.FRIENDS_TAG);
		
		JsonArray friendsArr = friendsJson.getAsJsonArray();
		
		for(JsonElement f : friendsArr){
			
			String friend = f.getAsJsonObject().get(ServerConstants.FRIEND_TAG).getAsString();
			this.addFriend(friend);
			
		}
				
	}
	
	public JsonObject toJson(){
		
		JsonObject userObject = new JsonObject();
		
		userObject.addProperty(CommonConstants.USERNAME_TAG, this.getUsername());
		userObject.addProperty(CommonConstants.PASSWORD_TAG, this.getPassword());
		
		userObject.addProperty(CommonConstants.UUID_TAG, this.getUUID());
		
		userObject.addProperty(CommonConstants.DATE_TAG, CommonConstants.DATE_FORMAT.format(new Date()));
		
		JsonArray friends = new JsonArray();
		
		userObject.add(CommonConstants.FRIENDS_TAG, friends);
		
		return userObject;
		
	}

	public void addFriend(String friend){
		
		friends.add(friend);
		
	}
	
	public String getUsername() {
		
		return username;
		
	}

	public String getPassword() {
		
		return password;
		
	}

	public long getUUID() {
		
		return UUID;
		
	}

	public Date getDateJoined() {
		
		return dateJoined;
		
	}

	public List<String> getFriends() {
		
		return friends;
		
	}
	
	@Override
	public String toString(){
		
		StringBuilder str = new StringBuilder();
		
		str.append("Username:    " + this.username + "\n");
		
		String bullets = "";
		
		for(int i = 0; i < this.password.length(); i++){
			
			bullets += "•";
			
		}
		
		str.append("Password:    " + bullets + "\n");
		str.append("UUID:        " + this.UUID + "\n");
		str.append("Date joined: " + CommonConstants.DATE_FORMAT.format(this.dateJoined) + "\n");
		str.append("Friends:     " + friends.size());
		
		return str.toString();
		
	}

}
