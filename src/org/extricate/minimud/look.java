package org.extricate.minimud;

import java.io.*;
import java.util.*;

/** Gives the player a description of their current location */

public class look extends mudcommand {

	/** Sends the player a description of their current location. */

	public void call(Controller con, Player p, StringTokenizer str) {

		p.sendLocation (p.getLocation(), con ) ;
		p.sendPrompt();

	}

}
