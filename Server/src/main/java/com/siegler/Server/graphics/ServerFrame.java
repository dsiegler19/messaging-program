package com.siegler.Server.graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.PrintStream;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.siegler.Server.server.Server;

public class ServerFrame extends JFrame {

	private static final long serialVersionUID = -646802809894909676L;
	
	private JSplitPane split;
	
	private JPanel leftPanel;
	private JPanel rightPanel;

	private JTextField textField;

	private JScrollPane scrollPane;
	
	private JTextArea outputArea;
	
	private JTextArea statusArea;
	
	private Server server;

	public ServerFrame() {

		super("Messanger Server");
				
		split = new JSplitPane();
		
		leftPanel = new JPanel();
		
		outputArea = new JTextArea();
		outputArea.setEditable(false);
		outputArea.setFont(new Font("Courier", Font.PLAIN, 14));
		
		scrollPane = new JScrollPane(outputArea);

		textField = new JTextField();
		textField.setFont(new Font("Courier", Font.PLAIN, 14));
		textField.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {}

			@Override
			public void keyPressed(KeyEvent e) {

				if (e.getKeyCode() == 10) {

					server.executeCommand(textField.getText());
					textField.setText("");

				}

			}

			@Override
			public void keyReleased(KeyEvent e) {}

		});
		
		BorderLayout leftLayout = new BorderLayout();
		
		leftPanel.setLayout(leftLayout);
		
		leftPanel.add(scrollPane, BorderLayout.CENTER);
		leftPanel.add(textField, BorderLayout.SOUTH);
		
		statusArea = new JTextArea();
		statusArea.setEditable(false);
		statusArea.setFont(new Font("Courier", Font.PLAIN, 14));
		statusArea.setText("STATUS\n");
		statusArea.append("OPERATIONAL");
		
		rightPanel = new JPanel();
		rightPanel.add(statusArea);
		
		split.setLeftComponent(leftPanel);
		split.setRightComponent(rightPanel);
		
		add(split);

		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(1000, 800);
		
		int screenWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int fontHeight = this.getGraphics().getFontMetrics(textField.getFont()).getHeight();
		
		textField.setMaximumSize(new Dimension(screenWidth, fontHeight + 2));

		split.setDividerLocation(700);
		
		System.setOut(new PrintStreamCapturer(outputArea, System.out, ">> "));
		System.setErr(new PrintStreamCapturer(outputArea, System.err, ">> "));

	}
	
	public void setServer(Server s){
		
		this.server = s;
		
	}

	class PrintStreamCapturer extends PrintStream {

		private JTextArea text;

		private String indent;
		
		private boolean atLineStart;

		public PrintStreamCapturer(JTextArea textArea, PrintStream capturedStream, String indent) {

			super(capturedStream);

			this.indent = indent;
			
			this.text = textArea;

			this.atLineStart = true;

		}

		public PrintStreamCapturer(JTextArea textArea, PrintStream capturedStream) {

			this(textArea, capturedStream, "");

		}

		private void writeToTextArea(String str) {

			if (text != null) {

				synchronized (text) {

					text.setCaretPosition(text.getDocument().getLength());
					text.append(str);

				}

			}

		}

		private void write(String str) {

			String[] s = str.split("\n", -1);

			if (s.length == 0) {

				return;

			}

			for (int i = 0; i < s.length - 1; i++) {

				writeWithPotentialIndent(s[1]);
				writeWithPotentialIndent("\n");
				atLineStart = true;

			}

			String last = s[s.length - 1];

			if (!last.equals("")) {

				writeWithPotentialIndent(last);

			}

		}

		private void writeWithPotentialIndent(String s) {

			if (atLineStart) {

				writeToTextArea(indent + s);

				atLineStart = false;

			}

			else {

				writeToTextArea(s);

			}

		}

		private void newLine() {
			
			write("\n");
			
		}

		@Override
		public void print(boolean b) {
			
			synchronized (this) {
				
				super.print(b);
				write(String.valueOf(b));
				
			}
			
		}

		@Override
		public void print(char c) {
			
			synchronized (this) {
				
				super.print(c);
				write(String.valueOf(c));
				
			}
			
		}

		@Override
		public void print(char[] s) {
			
			synchronized (this) {
				
				super.print(s);
				write(String.valueOf(s));
				
			}
			
		}

		@Override
		public void print(double d) {
			
			synchronized (this) {
				
				super.print(d);
				write(String.valueOf(d));
				
			}
			
		}

		@Override
		public void print(float f) {
			
			synchronized (this) {
				
				super.print(f);
				write(String.valueOf(f));
				
			}
			
		}

		@Override
		public void print(int i) {
			
			synchronized (this) {
				
				super.print(i);
				write(String.valueOf(i));
				
			}
			
		}

		@Override
		public void print(long l) {
			
			synchronized (this) {
				
				super.print(l);
				write(String.valueOf(l));
				
			}
			
		}

		@Override
		public void print(Object o) {
			
			synchronized (this) {
				
				super.print(o);
				write(String.valueOf(o));
				
			}
			
		}

		@Override
		public void print(String s) {
			
			synchronized (this) {
				
				super.print(s);
				
				if (s == null) {
					
					write("null");
					
				} 
				
				else {
					
					write(s);
					
				}
				
			}
			
		}

		@Override
		public void println() {
			
			synchronized (this) {
				
				newLine();
				super.println();
				
			}
			
		}

		@Override
		public void println(boolean x) {
			
			synchronized (this) {
				
				print(x);
				newLine();
				super.println();
				
			}
			
		}

		@Override
		public void println(char x) {
			
			synchronized (this) {
				
				print(x);
				newLine();
				super.println();
				
			}
			
		}

		@Override
		public void println(int x) {
			
			synchronized (this) {
				
				print(x);
				newLine();
				super.println();
				
			}
			
		}

		@Override
		public void println(long x) {
			
			synchronized (this) {
				
				print(x);
				newLine();
				super.println();
				
			}
			
		}

		@Override
		public void println(float x) {
			
			synchronized (this) {
				
				print(x);
				newLine();
				super.println();
				
			}
			
		}

		@Override
		public void println(double x) {
			
			synchronized (this) {
				
				print(x);
				newLine();
				super.println();
				
			}
			
		}

		@Override
		public void println(char x[]) {
			
			synchronized (this) {
				
				print(x);
				newLine();
				super.println();
				
			}
			
		}

		@Override
		public void println(String x) {
			
			synchronized (this) {
				
				print(x);
				newLine();
				super.println();
				
			}
			
		}

		@Override
		public void println(Object x) {
			
			String s = String.valueOf(x);
			
			synchronized (this) {
				
				print(s);
				newLine();
				super.println();
				
			}
			
		}

	}

}