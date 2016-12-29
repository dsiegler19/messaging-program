package com.siegler.Client.utils;

import java.io.File;

import com.siegler.Common.utils.Config;
import com.siegler.Common.logging.Logger;

public class ClientConfig implements Config{
	
	private File logFile;
	
	private static boolean initialized = false;
	
	private static ClientConfig theConfig;
	
	private ClientConfig(String pathToLogFile){
		
		this.logFile = new File(pathToLogFile);
		
	}
	
	public static ClientConfig getConfig(){
								
		if(!initialized){
						
			File configFile = new File(ClientConstants.PATH_TO_CONFIG_FILE);
									
			theConfig = readConfigFile(configFile);
						
			initialized = true;
			
		}
		
		return theConfig;
		
	}
	
	private static ClientConfig readConfigFile(File configFile){
				
		Logger.log("Reading config file " + configFile + " ...", false);
				
		Logger.log("Config file read", false);
				
		return new ClientConfig(ClientConstants.PATH_TO_LOG_FILE);
		
	}

	public File getLogFile() {
		
		return logFile;
		
	}
	
	@Override
	public String toString(){
		
		StringBuilder str = new StringBuilder();
		
		str.append("LOG FILE: " + this.getLogFile() + "\n");
		
		return str.toString();
		
	}

}
