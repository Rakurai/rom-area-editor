// George Frick
// RomLoader.java
// Area Editor Project, Spring 2002
//

package com.ageoflegacy.aedit.model;

import com.ageoflegacy.aedit.beans.Armor;
import com.ageoflegacy.aedit.beans.Dice;
import com.ageoflegacy.aedit.io.FileParser;
import com.ageoflegacy.aedit.model.area.Area;
import com.ageoflegacy.aedit.model.area.Mobile;
import com.ageoflegacy.aedit.model.area.MudExit;
import com.ageoflegacy.aedit.model.area.MudObject;
import com.ageoflegacy.aedit.model.area.MudReset;
import com.ageoflegacy.aedit.model.area.ObjectAffect;
import com.ageoflegacy.aedit.model.table.RaceDataTable;
import com.ageoflegacy.aedit.ui.view.mobView.MudShopView;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

// class to load an model.
public class RomLoader {

	int lowVnum;
	int highVnum;
	FileParser parser;
	File inputFile;

	public RomLoader(File file) {
		inputFile = file;
	}

	protected void readError(String cause) {
		System.out.println(inputFile.getName() + ", line " + parser.getLineNumber() + ": " + cause);
	}
	
	public void readArea(Model model) throws FileNotFoundException, IOException {
		System.out.println("Loading Area.");

		InputStream fstream = new FileInputStream(inputFile);
		parser = new FileParser(fstream);

		while (true) {
			String temp = parser.readHeader().toLowerCase();

			     if (temp.equals("area"))         readHeader(model);
			else if (temp.startsWith("mobi"))     readMobiles(model);
//			else if (temp.startsWith("mobp"))     readMobProgs(model);
			else if (temp.startsWith("room"))     readRooms(model);
			else if (temp.startsWith("obj"))      readObjects(model);
			else if (temp.startsWith("shop"))     readShops(model);
			else if (temp.startsWith("reset"))    readResets(model);
			else if (temp.startsWith("special"))  readSpecial(model);
			else if (temp.startsWith("$")) {
				System.out.println("Read Of Area File Complete.");
				break;
			}
			else
				readError("unknown header " + temp + " in file");
		}

		fstream.close();
	}

	int REMOVE_BIT(int a, int b) {
		return ((~b) & (a)); // remove bits b from a.
	}

	public void readMobiles(Model model) throws IOException {
		System.out.print("Reading Mobiles.");
		int vnum = parser.readVnum();

		try {
			while (vnum > 0) {
				Mobile mob = new Mobile(vnum, model);

				mob.setName(parser.readString());
				mob.setShortName(parser.readString());
				mob.setLongName(parser.readString());
				mob.setDescription(parser.readString());
				mob.setRace(model.getRaceDataTable().getRace(parser.readString()));

				mob.setActFlags(parser.readFlags() | mob.getRace().getActFlags());
				mob.setAffectedBy(parser.readFlags() | mob.getRace().getAffectedByFlags());
				mob.setAlignment(parser.readInt());

				mob.setGroup(0); parser.readWord();
//				mob.setGroup(Integer.parseInt(hold1));

				mob.setLevel(parser.readInt());
				mob.setHitRoll(parser.readInt());
				mob.setHitDice(new Dice(parser.readWord()));
				mob.setManaDice(new Dice(parser.readWord()));
				mob.setDamageDice(new Dice(parser.readWord()));
				mob.setDamageType(parser.readWord());

				mob.setAC(Armor.PIERCE, parser.readInt());
				mob.setAC(Armor.BASH, parser.readInt());
				mob.setAC(Armor.SLASH, parser.readInt());
				mob.setAC(Armor.MAGIC, parser.readInt());

				mob.setOffensiveFlags(parser.readFlags() | mob.getRace().getOffensiveFlags());
				mob.setImmunityFlags(parser.readFlags() | mob.getRace().getImmunityFlags());
				mob.setResistanceFlags(parser.readFlags() | mob.getRace().getResistanceFlags());
				mob.setVulnerabilityFlags(parser.readFlags() | mob.getRace().getVulnerableFlags());

				mob.setStartPosition(MudConstants.lookupPos(parser.readWord()));
				mob.setDefaultPosition(MudConstants.lookupPos(parser.readWord()));
				mob.setSex(MudConstants.lookupSex(parser.readWord()));
				mob.setWealth(Integer.parseInt(parser.readWord()));

				mob.setForm(parser.readFlags() | mob.getRace().getForm());
				mob.setParts(parser.readFlags() | mob.getRace().getParts());
				mob.setSize(new Size(parser.readWord()));
				mob.setMaterial(parser.readWord());

				// Insert new mobile into model and read next vnum.
				model.getArea().insert(mob);
				vnum = parser.readVnum();
			}
		} catch (Exception e) {
			readError("error in readMobiles: " + e.getMessage());
			e.printStackTrace();
			return;
		}

		System.out.println("...Complete");
	}

