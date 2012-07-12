package org.extricate.minimud;

import java.util.*;

/** The basic class for all the objects on the MiniMUD. These are set-up
 *  when loaded when the MUD initialises.
 */

public class mudobject {

	/** Keyword which the user uses to refer to the object. */
	protected String keyword;

	/** Description displayed to the user when in the room */
	protected String room_description;

	/** Where the object is located */
	protected int location;

	/** Standard constructor to create a mud object */

	public mudobject ( String k, String r_d, int l ) {

		keyword = k;
		room_description = r_d;
		location = l;

	}

	/** Return the object's keyword */

	public String getKeyword () {

		return keyword;

	}

	/** Return the object's room description */

	public String getRoom_desc () {

		return room_description;

	}

	/** Return the object's location number */

	public int getLocation () {

		return location;

	}

	/** Set the location number of the object */

	public void setLocation (int l) {

		location = l;

	}
		
}

