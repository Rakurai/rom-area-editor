package com.ageoflegacy.aedit.model;

import com.ageoflegacy.aedit.model.MobileProgram;

import javax.json.JsonValue;
import javax.swing.*;
import java.util.Vector;
import com.ageoflegacy.aedit.ui.view.mobView.MudShopView;

public class Area {

	private boolean validArea;
	private boolean changed;
	private AreaHeader header;
	private Vector<Room> rooms;
	private Vector<MudObject> objects;
	private Vector<Mobile> mobs;
	private Vector<MudReset> resets;
	private Vector<MobileProgram> mobprogs;

	// Creates an model file with given filename reference. Initiates data.
	public Area(String file) {
		this();
		getHeader().setFileName(file);
	}

	// Creates blank model file, no filename. Initiates data.
	public Area() {
		header = new AreaHeader();
		rooms = new Vector<Room>();
		objects = new Vector<MudObject>();
		mobs = new Vector<Mobile>();
		resets = new Vector<MudReset>();
		mobprogs = new Vector<MobileProgram>();
		validArea = false;
		changed = false;
	}

	public AreaHeader getHeader() {
		return header;
	}

	public void setHeader(AreaHeader header) {
		this.header = header; // You may have broken the area.
		validArea = true;
		changed = true;
	}

	// Accessors
	public String getAreaName() {
		return getHeader().getAreaName();
	}

	public String getBuilder() {
		return getHeader().getBuilder();
	}

	public String getRangeString() {
		return getHeader().getRangeString();
	}

	public String getFileName() {
		return getHeader().getFileName();
	}

	public String getPathName() {
		return getHeader().getPathName();
	}

	public int getLowVnum() {
		return getHeader().getLowVnum();
	}

	public int getHighVnum() {
		return getHeader().getHighVnum();
	}

	public int getRoomCount() {
		return rooms.size();
	}

	public int getMobCount() {
		return mobs.size();
	}

	public int getMprogCount() {
		return mobprogs.size();
	}

	/**
	 * Add up: exits, mobs, mobs eq, mobs inventory, mobs inventory's inventory,
	 * objects in room and objects inventories
	 * 
	 * @return Count of resets that will be generated if you save.
	 */
	public int getResetCount() {
		int count = 0;

		for (Room room : rooms) {
			for (int lockLoop = 0; lockLoop < 6; lockLoop++) {
				MudExit tempExit = room.getExit(lockLoop);
				if (tempExit != null && tempExit.isSet(MudConstants.EXIT_CLOSED)) {
					count++;
				}
			}

			for (Mobile mob : room.getMobiles()) {
				if (mob == null)
					continue;

				count++;
				for (int loop = 0; loop < 21; loop++) {
					if (mob.getEquipment(loop) != -1) {
						count++;
					}
				}

				if (!mob.getInventory().isEmpty()) {
					for (MudObject obj : mob.getInventory()) {
						count += (1 + obj.getInventory().size());
					}
				}
			}

			for (MudObject obj : room.getObjects()) {
				count += (1 + obj.getInventory().size());
			}
		}
		return count;
	}

	public int getObjectCount() {
		return objects.size();
	}

	public int getFlags() {
		return getHeader().getFlags();
	}

	public boolean valid() {
		return validArea;
	}

	public boolean changed() {
		return changed;
	}

	public Vector<MudReset> getResets() {
		return resets;
	}

	public boolean isValidVnum(int vnum) {
		return !(vnum < getHeader().getLowVnum() || vnum > getHeader().getHighVnum());
	}

	/*
	 * First mprog in the model
	 */
	public int getFirstMprogVnum() {
		if (mobprogs.isEmpty())
			return -1;

		return mobprogs.iterator().next().getVnum();
	}

	/*
	 * First room in the model
	 */
	public int getFirstRoomVnum() {
		if (rooms.isEmpty())
			return -1;

		return rooms.iterator().next().getVnum();
	}