	public void readObjects(Model model) throws IOException {
		System.out.print("Reading Objects.");
		int vnum = parser.readVnum();

		try {
			while (vnum != 0) {
				MudObject obj = new MudObject(vnum, model.getArea());

				obj.setName(parser.readString()); // 2. Name
				obj.setShortName(parser.readString()); // 3. Short
				obj.setLongName(parser.readString()); // 4. Description
				obj.setMaterial(parser.readString()); // 5. Material

				obj.setType(MudConstants.typeFromString(parser.readWord()));
				obj.setExtraFlags(parser.readFlags());
				obj.setWearFlags(parser.readFlags());

				// 7. Values. These are parsed based on object type(ACK!)
				// rather than nested switches, do it this way
				int type = obj.getType();
				int iValue = 0;
				
				switch (type) {
				case MudConstants.ITEM_WEAPON:
					obj.setsValue(iValue, parser.readWord()); break;
				case MudConstants.ITEM_KEY:
					obj.setiValue(iValue, parser.readFlags()); break;
				default:
					obj.setiValue(iValue, parser.readInt()); break;
				}

				iValue = 1;
				
				switch (type) {
				case MudConstants.ITEM_POTION:
				case MudConstants.ITEM_PILL:
				case MudConstants.ITEM_SCROLL:
					obj.setsValue(iValue, parser.readWord()); break;
				case MudConstants.ITEM_CONTAINER:
				case MudConstants.ITEM_CORPSE_NPC:
				case MudConstants.ITEM_CORPSE_PC:
				case MudConstants.ITEM_PORTAL:
					obj.setiValue(iValue, parser.readFlags()); break;
				default:
					obj.setiValue(iValue, parser.readInt()); break;
				}

				iValue = 2;

				switch (type) {
				case MudConstants.ITEM_POTION:
				case MudConstants.ITEM_PILL:
				case MudConstants.ITEM_SCROLL:
				case MudConstants.ITEM_DRINK_CON:
				case MudConstants.ITEM_FOUNTAIN:
					obj.setsValue(iValue, parser.readWord()); break;
				case MudConstants.ITEM_FURNITURE:
				case MudConstants.ITEM_PORTAL:
				case MudConstants.ITEM_ANVIL:
					obj.setiValue(iValue, parser.readFlags()); break;
				default:
					obj.setiValue(iValue, parser.readInt()); break;
				}

				iValue = 3;

				switch (type) {
				case MudConstants.ITEM_WEAPON:
				case MudConstants.ITEM_WAND:
				case MudConstants.ITEM_STAFF:
				case MudConstants.ITEM_POTION:
				case MudConstants.ITEM_PILL:
				case MudConstants.ITEM_SCROLL:
					obj.setsValue(iValue, parser.readWord()); break;
				case MudConstants.ITEM_DRINK_CON:
				case MudConstants.ITEM_FOUNTAIN:
				case MudConstants.ITEM_FOOD:
					obj.setiValue(iValue, parser.readFlags()); break;
				default:
					obj.setiValue(iValue, parser.readInt()); break;
				}

				iValue = 4;

				switch (type) {
				case MudConstants.ITEM_POTION:
				case MudConstants.ITEM_PILL:
				case MudConstants.ITEM_SCROLL:
					obj.setsValue(iValue, parser.readWord()); break;
				case MudConstants.ITEM_WEAPON:
					obj.setiValue(iValue, parser.readFlags()); break;
				default:
					obj.setiValue(iValue, parser.readInt()); break;
				}

				obj.setLevel(parser.readInt());
				obj.setWeight(parser.readInt());
				obj.setCost(parser.readInt());
				obj.setCondition(MudConstants.conditionLookup(parser.readWord()));

				boolean stop = false;
				
				while (!stop) {
					parser.skipWhitespace();
					char letter = parser.readChar();
					
					switch (letter) {
					case 'A': { // apply
						obj.addAffect(new ObjectAffect(
							parser.readInt(),
							parser.readInt()
						));
						break;
					}
					case 'F': { // affects on player?
						readError("Unsupported F affect on object.");
						parser.readWord();
						parser.readInt();
						parser.readInt();
						parser.readFlags();
						break;
					}
					case 'E': { // extra desc
						obj.getExtraDescriptions().put(
							parser.readString(),
							parser.readString()
						);
						break;
					}
					case 'S': { // gem settings
						obj.setNumSettings(parser.readInt());
					}
					
					default:
						parser.unreadChar(letter);
						stop = true;
					}
				}

				// Insert new object into model and read next vnum.
				model.getArea().insert(obj);
				vnum = parser.readVnum();
			}
		} catch (Exception e) {
			readError("error in readObjects: " + e.getMessage());
			e.printStackTrace();
			return;
		}
		System.out.println("...Complete.");
	}

