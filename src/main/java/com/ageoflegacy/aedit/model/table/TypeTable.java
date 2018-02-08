package com.ageoflegacy.aedit.model.table;

import java.io.File;
import java.io.IOException;

import javax.swing.JComboBox;

public abstract class TypeTable extends Table {

	public TypeTable(String filename) throws IOException {
		super(filename);
	}
	
	public boolean isType(String s) {
		for (TableEntry t : entries)
			if (((TypeTableEntry)t).getName().toLowerCase() == s.toLowerCase())
				return true;
		
		return false;
	}
	
	public String getName(int index) {
		return ((TypeTableEntry)entries.get(index)).getName();
	}

	public JComboBox getComboBox() {
		return new JComboBox(super.getNameList());
	}
}
