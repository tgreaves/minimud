package minimud;

import java.io.*;
import java.util.*;

/** Class to handle a player wishing to move east */

public class east extends mudcommand {

	/** If movement is possible, the player is moved. Anyone that can
	 *  see the player leaving/arriving is informed of the fact. 
	 */

	public void call(Controller con, Player p, StringTokenizer str) {

		if ( p.e.intValue() != 0 ) {

			con.sendtoAll_inroom (p, "\n" + p.getName() + " leaves east.");
			p.setLocation ( p.e.intValue() );
			p.sendLocation (p.getLocation(),con ) ;  // Send new location details.	
			con.sendtoAll_inroom (p, "\n" + p.getName() + " enters from the west.");

		} else {

			p.sendLine ("You cannot go that way.");

		}

			p.sendPrompt();
	}

}
