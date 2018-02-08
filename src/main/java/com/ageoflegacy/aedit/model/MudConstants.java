package com.ageoflegacy.aedit.model;
// George Frick

// MudConstants.java
// AreaEditor project, spring 2002
//
// This file packs up all the constants that a standard rom would have
// into a single file, it also includes any matching lookup functions
// that may be needed.

public class MudConstants {

	// rom constants for condition
	public final static int[] conditionFlags = { 100, 90, 75, 50, 25, 10, 0 };

	public final static String[] conditionNames = { "P", "G", "A", "W", "D", "B", "R" };

	public static int conditionLookup(String l) {
		int a = 6;
		while (a >= 0) {
			if (l.startsWith(conditionNames[a]))
				return conditionFlags[a];
			else
				a--;
		}

		return 100;
	}

	public static String conditionString(int l) {
		int a = 6;
		while (a >= 0) {
			if (l == conditionFlags[a])
				return conditionNames[a];
			else if (a != 6 && a != 0) {
				if (l < conditionFlags[a + 1] && l > conditionFlags[a - 1])
					return conditionNames[a];
			}

			a--;
		}

		return conditionNames[0];
	}

	// for shops.
	public final static int MAX_TRADE = 5;

	/*
	 * Bit flag constants from MERC.H. These constants allow easier
	 * representation of a bit set.
	 */
	public final static int BIT_A = 1; // 0001
	public final static int BIT_B = 2; // 0010
	public final static int BIT_C = 4; // 0100
	public final static int BIT_D = 8; // 1000
	public final static int BIT_E = 16; // 0001 0000
	public final static int BIT_F = 32; // 0010 0000
	public final static int BIT_G = 64; // 0100 0000
	public final static int BIT_H = 128; // 1000 0000
	public final static int BIT_I = 256; // 0001 0000 0000
	public final static int BIT_J = 512; // 0010 0000 0000
	public final static int BIT_K = 1024; // 0100 0000 0000
	public final static int BIT_L = 2048; // 1000 0000 0000
	public final static int BIT_M = 4096; // 0001 0000 0000 0000
	public final static int BIT_N = 8192; // 0010 0000 0000 0000
	public final static int BIT_O = 16384; // 0100 0000 0000 0000
	public final static int BIT_P = 32768; // 1000 0000 0000 0000
	public final static int BIT_Q = 65536; // 0001 0000 0000 0000 0000
	public final static int BIT_R = 131072; // 0010 0000 0000 0000 0000
	public final static int BIT_S = 262144; // 0100 0000 0000 0000 0000
	public final static int BIT_T = 524288; // 1000 0000 0000 0000 0000
	public final static int BIT_U = 1048576; // 0001 0000 0000 0000 0000 0000
	public final static int BIT_V = 2097152; // 0010 0000 0000 0000 0000 0000
	public final static int BIT_W = 4194304; // 0100 0000 0000 0000 0000 0000
	public final static int BIT_X = 8388608; // 1000 0000 0000 0000 0000 0000
	public final static int BIT_Y = 16777216; // 0001 0000 0000 0000 0000 0000
												// 0000
	public final static int BIT_Z = 33554432; // 0010 0000 0000 0000 0000 0000
												// 0000
	public final static int BIT_aa = 67108864; // 0100 0000 0000 0000 0000 0000
												// 0000
	public final static int BIT_bb = 134217728; // 1000 0000 0000 0000 0000 0000
												// 0000
	public final static int BIT_cc = 268435456; // 0001 0000 0000 0000 0000 0000
												// 0000 0000
	public final static int BIT_dd = 536870912; // 0010 0000 0000 0000 0000 0000
												// 0000 0000
	public final static int BIT_ee = 1073741824; // 0100 0000 0000 0000 0000
													// 0000 0000 0000
	// a number is negative if first bit is set.. ^ sign bit :-)

