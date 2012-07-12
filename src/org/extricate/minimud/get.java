package minimud;

import java.io.*;
import java.util.*;

/** Allows a player to pick up a mud object which is in the room */

public class get extends mudcommand {

	/** Allows a player to pick up a mud object which is in the room */

	public void call(Controller con, Player p, StringTokenizer str) {

		Vector mud_objects;
		int i;
		int found=-1;
		String temp=new String();

		mud_objects = con.getmud_objects();

		while ( str.hasMoreTokens() ) {

			temp=temp+str.nextToken();
	
		}

		for (i=0; i<mud_objects.size(); i++) {

                        if ( temp.compareTo ( ((mudobject)mud_objects.elementAt(i)).getKeyword() ) == 0 ) { found=i;break; } 

                }

		if (found==-1) {

			// That object doesn't exist.
			p.sendLine ("That object does not exist.");

		} else {

			// Is it here?

			if ( ((mudobject)mud_objects.elementAt(found)).getLocation() != p.getLocation() ) {

				// No, it isn't.
				p.sendLine ("I don't see that here.");

			} else {

				// It's okay. Add to inventory
				((mudobject)mud_objects.elementAt(found)).setLocation ( -1 ) ;	
				p.addToInventory ( ((mudobject)mud_objects.elementAt(found)) );
				p.sendLine ("Taken."); 

			}

		} 

		p.sendPrompt();

	}

}
