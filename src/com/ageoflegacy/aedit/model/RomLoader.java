// George Frick
// RomLoader.java
// Area Editor Project, Spring 2002
//

package com.ageoflegacy.aedit.model;

import com.ageoflegacy.aedit.beans.Armor;
import com.ageoflegacy.aedit.beans.Dice;
import com.ageoflegacy.aedit.ui.view.mobView.MudShopView;

import javax.swing.*;
import java.io.File;

// class to load an model.
public class RomLoader extends RomIO {

	int lowVnum;
	int highVnum;

	public RomLoader(String filename) {
		inputFile = new File(filename);
		if (!openArea(true)) {
			open = false;
			System.out.println("Couldn't open file.");
		} else
			open = true;
	}

	public RomLoader(File file) {
		inputFile = file;
		if (!openArea(true)) {
			open = false;
			System.out.println("Couldn't open file.");
		} else
			open = true;
	}

	public boolean isOpen() {
		return open;
	}

	public Area readArea(Area e) {
		if (!open)
			return e;

		System.out.println("Loading Area.");
		System.out.println("Opened " + inputFile + " successfully.");
		area = e;

		try {
			while (buf2.ready()) {
				String temp = buf2.readLine();
				temp = temp.toLowerCase();
				if (temp.length() <= 1)
					continue;
				if (temp.equals("#area"))
					readHeader();
				else if (temp.startsWith("#mobi"))
					readMobiles();
				else if (temp.startsWith("#mobp"))
					readMobProgs();
				else if (temp.startsWith("#room"))
					readRooms();
				else if (temp.startsWith("#obj"))
					readObjects();
				else if (temp.startsWith("#shop"))
					readShops();
				else if (temp.startsWith("#reset"))
					readResets();
				else if (temp.startsWith("#special"))
					readSpecial();
				else if (temp.startsWith("#$"))
					System.out.println("Read Of Area File Complete.");
				else
					System.out.println("\nExtra Line: [" + temp + "]");
			}
		} catch (Exception except) {
			System.out.println("General error reading model file.");
		}
		return area;
	}

	int REMOVE_BIT(int a, int b) {
		return ((~b) & (a)); // remove bits b from a.
	}

