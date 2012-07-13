package org.extricate.minimud;

import java.io.*;
import java.util.*;

/** Handles all player related routines, with the inclusion of
 *  sending messages to players.
 *
 *  @author  Tristan Greaves
 *  @version 1.0
 */

public class Player {

	/** Relevant connection class */
	protected Connection con;   

	/** Connection status. There are three different valid status
         *  codes...
         *
         *  0 = Just Connected.
	 *  1 = At "Enter Your Name:" phase.
	 *  2 = Playing Game.
         *
         */

	protected int status;   

	/** Character name. */
	protected String name; 

	/** Location number. */
	protected int location;

	/** Objects in inventory. */
	protected Vector<mudobject> objects_carried;

	/** Temporary storage for valid directions. This saves us having
	 *  to reload the location data to check the validity of a players
         *  move. It contains 0 if that move is not possible. Otherwise,
 	 *  it contains the destination location.
	 *
         */

	protected Integer n, e, s, w; 

	/** The no-arguments constructor is never used, and is here for Java
         *  syntax reasons only.
	 */

	public Player () { }

	/** A player is first created when a connection is formed, so the 
	 *  constructor really doesn't have to do a great deal.
	 */

	public Player (Connection x) {
	
		this.con = x;
		status=0;
		objects_carried = new Vector<mudobject>();

	}

	/** Returns the Connection associated with this player. */
	
	public Connection giveConnection () {

		return this.con;

	}

	/** Allows the setting of the Connection status.
	 *
	 *  @param s New status
	 */ 

	public void setStatus (int s) {

		this.status = s;

	}

	/** Returns the player's Connection status. */

	public int getStatus () {

		return this.status;

	}

	/** Sets the player's current location.
	 *
	 *  @param l Location number
	 */

	public void setLocation (int l) {

		this.location = l;

	}

	/** Sets the player's name.
 	 *
	 *  @param s Name
 	 */

	public void setName (String s) {

		this.name =s;

	}

	/** Returns the number of the location where the
 	 *  player is.
	 */

	public int getLocation () {

		return this.location;

	}

	/** Returns the name of the player. */

	public String getName() {

		return this.name;

	}

	/** Returns the objects carried. */

	public Vector<mudobject> getObjects() {

		return this.objects_carried;

	}


	/** Add a mudobjecty to the player's inventory 
	 *
	 *  @param o Object to add
	 */

	public void addToInventory ( mudobject o ) {

		objects_carried.addElement (o) ;

	}	

	/** Sends the contents of the specified file to the player.
         *  This should really only be an ASCII text file, otherwise it
	 *  it will do nasty things to the player's terminal!
         *
 	 *  @param s Filename
	 */

	public void sendFile (String s) {

                try {

                        File f = new File (Server.DATA_PATH + s);
                        FileInputStream in = new FileInputStream (f);
                        int size = (int) f.length();
                        int bytes_read=0;
                        byte[] data = new byte[size];

                        while (bytes_read < size) {

                                bytes_read += in.read (data,bytes_read,size-bytes_read);

                        }

                        in.close();

                        con.sendToSocket ( data, bytes_read );

                }

                catch (IOException e) { }

        }

	/** Sends the specified location to the player. This involves sending the
 	 *  basic text description, displaying the available exits, along with
	 *  any other players/objects that may be present.
	 *
	 *  @param l Location number
	 *  @param con Controller
	 */

	public void sendLocation (int l, Controller con) {

		try {

			String north, east, south, west;
			Vector<?> players_list, mud_objects;
			int temp=0;
			int i;

			players_list = con.getPlayers();
			mud_objects  = con.getmud_objects();

			File f = new File (Server.DATA_PATH + "rooms/" + l + ".exits");
			FileInputStream in = new FileInputStream (f);
			BufferedReader dis = new BufferedReader( new InputStreamReader (new DataInputStream(in) ));

			// First four lines contain movement data

			north = new String (dis.readLine()); 
			east  = new String (dis.readLine());
			south = new String (dis.readLine());
			west  = new String (dis.readLine());

			// Do a quick conversion...
			// We store these in the Player's structure, to save having
                        // to reload this file to check if a move is valid.

			n = new Integer (north);
			e = new Integer (east);
			s = new Integer (south);
			w = new Integer (west); 
	
			// Now simply send the text file of location description

			sendLine (" ");
			sendFile ("rooms/" + l + ".desc");

			// Any players here?

			sendLine (" ");

			for (i=0; i<players_list.size(); i++) {

				if ( ((Player)players_list.elementAt(i)).getLocation() == location ) {

					if ( ((Player)players_list.elementAt(i)).getName().compareTo (name) != 0) {

						// Make sure we don't say ourselves are here!

						sendLine ( ((Player)players_list.elementAt(i)).getName() + " is standing here.");

					}

				}

			}

			// Any objects here?

			for (i=0; i<mud_objects.size(); i++) {

				if ( ((mudobject)mud_objects.elementAt(i)).getLocation() == location) {

					sendLine ( ((mudobject)mud_objects.elementAt(i)).getRoom_desc() );

				}

			} 
	
			// Print out available exits.

			sendLineNoCR ("\nAvailable exits are: ");

			if (n.intValue() != 0) { sendLineNoCR ("north, "); temp=1; }	
			if (e.intValue() != 0) { sendLineNoCR ("east, "); temp=1;}
			if (s.intValue() != 0) { sendLineNoCR ("south, "); temp=1; }
			if (w.intValue() != 0) { sendLineNoCR ("west."); temp=1;}

			if (temp!=1) {

				sendLine ("None.");

			} else {
	
				sendLine (" ");

			}

			in.close();

		}

		catch (IOException e) { }

	}

	/** Sends a string to the player, without terminating it with a
         *  carriage return.
	 *
	 *  @param s String to send
	 */

	public void sendLineNoCR (String s) {

		con.out.print (s) ;

	}

	/** Sends a string to the player, terminating it with a carriage return.
	 *
	 *  @param s String to send
	 */

	public void sendLine (String s) {

		con.out.println (s) ;

	}

	/** Sends our cheesy MiniMUD prompt the player. */

	public void sendPrompt () {

		con.out.print ( "MiniMUD> ") ;

	}

}		
