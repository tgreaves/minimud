package minimud;

import java.io.*;
import java.util.*;

/** Class to handle a player wishing to move south */

public class south extends mudcommand {

        /** If movement is possible, the player is moved. Anyone that can
         *  see the player leaving/arriving is informed of the fact.
         */

	public void call(Controller con, Player p, StringTokenizer str) {

		if ( p.s.intValue() != 0 ) {

			con.sendtoAll_inroom (p, "\n" + p.getName() + " leaves south.");
			p.setLocation ( p.s.intValue() );
			p.sendLocation (p.getLocation(),con ) ;  // Send new location details.	
			con.sendtoAll_inroom (p, "\n" + p.getName() + " enters from the north.");

		} else {

			p.sendLine ("You cannot go that way.");

		}

			p.sendPrompt();
	}

}
