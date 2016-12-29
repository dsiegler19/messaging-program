package com.siegler.Common.logging;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Queue;

import com.siegler.Common.utils.Config;

public class Logger {
	
	private static File logFile;
	private static FileWriter logFileWriter;
	private static LoggingSeverity defaultSeverity;
	
	private static boolean hasLogFile = false;
	private static boolean initialized = false;
	
	private static Queue<String> toWrite = new LinkedList<String>();
			
	public static void setLogFile(Config c){
		
		hasLogFile = true;
		
		defaultSeverity = LoggingSeverity.INFO;
		logFile = c.getLogFile();
		
		if(!logFile.exists()){
			
			try {
				
				logFile.createNewFile();
				
			} catch (IOException e) {

				Logger.log("Error creating the log file!", e, LoggingSeverity.SEVERE, false);
				
			}
			
		}
		
		try {
			
			logFileWriter = new FileWriter(logFile, true);
			
		} catch (IOException e) {

			Logger.log("Error creating the log file writer!", e, LoggingSeverity.SEVERE, false);
			
		}
						
		Runtime.getRuntime().addShutdownHook(new Thread(){
			
			public void run(){
				
				try {
					
					logFileWriter.close();
					
				} catch (IOException e) {

					log("Error closing the log file writer!", e, LoggingSeverity.SEVERE, false);
					
				}
				
			}
			
		});
		
		Logger.log("Logger initialized");
				
	}
	
	public static void initialize(){
		
		if(!initialized){
			
			onlyWriteToFile("------------------------------------");
			onlyWriteToFile("STARTING LOG FILE");
			onlyWriteToFile(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
			onlyWriteToFile("------------------------------------");
			
		}
		
		initialized = true;
		
	}
	
	public static void setDefultLoggingSeverity(LoggingSeverity newSeverity){
		
		defaultSeverity = newSeverity;
		
	}
	
	public static void log(String message){
		
		log(message, defaultSeverity);
		
	}
	
	public static void log(String message, boolean writeToFile){
		
		if(defaultSeverity == null){
			
			defaultSeverity = LoggingSeverity.INFO;
			
		}
				
		if(writeToFile){
			
			log(message, defaultSeverity);
			
		}
		
		else{
			
			String[] split = message.split("\n");
			
			for(String s : split){
				
				String str = getCurrentTime() + " [" + defaultSeverity.toString() + "] " + s;
			
				System.out.println(str);
				
				toWrite.add(str);
			
			}
			
		}
		
	}
	
	public static void log(String message, Exception e){
		
		log(message + "\n", e, defaultSeverity);
		
	}
	
	public static void log(String message, LoggingSeverity severity){
		
		checkIfHasLogFile();
		
		emptyToWriteStack();
		
		if(message == null){
			
			log("null");
			
			return;
			
		}
		
		String[] split = message.split("\n");
		
		for(String s : split){
			
			String toWrite = getCurrentTime() + " [" + severity.toString() + "] " + s;
			
			outputAndWrite(toWrite);
		
		}
		
	}
	
	public static void log(String message, Exception e, LoggingSeverity severity){
		
		checkIfHasLogFile();
		
		emptyToWriteStack();
		
		e.printStackTrace();
		
			
			String[] split = message.split("\n");
			
			for(String s : split){
				
				String str = getCurrentTime() + " [" + severity.toString() + "] " + s;
				
				outputAndWrite(str);
				
			}
			
		try {
						
			String trace = "";
			PrintWriter w = new PrintWriter(trace);
			e.printStackTrace(w);
			
			logFileWriter.write(trace + "\n");
			
			logFileWriter.flush();
			
		} catch (IOException ex) {

			log("Error writing a log to the log file", ex, LoggingSeverity.SEVERE, false);
			
		}
		
	}
	
	public static void log(String message, Exception e, LoggingSeverity severity, boolean writeToLoggingFile){
		
		if(defaultSeverity == null){
			
			defaultSeverity = LoggingSeverity.INFO;
			
		}
		
		if(writeToLoggingFile){
			
			log(message, e, severity);
			
		}
		
		else{
			
			String[] split = message.split("\n");
			
			for(String s : split){
							
				System.out.println(getCurrentTime() + " [" + severity.toString() + "] " + s);
			
			}
			
			e.printStackTrace();
			
			String trace = "";
			PrintWriter w = null;
			
			try {
				
				w = new PrintWriter(trace);
				
			} catch (FileNotFoundException ex) {

				System.err.println("FATAL ERROR MAKING THE PRINT WRITER");
				ex.printStackTrace();
				System.exit(1);
				
			}
			
			e.printStackTrace(w);
			
			toWrite.add("[" + severity.toString() + "] " + message);
			toWrite.add(trace);
			
		}
		
	}
	
	private static void onlyWriteToFile(String message){
		
		if(hasLogFile){
			
			String[] split = message.split("\n");
			
			for(String s : split){
				
				String toWrite = getCurrentTime() + " [" + defaultSeverity.toString() + "] " + s;
				
				try {
					
					logFileWriter.write(toWrite + "\n");
					logFileWriter.flush();
					
				} catch (IOException e) {

					log("Error writing a log to the log file", e, LoggingSeverity.SEVERE, false);
					
				}
			
			}
			
		}
		
		else{
			
			toWrite.add(message);
			
		}
		
	}
	
	private static void outputAndWrite(String s){
		
		System.out.println(s);
		
		try {
			
			logFileWriter.write(s + "\n");
			logFileWriter.flush();
			
		} catch (IOException e) {

			log("Error writing a log to the log file", e, LoggingSeverity.SEVERE, false);
			
		}
				
	}
	
	private static void emptyToWriteStack(){
		
		while(!toWrite.isEmpty()){
			
			String str = toWrite.remove();
			
			try {
				
				String[] split = str.split("\n");
				
				for(String s : split){
								
					logFileWriter.write(s + "\n");
				
				}
								
			} catch (IOException e) {

				System.err.println("FATAL ERROR MAKING THE PRINT WRITER");
				e.printStackTrace();
				System.exit(1);
				
			}
			
		}
		
	}
	
	private static String getCurrentTime(){
		
		Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(cal.getTime());
		
	}
	
	private static void checkIfHasLogFile(){
		
		if(!hasLogFile){
			
			throw new IllegalStateException("The logger have a log file!");
			
		}
		
	}

}
