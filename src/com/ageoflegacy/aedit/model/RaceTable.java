/*
 * George Frick, Area Editor project, December 2002.
 */

/*
* This class stores a vector of races.
* the races are immutable once loaded from file.
* so there are no public set methods.
*/
package com.ageoflegacy.aedit.model;

import com.ageoflegacy.aedit.model.Race;
import com.ageoflegacy.aedit.model.RomIO;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Vector;

public class RaceTable {
	ClassLoader loader;
	protected static Vector raceList;
	static boolean loaded = false;
	protected FileInputStream inStream;
	protected BufferedReader buf;
	protected boolean open;
	static RaceTable rInstance;

	public static RaceTable Instance() {
		if (rInstance == null)
			rInstance = new RaceTable();

		return rInstance;
	}

	private RaceTable() {
		loaded = false;
		loader = ClassLoader.getSystemClassLoader();
		return;
	}

	public void loadRaceTable(String file) {
		// File inputFile = new File(file);
		open = false;
		try // Load the race table here...
		{
			// inStream = new FileInputStream( inputFile );
			buf = new BufferedReader(new InputStreamReader(loader.getResource(file).openStream()));
			open = true;
			raceList = new Vector();
			readRaceTable();

			loaded = true;
		} catch (FileNotFoundException e) {
			System.out.println("Bad filename!");
			open = false;
			loaded = false;
		} catch (java.io.IOException ex) {
			System.out.println("no race file!");
			open = false;
			loaded = false;
		}
		return;
	}

	public static Race defaultRace() {
		if (!loaded) {
			System.out.println("races not loaded yet!");
			return null;
		}
		return (Race) raceList.elementAt(0);
	}

	public static Race getRace(String lookup) {
		if (!loaded) {
			System.out.println("Race lookup before load race");
			return null;
		}
		for (int a = 0; a < raceList.size(); a++) {
			Race r = (Race) raceList.elementAt(a);

			if (r.getName().equalsIgnoreCase(lookup))
				return r;

		}
		System.out.println("couldn't lookup race: " + lookup);
		Race race = new Race(lookup);
		race.setPcRace(false);
		raceList.add(race);
		return race;
	}
	// name pcrace msg act aff aff2 off imm res vuln form parts

	private void readRaceTable() {
		Race newRace;
		String temp = getLine();
		while (temp != null && !temp.startsWith("$")) {
			newRace = new Race(temp.substring(0, temp.indexOf('~')));
			temp = temp.substring(temp.indexOf('~') + 2);

			if (RomIO.oneArgument(temp).startsWith("1"))
				newRace.setPcRace(true);
			else
				newRace.setPcRace(false);
			temp = RomIO.trimArgument(temp);

			newRace.setMovementMessage(RomIO.oneArgument(temp));
			temp = RomIO.trimArgument(temp);

			newRace.setActFlags(Integer.parseInt(RomIO.oneArgument(temp)));
			temp = RomIO.trimArgument(temp);

			newRace.setAffectedByFlags(Integer.parseInt(RomIO.oneArgument(temp)));
			temp = RomIO.trimArgument(temp);

			newRace.setAffectedBy2Flags(Integer.parseInt(RomIO.oneArgument(temp)));
			temp = RomIO.trimArgument(temp);

			newRace.setOffensiveFlags(Integer.parseInt(RomIO.oneArgument(temp)));
			temp = RomIO.trimArgument(temp);

			newRace.setImmunityFlags(Integer.parseInt(RomIO.oneArgument(temp)));
			temp = RomIO.trimArgument(temp);

			newRace.setResistanceFlags(Integer.parseInt(RomIO.oneArgument(temp)));
			temp = RomIO.trimArgument(temp);

			newRace.setVulnerableFlags(Integer.parseInt(RomIO.oneArgument(temp)));
			temp = RomIO.trimArgument(temp);

			newRace.setFormFlags(Integer.parseInt(RomIO.oneArgument(temp)));
			temp = RomIO.trimArgument(temp);

			newRace.setPartsFlags(Integer.parseInt(RomIO.oneArgument(temp)));
			temp = RomIO.trimArgument(temp);

			raceList.add(newRace);
			temp = getLine();
		}
	}

	private boolean isOpen() {
		return open;
	}

	private String getLine() {
		if (open == false)
			return null;

		try {
			return buf.readLine();
		} catch (Exception e) {
			System.out.println("Returning null from getLine()");
			return null;
		}
	}

	public JComboBox getRaceComboBox() {
		return new JComboBox(raceList);
	}

}

// char * name; /* call name of the race */
// bool pc_race; /* can be chosen by pcs */
// char * move; /* movement message! */
// long act; /* act bits for the race */
// long aff; /* aff bits for the race */
// long aff2; /* aff 2 bits for race Tenchi */
// long off; /* off bits for the race */
// long imm; /* imm bits for the race */
// long res; /* res bits for the race */
// long vuln; /* vuln bits for the race */
// long form; /* default form flag for the race */
// long parts; /* default parts for the race */
