package org.extricate.minimud;

import java.util.*;

/** Allows a user to quit the game nicely */

public class quit extends mudcommand {

	/** The player is removed from the Controller's storage list, and all
	 *  player's are informed of their departure. The fact is logged, and
	 *  the connection finally killed.
	 *
	 *  Please note that the MUD <b>does</b> handle players that for
	 *  some reason "drop" the connection.
	 */

	public void call(Controller con, Player p, StringTokenizer str) {

		con.removePlayer (p) ;      // Remove the player from the list.
		con.sendtoAll (p.getName() + " left the game.");
		Server.log ("[Lost Player] " + p.getName() + " left the game.");
		p.giveConnection().diediedie();

	}

}
