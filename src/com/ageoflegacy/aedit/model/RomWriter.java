// George Frick
// RomWriter.java

package com.ageoflegacy.aedit.model;

import com.ageoflegacy.aedit.beans.Armor;
import com.ageoflegacy.aedit.ui.view.mobView.MudShopView;

import java.io.File;
import java.util.Collection;
import java.text.NumberFormat;
import java.text.DecimalFormat;

public class RomWriter extends RomIO {
	Area theArea;

	public RomWriter(String filename) {
		inputFile = new File(filename);
		if (!openArea(false))
			return;
		open = true;
	}

	public RomWriter(File file) {
		inputFile = file;
		if (!openArea(false))
			return;
		open = true;
	}

	public boolean isOpen() {
		return open;
	}

	public void finish() {
		try {
			outbuf.flush();
			outbuf.close();
		} catch (Exception e) {
			System.out.println("Couldn't finish file writing.");
		}
	}

	public void writeArea(Area data) {
		theArea = data;
		try {
			writeHeader();

			if (theArea.getMobCount() > 0)
				writeMobiles();

			if (theArea.getObjectCount() > 0)
				writeObjects();

			if (theArea.getRoomCount() > 0)
				writeRooms();

			writeSpecials();

			if (theArea.getMobCount() > 0 && theArea.getObjectCount() > 0)
				writeResets();

			writeShops();

			if (theArea.getMprogCount() > 0)
				writeMobprogs();

			outbuf.write("#$\n\n", 0, 4);
			finish();
			System.out.println("Done writing");
		} catch (Exception e) {
			System.out.println("problem writing.");
		}
	}

	public void romWrite(String s) {
		try {
			outbuf.write(s, 0, s.length());
		} catch (Exception e) {
			System.out.println("Error writing to model file!!!");
		}
	}

	void writeHeader() {
		String low = Integer.toString(theArea.getLowVnum());
		String high = Integer.toString(theArea.getHighVnum());
		String flags = Integer.toString(theArea.getFlags());

		NumberFormat nf = new DecimalFormat("000");

		romWrite("#AREADATA\n");
		romWrite("Name " + theArea.getAreaName() + "~\n");
		romWrite("Builders " + theArea.getBuilder() + "~\n");
		romWrite("VNUMs " + low + " " + high + "\n");
		romWrite("Range " + theArea.getRangeString() + "~\n");
/*		romWrite("Credits [" + nf.format(theArea.getLowLevel()) + "  " + nf.format(theArea.getHighLevel()) + "] "
				+ theArea.getBuilder() + " " + theArea.getAreaName() + "~\n");
*/		if (theArea.getFlags() != 0) {
			romWrite("Flags " + flags + "\n");
		}
		romWrite("End\n\n\n\n");
	}

	int DIF(int a, int b) {
		return (~((~a) | (b))); // (~((~a)|(b)))
	}

