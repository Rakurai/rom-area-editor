package com.ageoflegacy.aedit.ui.view.objectView;

import com.ageoflegacy.aedit.ui.view.objectView.ObjectView;
import com.ageoflegacy.aedit.ui.FlagChoice;
import com.ageoflegacy.aedit.model.*;
import com.ageoflegacy.aedit.model.area.Area;
import com.ageoflegacy.aedit.model.area.MudObject;
import com.ageoflegacy.aedit.model.table.DamTypeTable;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;

public class ObjectTypePanel extends JPanel {
	private JComboBox typeBox;
	private JTextField v0, v1, v2, v3, v4;
	private TitledBorder b0, b1, b2, b3, b4;
	private JPanel p0, p1, p2, p3, p4;
	private final static int OFF = 0;
	private final static int ON0 = 1;
	private final static int ON1 = 2;
	private final static int ON2 = 4;
	private final static int ON3 = 8;
	private final static int ON4 = 16;
	private final static int ONALL = ON0 | ON1 | ON2 | ON3 | ON4;
	private final Model model;
	private int vnum;
	DamTypeTable theDamTypes;
	WeaponTypeTable theWeaponTypes;
	LiquidTypeTable theLiquidTypes;
	SpellListTable theSpellList;
	com.ageoflegacy.aedit.ui.view.objectView.ObjectView myParent;

