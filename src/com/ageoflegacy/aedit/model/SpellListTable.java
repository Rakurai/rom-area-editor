/*
 * George Frick, Area Editor project, December 2002.
 */

package com.ageoflegacy.aedit.model;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Vector;

public class SpellListTable {
	ClassLoader loader;
	protected Vector spells;
	boolean loaded;
	protected FileInputStream inStream;
	protected BufferedReader buf;
	protected boolean open;

	public SpellListTable() {
		loaded = false;
		loader = ClassLoader.getSystemClassLoader();
	}

	public void loadSpellListTable(String file) {
		// File inputFile = new File(file);
		open = false;
		try // Load the spell list here...
		{
			// inStream = new FileInputStream( inputFile );
			buf = new BufferedReader(new InputStreamReader(loader.getResource(file).openStream()));
			open = true;
			spells = new Vector();
			readTable();

			loaded = true;
		} catch (FileNotFoundException e) {
			System.out.println("Bad filename!");
			open = false;
			loaded = false;
		} catch (java.io.IOException ex) {
			System.out.println("Bad filename!");
			open = false;
			loaded = false;
		}
	}

	// name pcrace msg act aff aff2 off imm res vuln form parts

	private void readTable() {
		String temp = getLine();
		while (temp != null && !temp.startsWith("$")) {
			spells.add(temp);
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

	public boolean isSpell(String sp) {
		if (sp == null)
			return false;

		return spells.contains(sp.toLowerCase());
	}

	public JComboBox getSpellListComboBox() {
		return new JComboBox(spells);
	}

}
