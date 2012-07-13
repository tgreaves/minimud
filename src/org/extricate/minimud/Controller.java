package org.extricate.minimud;

import java.io.*;
import java.util.*;

/** The overall controller for the Minimud. 
 *
 *  @author   Tristan Greaves
 *  @version  1.0
 *
 */

public class Controller extends Thread {

	/** Queue of MUD messages. Things such as players issuing around are 
 	 *  stored in this queue, and picked up by the Controller which deals
	 *  with them.
	 */

	protected Vector<Object> queue;

	/** The list of players in the MUD, filled with Player objects. */
	protected Vector<Player> players_list; 

	/** The list of objects in the MUD, filled with mudobjects */
	protected Vector<mudobject> mud_objects;

	/** Adds a single object to the Minimud Events Queue 
	 *
	 *  @param obj The object to add, typically a queueItem
	 */ 

	public void AddToQueue (Object obj) {

		queue.addElement (obj) ;

	}

	/** Returns the list of players in the MUD */

	public Vector<Player> getPlayers () {

		return this.players_list;
	}

	/** Returns the list of objects in the MUD */

	public Vector<mudobject> getmud_objects () {

		return this.mud_objects;

	}

	/** Removes a player from the players list
	 *
	 *  @param p Player to be removed.
	 */

	public void removePlayer(Player p) {

		players_list.removeElement ( p ) ;

	}

	/** Constructor. Initialises event queue and players_list
	 *  vectors. Also reads in and initialises MUD objects list.
	 */

	public Controller () {

		queue        = new Vector<Object>();   // Our events queue
	 	players_list = new Vector<Player>();   // Our players list 	
		mud_objects  = new Vector<mudobject>();   // Our objects list

		String kw, desc, l;
		int obj_count=0;

		// Now we need to load and parse the object list from disk.
		// This just goes in a continuous loop until end of file is reached.
		// (Triggered by an IOException)
		//
		// Note that an IOException may occur if the file doesn't exist. This
		// is fine with us - the MUD simply won't have any objects.

		Server.log ("[Controller] Parsing objects data file.");
		BufferedReader dis;
		
		try {

			File f = new File (Server.DATA_PATH + "objects");

			FileInputStream in = new FileInputStream (f);
			dis = new BufferedReader ( new InputStreamReader (in));
			
			// FIXME: The way this loops via true is not appropriate.
			while (true) {

				kw = new String ( dis.readLine() );     // Object keyword
				desc = new String ( dis.readLine() );   // Object room description	
				l = new String ( dis.readLine() ) ;     // Location number

				// But we have l as a String. It must be an integer

				Integer itemp = new Integer (l) ;   // Now an "Integer"
			
				// Now we have all this, we can create the object!
				// (And then add it to the objects queue)

				mudobject temp_object = new mudobject (kw, desc, itemp.intValue() );
				mud_objects.addElement (temp_object); 
				obj_count++;

			}

		}

		catch ( IOException e ) {
			
 		}

		catch ( NullPointerException e2) {

		} 

		Server.log ("[Controller] " + obj_count +" objects loaded.");

		
		this.start();

	}

	/** Processes the entire queue whenever it is awoken. Calls external
         *  functions when required.
	 */

	public synchronized void run() {

                for (;;) {

        	        // Go to sleep for the moment.

			try {
	
				this.wait();

			}

			catch (InterruptedException e) {}

			// Okay, so is it a queue_user? I.e. a new user for us to deal with.
			// (Ignore any other events for now);

			while ( !queue.isEmpty() ) {

				//Server.log ("Controller Queue: " + queue.toString() );

				if ( queue.firstElement() instanceof queue_user ) {

					newPlayer ( (queue_user)queue.firstElement() );	

				}

				if ( queue.firstElement() instanceof queue_lost ) {

					// A connection has been lost. Destroy the relevant player.
				
					String temp_play = ((queue_lost)queue.firstElement()).givePlayer().getName();    // Store name.
					players_list.removeElement ( ((queue_lost)queue.firstElement()).givePlayer() );  // Remove player.
					sendtoAll (temp_play + " lost their connection.");                               // Notify others.
					Server.log ("[Lost Player] " + temp_play + " lost their connection");

				}

				if ( queue.firstElement() instanceof queue_line) {

					dealInput ( ((queue_line)queue.firstElement()).givePlayer(), ((queue_line)queue.firstElement()).giveString() );

				}

				queue.removeElementAt (0);

			}

		}


	}

	/** Registers a new player with the MUD, creating an instance for him/her.
	 *  Also sends introduction screen and prompts for a character name.
	 *
 	 *  @param qu User's queue structure
	 */

