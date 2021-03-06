//list of commands you can click on to start query
//switch between shells (option to select your own from a file .exe)
//automated commands
//skins

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;


public class View{
	public static File currentDirectory = new File("");
	public static JTextArea response = new JTextArea();
	public static JScrollPane responseScrollpanel = new JScrollPane(response);
	public static JFrame frame = new JFrame(System.getProperty("user.name")+"'s CMD");
	public static JTextArea consoleTextArea = new JTextArea();
	static Color backgroundColor = new Color(0,0,0);
	static Color fontColor = new Color(0x66FF00);
	static String welcome = "Welcome, "+System.getProperty("user.name")+".";
	static String windowsCMD = "C:\\Windows\\System32\\cmd.exe";
	static ArrayDeque<String> responseList = new ArrayDeque<String>();
	public static class SendCommandThread implements Runnable {
		public void run() {
			ProcessBuilder builder = new ProcessBuilder();
			//if windows
			if (slash() == "\\")
				builder = new ProcessBuilder(
						"cmd.exe", "/c", responseList.remove());
			//else if not windows send command to terminal
			else
				builder = new ProcessBuilder(
						"cmd.exe", "/c", responseList.remove());
			builder.redirectErrorStream(true);
			Process p;
			try {
				p = builder.start();
				BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
				String line;
				while (true) {
					line = r.readLine();
					if (line == null) { break; }
					responseList.push(line);
				}
				if (responseList.isEmpty())
				{
					builder = new ProcessBuilder(
							"cmd.exe", "/c", "cd " + currentDirectory.getAbsolutePath() + " && dir");
					builder.redirectErrorStream(true);
					p = builder.start();
					r = new BufferedReader(new InputStreamReader(p.getInputStream()));
					while (p.waitFor() != 1) {
						line = r.readLine();
						if (line == null) { break; }
						responseList.push(line);
					}

				}
				r.close();

			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		public static void main(String args[]) {
			(new Thread(new SendCommandThread())).start();
		}

	}
	public static String sendCommand(String command) throws IOException
	{
		if ((command.length()>2)&&(command.substring(0,2).toLowerCase().contains("cd")))
		{
			if (command.charAt(2)==' ')
			{
				if (command.contains(":"))
				{
					currentDirectory = new File(command.substring(3,command.length()));
				}
				else
				{
					currentDirectory = new File(currentDirectory.getAbsolutePath()+slash()+command.substring(3,command.length()));
				}
				return currentDirectory.getAbsolutePath();
			}
		}
		else if ((command.length()>4)&&(command.substring(0,5).toLowerCase().contains("mkdir")))
		{
			if (!command.contains(":"))
			{
				command = "cd " + currentDirectory.getAbsolutePath() + " && mkdir " + command.substring(5,command.length()) ;
			}
		}
		else if ((command.length()>1)&&(command.substring(0,2).toLowerCase().contains("md")))
		{
			if (!command.contains(":"))
			{
				command = "cd " + currentDirectory.getAbsolutePath() + " && md " + command.substring(3,command.length()) ;
			}
		}

		else if ((command.length()>4)&&(command.substring(0,5).toLowerCase().contains("rmdir")))
		{
			if (!command.contains(":"))
			{
				command = "cd " + currentDirectory.getAbsolutePath() + " && rmdir " + command.substring(5,command.length()) ;
			}
		}
		else if ((command.length()>1)&&(command.substring(0,2).toLowerCase().contains("rd")))
		{
			if (!command.contains(":"))
			{
				command = "cd " + currentDirectory.getAbsolutePath() + " && rd " + command.substring(3,command.length()) ;
			}
		}
		else if ((command.length()>1)&&(command.substring(0,2).toLowerCase().contains("rm")))
		{
			File f = new File(currentDirectory.getAbsolutePath()+slash()+command.substring(3,command.length()));
			if (!command.contains(":"))
			{
				if (f.isDirectory())
					command = "cd " + currentDirectory.getAbsolutePath() + " && rmdir " + command.substring(3,command.length()) ;
				else
					command = "cd " + currentDirectory.getAbsolutePath() + " && del " + command.substring(3,command.length()) ;
			}
			else
			{
				if (f.isDirectory())
					command = "rmdir " + command.substring(3,command.length()) ;
				else
					command = "del " + command.substring(3,command.length()) ;
			}
		}
		else if ((command.length()>3)&&(command.substring(0,4).toLowerCase().contains("type")))
		{
			if (!command.contains(":"))
			{
				if (slash() == "\\")
					command = "cd " + currentDirectory.getAbsolutePath() + " && type " + command.substring(5,command.length());
				else
					command = "cd " + currentDirectory.getAbsolutePath() + " && cat " + command.substring(5,command.length());
			}
		}
		else if ((command.length()>3)&&(command.substring(0,4).toLowerCase().contains("comp")))
		{
			if (!command.toLowerCase().equals("comp"))
			{
				if (!command.contains(":"))
				{
						command = "cd " + currentDirectory.getAbsolutePath() + " && comp " + command.substring(5,command.length());
				}
				else
				{
					command = "comp " + command.substring(5,command.length());
				}
			}
			else return ("You must select which files to compare!");
			System.out.println(command);
		}
		else if ((command.length()>2)&&(command.substring(0,3).toLowerCase().contains("cat")))
		{
			if (!command.contains(":"))
			{
				if (slash() == "\\")
					command = "cd " + currentDirectory.getAbsolutePath() + " && type " + command.substring(4,command.length());
				else
					command = "cd " + currentDirectory.getAbsolutePath() + " && cat " + command.substring(4,command.length());
			}
		}
		else if ((command.length()>2)&&(command.substring(0,3).toLowerCase().contains("cmd")))
		{
			command = "start cmd.exe";
		}
		else if ((command.length()>2)&&(command.substring(0,3).toLowerCase().contains("del")))
		{
			if (!command.contains(":"))
			{
				command = "cd " + currentDirectory.getAbsolutePath() + " && del " + command.substring(4,command.length());
			}
		}
		else if ((command.length()>5)&&(command.substring(0,6).toLowerCase().contains("delete")))
		{
			if (!command.contains(":"))
			{
				command = "cd " + currentDirectory.getAbsolutePath() + " && delete " + command.substring(7,command.length());
			}
		}
		else if ((command.length()>2)&&(command.substring(0,3).toLowerCase().contains("dir")))
		{
			command = "cd " + currentDirectory.getAbsolutePath() + " && dir";
		}
		//non program specific commands get evaluated first,
		else if ((command.length()>6)&&(command.substring(0,7).toLowerCase().contains("echo cd")))
		{
			return currentDirectory.getAbsolutePath();
		}
		else if ((command.length()>3)&&(command.substring(0,4).toLowerCase().contains("exit")))
		{
			System.exit(0);
		}
		else if ((command.length()>5)&&(command.substring(0,6).toLowerCase().contains("colorf")))
		{
			command = command.substring(7,command.length()).trim();
			String[] commands = new String[3];
			commands = command.split(",");
			fontColor = new Color(Integer.parseInt(commands[0]),Integer.parseInt(commands[1]),Integer.parseInt(commands[2]));
			response.setForeground(fontColor);
			consoleTextArea.setForeground(fontColor);
			return "Changed font color to R:"+commands[0]+",G:"+commands[1]+",B:"+commands[2];
		}
		else if ((command.length()>4)&&(command.substring(0,5).toLowerCase().contains("color")))
		{
			command = command.substring(6,command.length()).trim();
			String[] commands = new String[3];
			commands = command.split(",");
			backgroundColor = new Color(Integer.parseInt(commands[0]),Integer.parseInt(commands[1]),Integer.parseInt(commands[2]));
			response.setBackground(backgroundColor);
			consoleTextArea.setBackground(backgroundColor);
			return "Changed background color to R:"+commands[0]+",G:"+commands[1]+",B:"+commands[2];
		}
		//if a non program specific command was entered send command.
		responseList.push(command);
		Dimension d = new Dimension(500,frame.getHeight()-100);
		responseScrollpanel.getViewport().setPreferredSize(d);
		frame.pack();
		String res = "";
		SendCommandThread sendCommandThread =  new SendCommandThread();
		Thread thread = new Thread(sendCommandThread);
		thread.start();
		thread.run();

		while(!responseList.isEmpty()){
			res += responseList.removeFirst() + "\n";
		}
		if (consoleTextArea.getText().toLowerCase().equals("help"))
		{
			res+="COLOR	Change the background color. Usage: color 0-255,0-255,0-255 where 0-255 is your choice of any number between 0 and 255.";
			res+="\nCOLORF	Change the font color. Usage: colorf 0-255,0-255,0-255 where 0-255 is your choice of any number between 0 and 255.";
		}
		return res;

	}
	public static String slash(){
		String s = "";
		String OS = System.getProperty("os.name").toLowerCase();
		if (OS.indexOf("win") >= 0){
			s="\\";
		}

		else if (OS.indexOf("mac") >= 0){
			s="/";
		}

		else if (OS.indexOf("nux") >= 0){
			s="/";
		}
		else
			s="\\";
		return s;
	}
	public static void main(String[] args)
	{
		final JFrame frame = new JFrame(System.getProperty("user.name")+"'s CMD");
		final JPanel panel = new JPanel();
		consoleTextArea = new JTextArea("");

		response = new JTextArea(welcome);

		final JScrollPane responseScrollpanel = new JScrollPane(response);
		final JScrollPane queryScrollpanel = new JScrollPane(consoleTextArea);
		panel.addComponentListener(new ComponentListener() {
			public void componentResized(ComponentEvent e) {
				panel.setSize(frame.getSize());
				Dimension d = new Dimension(panel.getWidth()-25,panel.getHeight()-100);
				responseScrollpanel.getViewport().setPreferredSize(d); 
				d = new Dimension(panel.getWidth()-25,40);
				queryScrollpanel.getViewport().setPreferredSize(d); 
				if (frame.getHeight()<50)
				{
					frame.setSize(frame.getHeight(), 50);
				}
			}
			public void componentHidden(ComponentEvent arg0) {}
			public void componentMoved(ComponentEvent arg0) {}
			public void componentShown(ComponentEvent arg0) {}
		});
		response.setEditable(false);
		response.setLineWrap(true);
		panel.setLayout(new BorderLayout());
		consoleTextArea.addKeyListener(new KeyListener() {

			public void keyReleased(KeyEvent e) {}
			public void keyTyped(KeyEvent e) {}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					e.consume();
					try {
						response.setText(sendCommand(consoleTextArea.getText()));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					consoleTextArea.setText("");
				}

			}
		});

		consoleTextArea.setLineWrap(true);
		frame.setSize(500, 600);
		consoleTextArea.setBackground(backgroundColor);
		panel.setBackground(backgroundColor);
		frame.setBackground(backgroundColor);
		response.setBackground(backgroundColor);
		response.setForeground(fontColor);
		consoleTextArea.setForeground(fontColor);
		consoleTextArea.setCaret(new CMDCaret());
		panel.setBorder(new EtchedBorder());
		panel.add(BorderLayout.NORTH,responseScrollpanel);
		panel.add(BorderLayout.SOUTH,queryScrollpanel);
		frame.add(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setVisible(true);
	}
}
