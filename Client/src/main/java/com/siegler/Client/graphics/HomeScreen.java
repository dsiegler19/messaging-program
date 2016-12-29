package com.siegler.Client.graphics;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import com.siegler.Client.utils.ClientConstants;
import com.siegler.Common.logging.Logger;

public class HomeScreen extends JFrame {

	private static final long serialVersionUID = -1102586887502511212L;
	
	private static final String FONT = ClientConstants.DEFAULT_FONT;
		
	private List<String> chats;
	
	private JListWithXButton chatsDisplay;
		
	private JSplitPane split;
	
	public HomeScreen(List<String> chats){
		
		super("Dylan's Messaging Program");
				
		Logger.log("Creating a home screen...");
		
		chats.add("The cool chat");
		chats.add("Another cool chat");
		chats.add("A third cool chat");
		
		this.chats = chats;
		
		split = new JSplitPane();
		
		chatsDisplay = new JListWithXButton(chats);
		
		split.setLeftComponent(chatsDisplay);
		JTextArea t = new JTextArea();
		split.setRightComponent(t);
		split.setDividerLocation(250);
				
		split.addPropertyChangeListener(new PropertyChangeListener(){
			
			@Override
			public void propertyChange(PropertyChangeEvent e) {
				
				chatsDisplay.setPreferredSize(new Dimension(split.getDividerLocation(), getHeight()));
				
			}
			
		});
				
		this.add(split, BorderLayout.CENTER);
		
		this.setMinimumSize(new Dimension(400, 320));
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.pack();
		this.setSize(1000, 800);
		
		chatsDisplay.setPreferredSize(new Dimension(split.getDividerLocation(), this.getHeight()));
		
		t.requestFocus();
		
		Logger.log("Home screen created");
		
	}
	
	public void addChat(String name){
		
		this.chats.add(name);
		
	}

}