	public ObjectTypePanel(Model m, DamTypeTable _theDamTypes, ObjectView par) {
		this.model = m;
		myParent = par;
		vnum = -1;
		theDamTypes = _theDamTypes;
		theWeaponTypes = new WeaponTypeTable();
		theWeaponTypes.loadWeaponTypeTable("wtype.txt");
		theLiquidTypes = new LiquidTypeTable();
		theLiquidTypes.loadLiquidTypeTable("liquid.txt");
		theSpellList = new SpellListTable();
		theSpellList.loadSpellListTable("spell.txt");
		typeBox = new JComboBox(MudConstants.typeNames);
		typeBox.setBorder(new TitledBorder("Item Type"));
		v0 = new JTextField(10);
		v1 = new JTextField(10);
		v2 = new JTextField(10);
		v3 = new JTextField(10);
		v4 = new JTextField(10);
		p0 = new JPanel();
		p1 = new JPanel();
		p2 = new JPanel();
		p3 = new JPanel();
		p4 = new JPanel();
		p0.setLayout(new GridLayout(1, 1));
		p1.setLayout(new GridLayout(1, 1));
		p2.setLayout(new GridLayout(1, 1));
		p3.setLayout(new GridLayout(1, 1));
		p4.setLayout(new GridLayout(1, 1));
		b0 = new TitledBorder("V0");
		b1 = new TitledBorder("V1");
		b2 = new TitledBorder("V2");
		b3 = new TitledBorder("V3");
		b4 = new TitledBorder("V4");
		p0.setBorder(b0);
		p1.setBorder(b1);
		p2.setBorder(b2);
		p3.setBorder(b3);
		p4.setBorder(b4);
		p0.add(v0);
		p1.add(v1);
		p2.add(v2);
		p3.add(v3);
		p4.add(v4);

		setLayout(new MigLayout());
		add(typeBox, "growx, wrap");
		add(p0, "wrap");
		add(p1, "wrap");
		add(p2, "wrap");
		add(p3, "wrap");
		add(p4);

		update();

		typeBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (vnum != -1) {
					MudObject me = model.getArea().getObject(vnum);
					me.setType(MudConstants.TypeLookup(typeBox.getSelectedIndex()));
				}
				myParent.update();
			}
		});

		v0.addFocusListener(new v0FocusListener(theWeaponTypes));
		v1.addFocusListener(new v1FocusListener());
		v2.addFocusListener(new v2FocusListener(model.getArea()));
		v3.addFocusListener(new v3FocusListener(theDamTypes, model.getArea()));
		v4.addFocusListener(new v4FocusListener());
	}

	public void setEnabled(boolean value) {
		typeBox.setEnabled(value);
		v0.setEnabled(value);
		v1.setEnabled(value);
		v2.setEnabled(value);
		v3.setEnabled(value);
		v4.setEnabled(value);
	}

	public void update(int v) {
		vnum = v;

		if (vnum != -1) {
			MudObject temp = model.getArea().getObject(vnum);
			typeBox.setSelectedIndex(MudConstants.typePositionLookup(temp.getType()));
		}

		update();
	}

	public void update() {
		int which = MudConstants.TypeLookup(typeBox.getSelectedIndex());

		MudObject temp = null;

		if (vnum == -1)
			which = MudConstants.ITEM_TRASH;
		else
			temp = model.getArea().getObject(vnum);

		switch (which) {
		case MudConstants.ITEM_LIGHT: {
			turnOn(ON2);
			b2.setTitle("Hours");
			if (temp == null)
				break;

			int h = temp.getiValue(2);
			if (h == 0)
				h = 1;
			v2.setText(Integer.toString(h));
			break;
		}
		case MudConstants.ITEM_WAND:
		case MudConstants.ITEM_STAFF: {
			turnOn((ON0 | ON1 | ON2 | ON3));
			b0.setTitle("Casting Level");
			b1.setTitle("Charges Total");
			b2.setTitle("Charges Left");
			b3.setTitle("Spell");
			if (temp == null)
				break;

			v0.setText(Integer.toString(temp.getiValue(0, 1)));
			v1.setText(Integer.toString(temp.getiValue(1, 1)));
			v2.setText(Integer.toString(temp.getiValue(2, 1)));
			// TODO: make sure v3 is valid spell.
			v3.setText(temp.getsValue(3));
			break;
		}
		case MudConstants.ITEM_WEAPON: {
			turnOn(ONALL);
			b0.setTitle("Weapon Class");
			b1.setTitle("Number Of Dice");
			b2.setTitle("Type Of Dice");
			b3.setTitle("Type");
			b4.setTitle("Special Type");
			if (temp == null)
				break;

			// TODO: make sure v0 is valid weapon class.
			String st = temp.getsValue(0);
			if (!theWeaponTypes.isWeaponType(st))
				temp.setsValue(0, theWeaponTypes.getDefaultType());
			v0.setText(temp.getsValue(0));
			v1.setText(Integer.toString(temp.getiValue(1, 1)));
			v2.setText(Integer.toString(temp.getiValue(2, 1)));
			st = temp.getsValue(3);
			if (!theDamTypes.isType(st))
				temp.setsValue(3, theDamTypes.defaultEntry().getName());
			v3.setText(temp.getsValue(3));
			v4.setText(MudConstants.flagNameString(temp.getiValue(4)));
			break;
		}
		case MudConstants.ITEM_ARMOR: {
			turnOn(ON0 | ON1 | ON2 | ON3);
			b0.setTitle("AC Pierce");
			b1.setTitle("AC Bash");
			b2.setTitle("AC Slash");
			b3.setTitle("AC Exotic");
			if (temp == null)
				break;
			v0.setText(Integer.toString(temp.getiValue(0)));
			v1.setText(Integer.toString(temp.getiValue(1)));
			v2.setText(Integer.toString(temp.getiValue(2)));
			v3.setText(Integer.toString(temp.getiValue(3)));
			break;
		}
		case MudConstants.ITEM_FURNITURE: {
			turnOn(ONALL);
			b0.setTitle("Max People");
			b1.setTitle("Channel");
			b2.setTitle("Furniture Flags");
			b3.setTitle("Heal Bonus");
			b4.setTitle("Mana Bonus");
			if (temp == null)
				break;
			v0.setText(Integer.toString(temp.getiValue(0, 1)));
			v1.setText(Integer.toString(temp.getiValue(1)));
			v2.setText(MudConstants.furnitureFlagName(temp.getiValue(2)));
			v3.setText(Integer.toString(temp.getiValue(3)));
			v4.setText(Integer.toString(temp.getiValue(4)));
			break;
		}
		case MudConstants.ITEM_CONTAINER: {
			turnOn(ON0 | ON1 | ON2 | ON4);
			b0.setTitle("Max # of items");
			b1.setTitle("Flags");
			b2.setTitle("Key");
			b4.setTitle("Weight Multiplier");
			if (temp == null)
				break;
			v0.setText(Integer.toString(temp.getiValue(0, 1)));
			v1.setText(MudConstants.containerFlagString(temp.getiValue(1)));
			if (temp.getiValue(2) != 0) {
				MudObject obj = model.getArea().getObject(temp.getiValue(2));
				if (obj == null) {
					v2.setText("No key");
					temp.setiValue(2, 0);
				} else
					v2.setText(obj.toString());
			} else
				v2.setText("No Key");
			v4.setText(Integer.toString(temp.getiValue(4, 1)));
			break;
		}
		case MudConstants.ITEM_FOUNTAIN:
		case MudConstants.ITEM_DRINK_CON: {
			turnOn(ON0 | ON1 | ON2 | ON3);
			b0.setTitle("Liquid Total");
			b1.setTitle("Liquid Left");
			b2.setTitle("Liquid");
			b3.setTitle("Poisoned?");
			if (temp == null)
				break;
			v0.setText(Integer.toString(temp.getiValue(0, 1)));
			v1.setText(Integer.toString(temp.getiValue(1, 1)));
			String st = temp.getsValue(2);
			if (!theLiquidTypes.isLiquidType(st))
				temp.setsValue(2, theLiquidTypes.getDefaultLiquid());
			v2.setText(temp.getsValue(2));
			if (temp.getiValue(3) == 0)
				v3.setText("No");
			else
				v3.setText("Yes");
			break;
		}
		case MudConstants.ITEM_FOOD: {
			turnOn(ON0 | ON1 | ON3);
			b0.setTitle("Food Hours");
			b1.setTitle("Full Hours");
			b3.setTitle("Poisoned?");
			if (temp == null)
				break;
			v0.setText(Integer.toString(temp.getiValue(0, 1)));
			v1.setText(Integer.toString(temp.getiValue(1, 1)));
			if (temp.getiValue(3) == 0)
				v3.setText("No");
			else
				v3.setText("Yes");
			break;
		}
		case MudConstants.ITEM_MONEY: {
			turnOn(ON0);
			b0.setTitle("Amount of Yen/Gold");
			if (temp == null)
				break;
			v0.setText(Integer.toString(temp.getiValue(0, 1)));
			break;
		}
		case MudConstants.ITEM_SCROLL:
		case MudConstants.ITEM_POTION:
		case MudConstants.ITEM_PILL: {
			turnOn(ONALL);
			b0.setTitle("Cast Level");
			b1.setTitle("Spell: ");
			b2.setTitle("Spell: ");
			b3.setTitle("Spell: ");
			b4.setTitle("Spell: ");
			if (temp == null)
				break;
			v0.setText(Integer.toString(temp.getiValue(0, 1)));
			// TODO: make sure are valid spells.
			String st = temp.getsValue(1);
			for (int spc = 1; spc < 5; spc++) {
				st = temp.getsValue(spc);
				if (!theSpellList.isSpell(st))
					temp.setsValue(spc, "");
			}

			v1.setText(temp.getsValue(1));
			v2.setText(temp.getsValue(2));
			v3.setText(temp.getsValue(3));
			v4.setText(temp.getsValue(4));
			break;
		}
		case MudConstants.ITEM_PORTAL: {
			turnOn(ON0 | ON1 | ON2 | ON3);
			b0.setTitle("Charges");
			b1.setTitle("Exit Flags");
			b2.setTitle("Portal Flags");
			b3.setTitle("Goes to(vnum)");
			if (temp == null)
				break;
			v0.setText(Integer.toString(temp.getiValue(0, 1)));
			v1.setText(MudConstants.exitFlagString(temp.getiValue(1)));
			v2.setText(MudConstants.portalFlagString(temp.getiValue(2)));
			v3.setText(Integer.toString(temp.getiValue(3)));
			break;
		}
		case MudConstants.ITEM_TREASURE:
		case MudConstants.ITEM_CLOTHING:
		case MudConstants.ITEM_TRASH:
		case MudConstants.ITEM_KEY:
		case MudConstants.ITEM_BOAT:
		case MudConstants.ITEM_MAP:
		case MudConstants.ITEM_WARP_STONE:
		case MudConstants.ITEM_GEM:
		case MudConstants.ITEM_JEWELRY:
		case MudConstants.ITEM_JUKEBOX:
		case MudConstants.ITEM_MATERIAL: {
			turnOn(OFF);
			break;
		}

		} // switch

		p0.setBorder(b0);
		p1.setBorder(b1);
		p2.setBorder(b2);
		p3.setBorder(b3);
		p4.setBorder(b4);
		validate();
		repaint();

	} // function

	public void turnOn(int which) {
		v0.setEnabled(false);
		v1.setEnabled(false);
		v2.setEnabled(false);
		v3.setEnabled(false);
		v4.setEnabled(false);
		b0.setTitle("V0");
		b1.setTitle("V1");
		b2.setTitle("V2");
		b3.setTitle("V3");
		b4.setTitle("V4");
		v0.setText("unused");
		v1.setText("unused");
		v2.setText("unused");
		v3.setText("unused");
		v4.setText("unused");

		if ((which & ON0) == ON0) {
			v0.setEnabled(true);
			v0.setText("");
		}
		if ((which & ON1) == ON1) {
			v1.setEnabled(true);
			v1.setText("");
		}
		if ((which & ON2) == ON2) {
			v2.setEnabled(true);
			v2.setText("");
		}
		if ((which & ON3) == ON3) {
			v3.setEnabled(true);
			v3.setText("");
		}
		if ((which & ON4) == ON4) {
			v4.setEnabled(true);
			v4.setText("");
		}
	}

	class v0FocusListener implements FocusListener {
		protected WeaponTypeTable theWeaponTypes;

		public v0FocusListener(WeaponTypeTable wTable) {
			super();
			theWeaponTypes = wTable;
		}

		public void focusGained(FocusEvent e) {

			int which = MudConstants.TypeLookup(typeBox.getSelectedIndex());

			if (which == MudConstants.ITEM_WEAPON) {
				MudObject temp = (MudObject) model.getArea().getObject(vnum);
				JComboBox options = theWeaponTypes.getWeaponTypeComboBox();
				options.setSelectedItem(temp.getsValue(0));

				int dec = JOptionPane.showConfirmDialog(null, options, "Choose weapon type:",
						JOptionPane.OK_CANCEL_OPTION);

				if (dec == JOptionPane.CANCEL_OPTION)
					options.setSelectedItem(temp.getsValue(0));

				temp.setsValue(0, (String) options.getSelectedItem());
				vnum = temp.vnum;
				update();
			}
		}

		public void focusLost(FocusEvent e) {
			int type = MudConstants.TypeLookup(typeBox.getSelectedIndex());

			JTextField field = (JTextField) e.getSource();

			try {
				if (vnum == -1)
					return;

				MudObject temp = model.getArea().getObject(vnum);
				switch (type) {
				case MudConstants.ITEM_WAND: // all int types
				case MudConstants.ITEM_STAFF:
				case MudConstants.ITEM_ARMOR:
				case MudConstants.ITEM_FURNITURE:
				case MudConstants.ITEM_FOUNTAIN:
				case MudConstants.ITEM_DRINK_CON:
				case MudConstants.ITEM_FOOD:
				case MudConstants.ITEM_MONEY:
				case MudConstants.ITEM_SCROLL:
				case MudConstants.ITEM_POTION:
				case MudConstants.ITEM_PILL:
				case MudConstants.ITEM_PORTAL:
				case MudConstants.ITEM_CONTAINER: {
					temp.setiValue(0, Integer.parseInt(field.getText()));
					break;
				}
				default:
					break;
				}
			} catch (Exception exp) {
				System.out.println("Error setting value 0.");
			} finally {
				update();
			}
		}
	}

	class v1FocusListener implements FocusListener {
		public void focusGained(FocusEvent e) {
			int which = MudConstants.TypeLookup(typeBox.getSelectedIndex());

			if (which == MudConstants.ITEM_CONTAINER) {
				MudObject temp = (MudObject) model.getArea().getObject(vnum);
				FlagChoice choice = new FlagChoice("container flags", MudConstants.containerFlagNames,
						MudConstants.containerFlags, MudConstants.NUM_CONTAINER_FLAGS, null);
				choice.setFlags(temp.getiValue(1));

				int dec = JOptionPane.showConfirmDialog(null, choice, "Set container flags:",
						JOptionPane.OK_CANCEL_OPTION);

				if (dec == JOptionPane.CANCEL_OPTION)
					choice.setFlags(temp.getiValue(1));

				temp.setiValue(1, choice.getFlags());
				vnum = temp.vnum;
				update();
			} else if (which == MudConstants.ITEM_SCROLL || which == MudConstants.ITEM_POTION
					|| which == MudConstants.ITEM_PILL) {
				MudObject temp = (MudObject) model.getArea().getObject(vnum);
				JComboBox options = theSpellList.getSpellListComboBox();
				options.setSelectedItem(temp.getsValue(1));

				int dec = JOptionPane.showConfirmDialog(null, options, "Choose spell:", JOptionPane.OK_CANCEL_OPTION);

				if (dec == JOptionPane.CANCEL_OPTION)
					options.setSelectedItem(temp.getsValue(1));

				if (options.getSelectedIndex() != 0)
					temp.setsValue(1, (String) options.getSelectedItem());
				else
					temp.setsValue(1, "");

				vnum = temp.vnum;
				update();
			} else if (which == MudConstants.ITEM_PORTAL) {
				MudObject temp = (MudObject) model.getArea().getObject(vnum);
				FlagChoice choice = new FlagChoice("exit flags", MudConstants.exitNames, MudConstants.exitFlags,
						MudConstants.NUM_EXIT_FLAGS, null);
				choice.setFlags(temp.getiValue(1));

				int dec = JOptionPane.showConfirmDialog(null, choice, "Set exit flags:", JOptionPane.OK_CANCEL_OPTION);

				if (dec == JOptionPane.CANCEL_OPTION)
					choice.setFlags(temp.getiValue(1));

				temp.setiValue(1, choice.getFlags());
				vnum = temp.vnum;
				update();
			}
		}

		public void focusLost(FocusEvent e) {
			int type = MudConstants.TypeLookup(typeBox.getSelectedIndex());

			JTextField field = (JTextField) e.getSource();

			try {
				if (vnum == -1)
					return;

				MudObject temp = model.getArea().getObject(vnum);
				switch (type) {
				case MudConstants.ITEM_WAND: // all int types
				case MudConstants.ITEM_STAFF:
				case MudConstants.ITEM_WEAPON:
				case MudConstants.ITEM_ARMOR:
				case MudConstants.ITEM_FURNITURE:
				case MudConstants.ITEM_FOUNTAIN:
				case MudConstants.ITEM_DRINK_CON:
				case MudConstants.ITEM_FOOD: {
					temp.setiValue(1, Integer.parseInt(field.getText()));
					break;
				}
				case MudConstants.ITEM_SCROLL:
				case MudConstants.ITEM_POTION:
				case MudConstants.ITEM_PILL: // all string types
				{
					temp.setsValue(1, field.getText());
					break;
				}
				default:
					break;
				}
				System.out.println("Set the value..");
			} catch (Exception exp) {
				System.out.println("Error setting value 1.");
			} finally {
				update();
			}
		}
	}

	class v2FocusListener implements FocusListener {
		Area data;

		public v2FocusListener(Area d) {
			data = d;
		}

		public void focusGained(FocusEvent e) {
			int which = MudConstants.TypeLookup(typeBox.getSelectedIndex());

			if (which == MudConstants.ITEM_CONTAINER) {
				MudObject temp = (MudObject) model.getArea().getObject(vnum);
				JComboBox options = model.getArea().getVnumCombo("object");
				MudObject t2 = (MudObject) model.getArea().getObject(temp.getiValue(2));
				if (t2 != null)
					options.setSelectedItem(t2);

				int dec = JOptionPane.showConfirmDialog(null, options, "Choose container key:",
						JOptionPane.OK_CANCEL_OPTION);

				if (dec == JOptionPane.CANCEL_OPTION)
					options.setSelectedItem(t2);

				t2 = (MudObject) options.getSelectedItem();
				if (t2 == null)
					temp.setiValue(2, 0);
				else
					temp.setiValue(2, t2.getVnum());

				vnum = temp.vnum;
				update();
			} else if (which == MudConstants.ITEM_SCROLL || which == MudConstants.ITEM_POTION
					|| which == MudConstants.ITEM_PILL) {
				MudObject temp = (MudObject) model.getArea().getObject(vnum);
				JComboBox options = theSpellList.getSpellListComboBox();
				options.setSelectedItem(temp.getsValue(2));

				int dec = JOptionPane.showConfirmDialog(null, options, "Choose spell:", JOptionPane.OK_CANCEL_OPTION);

				if (dec == JOptionPane.CANCEL_OPTION)
					options.setSelectedItem(temp.getsValue(2));

				if (options.getSelectedIndex() != 0)
					temp.setsValue(2, (String) options.getSelectedItem());
				else
					temp.setsValue(2, "");

				vnum = temp.vnum;
				update();
			} else if (which == MudConstants.ITEM_FURNITURE) {
				MudObject temp = (MudObject) model.getArea().getObject(vnum);
				FlagChoice choice = new FlagChoice("furniture flags", MudConstants.furnitureFlagNames,
						MudConstants.furnitureFlags, MudConstants.NUM_FURNITURE_FLAGS, null);
				choice.setFlags(temp.getiValue(2));

				int dec = JOptionPane.showConfirmDialog(null, choice, "Set furniture flags:",
						JOptionPane.OK_CANCEL_OPTION);

				if (dec == JOptionPane.CANCEL_OPTION)
					choice.setFlags(temp.getiValue(2));

				temp.setiValue(2, choice.getFlags());
				vnum = temp.vnum;
				update();
			} else if (which == MudConstants.ITEM_PORTAL) {
				MudObject temp = (MudObject) model.getArea().getObject(vnum);
				FlagChoice choice = new FlagChoice("portal flags", MudConstants.portalFlagNames,
						MudConstants.portalFlags, MudConstants.NUM_PORTAL_FLAGS, null);
				choice.setFlags(temp.getiValue(2));

				int dec = JOptionPane.showConfirmDialog(null, choice, "Set portal flags:",
						JOptionPane.OK_CANCEL_OPTION);

				if (dec == JOptionPane.CANCEL_OPTION)
					choice.setFlags(temp.getiValue(2));

				temp.setiValue(2, choice.getFlags());
				vnum = temp.vnum;
				update();
			} else if (which == MudConstants.ITEM_DRINK_CON || which == MudConstants.ITEM_FOUNTAIN) {
				MudObject temp = (MudObject) model.getArea().getObject(vnum);
				JComboBox options = theLiquidTypes.getLiquidTypeComboBox();
				options.setSelectedItem(temp.getsValue(2));

				int dec = JOptionPane.showConfirmDialog(null, options, "Choose liquid:", JOptionPane.OK_CANCEL_OPTION);

				if (dec == JOptionPane.CANCEL_OPTION)
					options.setSelectedItem(temp.getsValue(2));

				temp.setsValue(2, (String) options.getSelectedItem());
				vnum = temp.vnum;
				update();
			}
		}

		public void focusLost(FocusEvent e) {
			int type = MudConstants.TypeLookup(typeBox.getSelectedIndex());

			JTextField field = (JTextField) e.getSource();

			try {
				if (vnum == -1)
					return;

				MudObject temp = model.getArea().getObject(vnum);
				switch (type) {
				case MudConstants.ITEM_LIGHT:
				case MudConstants.ITEM_WAND: // all int types
				case MudConstants.ITEM_STAFF:
				case MudConstants.ITEM_WEAPON:
				case MudConstants.ITEM_ARMOR: {
					temp.setiValue(2, Integer.parseInt(field.getText()));
					break;
				}
				case MudConstants.ITEM_SCROLL:
				case MudConstants.ITEM_POTION:
				case MudConstants.ITEM_PILL: // all string types
				{
					temp.setsValue(2, field.getText());
					break;
				}
				default:
					break;
				}
				System.out.println("Set the value..");
			} catch (Exception exp) {
				System.out.println("Error setting value 2.");
			} finally {
				update();
			}
		}
	}

	class v3FocusListener implements FocusListener {
		protected DamTypeTable theDamTypes;
		protected Area data;

		public v3FocusListener(DamTypeTable dTable, Area a) {
			super();
			theDamTypes = dTable;
			data = a;
		}

		public void focusGained(FocusEvent e) {
			int which = MudConstants.TypeLookup(typeBox.getSelectedIndex());

			if (which == MudConstants.ITEM_WEAPON) {
				MudObject temp = (MudObject) model.getArea().getObject(vnum);
				JComboBox options = theDamTypes.getComboBox();
				options.setSelectedItem(temp.getsValue(3));

				int dec = JOptionPane.showConfirmDialog(null, options, "Choose damage type:",
						JOptionPane.OK_CANCEL_OPTION);

				if (dec == JOptionPane.CANCEL_OPTION)
					options.setSelectedItem(temp.getsValue(3));

				temp.setsValue(3, (String) options.getSelectedItem());
				vnum = temp.vnum;
				update();
			} else if (which == MudConstants.ITEM_SCROLL || which == MudConstants.ITEM_POTION
					|| which == MudConstants.ITEM_PILL || which == MudConstants.ITEM_WAND
					|| which == MudConstants.ITEM_STAFF) {
				MudObject temp = (MudObject) model.getArea().getObject(vnum);
				JComboBox options = theSpellList.getSpellListComboBox();
				options.setSelectedItem(temp.getsValue(3));

				int dec = JOptionPane.showConfirmDialog(null, options, "Choose spell:", JOptionPane.OK_CANCEL_OPTION);

				if (dec == JOptionPane.CANCEL_OPTION)
					options.setSelectedItem(temp.getsValue(3));

				if (options.getSelectedIndex() != 0)
					temp.setsValue(3, (String) options.getSelectedItem());
				else
					temp.setsValue(3, "");

				vnum = temp.vnum;
				update();
			} else if (which == MudConstants.ITEM_FOOD || which == MudConstants.ITEM_DRINK_CON
					|| which == MudConstants.ITEM_FOUNTAIN) {
				MudObject temp = (MudObject) model.getArea().getObject(vnum);
				String[] yesno = { "No", "Yes" };
				JComboBox options = new JComboBox(yesno);
				options.setSelectedIndex(temp.getiValue(3));

				int dec = JOptionPane.showConfirmDialog(null, options, "Is this liquid poisoned?",
						JOptionPane.OK_CANCEL_OPTION);

				if (dec == JOptionPane.CANCEL_OPTION)
					options.setSelectedIndex(temp.getiValue(3));

				temp.setiValue(3, options.getSelectedIndex());
				vnum = temp.vnum;
				update();
			} else if (which == MudConstants.ITEM_PORTAL) {
				MudObject temp = (MudObject) model.getArea().getObject(vnum);
				JComboBox options = model.getArea().getVnumCombo("rooms");
				Room t2 = (Room) model.getArea().getRoom(temp.getiValue(3));
				if (t2 == null)
					options.setSelectedIndex(0);
				else
					options.setSelectedItem(t2);

				int dec = JOptionPane.showConfirmDialog(null, options, "Choose room portal leads to:",
						JOptionPane.OK_CANCEL_OPTION);

				if (dec == JOptionPane.CANCEL_OPTION)
					options.setSelectedItem(t2);

				t2 = (Room) options.getSelectedItem();
				if (t2 != null)
					temp.setiValue(3, t2.getVnum());
				else
					temp.setiValue(3, 0);

				vnum = temp.vnum;
				update();
			}
		}

		public void focusLost(FocusEvent e) {
			int type = MudConstants.TypeLookup(typeBox.getSelectedIndex());

			JTextField field = (JTextField) e.getSource();

			try {
				if (vnum == -1)
					return;

				MudObject temp = model.getArea().getObject(vnum);
				switch (type) {
				case MudConstants.ITEM_FURNITURE:
				case MudConstants.ITEM_ARMOR: {
					temp.setiValue(3, Integer.parseInt(field.getText()));
					break;
				}
				case MudConstants.ITEM_STAFF:
				case MudConstants.ITEM_WAND:
				case MudConstants.ITEM_SCROLL:
				case MudConstants.ITEM_POTION:
				case MudConstants.ITEM_PILL: // all string types
				{
					temp.setsValue(3, field.getText());
					break;
				}
				default:
					break;
				}
				System.out.println("Set the value..");
			} catch (Exception exp) {
				System.out.println("Error setting value 3.");
			} finally {
				update();
			}
		}
	}

	class v4FocusListener implements FocusListener {
		public void focusGained(FocusEvent e) {
			int which = MudConstants.TypeLookup(typeBox.getSelectedIndex());

			if (which == MudConstants.ITEM_WEAPON) {
				MudObject temp = (MudObject) model.getArea().getObject(vnum);
				FlagChoice choice = new FlagChoice("weapon flags", MudConstants.weaponFlagNames,
						MudConstants.weaponFlags, MudConstants.NUM_WEAPON_FLAGS, null);
				choice.setFlags(temp.getiValue(4));

				int dec = JOptionPane.showConfirmDialog(null, choice, "Set weapon flags:",
						JOptionPane.OK_CANCEL_OPTION);

				if (dec == JOptionPane.CANCEL_OPTION)
					choice.setFlags(temp.getiValue(4));

				temp.setiValue(4, choice.getFlags());
				vnum = temp.vnum;
				update();
			} else if (which == MudConstants.ITEM_SCROLL || which == MudConstants.ITEM_POTION
					|| which == MudConstants.ITEM_PILL) {
				MudObject temp = (MudObject) model.getArea().getObject(vnum);
				JComboBox options = theSpellList.getSpellListComboBox();
				options.setSelectedItem(temp.getsValue(4));

				int dec = JOptionPane.showConfirmDialog(null, options, "Choose spell:", JOptionPane.OK_CANCEL_OPTION);

				if (dec == JOptionPane.CANCEL_OPTION)
					options.setSelectedItem(temp.getsValue(4));

				if (options.getSelectedIndex() != 0)
					temp.setsValue(4, (String) options.getSelectedItem());
				else
					temp.setsValue(4, "");

				vnum = temp.vnum;
				update();
			}
		}

		public void focusLost(FocusEvent e) {
			int type = MudConstants.TypeLookup(typeBox.getSelectedIndex());

			JTextField field = (JTextField) e.getSource();

			try {
				if (vnum == -1)
					return;

				MudObject temp = model.getArea().getObject(vnum);
				switch (type) {
				case MudConstants.ITEM_FURNITURE:
				case MudConstants.ITEM_CONTAINER: {
					temp.setiValue(4, Integer.parseInt(field.getText()));
					break;
				}
				case MudConstants.ITEM_SCROLL:
				case MudConstants.ITEM_POTION:
				case MudConstants.ITEM_PILL: // all string types
				{
					temp.setsValue(4, field.getText());
					break;
				}
				default:
					break;
				}
				System.out.println("Set the value..");
			} catch (Exception exp) {
				System.out.println("Error setting value 4.");
			} finally {
				update();
			}
		}
	}

}

