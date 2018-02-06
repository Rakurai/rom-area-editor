package com.ageoflegacy.aedit.model;

public class ObjectAffect {
	private int value;
	private int type;

	/*
	 * affect types
	 */
	public final static int APPLY_NONE = 0;
	public final static int APPLY_STR = 1;
	public final static int APPLY_DEX = 2;
	public final static int APPLY_INT = 3;
	public final static int APPLY_WIS = 4;
	public final static int APPLY_CON = 5;
	public final static int APPLY_SEX = 6;
	public final static int APPLY_CLASS = 7;
	public final static int APPLY_LEVEL = 8;
	public final static int APPLY_AGE = 9;
	public final static int APPLY_HEIGHT = 10;
	public final static int APPLY_WEIGHT = 11;
	public final static int APPLY_MANA = 12;
	public final static int APPLY_HIT = 13;
	public final static int APPLY_MOVE = 14;
	public final static int APPLY_GOLD = 15;
	public final static int APPLY_EXP = 16;
	public final static int APPLY_AC = 17;
	public final static int APPLY_HITROLL = 18;
	public final static int APPLY_DAMROLL = 19;
	public final static int APPLY_SAVES = 20;
	public final static int APPLY_SAVING_PARA = 21;
	public final static int APPLY_SAVING_ROD = 22;
	public final static int APPLY_SAVING_PETRI = 23;
	public final static int APPLY_SAVING_BREATH = 24;
	public final static int APPLY_SAVING_SPELL = 25;
	public final static int NUM_APPLIES = 26;

	public final static String[] affectNames = { "Strength", "Dexterity", "Intelligence", "Wisdom", "Constitution",
			"Mana", "Hit", "Move", "AC", "Hitroll", "Damroll", "Saves" };

	public final static int[] affectFlags = { APPLY_STR, APPLY_DEX, APPLY_INT, APPLY_WIS, APPLY_CON, APPLY_MANA,
			APPLY_HIT, APPLY_MOVE, APPLY_AC, APPLY_HITROLL, APPLY_DAMROLL, APPLY_SAVES };

	public final static int NUM_OBJ_AFFS = 12;

	public ObjectAffect(int nType) {
		type = nType;
	}

	public ObjectAffect(int nType, int nValue) {
		type = nType;
		value = nValue;
	}

	public void setType(int nType) {
		type = nType;
	}

	public void setValue(int nValue) {
		value = nValue;
	}

	public int getType() {
		return type;
	}

	public int getValue() {
		return value;
	}

	public String toString() {
		return stringLookup(type) + "  " + signLookup(value) + value + "";
	}

	public String fileString() {
		return "A\n" + type + " " + value + "\n";
	}

	public int intLookup(String t) {
		return 0;
	}

	public String signLookup(int value) {
		if (value >= 0)
			return "+";

		return "";
	}

	public final static int flagToIndex(int i) {
		for (int a = 0; a < NUM_OBJ_AFFS; a++) {
			if (affectFlags[a] == i)
				return a;
		}

		return 0;
	}

	public String stringLookup(int t) {
		switch (t) {
		case APPLY_NONE: {
			return "none";
		}
		case APPLY_STR: {
			return "Strength";
		}
		case APPLY_DEX: {
			return "Dexterity";
		}
		case APPLY_INT: {
			return "Intelligence";
		}
		case APPLY_WIS: {
			return "Wisdom";
		}
		case APPLY_CON: {
			return "Constitution";
		}
		case APPLY_SEX: {
			return "Sex";
		}
		case APPLY_CLASS: {
			return "Class";
		}
		case APPLY_LEVEL: {
			return "Level";
		}
		case APPLY_AGE: {
			return "Age";
		}
		case APPLY_HEIGHT: {
			return "Height";
		}
		case APPLY_WEIGHT: {
			return "Weight";
		}
		case APPLY_MANA: {
			return "Mana";
		}
		case APPLY_HIT: {
			return "Hit Points";
		}
		case APPLY_MOVE: {
			return "Movement";
		}
		case APPLY_GOLD: {
			return "Gold/Yen";
		}
		case APPLY_EXP: {
			return "Experience";
		}
		case APPLY_AC: {
			return "Armor Class";
		}
		case APPLY_HITROLL: {
			return "Hitroll";
		}
		case APPLY_DAMROLL: {
			return "Damroll";
		}
		case APPLY_SAVES: {
			return "Saves";
		}
		case APPLY_SAVING_PARA: {
			return "Saves-Para";
		}
		case APPLY_SAVING_ROD: {
			return "Saves-Rod";
		}
		case APPLY_SAVING_PETRI: {
			return "Saves-Petrify";
		}
		case APPLY_SAVING_BREATH: {
			return "Saves-Breath";
		}
		case APPLY_SAVING_SPELL: {
			return "Saves-Spell";
		}
		default: {
			return "none";
		}
		}
	}
}
