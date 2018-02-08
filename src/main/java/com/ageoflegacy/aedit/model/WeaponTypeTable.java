package com.ageoflegacy.aedit.model;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Vector;

public class WeaponTypeTable {
	ClassLoader loader;
	protected Vector weaponTypes;
	boolean loaded;
	protected FileInputStream inStream;
	protected BufferedReader buf;
	protected boolean open;

	public WeaponTypeTable() {
		loaded = false;
		loader = ClassLoader.getSystemClassLoader();
	}

	public void loadWeaponTypeTable(String file) {
		// File inputFile = new File(file);
		open = false;
		try {
			// inStream = new FileInputStream( inputFile );
			buf = new BufferedReader(new InputStreamReader(loader.getResource(file).openStream()));
			open = true;
			weaponTypes = new Vector();
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

	private void readTable() {
		String temp = getLine();
		while (temp != null && !temp.startsWith("$")) {
			weaponTypes.add(temp);
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

	public boolean isWeaponType(String s) {
		if (s == null)
			return false;

		return weaponTypes.contains(s.toLowerCase());
	}

	public String getDefaultType() {
		return (String) (weaponTypes.elementAt(0));
	}

	public JComboBox getWeaponTypeComboBox() {
		return new JComboBox(weaponTypes);
	}

}