/*
 * switch( which ) { case MudConstants.ITEM_LIGHT : { break; } case
 * MudConstants.ITEM_SCROLL : { break; } case MudConstants.ITEM_WAND : { break;
 * } case MudConstants.ITEM_STAFF : { break; } case MudConstants.ITEM_WEAPON : {
 * break; } case MudConstants.ITEM_TREASURE : { break; } case
 * MudConstants.ITEM_ARMOR : { break; } case MudConstants.ITEM_POTION : { break;
 * } case MudConstants.ITEM_CLOTHING : { break; } case
 * MudConstants.ITEM_FURNITURE : { break; } case MudConstants.ITEM_TRASH : {
 * break; } case MudConstants.ITEM_CONTAINER : { break; } case
 * MudConstants.ITEM_DRINK_CON : { break; } case MudConstants.ITEM_KEY : {
 * break; } case MudConstants.ITEM_FOOD : { break; } case
 * MudConstants.ITEM_MONEY : { break; } case MudConstants.ITEM_BOAT : { break; }
 * case MudConstants.ITEM_FOUNTAIN : { break; } case MudConstants.ITEM_PILL : {
 * break; } case MudConstants.ITEM_MAP : { break; } case
 * MudConstants.ITEM_PORTAL : { break; } case MudConstants.ITEM_WARP_STONE : {
 * break; } case MudConstants.ITEM_ROOM_KEY : { break; } case
 * MudConstants.ITEM_GEM : { break; } case MudConstants.ITEM_JEWELRY : { break;
 * } case MudConstants.ITEM_JUKEBOX : { break; } case MudConstants.ITEM_ORE : {
 * break; }
 * 
 * }
 */
