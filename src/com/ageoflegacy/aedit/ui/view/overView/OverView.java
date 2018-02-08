/*
 * George Frick
 * OverView.java
 * Area Editor Project, Spring 2002
 *
 * This class displays the basic data of an model, it also provides
 * means to update this data, and revnum an model.
 */
package com.ageoflegacy.aedit.ui.view.overView;

import com.ageoflegacy.aedit.model.Model;
import com.ageoflegacy.aedit.model.MudConstants;
import com.ageoflegacy.aedit.model.area.Area;
import com.ageoflegacy.aedit.ui.view.EditorView;
import com.ageoflegacy.aedit.ui.FlagChoice;
import com.ageoflegacy.aedit.ui.JMudNumberField;
import com.ageoflegacy.aedit.ui.JMudTextField;
import com.ageoflegacy.aedit.ui.LabeledField;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import net.miginfocom.swing.MigLayout;
import net.miginfocom.layout.CC;

public class OverView extends EditorView implements ActionListener {
	private JTextField fields[];
	private JPanel fieldPanels[];
	private FlagChoice areaFlagChoice;
	protected JButton edit, revnum;
	private String[] fieldTitles = { "File Name", "Area Name", "Builder", "Low Vnum", "High Vnum",
			"Resets in Area", "Exits Out of Area", "Rooms in Area", "Mobs in Area", "Objects in Area",
			"Object:Mobile:Room Ratio" };

	enum field_types {
		FILE_NAME,
		AREA_NAME,
		BUILDER,
		VNUM_LOW,
		VNUM_HIGH,
		NUM_RESETS,
		NUM_EXITS_OUT,
		NUM_ROOMS,
		NUM_MOBS,
		NUM_OBJECTS,
	}
	
	public OverView(Model m) {
		super(m);
		this.setLayout(new MigLayout("fillx"));

		JPanel overviewPanel = new JPanel();
		overviewPanel.setLayout(new MigLayout());

		edit = new JButton("Change Values");
		revnum = new JButton("ReVnum Area");

		overviewPanel.add(edit);
		overviewPanel.add(revnum, "wrap");

		fields = new JTextField[field_types.values().length];
		fieldPanels = new JPanel[field_types.values().length];

		for (int a = 0; a < field_types.values().length; a++) {
			fields[a] = new JTextField(20);
			fields[a].setEditable(false);
			fieldPanels[a] = new LabeledField(fieldTitles[a], fields[a], true);
			if (a % 2 == 1) {
				overviewPanel.add(fieldPanels[a], "wrap");
			} else {
				overviewPanel.add(fieldPanels[a]);
			}
		}

		areaFlagChoice = new FlagChoice("Area Flags", MudConstants.areaFlagNames, MudConstants.areaFlagData,
				MudConstants.areaFlagCount, this);
		overviewPanel.add(areaFlagChoice, "span");

		edit.setEnabled(false);
		revnum.setEnabled(false);
		areaFlagChoice.setEnabled(false);

		edit.addActionListener(new dataUpdate());
		revnum.addActionListener(new revnumUpdate());

		CC componentConstraints = new CC();
		componentConstraints.alignX("center").spanX();
		add(overviewPanel, componentConstraints);
		update();
	}

	// update the display from the data
	public void update() {
		fields[field_types.FILE_NAME.ordinal()].setText(model.getArea().getFileName());
		fields[field_types.AREA_NAME.ordinal()].setText(model.getArea().getAreaName());
		fields[field_types.BUILDER.ordinal()].setText(model.getArea().getBuilder());
		fields[field_types.VNUM_LOW.ordinal()].setText(Integer.toString(model.getArea().getLowVnum()));
		fields[field_types.VNUM_HIGH.ordinal()].setText(Integer.toString(model.getArea().getHighVnum()));
		fields[field_types.NUM_RESETS.ordinal()].setText(Integer.toString(model.getArea().getResetCount()));
		fields[field_types.NUM_EXITS_OUT.ordinal()].setText(model.getArea().getExitFromAreaCount());
		fields[field_types.NUM_ROOMS.ordinal()].setText(Integer.toString(model.getArea().getRoomCount()));
		fields[field_types.NUM_MOBS.ordinal()].setText(Integer.toString(model.getArea().getMobCount()));
		fields[field_types.NUM_OBJECTS.ordinal()].setText(Integer.toString(model.getArea().getObjectCount()));
		areaFlagChoice.setFlags(model.getArea().getFlags());
		if (model.getArea().valid()) {
			edit.setEnabled(true);
			revnum.setEnabled(true);
			areaFlagChoice.setEnabled(true);
		} else {
			edit.setEnabled(false);
			revnum.setEnabled(false);
			areaFlagChoice.setEnabled(false);
		}
	}

