package com.siegler.Server.utils;

import com.siegler.Common.annotations.Temporary;
import com.siegler.Common.utils.Constants;

public class ServerConstants implements Constants{
	
	public static final String PATH_TO_CONFIG_FILE = "config/config.txt";
	
	public static final String USERS_TAG = "users";
	
	public static final String DATE_TAG = "date";
	
	public static final String FRIEND_TAG = "friend";
	
	public static final String CHATS_TAG = "friend";
	
	public static final @Temporary(reason = "Will be stored in the config file") String IP = "127.0.0.1";
	public static final @Temporary(reason = "Will be stored in the config file") String NAME = "Main Server";
	public static final @Temporary(reason = "Will be stored in the config file or should it remain constant?") int PORT = 10237;
	public static final @Temporary(reason = "Will be stored in the config file") String PATH_TO_LOG_FILE = "log/server.log";
	public static final @Temporary(reason = "Will be stored in the config file") String PATH_TO_USERS_FILE = "users/users.json";
	public static final @Temporary(reason = "Will be stored in the config file") String PATH_TO_CHATS_FILE = "chats/chats.json";
	public static final @Temporary(reason = "Will be stored in the config file") int MAX_NUM_THREADS = 10;
	public static final @Temporary(reason = "Will be stored in the config file") int MAX_NUM_USERS_CONNECTED = 10;
	public static final @Temporary(reason = "Will be stored in the config file") String PATH_TO_UPDATES_FILE = "updates/updates.json";

}
