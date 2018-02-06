package com.ageoflegacy.aedit.model;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Vector;

public class LiquidTypeTable {
	ClassLoader loader;
	protected Vector liquidTypes;
	boolean loaded;
	protected FileInputStream inStream;
	protected BufferedReader buf;
	protected boolean open;

	public LiquidTypeTable() {
		loaded = false;
		loader = ClassLoader.getSystemClassLoader();
	}

	public void loadLiquidTypeTable(String file) {
		// File inputFile = new File(file);
		open = false;
		try {
			// inStream = new FileInputStream( inputFile );
			buf = new BufferedReader(new InputStreamReader(loader.getResource(file).openStream()));
			open = true;
			liquidTypes = new Vector();
			readTable();

			loaded = true;
		} catch (FileNotFoundException e) {
			System.out.println("Bad filename!");
			open = false;
			loaded = false;
		} catch (java.io.IOException ex) {
			System.out.println("no liquid file!");
			open = false;
			loaded = false;
		}
	}

	private void readTable() {
		String temp = getLine();
		while (temp != null && !temp.startsWith("$")) {
			liquidTypes.add(temp);
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

	public boolean isLiquidType(String s) {
		if (s == null)
			return false;

		return liquidTypes.contains(s.toLowerCase());
	}

	public String getDefaultLiquid() {
		return (String) (liquidTypes.elementAt(0));
	}

	public JComboBox getLiquidTypeComboBox() {
		return new JComboBox(liquidTypes);
	}

}
