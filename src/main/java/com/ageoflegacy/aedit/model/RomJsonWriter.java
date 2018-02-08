// George Frick
// Having some fun with the old mud code and json...

package com.ageoflegacy.aedit.model;

import java.io.File;
import java.io.StringWriter;
import java.util.Collection;

import javax.json.*;

import com.ageoflegacy.aedit.beans.Armor;
import com.ageoflegacy.aedit.model.area.Area;
import com.ageoflegacy.aedit.model.area.MobTrigger;
import com.ageoflegacy.aedit.model.area.Mobile;
import com.ageoflegacy.aedit.model.area.MobileProgram;
import com.ageoflegacy.aedit.model.area.MudExit;
import com.ageoflegacy.aedit.model.area.MudObject;
import com.ageoflegacy.aedit.model.area.ObjectAffect;
import com.ageoflegacy.aedit.ui.view.mobView.MudShopView;

public class RomJsonWriter extends RomIO {
	Area theArea;

	public RomJsonWriter(String filename) {
		inputFile = new File(filename);
		if (!openArea(false))
			return;
		open = true;
	}

	public RomJsonWriter(File file) {
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
			JsonObjectBuilder builder = Json.createObjectBuilder();
			builder.add("header", createHeader());
			builder.add("mobiles", createMobiles());
			builder.add("objects", createObjects());
			builder.add("rooms", createRooms());
			builder.add("specials", createSpecials());
			builder.add("resets", createResets());
			builder.add("shops", createShops());
			JsonObject model = builder.build();
			StringWriter stWriter = new StringWriter();
			JsonWriter jsonWriter = Json.createWriter(stWriter);
			jsonWriter.writeObject(model);
			jsonWriter.close();

			String jsonData = stWriter.toString();
			outbuf.write(jsonData);
			finish();
			System.out.println("Done writing");
		} catch (Exception e) {
			System.out.println("problem writing.");
			e.printStackTrace();
		}
	}

	JsonObjectBuilder createHeader() {
		String low = Integer.toString(theArea.getLowVnum());
		String high = Integer.toString(theArea.getHighVnum());
		String flags = Integer.toString(theArea.getFlags());
//		NumberFormat nf = new DecimalFormat("000");

		JsonObjectBuilder builder = Json.createObjectBuilder()
				.add("name", theArea.getAreaName())
				.add("builders", theArea.getBuilder())
				.add("vnums", low + " " + high)
				.add("range", theArea.getRangeString());
/*				.add("credits",
						"[" + nf.format(theArea.getLowLevel()) + "  " + nf.format(theArea.getHighLevel()) + "] "
								+ theArea.getBuilder() + " " + theArea.getAreaName());
*/
		if (theArea.getFlags() != 0) {
			builder.add("flags", flags);
		}
		return builder;
	}

	int DIF(int a, int b) {
		return (~((~a) | (b))); // (~((~a)|(b)))
	}

	JsonArrayBuilder createMobiles() {
		JsonArrayBuilder builder = Json.createArrayBuilder();
		Mobile mob;
		for (int a = theArea.getLowVnum(); a <= theArea.getHighVnum(); a++) {
			mob = theArea.getMobile(a);
			if (mob != null) {
				builder.add(createSingleMobile(mob, a));
			}
		}
		return builder;
	}

	private JsonObjectBuilder createSingleMobile(Mobile mob, int vnum) {
		JsonObjectBuilder builder = Json.createObjectBuilder();
		try {
			builder.add("vnum", vnum);
			builder.add("name", mob.getName());
			builder.add("shortDesc", mob.getShortDesc());
			builder.add("longDesc", mob.getLongDesc().trim());
			builder.add("description", mob.getDescription().trim());
			builder.add("race", mob.getRace().toString().toLowerCase());
			builder.add("actFlags", MudConstants.getBitString(mob.getActFlags()));
			builder.add("affectedBy", MudConstants.getBitString(mob.getAffectedBy()));
			builder.add("alignment", mob.getAlignment());
			builder.add("group", mob.getGroup());
			builder.add("level", mob.getLevel());
			builder.add("hitRoll", mob.getHitRoll());
			builder.add("hitDice", mob.getHitDice().toString());
			builder.add("manaDice", mob.getManaDice().toString());
			builder.add("damageDice", mob.getDamageDice().toString());
			builder.add("damageType", mob.getDamageType());
			builder.add("armorPierce", mob.getAC(Armor.PIERCE));
			builder.add("armorBash", mob.getAC(Armor.BASH));
			builder.add("armorSlash", mob.getAC(Armor.SLASH));
			builder.add("armorMagic", mob.getAC(Armor.MAGIC));
			builder.add("offensiveFlags", MudConstants.getBitString(mob.getOffensiveFlags()));
			builder.add("immunityFlags", MudConstants.getBitString(mob.getImmunityFlags()));
			builder.add("resistanceFlags", MudConstants.getBitString(mob.getResistanceFlags()));
			builder.add("vulnerabilityFlags", MudConstants.getBitString(mob.getVulnerabilityFlags()));
			builder.add("startPosition", MudConstants.getPositionString(mob.getStartPosition()));
			builder.add("defaultPosition", MudConstants.getPositionString(mob.getDefaultPosition()));
			builder.add("sex", MudConstants.getSexString(mob.getSex()));
			builder.add("wealth", mob.getWealth());
			builder.add("form", MudConstants.getBitString(mob.getForm()));
			builder.add("parts", MudConstants.getBitString(mob.getParts()));
			builder.add("size", mob.getSize().print());
			builder.add("material", mob.getMaterial());
			if (mob.getDeathCry() != null) {
				builder.add("deathCry", mob.getDeathCry());
			}

			if (!mob.getTriggers().isEmpty()) {
				JsonArrayBuilder triggers = Json.createArrayBuilder();
				for (MobTrigger trigger : mob.getTriggers()) {
					triggers.add(Json.createObjectBuilder().add("trigger", trigger.toFile()));
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return builder;
	}

	JsonArrayBuilder createObjects() {
		JsonArrayBuilder builder = Json.createArrayBuilder();
		MudObject object;
		for (int a = theArea.getLowVnum(); a <= theArea.getHighVnum(); a++) {
			object = theArea.getObject(a);
			if (object != null) {
				builder.add(createSingleObject(object, a));
			}
		}
		return builder;
	}

	JsonObjectBuilder createSingleObject(MudObject object, int vnum) {
		JsonObjectBuilder builder = Json.createObjectBuilder();

		builder.add("vnum", vnum);
		builder.add("name", object.getName());
		builder.add("shortDesc", object.getShortDesc());
		builder.add("longDesc", object.getLongDesc().trim());
		builder.add("material", object.getMaterial());
		String typeTemp = MudConstants.stringFromType(object.getType());
		if (typeTemp.indexOf(' ') != -1)
			typeTemp = typeTemp.substring(0, typeTemp.indexOf(' '));

		builder.add("type", typeTemp.toLowerCase());
		builder.add("extraFlags", MudConstants.getBitString(object.getExtraFlags()));
		builder.add("wearFlag", MudConstants.getBitString(object.getWearFlags()));

		// write flags based on type
		switch (object.getType()) {
		case MudConstants.ITEM_STAFF:
		case MudConstants.ITEM_WAND: {
			builder.add("v0", object.getiValue(0));
			builder.add("v1", object.getiValue(1));
			builder.add("v2", object.getiValue(2));
			builder.add("v3", object.getsValue(3));
			break;
		}
		case MudConstants.ITEM_PILL:
		case MudConstants.ITEM_POTION:
		case MudConstants.ITEM_SCROLL: {
			builder.add("v0", object.getiValue(0));
			builder.add("v1", object.getsValue(1));
			builder.add("v2", object.getsValue(2));
			builder.add("v3", object.getsValue(3));
			builder.add("v4", object.getsValue(4));
			break;
		}
		case MudConstants.ITEM_ARMOR: {
			builder.add("v0", object.getiValue(0));
			builder.add("v1", object.getiValue(1));
			builder.add("v2", object.getiValue(2));
			builder.add("v3", object.getiValue(3));
			builder.add("v4", object.getiValue(4));
			break;
		}
		case MudConstants.ITEM_WEAPON: {
			builder.add("v0", object.getsValue(0));
			builder.add("v1", object.getiValue(1));
			builder.add("v2", object.getiValue(2));
			builder.add("v3", object.getsValue(3));
			builder.add("v4", MudConstants.getBitString(object.getiValue(4)));
			break;
		}
		case MudConstants.ITEM_FURNITURE: {
			builder.add("v0", object.getiValue(0));
			builder.add("v1", object.getiValue(1));
			builder.add("v2", MudConstants.getBitString(object.getiValue(2)));
			builder.add("v3", object.getiValue(3));
			builder.add("v4", object.getiValue(4));
			break;
		}
		case MudConstants.ITEM_PORTAL: {
			builder.add("v0", object.getiValue(0));
			builder.add("v1", MudConstants.getBitString(object.getiValue(1)));
			builder.add("v2", MudConstants.getBitString(object.getiValue(2)));
			builder.add("v3", object.getiValue(3));
			break;
		}
		case MudConstants.ITEM_FOOD: {
			builder.add("v0", object.getiValue(0));
			builder.add("v1", object.getiValue(1));
			builder.add("v3", MudConstants.getBitString(object.getiValue(3)));
			break;
		}
		case MudConstants.ITEM_CONTAINER: {
			builder.add("v0", object.getiValue(0));
			builder.add("v1", MudConstants.getBitString(object.getiValue(1)));
			builder.add("v2", object.getiValue(2));
			builder.add("v3", object.getiValue(3));
			builder.add("v4", object.getiValue(4));
			break;
		}
		case MudConstants.ITEM_FOUNTAIN:
		case MudConstants.ITEM_DRINK_CON: {
			builder.add("v0", object.getiValue(0));
			builder.add("v1", object.getiValue(1));
			builder.add("v2", object.getsValue(2));
			builder.add("v3", object.getiValue(3));
			break;
		}
		case MudConstants.ITEM_LIGHT: {
			builder.add("v2", object.getiValue(2));
			break;
		}
		case MudConstants.ITEM_MONEY: {
			builder.add("v0", object.getiValue(0));
			builder.add("v1", object.getiValue(1));
			break;
		}
		default: {
			builder.add("v0", MudConstants.getBitString(object.getiValue(0)));
			builder.add("v1", MudConstants.getBitString(object.getiValue(1)));
			builder.add("v2", MudConstants.getBitString(object.getiValue(2)));
			builder.add("v3", MudConstants.getBitString(object.getiValue(3)));
			builder.add("v4", MudConstants.getBitString(object.getiValue(4)));
			break;
		}
		}
		builder.add("level", object.getLevel());
		builder.add("weight", object.getWeight());
		builder.add("cost", object.getCost());
		builder.add("condition", MudConstants.conditionString(object.getCondition()));

		JsonArrayBuilder affectBuilder = Json.createArrayBuilder();
		for (ObjectAffect oTemp : object.getAffects()) {
			affectBuilder.add(oTemp.fileString());
		}
		builder.add("affects", affectBuilder);

		if (object.getWearMessage() != null) {
			builder.add("wearMessage", object.getWearMessage());
		}

		if (object.getRemoveMessage() != null) {
			builder.add("removeMessage", object.getRemoveMessage());
		}

		JsonArrayBuilder extraDescBuilder = Json.createArrayBuilder();
		for (String s : object.getExtraDescriptions().keySet()) {
			JsonObjectBuilder extraItem = Json.createObjectBuilder();
			extraItem.add("name", s);
			extraItem.add("description", object.getExtraDescriptions().get(s));
		}
		builder.add("extraDescriptions", extraDescBuilder);
		return builder;
	}

	JsonArrayBuilder createRooms() {
		JsonArrayBuilder builder = Json.createArrayBuilder();
		Room room;
		for (int a = theArea.getLowVnum(); a <= theArea.getHighVnum(); a++) {
			room = theArea.getRoom(a);
			if (room != null) {
				builder.add(createSingleRoom(room, a));
			}
		}
		return builder;
	}

	private JsonObjectBuilder createSingleRoom(Room room, int vnum) {
		JsonObjectBuilder builder = Json.createObjectBuilder();
		MudExit exit;
		builder.add("vnum", vnum);
		builder.add("name", room.getName());
		builder.add("description", room.getDescription());
		builder.add("teleport", Integer.toString(room.getTeleport()));
		builder.add("flags", Integer.toString(room.getFlags()));
		builder.add("sector", Integer.toString(room.getSector()));

		JsonArrayBuilder extraArrayBuilder = Json.createArrayBuilder();
		for (String s : room.getExtraDescriptions().keySet()) {
			JsonObjectBuilder extraBuilder = Json.createObjectBuilder();
			extraBuilder.add("name", s);
			extraBuilder.add("description", room.getExtraDescriptions().get(s));
			extraArrayBuilder.add(extraBuilder);
		}
		builder.add("extraDescriptions", extraArrayBuilder);

		JsonArrayBuilder exitArrayBuilder = Json.createArrayBuilder();
		for (int x = 0; x < MudConstants.MAX_EXITS; x++) {
			JsonObjectBuilder exitBuilder = Json.createObjectBuilder();
			exit = room.getExit(x);
			if (exit == null) {
				exit = room.getExitFromArea(x);
				if (exit == null) {
					continue;
				}
			}

			exitBuilder.add("direction", Integer.toString(x));
			exitBuilder.add("description", exit.getDescription());
			exitBuilder.add("keyword", exit.getKeyword());
			exitBuilder.add("key", exit.getKey());
			exitBuilder.add("destination", Integer.toString(exit.getToVnum()));
			exitBuilder.add("flags", exit.getFlagToken());
			exitArrayBuilder.add(exitBuilder);
		}
		builder.add("exits", exitArrayBuilder);

		builder.add("manaRate", room.getMana());
		builder.add("healRate", room.getHeal());
		return builder;
	}

	JsonArrayBuilder createSpecials() {
		JsonArrayBuilder builder = Json.createArrayBuilder();
		Mobile mob;
		for (int dLoop = theArea.getLowVnum(); dLoop <= theArea.getHighVnum(); dLoop++) {
			mob = theArea.getMobile(dLoop);

			if (mob != null && mob.getSpecial() != null && mob.getSpecial().length() > 0) {
				builder.add(createSingleSpecial(mob, dLoop));
			}
		}
		return builder;
	}

	JsonObjectBuilder createSingleSpecial(Mobile mob, int dLoop) {
		JsonObjectBuilder builder = Json.createObjectBuilder();
		builder.add("vnum", mob.getVnum());
		builder.add("special", mob.getSpecial());
		return builder;
	}

	JsonArrayBuilder createResets() {
		JsonArrayBuilder builder = Json.createArrayBuilder();
		Room room;

		/*
		 * Loop through each room, writing out each mobile. for each mobile, we
		 * right out that mobiles inventory/eq right away.
		 */
		for (int a = theArea.getLowVnum(); a <= theArea.getHighVnum(); a++) {
			room = theArea.getRoom(a);

			if (room != null) {
				JsonObjectBuilder roomResets = Json.createObjectBuilder();
				roomResets.add("roomVnum", room.getVnum());

				JsonArrayBuilder doors = Json.createArrayBuilder();
				JsonObjectBuilder door;
				for (int lockLoop = 0; lockLoop < 6; lockLoop++) {
					MudExit tempExit = room.getExit(lockLoop);
					if (tempExit != null) {
						door = Json.createObjectBuilder();
						int lockType = 0;
						if (tempExit.isSet(MudConstants.EXIT_CLOSED)) {
							lockType = 1;
							if (tempExit.isSet(MudConstants.EXIT_LOCKED))
								lockType = 2;

							door.add("vnum", room.getVnum());
							door.add("direction", lockLoop);
							door.add("lockType", lockType);
							doors.add(door);
						}
					}
				}
				roomResets.add("doors", doors);

				if (!room.getMobiles().isEmpty()) {
					roomResets.add("mobiles", writeRoomMobiles(room));
				}
				if (!room.getObjects().isEmpty()) {
					roomResets.add("objects", writeRoomObjects(room.getObjects(), room.getVnum()));
				}
				builder.add(roomResets);
			}
		}
		return builder;
	}

	/*
	 * given a room, write out its mobiles and their eq/inventory
	 */
	JsonArrayBuilder writeRoomMobiles(Room room) {
		JsonArrayBuilder builder = Json.createArrayBuilder();
		int eq;

		for (Mobile mob : room.getMobiles()) {
			JsonObjectBuilder mobileReset;
			if (mob != null) {
				mobileReset = Json.createObjectBuilder();
				mobileReset.add("chance", 100);
				mobileReset.add("max", mob.getMax());
				mobileReset.add("min", room.countMobile(mob.getVnum()));
				mobileReset.add("roomVnum", room.getVnum());
				mobileReset.add("mobVnum", mob.getVnum());

				JsonObjectBuilder equip = Json.createObjectBuilder();
				JsonObjectBuilder singleItem;
				for (int loop = 0; loop < 21; loop++) {
					eq = mob.getEquipment(loop);

					if (eq != -1) {
						singleItem = Json.createObjectBuilder();
						singleItem.add("chance", 100);
						singleItem.add("position", loop);
						singleItem.add("vnum", eq);
						equip.add(MudConstants.getPositionString(loop), equip);
					}
				}
				mobileReset.add("equipment", equip);

				if (!mob.getInventory().isEmpty()) {
					mobileReset.add("inventory", writeMobileInventory(mob.getInventory()));
				}
				builder.add(mobileReset);
			}
		}
		return builder;
	}

	JsonArrayBuilder writeRoomObjects(Collection<MudObject> mudObjects, int rVnum) {
		JsonArrayBuilder builder = Json.createArrayBuilder();
		for (MudObject obj : mudObjects) {
			JsonObjectBuilder givenObject = Json.createObjectBuilder();
			givenObject.add("chance", 100);
			givenObject.add("vnum", obj.getVnum());
			givenObject.add("roomVnum", rVnum); // room its in.
			if (obj.getInventory() != null && obj.getInventory().size() > 0) {
				JsonArrayBuilder containerItems = Json.createArrayBuilder();
				for (MudObject tObject : obj.getInventory()) {
					JsonObjectBuilder innerObject = Json.createObjectBuilder();
					innerObject.add("chance", 100);
					innerObject.add("vnum", tObject.getVnum());
				}
				givenObject.add("contains", containerItems);
			}
		}
		return builder;
	}

	JsonArrayBuilder writeMobileInventory(Collection<MudObject> v) {
		JsonArrayBuilder builder = Json.createArrayBuilder();
		for (MudObject obj : v) {
			JsonObjectBuilder givenObject = Json.createObjectBuilder();
			givenObject.add("chance", 100);
			givenObject.add("vnum", obj.getVnum());
			if (obj.getInventory() != null && obj.getInventory().size() > 0) {
				JsonArrayBuilder containerItems = Json.createArrayBuilder();
				for (MudObject tObject : obj.getInventory()) {
					JsonObjectBuilder innerObject = Json.createObjectBuilder();
					innerObject.add("chance", 100);
					innerObject.add("vnum", tObject.getVnum());
				}
				givenObject.add("contains", containerItems);
			}
		}
		return builder;
	}

	JsonArrayBuilder createShops() {
		JsonArrayBuilder builder = Json.createArrayBuilder();
		MudShopView sView;
		Mobile mob;
		for (int a = theArea.getLowVnum(); a <= theArea.getHighVnum(); a++) {
			mob = theArea.getMobile(a);
			JsonObjectBuilder shop;
			if (mob != null && mob.isShop()) {
				shop = Json.createObjectBuilder();
				sView = mob.getShop();
				shop.add("keeperVnum", sView.getKeeper());
				shop.add("openHour", sView.getOpenHour());
				shop.add("closeHour", sView.getCloseHour());
				shop.add("buyProfit", sView.getBuyProfit());
				shop.add("sellProfit", sView.getSellProfit());
				JsonArrayBuilder itemTypes = Json.createArrayBuilder();
				for (int type : sView.getBuyType()) {
					itemTypes.add(type);
				}
				shop.add("buyTypes", itemTypes);
			}
		}
		return builder;
	}
}