	public void newPlayer ( queue_user qu) {

		Player temp_player = new Player ( qu.giveConnection() );
		players_list.addElement (temp_player);
		
		temp_player.sendFile ("connect.txt");
		temp_player.sendLineNoCR ("Please enter your name: ");
		temp_player.setStatus (1);	// Enter name phase.

		temp_player.giveConnection().play = temp_player;

	}

	/** When a player has connected, future input ends up being dealt with by 
         *  this routine.
	 *
	 *  @param p Player
	 *  @param s String containing what they typed
	 */	

	public void dealInput (Player p, String s) {

		switch (p.getStatus()) {

			case 1:     // "Enter your name..."

				int i;
				int p_found=0;

				// Check that the name entered doesn't already exist.

				for (i=0; i< players_list.size(); i++) {

					if ( ((Player)players_list.elementAt(i)).getName() != null) {

						if ( s.compareTo ( ((Player)players_list.elementAt(i)).getName() ) == 0) { p_found=1; break; }

					}

				}

				if (p_found == 1) {

					// Already exists, so.......
					p.sendLine ("That name is already in use! Please try another.");
					p.sendLineNoCR ("Please enter your name: ");
					return;
				}

				p.setName ( s );             // Their MUD name is now set.
				p.setStatus ( 2 );           // Put them in "playing" mode.
				p.sendFile ("playing.txt");  // Send another info file.
				Server.log("[New Player] " + s) ;
				p.setLocation (1);           // Default location.
				p.sendLocation ( p.getLocation(), this );
				sendtoAll (s + " has entered the game.");
				break;

			case 2:     // General playing.

				Server.log ("[Command] " + p.getName() + ": " + s) ; 
				String comm;
				Class<?> test;
				mudcommand command = new mudcommand();
				StringTokenizer com;

				try {

					com = new StringTokenizer (s);

					comm = new String ( com.nextToken() );
				
				}

				catch (NoSuchElementException e2) {

					p.sendPrompt();
					return;       // It's just a blank line, who cares?

				}

				try {

					test = Class.forName ("org.extricate.minimud."+comm.toLowerCase()); 
				
				}

				catch (ClassNotFoundException e) {

					p.sendLine ("I don't understand that command."); 
					p.sendPrompt();
					return;
				 }

				catch (IllegalArgumentException e5) {

					p.sendLine ("Please don't use strange characters. :) ");
					p.sendPrompt();
					return;

				}

				 // Create a new instance of this class to call as a command.

				try {

					command = (mudcommand)test.newInstance();

				}

				catch (InstantiationException e3) { }
				catch (IllegalAccessException e4) { }

				// If we've got this far, let's try playing with the command.

				command.call(this, p, com);

		}
	}

	/** Sends a message to all the players on the MUD
	 *  It sends a newline character first in order to interrupt what a 
         *  player may already be typing. Note that this will cause an extra
         *  gap to be displayed on whoever <B>caused</B> the message to be sent.
	 * 
	 *  @param s String to send.
         */

	public void sendtoAll (String s) {

		for (int i=0; i<players_list.size(); i++) {

			// Don't send to those entering their name!

			if ( ((Player)players_list.elementAt(i)).getStatus() != 1) {

				((Player)players_list.elementAt(i)).sendLine ("\n" + s) ;
				((Player)players_list.elementAt(i)).sendPrompt();

			}

		}

	}

	/** Sends a message to all the players on the MUD, as with sendtoAll. 
         *  However, no extra CR is sent to the nominated player.
	 *
	 *  @param p Player to who an extra CR should <B>not</B> be sent.
	 *  @param s String to send.
	 */

	public void sendtoAll_noCR (Player p, String s) {

		for (int i=0; i<players_list.size(); i++) {

			// Don't send to those entering their name!

			if ( ((Player)players_list.elementAt(i)).getStatus() != 1) {

				if ( ((Player)players_list.elementAt(i)) == p) {

					((Player)players_list.elementAt(i)).sendLine (s) ;

				} else {

					((Player)players_list.elementAt(i)).sendLine ("\n" + s) ;

				}

				((Player)players_list.elementAt(i)).sendPrompt();

			}

		}
	}	

	/** Sends a message to all those that are in Player p's room, except
         *  for the player mentioned.
         *
         *  @param p Player to suck room number from, and do not send to this player.
	 *  @param s String to send.
	*/

	public void sendtoAll_inroom (Player p, String s) {

		int i;

                 for (i=0; i<players_list.size(); i++) {

                           if ( ((Player)players_list.elementAt(i)).getLocation() == p.getLocation() ) {

                                    if ( ((Player)players_list.elementAt(i)).getName().compareTo (p.getName()) != 0) {

                                            // Make sure we don't say ourselves are here!

                                            ((Player)players_list.elementAt(i)).sendLine ( s );   // Send the line.
					    ((Player)players_list.elementAt(i)).sendPrompt ();    // And the prompt.

                                    }

                            }

                  }

	}


}
