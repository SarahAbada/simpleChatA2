package edu.seg2105.server.ui;

import java.io.*;
import java.util.Scanner;
// the two imports above are needed to handle user input and i/o

import edu.seg2105.server.backend.EchoServer;
import edu.seg2105.client.common.*;
// these two imports above are needed for the methods the class must use


public class ServerConsole implements ChatIF{
	
	  //Class variables *************************************************
	  
	  /**
	   * The default port to connect on.
	   */
	  final public static int DEFAULT_PORT = 5555;
	  
	  //Instance variables **********************************************
	  
	  /**
	   * The instance of the client that created this ConsoleChat.
	   */
	  EchoServer server;
	  
	  /**
	   * Variable tracking whether the server is closed
	   */
	  boolean isClosed;
	  
	  /**
	   * Scanner to read from the console
	   */
	  Scanner fromConsole; 
	  
	  //Constructors ****************************************************

	  /**
	   * Constructs an instance of the ServerConsole UI.
	   *
	   * @param host The host to connect to.
	   * @param port The port to connect on.
	   */
	  public ServerConsole(int port) 
	  {
	    server= new EchoServer(port);
	    try {
			server.listen();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    isClosed = false;
	    // Create scanner object to read from console
	    fromConsole = new Scanner(System.in); 
	  }

	  //Instance methods ************************************************
	  
	  /**
	   * This method waits for input from the console.  Once it is 
	   * received, it sends it to the server's message handler.
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
	        		try {
	        			server.close();
	        			isClosed = true;
	        		}
	        		catch(IOException e) {
	        			this.display("Error in quitting the server: "+ e.getMessage());
	        		}
	        		
	        		System.exit(0);
	        	}
	        	else if (message.startsWith("#stop")) {
	        		// code do stop listening 
	        		try {
	        			server.stopListening();
	        		}
	        		catch (Exception e) {
	        			this.display("Unexpected error: "+ e.getMessage());
	        		}
	        		
	        	}
	        	else if (message.startsWith("#close")) {
	        		try {
	        			//server.sendToAllClients("The server has shut down")
	        			server.close();
	        			isClosed = true;
	        		}
	        		catch(IOException e) {
	        			this.display("Error in closing the server: "+ e.getMessage());
	        		}
	        		
	        	}
	        	else if (message.startsWith("#setport")) {
	        		if(isClosed == true) {
	        			// call setPort method
	        			String[] parts = message.split("<");
	        			if (parts.length>=2) {
	        				int port = Integer.parseInt(parts[1].replace(">", ""));
	        				server.setPort(port);
	        			}
	        		}
	        		else {
	        			// display error message
	        			this.display("Error: you cannot set port unless the server is closed");
	        		}
	        	}
	        	else if (message.startsWith("#start")) {
	        		if(server.isListening() == false) {
	        			// log in
	        			try {
	        				server.listen();
	        				isClosed = false;
	        			}
	        			catch (IOException e) {
	        				this.display("Error: "+e.getMessage());
	        			}
	        		}
	        		else {
	        			// display error message
	        			this.display("Error: the server is already listening");
	        		}
	        	}
	        	else if (message.startsWith("#getport")) {
	        		this.display(String.valueOf(server.getPort()));
	        	}
	        	else {
	        		this.display("Error: unknown command");
	        	}
	        } else {
	        	server.sendToAllClients("SERVER MSG >" + message);
	        	System.out.println("SERVER MSG >" + message);
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
	   * This method is responsible for the creation of the Server UI.
	   *
	   * @param args[0] the port to connect to 
	   */
	  public static void main(String[] args) 
	  {
	    
	    int port = 0;

	    try
	    {
	      port = Integer.parseInt(args[0]);
	    }
	    catch(ArrayIndexOutOfBoundsException e)
	    {
	      port = DEFAULT_PORT;
	    }
	    ServerConsole theServer= new ServerConsole(port);
	    theServer.accept();  //Wait for console data
	  }

}
