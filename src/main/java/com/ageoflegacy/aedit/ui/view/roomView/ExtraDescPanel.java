package com.ageoflegacy.aedit.ui.view.roomView;

import com.ageoflegacy.aedit.model.Room;
import com.ageoflegacy.aedit.model.area.Area;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/*
 * George Frick
 */

public class ExtraDescPanel extends JPanel {

	Area area;
	int vnum;
	Room room;

	protected JButton addButton, removeButton;
	protected JList descList;
	protected JTextField keywordField;
	protected JTextArea descArea;

	protected GridBagConstraints constraint;
	protected GridBagLayout layout;
	static final long serialVersionUID = 23423426341L;

	public ExtraDescPanel(int vnum, Area data) {
		area = data;
		this.vnum = vnum;
		constraint = new GridBagConstraints();
		layout = new GridBagLayout();
		this.setLayout(layout);

		room = data.getRoom(vnum);

		if (room == null)
			return;

		addButton = new JButton("Add/Update");
		removeButton = new JButton("Remove");
		keywordField = new JTextField(25);
		keywordField.setBorder(new TitledBorder("Keyword:"));
		descArea = new JTextArea(7, 25);
		descArea.setLineWrap(true);
		descArea.setColumns(19);
		JScrollPane descPane = new JScrollPane(descArea);
		descPane.setBorder(new TitledBorder("Description:"));
		descList = new JList(
				room.getExtraDescriptions().keySet().toArray(new String[room.getExtraDescriptions().keySet().size()]));
		descList.setBorder(new TitledBorder("Current:"));
		descList.setFixedCellWidth(100);

		JPanel bPanel = new JPanel();
		bPanel.setLayout(new GridLayout(1, 2));
		bPanel.add(addButton);
		bPanel.add(removeButton);

		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BorderLayout());
		leftPanel.add(keywordField, BorderLayout.NORTH);
		leftPanel.add(descPane, BorderLayout.CENTER);

		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BorderLayout());
		rightPanel.add(bPanel, BorderLayout.NORTH);
		rightPanel.add(descList, BorderLayout.CENTER);

		setLayout(new GridLayout(1, 2));
		add(leftPanel);
		add(rightPanel);

		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int a = 0;
				boolean update = false;
				for (String s : room.getExtraDescriptions().keySet()) {
					if (s.equalsIgnoreCase(keywordField.getText())) {
						if (room.getExtraDescriptions().get(s).equalsIgnoreCase(descArea.getText())) {
							return; // didn't change anything.
						}
						room.getExtraDescriptions().put(keywordField.getText(), descArea.getText()); // replace
						keywordField.setText("");
						descArea.setText("");
						update = true;
					}
				}
				if (!update) {
					room.getExtraDescriptions().put(keywordField.getText(), descArea.getText());
				}
				descList.setListData(room.getExtraDescriptions().keySet()
						.toArray(new String[room.getExtraDescriptions().keySet().size()]));
			}
		});

		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String temp = (String) descList.getSelectedValue();

				if (temp == null)
					return;

				room.getExtraDescriptions().remove(temp);
				descList.setListData(room.getExtraDescriptions().keySet()
						.toArray(new String[room.getExtraDescriptions().keySet().size()]));
				keywordField.setText("");
				descArea.setText("");
			}
		});

		descList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				String temp = (String) descList.getSelectedValue();

				if (temp == null)
					return;

				keywordField.setText(temp);
				descArea.setText(room.getExtraDescriptions().get(temp));
			}
		});
	}

}
