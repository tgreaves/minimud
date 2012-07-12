package minimud;

import java.io.*;
import java.util.*;

/** "Shouts" a message to all the players on the MUD. */

public class shout extends mudcommand {

	/** Effectively sends a message to everyone that is on the MUD. */

	public void call(Controller con, Player p, StringTokenizer str) {

		String temp=new String();

		while ( str.hasMoreTokens() ) {

			temp=temp+str.nextToken()+" ";
	
		}

		con.sendtoAll_noCR (p, p.getName()  + " shouts loudly, ' " + temp+ "'");		

	}

}
