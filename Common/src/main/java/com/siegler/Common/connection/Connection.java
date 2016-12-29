package com.siegler.Common.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import com.google.gson.JsonObject;
import com.siegler.Common.communication.Communication;
import com.siegler.Common.logging.Logger;
import com.siegler.Common.logging.LoggingSeverity;

public class Connection {
	
	private BufferedReader in;
	
	private PrintWriter out;
		
	public Connection(BufferedReader in, PrintWriter out) {

		this.in = in;
		this.out = out;
		
	}
	
	public String read(){
		
		String read = "";
		
		try {
			
			read = in.readLine();
			
		} catch (IOException e) {

			Logger.log("Error reading in from a connection!", e, LoggingSeverity.SEVERE);
			
		}
		
		return read;
		
	}
	
	public void send(String message){
		
		Logger.log("Writing " + message + " to the output at " + out.toString() + " ...");
		
		out.println(message);
		
		Logger.log("Finished writing");
		
	}
	
	public void send(JsonObject message){
		
		send(message.toString());
		
	}
	
	public void send(Communication message){
		
		send(message.getMetadata());
		
	}
	
	public BufferedReader getIn() {
		
		return in;
		
	}

	public PrintWriter getOut() {
		
		return out;
		
	}
	
}