	public void readMobiles() {
		System.out.print("Reading Mobiles.");
		Mobile temp;
		int vnum = readVnum(buf2);
		String hold1;

		try {
			while (vnum > 0) {
				temp = new Mobile(vnum, area);

				temp.setName(readToTilde(buf2));
				temp.setShortName(readToTilde(buf2));
				temp.setLongName(readToTilde(buf2));
				temp.setDescription(readToTilde(buf2));
				temp.setRace(RaceTable.getRace(readToTilde(buf2)));

				hold1 = buf2.readLine();
				// System.out.println("Reading mobile: " + temp.toString() );
				// System.out.println("Setting act flags from loader");
				temp.setActFlags(MudConstants.getBitInt(oneArgument(hold1)) | temp.getRace().getActFlags());
				hold1 = trimArgument(hold1);
				temp.setAffectedBy(MudConstants.getBitInt(oneArgument(hold1)) | temp.getRace().getAffectedByFlags());
				hold1 = trimArgument(hold1);
				temp.setAlignment(Integer.parseInt(oneArgument(hold1)));
				hold1 = trimArgument(hold1);
				temp.setGroup(Integer.parseInt(hold1));

				hold1 = buf2.readLine();
				temp.setLevel(Integer.parseInt(oneArgument(hold1)));
				hold1 = trimArgument(hold1);

				temp.setHitRoll(Integer.parseInt(oneArgument(hold1)));
				hold1 = trimArgument(hold1);

				Dice mydice = new Dice(oneArgument(hold1));

				temp.setHitDice(mydice);
				hold1 = trimArgument(hold1);

				temp.setManaDice(new Dice(oneArgument(hold1)));
				hold1 = trimArgument(hold1);

				temp.setDamageDice(new Dice(oneArgument(hold1)));
				hold1 = trimArgument(hold1);
				temp.setDamageType(hold1);

				hold1 = buf2.readLine(); // ac ac ac ac

				temp.setAC(Armor.PIERCE, Integer.parseInt(oneArgument(hold1)));
				hold1 = trimArgument(hold1);
				temp.setAC(Armor.BASH, Integer.parseInt(oneArgument(hold1)));
				hold1 = trimArgument(hold1);
				temp.setAC(Armor.SLASH, Integer.parseInt(oneArgument(hold1)));
				hold1 = trimArgument(hold1);
				temp.setAC(Armor.MAGIC, Integer.parseInt(oneArgument(hold1)));

				hold1 = buf2.readLine(); // flag flag flag flag

				temp.setOffensiveFlags(MudConstants.getBitInt(oneArgument(hold1)) | temp.getRace().getOffensiveFlags());
				hold1 = trimArgument(hold1);
				temp.setImmunityFlags(MudConstants.getBitInt(oneArgument(hold1)) | temp.getRace().getImmunityFlags());
				hold1 = trimArgument(hold1);
				temp.setResistanceFlags(
						MudConstants.getBitInt(oneArgument(hold1)) | temp.getRace().getResistanceFlags());
				hold1 = trimArgument(hold1);
				temp.setVulnerabilityFlags(
						MudConstants.getBitInt(oneArgument(hold1)) | temp.getRace().getVulnerableFlags());

				hold1 = buf2.readLine(); // startpos default sex wealth

				temp.setStartPosition(MudConstants.lookupPos(oneArgument(hold1)));
				hold1 = trimArgument(hold1);
				temp.setDefaultPosition(MudConstants.lookupPos(oneArgument(hold1)));
				hold1 = trimArgument(hold1);
				temp.setSex(MudConstants.lookupSex(oneArgument(hold1)));
				hold1 = trimArgument(hold1);
				temp.setWealth(Integer.parseInt(oneArgument(hold1)));

				// System.out.println("form, parts, size, material");
				hold1 = buf2.readLine(); // form parts size material

				temp.setForm(MudConstants.getBitInt(oneArgument(hold1)) | temp.getRace().getForm());
				hold1 = trimArgument(hold1);
				temp.setParts(MudConstants.getBitInt(oneArgument(hold1)) | temp.getRace().getParts());
				hold1 = trimArgument(hold1);
				temp.setSize(new Size(oneArgument(hold1)));
				hold1 = trimArgument(hold1);
				temp.setMaterial(oneArgument(hold1));

				// System.out.println("About to read extras");
				/*
				 * Here we are marking the stream and then reading, if we have
				 * read a vnum we reset the stream(unread the vnum) and start
				 * the next mobile, otherwise we parse the extra data
				 */
				try {
					// System.out.println("Marking input");
					buf2.mark(50);
					while (readVnum(buf2) == -1) {
						buf2.reset();
						hold1 = buf2.readLine();
						// System.out.println("Switching on extra");
						switch (hold1.charAt(0)) {
						case 'D': {
							// System.out.println("Loading deathcry: " + hold1);
							hold1 = trimArgument(hold1); // remove the D.
							String dCry = "";
							int tloc = hold1.indexOf('~');
							while (tloc == -1) {
								dCry += hold1;
								hold1 = buf2.readLine();
								tloc = hold1.indexOf('~');
							}
							dCry += hold1.substring(0, tloc);
							if (dCry.equals("(null)") || dCry.equals("default"))
								break;
							temp.setDeathCry(dCry);
							break;
						}
						case 'M': {
							hold1 = trimArgument(hold1); // remove the M.
							String mp = hold1.substring(0, hold1.indexOf('~'));

							int mType = MobTrigger.iLookup(oneArgument(mp));
							mp = trimArgument(mp);

							int mVnum = Integer.parseInt(oneArgument(mp));
							mp = trimArgument(mp);

							MobTrigger tigger = new MobTrigger(mType, mVnum, mp);
							temp.addTrigger(tigger);
							break;
						}
						case 'F': {
							hold1 = trimArgument(hold1); // remove the F
							String tType = oneArgument(hold1);
							hold1 = trimArgument(hold1); // remove the type
							int theBit = MudConstants.getBitInt(oneArgument(hold1));
							int rembit;
							if (tType.equals("act")) {
								rembit = REMOVE_BIT(temp.getActFlags(), theBit);
								temp.setActFlags(rembit);
							} else if (tType.equals("aff")) {
								rembit = REMOVE_BIT(temp.getAffectedBy(), theBit);
								temp.setAffectedBy(rembit);
							} else if (tType.equals("off")) {
								rembit = REMOVE_BIT(temp.getOffensiveFlags(), theBit);
								temp.setOffensiveFlags(rembit);
							} else if (tType.equals("imm")) {
								rembit = REMOVE_BIT(temp.getImmunityFlags(), theBit);
								temp.setImmunityFlags(rembit);
							} else if (tType.equals("res")) {
								rembit = REMOVE_BIT(temp.getResistanceFlags(), theBit);
								temp.setResistanceFlags(rembit);
							} else if (tType.equals("vul")) {
								rembit = REMOVE_BIT(temp.getVulnerabilityFlags(), theBit);
								temp.setVulnerabilityFlags(rembit);
							} else if (tType.equals("for")) {
								rembit = REMOVE_BIT(temp.getForm(), theBit);
								temp.setForm(rembit);
							} else if (tType.equals("par")) {
								rembit = REMOVE_BIT(temp.getParts(), theBit);
								temp.setParts(rembit);
							} else
								System.out.println("F ? ?");

							break;
						}
						default: {
							System.out.println("Extra line: " + hold1);
							break;
						}
						}
						buf2.mark(50);
					}
					buf2.reset();
				} catch (Exception e3) {
					System.out.println("Problem reading Extras");
					e3.printStackTrace();
				}
				// Insert new mobile into model and read next vnum.
				area.insert(temp);
				vnum = readVnum(buf2);
			}
			// read all special letters.
		} catch (Exception e) {
			System.out.println("...Error reading mobiles");
			return;
		}
		System.out.println("...Complete");
	}

