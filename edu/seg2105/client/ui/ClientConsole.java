package edu.seg2105.client.ui;
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import java.util.Scanner;

import edu.seg2105.client.backend.ChatClient;
import edu.seg2105.client.common.*;

/**
 * This class constructs the UI for a chat client.  It implements the
 * chat interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ServerConsole 
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge  
 * @author Dr Robert Lagani&egrave;re
 */
public class ClientConsole implements ChatIF 
{
  //Class variables *************************************************
  
  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Instance variables **********************************************
  
  /**
   * The instance of the client that created this ConsoleChat.
   */
  ChatClient client;
  
  
  
  /**
   * Scanner to read from the console
   */
  Scanner fromConsole; 

  
  //Constructors ****************************************************

  /**
   * Constructs an instance of the ClientConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public ClientConsole(String uid, String host, int port) 
  {
    try 
    {
      client= new ChatClient(uid, host, port, this);
      
      
    } 
    catch(IOException exception) 
    {
      System.out.println("Error: Can't setup connection!"
                + " Terminating client.");
      System.exit(1);
    }
    
    // Create scanner object to read from console
    fromConsole = new Scanner(System.in); 
    
    
  }

  
  //Instance methods ************************************************
  
  /**
   * This method waits for input from the console.  Once it is 
   * received, it sends it to the client's message handler.
   */
  public void accept() 
  {
    try
    {

      String message;
      
      while (true) 
      {
        message = fromConsole.nextLine().trim();
        if (message.startsWith("#")) {
        	if (message.startsWith("#quit")) {
        		// code to quit
        		client.setQuitting(true);
        		try {
        			client.closeConnection();
        		} catch (IOException e) {
        			e.printStackTrace();
        		}
        		
        		System.exit(0);
        		
        		
        		
        		//client.quit();
        		// note that this already checks if the client is disconnected
        		// because of how quit() and closeConnection() work
        	}
        	else if (message.startsWith("#logoff")) {
        		// code do disconnect the client 
        		// without quitting 
        		client.setQuitting(true);
        		try {
        			client.closeConnection();
        		}
        		catch (IOException e) {
        			System.out.println("Error while logging off "+ e.getMessage());
        		}
        	}
        	else if (message.startsWith("#sethost")) {
        		if(client.isConnected() == false) {
        			// call setHost method
        			String[] parts = message.split("<");
        			if (parts.length>=2) {
        				String host = parts[1].replace(">", "");
        				client.setHost(host);
        			}
        		}
        		else {
        			// display error message
        			this.display("Error: you cannot set host unless you are logged off");
        		}
        	}
        	else if (message.startsWith("#setport")) {
        		if(client.isConnected() == false) {
        			// call setPort method
        			String[] parts = message.split("<");
        			if (parts.length>=2) {
        				int port = Integer.parseInt(parts[1].replace(">", ""));
        				client.setPort(port);
        			}
        		}
        		else {
        			// display error message
        			this.display("Error: you cannot set port unless you are logged off");
        		}
        	}
        	else if (message.startsWith("#login")) {
        		if(client.isConnected() == false) {
        			// log in
        			try {
        				client.openConnection();
        			}
        			catch (IOException e) {
        				this.display("Error: "+e.getMessage());
        			}
        		}
        		else {
        			// display error message
        			this.display("Error: You are already logged in");
        		}
        	}
        	else if (message.startsWith("#gethost")) {
        		this.display(client.getHost());
        	}
        	else if (message.startsWith("#getport")) {
        		this.display(String.valueOf(client.getPort()));
        	}
        	else {
        		this.display("Error: unknown command");
        	}
        } else {
        	client.handleMessageFromClientUI(message);
        }
        
      }
    } 
    catch (Exception ex) 
    {
      System.out.println
        ("Unexpected error while reading from console!");
    }
  }

  /**
   * This method overrides the method in the ChatIF interface.  It
   * displays a message onto the screen.
   *
   * @param message The string to be displayed.
   */
  public void display(String message) 
  {
    System.out.println("> " + message);
  }

  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of the Client UI.
   *
   * @param args[0] The host to connect to.
   */
  public static void main(String[] args) 
  {
    String host = "";
    int port = 0;
    String uid = null;
    try {
    	uid = args[0];
    }
    catch(ArrayIndexOutOfBoundsException e) {
    	System.out.println("ERROR - No login ID specified. Connection aborted.");
    	System.exit(1);
    }
    try
    {
      host = args[1];
      port = Integer.parseInt(args[2]);
    }
    catch(ArrayIndexOutOfBoundsException e)
    {
      host = "localhost";
      port = DEFAULT_PORT;
    }
    ClientConsole chat= new ClientConsole(uid, host, port);
    
    try {
    	chat.client.openConnection();
    	chat.client.handleMessageFromClientUI("#login<" + chat.client.getUid() + ">");
    }
    catch(IOException e) {
    	System.out.println("Could not open connection: " + e.getMessage());
    	System.exit(1);
    }
    
    chat.accept();  //Wait for console data
    
  }
}
//End of ConsoleChat class
