package com.ageoflegacy.aedit.ui.view;

import com.ageoflegacy.aedit.model.Area;

import javax.swing.*;
import java.awt.*;

public abstract class EditorView extends JComponent {
	protected GridBagConstraints constraint;
	protected GridBagLayout layout;
	protected Area data;
	protected int vnum; // current item being viewed

	public EditorView(Area d) {
		data = d;
		vnum = -1;
		layout = new GridBagLayout();
		constraint = new GridBagConstraints();
		this.setLayout(layout);
	}

	public void insertAt(int x, int y, int width, int height, Component i) {
		constraint.gridx = x;
		constraint.gridy = y;
		constraint.gridwidth = width;
		constraint.gridheight = height;
		layout.setConstraints(i, constraint);
		this.add(i);
	}

	public void insertAt(int x, int y, int width, int height, Component i, boolean fill) {
		if (fill)
			constraint.fill = GridBagConstraints.HORIZONTAL;

		insertAt(x, y, width, height, i);

		constraint.fill = GridBagConstraints.NONE;
	}

	public void insertAt(int x, int y, int width, int height, Component i, boolean fillw, boolean fillh) {
		if (fillw && !fillh)
			constraint.fill = GridBagConstraints.HORIZONTAL;

		if (fillh && !fillw)
			constraint.fill = GridBagConstraints.VERTICAL;

		if (fillh && fillw)
			constraint.fill = GridBagConstraints.BOTH;

		insertAt(x, y, width, height, i);

		constraint.fill = GridBagConstraints.NONE;
	}

	public void update(int newVnum) {
		vnum = newVnum;
		update();
	}

	/*
	 * These private functions are for quick popup windows...
	 */
	protected static void inform(String msg) {
		JOptionPane.showMessageDialog(null, msg, msg, JOptionPane.WARNING_MESSAGE);
	}

	protected boolean confirm(String msg) {
		int ans;

		ans = JOptionPane.showConfirmDialog(null, msg, "Please Confirm.", JOptionPane.OK_CANCEL_OPTION);

		return ans == JOptionPane.OK_OPTION;
	}

	public abstract void update();
}