	public void readObjects() {
		System.out.print("Reading Objects.");
		MudObject temp;
		int vnum = readVnum(buf2);
		String hold1;

		try {
			while (vnum != 0) {
				// 1. vnum

				temp = new MudObject(vnum, area);
				temp.setName(readToTilde(buf2)); // 2. Name
				temp.setShortName(readToTilde(buf2)); // 3. Short
				temp.setLongName(readToTilde(buf2)); // 4. Description
				temp.setMaterial(readToTilde(buf2)); // 5. Material

				// System.out.println("Loading Object: " + temp.getName() + "("
				// + temp.getVnum() + ")");
				// 6. item type, extra, wear
				hold1 = buf2.readLine();
				temp.setType(MudConstants.typeFromString(oneArgument(hold1)));
				hold1 = trimArgument(hold1);
				temp.setExtraFlags(MudConstants.getBitInt(oneArgument(hold1)));
				hold1 = trimArgument(hold1);
				temp.setWearFlags(MudConstants.getBitInt(oneArgument(hold1)));

				// 7. Values. These are parsed based on object type(ACK!)
				// (5 values 0,1,2,3,4)
				hold1 = buf2.readLine();
				int type = temp.getType();

				/*
				 * Item types with spells need to check spell list.
				 */
				switch (type) {
				case MudConstants.ITEM_WEAPON: {
					temp.setsValue(0, oneArgument(hold1));
					// System.out.println(temp.toString() + ":" +
					// temp.getsValue(0));
					hold1 = trimArgument(hold1);
					temp.setiValue(1, Integer.parseInt(oneArgument(hold1)));
					hold1 = trimArgument(hold1);
					temp.setiValue(2, Integer.parseInt(oneArgument(hold1)));
					hold1 = trimArgument(hold1);
					temp.setsValue(3, oneArgument(hold1));
					hold1 = trimArgument(hold1);
					temp.setiValue(4, MudConstants.getBitInt(oneArgument(hold1)));
					break;
				}
				case MudConstants.ITEM_CONTAINER: {
					temp.setiValue(0, Integer.parseInt(oneArgument(hold1)));
					hold1 = trimArgument(hold1);
					temp.setiValue(1, MudConstants.getBitInt(oneArgument(hold1)));
					hold1 = trimArgument(hold1);
					temp.setiValue(2, Integer.parseInt(oneArgument(hold1)));
					hold1 = trimArgument(hold1);
					temp.setiValue(3, Integer.parseInt(oneArgument(hold1)));
					hold1 = trimArgument(hold1);
					temp.setiValue(4, Integer.parseInt(oneArgument(hold1)));
					break;
				}
				case MudConstants.ITEM_DRINK_CON:
				case MudConstants.ITEM_FOUNTAIN: {
					temp.setiValue(0, Integer.parseInt(oneArgument(hold1)));
					hold1 = trimArgument(hold1);
					temp.setiValue(1, Integer.parseInt(oneArgument(hold1)));
					hold1 = trimArgument(hold1);
					temp.setsValue(2, oneArgument(hold1));
					hold1 = trimArgument(hold1);
					temp.setiValue(3, Integer.parseInt(oneArgument(hold1)));
					// v4 unused.
					break;
				}
				case MudConstants.ITEM_WAND:
				case MudConstants.ITEM_STAFF: {
					temp.setiValue(0, Integer.parseInt(oneArgument(hold1)));
					hold1 = trimArgument(hold1);
					temp.setiValue(1, Integer.parseInt(oneArgument(hold1)));
					hold1 = trimArgument(hold1);
					temp.setiValue(2, Integer.parseInt(oneArgument(hold1)));
					hold1 = trimArgument(hold1);
					temp.setsValue(3, oneArgument(hold1));
					// v4 unused
					break;
				}
				case MudConstants.ITEM_POTION:
				case MudConstants.ITEM_PILL:
				case MudConstants.ITEM_SCROLL: {
					temp.setiValue(0, Integer.parseInt(oneArgument(hold1)));
					hold1 = trimArgument(hold1);
					temp.setsValue(1, oneArgument(hold1));
					hold1 = trimArgument(hold1);
					temp.setsValue(2, oneArgument(hold1));
					hold1 = trimArgument(hold1);
					temp.setsValue(3, oneArgument(hold1));
					hold1 = trimArgument(hold1);
					temp.setsValue(4, oneArgument(hold1));
					break;
				}
				case MudConstants.ITEM_MONEY:
				case MudConstants.ITEM_LIGHT:
				case MudConstants.ITEM_ARMOR: {
					temp.setiValue(0, MudConstants.parseInt(oneArgument(hold1)));
					hold1 = trimArgument(hold1);
					temp.setiValue(1, MudConstants.parseInt(oneArgument(hold1)));
					hold1 = trimArgument(hold1);
					temp.setiValue(2, MudConstants.parseInt(oneArgument(hold1)));
					hold1 = trimArgument(hold1);
					temp.setiValue(3, MudConstants.parseInt(oneArgument(hold1)));
					hold1 = trimArgument(hold1);
					temp.setiValue(4, MudConstants.parseInt(oneArgument(hold1)));
					break;
				}
				case MudConstants.ITEM_PORTAL: {
					temp.setiValue(0, MudConstants.parseInt(oneArgument(hold1)));
					hold1 = trimArgument(hold1);
					temp.setiValue(1, MudConstants.getBitInt(oneArgument(hold1)));
					hold1 = trimArgument(hold1);
					temp.setiValue(2, MudConstants.getBitInt(oneArgument(hold1)));
					hold1 = trimArgument(hold1);
					temp.setiValue(3, MudConstants.parseInt(oneArgument(hold1)));
					hold1 = trimArgument(hold1);
					temp.setiValue(4, MudConstants.parseInt(oneArgument(hold1)));
					break;
				}
				case MudConstants.ITEM_FOOD:
				case MudConstants.ITEM_FURNITURE: {
					temp.setiValue(0, MudConstants.parseInt(oneArgument(hold1)));
					hold1 = trimArgument(hold1);
					temp.setiValue(1, MudConstants.parseInt(oneArgument(hold1)));
					hold1 = trimArgument(hold1);
					temp.setiValue(2, MudConstants.getBitInt(oneArgument(hold1)));
					hold1 = trimArgument(hold1);
					temp.setiValue(3, MudConstants.parseInt(oneArgument(hold1)));
					hold1 = trimArgument(hold1);
					temp.setiValue(4, MudConstants.parseInt(oneArgument(hold1)));
					break;
				}
				default: {
					temp.setiValue(0, MudConstants.getBitInt(oneArgument(hold1)));
					hold1 = trimArgument(hold1);
					temp.setiValue(1, MudConstants.getBitInt(oneArgument(hold1)));
					hold1 = trimArgument(hold1);
					temp.setiValue(2, MudConstants.getBitInt(oneArgument(hold1)));
					hold1 = trimArgument(hold1);
					temp.setiValue(3, MudConstants.getBitInt(oneArgument(hold1)));
					hold1 = trimArgument(hold1);
					temp.setiValue(4, MudConstants.getBitInt(oneArgument(hold1)));
					break;
				}
				}

				// 8. level, weight, cost, condition
				// May be parsed in diff ways, flag or number.
				hold1 = buf2.readLine();
				temp.setLevel(Integer.parseInt(oneArgument(hold1)));
				hold1 = trimArgument(hold1);
				temp.setWeight(Integer.parseInt(oneArgument(hold1)));
				hold1 = trimArgument(hold1);
				temp.setCost(Integer.parseInt(oneArgument(hold1)));
				hold1 = trimArgument(hold1);
				temp.setCondition(MudConstants.conditionLookup(oneArgument(hold1)));

				// 14. parse affects by letter.
				/*
				 * Here we are marking the stream and then reading, if we have
				 * read a vnum we reset the stream(unread the vnum) and start
				 * the next object, otherwise we parse the extra data
				 */
				buf2.mark(50);
				while (readVnum(buf2) == -1) {
					buf2.reset();
					hold1 = buf2.readLine();

					switch (hold1.charAt(0)) {
					case 'A': {
						hold1 = buf2.readLine();
						String nType = oneArgument(hold1);
						hold1 = trimArgument(hold1);
						String nValue = oneArgument(hold1);
						try {
							int nType2 = Integer.parseInt(nType);
							int nValue2 = Integer.parseInt(nValue);
							ObjectAffect nAffect = new ObjectAffect(nType2, nValue2);
							temp.addAffect(nAffect);
						} catch (Exception exct) {
							System.out.println("Exception creating object affect in loader");
						}
						break;
					}
					case 'R': // wear
					{
						temp.setWearMessage(readToTilde(buf2));
						break;
					}
					case 'M': // remove
					{
						temp.setRemoveMessage(readToTilde(buf2));
						break;
					}
					case 'E': // extra desc
					{
						String dKeyword = readToTilde(buf2);
						String dDesc = readToTilde(buf2);
						temp.getExtraDescriptions().put(dKeyword, dDesc);
						break;
					}
					case 'F': // affects on player?
					{
						System.out.println("Unsupported object affect: " + hold1);
						break;
					}
					default: {
						System.out.println("Extra line: " + hold1);
						break;
					}
					}

					buf2.mark(50);
				}
				buf2.reset();

				// Insert new object into model and read next vnum.
				area.insert(temp);
				vnum = readVnum(buf2);
			}
		} catch (Exception e) {
			System.out.println("...Failed." + e.getMessage());
			return;
		}
		System.out.println("...Complete.");
	}