	void writeMobiles() {
		Mobile mob;
		romWrite("#MOBILES\n");
		for (int a = theArea.getLowVnum(); a <= theArea.getHighVnum(); a++) {
			mob = theArea.getMobile(a);

			if (mob == null)
				continue;

			romWrite("#" + Integer.toString(a) + "\n");
			romWrite(mob.getName() + "~\n");
			romWrite(mob.getShortDesc() + "~\n");
			if (mob.getLongDesc().trim().endsWith("\n"))
				romWrite(mob.getLongDesc().trim() + "~\n");
			else
				romWrite(mob.getLongDesc().trim() + "\n~\n");

			if (mob.getDescription().trim().length() > 0) {
				romWrite(mob.getDescription().trim());
				if (!mob.getDescription().trim().endsWith("\n")) {
					romWrite("\n");
				}
			}
			romWrite("~\n");

			romWrite(mob.getRace().toString().toLowerCase() + "~\n");
			romWrite(MudConstants.getBitString(mob.getActFlags()) + " " + MudConstants.getBitString(mob.getAffectedBy())
					+ " " + mob.getAlignment() + " " + mob.getGroup() + "\n");
			romWrite(mob.getLevel() + " " + mob.getHitRoll() + " " + mob.getHitDice() + " " + mob.getManaDice() + " "
					+ mob.getDamageDice() + " " + mob.getDamageType() + "\n");
			romWrite(mob.getAC(Armor.PIERCE) + " " + mob.getAC(Armor.BASH) + " " + mob.getAC(Armor.SLASH) + " "
					+ mob.getAC(Armor.MAGIC) + "\n");
			romWrite(MudConstants.getBitString(mob.getOffensiveFlags()) + " "
					+ MudConstants.getBitString(mob.getImmunityFlags()) + " "
					+ MudConstants.getBitString(mob.getResistanceFlags()) + " "
					+ MudConstants.getBitString(mob.getVulnerabilityFlags()) + "\n");
			romWrite(MudConstants.getPositionString(mob.getStartPosition()) + " "
					+ MudConstants.getPositionString(mob.getDefaultPosition()) + " "
					+ MudConstants.getSexString(mob.getSex()) + " " + mob.getWealth() + "\n");
			romWrite(MudConstants.getBitString(mob.getForm()) + " " + MudConstants.getBitString(mob.getParts()) + " "
					+ mob.getSize().print() + " " + mob.getMaterial() + "\n");

			/*
			 * Write extras
			 */
			if (mob.getDeathCry() != null && !mob.getDeathCry().equals("default") && !(mob.getDeathCry().length() == 0))
				romWrite("D " + mob.getDeathCry() + "~\n");

			for (MobTrigger trigger : mob.getTriggers()) {
				romWrite("M " + trigger.toFile() + "~\n");
			}

			Race myRace = mob.getRace();
			int temp;

			temp = DIF(myRace.getActFlags(), mob.getActFlags());
			if (temp > 0)
				romWrite("F act " + MudConstants.getBitString(temp) + "\n");

			temp = DIF(myRace.getAffectedByFlags(), mob.getAffectedBy());
			if (temp > 0)
				romWrite("F aff " + MudConstants.getBitString(temp) + "\n");

			temp = DIF(myRace.getOffensiveFlags(), mob.getOffensiveFlags());
			if (temp > 0)
				romWrite("F off " + MudConstants.getBitString(temp) + "\n");

			temp = DIF(myRace.getImmunityFlags(), mob.getImmunityFlags());
			if (temp > 0)
				romWrite("F imm " + MudConstants.getBitString(temp) + "\n");

			temp = DIF(myRace.getResistanceFlags(), mob.getResistanceFlags());
			if (temp > 0)
				romWrite("F res " + MudConstants.getBitString(temp) + "\n");

			temp = DIF(myRace.getVulnerableFlags(), mob.getVulnerabilityFlags());
			if (temp > 0)
				romWrite("F vul " + MudConstants.getBitString(temp) + "\n");

			temp = DIF(myRace.getForm(), mob.getForm());
			if (temp > 0)
				romWrite("F for " + MudConstants.getBitString(temp) + "\n");

			temp = DIF(myRace.getParts(), mob.getParts());
			if (temp > 0)
				romWrite("F par " + MudConstants.getBitString(temp) + "\n");

			// if(act,aff,off,imm,res,vuln,form,parts diff from race) write
			// them.
			// if (mprogs) mprogs
		}
		romWrite("#0\n\n\n\n");
		System.out.println("wrote mobiles");
	}

