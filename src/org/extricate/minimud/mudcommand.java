package org.extricate.minimud;

import java.util.*;

/** The basic class for all commands on MiniMUD. Note that such commands 
 *  are loaded dynamically as required, so old commands may be altered and
 *  new commands created, <B>while the server is still running</B>.
 */

public class mudcommand {

	/** The constructor does not need to do anything. */

	public mudcommand () { }

	/** The basic call for a MUD command. A mudcommand should always have
         *  a subclass detailing what it does. Otherwise it will simply return
         *  a message saying that it hasn't been implemented.
	 *
	 *  Please note that this will always be overridden by an actual command
	 *  definition!
	 *
	 *  @param con Controller
	 *  @param p Player
	 *  @param str Command arguments
	 */ 

	public void call(Controller con, Player p, StringTokenizer str) { 

		p.sendLineNoCR("That command exists, but has not yet been implemented.");	

	}


}

