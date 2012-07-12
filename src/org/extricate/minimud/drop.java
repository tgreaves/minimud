package org.extricate.minimud;

import java.util.*;

/** Allows a player to drop a mud object in their room */

public class drop extends mudcommand {

	/** Allows a player to drop a mud object in their room */

	public void call(Controller con, Player p, StringTokenizer str) {

		Vector<mudobject> mud_objects;
		Vector<?> objs;
		int i;
		int found=-1;
		String temp=new String();

		mud_objects = con.getmud_objects();
		objs        = p.getObjects();

		while ( str.hasMoreTokens() ) {

			temp=temp+str.nextToken();
	
		}

		if ( objs.size() == 0) {

			p.sendLine ("You are not carrying any items.");

		} else {

			for (i=0; i<objs.size(); i++) {

                        	if ( temp.compareTo ( ((mudobject)objs.elementAt(i)).getKeyword() ) == 0 ) { found=i;break; } 

                	}

			if (found==-1) {

				// Not in the inventory
				p.sendLine ("You are not carrying that.");

			} else {

				// That can be dropped.
				// First locate equivalent in master MUD objects list.

				int here = mud_objects.indexOf ( (mudobject)objs.elementAt(found) );   // It's here	
				mud_objects.removeElementAt (here) ;                                   // Die! Die!
				
				mudobject tempobj = (mudobject)objs.elementAt(found);          // Temp. storage
				tempobj.setLocation (p.getLocation()) ;                        // Drop it.
				mud_objects.addElement (tempobj);			       // Back into master file.
				objs.removeElementAt (found);				       // Delete from inventory

				p.sendLine ("Dropped.");
	
			}

		} 

		p.sendPrompt();

	}

}
