/**
 * George Frick
 * NewAreaPanel.java
 * Area Editor Project, spring 2002
 *
 * @author gfrick
 * 12/19/15
 * This is a new area dialog. It returns a new AreaHeader or null.
 */

package com.ageoflegacy.aedit;

import com.ageoflegacy.aedit.model.*;
import com.ageoflegacy.aedit.model.area.AreaHeader;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;

public class NewAreaPanel extends JPanel {
	JLabel[] labels;
	private final int NUM_FIELDS = 5;
	JTextField[] fields;
	private GridBagConstraints constraint;
	private GridBagLayout layout;
	private AreaHeader header;
	private String[] labelStrings = { "File Name", "Area Name", "Builder", "Lower Vnum", "High Vnum" };

	private enum field_types {
			FILE_NAME,
			AREA_NAME,
			BUILDER,
			VNUM_LOWER,
			VNUM_UPPER
	}
	
	public NewAreaPanel() {
		super();
		header = new AreaHeader();
		labels = new JLabel[field_types.values().length];
		fields = new JTextField[field_types.values().length];
		for (int a = 0; a < field_types.values().length; a++) {
			fields[a] = new JTextField(20);
		}

		layout = new GridBagLayout();
		constraint = new GridBagConstraints();
		setSize(new Dimension(300, 200));
		setPreferredSize(new Dimension(500, 300));
		setLayout(layout);
		constraint.insets = new Insets(3, 3, 3, 3);
		constraint.gridwidth = 2;
		constraint.gridheight = 1;
		constraint.gridy = 0;
		constraint.gridx = 0;
		JLabel l = new JLabel("Creating a new area requires a basic amount of information, ");
		JLabel l2 = new JLabel("You can retrieve this information from your head builder or ");
		JLabel l3 = new JLabel("master builder, so ask them about this part if it's confusing.");
		layout.setConstraints(l, constraint);
		add(l);
		constraint.gridy = 1;
		layout.setConstraints(l2, constraint);
		add(l2);
		constraint.gridy = 2;
		layout.setConstraints(l3, constraint);
		add(l3);
		constraint.gridwidth = 1;
		constraint.gridheight = 1;
		for (int a = 0; a < field_types.values().length; a++) {
			labels[a] = new JLabel(labelStrings[a] + ": ", JLabel.RIGHT);
			constraint.gridx = 0;
			constraint.gridy = a + 3;
			layout.setConstraints(labels[a], constraint);
			add(labels[a]);
			constraint.gridx = 1;
			layout.setConstraints(fields[a], constraint);
			add(fields[a]);
		}
	}

	public AreaHeader getNewArea() {
		int choice;
		int errornum;
		Object[] options = { "OK", "CANCEL" };
		do {
			errornum = -1;
			choice = JOptionPane.showOptionDialog(null, this, "Creating a new model", JOptionPane.DEFAULT_OPTION,
					JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
			if (choice == 0) {
				// for now, simple check...
				for (int a = 0; a < field_types.values().length; a++) {
					if (fields[a].getText().equals("")) {
						JOptionPane.showMessageDialog(null, "-All- fields must be entered!",
								"Couldn't Create New Area!", JOptionPane.ERROR_MESSAGE);
						System.out.println("Couldn't create new!");
						errornum = 1;
						break;
					}
				}
			} else {
				// choice == 1
				System.out.println("Cancelled new.");
				return null;
			}
		} while ((errornum == 1 || !checkBasicData(fields)));

		header.setFileName(fields[field_types.FILE_NAME.ordinal()].getText());
		header.setAreaName(fields[field_types.AREA_NAME.ordinal()].getText());
		header.setBuilder(fields[field_types.BUILDER.ordinal()].getText());
		header.setVnumRange(
				Integer.parseInt(fields[field_types.VNUM_LOWER.ordinal()].getText()),
				Integer.parseInt(fields[field_types.VNUM_UPPER.ordinal()].getText()));
		return header;
	}

	private boolean checkBasicData(JTextField[] fields) {
		int errornum = -1;
		int c = 0, d = 0, lowv = 0, highv = 0;
		String error = null;
		if (fields[0].getText().length() != 0
				&& (fields[field_types.FILE_NAME.ordinal()].getText().length() < 7
				 || !(fields[field_types.FILE_NAME.ordinal()].getText().endsWith(".are")))) {
			error = "File name must be in the form XXX.are!!";
		}
		if (fields[field_types.AREA_NAME.ordinal()].getText().length() != 0
		 && fields[field_types.AREA_NAME.ordinal()].getText().length() < 6) {
			error = "Area name must be 6 characters or more!";
		}
		if (fields[field_types.BUILDER.ordinal()].getText().length() != 0
		 && fields[field_types.BUILDER.ordinal()].getText().length() < 4) {
			error = "Builder name must be 4 characters or more!";
		}
		try {
			if (fields[field_types.VNUM_LOWER.ordinal()].getText().length() != 0
			 && fields[field_types.VNUM_UPPER.ordinal()].getText().length() != 0) {
				errornum = 2;
				lowv = Integer.parseInt(fields[field_types.VNUM_LOWER.ordinal()].getText());
				errornum = 3;
				highv = Integer.parseInt(fields[field_types.VNUM_UPPER.ordinal()].getText());
				if (lowv + 24 >= highv) {
					error = "High vnum must be GREATER than low vnum by at least 25!!";
				}
				if (lowv <= 0 || highv <= 0) {
					error = "High and Low vnum must be greater than 0!!";
				}
			}
		} catch (Exception exc) {
			switch (errornum) {
			case 2: {
				error = "Low vnum must be a NUMBER!!";
				break;
			}
			case 3: {
				error = "High vnum must be a NUMBER!!";
				break;
			}
			default: {
				error = "There was an unknown error validating the Vnums and Security.";
				break;
			}
			}
		}
		if (error != null) {
			JOptionPane.showMessageDialog(null, error, "Couldn't Create New Area!", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}

}
