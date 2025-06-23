// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package edu.seg2105.client.backend;

import ocsf.client.*;

import java.io.*;

import edu.seg2105.client.common.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 
  
  String uid;
  
  private boolean isQuitting;

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String uid, String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.uid=uid;
    this.clientUI = clientUI;
    this.setQuitting(false);
    openConnection();
  }

  
  //Instance methods ************************************************
  public String getUid() {
	  return uid;
  }
  
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
    
    
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
    try
    {
      sendToServer(message);
    }
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {
    	clientUI.display("Error while closing the connection: " + e.getMessage());
    }
    clientUI.display("Client has quit gracefully.");
    System.exit(0);
  }
  
  @Override
  protected void connectionClosed() {
	  
	if (isQuitting()) {
		clientUI.display("Connection closed");
	}else {
		clientUI.display("The server has shut down");
	}
	
	
  }
  @Override
  protected void connectionException(Exception exception) {
	clientUI.display("The server has shut down");
	System.exit(0);
	
  }


  public boolean isQuitting() {
	return isQuitting;
  }


  public void setQuitting(boolean isQuitting) {
	this.isQuitting = isQuitting;
  }
}
//End of ChatClient class
