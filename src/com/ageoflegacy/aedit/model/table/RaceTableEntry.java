package com.ageoflegacy.aedit.model.table;

import com.ageoflegacy.aedit.model.MudConstants;

public class RaceTableEntry extends TableEntry {
	String name;
	boolean isPcRace;
	int actFlags;
	int affectedByFlags;
	int offensiveFlags;
	int immunityFlags;
	int resistanceFlags;
	int vulnerableFlags;
	int formFlags;
	int partsFlags;

	public RaceTableEntry(String[] values) {
		super(values);

		name = values[0];
		isPcRace = Integer.parseInt(values[1]) != 0;
		actFlags = MudConstants.getBitInt(values[2]);
		affectedByFlags = MudConstants.getBitInt(values[3]);
		offensiveFlags = MudConstants.getBitInt(values[4]);
		immunityFlags = MudConstants.getBitInt(values[5]);
		resistanceFlags = MudConstants.getBitInt(values[6]);
		vulnerableFlags = MudConstants.getBitInt(values[7]);
		formFlags = MudConstants.getBitInt(values[8]);
		partsFlags = MudConstants.getBitInt(values[9]);
	}

	public boolean isPcRace() {
		return isPcRace;
	}

	public int getActFlags() {
		return actFlags;
	}

	public int getAffectedByFlags() {
		return affectedByFlags;
	}

	public int getOffensiveFlags() {
		return offensiveFlags;
	}

	public int getImmunityFlags() {
		return immunityFlags;
	}

	public int getResistanceFlags() {
		return resistanceFlags;
	}

	public int getVulnerableFlags() {
		return vulnerableFlags;
	}

	public int getForm() {
		return formFlags;
	}

	public int getParts() {
		return partsFlags;
	}

	public String getName() {
		return name;
	}

	public String toString() {
		return name;
	}

	public void PrintToPrompt() {
		System.out.println("Race: " + name);
		System.out.println("--------------------------");
		System.out.print("Is PC race? ");
		if (isPcRace())
			System.out.println("Yes");
		else
			System.out.println("No");
		System.out.println("Act Flags       : " + Integer.toString(getActFlags()));
		System.out.println("Affected Flags  : " + Integer.toString(getAffectedByFlags()));
		System.out.println("Offensive Flags : " + Integer.toString(getOffensiveFlags()));
		System.out.println("Immunity Flags  : " + Integer.toString(getImmunityFlags()));
		System.out.println("Resistance Flags: " + Integer.toString(getResistanceFlags()));
		System.out.println("Vulnerable Flags: " + Integer.toString(getVulnerableFlags()));
		System.out.println("Form Flags      : " + Integer.toString(getForm()));
		System.out.println("Parts Flags     : " + Integer.toString(getParts()));
	}
}
