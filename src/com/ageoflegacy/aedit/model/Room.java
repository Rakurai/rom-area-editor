// George Frick
// Room.java
// Area Editor Project, Spring 2002
package com.ageoflegacy.aedit.model;

import com.ageoflegacy.aedit.model.MudConstants;
import com.ageoflegacy.aedit.model.Size;

import java.util.Collection;
import java.util.Vector;

public class Room extends MudThing {
	private int flags;
	private int sector;
	private int heal;
	private int mana;
	protected int myX, myY; // Store where it is graphically :-)
	private MudExit[] exits;
	private MudExit[] exitsFromArea;
	public Collection<MudObject> items;
	public Collection<Mobile> mobiles;

	public Room(int newVnum, Area parentArea) {
		super(newVnum);
		myarea = parentArea;
		items = new Vector<MudObject>();
		mobiles = new Vector<Mobile>();
		exits = new MudExit[MudExit.MAX_EXITS];
		exitsFromArea = new MudExit[MudExit.MAX_EXITS];
		size = new Size("medium");
		name = "Room: " + name;
		for (int a = 0; a < MudExit.MAX_EXITS; a++) {
			exits[a] = null;
			exitsFromArea[a] = null;
		}
		heal = 100;
		mana = 100;
		myX = -1;
		myY = -1;
	}

	public void addObject(MudObject obj) {
		items.add(obj);
	}

	public void addMobile(Mobile mob) {
		mob.upMax();
		mobiles.add(mob);
	}

	public void removeObject(MudObject obj) {
		// while( items.contains(obj) )
		items.remove(obj);
	}

	public void removeMobile(Mobile mob) {

		// while( mobiles.contains(mob) )
		// {

		mobiles.remove(mob);
		mob.downMax();
		// }
	}

	public int countMobile(int vnum) {
		int count = 0;

		for (Mobile mob : mobiles) {
			if (mob.getVnum() == vnum) {
				count++;
			}
		}

		return count;
	}

	public void setXY(int x, int y) {
		myX = x;
		myY = y;
	}

	public int getX() {
		return myX;
	}

	public int getY() {
		return myY;
	}

	public Room getUp() {
		MudExit temp = getExit(MudConstants.DIR_UP);
		if (temp != null) {
			return myarea.getRoom(temp.getToVnum());
		} else
			return null;
	}

	public Room getDown() {
		MudExit temp = getExit(MudConstants.DIR_DOWN);
		if (temp != null) {
			return myarea.getRoom(temp.getToVnum());
		} else
			return null;
	}

	public Room getNorth() {
		MudExit temp = getExit(MudConstants.DIR_NORTH);
		if (temp != null) {
			return myarea.getRoom(temp.getToVnum());
		} else
			return null;
	}

	public Room getSouth() {
		MudExit temp = getExit(MudConstants.DIR_SOUTH);
		if (temp != null) {
			return myarea.getRoom(temp.getToVnum());
		} else
			return null;
	}

	public Room getWest() {
		MudExit temp = getExit(MudConstants.DIR_WEST);
		if (temp != null) {
			return myarea.getRoom(temp.getToVnum());
		} else
			return null;
	}

	public Room getEast() {
		MudExit temp = getExit(MudConstants.DIR_EAST);
		if (temp != null) {
			return myarea.getRoom(temp.getToVnum());
		} else
			return null;
	}

	public void setExit(MudExit e, int d) {
		if (exits == null)
			exits = new MudExit[MudExit.MAX_EXITS];

		try {
			exits[d] = e;
		} catch (Exception exc) {
			System.out.println("Couldn't set exit #: " + d);
		}
	}

	public void setExitFromArea(MudExit e, int d) {
		if (exitsFromArea == null)
			exitsFromArea = new MudExit[MudExit.MAX_EXITS];

		try {
			exitsFromArea[d] = e;
		} catch (Exception exc) {
			System.out.println("Couldn't set exit #: " + d);
		}
	}

	public void deleteExit(int d) {
		setExit(null, d);
	}

	public String toString() {
		return Integer.toString(getVnum()) + ": " + getName();
	}

	public MudExit getExit(int d) {
		try {
			if (exits == null || exits[d] == null)
				return null;
			else
				return exits[d];
		} catch (Exception e) {
			System.out.println("Problem here...");
			System.exit(0);
		}

		return null;
	}

	public MudExit getExitFromArea(int d) {
		try {
			if (exitsFromArea == null || exitsFromArea[d] == null)
				return null;
			else
				return exitsFromArea[d];
		} catch (Exception e) {
			System.out.println("Problem here...");
			System.exit(0);
		}

		return null;
	}

	public int getFlags() {
		return flags;
	}

	public void removeFlag(int f) {
		flags = flags ^ f;
	}

	public void addFlag(int f) {
		flags = flags | f;
	}

	public void setFlags(int f) {
		flags = f;
	}

	public int getSector() {
		return sector;
	}

	public int getHeal() {
		return heal;
	}

	public int getMana() {
		return mana;
	}

	public void setHeal(int h) {
		heal = h;
	}

	public void setMana(int m) {
		mana = m;
	}

	public void setSector(int sect) {
		sector = sect;
	}

	// room names can have color.
	public void setName(String newname) {
		name = newname;
	}

	public Collection<MudObject> getObjects() {
		return items;
	}

	public Collection<Mobile> getMobiles() {
		return mobiles;
	}

	public int getExitFromAreaCount() {
		if (exitsFromArea == null) {
			return 0;
		}
		int total = 0;
		for (int a = 0; a < MudExit.MAX_EXITS; a++) {
			if (exitsFromArea[a] != null) {
				total++;
			}
		}
		return total;
	}
}
