package com.ageoflegacy.aedit.model.table;

import java.io.IOException;

public class WeaponTypeTable extends TypeTable {

	public WeaponTypeTable(String filename) throws IOException {
		super(filename);
	}

	@Override
	public TableEntry defaultEntry() {
		return entries.get(0);
	}
}
