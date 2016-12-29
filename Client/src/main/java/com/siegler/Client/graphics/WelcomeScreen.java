package com.siegler.Client.graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.siegler.Client.client.Client;
import com.siegler.Client.utils.ClientConstants;
import com.siegler.Common.communication.response.LoginResponseType;
import com.siegler.Common.communication.response.RegisterNewUserResponseType;
import com.siegler.Common.logging.Logger;

public class WelcomeScreen extends JFrame{

	private static final long serialVersionUID = 176006065630143082L;
	
	private static final int GREETING_SIZE = 28;
	private static final int NEWS_TITLE_SIZE = 12; // Note this is HTML font size not Java size
	private static final int CONTENT_SIZE = 6; // Note this is HTML font size not Java size
	private static final int LOGIN_SIZE = 18;
	
	private static final String FONT = ClientConstants.DEFAULT_FONT;
	
	private Client client;
	
	private JScrollPane scroll;
	
	private JTextPane title;
	private JTextPane updatesAndNews;
	
	private JPanel loginPanel;
	
	private JLabel responseAlertLabel;
	private JLabel usernameLabel;
	private JTextField usernameInput;
	private JLabel passwordLabel;
	private JPasswordField passwordInput;
	private JButton loginButton;
	private JButton registerNewUserButton;
	
	public WelcomeScreen(Client cl, String greeting, String[] newsHeaders, String[] contents){
		
		super("Welcome Screen");
				
		this.client = cl;
		
		title = new JTextPane();
		
		StyledDocument doc = title.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		
		title.setStyledDocument(doc);
		
		title.setFont(new Font(FONT, Font.BOLD, GREETING_SIZE));
		title.setText(greeting);
		title.setEditable(false);
		title.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		updatesAndNews = new JTextPane();
		updatesAndNews.setContentType("text/html");
		updatesAndNews.setText(buildHtml(newsHeaders, contents));
		updatesAndNews.setEditable(false);
		
		scroll = new JScrollPane(updatesAndNews);
		
		responseAlertLabel = new JLabel();
		responseAlertLabel.setFont(new Font(FONT, Font.PLAIN, LOGIN_SIZE));
		responseAlertLabel.setForeground(Color.RED);
		
		usernameLabel = new JLabel("Username:");
		usernameLabel.setFont(new Font(FONT, Font.PLAIN, LOGIN_SIZE));
		
		usernameInput = new JTextField();
		usernameInput.setFont(new Font(FONT, Font.PLAIN, LOGIN_SIZE));
		
		passwordLabel = new JLabel("Password:");
		passwordLabel.setFont(new Font(FONT, Font.PLAIN, LOGIN_SIZE));
		
		passwordInput = new JPasswordField();
		passwordInput.setFont(new Font(FONT, Font.PLAIN, LOGIN_SIZE));
		
		passwordInput.addKeyListener(new KeyListener(){

			@Override
			public void keyTyped(KeyEvent e) {}

			@Override
			public void keyPressed(KeyEvent e) {
				
				if(e.getKeyCode() == 10){
					
					LoginResponseType response = client.requestLogin(usernameInput.getText(), passwordInput.getPassword());
					
					client.dispatchOnType(response);
					
				}
				
			}

			@Override
			public void keyReleased(KeyEvent e) {}
			
		});
		
		loginButton = new JButton();
		loginButton.setText("Login");
		loginButton.setFont(new Font(FONT, Font.PLAIN, LOGIN_SIZE));
		
		loginButton.addActionListener(e -> {
			
			LoginResponseType response = client.requestLogin(usernameInput.getText(), passwordInput.getPassword());
			
			client.dispatchOnType(response);
			
		});
		
		registerNewUserButton = new JButton();
		registerNewUserButton.setText("Register New User");
		registerNewUserButton.setFont(new Font(FONT, Font.PLAIN, LOGIN_SIZE));
		
		registerNewUserButton.addActionListener(e ->{
			
			RegisterNewUserResponseType response = client.requestRegisterNewUser(usernameInput.getText(), passwordInput.getPassword());
			
			client.dispatchOnType(response);
			
		});
				
		loginPanel = new JPanel(new GridBagLayout()){
			
			private static final long serialVersionUID = -2684243694758111447L;

			public Dimension getPreferredSize() {
				
				return new Dimension(this.getWidth(), 80);
				
			};
			
		};
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 3;
		c.weightx = 0.225;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.EAST;
		loginPanel.add(responseAlertLabel, c);
			
		c.gridwidth = 1;
		c.gridy = 1;
		loginPanel.add(usernameLabel, c);
		
		c.gridx = 0;
		c.gridy = 2;
		c.anchor = GridBagConstraints.WEST;
		loginPanel.add(usernameInput, c);
		
		c.gridx = 1;
		c.gridy = 1;
		loginPanel.add(passwordLabel, c);
		
		c.gridx = 1;
		c.gridy = 2;
		loginPanel.add(passwordInput, c);
		
		c.gridx = 2;
		c.gridy = 1;
		c.weightx = 0.1;	
		loginPanel.add(loginButton, c);
		
		c.gridy = 2;
		loginPanel.add(registerNewUserButton, c);

		this.add(title, BorderLayout.NORTH);
		this.add(scroll, BorderLayout.CENTER);
		this.add(loginPanel, BorderLayout.SOUTH);
				
		this.setSize(1000, 800);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setMinimumSize(new Dimension(400, 320));
		
		usernameInput.setText("dsiegler19");
		passwordInput.setText("Password123");
				
	}
	
	private String buildHtml(String[] newsHeaders, String[] contents){
		
		StringBuilder str = new StringBuilder();
		
		str.append("<html>");
		
		for(int i = 0; i < newsHeaders.length; i++){
			
			String title = newsHeaders[i];
			
			String content = "";
			
			try{
				
				content = contents[i];
				
			} catch(IndexOutOfBoundsException e){
				
				Logger.log("The number of news headers didn't match the number of contents!");
				
			}
						
			str.append("<b><font face=\"" + FONT + "\"font size=\"" + NEWS_TITLE_SIZE + "\">" + title + "</b></font><br />");
			str.append("<font face=\"" + FONT + "\" size=\"" + CONTENT_SIZE + "\">" + content + "</font><br /><br />");
						
		}
		
		str.append("</html>");
		
		return str.toString();
		
	}
	
	public void setAlertText(String text){
		
		responseAlertLabel.setText(text);
		
	}
	
	public String getUsername(){
		
		if(usernameInput != null){
			
			return usernameInput.getText();
			
		}
		
		return null;
				
	}
	
	public String getPassword(){
		
		if(passwordInput != null){
			
			return new String(passwordInput.getPassword());
			
		}
		
		return null;
		
	}

}
