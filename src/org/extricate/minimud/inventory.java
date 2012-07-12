package org.extricate.minimud;

import java.util.*;

/** Gives a player a listing of their current inventory */

public class inventory extends mudcommand {

	/** Gives a player a listing of their current inventory */

	public void call(Controller con, Player p, StringTokenizer str) {

		int i;
		Vector<?> objs = p.getObjects();
		
		if ( objs.size() == 0 ) {

			p.sendLine ("You are not carrying anything.");

		} else {

			// Print out each item name (keyword)

			p.sendLine ("You are carrying the following items:");

			for (i=0; i<objs.size(); i++) {

				p.sendLine ( ((mudobject)objs.elementAt(i)).getKeyword() );

			}

		}

		p.sendPrompt();

	}

}