	void writeObjects() {
		MudObject object;
		romWrite("#OBJECTS\n");
		System.out.println("Writing objects");
		for (int a = theArea.getLowVnum(); a <= theArea.getHighVnum(); a++) {
			object = theArea.getObject(a);

			System.out.print(".");

			if (object == null)
				continue;

			// write an object
			romWrite("#" + Integer.toString(a) + "\n");
			romWrite(object.getName() + "~\n");
			romWrite(object.getShortDesc() + "~\n");
			romWrite(object.getLongDesc() + "~\n");
			romWrite(object.getMaterial() + "~\n");
			String typeTemp = MudConstants.stringFromType(object.getType());
			if (typeTemp.indexOf(' ') != -1)
				typeTemp = typeTemp.substring(0, typeTemp.indexOf(' '));

			romWrite(typeTemp.toLowerCase() + " ");
			romWrite(MudConstants.getBitString(object.getExtraFlags()) + " ");
			romWrite(MudConstants.getBitString(object.getWearFlags()) + "\n");

			// write flags based on type
			switch (object.getType()) {
			case MudConstants.ITEM_STAFF:
			case MudConstants.ITEM_WAND: {
				romWrite(object.getiValue(0) + " " + object.getiValue(1) + " " + object.getiValue(2) + " " + "'"
						+ object.getsValue(3) + "' 0\n");
				break;
			}
			case MudConstants.ITEM_PILL:
			case MudConstants.ITEM_POTION:
			case MudConstants.ITEM_SCROLL: {
				romWrite(object.getiValue(0) + " '" + object.getsValue(1) + "' '" + object.getsValue(2) + "' '"
						+ object.getsValue(3) + "' '" + object.getsValue(4) + "'\n");
				break;
			}
			case MudConstants.ITEM_ARMOR: {
				romWrite(object.getiValue(0) + " " + object.getiValue(1) + " " + object.getiValue(2) + " "
						+ object.getiValue(3) + " " + object.getiValue(4) + "\n");
				break;
			}
			case MudConstants.ITEM_WEAPON: {
				romWrite(object.getsValue(0) + " " + object.getiValue(1) + " " + object.getiValue(2) + " "
						+ object.getsValue(3) + " " + MudConstants.getBitString(object.getiValue(4)) + "\n");
				break;
			}
			case MudConstants.ITEM_FURNITURE: {
				romWrite(object.getiValue(0) + " " + object.getiValue(1) + " "
						+ MudConstants.getBitString(object.getiValue(2)) + " " + object.getiValue(3) + " "
						+ object.getiValue(4) + "\n");
				break;
			}
			case MudConstants.ITEM_PORTAL: {
				romWrite(object.getiValue(0) + " " + MudConstants.getBitString(object.getiValue(1)) + " "
						+ MudConstants.getBitString(object.getiValue(2)) + " " + object.getiValue(3) + " 0\n");
				break;
			}
			case MudConstants.ITEM_FOOD: {
				romWrite(object.getiValue(0) + " " + object.getiValue(1) + " 0 "
						+ MudConstants.getBitString(object.getiValue(3)) + " 0\n");
				break;
			}
			case MudConstants.ITEM_CONTAINER: {
				romWrite(object.getiValue(0) + " " + MudConstants.getBitString(object.getiValue(1)) + " "
						+ object.getiValue(2) + " " + object.getiValue(3) + " " + object.getiValue(4) + "\n");
				break;
			}
			case MudConstants.ITEM_FOUNTAIN:
			case MudConstants.ITEM_DRINK_CON: {
				romWrite(object.getiValue(0) + " " + object.getiValue(1) + " '" + object.getsValue(2) + "' "
						+ object.getiValue(3) + " 0\n");
				break;
			}
			case MudConstants.ITEM_LIGHT: {
				romWrite("0 0 " + object.getiValue(2) + " 0 0\n");
				break;
			}
			case MudConstants.ITEM_MONEY: {
				romWrite(object.getiValue(0) + " " + object.getiValue(1) + " 0 0 0\n");
				break;
			}
			default: {
				romWrite(MudConstants.getBitString(object.getiValue(0)) + " "
						+ MudConstants.getBitString(object.getiValue(1)) + " "
						+ MudConstants.getBitString(object.getiValue(2)) + " "
						+ MudConstants.getBitString(object.getiValue(3)) + " "
						+ MudConstants.getBitString(object.getiValue(4)) + "\n");
				break;
			}
			}

			romWrite(Integer.toString(object.getLevel()) + " ");
			romWrite(Integer.toString(object.getWeight()) + " ");
			romWrite(Integer.toString(object.getCost()) + " ");
			romWrite(MudConstants.conditionString(object.getCondition()) + "\n");

			/*
			 * write extras
			 */
			for (ObjectAffect oTemp : object.getAffects()) {
				romWrite(oTemp.fileString());
			}

			if (object.getWearMessage() != null)
				romWrite("R\n" + object.getWearMessage() + "~\n");
			if (object.getRemoveMessage() != null)
				romWrite("M\n" + object.getRemoveMessage() + "~\n");

			for (String s : object.getExtraDescriptions().keySet()) {
				romWrite("E\n" + s + "~\n" + object.getExtraDescriptions().get(s) + "~\n");
			}
		}

		romWrite("#0\n\n\n\n");
		System.out.println("wrote objects");
	}

