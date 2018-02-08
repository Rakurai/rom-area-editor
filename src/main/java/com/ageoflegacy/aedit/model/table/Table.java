package com.ageoflegacy.aedit.model.table;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVReader;

public abstract class Table {
	protected final List<TableEntry> entries;
	
	protected abstract TableEntry getTableEntry(String[] values);
	public abstract TableEntry defaultEntry();

	public Table(String filename) throws IOException {
		entries = new ArrayList<TableEntry>();
		load(filename);
	}
	
	private void load(String filename) throws IOException {
		InputStream is = ClassLoader.getSystemResourceAsStream(filename);
		CSVReader reader = new CSVReader(new InputStreamReader(is));

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