	public void readRooms() {
		System.out.print("Reading Rooms.");
		Room temp;
		int vnum = readVnum(buf2);
		String hold1 = "!ERROR!", hold2;
		boolean dumped = false;

		try {
			while (vnum != 0) {
				// System.out.println("Attempt to read room #" + vnum + ".");
				temp = new Room(vnum, area);
				temp.setName(readToTilde(buf2));
				temp.setDescription(readToTilde(buf2));
				hold1 = buf2.readLine(); // model num, flags, sector
				hold1 = hold1.substring(hold1.indexOf(' ') + 1); // flags,
																	// sector
				hold2 = hold1.substring(0, hold1.indexOf(' ')); // flags
				hold1 = hold1.substring(hold1.indexOf(' ') + 1); // sector
				temp.setFlags(MudConstants.parseInt(hold2));
				temp.setSector(Integer.parseInt(hold1));

				hold1 = buf2.readLine(); // read first flag

				// report("sector, flags set");
				while (hold1.charAt(0) != 'S') {
					switch (hold1.charAt(0)) {
					case 'E': {
						String keyword = readToTilde(buf2);
						String desc = readToTilde(buf2);
						temp.getExtraDescriptions().put(keyword, desc);
						break;
					}
					case 'H': {
						break;
					}
					case 'M': {
						// M 0 H 0
						hold1 = hold1.substring(hold1.indexOf(' ') + 1); // 0 H
																			// 0
						hold2 = hold1.substring(0, hold1.indexOf(' ')); // 0
						temp.setMana(Integer.parseInt(hold2));
						hold1 = hold1.substring(hold1.indexOf(' ') + 1); // H 0
						hold1 = hold1.substring(hold1.indexOf(' ') + 1); // 0
						temp.setHeal(Integer.parseInt(hold1));
						break;
					}
					case 'D': {
						MudExit exitTemp;
						int locks, key, toVnum;
						int dir = ((int) hold1.charAt(1)) - 48;
						// System.out.println(".........." + hold1.charAt(1));
						String exitDesc = readToTilde(buf2);
						String exitName = readToTilde(buf2);
						String lkv = buf2.readLine();
						locks = Integer.parseInt(oneArgument(lkv));
						lkv = trimArgument(lkv);
						key = Integer.parseInt(oneArgument(lkv));
						lkv = trimArgument(lkv);
						toVnum = Integer.parseInt(lkv);
						exitTemp = new MudExit(toVnum, temp);
						exitTemp.setKey(key);
						exitTemp.setFlagsByKey(locks);
						// exitTemp.setFlags(locks);
						// if( locks != 0 )
						// System.out.println(temp.toString() + ":locks-> " +
						// locks );
						exitTemp.setKeyword(exitName);
						exitTemp.setDescription(exitDesc);
						if (toVnum < lowVnum || toVnum > highVnum) {
							temp.setExitFromArea(exitTemp, dir); // add exit to
																	// room.
						} else {
							temp.setExit(exitTemp, dir); // add exit to room.
						}
						break;
					}
					}
					hold1 = buf2.readLine();
				}

				area.insert(temp);
				vnum = readVnum(buf2);
				// System.out.println("Read a single room.");
			}
		} catch (Exception e) {
			System.out.println("Failed to read all rooms?! -" + hold1);
			return;
		}

		System.out.println(".....Complete.");

		// inform("Dumped exit(s) leading out of model/vnum range.");
	}

