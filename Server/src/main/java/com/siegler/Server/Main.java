package com.siegler.Server;

import com.siegler.Common.logging.Logger;
import com.siegler.Server.graphics.ServerFrame;
import com.siegler.Server.server.Server;
import com.siegler.Server.utils.ServerConfig;

public class Main {
	
	public static void main(String[] args){
		
		ServerFrame frame = new ServerFrame();

		Logger.initialize();
		Logger.log("Starting Messanger Server", false);
		ServerConfig config = ServerConfig.getConfig();
		Logger.setLogFile(config);
		
		Server server = new Server(config);
		
		frame.setServer(server);
		
		server.startListeningThread();

	}

}