	void writeRooms() {
		Room room;
		MudExit exit;
		romWrite("#ROOMS\n");
		/*
		 * write room data
		 */
		for (int a = theArea.getLowVnum(); a <= theArea.getHighVnum(); a++) {
			room = theArea.getRoom(a);

			if (room == null)
				continue;

			romWrite("#" + Integer.toString(a) + "\n");
			romWrite(room.getName() + "~\n");
			romWrite(room.getDescription() + "~\n");
			romWrite("0 " + Integer.toString(room.getFlags()) + " " + Integer.toString(room.getSector()) + "\n");

			/*
			 * write extra descs
			 */
			for (String s : room.getExtraDescriptions().keySet()) {
				romWrite("E\n" + s + "~\n" + room.getExtraDescriptions().get(s) + "~\n");
			}

			for (int x = 0; x < MudConstants.MAX_EXITS; x++) {
				exit = room.getExit(x);
				if (exit == null) {
					exit = room.getExitFromArea(x);
					if (exit == null) {
						continue;
					}
				}

				romWrite("D" + Integer.toString(x) + "\n");
				romWrite(exit.getDescription() + "~\n");
				romWrite(exit.getKeyword() + "~\n");
				romWrite(Integer.toString(exit.getFlagToken()) + " " + Integer.toString(exit.getKey()) + " "
						+ Integer.toString(exit.getToVnum()) + "\n");
			}
			if (room.getMana() != 100 || room.getHeal() != 100)
				romWrite("M " + Integer.toString(room.getMana()) + " H " + Integer.toString(room.getHeal()) + "\n");
			// NOT IN EDITOR C <clan> + ~
			// NOT IN EDITOR O <owner> + ~
			romWrite("S\n"); // S
		}

		romWrite("#0\n\n\n\n");
	}

	void writeSpecials() {
		boolean found = false;
		Mobile mob;

		for (int dLoop = theArea.getLowVnum(); dLoop <= theArea.getHighVnum(); dLoop++) {
			mob = theArea.getMobile(dLoop);

			if (mob == null)
				continue;

			if (mob.getSpecial() == null || mob.getSpecial().equals(""))
				continue;

			if (!found) {
				found = true;
				romWrite("#SPECIALS\n");
			}

			romWrite("M " + mob.getVnum() + " " + mob.getSpecial() + "\n");
		}

		if (found) {
			romWrite("S\n\n\n\n");
			System.out.println("wrote specials [*PARTIALLY* SUPPORTED]");
		} else
			System.out.println("No Specials to write. Skipping.");

	}

