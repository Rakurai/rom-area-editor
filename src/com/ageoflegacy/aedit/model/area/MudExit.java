// George Frick
// MudExit.java
// Area Editor Project, Spring 2002
package com.ageoflegacy.aedit.model.area;

import com.ageoflegacy.aedit.model.MudConstants;
import com.ageoflegacy.aedit.model.Room;

public class MudExit {
	private Room myRoom;
	private int toVnum;
	private int keyVnum;
	private String description;
	private String keyword;
	private int exitFlags;

	public final static int DIR_NORTH = 0;
	public final static int DIR_EAST = 1;
	public final static int DIR_SOUTH = 2;
	public final static int DIR_WEST = 3;
	public final static int DIR_UP = 4;
	public final static int DIR_DOWN = 5;

	public final static int MAX_EXITS = 6;

	public final static int EXIT_ISDOOR = 1; // 0000 0000 0001
	public final static int EXIT_CLOSED = 2; // 0000 0000 0010
	public final static int EXIT_LOCKED = 4; // 0000 0000 0100
	public final static int EXIT_PICKPROOF = 32; // 0000 0010 0000
	public final static int EXIT_NOPASS = 64; // 0000 0100 0000
	public final static int EXIT_EASY = 128; // 0000 1000 0000
	public final static int EXIT_HARD = 256; // 0001 0000 0000
	public final static int EXIT_INFURIATING = 512; // 0010 0000 0000
	public final static int EXIT_NOCLOSE = 1024; // 0100 0000 0000
	public final static int EXIT_NOLOCK = 2048; // 1000 0000 0000
	public final static int EXIT_WALL = 8192; // 0010 0000 0000 0000
	public final static int EXIT_NOWALL = 16384; // 0100 0000 0000 0000

	public MudExit(int v, Room m) {
		toVnum = v;
		myRoom = m;
		keyword = "Door";
		description = "A plain door.";
		keyVnum = 0;
	}

	public static int getReverseExit(int d) {
		switch (d) {
		case DIR_NORTH:
			return DIR_SOUTH;
		case DIR_SOUTH:
			return DIR_NORTH;
		case DIR_DOWN:
			return DIR_UP;
		case DIR_UP:
			return DIR_DOWN;
		case DIR_EAST:
			return DIR_WEST;
		case DIR_WEST:
			return DIR_EAST;
		default:
			return -1;
		}
	}

	public void setFlags(int f) {
		exitFlags = f;

		/*
		 * An exit cannot have both opposite flags, we remove both if they get
		 * set
		 */
		if (isSet(MudConstants.EXIT_NOWALL) && isSet(MudConstants.EXIT_WALL))
			exitFlags = exitFlags ^ EXIT_NOWALL ^ EXIT_WALL;

		if (isSet(MudConstants.EXIT_NOCLOSE) && isSet(MudConstants.EXIT_CLOSED))
			exitFlags = exitFlags ^ EXIT_NOCLOSE ^ EXIT_CLOSED;

		if (isSet(MudConstants.EXIT_NOLOCK) && isSet(MudConstants.EXIT_LOCKED))
			exitFlags = exitFlags ^ EXIT_NOLOCK ^ EXIT_LOCKED;

		/*
		 * An exit cannot be locked without isdoor and isclosed An exit cannot
		 * be closed without isdoor
		 */
		if (isSet(MudConstants.EXIT_LOCKED))
			exitFlags = exitFlags | EXIT_ISDOOR | EXIT_LOCKED | EXIT_CLOSED;
		else if (isSet(MudConstants.EXIT_CLOSED))
			exitFlags = exitFlags | EXIT_ISDOOR | EXIT_CLOSED;
	}

	public void setFlagsByKey(int key) {
		switch (key) {
		case 0:
		default: {
			exitFlags = 0;
			return;
		}
		case 1: {
			exitFlags = EXIT_ISDOOR;
			return;
		}
		case 2: {
			exitFlags = EXIT_ISDOOR | EXIT_PICKPROOF;
			return;
		}
		case 3: {
			exitFlags = EXIT_ISDOOR | EXIT_NOPASS;
			return;
		}
		case 4: {
			exitFlags = EXIT_ISDOOR | EXIT_PICKPROOF | EXIT_NOPASS;
			return;
		}
		}
	}

	public void setFlag(int f) {
		exitFlags = exitFlags | f;
	}

	public void removeFlag(int f) {
		exitFlags = exitFlags ^ f;
	}

	public int getFlagToken() {
		if (isSet(EXIT_ISDOOR) && !isSet(EXIT_PICKPROOF) && !isSet(EXIT_NOPASS))
			return 1;

		if (isSet(EXIT_ISDOOR) && isSet(EXIT_PICKPROOF) && !isSet(EXIT_NOPASS))
			return 2;

		if (isSet(EXIT_ISDOOR) && !isSet(EXIT_PICKPROOF) && isSet(EXIT_NOPASS))
			return 3;

		if (isSet(EXIT_ISDOOR) && isSet(EXIT_PICKPROOF) && isSet(EXIT_NOPASS))
			return 4;

		return 0;
	}

	public int getFlags() {
		return exitFlags;
	}

	public int getKey() {
		return keyVnum;
	}

	public String getKeyword() {
		return keyword;
	}

	public int getToVnum() {
		return toVnum;
	}

	public void setToVnum(int newVnum) {
		toVnum = newVnum;
	}

	public String getDescription() {
		return description;
	}

	public void setKeyword(String k) {
		keyword = k;
	}

	public void setDescription(String d) {
		description = d;
	}

	public void setKey(int v) {
		keyVnum = v;
	}

	public boolean isSet(int flag) {
		return (exitFlags & flag) == flag;
	}

	/*
	 * to determine if we have a key selected for a locked exit.
	 */
	public boolean validLock() {
		return !(isSet(MudConstants.EXIT_LOCKED) && getKey() == 0)
				&& !(getKey() != 0 && !isSet(MudConstants.EXIT_LOCKED));
	}
}
