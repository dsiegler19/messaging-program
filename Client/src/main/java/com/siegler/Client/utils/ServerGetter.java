package com.siegler.Client.utils;

import com.siegler.Common.logging.Logger;

public class ServerGetter {
	
	public static String getBestIp(){
		
		Logger.log("Getting the IP address for the optimal server");
		
		String ip = ClientConstants.SERVER_IP;
		
		Logger.log("Found the ip " + ip);
		
		return ip;

	}

}
