package com.siegler.Client;

import com.siegler.Client.client.Client;
import com.siegler.Client.utils.ClientConfig;
import com.siegler.Client.utils.ClientConstants;
import com.siegler.Client.utils.ServerGetter;
import com.siegler.Common.logging.Logger;

public class Main {

	public static void main(String[] args) throws Exception {
		
		Logger.initialize();
		Logger.log("Starting Messanger Client", false);
		ClientConfig config = ClientConfig.getConfig();
		Logger.setLogFile(config);
		
		String ip = ServerGetter.getBestIp();
		
		Client me = new Client(ip, ClientConstants.SERVER_PORT);
		me.run();

	}

}