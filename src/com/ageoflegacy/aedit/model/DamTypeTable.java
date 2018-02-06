/*
 * George Frick, Area Editor project, December 2002.
 */
package com.ageoflegacy.aedit.model;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Vector;

public class DamTypeTable {
	ClassLoader loader;
	protected Collection<String> damTypes;
	boolean loaded;
	protected FileInputStream inStream;
	protected BufferedReader buf;
	protected boolean open;
	private static DamTypeTable _instance = null;

	public static DamTypeTable getInstance() {
		if (_instance == null) {
			_instance = new DamTypeTable();
		}
		return _instance;
	}

	private DamTypeTable() {
		loaded = false;
		loader = ClassLoader.getSystemClassLoader();
		loadDamTypeTable("damtype.txt");
	}

	private void loadDamTypeTable(String file) {
		// File inputFile = new File(file);
		open = false;
		try // Load the race table here...
		{
			// inStream = new FileInputStream( inputFile );
			buf = new BufferedReader(new InputStreamReader(loader.getResource(file).openStream()));
			open = true;
			damTypes = new Vector<String>();
			readTable();

			loaded = true;
		} catch (FileNotFoundException e) {
			System.out.println("Bad dam type filename!");
			open = false;
			loaded = false;
		} catch (java.io.IOException ex) {
			System.out.println("no dam type file!");
			open = false;
			loaded = false;
		}
	}

	// name pcrace msg act aff aff2 off imm res vuln form parts

	private void readTable() {
		String temp = getLine();
		while (temp != null && !temp.startsWith("$")) {
			damTypes.add(temp);
			temp = getLine();
		}
	}

	private String getLine() {
		if (!open)
			return null;

		try {
			return buf.readLine();
		} catch (Exception e) {
			System.out.println("Returning null from getLine()");
			return null;
		}
	}

	public boolean isDamType(String s) {
		return s != null && damTypes.contains(s.toLowerCase());

	}

	public String getDefaultType() {
		if (damTypes.isEmpty()) {
			return "default hit";
		} else {
			return damTypes.iterator().next();
		}
	}

	public JComboBox getDamTypeComboBox() {
		return new JComboBox(damTypes.toArray(new String[damTypes.size()]));
	}

}
