package com.siegler.Server.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.siegler.Common.chat.Chat;
import com.siegler.Common.communication.response.LoginResponse;
import com.siegler.Common.communication.response.LoginResponseType;
import com.siegler.Common.communication.response.RegisterNewUserResponse;
import com.siegler.Common.communication.response.RegisterNewUserResponseType;
import com.siegler.Common.communication.response.UserInformationResponse;
import com.siegler.Common.communication.response.UserInformationResponseType;
import com.siegler.Common.connection.Connection;
import com.siegler.Common.logging.Logger;
import com.siegler.Common.logging.LoggingSeverity;
import com.siegler.Server.user.User;
import com.siegler.Server.utils.ServerConfig;
import com.siegler.Server.utils.ServerConstants;
import com.siegler.Server.utils.ServerUtils;

public class Server {

	private String ip;

	private int port;

	private File usersFile;
	
	private File chatsFile;

	private ServerSocket listenerSocket;

	private Set<Connection> connectedUsers;

	private Set<User> loggedInUsers;
	
	private Set<User> registeredUsers;
	
	private Set<Chat> chats;
	
	private boolean writtenToUsersFile;
	
	private boolean writtenToChatsFile;
	
	public Server(ServerConfig config) {

		Logger.log("Creating server with the following configuration:\n" + config.toString());

		this.ip = config.getIp();

		this.port = config.getPort();

		this.usersFile = config.getUsersFile();
		
		this.chatsFile = config.getChatsFile();
		
		this.loggedInUsers = new HashSet<User>();
				
		this.registeredUsers = readUserFile();

		try {

			listenerSocket = new ServerSocket(this.port);

		} catch (IOException e) {

			Logger.log("Error starting the listener socket!", e, LoggingSeverity.SEVERE);

			try {

				listenerSocket.close();

			} catch (IOException ex) {

				Logger.log("Error closing the listener socket!", ex, LoggingSeverity.SEVERE);

			}

		}
		
		this.registeredUsers = readUserFile();
		
		this.chats = readChatsFile();

		this.connectedUsers = new HashSet<>();
		
		this.writtenToUsersFile = false;
		
		this.writtenToChatsFile = false;
		
		Runtime.getRuntime().addShutdownHook(new Thread(){
			
			@Override
			public void run(){
				
				cleanup();
				
			}
			
		});

		Logger.log("Server created");

	}

	public Set<User> readUserFile() {

		Logger.log("Reading the user file...");

		HashSet<User> usersRead = new HashSet<>();
				
		JsonObject usersJson = null;
				
		try {
																	
			JsonParser parser = new JsonParser();
			
			BufferedReader reader = new BufferedReader(new FileReader(this.usersFile));
			
			usersJson = parser.parse(reader).getAsJsonObject();
			
			reader.close();
									
		} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {

			Logger.log("Error parsing or reading the users file!", e, LoggingSeverity.SEVERE);
			System.exit(1);
			
		} catch (IOException e) {

			Logger.log("Error reading the user file!", e, LoggingSeverity.SEVERE);
			System.exit(1);
			
		}
		
		if(usersJson != null && !usersJson.get(ServerConstants.USERS_TAG).toString().equals("[]")){
						
			JsonArray arr = usersJson.get(ServerConstants.USERS_TAG).getAsJsonArray();
			
			for(JsonElement user : arr){
				
				JsonObject userObject = user.getAsJsonObject();				
				
				User u = new User(userObject);
				
				usersRead.add(u);
				
			}
			
		}
				
		Logger.log("User file read");
		
		return usersRead;

	}
	
	public Set<Chat> readChatsFile(){
		
		Logger.log("Reading the chats file...");
		
		HashSet<Chat> chatsRead = new HashSet<>();
						
		if(!this.chatsFile.exists()){
			
			return chatsRead;
			
		}
		
		JsonObject chatsJson = null;
		
		try {
			
			JsonParser parser = new JsonParser();
			
			BufferedReader reader = new BufferedReader(new FileReader(this.chatsFile));
			
			chatsJson = parser.parse(reader).getAsJsonObject();
			
			reader.close();
									
		} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {

			Logger.log("Error parsing or reading the chats file!", e, LoggingSeverity.SEVERE);
			System.exit(1);
			
		} catch (IOException e) {

			Logger.log("Error reading the chats file!", e, LoggingSeverity.SEVERE);
			System.exit(1);
			
		}
		
		if(chatsJson != null && !chatsJson.get(ServerConstants.CHATS_TAG).toString().equals("[]")){
			
			JsonArray arr = chatsJson.get(ServerConstants.CHATS_TAG).getAsJsonArray();
			
			for(JsonElement chat : arr){
				
				JsonObject chatObject = chat.getAsJsonObject();				
				
				Chat c = new Chat(chatObject);
				
				chatsRead.add(c);
				
			}
			
		}
		
		Logger.log("Chats file read");
		
		return chatsRead;
		
	}
	
	private void writeUsersToUserFile(){
		
		if(!this.writtenToUsersFile){
			
			Logger.log("Writing the users to the user file...", false);
			
			JsonArray usersArr = new JsonArray();
			
			for(User u : registeredUsers){
				
				usersArr.add(u.toJson());
				
			}
			
			JsonObject usersObject = new JsonObject();
			usersObject.add(ServerConstants.USERS_TAG, usersArr);
					
			try {

				FileWriter writer = new FileWriter(usersFile, false);
				
				writer.write(usersObject.toString());
				
				writer.close();
				
				Logger.log("Finished writing users to user file", false);
				
			} catch (IOException e) {
				
				Logger.log("Error creating the writer when writing to the users file!", e, LoggingSeverity.SEVERE, false);
				
			}
			
			this.writtenToUsersFile = true;
			
		}
		
	}
	
