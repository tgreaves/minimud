package minimud;

import java.io.*;
import java.net.*;
import java.util.Date;

/**
 *  A very basic MUD (Multi User Dungeon) server, designed to be
 *  accessed by clients using the "telnet" protocol. 
 *
 *  @author      Tristan Greaves
 *  @version     1.0
 *  
*/

public class Server extends Thread {

	/** The default port for the MUD to run on, if not specified
	 *  on the command line.
	 */
	public final static int DEFAULT_PORT = 6969;

	/** Path of where to find our data files. */
	public final static String DATA_PATH = new String ("data/");
	
	/** Port for the MUD to be run on. */
	protected int port;

	/** The socket that the server binds to. */
	protected ServerSocket our_socket;

	/** The almighty Controller that oversee us. */
	protected Controller con;

	/** Exits Minimud when another part of the program
	 *  throws a particularly nasty exception. The exception along
         *  with a textual explanation is sent to the screen.
         *
	 *  @param e Exception
	 *  @param text Description of failure
        */

	public static void fail (Exception e, String text) {

		System.err.println (text + ":" + e);
                log (text + ":" + e);                  // Log error as well.
                log ("[System] MiniMUD end");
		System.exit(20);

	}

        /** Logs MiniMUD activity (time/date stamped). At the
         *  moment, we only output to System.out (instead of a file).
         *
         *  @param text Text to log
        */

	public static void log (String text) {

		Date cur_date = new Date();
		System.out.println ("[" + cur_date.toGMTString() + "] " + text);

	}

	/** Begins listening for connections, then launches the thread to handle 
         *  them.
         *
	 *  @param port Port to bind server to
        */

	public Server (int port) {

		if (port==0) { port = DEFAULT_PORT; }

		this.port=port;

                // Get the controller up and running.

		con = new Controller ();

		try {

			our_socket=new ServerSocket(port);

		}

		catch (IOException e) {

			fail (e, "Fatal error: Could not create listening socket.");

		}

		log ("[Server] Listening on port " + port);
		this.start();  // Go thread!

	}

	/** Sits waiting for client connections. When a client connects, a new "Connection"
 	 *  thread is spawned for each one.
        */

	public void run() {

		try {

			while (true) {

				Socket client_socket = our_socket.accept();
				Connection c = new Connection (client_socket, con);

			}

		}

		catch (IOException e) {

			fail (e, "Exception while listening for connections.");

		}

	}	

        /** Handles any command line arguments. The user can pass the port they
         *  want to use via the command line if they desire. If everything is
         *  okay a new Minimud is created.
	 *
	 *  @param args Command line arguments
        */

	public static void main (String[] args) {

		int port = 0 ;

                // In case people get a bit trigger happy with the
                // command line options, print some useful text instead.

		if (args.length > 1) {

			System.out.println ("Usage: java Minimud [Port]");
			System.exit(20);

		}		

                // If the user has specified a port, use that instead of the
                // default.

		if (args.length == 1) {

			try {

				port=Integer.parseInt (args[0]); 
			
			}

			catch (NumberFormatException e) {

				port=0;

			}

		}

                log ("[System] MiniMUD start");

		new Server (port);    // Go for it.

	}

}
