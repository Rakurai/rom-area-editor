/*
 * George Frick
 * RoomView.java
 * Area Editor Project, Spring 2002
 *
 * This class displays the rooms, including a navigator, and map.
 * It allows viewing, changing, creation, and deletion of rooms and exits.
 */
package com.ageoflegacy.aedit.ui.view.roomView;

import com.ageoflegacy.aedit.model.MudConstants;
import com.ageoflegacy.aedit.model.area.Area;
import com.ageoflegacy.aedit.model.area.MudExit;
import com.ageoflegacy.aedit.model.area.MudObject;
import com.ageoflegacy.aedit.io.ResourceLoader;
import com.ageoflegacy.aedit.model.*;
import com.ageoflegacy.aedit.ui.*;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;

public class RoomView extends com.ageoflegacy.aedit.ui.view.EditorView implements ActionListener {
	private JTextField[] fields;
	private JTextArea desc;
	private com.ageoflegacy.aedit.ui.view.roomView.SectorChooser sectorchoice;
	private FlagChoice flagchoice;
	private MapView themap;
	private JButton newRoomButton;
	private JButton deleteRoomButton;
	private JButton backRoomButton;
	private JButton nextRoomButton;
	private JButton doorsRoomButton;
	private ExitPanel exitpanel;
	private JButton containerButton;
	private JButton extraDescButton;
	private JRadioButton z1;
	private JRadioButton z2;
	private JScrollPane mapPort;
	private JComboBox vnumBox;
	private JCheckBox keyDig;
	private JCheckBox keyLink;
	static final long serialVersionUID = 23342342341L;

	public RoomView(Model m) {
		super(m);
		Dimension mapHolderDimension = new Dimension(500, 600);

		containerButton = createResetButton();
		extraDescButton = createExtraDescriptionButton();

		flagchoice = new FlagChoice("Room Flags", MudConstants.roomFlags, MudConstants.roomFlagData,
				MudConstants.roomFlagCount, this, 6);

		fields = new JTextField[5];
		constraint.insets = new Insets(3, 3, 3, 3);

		fields[1] = new JTextField(20);
		JPanel namePanel = new LabeledField("Name", fields[1]);

		fields[2] = new JMudNumberField(4);
		JPanel hpPanel = new LabeledField("HP Regen", fields[2]);

		fields[3] = new JMudNumberField(4);
		JPanel manaPanel = new LabeledField("Mana Regen", fields[3]);
		
		fields[4] = new JMudNumberField(5);
		JPanel telePanel = new LabeledField("Teleport", fields[4]);

		sectorchoice = createSectorChooser();
		JPanel sectorPanel = new LabeledField("Sector", sectorchoice);

		desc = new JTextArea(6, 60);
		desc.setLineWrap(true);
		desc.setColumns(60);
		JScrollPane descHolder = new JScrollPane(desc);
		JPanel descPanel = new LabeledField("Description", descHolder, true);

		fields[1].addFocusListener(new StringFieldFocusListener());
		fields[2].addFocusListener(new StringFieldFocusListener());
		fields[3].addFocusListener(new StringFieldFocusListener());
		fields[4].addFocusListener(new StringFieldFocusListener());
		desc.addFocusListener(new StringFieldFocusListener());

		themap = new MapView(model.getArea(), this);
		JPanel mapHolder = new JPanel();
		mapHolder.setLayout(new MigLayout());

		mapPort = new JScrollPane(themap);

		JPanel zPanel = new JPanel();
		zPanel.setLayout(new MigLayout());
		ButtonGroup bGroup = new ButtonGroup();
		z1 = new JRadioButton("1X");
		z2 = new JRadioButton("2X", true);
		keyDig = new JCheckBox("KeyDig");
		keyLink = new JCheckBox("KeyLink");
		zoomListener zListen = new zoomListener();
		z1.addActionListener(zListen);
		z2.addActionListener(zListen);
		bGroup.add(z1);
		bGroup.add(z2);
		zPanel.add(new JLabel(new ImageIcon(ResourceLoader.getURL("image/magnify.gif"))));
		zPanel.add(z1);
		zPanel.add(z2);
		zPanel.add(keyDig);
		zPanel.add(keyLink);
		mapHolder.add(zPanel, "wrap");
		mapHolder.add(mapPort, "span, growx, growy");
		mapHolder.setPreferredSize(mapHolderDimension);

		vnumBox = model.getArea().getVnumCombo("room");
		newRoomButton = new JButton("New");
		deleteRoomButton = new JButton("Delete");
		backRoomButton = new JButton("Back");
		nextRoomButton = new JButton("Next");
		doorsRoomButton = new JButton("Doors");
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new MigLayout());
		buttonPanel.add(vnumBox);
		buttonPanel.add(backRoomButton);
		buttonPanel.add(nextRoomButton);
		buttonPanel.add(newRoomButton);
		buttonPanel.add(deleteRoomButton);
		buttonPanel.add(doorsRoomButton);
		buttonPanel.add(extraDescButton);
		buttonPanel.add(containerButton);

