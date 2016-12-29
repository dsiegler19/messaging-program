package com.siegler.Server.utils;

import com.siegler.Common.logging.Logger;
import com.siegler.Server.server.Server;
import com.siegler.Server.user.User;

public class ServerUtils {
	
	public static void show(String what, Server server){
		
		Logger.log("Showing " + what + "...");
		
		switch(what){
		
		case("config"):
			Logger.log(ServerConfig.getConfig().toString());
			break;
		
		case("connected_users"):
			for(User u : server.getRegisteredUsers()){
				
				Logger.log(u.toString());
				
			}
			break;
		
		case("registered_users"):
			for(User u : server.getRegisteredUsers()){
				
				Logger.log(u.toString());
				
			}
			break;
		
		default:
			Logger.log("Unknown show argument!");
			break;
			
		}
		
	}
	
	public static boolean checkIfValidPassword(String password){
		
		if(password == null){
			
			return false;
			
		}
		
		if(password.length() < 2 || password.length() > 20){
			
			return false;
			
		}
		
		return true;
		
	}

}