	private void writeChatsToChatsFile(){

		if(!this.writtenToChatsFile){
			
			Logger.log("Writing the chats to the chats file", false);
			
			JsonArray chatsArr = new JsonArray();
			
			for(Chat c : chats){
				
				chatsArr.add(c.toJson());
				
			}
			
			JsonObject chatsObject = new JsonObject();
			chatsObject.add(ServerConstants.CHATS_TAG, chatsArr);
			
			try{
				
				FileWriter writer = new FileWriter(chatsFile, false);
				
				writer.write(chatsObject.toString());
				
				writer.close();
				
				Logger.log("Finished writing chats to the chats file", false);
								
			} catch (IOException e){
				
				Logger.log("Error creating the writer when writing to the chats file!", e, LoggingSeverity.SEVERE, false);
				
			}
			
			this.writtenToChatsFile = true;
			
		}
		
	}
	
	public LoginResponse loginUser(String username, String password){
		
		LoginResponse found = null;
		User userFound = null;
		
		for(User u : registeredUsers){
			
			if(u.getUsername().equalsIgnoreCase(username) && u.getPassword().equals(password)){
				
				found = new LoginResponse(this, LoginResponseType.ACCEPTED, u.getFriends());
				userFound = u;
				
			}
			
		}
		
		if(found != null){
			
			addToConnectedUsers(userFound);
			
			return found;
			
		}
		
		return new LoginResponse(this, LoginResponseType.INVALID_USERNAME_OR_PASSWORD, null);
		
	}
	
	public RegisterNewUserResponse registerNewUser(String username, String password){
		
		Logger.log("Registering a new user with username " + username + " and password " + password.hashCode());
		
		if(!ServerUtils.checkIfValidPassword(password)){
			
			Logger.log("Invalid password");
			
			return new RegisterNewUserResponse(this, RegisterNewUserResponseType.INVALID_PASSWORD);
			
		}
		
		boolean usernameTaken = false;
		
		for(User u : registeredUsers){
			
			if(u.getUsername().equals(username)){
				
				usernameTaken = true;
				break;
				
			}
			
		}
		
		if(usernameTaken){
			
			Logger.log("Username taken");
			
			return new RegisterNewUserResponse(this, RegisterNewUserResponseType.USERNAME_TAKEN);
			
		}
		
		else{
						
			User newUser = new User(username, password, new Date());
			
			addToConnectedUsers(newUser);
			addToRegisteredUsers(newUser);
			
			Logger.log("User registered");
			
			return new RegisterNewUserResponse(this, RegisterNewUserResponseType.USER_REGISTERED);
			
		}
		
	}
	
	public UserInformationResponse getUserInformation(String username, String password){
		
		Logger.log("Getting the user information for " + username);
		
		User user = findLoggedInUser(username, password);
		
		if(user == null){
			
			Logger.log("The user that requested information isn't logged in", LoggingSeverity.WARNING);
			
			UserInformationResponse response = new UserInformationResponse(this, UserInformationResponseType.INVALID_USERNAME_OR_PASSWORD, null, null);
			
			return response;
			
		}
				
		List<Chat> chatsUserIsIn = new ArrayList<Chat>();
		
		for(Chat c : chats){
			
			if(c.isInChat(user)){
				
				chatsUserIsIn.add(c);
				
			}
			
		}
		
		UserInformationResponse response = new UserInformationResponse(this, UserInformationResponseType.ACCEPTED, user.getFriends(), chatsUserIsIn);
		
		return response;
		
	}
	
	private User findLoggedInUser(String username, String password){
		
		User loggedIn =  null;
		
		for(User u : loggedInUsers){
			
			if(u.getUsername().equals(username) && u.getPassword().equals(password)){
				
				loggedIn = u;
				break;
				
			}
			
		}
		
		return loggedIn;
		
	}

	public void startListeningThread() {

		Logger.log("Starting the first listening thread...");

		ClientListenerThread newThread = new ClientListenerThread(listenerSocket, this);
		newThread.start();

		Logger.log("Listening thread started");

	}
	
	public void addConnection(Connection c){
		
		connectedUsers.add(c);
		
	}

	public void executeCommand(String s) {

		Logger.log("Executing command " + s + "...");
		
		String[] split = s.split(" ");
		String command = split[0];
		String[] args = new String[split.length - 1];
		
		for(int i = 1; i < split.length; i++){
			
			args[i - 1] = split[i];
			
		}

		dispatchCommand(command, args);

	}
	
	private void dispatchCommand(String command, String[] args){
		
		switch (command) {

		case ("quit"):
			cleanup();
			System.exit(0);
			break;
			
		case ("shutdown"):
			cleanup();
			System.exit(0);
			break;

		case ("show"):
			ServerUtils.show(args[0], this);
			break;
			
		case ("save"):
			saveData();
			break;
			
		case ("save_data"):
			saveData();
			break;
			
		default:
			Logger.log("Unknown command!");
			break;

		}
		
	}
	
	public void cleanup(){
		
		Logger.log("Cleaning up...", false);
		
		saveData();
		
	}
	
	private void addToConnectedUsers(User u){
		
		Logger.log("Logging in the following user...");
		Logger.log(u.toString());
		
		loggedInUsers.add(u);
		
	}
	
	private void addToRegisteredUsers(User u){
		
		Logger.log("Registering the following user...");
		Logger.log(u.toString());
		
		registeredUsers.add(u);
		
	}
	
	public String getIp() {

		return ip;

	}
	
	public Set<User> getLoggedInUsers(){
		
		return loggedInUsers;
		
	}
	
	public Set<User> getRegisteredUsers(){
		
		return registeredUsers;
		
	}
	
	public void saveData(){
		
		writeUsersToUserFile();
		writeChatsToChatsFile();
		
	}

}
