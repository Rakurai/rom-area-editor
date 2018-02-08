package com.ageoflegacy.aedit.model.table;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

public abstract class Table {
	protected final List<TableEntry> entries;
	
	protected abstract TableEntry getTableEntry(String[] values);
	public abstract TableEntry defaultEntry();

	public Table(File file) throws IOException {
		entries = new ArrayList<TableEntry>();
		load(file);
	}
	
	private void load(File file) throws IOException {
		CSVReader reader = new CSVReader(new FileReader(file));

		try {
			reader.readNext(); // headers
			
			String[] values;
			while ((values = reader.readNext()) != null)
				entries.add(getTableEntry(values));
		}
		finally {
			reader.close();
		}
	}
		
	public String[] getNameList() {
		String[] ret = new String[entries.size()];
		
		for (int i = 0; i < entries.size(); i++)
			ret[i] = entries.get(i).getName();
		
		return ret;
	}

}
