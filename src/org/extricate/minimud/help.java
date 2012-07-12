package org.extricate.minimud;

import java.util.*;

/** Sends the player the "help" text (playing.txt) */

public class help extends mudcommand {

	/** Sends the player the "help" text (playing.txt) */

	public void call(Controller con, Player p, StringTokenizer str) {

		p.sendFile ("playing.txt");
		p.sendPrompt();

	}

}