	public void readSpecial() {
		System.out.print("Reading Specials.");
		try {
			String special = buf2.readLine();
			while (!special.startsWith("S")) {
				special = trimArgument(special);
				int mVnum = Integer.parseInt(oneArgument(special));
				special = trimArgument(special);
				Mobile temp = area.getMobile(mVnum);
				if (temp != null)
					temp.setSpecial(special);
				else
					System.out.println("Special discarded, no mobile");

				special = buf2.readLine();
			}
		} catch (Exception e) {
			System.out.println("...Error.");
			return;
		}
		System.out.println("..Complete. [NOT SUPPORTED BY EDITOR]");
		System.out.println("Mafia will Store/Save your specials, but not allow processing.");
	}

	public void readResets() {
		System.out.print("Reading Resets.");
		/*
		 * remember lines starting with * are comments.
		 */
		MudReset temp;
		String command;
		int arg1, arg2, arg3 = 0, arg4 = 0, chance;

		try {
			String reset = buf2.readLine();
			while (!reset.startsWith("S")) {
				if (!reset.startsWith("*")) {
					// System.out.println("parsing reset: " + reset );
					command = oneArgument(reset);
					reset = trimArgument(reset);
					chance = Integer.parseInt(oneArgument(reset));
					reset = trimArgument(reset);
					arg1 = Integer.parseInt(oneArgument(reset));
					reset = trimArgument(reset);
					arg2 = Integer.parseInt(oneArgument(reset));
					reset = trimArgument(reset);

					if (!command.startsWith("G") && !command.startsWith("R")) {
						arg3 = Integer.parseInt(oneArgument(reset));
						reset = trimArgument(reset);
					}

					if (command.startsWith("P") || command.startsWith("M")) {
						arg4 = Integer.parseInt(oneArgument(reset));
					}

					temp = new MudReset(command, arg1, arg2, arg3, arg4, chance);
					// System.out.println("Created reset: " + temp.toString() );
					area.insert(temp);
				}
				reset = buf2.readLine();
			}
		} catch (Exception e) {
			System.out.println("...Error.");
			return;
		}
		System.out.println("....Complete. (Read " + area.getResets().size() + " resets.)");
	}