	/*
	 * First mobile in model.
	 */
	public int getFirstMobVnum() {
		if (mobs.isEmpty())
			return -1;

		return mobs.iterator().next().getVnum();
	}

	/*
	 * First object in model.
	 */
	public int getFirstObjectVnum() {
		if (objects.isEmpty())
			return -1;

		return objects.iterator().next().getVnum();
	}

	// call countMobile for every room and add.
	public int maxMob(int vnum) {
		int total = 0;

		if (vnum < getHeader().getLowVnum() || vnum > getHeader().getHighVnum())
			return 0;

		for (Room room : rooms) {
			total += room.countMobile(vnum);
		}

		return total;
	}

	/*
	 * Get room of specified vnum
	 */
	public Room getRoom(int vnum) {

		if (vnum < getHeader().getLowVnum() || vnum > getHeader().getHighVnum())
			return null;

		for (Room room : rooms) {
			if (room.getVnum() == vnum)
				return room;
		}

		return null;
	}

	/*
	 * Get object of specified vnum
	 */
	public MudObject getObject(int vnum) {

		if (vnum < getHeader().getLowVnum() || vnum > getHeader().getHighVnum())
			return null;

		for (MudObject object : objects) {
			if (object.getVnum() == vnum)
				return object;
		}

		return null;
	}

	/*
	 * Get mobile of specified vnum
	 */
	public Mobile getMobile(int vnum) {

		if (vnum < getHeader().getLowVnum() || vnum > getHeader().getHighVnum())
			return null;

		for (Mobile mob : mobs) {
			if (mob.getVnum() == vnum)
				return mob;
		}

		return null;
	}

	public MobileProgram getScriptByVnum(int vnum) {

		if (vnum < getHeader().getLowVnum() || vnum > getHeader().getHighVnum())
			return null;

		for (MobileProgram mprog : mobprogs) {
			if (mprog.getVnum() == vnum)
				return mprog;
		}

		return null;
	}

	public Vector<MobileProgram> getMobprogs() {
		return mobprogs;
	}

	public void setMobprogs(Vector<MobileProgram> mobprogs) {
		this.mobprogs = mobprogs;
	}

	public void removeMobileProgram(MobileProgram dMProg) {
		mobprogs.remove(dMProg);
	}

	/*
	 * Get an available mob program vnum
	 */
	public int getFreeScriptVnum() {
		if (mobprogs.isEmpty())
			return getLowVnum();
		else {
			int vnum;
			for (vnum = getHeader().getLowVnum(); vnum <= getHeader().getHighVnum(); vnum++) {
				if (getScriptByVnum(vnum) == null)
					return vnum;
			}
			return -1;
		}
	}

	/*
	 * Get an available room vnum
	 */
	public int getFreeRoomVnum() {
		if (rooms.isEmpty())
			return getHeader().getLowVnum();
		else {
			int vnum;
			for (vnum = getHeader().getLowVnum(); vnum <= getHeader().getHighVnum(); vnum++) {
				if (getRoom(vnum) == null)
					return vnum;
			}
			return -1;
		}
	}

	/*
	 * get an available mobile vnum
	 */
	public int getFreeMobileVnum() {
		if (mobs.isEmpty())
			return getLowVnum();
		else {
			int vnum;
			for (vnum = getHeader().getLowVnum(); vnum <= getHeader().getHighVnum(); vnum++) {
				if (getMobile(vnum) == null)
					return vnum;
			}
			return -1;
		}

	}

	/*
	 * get an available object vnum
	 */
	public int getFreeObjectVnum() {
		if (objects.isEmpty())
			return getLowVnum();
		else {
			int vnum;
			for (vnum = getHeader().getLowVnum(); vnum <= getHeader().getHighVnum(); vnum++) {
				if (getObject(vnum) == null)
					return vnum;
			}
			return -1;
		}

	}

