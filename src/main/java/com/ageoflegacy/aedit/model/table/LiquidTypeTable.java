package com.ageoflegacy.aedit.model.table;

import java.io.IOException;

public class LiquidTypeTable extends TypeTable {
	public LiquidTypeTable(String filename) throws IOException {
		super(filename);
	}

	@Override
	public TableEntry defaultEntry() {
		return entries.get(0);
	}
}
