package com.ageoflegacy.aedit.ui.view.mobView;

import com.ageoflegacy.aedit.model.MudConstants;
import com.ageoflegacy.aedit.ui.view.EditorView;
import com.ageoflegacy.aedit.model.Area;
import com.ageoflegacy.aedit.model.Mobile;
import com.ageoflegacy.aedit.model.MudObject;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class MobInPanel extends JPanel {

	Area area;
	int vnum;
	JLabel label1;
	JList inventoryList;
	JComboBox availableList;
	JScrollPane inventoryPane;
	JButton addButton, removeButton, clearButton;
	JCheckBox isShop;
	Mobile mob;
	EditorView myParent;

	public MobInPanel(Area data, com.ageoflegacy.aedit.ui.view.EditorView editorView) {
		area = data;
		vnum = -1;

		this.setLayout(new MigLayout());

		myParent = editorView;
		isShop = new JCheckBox("ShopKeeper?");
		inventoryList = new JList();
		inventoryList.setSelectionModel(new ToggleSelectionModel());
		availableList = new JComboBox();
		availableList.setBorder(new TitledBorder("Object to add"));
		inventoryPane = new JScrollPane(inventoryList);
		inventoryPane.setBorder(new TitledBorder("Mobile Inventory"));

		addButton = new JButton("Add Object");
		removeButton = new JButton("Remove Object");
		clearButton = new JButton("Clear objects");
		JPanel panel1 = new JPanel();
		panel1.setLayout(new MigLayout());
		panel1.add(availableList, "wrap, growx, span");
		panel1.add(addButton);
		panel1.add(clearButton);
		panel1.add(removeButton, "wrap");
		panel1.add(isShop, "span");

		this.add(inventoryPane);
		this.add(panel1);

		isShop.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				mob = area.getMobile(vnum);
				if (e.getStateChange() == ItemEvent.DESELECTED) {
					mob.removeShop();
					myParent.update();
				} else if (e.getStateChange() == ItemEvent.SELECTED) {
					if (mob.getShop() == null)
						mob.addShop();

					myParent.update();
				}
			}
		});

		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mob = area.getMobile(vnum);
				mob.getInventory().clear();
				update();
			}
		});

		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mob = area.getMobile(vnum);

				if (mob == null) {
					System.out.println("problem" + Integer.toString(vnum));
					return;
				}

				MudObject myObj = (MudObject) availableList.getSelectedItem();

				if (myObj == null) {
					System.out.println("problem 2");
					return;
				}

				mob.giveToMobile(myObj);
				update(vnum);
			}
		});

		// get highlighted item from Jlist, if non highlighted, do nothing,
		// else remove from mobile inventory and update jlist.
		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MudObject myObj = (MudObject) inventoryList.getSelectedValue();
				if (myObj == null)
					return;

				mob.takeFromMobile(myObj);
				update(vnum);
			}
		});

	}

	private void update() {
		if (area.getObjectCount() > 0)
			setEnabled(true);
		else
			setEnabled(false);

		inventoryList.validate();
		availableList.validate();
		validate();
		repaint();
	}

	public void update(int v) {
		vnum = v;

		if (vnum == -1) {
			update();
			return;
		}

		mob = area.getMobile(vnum);

		if (mob == null) {
			System.out.println("Bad mob in inventory panel");
			return;
		}
		inventoryList.setListData(mob.getInventory().toArray(new MudObject[mob.getInventory().size()]));
		populate();
		if (mob.getShop() != null)
			isShop.setSelected(true);

		update();
	}

	private void populate() {
		MudObject temp;
		int wear = 0;

		availableList.removeAllItems();

		for (int loop = area.getLowVnum(); loop <= area.getHighVnum(); loop++) {
			temp = area.getObject(loop);

			if (temp == null)
				continue;

			wear = temp.getWearFlags();

			// unchoosable if no take flag.
			if ((wear & MudConstants.ITEM_TAKE) != MudConstants.ITEM_TAKE)
				continue;

			availableList.addItem(temp);
		}
	}

	public void setEnabled(boolean enable) {
		addButton.setEnabled(enable);
		removeButton.setEnabled(enable);
		clearButton.setEnabled(enable);
		availableList.setEnabled(enable);
		inventoryList.setEnabled(enable);

		if (vnum == -1 || area.getMobCount() == 0)
			return;

		mob = area.getMobile(vnum);

		if (mob == null) {
			System.out.println("Bad mob in inventory panel");
			return;
		}

		if (mob.getInventory().size() == 0) {
			clearButton.setEnabled(false);
			removeButton.setEnabled(false);
			inventoryList.removeAll(); // insurance.
		}

		if (inventoryList.getSelectedValue() == null) {
			removeButton.setEnabled(false);
		}
	}

	class ToggleSelectionModel extends DefaultListSelectionModel {
		boolean gestureStarted = false;

		public ToggleSelectionModel() {
			setSelectionMode(SINGLE_SELECTION);
		}

		public void setSelectionInterval(int index0, int index1) {
			// int oldIndex = getMinSelectionIndex();

			if (isSelectedIndex(index0) && !gestureStarted)
				super.removeSelectionInterval(index0, index1);
			else
				super.setSelectionInterval(index0, index1);

			gestureStarted = true;

			// int newIndex = getMinSelectionIndex();
			// if (oldIndex != newIndex)
			update();
		}

		public void setValueIsAdjusting(boolean isAdjusting) {
			if (!isAdjusting)
				gestureStarted = false;
		}
	}
}
