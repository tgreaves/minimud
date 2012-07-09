package minimud;

import java.io.*;
import java.net.*;

/** Handles individual connections to the Minimud
 *  (Each connection has its own Connection class)
 *
 *  @author  Tristan Greaves
 *  @version 1.0
 *  
 */

public class Connection extends Thread {

	/** The Socket that belongs to this client */
	protected Socket client;

	/** Where we receive input from the player's terminal */
	protected DataInputStream in;

	/** Where we send output to go to the player's terminal */
	protected PrintStream out;

	/** The almighty Controller that overlooks all these */
	protected Controller conn;

	/** The Player class associated with the connection */
	public Player play;

	/** Send the contents of stuff[] to the player's terminal.
	 *
	 *  @param stuff[] Data to send
	 *  @param len Length of data to be sent
	 */

	public void sendToSocket (byte stuff[], int len) {

		out.write ( stuff, 0, len ) ;

	}

	/** Kill any socket connection, and stop the thread */

	public void diediedie () {

		try {

			client.close();
			this.stop();

		}

		catch (IOException e) { }

	}

	/** Initialize the input and output streams to the socket and
         *  start the thread.
         *
         *  @param client_socket The socket where the new client is.
         *  @param con The almighty controller.
         */

	public Connection (Socket client_socket, Controller con) {

		client=client_socket;
          	conn=con; 

                // Log where the connection is coming from. 

		Server.log ("[Connection] New connection from " + client.getInetAddress().getHostName() + " (" + client.getInetAddress().getHostAddress() + ")" );

		try {

			in = new DataInputStream (client.getInputStream() );
			out= new PrintStream     (client.getOutputStream() );

		}

		catch (IOException e) {

			try {

				client.close();
				Server.log ("Connection closed.");

			}

			catch (IOException e2) {};

			Server.log ("Exception while getting socket streams: " + e);
			return;

		}

		this.start();

	}

	/** Provide the service to the client. Queues stuff for the controller if it 
         *  needs to deal with anything.
         */

        public void run() {

               String line;

		conn.AddToQueue ( new queue_user (this) ) ;  // Pass whole user stuff.
		synchronized (conn) {conn.notify();}         // We can't let the user hang there waiting.

               try {

                       for (;;) {

                               line = in.readLine();
                           
			       if (line==null) { 

				   // Our connection has been pulled from underneath us.
                                   // Log the fact and exit the thread.

				   if ( play.getStatus() > 1 ) {

					// They are a registered player etc.

			           	Server.log ("[Connection] Connection reset by " + play.getName() );
				   	conn.AddToQueue ( new queue_lost (this.play) ) ;
				   	synchronized (conn) {conn.notify();}

				    } else {

					// Not yet registered, so don't bother telling controller. It doesn't care.

					Server.log ("[Connection] Unregistered connection lost");

				    }	

				    break;

		               }			
		
				// Test if we can add stuff to event loop queue

				conn.AddToQueue ( new queue_line (play, line) );       

                               synchronized (conn) {conn.notify();}    // Testing!

                       }

               }

               catch (IOException e) {


			Server.log ("Exception while reading.");

		}

        }

}
				
			
