package org.extricate.minimud;

/** The basic structure behind an item that is stored in the minimud queue
 *  for the controller. All items will be an extension of this, so the
 *  basic class doesn't really do anything.
 */

class queueItem {

	public String wibble;

	// Methods

	/** The constructor does nothing */

	public queueItem () { }

	/** Just takes a little string for testing
	 *
	 *  @param x Test string
	 */

	public queueItem (String x) {

		this.wibble = x;

	}

}

/** A queue item which is used when a new player joins the MUD. */

class queue_user extends queueItem {

	public Connection con;

	// Methods

	/** Blank constructor. Has no usage. */

	public queue_user() { }

	/** Standard constructor. 
	 *
	 *  @param x Connection associated with user
 	 */

	public queue_user (Connection x) {

		this.con = x;

	}

	/** Returns the Connection instance related to the user. */

	public Connection giveConnection() {

		return this.con;

	}

}

/** A queue item which is used when a player's connection is lost */

class queue_lost extends queueItem {

        public Player p;

        // Methods

	/** Blank constructor. Has no usage. */

        public queue_lost() { }

	/** Standard constructor.
	 *
	 *  @param x Player associated with connection.
	 */

        public queue_lost (Player x) {

                this.p = x;

        }

	/** Returns the Player instance related to this queue item */

        public Player givePlayer() {

                return this.p;

        }

}
 
/** A queue item which is used when the user sends a line of text */
 
class queue_line extends queueItem {

	public Player p;
	public String s;

	// Methods

	/** Blank constructor. Has no usage. */

	public queue_line() { }

	/** Standard constructor.
	 *
	 *  @param p2 The Player sending the line.
	 *  @param s2 The text of the line.
	 */

	public queue_line (Player p2, String s2) {

		this.p = p2;
		this.s = s2;

	}
	
	/** Returns the Player associated with the item */

	public Player givePlayer() {

		return this.p;

	}

	/** Returns the String associated with the item */

	public String giveString() {

		return this.s;

	}

}
 