	/*
	 * returns the object to mob to room ratio R:O:M
	 */
	public String getRatio() {
		int mob = getMobCount();
		int room = getRoomCount();
		int obj = getObjectCount();

		for (int a = 99; a > 0; a--) {
			if (mob % a == 0 && room % a == 0 && obj % a == 0) {
				mob = mob / a;
				room = room / a;
				obj = obj / a;
				break;
			}
		}

		return Integer.toString(obj) + ":" + Integer.toString(mob) + ":" + Integer.toString(room);
	}

	// Modifiers
	public void setBuilder(String newbuilder) {
		getHeader().setBuilder(newbuilder);
	}

	public void setAreaName(String newname) {
		getHeader().setAreaName(newname);
	}

	public void setRangeString(String str) {
		getHeader().setRangeString(str);
	}

	public void setFileName(String newname) {
		getHeader().setFileName(newname);
	}

	public void setPathName(String newpath) {
		getHeader().setPathName(newpath);
	}

	public void setVnumRange(int newLowVnum, int newHighVnum) {
		getHeader().setVnumRange(newLowVnum, newHighVnum);
		validArea = true;
	}

	public void setFlags(int newFlags) {
		getHeader().setFlags(newFlags);
	}

	/*
	 * Insert an object, it inserts the object into the proper array based on
	 * its type.
	 */
	public void insert(Object m) {
		if (m instanceof Room) {
			rooms.add((Room) m);
		} else if (m instanceof MudObject) {
			objects.add((MudObject) m);
		} else if (m instanceof Mobile) {
			mobs.add((Mobile) m);
		} else if (m instanceof MudReset) {
			resets.add((MudReset) m);
		} else if (m instanceof MudShopView) {
			MudShopView temp = (MudShopView) m;
			int kVnum = temp.getKeeper();
			Mobile mTemp = getMobile(kVnum);
			if (mTemp == null)
				return;

			mTemp.setShop(temp);
		} else if (m instanceof MobileProgram) {
			mobprogs.add((MobileProgram) m);
		} else {
			System.out.println("Unknown type inserted into model.");
		}
	}

	// revnum this model.
	public void reVnum(int newVnum) {
		int oldLow = getHeader().getLowVnum();
		int diff = newVnum - oldLow;

		int lowVnum = newVnum;
		int highVnum = getHeader().getHighVnum() + diff;

		// Start by revnuming the rooms.
		// * Rooms vnum
		// * Rooms exits
		for (Room temp : rooms) {
			temp.setVnum(temp.getVnum() + diff);
			MudExit exit;
			for (int a = 0; a < MudConstants.MAX_EXITS; a++) {
				exit = temp.getExit(a);
				if (exit == null)
					continue;

				exit.setToVnum(exit.getToVnum() + diff);
				if (exit.getKey() >= oldLow)
					exit.setKey(exit.getKey() + diff);
			}
		}

		// Revnum every mobile.
		// Mobiles Vnum
		// Mobiles Group
		// equipment slots
		// Triggers
		// Shop
		for (Mobile temp : mobs) {
			temp.setVnum(temp.getVnum() + diff);
			if (temp.getGroup() != 0)
				temp.setGroup(temp.getGroup() + diff);

			for (int w = 0; w < 21; w++) {
				if (temp.getEquipment(w) != -1)
					temp.equipMobile(w, temp.getEquipment(w) + diff);
			}
			for (MobTrigger trigger : temp.getTriggers()) {
				trigger.setVnum(trigger.getVnum() + diff);
			}
			if (temp.isShop())
				temp.getShop().setKeeper(temp.getShop().getKeeper() + diff);
			// mob programs attached to mobile need to be done here.
		}

		// revnum every object
		// objects vnum
		// portal vnum
		// container key
		for (MudObject temp : objects) {
			temp.setVnum(temp.getVnum() + diff);

			if (temp.getType() == MudConstants.ITEM_PORTAL)
				temp.setiValue(3, temp.getiValue(3) + diff);

			if (temp.getType() == MudConstants.ITEM_CONTAINER) {
				if (temp.getiValue(2) != 0)
					temp.setiValue(2, temp.getiValue(2) + diff);
			}
		}

		// revnum every mprog
		// mprog's vnum
		// vnums in text of mprog(how the hell...)
		for (MobileProgram temp : mobprogs) {
			temp.setVnum(temp.getVnum() + diff);
			// have to scan the text of the mobprogram, looking for vnums in
			// this
			// model range, and changing them to new vnum.
		}

		// change shops(done by mobile change)

		getHeader().setVnumRange(lowVnum, highVnum);

	}