		doorsRoomButton.addActionListener(new DoorListener(model));
		newRoomButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Get the lowest available room vnum
				int vnum = model.getArea().getFreeRoomVnum();
				if (vnum == -1) {
					inform("You are out of vnums!");
					return;
				}
				Room temp = new Room(vnum, model.getArea());
				model.getArea().insert(temp);
				update(vnum);
			}
		});

		deleteRoomButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteRoom();
			}
		});

		backRoomButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (vnum <= model.getArea().getFirstRoomVnum())
					return;

				int temp = vnum - 1;
				while (model.getArea().getRoom(temp) == null && temp >= model.getArea().getFirstRoomVnum())
					temp--;

				if (temp >= model.getArea().getFirstRoomVnum())
					vnum = temp;

				update();

			}
		});

		nextRoomButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (vnum >= model.getArea().getHighVnum())
					return;

				int temp = vnum + 1;
				while (model.getArea().getRoom(temp) == null && temp <= model.getArea().getHighVnum())
					temp++;

				if (temp <= model.getArea().getHighVnum())
					vnum = temp;

				update();
			}
		});

		vnumBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Room temp = (Room) vnumBox.getSelectedItem();
				if (temp == null)
					return;

				vnum = temp.getVnum();
				update();
			}
		});

		exitpanel = new ExitPanel(this, model.getArea());

		setLayout(new MigLayout());

		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new MigLayout());
		leftPanel.add(sectorPanel);
		leftPanel.add(telePanel);
		leftPanel.add(hpPanel);
		leftPanel.add(manaPanel, "wrap");
		leftPanel.add(namePanel, "wrap,  span");
		leftPanel.add(descPanel, "wrap,  span");
		leftPanel.add(flagchoice, "wrap, span");
		leftPanel.add(exitpanel, "wrap, growx, span");

		JPanel split = new JPanel();
		split.setLayout(new MigLayout());
		split.add(leftPanel);
		split.add(mapHolder);

		add(buttonPanel, "wrap");
		add(split, "grow");

		update();
	}

	private SectorChooser createSectorChooser() {
		SectorChooser chooser = new SectorChooser();
		chooser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (vnum != -1) {
					Room temp = model.getArea().getRoom(vnum);
					temp.setSector(sectorchoice.getSector());
				}
				update();
			}
		});
		return chooser;
	}

	private JButton createExtraDescriptionButton() {
		JButton button = new JButton("Extra Descriptions");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ExtraDescPanel mp = new ExtraDescPanel(vnum, model.getArea());
				int choice = -1;
				int errornum = -1;
				Room room = model.getArea().getRoom(vnum);
				if (room == null)
					return;

				Object[] options = { "CLOSE" };
				do {
					errornum = -1;
					choice = JOptionPane.showOptionDialog(null, mp, "Extra Descriptions for: " + room.toString(),
							JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

				} while (errornum == 1);

			}
		});
		return button;

	}

	private JButton createResetButton() {
		JButton button = new JButton("Resets");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RoomInPanel mp = new RoomInPanel(vnum, model.getArea());
				int errornum = -1;
				Room room = model.getArea().getRoom(vnum);
				if (room == null)
					return;

				Object[] options = { "CLOSE" };
				do {
					errornum = -1;
					JOptionPane.showOptionDialog(null, mp, "Room Resets: " + room.toString(),
							JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

				} while (errornum == 1);

			}
		});
		return button;
	}

	public void update(int newVnum) {
		vnum = newVnum;
		update();
	}

	public void update() {
		if (model.getArea().getRoomCount() == 0) {
			fields[1].setText("No Room Loaded.");
			desc.setText("No Room Loaded." + sectorchoice.currentSector());
			fields[2].setText("100");
			fields[3].setText("100");
			fields[4].setText("0");
			exitpanel.reset();
			setEnabled(false);
		} else {
			if (vnum == -1 || vnum < model.getArea().getLowVnum() || vnum > model.getArea().getHighVnum())
				vnum = model.getArea().getFirstRoomVnum();
			Room temp = model.getArea().getRoom(vnum);
			fields[1].setText(temp.getName());
			desc.setText(temp.getDescription());
			fields[2].setText(Integer.toString(temp.getHeal())); // heal
			fields[3].setText(Integer.toString(temp.getMana())); // mana
			fields[4].setText(Integer.toString(temp.getTeleport())); // teleport
			sectorchoice.setCurrentSector(temp.getSector());
			flagchoice.setFlags(temp.getFlags());
			exitpanel.setVnum(vnum);
			themap.update(vnum);
			themap.repaint();
			JScrollBar barA = mapPort.getVerticalScrollBar();
			barA.setValue(barA.getMaximum() / 3);
			barA = mapPort.getHorizontalScrollBar();
			barA.setValue(barA.getMaximum() / 3);
			setEnabled(true);
		}
		vnumBox.setSelectedItem(model.getArea().getRoom(vnum));
	}

	public void setEnabled(boolean value) {
		fields[1].setEnabled(value);
		fields[2].setEnabled(value);
		fields[3].setEnabled(value);
		fields[4].setEnabled(value);
		desc.setEnabled(value);
		sectorchoice.setEnabled(value);
		flagchoice.setEnabled(value);
		exitpanel.setEnabled(value);
		vnumBox.setEnabled(value);
		extraDescButton.setEnabled(value);
		containerButton.setEnabled(value);
		z1.setEnabled(value);
		z2.setEnabled(value);
		keyDig.setEnabled(value);
		keyLink.setEnabled(value);
		deleteRoomButton.setEnabled(value);
		backRoomButton.setEnabled(value);
		nextRoomButton.setEnabled(value);
		doorsRoomButton.setEnabled(value);
	}

	public void deleteRoom() {
		int uVnum = -1;
		Room tRoom = model.getArea().getRoom(vnum);

		if (!confirm("Please confirm room deletion. All exits will be lost."))
			return;

		if (tRoom.getNorth() != null)
			uVnum = tRoom.getNorth().getVnum();
		else if (tRoom.getSouth() != null)
			uVnum = tRoom.getSouth().getVnum();
		else if (tRoom.getWest() != null)
			uVnum = tRoom.getWest().getVnum();
		else if (tRoom.getEast() != null)
			uVnum = tRoom.getEast().getVnum();

		model.getArea().deleteRoom(vnum);
		vnum = uVnum;
		update();
	}

	public void moveView(int dir) {
		Room temp;
		temp = model.getArea().getRoom(vnum);
		MudExit texit;

		if (temp == null) {
			inform("Exit leads out of model!");
		} else if (temp.getExit(dir) == null) {
			JPanel temp2 = new JPanel();
			GridBagConstraints constraint;
			GridBagLayout layout;
			constraint = new GridBagConstraints();
			constraint.insets = new Insets(3, 3, 3, 3);
			layout = new GridBagLayout();
			temp2.setLayout(layout);
			constraint.gridy = 0;
			constraint.gridx = 0;
			constraint.gridwidth = 2;

			JLabel msg = new JLabel(
					"<HTML><BODY><BOLD><HR>There is no room in that direction!</BOLD><BR><HR></BODY></HTML>");
			layout.setConstraints(msg, constraint);
			temp2.add(msg);

			JRadioButton linkRadio = new JRadioButton("Link to ->");
			JComboBox linkBox = model.getArea().getVnumCombo("rooms");
			JRadioButton digRadio = new JRadioButton("Dig a new room");
			digRadio.setMnemonic(KeyEvent.VK_D);
			linkRadio.setMnemonic(KeyEvent.VK_L);
			ButtonGroup group = new ButtonGroup();
			group.add(linkRadio);
			group.add(digRadio);

			constraint.gridy = 1;
			constraint.gridx = 0;
			constraint.gridwidth = 1;
			layout.setConstraints(linkRadio, constraint);
			temp2.add(linkRadio);

			constraint.gridy = 2;
			constraint.gridx = 0;
			layout.setConstraints(digRadio, constraint);
			temp2.add(digRadio);

			constraint.gridy = 1;
			constraint.gridx = 1;
			layout.setConstraints(linkBox, constraint);
			temp2.add(linkBox);

			if (keyLink.isSelected()) {
				int myX, myY;
				myX = temp.getX();
				myY = temp.getY();
				if (dir == 0) // north
					myY -= themap.getShortSize();
				else if (dir == 1) // east
					myX += themap.getShortSize();
				else if (dir == 2) // south
					myY += themap.getShortSize();
				else if (dir == 3) // west
					myX -= themap.getShortSize();
				else
					return;

				// go through the rooms.
				Room mRoom;
				for (int a = model.getArea().getLowVnum(); a <= model.getArea().getHighVnum(); a++) {
					mRoom = model.getArea().getRoom(a);
					if (mRoom == null)
						continue;
					if (mRoom.getX() == myX && mRoom.getY() == myY) {
						// link the two rooms.
						if (mRoom.getExit(MudExit.getReverseExit(dir)) != null) {
							System.out.println("other side has link");
							return;
						}

						MudExit newExit = new MudExit(mRoom.getVnum(), temp);
						temp.setExit(newExit, dir);
						newExit = new MudExit(temp.getVnum(), mRoom);
						mRoom.setExit(newExit, MudExit.getReverseExit(dir));
						update();
						return;
					}
				}
				if (dir == 0) // north
					myY -= themap.getShortSize();
				else if (dir == 1) // east
					myX += themap.getShortSize();
				else if (dir == 2) // south
					myY += themap.getShortSize();
				else if (dir == 3) // west
					myX -= themap.getShortSize();

				for (int a = model.getArea().getLowVnum(); a <= model.getArea().getHighVnum(); a++) {
					mRoom = model.getArea().getRoom(a);
					if (mRoom == null)
						continue;

					if (mRoom.getX() == myX && mRoom.getY() == myY) {
						// link the two rooms.
						if (mRoom.getExit(MudExit.getReverseExit(dir)) != null) {
							System.out.println("other side has link");
							return;
						}

						MudExit newExit = new MudExit(mRoom.getVnum(), temp);
						temp.setExit(newExit, dir);

						newExit = new MudExit(temp.getVnum(), mRoom);
						mRoom.setExit(newExit, MudExit.getReverseExit(dir));
						update();
						return;
					}
				}
				// go through the rooms.
			} else if (!keyDig.isSelected() && !keyLink.isSelected()) {
				int ans = JOptionPane.showConfirmDialog(null, temp2, "No exit in given direction...",
						JOptionPane.OK_CANCEL_OPTION);

				if (ans == JOptionPane.CANCEL_OPTION)
					return;
			}

			if (!keyDig.isSelected() && linkRadio.isSelected()) {
				Room t2 = (Room) linkBox.getSelectedItem();
				// we need to check if other side already has an exit
				if (t2.getExit(MudExit.getReverseExit(dir)) != null
						&& t2.getExit(MudExit.getReverseExit(dir)).getToVnum() != vnum) {
					inform("Other side has exit, cannot link.");
					return;
				}
				MudExit exit1 = new MudExit(t2.getVnum(), model.getArea().getRoom(vnum));
				MudExit exit2 = new MudExit(vnum, t2);
				model.getArea().getRoom(vnum).setExit(exit1, dir);
				t2.setExit(exit2, MudExit.getReverseExit(dir));
				moveView(dir);
			} else if (keyDig.isSelected() || digRadio.isSelected()) {
				int newVnum = model.getArea().getFreeRoomVnum();
				if (newVnum == -1) {
					inform("You are out of vnums!");
					return;
				}
				Room t2 = new Room(newVnum, model.getArea());
				model.getArea().insert(t2);
				MudExit exit1 = new MudExit(t2.getVnum(), model.getArea().getRoom(vnum));
				MudExit exit2 = new MudExit(vnum, t2);
				model.getArea().getRoom(vnum).setExit(exit1, dir);
				t2.setExit(exit2, MudExit.getReverseExit(dir));
				update();
				moveView(dir);
			}

		} else {
			texit = temp.getExit(dir);
			vnum = texit.getToVnum();
			update();
		}
	}

	public void digRoom(int dir) {
		int newVnum = model.getArea().getFreeRoomVnum();
		if (newVnum == -1) {
			inform("You are out of vnums!");
			return;
		}
		Room t2 = new Room(newVnum, model.getArea());
		model.getArea().insert(t2);
		MudExit exit1 = new MudExit(t2.getVnum(), model.getArea().getRoom(vnum));
		MudExit exit2 = new MudExit(vnum, t2);
		model.getArea().getRoom(vnum).setExit(exit1, dir);
		t2.setExit(exit2, MudExit.getReverseExit(dir));
		update();
		moveView(dir);
	}

	class MapKeyListener implements KeyListener {
		/**
		 * Handle the key typed event from the text field.
		 */
		public void keyTyped(KeyEvent e) {
			keyHandle(e);
		}

		/**
		 * Handle the key pressed event from the text field.
		 */
		public void keyPressed(KeyEvent e) {
			keyHandle(e);
		}

		/**
		 * Handle the key released event from the text field.
		 */
		public void keyReleased(KeyEvent e) {
			keyHandle(e);
		}

		public void keyHandle(KeyEvent e) {
			System.out.println("hello");
			int keyCode = e.getKeyCode();
			switch (keyCode) {
			case KeyEvent.VK_KP_DOWN:
			case KeyEvent.VK_DOWN: {
				System.out.println("Down");
				return;
			}
			default: {
				System.out.println("blah");
			}
			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		Room temp = model.getArea().getRoom(vnum);
		if (temp == null || flagchoice == null)
			return;
		temp.setFlags(flagchoice.getFlags());
		update();
	}

	class zoomListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			JRadioButton b = (JRadioButton) evt.getSource();

			if (b == z1)
				themap.update(vnum, MapView.ZOOM_1X);
			else if (b == z2)
				themap.update(vnum, MapView.ZOOM_2X);
			themap.repaint();
		}
	}

	class StringFieldFocusListener implements FocusListener {
		public void focusGained(FocusEvent e) {
		}

		public void focusLost(FocusEvent e) {
			Room room = model.getArea().getRoom(vnum);

			if (room == null)
				return;

			String newS, oldS;
			int oldVal, newVal;

			if (e.getSource() instanceof JTextArea) {
				oldS = room.getDescription();
				newS = desc.getText();
				if (newS.equals("")) {
					update();
					return;
				}
				if (newS.equalsIgnoreCase(oldS))
					return;
				else
					room.setDescription(newS);

				return;
			}

			JTextField field = (JTextField) e.getSource();

			newS = field.getText();
			if (newS.equals("")) {
				update();
				return;
			}

			if (field == fields[1]) {
				oldS = room.getName();
				if (newS.equalsIgnoreCase(oldS))
					return;
				else
					room.setName(newS);
			} else {
				try {
					newVal = Integer.parseInt(newS);

					if (field == fields[2]) {
						oldVal = room.getHeal();

						if (newVal == oldVal)
							return;
						else
							room.setHeal(newVal);
					} else if (field == fields[3]) {
						oldVal = room.getMana();

						if (newVal == oldVal)
							return;
						else
							room.setMana(newVal);
					} else if (field == fields[4]) {
						oldVal = room.getTeleport();
	
						if (newVal == oldVal)
							return;
						else
							room.setTeleport(newVal);
					} else
						return;
				} catch (Exception evt1) {
					update();
					return;
				}
			}
			update();
		}
	} // end of class

	/*
	 * Listens to the door button, popping up an edit windor for exits. this
	 * window has a radio button set for available directions, and displays the
	 * selected directions exit data, with a checkable flag set.
	 */
	class DoorListener implements ActionListener {
		JFrame myparent = null;
		private GridBagConstraints constraint;
		private GridBagLayout layout;
		JPanel panel;
		/*
		 * Components for layout
		 */
		private JComboBox keySelection;
		private JRadioButton[] exitSelection;
		private ButtonGroup group;
		private FlagChoice exitFlags;
		private boolean keys;
		String nokey;
		keySelectionListener ksListener;

		public DoorListener(Model m) {
			super();
			model = m;
			panel = new JPanel();
			nokey = "No Key";
			ksListener = new keySelectionListener();
			exitSelection = new JRadioButton[MudExit.MAX_EXITS];
			exitSelection[0] = new JRadioButton("North");
			exitSelection[1] = new JRadioButton("East");
			exitSelection[2] = new JRadioButton("South");
			exitSelection[3] = new JRadioButton("West");
			exitSelection[4] = new JRadioButton("Up");
			exitSelection[5] = new JRadioButton("Down");
			group = new ButtonGroup();
			for (int a = 0; a < 6; a++)
				group.add(exitSelection[a]);
			layout = new GridBagLayout();
			constraint = new GridBagConstraints();
			panel.setLayout(layout);
			constraint.insets = new Insets(3, 3, 3, 3);
			constraint.gridwidth = 6;
			constraint.gridheight = 1;
			constraint.gridx = 0;
			JLabel l = new JLabel("This is the door editor, you may choose a direction and then ");
			JLabel l2 = new JLabel("setup the doors/flags in that direction.");
			constraint.gridy = 0;
			layout.setConstraints(l, constraint);
			panel.add(l);
			constraint.gridy = 1;
			layout.setConstraints(l2, constraint);
			panel.add(l2);
			constraint.gridwidth = 1;
			// add radio group.
			constraint.gridy = 2;
			for (int a = 0; a < 6; a++) {
				constraint.gridx = a;
				layout.setConstraints(exitSelection[a], constraint);
				panel.add(exitSelection[a]);
				exitSelection[a].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						update();
					}
				});
			}

			keySelection = new JComboBox();
			keySelection.setBorder(new TitledBorder("Key:"));

			constraint.gridy = 3;
			constraint.gridwidth = 5;
			constraint.gridx = 0;
			layout.setConstraints(keySelection, constraint);
			panel.add(keySelection);

			exitFlags = new FlagChoice("Exit Flags", MudConstants.exitNames, MudConstants.exitFlags,
					MudConstants.NUM_EXIT_FLAGS, new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							Room temp = model.getArea().getRoom(vnum);
							if (temp == null)
								return;
							MudExit exit = temp.getExit(selectedD());
							if (exit == null)
								return;

							exit.setFlags(exitFlags.getFlags());

							Room temp2 = model.getArea().getRoom(exit.getToVnum());
							MudExit exit2 = temp2.getExit(MudExit.getReverseExit(selectedD()));
							if (exit2 == null)
								return;
							exit2.setFlags(exitFlags.getFlags());
							update();
						}
					});

			constraint.gridy = 4;
			constraint.gridwidth = 6;
			constraint.gridx = 0;
			layout.setConstraints(exitFlags, constraint);
			panel.add(exitFlags);
			exitSelection[0].setSelected(true);
			keySelection.addActionListener(ksListener);
			update();
		}

		class keySelectionListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				if (keySelection.getSelectedItem() instanceof String) {
					Room temp = model.getArea().getRoom(vnum);
					if (temp == null)
						return;
					MudExit exit = temp.getExit(selectedD());
					if (exit == null)
						return;
					exit.setKey(0);

					return;
				}

				MudObject key = (MudObject) keySelection.getSelectedItem();
				if (key == null)
					return;
				Room temp = model.getArea().getRoom(vnum);
				if (temp == null)
					return;
				MudExit exit = temp.getExit(selectedD());
				if (exit == null)
					return;
				exit.setKey(key.getVnum());
				System.out.println("Selected a key!");
			}
		}

		private int selectedD() {
			for (int a = 0; a < MudExit.MAX_EXITS; a++)
				if (exitSelection[a].isSelected())
					return a;

			return -1;
		}

		public void update() {
			keySelection.removeActionListener(ksListener);
			if (vnum == -1)
				return;

			// set it up!
			Room temp = model.getArea().getRoom(vnum);
			MudExit exit;
			int selection = -1;
			for (int a = 0; a < MudExit.MAX_EXITS; a++) {
				exit = temp.getExit(a);
				if (exit == null)
					exitSelection[a].setEnabled(false);
				else
					exitSelection[a].setEnabled(true);

				if (exitSelection[a].isSelected()) {
					if (exit != null) {
						selection = a;
						keySelection.setEnabled(true);
						exitFlags.setEnabled(true);
					} else {
						keySelection.setEnabled(false);
						exitFlags.setEnabled(false);
					}
				}
			}

			if (selection == -1)
				return;

			keySelection.removeAllItems();
			keys = false;
			for (int loop = model.getArea().getLowVnum(); loop <= model.getArea().getHighVnum(); loop++) {
				MudObject tempo = model.getArea().getObject(loop);
				if (tempo == null)
					continue;
				if (tempo.getType() == MudConstants.ITEM_KEY) {
					keySelection.addItem(tempo);
					keys = true;
				}
			}

			if (!keys) {
				exitFlags.disableFlag(MudConstants.EXIT_LOCKED);
				keySelection.setEnabled(false);
			} else {
				exitFlags.enableFlag(MudConstants.EXIT_LOCKED);
			}

			exit = temp.getExit(selection);
			exitFlags.setFlags(exit.getFlags());
			keySelection.addItem(nokey);
			keySelection.setSelectedItem(nokey);
			if (model.getArea().getObject(exit.getKey()) != null)
				keySelection.setSelectedItem(model.getArea().getObject(exit.getKey()));

			keySelection.addActionListener(ksListener);
		}

		public void actionPerformed(ActionEvent e) {
			int errornum = -1;
			Object[] options = { "CLOSE" };
			do {
				errornum = -1;
				update();
				JOptionPane.showOptionDialog(myparent, panel, "Setting Exit Flag/Doors", JOptionPane.DEFAULT_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

				/*
				 * Here, if locked flag is set, a valid key must be selected.
				 */
				MudExit temp;
				for (int a = 0; a < MudExit.MAX_EXITS; a++) {
					temp = model.getArea().getRoom(vnum).getExit(a);
					if (temp == null || temp.validLock())
						continue;
					else {
						if (temp.getKey() != 0) {
							inform("No lock set, key setting discarded.");
							temp.setKey(0);
							continue;
						}
						inform("You must select a key.");
						errornum = 1;
					}
				}
				// check for valid setup..
			} while (errornum == 1);
		}
	}
}