	public final static String[] bitNames = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O",
			"P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a", "b", "c", "d", "e" };

	public final static int[] bitFlags = { BIT_A, BIT_B, BIT_C, BIT_D, BIT_E, BIT_F, BIT_G, BIT_H, BIT_I, BIT_J, BIT_K,
			BIT_L, BIT_M, BIT_N, BIT_O, BIT_P, BIT_Q, BIT_R, BIT_S, BIT_T, BIT_U, BIT_V, BIT_W, BIT_X, BIT_Y, BIT_Z,
			BIT_aa, BIT_bb, BIT_cc, BIT_dd, BIT_ee };

	public final static int bitCount = 31;

	/*
	 * Act flags for mobiles.
	 */
	public final static int ACT_IS_NPC = BIT_A; // must be set on all mobs.
	public final static int ACT_SENTINEL = BIT_B;
	public final static int ACT_SCAVENGER = BIT_C;
	public final static int ACT_AGGRESSIVE = BIT_F;
	public final static int ACT_STAY_AREA = BIT_G;
	public final static int ACT_WIMPY = BIT_H;
	public final static int ACT_PET = BIT_I;
	public final static int ACT_TRAIN = BIT_J;
	public final static int ACT_PRACTICE = BIT_K;
	// PLR_QUESTOR (L)
	public final static int ACT_CLERIC = BIT_Q;
	public final static int ACT_MAGE = BIT_R;
	public final static int ACT_THIEF = BIT_S;
	public final static int ACT_WARRIOR = BIT_T;
	public final static int ACT_NOALIGN = BIT_U;
	public final static int ACT_NOPURGE = BIT_V;
	public final static int ACT_OUTDOORS = BIT_W;
	public final static int ACT_INDOORS = BIT_Y;
	public final static int ACT_IS_HEALER = BIT_aa;
	public final static int ACT_GAIN = BIT_bb;
	public final static int ACT_UPDATE_ALWAYS = BIT_cc;
	public final static int ACT_IS_CHANGER = BIT_dd;
	public final static int ACT_LEADER = BIT_ee;

	public final static int NUM_ACT_FLAGS = 20;

	public final static String[] actFlagNames = { "Sentinel", "Scavenger", "Aggressive", "Stay Area", "Wimpy", "Pet",
			"Train", "Practice", "Cleric", "Mage", "Thief", "Warrior", "NoAlign", "NoPurge", "Outdoors", "Indoors",
			"Healer", "Gain", "Update Always", "Changer" };

	public final static int[] actFlags = { ACT_SENTINEL, ACT_SCAVENGER, ACT_AGGRESSIVE, ACT_STAY_AREA, ACT_WIMPY,
			ACT_PET, ACT_TRAIN, ACT_PRACTICE, ACT_CLERIC, ACT_MAGE, ACT_THIEF, ACT_WARRIOR, ACT_NOALIGN, ACT_NOPURGE,
			ACT_OUTDOORS, ACT_INDOORS, ACT_IS_HEALER, ACT_GAIN, ACT_UPDATE_ALWAYS, ACT_IS_CHANGER };

	/*
	 * Alignment flags
	 */
	public final static int ALIGN_SATANIC = 0;
	public final static int ALIGN_EVIL = 1;
	public final static int ALIGN_NEUTRAL = 2;
	public final static int ALIGN_GOOD = 3;
	public final static int ALIGN_ANGELIC = 4;

	public final static String[] alignmentArray = { "Satanic", "Evil", "Neutral", "Good", "Angelic" };

	public static int alignmentLookup(int align) {
		switch (align) {
		case ALIGN_SATANIC:
			return -1000;
		case ALIGN_EVIL:
			return -500;
		case ALIGN_NEUTRAL:
			return 0;
		case ALIGN_GOOD:
			return 500;
		case ALIGN_ANGELIC:
			return 1000;
		default:
			return 0;
		}
	}

	public static int arrayAlignmentLookup(int align) {
		if (align > 249) {
			if (align > 749)
				return ALIGN_ANGELIC;
			else
				return ALIGN_GOOD;
		} else if (align < -249) {
			if (align < -749)
				return ALIGN_SATANIC;
			else
				return ALIGN_EVIL;
		} else
			return ALIGN_NEUTRAL;
	}

	/*
	 * sex flags
	 */
	public final static int SEX_NEUTRAL = 0;
	public final static int SEX_MALE = 1;
	public final static int SEX_FEMALE = 2;

	public final static String[] sexArray = { "Neutral", "Male", "Female" };

	public static int lookupSex(String arg) {
		String temp = arg.toLowerCase();
		switch (temp.charAt(0)) {
		case 'e':
		case 'n':
			return SEX_NEUTRAL;
		case 'm':
			return SEX_MALE;
		case 'f':
			return SEX_FEMALE;
		default:
			return SEX_NEUTRAL;
		}
	}

	public static String getSexString(int sex) {
		switch (sex) {
		case SEX_NEUTRAL:
			return "none";
		case SEX_MALE:
			return "male";
		case SEX_FEMALE:
			return "female";
		default:
			return "male";
		}
	}

	/*
	 * returns string representation of a bit set rom style
	 */
	public static String getBitString(int bit) {
		if (bit == 0)
			return "0";

		StringBuilder builder = new StringBuilder();
		if ((bit & BIT_A) == BIT_A)
			builder.append("A");
		if ((bit & BIT_B) == BIT_B)
			builder.append("B");
		if ((bit & BIT_C) == BIT_C)
			builder.append("C");
		if ((bit & BIT_D) == BIT_D)
			builder.append("D");
		if ((bit & BIT_E) == BIT_E)
			builder.append("E");
		if ((bit & BIT_F) == BIT_F)
			builder.append("F");
		if ((bit & BIT_G) == BIT_G)
			builder.append("G");
		if ((bit & BIT_H) == BIT_H)
			builder.append("H");
		if ((bit & BIT_I) == BIT_I)
			builder.append("I");
		if ((bit & BIT_J) == BIT_J)
			builder.append("J");
		if ((bit & BIT_K) == BIT_K)
			builder.append("K");
		if ((bit & BIT_L) == BIT_L)
			builder.append("L");
		if ((bit & BIT_M) == BIT_M)
			builder.append("M");
		if ((bit & BIT_N) == BIT_N)
			builder.append("N");
		if ((bit & BIT_O) == BIT_O)
			builder.append("O");
		if ((bit & BIT_P) == BIT_P)
			builder.append("P");
		if ((bit & BIT_Q) == BIT_Q)
			builder.append("Q");
		if ((bit & BIT_R) == BIT_R)
			builder.append("R");
		if ((bit & BIT_S) == BIT_S)
			builder.append("S");
		if ((bit & BIT_T) == BIT_T)
			builder.append("T");
		if ((bit & BIT_U) == BIT_U)
			builder.append("U");
		if ((bit & BIT_V) == BIT_V)
			builder.append("V");
		if ((bit & BIT_W) == BIT_W)
			builder.append("W");
		if ((bit & BIT_X) == BIT_X)
			builder.append("X");
		if ((bit & BIT_Y) == BIT_Y)
			builder.append("Y");
		if ((bit & BIT_Z) == BIT_Z)
			builder.append("Z");
		if ((bit & BIT_aa) == BIT_aa)
			builder.append("a");
		if ((bit & BIT_bb) == BIT_bb)
			builder.append("b");
		if ((bit & BIT_cc) == BIT_cc)
			builder.append("c");
		if ((bit & BIT_dd) == BIT_dd)
			builder.append("d");
		if ((bit & BIT_ee) == BIT_ee)
			builder.append("e");

		return builder.toString();
	}

	public static int parseInt(String value) {
		int ret = 0;
		try {
			ret = Integer.parseInt(value);
		} catch (Exception e) {
			try {
				ret = getBitInt(value); // it was stored as a bit for some
										// reason?
			} catch (Exception e2) {
				System.out.println("Tried to parse " + value + " as int and bitInt, but failed.");
			}
		}
		return ret;
	}

	/*
	 * Takes in a bit string and returns integer bitset
	 */
	public static int getBitInt(String bit) {
		int value = 0;

		for (int a = 0; a < bit.length(); a++) {
			switch (bit.charAt(a)) {
			case 'A': {
				value += BIT_A;
				break;
			}
			case 'B': {
				value += BIT_B;
				break;
			}
			case 'C': {
				value += BIT_C;
				break;
			}
			case 'D': {
				value += BIT_D;
				break;
			}
			case 'E': {
				value += BIT_E;
				break;
			}
			case 'F': {
				value += BIT_F;
				break;
			}
			case 'G': {
				value += BIT_G;
				break;
			}
			case 'H': {
				value += BIT_H;
				break;
			}
			case 'I': {
				value += BIT_I;
				break;
			}
			case 'J': {
				value += BIT_J;
				break;
			}
			case 'K': {
				value += BIT_K;
				break;
			}
			case 'L': {
				value += BIT_L;
				break;
			}
			case 'M': {
				value += BIT_M;
				break;
			}
			case 'N': {
				value += BIT_N;
				break;
			}
			case 'O': {
				value += BIT_O;
				break;
			}
			case 'P': {
				value += BIT_P;
				break;
			}
			case 'Q': {
				value += BIT_Q;
				break;
			}
			case 'R': {
				value += BIT_R;
				break;
			}
			case 'S': {
				value += BIT_S;
				break;
			}
			case 'T': {
				value += BIT_T;
				break;
			}
			case 'U': {
				value += BIT_U;
				break;
			}
			case 'V': {
				value += BIT_V;
				break;
			}
			case 'W': {
				value += BIT_W;
				break;
			}
			case 'X': {
				value += BIT_X;
				break;
			}
			case 'Y': {
				value += BIT_Y;
				break;
			}
			case 'Z': {
				value += BIT_Z;
				break;
			}
			case 'a': {
				a++;
				value += BIT_aa;
				break;
			}
			case 'b': {
				a++;
				value += BIT_bb;
				break;
			}
			case 'c': {
				a++;
				value += BIT_cc;
				break;
			}
			case 'd': {
				a++;
				value += BIT_dd;
				break;
			}
			case 'e': {
				a++;
				value += BIT_ee;
				break;
			}
			case '0': {
				return 0;
			}
			case '\'':
			case '\"':
				return 0;
			// need the rest here.
			default: {
				System.out.println("found other in getbitint: " + bit.charAt(a));
				try {
					return Integer.parseInt(bit);
				} catch (Exception ext) {
					return 0;
				}

			}
			}
		}

		return value;
	}

	/*
	 * Position values
	 */
	public final static int POS_DEAD = 0;
	public final static int POS_MORTAL = 1;
	public final static int POS_INCAP = 2;
	public final static int POS_STUNNED = 3;
	public final static int POS_SLEEPING = 4;
	public final static int POS_RESTING = 5;
	public final static int POS_SITTING = 6;
	public final static int POS_FIGHTING = 7;
	public final static int POS_STANDING = 8;

	public final static String[] positionNames = { "Sleeping", "Resting", "Sitting", "Standing" };

	public final static int[] positionFlags = { POS_SLEEPING, POS_RESTING, POS_SITTING, POS_STANDING };

	public final static int NUM_POSITIONS = 4;

	public static int arrayLookupPos(int which) {
		switch (which) {
		case POS_SLEEPING:
			return 0;
		case POS_RESTING:
			return 1;
		case POS_SITTING:
			return 2;
		case POS_STANDING:
			return 3;
		default:
			return 3;
		}
	}

	public static int lookupPos(String lookup) {
		String temp = lookup.toLowerCase();
		if (temp.equals("stand"))
			return POS_STANDING;
		else
			return POS_SITTING;
	}

	public static String getPositionString(int pos) {
		switch (pos) {
		case POS_DEAD:
			return "dead";
		case POS_MORTAL:
			return "mortaly";
		case POS_INCAP:
			return "incapacitated";
		case POS_STUNNED:
			return "stunned";
		case POS_SLEEPING:
			return "sleep";
		case POS_RESTING:
			return "rest";
		case POS_SITTING:
			return "sit";
		case POS_FIGHTING:
			return "fight";
		case POS_STANDING:
			return "stand";
		default:
			return "stand";
		}
	}

	/*
	 * Size constants
	 */
	public final static int SIZE_XS = 0;
	public final static int SIZE_SMALL = 1;
	public final static int SIZE_MEDIUM = 2;
	public final static int SIZE_LARGE = 3;
	public final static int SIZE_XL = 4;
	public final static int MAX_SIZE = SIZE_XL;

	/*
	 * direction constants
	 */
	public final static int DIR_NORTH = 0;
	public final static int DIR_EAST = 1;
	public final static int DIR_SOUTH = 2;
	public final static int DIR_WEST = 3;
	public final static int DIR_UP = 4;
	public final static int DIR_DOWN = 5;

	public final static int MAX_EXITS = 6;

	/*
	 * Exit flags
	 */
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
	public final static int NUM_EXIT_FLAGS = 12;

	public final static String[] exitNames = { "Is Door", "Closed", "Locked", "Pick Proof", "No Pass", "Easy pick",
			"Hard pick", "Infur. Pick", "No Close", "No Lock", "Wall", "No Wall" };

	public final static int[] exitFlags = { EXIT_ISDOOR, EXIT_CLOSED, EXIT_LOCKED, EXIT_PICKPROOF, EXIT_NOPASS,
			EXIT_EASY, EXIT_HARD, EXIT_INFURIATING, EXIT_NOCLOSE, EXIT_NOLOCK, EXIT_WALL, EXIT_NOWALL };

	public static String exitFlagString(int flag) {
		if (flag == 0)
			return "None";

		String temp = "";
		for (int a = 0; a < 12; a++) {
			if ((flag & exitFlags[a]) == exitFlags[a])
				temp += (exitNames[a] + " ");
		}

		return temp;
	}

	/*
	 ** room flags
	 */
	public final static int FLAG_DARK = 1; // 00000000000000000001
	public final static int FLAG_NO_MOB = 4; // 00000000000000000100
	public final static int FLAG_INDOORS = 8; // 00000000000000001000
	public final static int FLAG_PRIVATE = 512; // 00000000001000000000
	public final static int FLAG_SAFE = 1024; // 00000000010000000000
	public final static int FLAG_PET_SHOP = 4096; // 00000001000000000000
	public final static int FLAG_NO_RECALL = 8192; // 00000010000000000000
	public final static int FLAG_UNDERWATER = 16384; // 00000100000000000000
	public final static int FLAG_GODS_ONLY = 32768; // 00001000000000000000
	public final static int FLAG_DEATH_TRAP = 65536; // 00010000000000000000
	public final static int FLAG_NEWBIES_ONLY = 131072; // 00100000000000000000
	public final static int FLAG_LAW = 262144; // 01000000000000000000
	public final static int FLAG_NOWHERE = 524288; // 10000000000000000000
	public final static int FLAG_NOMAGIC = 2097152; // etc.
	public final static int FLAG_TELEPORT = 4194304;
	public final static int FLAG_FLY_FALL = 8388608;
	public final static int FLAG_NO_WEAPON = 33554432;
	public final static int FLAG_NO_GATE_IN = 67108864;
	public final static int FLAG_NO_GATE_OUT = 134217728;
	public final static int FLAG_NOSUMMON = 268435456;
	public final static int FLAG_SLICK = 536870912;

	public final static String[] roomFlags = { "dark", "no_mob", "indoors", "private", "safe", "pet_shop", "no_recall",
			"underwater", "gods_only", "death_t", "newbies_only", "law", "nowhere", "teleport", "fly_fall", "no_weapon",
			"no_gate_in", "no_gate_out", "nosummon", "slick", "nomagic" };

	public final static int[] roomFlagData = { BIT_A, BIT_C, BIT_D, BIT_J, BIT_K, BIT_M, BIT_N, BIT_O, BIT_P, BIT_Q,
			BIT_R, BIT_S, BIT_T, BIT_W, BIT_X, BIT_Z, BIT_aa, BIT_bb, BIT_cc, BIT_dd, BIT_V };

	public final static int roomFlagCount = 21;

	public final static String[] areaFlagNames = { "Dark", "No Mob", "Indoors", "Safe", "No Recall", "Underwater",
			"Gods Only", "Newbie Only", "Law", "Nowhere", "No Weapon", "No Gate In", "No Gate Out", "Nosummon", "Slick",
			"Nomagic" };

	public final static int[] areaFlagData = { FLAG_DARK, FLAG_NO_MOB, FLAG_INDOORS, FLAG_SAFE, FLAG_NO_RECALL,
			FLAG_UNDERWATER, FLAG_GODS_ONLY, FLAG_NEWBIES_ONLY, FLAG_LAW, FLAG_NOWHERE, FLAG_NO_WEAPON, FLAG_NO_GATE_IN,
			FLAG_NO_GATE_OUT, FLAG_NOSUMMON, FLAG_SLICK, FLAG_NOMAGIC };

	public final static int areaFlagCount = 16;

	/*
	 * Item constants
	 */
	public final static int ITEM_LIGHT = 1;
	public final static int ITEM_SCROLL = 2;
	public final static int ITEM_WAND = 3;
	public final static int ITEM_STAFF = 4;
	public final static int ITEM_WEAPON = 5;
	// 6, 7
	public final static int ITEM_TREASURE = 8;
	public final static int ITEM_ARMOR = 9;
	public final static int ITEM_POTION = 10;
	public final static int ITEM_CLOTHING = 11;
	public final static int ITEM_FURNITURE = 12;
	public final static int ITEM_TRASH = 13;
	// 14
	public final static int ITEM_CONTAINER = 15;
	// 16
	public final static int ITEM_DRINK_CON = 17;
	public final static int ITEM_KEY = 18;
	public final static int ITEM_FOOD = 19;
	public final static int ITEM_MONEY = 20;
	// 21
	public final static int ITEM_BOAT = 22;
	public final static int ITEM_CORPSE_NPC = 23;
	public final static int ITEM_CORPSE_PC = 24;
	public final static int ITEM_FOUNTAIN = 25;
	public final static int ITEM_PILL = 26;
	// 27
	public final static int ITEM_MAP = 28;
	public final static int ITEM_PORTAL = 29;
	public final static int ITEM_WARP_STONE = 30;