	public void deleteMprog(int vnum) {

		for (MobileProgram mprog : mobprogs) {
			if (mprog.getVnum() == vnum) {
				mobprogs.remove(mprog);
			}
		}

		for (Mobile mob : mobs) {
			mob.deleteTriggers(vnum);
		}
	}

	public void deleteObject(int vnum) {

		for (MudObject oTemp : objects) {
			if (oTemp.getVnum() == vnum) {
				// clear from vector
				objects.remove(oTemp);

				for (Mobile mTemp : mobs) {
					mTemp.takeFromMobile(oTemp);
					mTemp.unEquip(vnum);
				}

				for (MudObject inside : objects) {
					if (inside.isContainer()) {
						inside.removeObject(oTemp);
						inside.setiValue(2, -1);
					}
				}

				for (Room loc : rooms) {
					loc.removeObject(oTemp);
				}

				return;
			}
		}
	}

	public void deleteMobile(int vnum) {
		for (Mobile mob : mobs) {
			if (mob.getVnum() == vnum) {
				mobs.remove(mob);
				for (Room room : rooms) {
					room.removeMobile(mob);
				}
				break;
			}
		}
	}

	public void deleteRoom(int vnum) {
		MudExit exit;
		Room target = null;
		// clear from rooms.
		for (Room room : rooms) {
			if (room.getVnum() == vnum) {
				target = room;
				rooms.remove(room);
				break;
			}
		}
		if (target != null) {
			for (Room room2 : rooms) {
				for (int b = 0; b < MudExit.MAX_EXITS; b++) {
					exit = room2.getExit(b);

					if (exit != null && exit.getToVnum() == vnum)
						room2.deleteExit(b);
				}
			}
		}
	}

	public Vector<Room> getRooms() {
		return rooms;
	}

	public Vector<MudObject> getObjects() {
		return objects;
	}

	public Vector<Mobile> getMobs() {
		return mobs;
	}

	public int getLowLevel() {
		int low = 9999;
		for (Mobile mob : mobs) {
			if (mob.getLevel() < low) {
				low = mob.getLevel();
			}
		}
		return low;
	}

	public int getHighLevel() {
		int high = 0;
		for (Mobile mob : mobs) {
			if (mob.getLevel() > high) {
				high = mob.getLevel();
			}
		}
		return high;
	}

	public void clear() {
		rooms.clear();
		objects.clear();
		mobs.clear();
		resets.clear();
		mobprogs.clear();
		header.reset();
	}

	public JList getResetList() {
		return new JList(resets.toArray(new MudReset[resets.size()]));
	}

	public JComboBox getVnumCombo(String type) {
		JComboBox temp;

		if (type.startsWith("room")) {
			temp = new JComboBox((Vector<Room>) rooms);
		} else if (type.startsWith("obj"))
			temp = new JComboBox((Vector<MudObject>) objects);
		else if (type.startsWith("mob"))
			temp = new JComboBox((Vector<Mobile>) mobs);
		else if (type.startsWith("reset"))
			temp = new JComboBox((Vector<MudReset>) resets);
		else if (type.startsWith("script"))
			temp = new JComboBox((Vector<MobileProgram>) mobprogs);
		else
			return null;

		temp.setPrototypeDisplayValue("01234567890123456789001234567890123456789");
		return temp;
	}

