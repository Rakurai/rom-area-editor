// George Frick
// JsonFileFilter.java
// Area Editor Project, Spring 2002

package com.ageoflegacy.aedit.ui;

import java.io.File;

public class JsonFileFilter extends javax.swing.filechooser.FileFilter {

	public static final String DESCRIPTION = "Json Area File (.jsa)";

	public boolean accept(File f) {
		if (f.isDirectory())
			return true;

		String fname = f.getName();
		int periodIndex = fname.lastIndexOf('.');

		boolean accepted = false;

		if (periodIndex > 0 && periodIndex < fname.length() - 1) {
			String ext = fname.substring(periodIndex + 1).toLowerCase();
			if (ext.equals("json"))
				accepted = true;
		}

		return accepted;
	}

	public String getDescription() {
		return DESCRIPTION;
	}
}
