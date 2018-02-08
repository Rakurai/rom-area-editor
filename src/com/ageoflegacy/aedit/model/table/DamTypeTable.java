/*
 * George Frick, Area Editor project, December 2002.
 */
package com.ageoflegacy.aedit.model.table;

import java.io.File;
import java.io.IOException;

public class DamTypeTable extends TypeTable {

	public DamTypeTable(File file) throws IOException {
		super(file);
	}

	@Override
	public TableEntry defaultEntry() {
		return entries.get(0);
	}

	@Override
	protected TableEntry getTableEntry(String[] values) {
		return new DamTypeTableEntry(values);
	}
}
