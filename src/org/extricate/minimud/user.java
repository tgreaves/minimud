package minimud;

import java.io.*;
import java.util.*;

/** Allows the user to change their name */

public class user extends mudcommand {

	/** Sets the player's name to the new one that they specify, as long
	 *  as someone doesn't already have it!
	 */

	public void call(Controller con, Player p, StringTokenizer str) {

		if ( str.hasMoreElements() == false ) {

			// No name provided

			p.sendLine ( "You need to specify a name.") ;
			p.sendPrompt();
			return;

		} else {

			int i;
                        int p_found=0;
			Vector players_list;
			String s;

			players_list = con.getPlayers(); 
			s = (String)str.nextElement() ;

                        // Check that the name entered doesn't already exist.

                        for (i=0; i< players_list.size(); i++) {

                               if ( ((Player)players_list.elementAt(i)).getName() != null) {

                                     if ( s.compareTo ( ((Player)players_list.elementAt(i)).getName() ) == 0) { p_found=1; break; }

                               }

                         }

                         if (p_found == 1) {

                              // Already exists, so.......
                              p.sendLine ("That name is already in use!");
			      p.sendPrompt();
                              return;
                         }

			p.setName ( s );
			p.sendLine ( "You are now known as " + p.getName() );
			p.sendPrompt();

		}

	}
}
