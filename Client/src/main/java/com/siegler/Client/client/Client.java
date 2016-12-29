package com.siegler.Client.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.JOptionPane;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.siegler.Client.graphics.HomeScreen;
import com.siegler.Client.graphics.WelcomeScreen;
import com.siegler.Common.connection.Connection;
import com.siegler.Common.logging.Logger;
import com.siegler.Common.logging.LoggingSeverity;
import com.siegler.Common.communication.Communication;
import com.siegler.Common.communication.CommunicationType;
import com.siegler.Common.communication.request.LoginRequest;
import com.siegler.Common.communication.request.RegisterNewUserRequest;
import com.siegler.Common.communication.request.Request;
import com.siegler.Common.communication.request.UserInformationRequest;
import com.siegler.Common.communication.response.LoginResponseType;
import com.siegler.Common.communication.response.RegisterNewUserResponseType;
import com.siegler.Common.communication.response.ResponseType;
import com.siegler.Common.communication.response.UserInformationResponse;
import com.siegler.Common.utils.CommonConstants;

public class Client {
	
	private String serverIp;
	private int serverPort;
	
	private Socket socket;
	
	private Connection connection;
	
	private boolean connected;
	
	private WelcomeScreen welcomeScreen;
	
	private HomeScreen homeScreen;
	
	public Client(String serverIp, int serverPort){
		
		this.serverIp = serverIp;
		this.serverPort = serverPort;
		
		this.connected = false;
				
	}
	
	public void run(){
		
		this.connect();
					
		JsonObject metadata = receiveInitilizationMetadata();
		processMetadata(metadata);
						
	}

	public void connect(){
		
		BufferedReader in = null;
		PrintWriter out = null;
				
		try {
			
			socket = new Socket(serverIp, serverPort);
			
		} catch (IOException e) {

			Logger.log("Could not create a socket with the server at " + serverIp + " on the port " + serverPort, e, LoggingSeverity.SEVERE);
			
			connected = false;
			
		}

		try {
			
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
		} catch (IOException e) {

			Logger.log("Could not create a input stream with the socket", e, LoggingSeverity.SEVERE);
			
			connected = false;

		}
		
		try {
			
			out = new PrintWriter(socket.getOutputStream(), true);
			
			connected = true;
					
		} catch (IOException e) {

			Logger.log("Could not create an output stream with socket", e, LoggingSeverity.SEVERE);

			connected = false;

		}
		
		if(in != null && out != null){
			
			connection = new Connection(in, out);
			
		}
		
		else{
			
			connected = false;
			
		}
						
	}
	
	public void processMetadata(JsonObject metadata) {

		CommunicationType communicationType = CommunicationType.getCommunicationType(metadata);
		
		dispatchOnType(communicationType, metadata);
		
	}
	
	private void dispatchOnType(CommunicationType type, JsonObject meta){
		
		Logger.log("Dispatching the meta given communication type " + type.toString());

		switch(type){
		
		case COMMAND:
			executeCommand(meta);
			break;
			
		case TEXT:
			Logger.log("CTEXT IS CURRENTLY UNIMPLEMENTED!", LoggingSeverity.SEVERE);
			break;
			
		case INITILIZATION:
			createWelcomeScreen(meta);
			break;
			
		case RESPONSE:
			ResponseType responseType = ResponseType.getResponseType(meta);
			dispatchOnType(responseType, meta);
			break;
			
		default:
			Logger.log("Unknown communication type received", LoggingSeverity.WARNING);
			break;
		
		}
		
	}
	
	private void dispatchOnType(ResponseType type, JsonObject meta){
		
		Logger.log("Dispatching the meta given response type " + type.toString());
		
		switch(type){
		
		case LOGIN_RESPONSE:
			LoginResponseType answer = LoginResponseType.getLoginResponseType(meta);
			dispatchOnType(answer);
			break;
			
		case REGISTER_NEW_USER_RESPONSE:
			RegisterNewUserResponseType ans = RegisterNewUserResponseType.getRegisterNewUserResponseType(meta);
			dispatchOnType(ans);
			break;
		
		default:
			Logger.log("Unknown response type received", LoggingSeverity.WARNING);
			break;
		
		}
		
	}
	
	public void dispatchOnType(LoginResponseType answer){
		
		Logger.log("Dispatching the meta given login response type " + answer.toString());
				
		switch(answer){
		
		case ACCEPTED:
			Logger.log("Logging in...");
			
			if(welcomeScreen != null){
				
				String username = welcomeScreen.getUsername();
				String password = welcomeScreen.getPassword();
				login(username, password);
				
			}
			
			break;
			
		case INVALID_USERNAME_OR_PASSWORD:
			Logger.log("Invalid username or password response received");
			break;
			
		default:
			Logger.log("Unknown response type received", LoggingSeverity.WARNING);
		
		}
		
	}
	
