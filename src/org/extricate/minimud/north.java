package org.extricate.minimud;

import java.util.*;

/** Class to handle a player wishing to move north */

public class north extends mudcommand {

        /** If movement is possible, the player is moved. Anyone that can
         *  see the player leaving/arriving is informed of the fact.
         */

	public void call(Controller con, Player p, StringTokenizer str) {

		if ( p.n.intValue() != 0 ) {

			con.sendtoAll_inroom (p, "\n" + p.getName() + " leaves north.");
			p.setLocation ( p.n.intValue() );
			p.sendLocation (p.getLocation(), con ) ;  // Send new location details.	
			con.sendtoAll_inroom (p, "\n" + p.getName() + " enters from the south.");

		} else {

			p.sendLine ("You cannot go that way.");

		}

			p.sendPrompt();
	}

}