	void writeResets() {
		Room room;

		romWrite("#RESETS\n");

		/*
		 * Write out the door resets to file. These resets set the lock and
		 * closed flags on doors
		 */
		for (int dLoop = theArea.getLowVnum(); dLoop <= theArea.getHighVnum(); dLoop++) {
			room = theArea.getRoom(dLoop);

			if (room == null)
				continue;

			for (int lockLoop = 0; lockLoop < 6; lockLoop++) {
				MudExit tempExit = room.getExit(lockLoop);
				if (tempExit == null)
					continue;
				int locktype;
				if (tempExit.isSet(MudConstants.EXIT_CLOSED)) {
					locktype = 1;
					if (tempExit.isSet(MudConstants.EXIT_LOCKED))
						locktype = 2;

					romWrite("D 0 " + room.getVnum() + " " + lockLoop + " " + locktype + "\n");
				}
			}
		}

		/*
		 * Loop through each room, writing out each mobile. for each mobile, we
		 * right out that mobiles inventory/eq right away.
		 */
		for (int a = theArea.getLowVnum(); a <= theArea.getHighVnum(); a++) {
			room = theArea.getRoom(a);

			if (room == null)
				continue;

			if (!room.getMobiles().isEmpty()) {
				writeRoomMobiles(room);
			}
			if (!room.getObjects().isEmpty()) {
				writeRoomObjects(room.getObjects(), room.getVnum());
			}
		}

		romWrite("S\n\n\n\n");
		System.out.println("Wrote resets");
	}

	/*
	 * given a room, write out its mobiles and their eq/inventory
	 */
	void writeRoomMobiles(Room room) {
		int eq;

		for (Mobile mob : room.getMobiles()) {
			if (mob == null)
				continue;

			romWrite("M 0 " + Integer.toString(mob.getVnum()) + " " + Integer.toString(mob.getMax()) + " "
					+ Integer.toString(room.getVnum()) + " " + room.countMobile(mob.getVnum()) + "\n");

			for (int loop = 0; loop < 21; loop++) {
				eq = mob.getEquipment(loop);

				if (eq == -1)
					continue;

				romWrite("E 100 " + Integer.toString(eq) + " 0 " + Integer.toString(loop) + "\n");
			}

			if (!mob.getInventory().isEmpty()) {
				writeMobileInventory(mob.getInventory());
			}
		}
	}

	/*
	 * Given a room, write out any objects that go to the room if an object
	 * contains objects, immediately write those out too.
	 */
	void writeRoomObjects(Collection<MudObject> mudObjects, int rVnum) {
		for (MudObject obj : mudObjects) {
			romWrite("O 100 " + Integer.toString(obj.getVnum()) + " 0 " + Integer.toString(rVnum) + "\n");

			/*
			 * put objects in objects
			 */
			for (MudObject tObject : obj.getInventory()) {
				romWrite("P 100 " + Integer.toString(tObject.getVnum()) + " 1 " + Integer.toString(obj.getVnum())
						+ " 1\n");
			}
		}
	}

	void writeMobileInventory(Collection<MudObject> v) {
		for (MudObject obj : v) {
			romWrite("G 100 " + Integer.toString(obj.getVnum()) + " 0\n");

			/*
			 * add the resets for objects put in containers held by mobs.
			 */
			for (MudObject tObject : obj.getInventory()) {
				romWrite("P 100 " + Integer.toString(tObject.getVnum()) + " 1 " + Integer.toString(obj.getVnum())
						+ " 1\n");
			}
		}
	}

	void writeShops() {
		MudShopView sView;
		Mobile mob;
		boolean found = false;

		for (int a = theArea.getLowVnum(); a <= theArea.getHighVnum(); a++) {
			mob = theArea.getMobile(a);

			if (mob == null || !mob.isShop())
				continue;

			if (!found) {
				found = true;
				romWrite("#SHOPS\n");
			}
			sView = mob.getShop();
			romWrite(sView.toString());
		}

		if (found)
			romWrite("0\n\n\n\n");

		System.out.println("wrote shops");
	}

	void writeMobprogs() {
		romWrite("#MOBPROGS\n");

		for (MobileProgram mprog : theArea.getMobprogs()) {
			romWrite(mprog.toFile());
		}
		romWrite("#0\n\n");
		System.out.println("wrote mobprogs");
	}
}
