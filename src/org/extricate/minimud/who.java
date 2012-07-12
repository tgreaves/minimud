package org.extricate.minimud;

import java.io.*;
import java.util.*;

/** Tells the player who is currently playing the MUD */

public class who extends mudcommand {

	/** Gives a complete list of all the players on the MUD, with the
	 *  inclusion of the player themselves.
	 */

	public void call(Controller con, Player p, StringTokenizer str) {

		Vector players_list;
		int i;

		players_list = con.getPlayers();

		p.sendLineNoCR ("The following users are connected:\n");

		for (i=0; i<players_list.size(); i++) {

			if ( ((Player)players_list.elementAt(i)).getStatus() != 1) {

				p.sendLine ( ((Player)players_list.elementAt(i)).getName()) ;

			}

		}		

		p.sendPrompt();

	}

}
