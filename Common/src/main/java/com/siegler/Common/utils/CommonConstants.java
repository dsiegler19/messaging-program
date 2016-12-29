package com.siegler.Common.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class CommonConstants implements Constants {
	
	public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static final String COMMUNICATION_TYPE_TAG = "communication_type";
	
	public static final String REQUEST_TYPE_TAG = "request_type";
	
	public static final String RESPONSE_TYPE_TAG = "response_type";
	
	public static final String LOGIN_RESPONSE_TYPE_TAG = "login_answer";
	public static final String REGISTER_NEW_USER_TYPE_TAG = "register_new_user_answer";
	public static final String USER_INFORMATION_TYPE_TAG = "user_information_answer";
	
	public static final String DATE_TAG = "date";
	public static final String IP_TAG = "ip";
	public static final String GREETING_TAG = "greeting";
	
	public static final String UPDATES_TAG = "updates";
	
	public static final String USERNAME_TAG = "username";
	public static final String PASSWORD_TAG = "password";
	
	public static final String FRIENDS_TAG = "friends";
	public static final String CHATS_TAG = "chats";
	
	public static final String USERS_IN_CHAT_TAG = "users_in_chat";
	public static final String MESSAGES_IN_CHAT_TAG = "messages_in_chat";
	
	public static final String UUID_TAG = "uuid";
	
	public static final String SENDER_TAG = "sender";
	public static final String MESSAGE_CONTENT_TAG = "message_content";

}
