package org.extricate.minimud;

import java.io.*;
import java.util.*;

/** Class to handle a player wishing to express emotion */

public class emote extends mudcommand {

	/** Sends the emotion to everyone in the room, including the player
	 *  issuing it. As an example: <I>emote smiles</I> would be sent
	 *  out as: <I>Mercenary smiles</I>
	 */

	public void call(Controller con, Player p, StringTokenizer str) {

		String temp = new String();

		while ( str.hasMoreTokens() ) {

			temp=temp+str.nextToken()+" ";

		}

		con.sendtoAll_inroom (p, "\n" + p.getName() + " " + temp);   // Does the emote.
		p.sendLine ( p.getName() + " " + temp);                      // To local player.

		p.sendPrompt();
	}

}
