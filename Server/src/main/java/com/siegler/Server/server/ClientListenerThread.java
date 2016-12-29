package com.siegler.Server.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.siegler.Common.connection.Connection;
import com.siegler.Common.logging.Logger;

public class ClientListenerThread extends Thread{
	
	private ServerSocket listenerSocket;
	
	private Server server;
			
	public ClientListenerThread(ServerSocket listenerSocket, Server s){
		
		this.listenerSocket = listenerSocket;
		
		this.server = s;
						
	}
	
	@Override
	public void run(){
		
		super.run();
		
		while(true){
			
			Connection c = connect();
			
			if(c != null){
				
				addConnectionToServer(c);
				startHandlerThread(c);
				
			}
						
		}
				
	}
	
	public Connection connect(){
		
		Logger.log("Listening for a user to connect...");
		
		Connection connection = null;
		
		try {
			
			Socket acceptedSocket = listenerSocket.accept();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(acceptedSocket.getInputStream()));
			
	        PrintWriter out = new PrintWriter(acceptedSocket.getOutputStream(), true);
	        
	        connection = new Connection(in, out);
	                    
            Logger.log("User connected");
            			                
		} catch (IOException e) {

			Logger.log("Error accepting the socket", e);
			
		}
		
        return connection;
		
	}
	
	public void addConnectionToServer(Connection c){
		
		server.addConnection(c);
		
	}
	
	public void startHandlerThread(Connection c){
		
		ClientHandlerThread handler = new ClientHandlerThread(c, this.server);
		handler.start();
		
	}
	
}
