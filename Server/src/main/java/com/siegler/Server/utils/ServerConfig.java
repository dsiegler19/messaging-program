package com.siegler.Server.utils;

import java.io.File;

import com.siegler.Common.utils.Config;
import com.siegler.Common.logging.Logger;

public class ServerConfig implements Config{
	
	private String ip;
	
	private String name;
	
	private int port;
	
	private File logFile;
	
	private File usersFile;
	
	private File chatsFile;
	
	private int maxNumThreads;
	
	private int maxNumUsersConnected;
	
	private File updatesFile;
	
	private static boolean initialized = false;
	
	private static ServerConfig theConfig;
	
	private ServerConfig(String ip, String name, int port, String pathToLogFile, String pathToUsersFile, String pathToChatsFile, int maxNumThreads, int maxNumUsersConnected, String pathToUpdatesFile){
		
		this.ip = ip;
		this.name = name;
		this.port = port;
		this.logFile = new File(pathToLogFile);
		this.usersFile = new File(pathToUsersFile);
		this.chatsFile = new File(pathToChatsFile);
		this.maxNumThreads = maxNumThreads;
		this.maxNumUsersConnected = maxNumUsersConnected;
		this.updatesFile = new File(pathToUpdatesFile);
		
	}
	
	public static ServerConfig getConfig(){
								
		if(!initialized){
						
			File configFile = new File(ServerConstants.PATH_TO_CONFIG_FILE);
									
			theConfig = readConfigFile(configFile);
						
			initialized = true;
			
		}
		
		return theConfig;
		
	}
	
	private static ServerConfig readConfigFile(File configFile){
				
		Logger.log("Reading config file " + configFile + " ...", false);
				
		Logger.log("Config file read", false);
				
		return new ServerConfig(ServerConstants.IP, ServerConstants.NAME, ServerConstants.PORT, ServerConstants.PATH_TO_LOG_FILE, 
				ServerConstants.PATH_TO_USERS_FILE, ServerConstants.PATH_TO_CHATS_FILE, ServerConstants.MAX_NUM_THREADS, ServerConstants.MAX_NUM_USERS_CONNECTED, ServerConstants.PATH_TO_UPDATES_FILE);
		
	}
	
	public String getIp() {
		
		return ip;
		
	}

	public String getName() {
		
		return name;
		
	}
	
	public int getPort() {
		
		return port;
		
	}

	public File getLogFile() {
		
		return logFile;
		
	}
	
	public File getUsersFile() {
		
		return usersFile;
		
	}
	
	public File getChatsFile(){
		
		return chatsFile;
		
	}

	public int getMaxNumThreads() {
		
		return maxNumThreads;
		
	}

	public int getMaxNumUsersConnected() {
		
		return maxNumUsersConnected;
		
	}
	
	public File getUpdatesFile() {
		
		return updatesFile;
		
	}
	
	@Override
	public String toString(){
		
		StringBuilder str = new StringBuilder();
		
		str.append("IP:                            " + this.getIp() + "\n");
		str.append("NAME:                          " + this.getName() + "\n");
		str.append("LOG FILE:                      " + this.getLogFile() + "\n");
		str.append("USERS FILE:                    " + this.getUsersFile() + "\n");
		str.append("MAX NUMBER OF THREADS:         " + this.getMaxNumThreads() + "\n");
		str.append("MAX NUMBER OF USERS CONNECTED: " + this.getMaxNumUsersConnected() + "\n");
		
		return str.toString();
		
	}

}