	/*
	 * This function takes the ROM style string resets and parses them into
	 * better usable data. This includes turning 'e' resets into equipment flags
	 * on that mobiles vnum. 11/28/2003, added code to filter bad vnums
	 */
	public void transformResets() {
		String command;
		Mobile mob = null;
		MudReset prevReset = null;

		try {
			for (MudReset reset : resets) {
				command = reset.getCommand().toUpperCase();
				switch (command.charAt(0)) {
				case 'M': {
					// Add a mobile to a room
					int mobVnum = reset.getArg(1);
					mob = getMobile(mobVnum);
					int roomVnum = reset.getArg(3);
					Room room = getRoom(roomVnum);

					if (mob == null || room == null) {
						break;
					}

					room.addMobile(mob);
					break;
				}
				case 'O': {
					// Add an object to a room
					int objVnum = reset.getArg(1);
					MudObject object = getObject(objVnum);
					int roomVnum = reset.getArg(3);
					Room room = getRoom(roomVnum);

					if (object == null || room == null) {
						break;
					}

					room.addObject(object);
					break;
				}
				case 'E': {
					// Equip the last mobile reset, which must exist.
					int objVnum = reset.getArg(1);
					MudObject myObj = getObject(objVnum);
					int pos = reset.getArg(3);

					if (mob == null || myObj == null) {
						break;
					}

					System.out.println("Equipping mobile: " + mob + " with object: " + myObj);
					mob.equipMobile(pos, objVnum);
					break;
				}
				case 'G': {
					// Give an object to the last mobile reset, which must exist
					int objVnum = reset.getArg(1);
					MudObject myObj = getObject(objVnum);

					if (mob == null || myObj == null) {
						break;
					}

					// Only give the object to the mobile if they don't already
					// have
					// one or the previous reset was the same thing (intentional
					// duplication)
					// otherwise old style rom resets will put multiples in our
					// new resets...
					if (mob.getInventory().contains(myObj) && prevReset.getCommand().toUpperCase().charAt(0) != 'G'
							&& prevReset.getArg(1) != objVnum) {
						break;
					}
					mob.giveToMobile(myObj);
					break;
				}
				case 'D': {
					Room rTemp = getRoom(reset.getArg(1));

					if (rTemp == null) {
						break;
					}
					int dir = reset.getArg(2);
					int lockType = reset.getArg(3);
					MudExit tempExit = rTemp.getExit(dir);
					if (tempExit == null) {
						break;
					}
					tempExit.setFlag(MudConstants.EXIT_ISDOOR);
					tempExit.setFlag(MudConstants.EXIT_CLOSED);
					if (lockType == 2)
						tempExit.setFlag(MudConstants.EXIT_LOCKED);

					Room rReverse;
					rReverse = getRoom(tempExit.getToVnum());
					if (rReverse == null)
						break;
					int dir2 = MudExit.getReverseExit(dir);
					MudExit tempExit2 = rReverse.getExit(dir2);
					if (tempExit2 == null)
						break;
					tempExit2.setFlag(MudConstants.EXIT_ISDOOR);
					tempExit2.setFlag(MudConstants.EXIT_CLOSED);
					if (lockType == 2)
						tempExit2.setFlag(MudConstants.EXIT_LOCKED);

					break;
				}
				case 'P': {
					int oTemp = reset.getArg(1);
					int oTemp2 = reset.getArg(3);
					MudObject holder = getObject(oTemp2);
					MudObject placer = getObject(oTemp);

					if (holder == null || placer == null)
						break;

					holder.addObject(placer);
					break;
				}
				default: {
					System.out.println("Nothing to do with reset:");
					System.out.println(reset.toString());
					break;
				}
				}
				prevReset = reset;
			}
		} catch (Exception ext) {
			System.out.println("Exception transforming resets.");
		}
		resets.clear();
	}

	public String getExitFromAreaCount() {
		int total = 0;
		for (Room room : rooms) {
			total += room.getExitFromAreaCount();
		}
		return Integer.toString(total);
	}

}
