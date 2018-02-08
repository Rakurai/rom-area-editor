// George Frick
// MobView.java
// Area Editor Project, Spring 2002
package com.ageoflegacy.aedit.ui.view.mobView;

import com.ageoflegacy.aedit.beans.Armor;
import com.ageoflegacy.aedit.beans.Dice;
import com.ageoflegacy.aedit.model.*;
import com.ageoflegacy.aedit.model.area.Area;
import com.ageoflegacy.aedit.model.area.Mobile;
import com.ageoflegacy.aedit.model.table.RaceTableEntry;
import com.ageoflegacy.aedit.model.table.RaceDataTable;
import com.ageoflegacy.aedit.ui.*;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.net.URL;

/**
 * The mob view collects together all of the fields and handles the form
 * actions.sss
 */
public class MobView extends com.ageoflegacy.aedit.ui.view.EditorView implements ActionListener,
		com.ageoflegacy.aedit.ui.view.mobView.ArmorForm, com.ageoflegacy.aedit.ui.view.mobView.DiceForm {
	private JTextArea desc;
	private JScrollPane descHolder;
	private JTextField fields[];
	private JButton newMobButton;
	private JButton deleteMobButton;
	private JButton nextButton;
	private JButton previousButton;
	private JButton triggerButton;
	private JButton duplicateButton;
	private JButton shopButton;
	private JButton diceButton;
	private JTextField levelField;
	private JTextField densityField;
	private JComboBox alignmentBox;
	private JComboBox sexBox;
	private JComboBox raceBox;
	private FlagChoice actFlags;
	private JTextField groupField;
	private JTextField hitRollField;
	private JComboBox damTypeBox;
	private JTabbedPane tabPane;
	private DiceField hitDice;
	private DiceField damageDice;
	private DiceField manaDice;
	private ArmorField armorField;
	private FlagChoice affectChoice;
	private SizeChooser sizeChoice;
	private JTextField mobMaterial;
	private JTextField mobWealth;
	private JTextField mobDeathCry;
	private JComboBox defaultPos;
	private JComboBox startPos;
	private JComboBox vnumBox;
	private IRVChoice irvPanel;
	private FlagChoice partChoice, formChoice, offenseChoice;
	private JPanel mainPanel;
	private MobileEquipmentPanel equipmentPanel;
	private MobInPanel inventoryPanel;
	ClassLoader loader;
	URL b1;

	public MobView(Model m) {
		super(m);

		loader = ClassLoader.getSystemClassLoader();
		b1 = loader.getResource("dice.gif");

		tabPane = new JTabbedPane();
		fields = new JTextField[4]; // [vnum], short, short, long, desc.

		constraint.insets = new Insets(0, 0, 0, 0);

		// Name
		fields[1] = new JTextField(20);
		JPanel f1panel = new JPanel();
		f1panel.setLayout(new GridLayout(1, 1));
		f1panel.setBorder(new TitledBorder("Name"));
		f1panel.add(fields[1]);

		// Short
		fields[2] = new JTextField(29);
		JPanel f2panel = new JPanel();
		f2panel.setLayout(new GridLayout(1, 1));
		f2panel.setBorder(new TitledBorder("Short Description"));
		f2panel.add(fields[2]);

		// Long
		fields[3] = new JTextField(50);
		JPanel f3panel = new JPanel();
		f3panel.setLayout(new GridLayout(1, 1));
		f3panel.setBorder(new TitledBorder("Long Description"));
		f3panel.add(fields[3]);

		fields[1].addFocusListener(new StringFieldFocusListener());
		fields[2].addFocusListener(new StringFieldFocusListener());
		fields[3].addFocusListener(new StringFieldFocusListener());

		// Vnum box
		vnumBox = model.getArea().getVnumCombo("mob");
		newMobButton = new JButton("New");
		deleteMobButton = new JButton("Delete");
		nextButton = new JButton("Next");
		previousButton = new JButton("Back");
		triggerButton = new JButton("Triggers");
		duplicateButton = new JButton("Duplicate");
		shopButton = new JButton("Shop");
		diceButton = new JButton(new ImageIcon(b1));

		levelField = new JMudNumberField(4);
		JPanel levelPanel = new JPanel();
		levelPanel.setLayout(new GridLayout(1, 1));
		levelPanel.setBorder(new TitledBorder("Level"));
		levelPanel.add(levelField);
		levelField.addFocusListener(new StringFieldFocusListener());
		levelField.setToolTipText("The mobiles level, same as a players.");

		densityField = new JTextField(6);
		JPanel densityPanel = new JPanel();
		densityPanel.setLayout(new GridLayout(1, 1));
		densityPanel.setBorder(new TitledBorder("Density"));
		densityPanel.add(densityField);
		densityField.setEditable(false);
		densityField.setToolTipText("Displays how many of this mob are reset in the model.");

		sexBox = new JComboBox(MudConstants.sexArray);
		sexBox.setBorder(new TitledBorder("Sex"));

		alignmentBox = new JComboBox(MudConstants.alignmentArray);
		alignmentBox.setBorder(new TitledBorder("Alignment"));
		alignmentBox.setToolTipText("Selecting the mobiles role, from satanic to angelic");

		raceBox = model.getRaceDataTable().getRaceComboBox();
		raceBox.setBorder(new TitledBorder("Race"));

		groupField = new JMudNumberField(4);
		JPanel groupPanel = new JPanel();
		groupPanel.setLayout(new GridLayout(1, 1));
		groupPanel.setBorder(new TitledBorder("Group"));
		groupPanel.add(groupField);
		groupField.addFocusListener(new StringFieldFocusListener());

		hitRollField = new JMudNumberField(4);
		JPanel hitRollPanel = new JPanel();
		hitRollPanel.setLayout(new GridLayout(1, 1));
		hitRollPanel.setBorder(new TitledBorder("HitRoll"));
		hitRollPanel.add(hitRollField);
		hitRollField.addFocusListener(new StringFieldFocusListener());

		damTypeBox = model.getDamTypeTable().getComboBox();
		damTypeBox.setBorder(new TitledBorder("DamType"));

		actFlags = new FlagChoice(null, MudConstants.actFlagNames, MudConstants.actFlags, MudConstants.NUM_ACT_FLAGS,
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						Mobile temp = model.getArea().getMobile(vnum);
						if (temp == null)
							return;

						temp.setActFlags(actFlags.getFlags());
						updateIRV();
					}
				}, 7);

		hitDice = new com.ageoflegacy.aedit.ui.view.mobView.DiceField("Hit", this);
		damageDice = new DiceField("Damage", this);
		manaDice = new DiceField("Mana", this);
		armorField = new com.ageoflegacy.aedit.ui.view.mobView.ArmorField(this);
		armorField.addFocusListener(new StringFieldFocusListener());

		affectChoice = new FlagChoice(null, MudConstants.affectFlagNames, MudConstants.affectFlags,
				MudConstants.NUM_AFFECTS, new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						Mobile temp = model.getArea().getMobile(vnum);
						if (temp == null)
							return;

						temp.setAffectedBy(affectChoice.getFlags());
						updateIRV();
					}
				}, 6);

		irvPanel = new IRVChoice("Immune/Resist/Vulnerable", MudConstants.IRVNames, MudConstants.IRVFlags,
				MudConstants.NUM_IRV, new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						Mobile temp = model.getArea().getMobile(vnum);
						if (temp == null)
							return;

						temp.setImmunityFlags(irvPanel.getFlags(0));
						temp.setResistanceFlags(irvPanel.getFlags(1));
						temp.setVulnerabilityFlags(irvPanel.getFlags(2));
						updateIRV();
					}
				}, 6);

		partChoice = new FlagChoice(null, MudConstants.partNames, MudConstants.partFlags, MudConstants.NUM_PARTS,
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						Mobile temp = model.getArea().getMobile(vnum);
						if (temp == null)
							return;

						temp.setParts(partChoice.getFlags());
						updateIRV();
					}
				}, 8);

		formChoice = new FlagChoice(null, MudConstants.formNames, MudConstants.formFlags, MudConstants.NUM_FORMS,
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						Mobile temp = model.getArea().getMobile(vnum);
						if (temp == null)
							return;

						temp.setForm(formChoice.getFlags());
						updateIRV();
					}
				}, 7);

		offenseChoice = new FlagChoice(null, MudConstants.offenseNames, MudConstants.offenseFlags,
				MudConstants.NUM_OFFENSE, new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						Mobile temp = model.getArea().getMobile(vnum);
						if (temp == null)
							return;

						temp.setOffensiveFlags(offenseChoice.getFlags());
						updateIRV();
					}
				}, 7);

		desc = new JTextArea(5, 20);
		desc.addFocusListener(new StringFieldFocusListener());
		descHolder = new JScrollPane(desc);
		descHolder.setBorder(new EtchedBorder(EtchedBorder.RAISED));

		sizeChoice = new SizeChooser();

		mobMaterial = new JMudTextField(8);
		JPanel materialPanel = new JPanel();
		materialPanel.setLayout(new GridLayout(1, 1));
		materialPanel.setBorder(new TitledBorder("Material"));
		materialPanel.add(mobMaterial);
		mobMaterial.addFocusListener(new StringFieldFocusListener());

		mobWealth = new JMudNumberField(4);
		JPanel wealthPanel = new JPanel();
		wealthPanel.setLayout(new GridLayout(1, 1));
		wealthPanel.setBorder(new TitledBorder("Wealth"));
		wealthPanel.add(mobWealth);
		mobWealth.addFocusListener(new StringFieldFocusListener());

		mobDeathCry = new JMudTextField(30);
		JPanel deathCryPanel = new JPanel();
		deathCryPanel.setLayout(new GridLayout(1, 1));
		deathCryPanel.setBorder(new TitledBorder("Death Cry"));
		deathCryPanel.add(mobDeathCry);
		mobDeathCry.addFocusListener(new StringFieldFocusListener());

		defaultPos = new JComboBox(MudConstants.positionNames);
		defaultPos.setBorder(new TitledBorder("Default Pos."));

		startPos = new JComboBox(MudConstants.positionNames);
		startPos.setBorder(new TitledBorder("Start. Pos."));

		inventoryPanel = new MobInPanel(model.getArea(), this);

		/*******************************************************
		 * Layout.
		 *******************************************************/
		mainPanel = new JPanel();
		mainPanel.setLayout(layout);

		// Browser
		Box buttonPanel = Box.createHorizontalBox();
		buttonPanel.add(vnumBox);
		buttonPanel.add(previousButton);
		buttonPanel.add(nextButton);
		buttonPanel.add(newMobButton);
		buttonPanel.add(deleteMobButton);
		buttonPanel.add(triggerButton);
		buttonPanel.add(duplicateButton);
		buttonPanel.add(shopButton);
		buttonPanel.add(diceButton);

		JPanel row1 = new JPanel();
		row1.setLayout(new MigLayout());
		row1.add(raceBox);
		row1.add(sexBox);
		row1.add(startPos);
		row1.add(defaultPos);
		row1.add(alignmentBox);
		row1.add(sizeChoice);
		row1.add(damTypeBox);

		JPanel row2 = new JPanel();
		row2.setLayout(new MigLayout());
		row2.add(levelPanel);
		row2.add(f1panel);
		row2.add(f2panel);

		JPanel row3 = new JPanel();
		row3.setLayout(new MigLayout());
		row3.add(materialPanel);
		row3.add(f3panel);

		JPanel row4 = new JPanel();
		row4.setLayout(new MigLayout());
		row4.add(deathCryPanel);
		row4.add(hitRollPanel);
		row4.add(groupPanel);
		row4.add(wealthPanel);

		JPanel row5 = new JPanel();
		row5.setLayout(new MigLayout());
		row5.add(hitDice);
		row5.add(damageDice);
		row5.add(manaDice);
		row5.add(armorField);

		JPanel actoffPanel = new JPanel();
		actoffPanel.setLayout(new MigLayout());
		actoffPanel.add(actFlags, "wrap");
		actoffPanel.add(offenseChoice);

		tabPane.addTab("Act & Offense", null, actoffPanel, "mobile act and offense flags");
		tabPane.addTab("Affect Flags", null, affectChoice, "Mobile Affected By Flags");
		tabPane.addTab("Description", null, descHolder, "Mobile description");
		tabPane.addTab("Immune/Resist/Vulnerable", null, irvPanel, "Mobiles immune/resist/vulnerable flags");
		tabPane.addTab("Parts", null, partChoice, "Mobile parts");
		tabPane.addTab("Form", null, formChoice, "Mobile form");
		tabPane.addTab("Inventory", null, inventoryPanel, "Mobile inventory");

		equipmentPanel = new MobileEquipmentPanel();
		equipmentPanel.setBorder(new TitledBorder("Equipment:"));

		insertAt(0, 0, 1, 1, row1, true, true);
		insertAt(0, 1, 1, 1, row2, true);
		insertAt(0, 2, 1, 1, row3, true);
		insertAt(0, 3, 1, 1, row4, true);
		insertAt(0, 4, 1, 1, row5, true);
		insertAt(0, 5, 1, 1, tabPane, true);

		this.setLayout(new MigLayout());
		this.add(buttonPanel, "wrap,span");
		this.add(equipmentPanel);
		this.add(mainPanel, "wrap, growx");
		addListeners();
		vnum = -1;
		update();
	}

	public void insertAt(int x, int y, int width, int height, Component i) {
		constraint.gridx = x;
		constraint.gridy = y;
		constraint.gridwidth = width;
		constraint.gridheight = height;
		layout.setConstraints(i, constraint);
		mainPanel.add(i);
	}

	public void update() {
		if (model.getArea().getMobCount() == 0) {
			// set defaults.
			groupField.setText("0");
			levelField.setText("0");
			densityField.setText("0");
			hitRollField.setText("0");
			mobWealth.setText("0");
			mobMaterial.setText("nothing");
			fields[1].setText("no mob loaded");
			fields[2].setText("no mob loaded");
			fields[3].setText("no mob loaded");
			desc.setText("no mob loaded");
			mobDeathCry.setText("no mob loaded");
			// theDice.update();
			actFlags.setFlags(0);
			affectChoice.setFlags(0);
			offenseChoice.setFlags(0);
			partChoice.setFlags(0);
			formChoice.setFlags(0);
			irvPanel.setFlags(0, 0);
			irvPanel.setFlags(0, 1);
			irvPanel.setFlags(0, 2);
			shopButton.setEnabled(false);
			setEnabled(false);
		} else {
			if (vnum == -1 || vnum < model.getArea().getLowVnum() || vnum > model.getArea().getHighVnum())
				vnum = model.getArea().getFirstMobVnum();

			Mobile temp = (Mobile) vnumBox.getSelectedItem();
			if (temp == null || temp.getVnum() != vnum)
				temp = model.getArea().getMobile(vnum);

			setEnabled(true);
			sexBox.setSelectedIndex(temp.getSex());
			startPos.setSelectedIndex(MudConstants.arrayLookupPos(temp.getStartPosition()));
			defaultPos.setSelectedIndex(MudConstants.arrayLookupPos(temp.getDefaultPosition()));

			sizeChoice.setCurrentSize(temp.getSize());
			alignmentBox.setSelectedIndex(MudConstants.arrayAlignmentLookup(temp.getAlignment()));

			fields[1].setText(temp.getName());
			fields[2].setText(temp.getShortDesc());
			fields[3].setText(temp.getLongDesc());
			mobDeathCry.setText(temp.getDeathCry());
			levelField.setText(Integer.toString(temp.getLevel()));
			densityField.setText(Integer.toString(temp.getMax()));
			hitRollField.setText(Integer.toString(temp.getHitRoll()));
			groupField.setText(Integer.toString(temp.getGroup()));
			mobWealth.setText(Integer.toString(temp.getWealth()));
			// 4damtype
			damTypeBox.setSelectedItem(temp.getDamageType());
			mobMaterial.setText(temp.getMaterial());
			raceBox.setSelectedItem(temp.getRace());
			desc.setText(temp.getDescription());
			updateIRV();

			for (Armor at : Armor.values()) {
				armorField.setValue(at, temp.getAC(at));
			}

			hitDice.setValue(temp.getHitDice());
			damageDice.setValue(temp.getDamageDice());
			manaDice.setValue(temp.getManaDice());

			if (temp.getShop() != null)
				shopButton.setEnabled(true);
			else
				shopButton.setEnabled(false);

			if (model.getArea().getMprogCount() > 0)
				triggerButton.setEnabled(true);
			else
				triggerButton.setEnabled(false);

			equipmentPanel.update(model.getArea(), temp);
		}
		vnumBox.setSelectedItem(model.getArea().getMobile(vnum));
		inventoryPanel.update(vnum);
		this.validate();
		equipmentPanel.validate();
		repaint();
	}

	public void setEnabled(boolean value) {
		groupField.setEnabled(value);
		levelField.setEnabled(value);
		densityField.setEnabled(value);
		hitRollField.setEnabled(value);
		mobWealth.setEnabled(value);
		mobMaterial.setEnabled(value);
		fields[1].setEnabled(value);
		fields[2].setEnabled(value);
		fields[3].setEnabled(value);
		desc.setEnabled(value);
		mobDeathCry.setEnabled(value);
		hitDice.setEnabled(value);
		damageDice.setEnabled(value);
		manaDice.setEnabled(value);
		actFlags.setEnabled(value);
		affectChoice.setEnabled(value);
		offenseChoice.setEnabled(value);
		partChoice.setEnabled(value);
		formChoice.setEnabled(value);
		irvPanel.setEnabled(value);
		triggerButton.setEnabled(value);
		sexBox.setEnabled(value);
		startPos.setEnabled(value);
		defaultPos.setEnabled(value);
		sizeChoice.setEnabled(value);
		alignmentBox.setEnabled(value);
		raceBox.setEnabled(value);
		deleteMobButton.setEnabled(value);
		nextButton.setEnabled(value);
		previousButton.setEnabled(value);
		damTypeBox.setEnabled(value);
		for (int a = 0; a < 6; a++) {
			tabPane.setEnabledAt(a, value);
		}
		vnumBox.setEnabled(value);
		equipmentPanel.setEnabled(value);
		inventoryPanel.setEnabled(value);
		armorField.setEnabled(value);
	}

	private void updateIRV() {
		Mobile temp = (Mobile) vnumBox.getSelectedItem();
		if (temp == null || temp.getVnum() != vnum)
			temp = model.getArea().getMobile(vnum);

		actFlags.setFlags(temp.getActFlags());
		affectChoice.setFlags(temp.getAffectedBy());
		offenseChoice.setFlags(temp.getOffensiveFlags());
		partChoice.setFlags(temp.getParts());
		formChoice.setFlags(temp.getForm());
		irvPanel.setFlags(temp.getImmunityFlags(), 0);
		irvPanel.setFlags(temp.getResistanceFlags(), 1);
		irvPanel.setFlags(temp.getVulnerabilityFlags(), 2);
	}

	private void addListeners() {
		sexBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Mobile temp = model.getArea().getMobile(vnum);
				if (temp == null)
					return;

				temp.setSex(sexBox.getSelectedIndex());
				update();
			}
		});
		alignmentBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Mobile temp = model.getArea().getMobile(vnum);
				if (temp == null)
					return;

				temp.setAlignment(MudConstants.alignmentLookup(alignmentBox.getSelectedIndex()));
				update();
			}
		});

		raceBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Mobile temp = model.getArea().getMobile(vnum);
				if (temp == null)
					return;

				if (temp.getRace() != raceBox.getSelectedItem()) {
					temp.setRace((RaceTableEntry) raceBox.getSelectedItem());
				}

				updateIRV();
			}
		});

		diceButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Mobile mob = model.getArea().getMobile(vnum);
				if (mob == null)
					return;
				int level = mob.getLevel() <= 1 ? 0 : mob.getLevel() - 1;

				// Hitpoints
				setDice("hit", new Dice(DiceTables.MOBstat_table[level][0]));

				// Armor
				setArmor(Armor.PIERCE, Integer.parseInt(DiceTables.MOBstat_table[level][1]) / 10);
				setArmor(Armor.BASH, Integer.parseInt(DiceTables.MOBstat_table[level][1]) / 10);
				setArmor(Armor.SLASH, Integer.parseInt(DiceTables.MOBstat_table[level][1]) / 10);
				setArmor(Armor.MAGIC, Integer.parseInt(DiceTables.MOBstat_table[level][2]) / 10);

				// Damage
				setDice("damage", new Dice(DiceTables.MOBstat_table[level][3]));

				// Mana
				setDice("mana", new Dice(DiceTables.MOBstat_table[level][4]));

				update();
			}
		});

		shopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Mobile mob = model.getArea().getMobile(vnum);
				if (mob == null)
					return;

				Object[] options = { "CLOSE" };
				JOptionPane.showOptionDialog(null, mob.getShop().getPanel(), "Shop Data for: " + mob.getShortDesc(),
						JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			}
		});

		duplicateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int newVnum = model.getArea().getFreeMobileVnum();
				if (newVnum == -1) {
					inform("You are out of vnums!");
					return;
				}
				Mobile temp = new Mobile(newVnum, model, vnum);
				model.getArea().insert(temp);
				vnum = newVnum;
				update();
			}
		});

		triggerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TriggerPanel mp = new TriggerPanel(vnum, model.getArea());
				Mobile mob = model.getArea().getMobile(vnum);
				if (mob == null)
					return;

				Object[] options = { "CLOSE" };
				JOptionPane.showOptionDialog(null, mp, "Mprog Triggers for: " + mob.getShortDesc(),
						JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			}

		});

		previousButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (vnum <= model.getArea().getFirstMobVnum())
					return;

				int temp = vnum - 1;
				while (model.getArea().getMobile(temp) == null && temp >= model.getArea().getFirstMobVnum())
					temp--;

				if (temp >= model.getArea().getFirstMobVnum())
					vnum = temp;

				update();

			}
		});

		nextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (vnum >= model.getArea().getHighVnum())
					return;

				int temp = vnum + 1;
				while (model.getArea().getMobile(temp) == null && temp <= model.getArea().getHighVnum())
					temp++;

				if (temp <= model.getArea().getHighVnum())
					vnum = temp;

				update();
			}
		});

		newMobButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Get the lowest available mob vnum
				int vnum = model.getArea().getFreeMobileVnum();
				if (vnum == -1) {
					inform("You are out of vnums!");
					return;
				}
				Mobile temp = new Mobile(vnum, model);
				temp.setRace(model.getRaceDataTable().defaultEntry());
				model.getArea().insert(temp);
				update(vnum);
			}
		});

		deleteMobButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!confirm("Please confirm Mob deletion. All resets will be lost."))
					return;

				model.getArea().deleteMobile(vnum);
				vnum = -1;
				update();
			}
		});

		sizeChoice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Mobile temp = model.getArea().getMobile(vnum);
				if (temp == null)
					return;

				temp.setSize(sizeChoice.getSelectedSize());
			}
		});
		damTypeBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Mobile temp = model.getArea().getMobile(vnum);
				if (temp == null)
					return;

				temp.setDamageType((String) damTypeBox.getSelectedItem());
				updateIRV();
			}
		});
		vnumBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Mobile temp = (Mobile) vnumBox.getSelectedItem();
				if (temp == null)
					return;

				vnum = temp.getVnum();
				update();
			}
		});
		startPos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Mobile temp = model.getArea().getMobile(vnum);
				if (temp == null)
					return;

				temp.setStartPosition(MudConstants.positionFlags[startPos.getSelectedIndex()]);
			}
		});
		defaultPos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Mobile temp = model.getArea().getMobile(vnum);
				if (temp == null)
					return;

				temp.setDefaultPosition(MudConstants.positionFlags[defaultPos.getSelectedIndex()]);
			}
		});
	}

	public void actionPerformed(ActionEvent e) {
	}

	/**
	 * The armorfield determined there was a change, so this is called.
	 *
	 * @param type
	 *            The ArmorType that changed.
	 * @param value
	 *            The new value
	 */
	public void setArmor(Armor type, int value) {
		Mobile mob = (Mobile) vnumBox.getSelectedItem();
		if (mob == null) {
			return;
		}
		mob.setAC(type, value);
	}

	/**
	 * A dice field determined there was a change, so this is called.
	 *
	 * @param name
	 *            alias of dice field that changed
	 * @param dice
	 *            new dice value.
	 */
	public void setDice(String name, Dice dice) {
		Mobile mob = (Mobile) vnumBox.getSelectedItem();
		if (mob == null) {
			return;
		}
		if (name.equalsIgnoreCase("hit")) {
			mob.setHitDice(dice);
		} else if (name.equalsIgnoreCase("mana")) {
			mob.setManaDice(dice);
		} else if (name.equalsIgnoreCase("damage")) {
			mob.setDamageDice(dice);
		}
	}

	class DeathCryPanel extends JPanel {
		Mobile mob;
		String dCry;
		JTextField field;

		public DeathCryPanel() {
			mob = model.getArea().getMobile(vnum);
			dCry = mob.getDeathCry();

			field = new JTextField(40);
			field.setBorder(new TitledBorder("Mobile DeathCry:"));

			this.add(field);
			if (dCry != null)
				field.setText(dCry);
		}

		public String getDeathCry() {
			return field.getText();
		}
	}

	class StringFieldFocusListener implements FocusListener {
		public void focusGained(FocusEvent e) {
		}

		public void focusLost(FocusEvent e) {
			Mobile mob = model.getArea().getMobile(vnum);
			if (mob == null)
				return;

			String newS, oldS;
			int oldVal, newVal;

			if (e.getSource() instanceof JTextArea) {
				oldS = mob.getDescription();
				newS = desc.getText();
				if (newS.equals("")) {
					update();
					return;
				}
				if (newS.equalsIgnoreCase(oldS))
					return;
				else
					mob.setDescription(newS);

				return;
			}

			JTextField field = (JTextField) e.getSource();

			newS = field.getText();
			if (newS.equals("")) {
				update();
				return;
			}

			if (field == fields[1]) {
				oldS = mob.getName();
				if (newS.equalsIgnoreCase(oldS))
					return;
				else
					mob.setName(newS);
			} else if (field == fields[2]) {
				oldS = mob.getShortDesc();
				if (newS.equalsIgnoreCase(oldS))
					return;
				else
					mob.setShortName(newS);

				vnumBox.updateUI();
			} else if (field == fields[3]) {
				oldS = mob.getLongDesc();
				if (newS.equalsIgnoreCase(oldS))
					return;
				else
					mob.setLongName(newS);
			} else if (field == mobMaterial) {
				oldS = mob.getMaterial();
				if (newS.equalsIgnoreCase(oldS))
					return;
				else
					mob.setMaterial(newS); // one argument this.
			} else if (field == mobDeathCry) {
				oldS = mob.getDeathCry();
				if (newS.equalsIgnoreCase(oldS))
					return;
				else
					mob.setDeathCry(newS); // one argument this.
			} else {
				try {
					newVal = Integer.parseInt(newS);
					if (field == mobWealth) {
						oldVal = mob.getWealth();

						if (newVal == oldVal)
							return;
						else
							mob.setWealth(newVal);
					} else if (field == groupField) {
						oldVal = mob.getGroup();

						if (newVal == oldVal)
							return;
						else
							mob.setGroup(newVal);
					} else if (field == levelField) {
						oldVal = mob.getLevel();

						if (newVal == oldVal)
							return;
						else
							mob.setLevel(newVal);
					} else if (field == hitRollField) {
						oldVal = mob.getHitRoll();

						if (newVal == oldVal)
							return;
						else
							mob.setHitRoll(newVal);
					} else
						return;
				} catch (Exception evt1) {
					update();
					return;
				}
			} // else
			update();
		} // focus lost
	} // class
}
