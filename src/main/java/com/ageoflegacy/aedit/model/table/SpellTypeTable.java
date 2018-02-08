/*
 * George Frick, Area Editor project, December 2002.
 */

package com.ageoflegacy.aedit.model.table;

import java.io.IOException;

public class SpellTypeTable extends TypeTable {
	public SpellTypeTable(String filename) throws IOException {
		super(filename);
	}

	@Override
	public TableEntry defaultEntry() {
		return entries.get(0);
	}
}
