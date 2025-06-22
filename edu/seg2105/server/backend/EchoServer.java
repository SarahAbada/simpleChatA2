package edu.seg2105.server.backend;
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 


import java.io.IOException;

import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
    if(msg instanceof String) {
    	String message = (String) msg;
    	if (message.startsWith("#login")) {
    		// handle login
    		if (client.getInfo("Login id") != null) {
    			try {
					client.sendToClient("Error: you can only log in once.");
					return;
				} catch (IOException e) {
					e.printStackTrace();
				}
    			
    		} else {
    			// the user is doing a new login
    			System.out.println("A new client has connected to the server.");
    			// first step: send the message as you do with all messages
    			String mssWuid = "Message received: "+message +"from "+(String)client.getInfo("Login id");
    			System.out.println(mssWuid);
    			
    			
    			// next step: add their login id to their user info
    			String[] parts = message.split("[<>]");
    			if (parts.length>=2) {
    				String theUid = parts[1];
    				client.setInfo("Login id",theUid);
    				// i also need to send the login messages
    				System.out.println("<"+client.getInfo("Login id")+">"+" has logged on");
    				sendToAllClients("<"+client.getInfo("Login id")+">"+" has logged on");
    			}
    			else {
    				System.out.println("Invalid login format. Use #login<your_id>");
    			}
    		}
    		
    	}
    	else {
    		if (client.getInfo("Login id") != null) {
    			// append uid to message and echo to all clients
    			String mssWuid = "Message received: "+message +"from "+(String)client.getInfo("Login id");
    			sendToAllClients(mssWuid);
    			
        		//String msgWuid = "["+(String)client.getInfo("Login id")+"]: " + message;
        		//sendToAllClients(msgWuid);
    		} else {
    			try {
					client.sendToClient("Error: you must be logged in to send messages.");
				} catch (IOException e) {
					e.printStackTrace();
				}
    			return;
    		}
    		
    	}
          
    }
	
  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }
  
  @Override
  protected void clientConnected(ConnectionToClient client) {
	  // print out a nice message
	  System.out.println("[Server] Client connected from: " + client.getInetAddress().getHostAddress());
  }
  
  @Override
  synchronized protected void clientDisconnected(ConnectionToClient client) {
	  // print out a nice message
	  System.out.println("[Server] Client connected from: " + client.getInetAddress().getHostAddress());
  }
  
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    EchoServer sv = new EchoServer(port);
    
    try 
    {
      sv.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }
}
//End of EchoServer class