//	public final static int ITEM_ROOM_KEY = 31;
	public final static int ITEM_GEM = 32;
	public final static int ITEM_JEWELRY = 33;
	public final static int ITEM_JUKEBOX = 34;
//	public final static int ITEM_PBTUBE = 35;
//	public final static int ITEM_PBGUN = 36;
	public final static int ITEM_MATERIAL = 37;
	public final static int ITEM_ANVIL = 38;
	// 39
	public final static int ITEM_WEDDINGRING = 40;
	public final static int ITEM_TOKEN = 41;
	public final static int ITEM_WARP_CRYSTAL = 42;

	public final static String[] typeNames = { "Light", "Scroll", "Wand", "Staff", "Weapon", "Treasure", "Armor",
			"Potion", "Clothing", "Furniture", "Trash", "Container", "Drink", "Key", "Food", "Money", "Boat",
			"Fountain", "Pill", "Map", "Portal", "Warp_stone", "Gem", "Jewelry", "Jukebox", "Material",
			"Anvil", "Wedding_ring", "Token", "Warp_crystal" };

	public final static int[] typeFlags = { ITEM_LIGHT, ITEM_SCROLL, ITEM_WAND, ITEM_STAFF, ITEM_WEAPON, ITEM_TREASURE,
			ITEM_ARMOR, ITEM_POTION, ITEM_CLOTHING, ITEM_FURNITURE, ITEM_TRASH, ITEM_CONTAINER, ITEM_DRINK_CON,
			ITEM_KEY, ITEM_FOOD, ITEM_MONEY, ITEM_BOAT, ITEM_FOUNTAIN, ITEM_PILL, ITEM_MAP, ITEM_PORTAL,
			ITEM_WARP_STONE, ITEM_GEM, ITEM_JEWELRY, ITEM_JUKEBOX,
			ITEM_MATERIAL, ITEM_ANVIL, ITEM_WEDDINGRING, ITEM_TOKEN, ITEM_WARP_CRYSTAL };

	public final static int NUM_ITEMS = typeFlags.length;

	public static String stringFromType(int type) {
		for (int a = 0; a < NUM_ITEMS; a++) {
			if (type == typeFlags[a])
				return typeNames[a];
		}

		return "Trash";
	}

	public static int typeFromString(String type) {
		for (int a = 0; a < NUM_ITEMS; a++) {
			if (type.equalsIgnoreCase(typeNames[a]))
				return typeFlags[a];
		}

		return ITEM_TRASH;
	}

	public static int TypeLookup(int location) {
		return typeFlags[location];
	}

	public static int typePositionLookup(int type) {
		for (int a = 0; a < NUM_ITEMS; a++) {
			if (type == typeFlags[a])
				return a;
		}

		return -1;
	}

	public static boolean isValidType(final int a) {
		switch (a) {
		case 0:
		case 6:
		case 7:
		case 14:
		case 16:
		case 21:
		case 27: {
			return false;
		}
		default: {
			return true;
		}
		}
	}

	/*
	 * Flags for wear positions
	 */
	public final static int ITEM_TAKE = 1;
	public final static int ITEM_WEAR_FINGER = 2;
	public final static int ITEM_WEAR_NECK = 4;
	public final static int ITEM_WEAR_BODY = 8;
	public final static int ITEM_WEAR_HEAD = 16;
	public final static int ITEM_WEAR_LEGS = 32;
	public final static int ITEM_WEAR_FEET = 64;
	public final static int ITEM_WEAR_HANDS = 128;
	public final static int ITEM_WEAR_ARMS = 256;
	public final static int ITEM_WEAR_SHIELD = 512;
	public final static int ITEM_WEAR_ABOUT = 1024;
	public final static int ITEM_WEAR_WAIST = 2048;
	public final static int ITEM_WEAR_WRIST = 4096;
	public final static int ITEM_WIELD = 8192;
	public final static int ITEM_HOLD = 16384;
	public final static int ITEM_NO_SAC = 32768;
	public final static int ITEM_WEAR_FLOAT = 65536;
	public final static int ITEM_WEAR_EARS = 131072;

	public final static String[] wearNames = { "Take", "Finger", "Neck", "Body", "Head", "Legs", "Feet", "Hands",
			"Arms", "Shield", "About", "Waist", "Wrist", "Wield", "Hold", "No Sac", "Float", "Ears" };

	public final static int[] wearFlags = { ITEM_TAKE, ITEM_WEAR_FINGER, ITEM_WEAR_NECK, ITEM_WEAR_BODY, ITEM_WEAR_HEAD,
			ITEM_WEAR_LEGS, ITEM_WEAR_FEET, ITEM_WEAR_HANDS, ITEM_WEAR_ARMS, ITEM_WEAR_SHIELD, ITEM_WEAR_ABOUT,
			ITEM_WEAR_WAIST, ITEM_WEAR_WRIST, ITEM_WIELD, ITEM_HOLD, ITEM_NO_SAC, ITEM_WEAR_FLOAT, ITEM_WEAR_EARS };

	public final static int NUM_WEAR = 18;

	public static int wearLookup(int pos) {
		for (int a = 0; a < NUM_WEAR; a++) {
			if (wearFlags[a] == pos)
				return a;
		}

		return -1;
	}

	/*
	 * Item Extra Flags
	 */
	public final static int ITEM_GLOW = 1;
	public final static int ITEM_HUM = 2;
	public final static int ITEM_DARK = 4;
	public final static int ITEM_HIDDEN = 8;
	public final static int ITEM_EVIL = 16;
	public final static int ITEM_INVIS = 32;
	public final static int ITEM_MAGIC = 64;
	public final static int ITEM_NODROP = 128;
	public final static int ITEM_BLESS = 256;
	public final static int ITEM_ANTI_GOOD = 512;
	public final static int ITEM_ANTI_EVIL = 1024;
	public final static int ITEM_ANTI_NEUTRAL = 2048;
	public final static int ITEM_NOREMOVE = 4096;
	public final static int ITEM_INVENTORY = 8192;
	public final static int ITEM_NOPURGE = 16384;
	public final static int ITEM_ROT_DEATH = 32768;
	public final static int ITEM_VIS_DEATH = 65536;
	public final static int ITEM_NONMETAL = 262144;
	public final static int ITEM_NOLOCATE = 524288;
	public final static int ITEM_MELT_DROP = 1048576;
	public final static int ITEM_HAD_TIMER = 2097152;
	public final static int ITEM_SELL_EXTRACT = 4194304;
	public final static int ITEM_COMMUNICATE = 8388608;
	public final static int ITEM_BURN_PROOF = 16777216;
	public final static int ITEM_NOUNCURSE = 33554432;
	public final static int ITEM_QUEST = 67108864;
	public final static int ITEM_GUILD = 134217728;
	public final static int ITEM_BLADE_THIRST = 268435456;
	public final static int ITEM_ARTIFACT = 536870912;

	public final static String[] extraNames = { "Glow", "Hum", "Dark", "Hidden", "Evil", "Invisible", "Magic",
			"No Drop", "Bless", "Anti-Good", "Anti-Evil", "Anti-Neutral", "NoRemove", "Inventory", "NoPurge",
			"Rot Death", "Vis Death", "NonMetal", "No Locate", "Melt Drop", "Sell Extract", "Burn Proof", "No Uncurse",
			"Quest", "Artifact" };

	public final static int[] extraFlags = { ITEM_GLOW, ITEM_HUM, ITEM_DARK, ITEM_HIDDEN, ITEM_EVIL, ITEM_INVIS,
			ITEM_MAGIC, ITEM_NODROP, ITEM_BLESS, ITEM_ANTI_GOOD, ITEM_ANTI_EVIL, ITEM_ANTI_NEUTRAL, ITEM_NOREMOVE,
			ITEM_INVENTORY, ITEM_NOPURGE, ITEM_ROT_DEATH, ITEM_VIS_DEATH, ITEM_NONMETAL, ITEM_NOLOCATE, ITEM_MELT_DROP,
			ITEM_SELL_EXTRACT, ITEM_BURN_PROOF, ITEM_NOUNCURSE, ITEM_QUEST, ITEM_ARTIFACT };

	public final static int NUM_EXTRA = 25;

	// affect flags
	public final static int AFF_BLIND = BIT_A;
	public final static int AFF_INVIS = BIT_B;
	public final static int AFF_INVISIBLE = BIT_B;
	public final static int AFF_DETECT_EVIL = BIT_C;
	public final static int AFF_DETECT_INVIS = BIT_D;
	public final static int AFF_DETECT_MAGIC = BIT_E;
	public final static int AFF_DETECT_HIDDEN = BIT_F;
	public final static int AFF_DETECT_GOOD = BIT_G;
	public final static int AFF_SANCTUARY = BIT_H;
	public final static int AFF_FAERIE_FIRE = BIT_I;
	public final static int AFF_INFRARED = BIT_J;
	public final static int AFF_CURSE = BIT_K;
	public final static int AFF_UNUSED_FLAG = BIT_L;
	public final static int AFF_POISON = BIT_M;
	public final static int AFF_PROTECT_EVIL = BIT_N;
	public final static int AFF_PROTECT_GOOD = BIT_O;
	public final static int AFF_SNEAK = BIT_P;
	public final static int AFF_HIDE = BIT_Q;
	public final static int AFF_SLEEP = BIT_R;
	public final static int AFF_CHARM = BIT_S;
	public final static int AFF_FLYING = BIT_T;
	public final static int AFF_PASS_DOOR = BIT_U;
	public final static int AFF_HASTE = BIT_V;
	public final static int AFF_CALM = BIT_W;
	public final static int AFF_PLAGUE = BIT_X;
	public final static int AFF_WEAKEN = BIT_Y;
	public final static int AFF_DARK_VISION = BIT_Z;
	public final static int AFF_BERSERK = BIT_aa;
	public final static int AFF_SWIM = BIT_bb;
	public final static int AFF_REGENERATION = BIT_cc;
	public final static int AFF_SLOW = BIT_dd;

	public final static String[] affectFlagNames = { "Blind", "Invisible", "Detect Evil", "Detect Invis",
			"Detect Magic", "Detect Hidden", "Detect Good", "Sanctuary", "Faerie Fire", "Infrared", "Curse", "Poison",
			"Protect Evil", "Protect Good", "Sneak", "Hide", "Flying", "Pass Door", "Haste", "Calm", "Plague", "Weaken",
			"Dark Vision", "Berserk", "Swim", "Regeneration", "Slow" };

	public final static int[] affectFlags = { BIT_A, BIT_B, BIT_C, BIT_D, BIT_E, BIT_F, BIT_G, BIT_H, BIT_I, BIT_J,
			BIT_K, BIT_M, BIT_N, BIT_O, BIT_P, BIT_Q, BIT_T, BIT_U, BIT_V, BIT_W, BIT_X, BIT_Y, BIT_Z, BIT_aa, BIT_bb,
			BIT_cc, BIT_dd };

	public final static int NUM_AFFECTS = 27;

	/* IMM/RES/VULN bits for mobs */
	public final static int IRV_SUMMON = BIT_A;
	public final static int IRV_CHARM = BIT_B;
	public final static int IRV_MAGIC = BIT_C;
	public final static int IRV_WEAPON = BIT_D;
	public final static int IRV_BASH = BIT_E;
	public final static int IRV_PIERCE = BIT_F;
	public final static int IRV_SLASH = BIT_G;
	public final static int IRV_FIRE = BIT_H;
	public final static int IRV_COLD = BIT_I;
	public final static int IRV_LIGHTNING = BIT_J;
	public final static int IRV_ACID = BIT_K;
	public final static int IRV_POISON = BIT_L;
	public final static int IRV_NEGATIVE = BIT_M;
	public final static int IRV_HOLY = BIT_N;
	public final static int IRV_ENERGY = BIT_O;
	public final static int IRV_MENTAL = BIT_P;
	public final static int IRV_DISEASE = BIT_Q;
	public final static int IRV_DROWNING = BIT_R;
	public final static int IRV_LIGHT = BIT_S;
	public final static int IRV_SOUND = BIT_T;

	public final static String[] IRVNames = { "Summon", "Charm", "Magic", "Weapon", "Bash", "Pierce", "Slash", "Fire",
			"Cold", "Lightning", "Acid", "Poison", "Negative", "Holy", "Energy", "Mental", "Disease", "Drowning",
			"Light", "Sound" };

	public final static int[] IRVFlags = { BIT_A, BIT_B, BIT_C, BIT_D, BIT_E, BIT_F, BIT_G, BIT_H, BIT_I, BIT_J, BIT_K,
			BIT_L, BIT_M, BIT_N, BIT_O, BIT_P, BIT_Q, BIT_R, BIT_S, BIT_T };

	public final static int NUM_IRV = 20;

	/*
	 * form
	 */
	public final static int FORM_EDIBLE = BIT_A;
	public final static int FORM_POISON = BIT_B;
	public final static int FORM_MAGICAL = BIT_C;
	public final static int FORM_INSTANT_DECAY = BIT_D;
	public final static int FORM_OTHER = BIT_E;
	public final static int FORM_ANIMAL = BIT_G;
	public final static int FORM_SENTIENT = BIT_H;
	public final static int FORM_UNDEAD = BIT_I;
	public final static int FORM_CONSTRUCT = BIT_J;
	public final static int FORM_MIST = BIT_K;
	public final static int FORM_INTANGIBLE = BIT_L;
	public final static int FORM_BIPED = BIT_M;
	public final static int FORM_CENTAUR = BIT_N;
	public final static int FORM_INSECT = BIT_O;
	public final static int FORM_SPIDER = BIT_P;
	public final static int FORM_CRUSTACEAN = BIT_Q;
	public final static int FORM_WORM = BIT_R;
	public final static int FORM_BLOB = BIT_S;
	public final static int FORM_MAMMAL = BIT_V;
	public final static int FORM_BIRD = BIT_W;
	public final static int FORM_REPTILE = BIT_X;
	public final static int FORM_SNAKE = BIT_Y;
	public final static int FORM_DRAGON = BIT_Z;
	public final static int FORM_AMPHIBIAN = BIT_aa;
	public final static int FORM_FISH = BIT_bb;
	public final static int FORM_COLD_BLOOD = BIT_cc;

	public final static String[] formNames = { "Edible", "Poison", "Magical", "Instant Decay", "Other", "Animal",
			"Sentient", "Undead", "Construct", "Mist", "Intangible", "Biped", "Centaur", "Insect", "Spider",
			"Crustacean", "Worm", "Blob", "Mammal", "Bird", "Reptile", "Snake", "Dragon", "Amphibian", "Fish",
			"Cold Blood" };

	public final static int[] formFlags = { BIT_A, BIT_B, BIT_C, BIT_D, BIT_E, BIT_G, BIT_H, BIT_I, BIT_J, BIT_K, BIT_L,
			BIT_M, BIT_N, BIT_O, BIT_P, BIT_Q, BIT_R, BIT_S, BIT_V, BIT_W, BIT_X, BIT_Y, BIT_Z, BIT_aa, BIT_bb,
			BIT_cc };

	public final static int NUM_FORMS = 26;

	/*
	 * body parts for races
	 */
	public final static int PART_HEAD = BIT_A;
	public final static int PART_ARMS = BIT_B;
	public final static int PART_LEGS = BIT_C;
	public final static int PART_HEART = BIT_D;
	public final static int PART_BRAINS = BIT_E;
	public final static int PART_GUTS = BIT_F;
	public final static int PART_HANDS = BIT_G;
	public final static int PART_FEET = BIT_H;
	public final static int PART_FINGERS = BIT_I;
	public final static int PART_EAR = BIT_J;
	public final static int PART_EYE = BIT_K;
	public final static int PART_LONG_TONGUE = BIT_L;
	public final static int PART_EYESTALKS = BIT_M;
	public final static int PART_TENTACLES = BIT_N;
	public final static int PART_FINS = BIT_O;
	public final static int PART_WINGS = BIT_P;
	public final static int PART_TAIL = BIT_Q;
	public final static int PART_CLAWS = BIT_U;
	public final static int PART_FANGS = BIT_V;
	public final static int PART_HORNS = BIT_W;
	public final static int PART_SCALES = BIT_X;
	public final static int PART_TUSKS = BIT_Y;

	public final static String[] partNames = { "Head", "Arms", "Legs", "Heart", "Brains", "Guts", "Hands", "Feet",
			"Fingers", "Ear", "Eye", "Long Tongue", "Eye Stalks", "Tentacles", "Fins", "Wings", "Tail", "Claws",
			"Fangs", "Horns", "Scales", "Tusks" };

	public final static int[] partFlags = { BIT_A, BIT_B, BIT_C, BIT_D, BIT_E, BIT_F, BIT_G, BIT_H, BIT_I, BIT_J, BIT_K,
			BIT_L, BIT_M, BIT_N, BIT_O, BIT_P, BIT_Q, BIT_U, BIT_V, BIT_W, BIT_X, BIT_Y };

	public final static int NUM_PARTS = 22;

	/*
	 * offensive flags
	 */
	public final static int OFF_AREA_ATTACK = BIT_A;
	public final static int OFF_BACKSTAB = BIT_B;
	public final static int OFF_BASH = BIT_C;
	public final static int OFF_BERSERK = BIT_D;
	public final static int OFF_DISARM = BIT_E;
	public final static int OFF_DODGE = BIT_F;
	public final static int OFF_FADE = BIT_G;
	public final static int OFF_FAST = BIT_H;
	public final static int OFF_KICK = BIT_I;
	public final static int OFF_KICK_DIRT = BIT_J;
	public final static int OFF_PARRY = BIT_K;
	public final static int OFF_RESCUE = BIT_L;
	public final static int OFF_TAIL = BIT_M;
	public final static int OFF_TRIP = BIT_N;
	public final static int OFF_CRUSH = BIT_O;

	public final static String[] offenseNames = { "Area Attack", "Backstab", "Bash", "Berserk", "Disarm", "Dodge",
			"Fade", "Fast", "Kick", "Dirt Kick", "Parry", "Rescue", "Tail", "Trip", "Crush" };

	public final static int[] offenseFlags = { BIT_A, BIT_B, BIT_C, BIT_D, BIT_E, BIT_F, BIT_G, BIT_H, BIT_I, BIT_J,
			BIT_K, BIT_L, BIT_M, BIT_N, BIT_O };

	public final static int NUM_OFFENSE = 15;

	// rom constants for container flags
	public final static int CLOSEABLE = BIT_A;
	public final static int PICKPROOF = BIT_B;
	public final static int CLOSED = BIT_C;
	public final static int LOCKED = BIT_D;

	public final static String[] containerFlagNames = { "Closeable", "PickProof", "Closed", "Locked" };

	public final static int[] containerFlags = { CLOSEABLE, PICKPROOF, CLOSED, LOCKED };

	public final static int NUM_CONTAINER_FLAGS = 4;

	public static String containerFlagString(int flag) {
		if (flag == 0)
			return "No Flags";

		String temp = "";

		if ((flag & CLOSEABLE) == CLOSEABLE)
			temp += "Closeable ";
		if ((flag & PICKPROOF) == PICKPROOF)
			temp += "PickProof ";
		if ((flag & CLOSED) == CLOSED)
			temp += "Closed ";
		if ((flag & LOCKED) == LOCKED)
			temp += "Locked";

		return temp;
	}

	// Furniture flags
	public final static int STAND_AT = BIT_A;
	public final static int STAND_ON = BIT_B;
	public final static int STAND_IN = BIT_C;
	public final static int SIT_AT = BIT_D;
	public final static int SIT_ON = BIT_E;
	public final static int SIT_IN = BIT_F;
	public final static int REST_AT = BIT_G;
	public final static int REST_ON = BIT_H;
	public final static int REST_IN = BIT_I;
	public final static int SLEEP_AT = BIT_J;
	public final static int SLEEP_ON = BIT_K;
	public final static int SLEEP_IN = BIT_L;
	public final static int PUT_AT = BIT_M;
	public final static int PUT_ON = BIT_N;
	public final static int PUT_IN = BIT_O;
	public final static int PUT_INSIDE = BIT_P;
	public final static int HAS_SPEAKER = BIT_Q;

	public final static String[] furnitureFlagNames = { "Stand at", "Stand on", "Stand in", "Sit at", "Sit on",
			"Sit in", "Rest at", "Rest on", "Rest in", "Sleep at", "Sleep on", "Sleep in", "Put at", "Put on", "Put in",
			"Put inside", "Has speaker" };

	public final static int[] furnitureFlags = { STAND_AT, STAND_ON, STAND_IN, SIT_AT, SIT_ON, SIT_IN, REST_AT, REST_ON,
			REST_IN, SLEEP_AT, SLEEP_IN, PUT_AT, PUT_ON, PUT_IN, PUT_INSIDE, HAS_SPEAKER };

	public final static int NUM_FURNITURE_FLAGS = 16;

	public static String furnitureFlagName(int flag) {
		String namestring = "No flags";
		boolean found = false;

		if (flag == 0)
			return namestring;

		namestring = "";

		for (int a = 0; a < NUM_FURNITURE_FLAGS; a++) {
			if ((flag & furnitureFlags[a]) == furnitureFlags[a]) {
				if (found)
					namestring += (", " + furnitureFlagNames[a] + " ");
				else {
					namestring += (furnitureFlagNames[a] + " ");
					found = true;
				}
			}
		}

		if (found)
			return namestring;
		else
			return "No flags";
	}

	// rom constants for weapon secondary type
	public final static int WEAPON_FLAMING = BIT_A;
	public final static int WEAPON_FROST = BIT_B;
	public final static int WEAPON_VAMPIRIC = BIT_C;
	public final static int WEAPON_SHARP = BIT_D;
	public final static int WEAPON_VORPAL = BIT_E;
	public final static int WEAPON_TWO_HANDS = BIT_F;
	public final static int WEAPON_SHOCKING = BIT_G;
	public final static int WEAPON_POISON = BIT_H;

	public final static String[] weaponFlagNames = { "flaming", "frost", "vampiric", "sharp", "vorpal", "two hands",
			"shocking", "poison" };

	public final static int[] weaponFlags = { WEAPON_FLAMING, WEAPON_FROST, WEAPON_VAMPIRIC, WEAPON_SHARP,
			WEAPON_VORPAL, WEAPON_TWO_HANDS, WEAPON_SHOCKING, WEAPON_POISON };

	public final static int NUM_WEAPON_FLAGS = 8;

	public static String flagNameString(int flag) {
		String nameString = "";

		if (flag == 0)
			return "None";

		if ((flag & WEAPON_FLAMING) == WEAPON_FLAMING)
			nameString += "Flame ";
		if ((flag & WEAPON_FROST) == WEAPON_FROST)
			nameString += "Frost ";
		if ((flag & WEAPON_VAMPIRIC) == WEAPON_VAMPIRIC)
			nameString += "Vamp ";
		if ((flag & WEAPON_SHARP) == WEAPON_SHARP)
			nameString += "Sharp ";
		if ((flag & WEAPON_VORPAL) == WEAPON_VORPAL)
			nameString += "Vorp ";
		if ((flag & WEAPON_TWO_HANDS) == WEAPON_TWO_HANDS)
			nameString += "Two.H ";
		if ((flag & WEAPON_SHOCKING) == WEAPON_SHOCKING)
			nameString += "Shock ";
		if ((flag & WEAPON_POISON) == WEAPON_POISON)
			nameString += "Poison ";

		return nameString;
	}

	public final static int GATE_NORMAL_EXIT = BIT_A;
	public final static int GATE_NOCURSE = BIT_B;
	public final static int GATE_GOWITH = BIT_C;
	public final static int GATE_BUGGY = BIT_D;
	public final static int GATE_RANDOM = BIT_E;

	public final static String[] portalFlagNames = { "Normal", "Nocurse", "Gowith", "Buggy", "Random" };

	public final static int[] portalFlags = { GATE_NORMAL_EXIT, GATE_NOCURSE, GATE_GOWITH, GATE_BUGGY, GATE_RANDOM };

	public final static int NUM_PORTAL_FLAGS = 5;

	public static String portalFlagString(int flag) {
		String namestring = "No flags";
		boolean found = false;

		if (flag == 0)
			return namestring;

		namestring = "";

		for (int a = 0; a < NUM_PORTAL_FLAGS; a++) {
			if ((flag & portalFlags[a]) == portalFlags[a]) {
				if (found)
					namestring += (", " + portalFlagNames[a]);
				else {
					namestring += (portalFlagNames[a]);
					found = true;
				}
			}
		}

		if (found)
			return namestring;
		else
			return "No flags";
	}

	// rom constants for mprogs
	public final static int TRIG_ACT = BIT_A;
	public final static int TRIG_BRIBE = BIT_B;
	public final static int TRIG_DEATH = BIT_C;
	public final static int TRIG_ENTRY = BIT_D;
	public final static int TRIG_FIGHT = BIT_E;
	public final static int TRIG_GIVE = BIT_F;
	public final static int TRIG_GREET = BIT_G;
	public final static int TRIG_GRALL = BIT_H;
	public final static int TRIG_KILL = BIT_I;
	public final static int TRIG_HPCNT = BIT_J;
	public final static int TRIG_RANDOM = BIT_K;
	public final static int TRIG_SPEECH = BIT_L;
	public final static int TRIG_EXIT = BIT_M;
	public final static int TRIG_EXALL = BIT_N;
	public final static int TRIG_DELAY = BIT_O;
	public final static int TRIG_SURR = BIT_P;
	public final static int TRIG_EXITED = BIT_Q;

	public final static String[] triggerNames = { "act", "bribe", "death", "entry", "fight", "give", "greet", "grall",
			"kill", "hp count", "random", "speech", "exit", "exall", "delay", "surrender", "exited" };

	public final static int[] triggerFlags = { TRIG_ACT, TRIG_BRIBE, TRIG_DEATH, TRIG_ENTRY, TRIG_FIGHT, TRIG_GIVE,
			TRIG_GREET, TRIG_GRALL, TRIG_KILL, TRIG_HPCNT, TRIG_RANDOM, TRIG_SPEECH, TRIG_EXIT, TRIG_EXALL, TRIG_DELAY,
			TRIG_SURR, TRIG_EXITED };

	public final static int NUM_TRIGGERS = 17;

}