	// NOT USED in this class!
	public void update(int v) {
		update();
	}

	// When model flags are adjusted, set to data
	public void actionPerformed(ActionEvent e) {
		model.getArea().setFlags(areaFlagChoice.getFlags());
		update();
	}

	// confirms entry to the model data text fields
	public boolean checkBasicData(JTextField[] fields) {
		int errornum = -1;
		int c = 0, d = 0, lowv = 0, highv = 0;
		if (fields[field_types.FILE_NAME.ordinal()].getText().length() != 0
		 && (fields[field_types.FILE_NAME.ordinal()].getText().length() < 7
		  || !(fields[field_types.FILE_NAME.ordinal()].getText().endsWith(".are")))) {
			JOptionPane.showMessageDialog(null, "File name must be in the form XXX.are!!", "Couldn't Create New Area!",
					JOptionPane.ERROR_MESSAGE);
			System.out.println("Couldn't create new!(b)");
			return false;
		}
		if (fields[field_types.AREA_NAME.ordinal()].getText().length() != 0
		 && fields[field_types.AREA_NAME.ordinal()].getText().length() < 6) {
			JOptionPane.showMessageDialog(null, "Area name must be 6 characters or more!", "Couldn't Create New Area!",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if (fields[field_types.BUILDER.ordinal()].getText().length() != 0
		 && fields[field_types.BUILDER.ordinal()].getText().length() < 4) {
			JOptionPane.showMessageDialog(null, "Builder name must be 4 characters or more!",
					"Couldn't Create New Area!", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		try {
			if (fields[field_types.VNUM_LOW.ordinal()].getText().length() != 0
			 && fields[field_types.VNUM_HIGH.ordinal()].getText().length() != 0) {
				errornum = 2;
				lowv = Integer.parseInt(fields[field_types.VNUM_LOW.ordinal()].getText());
				errornum = 3;
				highv = Integer.parseInt(fields[field_types.VNUM_HIGH.ordinal()].getText());
				if (lowv + 24 >= highv) {
					JOptionPane.showMessageDialog(null, "High vnum must be GREATER than low vnum by at least 25!!",
							"Couldn't Create New Area!", JOptionPane.ERROR_MESSAGE);
					return false;
				}
				if (lowv <= 0 || highv <= 0) {
					JOptionPane.showMessageDialog(null, "High and Low vnum must be greater than 0!!",
							"Couldn't Create New Area!", JOptionPane.ERROR_MESSAGE);
					return false;
				}
				int temp = (highv - lowv) + 1;
				if (temp < model.getArea().getRoomCount() || temp < model.getArea().getObjectCount() || temp < model.getArea().getMobCount()) {
					JOptionPane.showMessageDialog(null,
							"Vnum range must be great enough to hold all objects, mobiles and rooms.", "Too few vnums!",
							JOptionPane.ERROR_MESSAGE);
					return false;
				}
			}
		} catch (Exception exc) {
			switch (errornum) {
			case 2:
				JOptionPane.showMessageDialog(null, "Low vnum must be a NUMBER!!", "Couldn't Create New Area!",
						JOptionPane.ERROR_MESSAGE);
				break;
			case 3:
				JOptionPane.showMessageDialog(null, "High vnum must be a NUMBER!!", "Couldn't Create New Area!",
						JOptionPane.ERROR_MESSAGE);
				break;
			default:
				System.out.println("ACK!");
				break;
			}
			return false;
		}
		return true;
	}

	/**
	 * listens for revnum button, brings up window, then revnums
	 */
	class revnumUpdate implements ActionListener {
		GridBagConstraints constraint;
		GridBagLayout layout;
		JPanel temp;
		JLabel oldLabel, newLabel;
		JTextField oldField, newField;

		public revnumUpdate() {
			oldLabel = new JLabel("Current Starting Vnum");
			newLabel = new JLabel("New Starting Vnum");
			oldField = new JTextField(7);
			newField = new JMudNumberField(7);
			oldField.setEnabled(false);

			temp = new JPanel();
			layout = new GridBagLayout();
			constraint = new GridBagConstraints();
			// temp.setSize(new Dimension(300,200));
			// temp.setPreferredSize(new Dimension(500,300));
			temp.setLayout(layout);
			constraint.insets = new Insets(3, 3, 3, 3);
			constraint.gridwidth = 1;
			constraint.gridheight = 1;
			constraint.gridy = 0;
			constraint.gridx = 0;
			JLabel l1 = new JLabel("Change an Area vnum range");
			l1.setBorder(new BevelBorder(BevelBorder.RAISED));
			layout.setConstraints(l1, constraint);
			temp.add(l1);
			constraint.gridy = 1;
			layout.setConstraints(oldLabel, constraint);
			temp.add(oldLabel);
			constraint.gridy = 2;
			layout.setConstraints(oldField, constraint);
			temp.add(oldField);
			constraint.gridy = 3;
			layout.setConstraints(newLabel, constraint);
			temp.add(newLabel);
			constraint.gridy = 4;
			layout.setConstraints(newField, constraint);
			temp.add(newField);
		}

		public void actionPerformed(ActionEvent e) {
			int choice = -1;
			int errornum = -1;
			Object[] options = { "Revnum", "Cancel" };

			oldField.setText(Integer.toString(model.getArea().getLowVnum()));
			do {
				errornum = -1;
				newField.setText("");
				choice = JOptionPane.showOptionDialog(null, temp, "Revnum an model", JOptionPane.DEFAULT_OPTION,
						JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

				if (choice == 0) {
					if (newField.getText().equals("")) {
						JOptionPane.showMessageDialog(null, "You must provide a new starting vnum.", "Empty field!",
								JOptionPane.ERROR_MESSAGE);
						errornum = 1;
						continue;
					}
					try {
						int temp = Integer.parseInt(newField.getText());
						if (temp <= 0) {
							JOptionPane.showMessageDialog(null, "New starting vnum must be greater >= 1.",
									"new vnum less than 1!", JOptionPane.ERROR_MESSAGE);
							errornum = 1;
							continue;
						}
						model.getArea().reVnum(temp);
						update(temp);
						return;
					} catch (Exception exc) {
						JOptionPane.showMessageDialog(null, "New vnum must be a number.", "Not a number!",
								JOptionPane.ERROR_MESSAGE);
						errornum = 1;
						continue;
					}

				} else // canceled new.
					return;
			} while (choice == 0 && errornum == 1);

			JOptionPane.showMessageDialog(null, "You must revnum mob program text by hand!", "Revnum Warning!",
					JOptionPane.WARNING_MESSAGE);
			update();
		}
	}

	class dataUpdate implements ActionListener {
		JLabel[] labels;
		JTextField[] fields;
		GridBagConstraints constraint;
		GridBagLayout layout;
		JPanel temp;
		private final int NUM_FIELDS = 5;

		public dataUpdate() {
			labels = new JLabel[NUM_FIELDS];
			fields = new JTextField[NUM_FIELDS];
			fields[field_types.FILE_NAME.ordinal()] = new JMudTextField(20);
			fields[field_types.AREA_NAME.ordinal()] = new JMudTextField(20);
			fields[field_types.BUILDER.ordinal()] = new JMudTextField(20);
			fields[field_types.VNUM_LOW.ordinal()] = new JTextField(20);
			fields[field_types.VNUM_HIGH.ordinal()] = new JMudNumberField(20);
			temp = new JPanel();
			layout = new GridBagLayout();
			constraint = new GridBagConstraints();
			temp.setSize(new Dimension(300, 200));
			temp.setPreferredSize(new Dimension(500, 300));
			temp.setLayout(layout);
			constraint.insets = new Insets(3, 3, 3, 3);
			constraint.gridwidth = 2;
			constraint.gridheight = 1;
			constraint.gridy = 0;
			constraint.gridx = 0;
			JLabel l1 = new JLabel("You may adjust the model's header data:");
			layout.setConstraints(l1, constraint);
			temp.add(l1);
			constraint.gridwidth = 1;
			constraint.gridheight = 1;
			labels[field_types.FILE_NAME.ordinal()] = new JLabel("File Name : ", JLabel.RIGHT);
			labels[field_types.AREA_NAME.ordinal()] = new JLabel("Area Name :", JLabel.RIGHT);
			labels[field_types.BUILDER.ordinal()] = new JLabel("Builder   :", JLabel.RIGHT);
			labels[field_types.VNUM_LOW.ordinal()] = new JLabel("Lower Vnum:", JLabel.RIGHT);
			labels[field_types.VNUM_HIGH.ordinal()] = new JLabel("High Vnum :", JLabel.RIGHT);
			for (int a = 0; a < NUM_FIELDS; a++) {
				constraint.gridx = 0;
				constraint.gridy = a + 3;
				layout.setConstraints(labels[a], constraint);
				temp.add(labels[a]);
				constraint.gridx = 1;
				layout.setConstraints(fields[a], constraint);
				temp.add(fields[a]);
			}
			fields[field_types.FILE_NAME.ordinal()].setText(model.getArea().getFileName());
			fields[field_types.AREA_NAME.ordinal()].setText(model.getArea().getAreaName());
			fields[field_types.BUILDER.ordinal()].setText(model.getArea().getBuilder());
			fields[field_types.VNUM_LOW.ordinal()].setEnabled(false);
			fields[field_types.VNUM_LOW.ordinal()].setText("Use REVNUM to change starting vnum.");
			fields[field_types.VNUM_HIGH.ordinal()].setText(Integer.toString(model.getArea().getHighVnum()));
			fields[field_types.VNUM_HIGH.ordinal()].setEnabled(false);
		}

		public void actionPerformed(ActionEvent e) {
			int choice = -1;
			int errornum = -1;
			Object[] options = { "OK", "CANCEL" };

			fields[field_types.FILE_NAME.ordinal()].setText(model.getArea().getFileName());
			fields[field_types.AREA_NAME.ordinal()].setText(model.getArea().getAreaName());
			fields[field_types.BUILDER.ordinal()].setText(model.getArea().getBuilder());
			fields[field_types.VNUM_LOW.ordinal()].setText("Use REVNUM to change starting vnum.");
			fields[field_types.VNUM_HIGH.ordinal()].setText(Integer.toString(model.getArea().getHighVnum()));
			do {
				errornum = -1;
				choice = JOptionPane.showOptionDialog(null, temp, "Basic Area Data", JOptionPane.DEFAULT_OPTION,
						JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
				fields[field_types.VNUM_LOW.ordinal()].setText(Integer.toString(model.getArea().getLowVnum()));
				if (choice == 0) {
					for (int a = 0; a < NUM_FIELDS; a++) {
						if (fields[a].getText().equals("")) {
							JOptionPane.showMessageDialog(null, "-All- fields must be entered!",
									"All fields mus be entered!", JOptionPane.ERROR_MESSAGE);
							errornum = 1;
							break;
						}
					}
				} else // canceled new.
					return;
			} while (choice == 0 && (errornum == 1 || checkBasicData(fields) == false));

			model.getArea().setFileName(fields[field_types.FILE_NAME.ordinal()].getText());
			model.getArea().setAreaName(fields[field_types.AREA_NAME.ordinal()].getText());
			model.getArea().setBuilder(fields[field_types.BUILDER.ordinal()].getText());
			model.getArea().setVnumRange(
				Integer.parseInt(fields[field_types.VNUM_LOW.ordinal()].getText()),
				Integer.parseInt(fields[field_types.VNUM_HIGH.ordinal()].getText()));
			update();

		}
	}
}
