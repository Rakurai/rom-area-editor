/*
 * George Frick, Area Editor project, December 2002.
 */

/*
* This class stores a vector of races.
* the races are immutable once loaded from file.
* so there are no public set methods.
*/
package com.ageoflegacy.aedit.model.table;

import javax.swing.*;
import java.io.IOException;

public class RaceDataTable extends DataTable {

	public RaceDataTable(String filename) throws IOException {
		super(filename);
	}

	public RaceTableEntry getRace(String lookup) {
		for (int a = 0; a < entries.size(); a++) {
			RaceTableEntry r = (RaceTableEntry) entries.get(a);

			if (r.getName().equalsIgnoreCase(lookup))
				return r;
		}

		System.out.println("race " + lookup + " not found");
		return defaultEntry();
	}

	public JComboBox getRaceComboBox() {
		return new JComboBox(getNameList());
	}

	@Override
	protected TableEntry getTableEntry(String[] values) {
		return new RaceTableEntry(values);
	}

	@Override
	public RaceTableEntry defaultEntry() {
		return (RaceTableEntry) entries.get(0);
	}

}

// char * name; /* call name of the race */
// bool pc_race; /* can be chosen by pcs */
// char * move; /* movement message! */
// long act; /* act bits for the race */
// long aff; /* aff bits for the race */
// long aff2; /* aff 2 bits for race Tenchi */
// long off; /* off bits for the race */
// long imm; /* imm bits for the race */
// long res; /* res bits for the race */
// long vuln; /* vuln bits for the race */
// long form; /* default form flag for the race */
// long parts; /* default parts for the race */