	public void readRooms(Model model) throws IOException {
		System.out.print("Reading Rooms.");

		int vnum = parser.readVnum();

		try {
			while (vnum != 0) {
				Room temp = new Room(vnum, model.getArea());
				
				temp.setName(parser.readString());
				temp.setDescription(parser.readString());
				
				temp.setTeleport(parser.readInt());
				temp.setFlags(parser.readFlags());
				temp.setSector(parser.readInt());
				boolean stop = false;

				while (!stop) {
					parser.skipWhitespace();
					switch (parser.readChar()) {
					case 'C':
						temp.setClanName(parser.readString().trim());
						break;
					case 'H':
						temp.setHeal(parser.readInt());
						break;
					case 'M':
						temp.setMana(parser.readInt());
						break;
					case 'O':
						temp.setOwner(parser.readString().trim());
						break;
					case 'E': // extra desc
						temp.getExtraDescriptions().put(
							parser.readString(),
							parser.readString()
						);
						break;
					case 'D': {
						int dir = parser.readInt();
						String exitDesc = parser.readString();
						String exitName = parser.readString();
						int locks = parser.readInt();
						int key = parser.readInt();
						int toVnum = parser.readInt();

						MudExit exit = new MudExit(toVnum, temp);
						exit.setKey(key);
						exit.setFlagsByKey(locks);
						exit.setKeyword(exitName);
						exit.setDescription(exitDesc);

						// add exit to room
						if (toVnum < lowVnum || toVnum > highVnum)
							temp.setExitFromArea(exit, dir);
						else
							temp.setExit(exit, dir);
						break;
					}
					case 'S':
						stop = true;
						break;
					default:
						readError("Unknown symbol in room.\n");
						stop = true;
						break;
					}
				}
				
				model.getArea().insert(temp);
				vnum = parser.readVnum();
			}
		} catch (Exception e) {
			readError("error in readRooms: " + e.getMessage());
			e.printStackTrace();
			return;
		}

		System.out.println(".....Complete.");
	}

	public void readSpecial(Model model) {
		System.out.print("Reading Specials.");
		try {
			while (true) {
				parser.skipWhitespace();
				char letter = parser.readChar();
				
				if (letter == 'S')
					break;

				if (letter == '*') { // handles leftover trailing comments too
					parser.readToEOL();
					continue;
				}
				
				if (letter == 'M') {
					int mVnum = parser.readInt();
					Mobile mob = model.getArea().getMobile(mVnum);
	
					if (mob == null) {
						readError("Special discarded, no mobile with vnum " + mVnum);
						continue;
					}
	
					mob.setSpecial(parser.readWord());
					continue;
				}
				
				readError("unknown letter " + letter + " in specials line");
				parser.readToEOL();
			}
		} catch (Exception e) {
			readError("error in readSpecials: " + e.getMessage());
			e.printStackTrace();
			return;
		}
		
		System.out.println("..Complete. [NOT SUPPORTED BY EDITOR]");
		System.out.println("Mafia will Store/Save your specials, but not allow processing.");
	}