	public void dispatchOnType(RegisterNewUserResponseType answer){
		
		Logger.log("Dispatching the meta given register new user response type " + answer.toString());

		switch(answer){
		
		case USER_REGISTERED:
			Logger.log("User successfully registered");
			Logger.log("Logging in...");
			
			if(welcomeScreen != null){
				
				JOptionPane.showConfirmDialog(welcomeScreen, "Thanks " + welcomeScreen.getUsername() + "!\nYou have been successfully registered and will be logged in immediately.", "Successful Registration!", JOptionPane.OK_CANCEL_OPTION);
				
				String username = welcomeScreen.getUsername();
				String password = welcomeScreen.getPassword();
				login(username, password);
				
			}
			
			break;
			
		case INVALID_PASSWORD:
			Logger.log("Invalid password response received");
			welcomeScreen.setAlertText("Username taken!");
			break;
			
		case USERNAME_TAKEN:
			Logger.log("Username taken response received");
			welcomeScreen.setAlertText("Invalid password! A password must be between 2 and 20 characters!");
			break;
			
		default:
			Logger.log("Unknown response type received", LoggingSeverity.WARNING);
			break;
			
		}
		
	}
	
	private void login(String username, String password){
		
		createHomeScreen();
		welcomeScreen.dispose();
		welcomeScreen = null;
		UserInformationResponse userInfo = requestUserInformation(username, password);
		// homeScreen.displayUserInformation(x);
		
	}
	
	private UserInformationResponse requestUserInformation(String username, String password){
		
		UserInformationRequest request = new UserInformationRequest(this, username, password);
		connection.send(request);
		
		receiveMetadata();
		
		return null;
		
	}
	
	private JsonObject receiveInitilizationMetadata(){
		
		return receiveMetadata();
		
	}
		
	private JsonObject receiveMetadata(){
		
		Logger.log("Receiving metadata...");
		
		this.checkIfConnected();
		
		String received = null;
					
		received = connection.read();
		
		if(received != null){
			
			JsonParser parser = new JsonParser();
			
			JsonObject meta = parser.parse(received).getAsJsonObject();
			
			Logger.log("Metadata " + meta.toString() + " received");
			
			return meta;
			
		}
		
		return null;
		
	}
	
	public void executeCommand(JsonObject metadata){
		
		Logger.log("Executing a command...");
		
		Logger.log("COMMAND EXECUTION IS CURRENTLY UNIMPLEMENTED!", LoggingSeverity.SEVERE);
		
		Logger.log("Finished executing command");
		
	}
	
	public void checkIfConnected(){
				
		if(!connected){
			
			throw new IllegalStateException("This client must be connected!");
			
		}
		
	}
	
	private void createWelcomeScreen(JsonObject meta){
		
		Logger.log("Creating welcome screen");
		
		String greeting = meta.get(CommonConstants.GREETING_TAG).toString();
		
		JsonElement newsAndUpdates = meta.get(CommonConstants.UPDATES_TAG);
		
		List<String> headers = new ArrayList<>();
		List<String> news = new ArrayList<>();
		
		for(Entry<String, JsonElement> e : newsAndUpdates.getAsJsonObject().entrySet()){
						
			headers.add(e.getKey());
			news.add(e.getValue().toString());
			
		}
				
		WelcomeScreen screen = new WelcomeScreen(this, greeting, headers.toArray(new String[headers.size()]), news.toArray(new String[news.size()]));
		this.welcomeScreen = screen;
		
	}
	
	private void createHomeScreen(){
				
		homeScreen = new HomeScreen(new ArrayList<String>());
		
	}

	public String getServerIp(){
		
		return serverIp;
		
	}

	public LoginResponseType requestLogin(String username, char[] password) {
		
		Logger.log("Requesting to login with the username " + username + " and the password " + password.hashCode());
		
		Request loginRequest = new LoginRequest(this, username, password);
				
		sendCommunication(loginRequest);
		
		/*
		 * TODO:
		 * Have this moved to receivingThread
		 */
		
		LoginResponseType response = LoginResponseType.getLoginResponseType(receiveMetadata());
		
		Logger.log("The server returned the login request");
		
		return response;
				
	}

	public RegisterNewUserResponseType requestRegisterNewUser(String username, char[] password) {

		Logger.log("Requesting to register a new user with the username " + username + " and the password " + password.hashCode());
				
		Request registerNewUserRequest = new RegisterNewUserRequest(this, username, password);
				
		sendCommunication(registerNewUserRequest);
		
		/*
		 * TODO:
		 * Have this moved to receivingThread
		 */
		
		RegisterNewUserResponseType response = RegisterNewUserResponseType.getRegisterNewUserResponseType(receiveMetadata());
		
		Logger.log("The server returned the register new user request");
		
		return response;
		
	}
	
	public void sendCommunication(Communication m){
		
		sendMetadata(m);
		
	}
	
	public void sendMetadata(Communication m){
		
		Logger.log("Sending metadata " + m + " ...");
		
		connection.send(m.getMetadata());
		
	}

	public String getIp() {
		
		URL ipAdress;
		
		String ip = "";

        try {
        	
            ipAdress = new URL("http://myexternalip.com/raw");

            BufferedReader in = new BufferedReader(new InputStreamReader(ipAdress.openStream()));

            ip = in.readLine();
            
        } catch (MalformedURLException e) {
        	
            Logger.log("Malformed URL exception when requesting the client's IP address from myesternalip.com/raw", e, LoggingSeverity.WARNING);
            
        } catch (IOException e) {
        	
            Logger.log("IO exception when requesting the client's IP address from myesternalip.com/raw", e, LoggingSeverity.WARNING);
            
        }

		return ip;
		
	}
	
	public Connection getConnection(){
		
		return connection;
		
	}

}
