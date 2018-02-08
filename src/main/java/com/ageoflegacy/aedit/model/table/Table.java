package com.ageoflegacy.aedit.model.table;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;

import com.ageoflegacy.aedit.io.ResourceLoader;
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
//		System.out.println("loading " + filename);
//		System.out.println(this.getClass().getClassLoader().getResource(filename));
		InputStream is = ResourceLoader.getInputStream(filename);
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