	public void readShops() {
		System.out.print("Reading Shops.");
		MudShopView shop;
		int[] data;
		int keeper, bProf, sProf, openH, closeH;
		try {
			String temp = buf2.readLine();
			while (!temp.startsWith("0")) {
				data = new int[MudConstants.MAX_TRADE];

				keeper = Integer.parseInt(oneArgument(temp));
				temp = trimArgument(temp);

				for (int a = 0; a < MudConstants.MAX_TRADE; a++) {
					data[a] = Integer.parseInt(oneArgument(temp));
					temp = trimArgument(temp);
				}

				bProf = Integer.parseInt(oneArgument(temp));
				temp = trimArgument(temp);
				sProf = Integer.parseInt(oneArgument(temp));
				temp = trimArgument(temp);
				openH = Integer.parseInt(oneArgument(temp));
				temp = trimArgument(temp);
				closeH = Integer.parseInt(oneArgument(temp));

				shop = new MudShopView(keeper, data, bProf, sProf, openH, closeH);
				Mobile sMob = area.getMobile(keeper);
				sMob.setShop(shop);
				// System.out.println("inserting shop:");
				// System.out.println(shop.toString());
				// model.insert(shop);

				temp = buf2.readLine();
			}
		} catch (Exception e) {
			System.out.println("...Error.");
			return;
		}
		System.out.println(".....Complete.");
	}

