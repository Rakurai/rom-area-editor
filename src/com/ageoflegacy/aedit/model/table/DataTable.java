package com.ageoflegacy.aedit.model.table;

import java.io.File;
import java.io.IOException;

public abstract class DataTable extends Table {

	public DataTable(File file) throws IOException {
		super(file);
	}

}
