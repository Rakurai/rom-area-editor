// George Frick
// MudFileView.java
// Area Editor Project, Spring 2002

package com.ageoflegacy.aedit.ui;

import javax.swing.*;
import java.io.File;

public class MudFileView extends javax.swing.filechooser.FileView {
	ImageIcon areIcon = new ImageIcon("bullet2.gif");

	public String getName(File f) {
		return null;
	}

	public String getDescription(File f) {
		return null;
	}

	public String getTypeDescription(File f) {
		String extension = getExtension(f);
		if (extension != null && extension.equals("are"))
			return "Java File";
		else
			return null;
	}

	public Icon getIcon(File f) {
		String extension = getExtension(f);
		if (extension != null && extension.equals("are"))
			return areIcon;
		else
			return null;
	}

	private String getExtension(File f) {
		String fname = f.getName();
		int p = fname.lastIndexOf('.');
		String ext = null;
		if (p > 0 && p < fname.length() - 1) {
			ext = fname.substring(p + 1).toLowerCase();
		}
		return ext;
	}

}
