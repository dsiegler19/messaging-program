package com.siegler.Client.utils;

import com.siegler.Common.annotations.Temporary;
import com.siegler.Common.utils.Constants;

public class ClientConstants implements Constants{
	
	public static final String PATH_TO_CONFIG_FILE = "config/config.txt";
	
	public static final String DEFAULT_FONT = "Copperplate Gothic";
	
	public static final @Temporary(reason = "Will be stored in the config file") String PATH_TO_LOG_FILE = "log/client.log";
	
	public static final @Temporary(reason = "Will be stored in the config file or determined by the ServerGetter.getBestIp method") String SERVER_IP = "127.0.0.1";
	public static final @Temporary(reason = "Possibly will be in the config file or determined by the ServerGetter, but should probably be standard among all servers") int SERVER_PORT = 10237;

}
