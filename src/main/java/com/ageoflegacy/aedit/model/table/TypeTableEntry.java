package com.ageoflegacy.aedit.model.table;

public class TypeTableEntry extends TableEntry {

	private final String name;

	public TypeTableEntry(String[] values) {
		super(values);
		name = values[0];
	}

	public String getName() {
		return name;
	}
}