	public void readMobProgs() {
		System.out.print("Reading MProgs.");
		try {
			String temp;
			int v;
			v = readVnum(buf2);
			while (v > 0) {
				temp = readToTilde(buf2);
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

	public void readHeader() {
		System.out.println("Reading Header.");
		try {
			String line, command, argument;
			int indexa;

			line = buf2.readLine();
			while (line.toLowerCase().trim().equals("end") == false) {
				indexa = line.indexOf(' ');
				command = line.substring(0, indexa);
				command = command.toLowerCase();
				argument = line.substring(indexa + 1, line.length());
				argument = argument.trim();
				argument = argument.replaceAll("~", "");
				if (command.toLowerCase().startsWith("credit")) {
					// Do nothing.
				} else if (command.toLowerCase().startsWith("file")) {
					// do nothing.
				} else if (command.toLowerCase().startsWith("range")) {
					area.setRangeString(argument.trim());
				} else if (command.toLowerCase().startsWith("builder")) {
					area.setBuilder(argument.trim());
				} else if (command.toLowerCase().startsWith("vnum")) {
					indexa = argument.indexOf(' ');
					int low, high;
					low = Integer.parseInt(argument.substring(0, indexa));
					high = Integer.parseInt(argument.substring(indexa + 1, argument.length()));
					area.setVnumRange(low, high);
					lowVnum = low;
					highVnum = high;
				} else if (command.toLowerCase().startsWith("security")) {
					// do nothing
				} else if (command.toLowerCase().startsWith("name")) {
					area.setAreaName(argument.trim());
				} else if (command.toLowerCase().startsWith("flag")) {
					area.setFlags(Integer.parseInt(argument.trim()));
				}
				line = buf2.readLine();
			}
			// File name
			area.setFileName(inputFile.getName());
			area.setPathName(inputFile.getPath());

		} catch (Exception e) {
			System.out.println("...Error.");
			return;
		}

		System.out.println("....Complete.");
	}

	private void inform(String msg) {
		JOptionPane.showMessageDialog(null, msg, msg, JOptionPane.WARNING_MESSAGE);
	}
}
