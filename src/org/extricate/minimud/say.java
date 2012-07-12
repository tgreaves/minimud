package org.extricate.minimud;

import java.util.*;

/** Says something to all players in the same room. */

public class say extends mudcommand {

	/** Sends the message to all the players in the same room, by working
	 *  through the players_list that the Controller has.
	 */

	public void call(Controller con, Player p, StringTokenizer str) {

		Vector<?> players_list;
		int i;
		String temp=new String();

		players_list = con.getPlayers();

		while ( str.hasMoreTokens() ) {

			temp=temp+str.nextToken()+" ";
	
		}

		for (i=0; i<players_list.size(); i++) {

                        if ( ((Player)players_list.elementAt(i)).getLocation() == p.getLocation() ) {

                                 if ( ((Player)players_list.elementAt(i)).getName().compareTo (p.getName()) != 0) {

                                          // This goes to other people in the room...
 
                                          ((Player)players_list.elementAt(i)).sendLine ( "\n" + p.getName() + " says, ' " + temp + "'");
					  ((Player)players_list.elementAt(i)).sendPrompt();

                                 } else {

					// This goes to the player speaking

					((Player)players_list.elementAt(i)).sendLine ( "You say, ' " + temp + "'");
					((Player)players_list.elementAt(i)).sendPrompt();

				} 

                         }

                }

	}

}