	public void readResets(Model model) {
		System.out.print("Reading Resets.");

		try {
			while (true) {
				parser.skipWhitespace();
				char letter = parser.readChar();
				
				if (letter == 'S')
					break;

				if (letter == '*') {
					parser.readToEOL();
					continue;
				}

				parser.readInt(); // leading 0
				int args[] = new int[4];
				int place = 0;

				args[place++] = parser.readInt();
				args[place++] = parser.readInt();
				
				switch (letter) {
				case 'M':
				case 'P':
					args[place++] = parser.readInt();
					// drop through
				case 'E':
				case 'O':
				case 'D':
					args[place++] = parser.readInt();
					// drop through
				case 'G':
				case 'R':
					break;
				default:
					readError("unknown letter " + letter + " in resets line");
					parser.readToEOL();
					continue;
				}
				
				model.getArea().insert(new MudReset(letter, args[0], args[1], args[2], args[3]));
			}
		} catch (Exception e) {
			readError("error in readResets: " + e.getMessage());
			e.printStackTrace();
			return;
		}
		System.out.println("....Complete. (Read " + model.getArea().getResets().size() + " resets.)");
	}

	public void readShops(Model model) {
		System.out.print("Reading Shops.");
		
		try {
			while (true) {
				parser.skipWhitespace();
				char letter = parser.readChar();
				
				if (letter == '0')
					break;

				if (letter == '*') {
					parser.readToEOL();
					continue;
				}

				parser.unreadChar(letter);
				int vnum = parser.readInt();

				Mobile mob = model.getArea().getMobile(vnum);
				
				if (mob == null) {
					readError("Mobile " + vnum + " does not exist in SHOPS section, skipping");
					parser.readToEOL();
				}

				mob.setShop(new MudShopView(
					vnum,
					new int[] {
						parser.readInt(), // buy type 1
						parser.readInt(), // buy type 2
						parser.readInt(), // buy type 3
						parser.readInt(), // buy type 4
						parser.readInt()  // buy type 5
					},
					parser.readInt(), // markup
					parser.readInt(), // buyback rate
					parser.readInt(), // open time
					parser.readInt()  // close time
				));
			}
		} catch (Exception e) {
			readError("error in readShops: " + e.getMessage());
			e.printStackTrace();
			return;
		}

		System.out.println(".....Complete.");
	}
/*
	public void readMobProgs(Area area) {
		System.out.print("Reading MProgs.");
		try {
			String temp;
			int v;
			v = readVnum(buf2);
			while (v > 0) {
				temp = parser.readString();
				area.insert(new MobileProgram(v, temp));
				System.out.println("Read mob prog");
				v = readVnum(buf2);
			}
		} catch (Exception e) {
			System.out.println("...Error");
			return;
		}
		System.out.println("....Complete.");
	}
*/
	public void readHeader(Model model) {
		System.out.println("Reading Header.");
		try {
			parser.skipWhitespace();
			parser.readString(); // filename~
			model.getArea().setAreaName(parser.readString().trim()); // area name~

			parser.skipWhitespace();

			// credits string "{H{{from to} {MCreator {TAreaname~"
			model.getArea().setRangeString(parser.readTo('}') + '}');
			model.getArea().setBuilder(parser.readWord());
			model.getArea().setAreaTitle(parser.readString().trim());

			model.getArea().setVnumRange(parser.readInt(), parser.readInt());

			// File name
			model.getArea().setFileName(inputFile.getName());
			model.getArea().setPathName(inputFile.getPath());
		} catch (Exception e) {
			readError("error in readHeader: " + e.getMessage());
			e.printStackTrace();
			return;
		}

		System.out.println("....Complete.");
	}

	private void inform(String msg) {
		JOptionPane.showMessageDialog(null, msg, msg, JOptionPane.WARNING_MESSAGE);
	}
}
