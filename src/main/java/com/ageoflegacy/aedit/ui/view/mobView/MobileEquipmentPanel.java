package com.ageoflegacy.aedit.ui.view.mobView;

import com.ageoflegacy.aedit.model.area.Area;
import com.ageoflegacy.aedit.model.area.Mobile;
import com.ageoflegacy.aedit.model.area.MudObject;
import com.ageoflegacy.aedit.model.MudConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Collection;

/**
 * George Frick Date: Jan 18, 2009 Time: 2:01:43 PM
 */
public class MobileEquipmentPanel extends JPanel {

	private JComboBox[] wearSlots;
	private WearSlotListener sListener;
	private String[] labels = { "<Light>", "<Head>", "<Neck1>", "<Neck2>", "<Finger1>", "<Finger2>", "<Torso>",
			"<Legs>", "<Feet>", "<Hands>", "<Arms>", "<Shield>", "<Body>", "<Waist>", "<Wrist1>", "<Wrist2>",
			"<Wielded>", "<Held>", "<Surround>", "<Ear1>", "<Ear2>" };
	private Area data;
	private Mobile mobile;

	public MobileEquipmentPanel() {
		this.setLayout(new GridLayout(22, 1));
		wearSlots = new JComboBox[21];
		for (int a = 0; a < 21; a++) {
			wearSlots[a] = new JComboBox();
			wearSlots[a].addItem("##### - " + labels[a]);
			wearSlots[a].setPreferredSize(wearSlots[a].getPreferredSize());
		}

		add(wearSlots[0]);
		add(wearSlots[6]);
		add(wearSlots[3]);
		add(wearSlots[4]);
		add(wearSlots[1]);
		add(wearSlots[2]);
		add(wearSlots[5]);
		add(wearSlots[7]);
		add(wearSlots[8]);
		add(wearSlots[9]);
		add(wearSlots[10]);
		add(wearSlots[11]);
		add(wearSlots[12]);
		add(wearSlots[13]);
		add(wearSlots[14]);
		add(wearSlots[15]);
		add(wearSlots[16]);
		add(wearSlots[17]);
		add(wearSlots[18]);
		add(wearSlots[19]);
		add(wearSlots[20]);

		sListener = new WearSlotListener();
		for (int z = 0; z < 21; z++)
			wearSlots[z].addActionListener(sListener);
	}

	public void setEnabled(boolean value) {
		for (int z = 0; z < 21; z++)
			wearSlots[z].setEnabled(value);
	}

	public void populate(Collection<MudObject> objects) {
		int wear;
		for (int a = 0; a < 21; a++) {
			wearSlots[a].removeAllItems();
			wearSlots[a].addItem("##### - " + labels[a]);
		}

		for (MudObject object : objects) {
			wear = object.getWearFlags();

			// unchoosable if no take flag.
			if ((wear & MudConstants.ITEM_TAKE) != MudConstants.ITEM_TAKE)
				continue;

			if (object.getType() == MudConstants.ITEM_LIGHT)
				wearSlots[0].addItem(object);

			// item is only take.
			if (wear == MudConstants.ITEM_TAKE) {
				continue;
			}

			wear = (wear ^ MudConstants.ITEM_TAKE);

			switch (wear) {
			case MudConstants.ITEM_WEAR_FINGER: {
				wearSlots[1].addItem(object);
				wearSlots[2].addItem(object);
				break;
			}
			case MudConstants.ITEM_WEAR_NECK: {
				wearSlots[3].addItem(object);
				wearSlots[4].addItem(object);
				break;
			}
			case MudConstants.ITEM_WEAR_BODY: {
				wearSlots[5].addItem(object);
				break;
			}
			case MudConstants.ITEM_WEAR_HEAD: {
				wearSlots[6].addItem(object);
				break;
			}
			case MudConstants.ITEM_WEAR_LEGS: {
				wearSlots[7].addItem(object);
				break;
			}
			case MudConstants.ITEM_WEAR_FEET: {
				wearSlots[8].addItem(object);
				break;
			}
			case MudConstants.ITEM_WEAR_HANDS: {
				wearSlots[9].addItem(object);
				break;
			}
			case MudConstants.ITEM_WEAR_ARMS: {
				wearSlots[10].addItem(object);
				break;
			}
			case MudConstants.ITEM_WEAR_SHIELD: {
				wearSlots[11].addItem(object);
				break;
			}
			case MudConstants.ITEM_WEAR_ABOUT: {
				wearSlots[12].addItem(object);
				break;
			}
			case MudConstants.ITEM_WEAR_WAIST: {
				wearSlots[13].addItem(object);
				break;
			}
			case MudConstants.ITEM_WEAR_WRIST: {
				wearSlots[14].addItem(object);
				wearSlots[15].addItem(object);
				break;
			}
			case MudConstants.ITEM_WIELD: {
				wearSlots[16].addItem(object);
				break;
			}
			case MudConstants.ITEM_HOLD: {
				wearSlots[17].addItem(object);
				break;
			}
			case MudConstants.ITEM_WEAR_FLOAT: {
				wearSlots[18].addItem(object);
				break;
			}
			case MudConstants.ITEM_WEAR_EARS: {
				wearSlots[19].addItem(object);
				wearSlots[20].addItem(object);
				break;
			}
			default: {
				System.out.println("Not a valid wear flag: " + wear);
				break;
			}
			}
		}
	}

	class WearSlotListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JComboBox box = (JComboBox) e.getSource();

			MudObject obj = null;

			if (box.getSelectedItem() instanceof MudObject)
				obj = (MudObject) box.getSelectedItem();

			for (int loop = 0; loop < 21; loop++) {
				if (box == wearSlots[loop]) {

					if (mobile == null)
						return;

					if (obj != null)
						mobile.equipMobile(loop, obj.getVnum());
					else
						mobile.equipMobile(loop, -1);
				}
			}
		}
	}

	public void update(Area data, Mobile mob) {
		this.data = data;
		this.mobile = mob;
		for (int a = 0; a < 21; a++)
			wearSlots[a].removeActionListener(sListener);
		populate(data.getObjects());
		setEq(mob);
		for (int a = 0; a < 21; a++)
			wearSlots[a].addActionListener(sListener);
	}

	/*
	 * this function takes the eq equipped to mobile and sets the combo boxes to
	 * that eq.
	 */
	private void setEq(Mobile mob) {

		MudObject obj;
		int eqVnum;

		if (mob == null) {
			return;
		}

		for (int a = 0; a < 21; a++) {
			wearSlots[a].setSelectedIndex(0);

			eqVnum = mob.getEquipment(a);

			if (eqVnum == -1)
				continue;

			obj = data.getObject(eqVnum);

			if (obj == null)
				continue;

			wearSlots[a].setSelectedItem(obj);
		}
	}
}
