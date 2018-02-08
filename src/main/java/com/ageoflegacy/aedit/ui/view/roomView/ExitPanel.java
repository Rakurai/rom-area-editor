// George Frick
// ExitPanel.java
// Area Editor Project, Spring 2002
//
// This is a special purpose JPanel for use in the GUI of the model editor's
// room tab. It listens for the
// given buttons and attempts to load the room in that direction(or create)
// into the room tab.
package com.ageoflegacy.aedit.ui.view.roomView;

import com.ageoflegacy.aedit.ui.SpringUtilities;
import com.ageoflegacy.aedit.io.ResourceLoader;
import com.ageoflegacy.aedit.model.Room;
import com.ageoflegacy.aedit.model.area.Area;
import com.ageoflegacy.aedit.model.area.MudExit;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class ExitPanel extends JPanel {
	private URL[] dirUrls;
	JButton[] directions;
	JComboBox[] targets;
	exitButton[] exits;
	String[] names = { "North", "Up", "West", "East", "Down", "South" };
	String[] icons = { "north2.gif", "east2.gif", "south2.gif", "west2.gif", "up2.gif", "down2.gif" };
	String[] exitIcons = { "noexit.gif", "links.gif", "goes.gif" };
	RoomView parent;
	MudExit[] data;
	int lowVnum, highVnum;
	Area theArea;
	int vnum;

	public ExitPanel(RoomView roomView, Area area) {
		super(true);
		vnum = -1;
		dirUrls = new URL[6];
		parent = roomView;
		lowVnum = area.getLowVnum();
		highVnum = area.getHighVnum();
		theArea = area;
		data = new MudExit[MudExit.MAX_EXITS];
		directions = new JButton[MudExit.MAX_EXITS];
		exits = new exitButton[MudExit.MAX_EXITS];
		targets = new JComboBox[MudExit.MAX_EXITS];

		// Layout
		setBorder(new TitledBorder("Exit Manager"));
		setLayout(new SpringLayout());

		for (int a = MudExit.DIR_NORTH; a < MudExit.MAX_EXITS; a++) {
			dirUrls[a] = ResourceLoader.getURL("image/" + icons[a]);
			ImageIcon tIcon = new ImageIcon(dirUrls[a]);
			directions[a] = new JButton(tIcon);
			directions[a].setMargin(new Insets(0, 0, 0, 0));
			targets[a] = theArea.getVnumCombo("room");
			targets[a].setPrototypeDisplayValue("01234567890123456789");
			targets[a].setMaximumRowCount(10);
			exits[a] = new exitButton(this, a, targets[a]);
		}

		// North
		add(directions[MudExit.DIR_NORTH]);
		add(exits[MudExit.DIR_NORTH]);
		add(targets[MudExit.DIR_NORTH]);

		// Up
		add(directions[MudExit.DIR_UP]);
		add(exits[MudExit.DIR_UP]);
		add(targets[MudExit.DIR_UP]);

		// West
		add(directions[MudExit.DIR_WEST]);
		add(exits[MudExit.DIR_WEST]);
		add(targets[MudExit.DIR_WEST]);

		// East
		add(directions[MudExit.DIR_EAST]);
		add(exits[MudExit.DIR_EAST]);
		add(targets[MudExit.DIR_EAST]);

		// Down
		add(directions[MudExit.DIR_DOWN]);
		add(exits[MudExit.DIR_DOWN]);
		add(targets[MudExit.DIR_DOWN]);

		// South
		add(directions[MudExit.DIR_SOUTH]);
		add(exits[MudExit.DIR_SOUTH]);
		add(targets[MudExit.DIR_SOUTH]);

		SpringUtilities.makeCompactGrid(this, 3, 6, 0, 0, 0, 0);

		for (int a = MudExit.DIR_NORTH; a < MudExit.MAX_EXITS; a++) {
			directions[a].addActionListener(new directionListener());
		}
	}

	public void update() {
		if (vnum == -1)
			return;

		Room temp = theArea.getRoom(vnum);
		for (int a = 0; a < MudExit.MAX_EXITS; a++) {
			data[a] = temp.getExit(a);
			if (data[a] != null) {
				targets[a].setSelectedItem(theArea.getRoom(data[a].getToVnum()));
				// targets[a].setPopupVisible(false);
				/* Check linked room for exit coming back... */
				Room t2 = theArea.getRoom(data[a].getToVnum());
				int d = MudExit.getReverseExit(a);
				if (t2.getExit(d) != null && t2.getExit(d).getToVnum() == vnum)
					exits[a].setState(2);
				else
					exits[a].setState(1);
			} else {
				targets[a].setSelectedIndex(-1);
				// targets[a].setPopupVisible(true);
				exits[a].setState(0);
			}
		}
	}

	public void setEnabled(boolean value) {
		for (int a = 0; a < MudExit.MAX_EXITS; a++) {
			targets[a].setEnabled(value);
			exits[a].setEnabled(value);
			directions[a].setEnabled(value);
		}
	}

	public void setVnum(int newVnum) {
		if (newVnum == vnum)
			return;
		else
			reset();

		vnum = newVnum;
		update();
	}

	public void reset() {
		vnum = -1;
		update();
	}

	private void deleteOpposingExit(int d) {
		if (data[d] == null)
			return;

		Room t2 = theArea.getRoom(data[d].getToVnum());

		if (t2 == null)
			return;

		if (t2.getExit(MudExit.getReverseExit(d)).getToVnum() == vnum)
			t2.setExit(null, MudExit.getReverseExit(d));
	}

	private void deleteCurrentExit(int d) {
		Room t2 = theArea.getRoom(vnum);

		if (t2 == null)
			return;

		t2.setExit(null, d);
	}

	/*
	 * For these two functions you are inputing what direction in the array to
	 * take the data from..
	 */
	private boolean createCurrentExit(int d) {
		Room temp;
		int v;

		if (targets[d].getSelectedItem() == null) {
			inform("No target vnum selected!");
			return false;
		}

		temp = (Room) targets[d].getSelectedItem();
		if (temp == null) {
			System.out.println("!!!Could not create exit!!!");
			return false;
		}
		MudExit newExit;
		newExit = new MudExit(temp.getVnum(), theArea.getRoom(vnum));
		theArea.getRoom(vnum).setExit(newExit, d);
		data[d] = newExit;
		return true;
	}

	private boolean createOpposingExit(int d) {
		Room temp;
		int dir = MudExit.getReverseExit(d);

		if (targets[d].getSelectedItem() == null) {
			inform("No target vnum selected!");
			return false;
		}

		temp = (Room) targets[d].getSelectedItem();
		if (temp == null) {
			System.out.println("!!!Could not create exit!!!");
			return false;
		}

		MudExit newExit = new MudExit(vnum, temp);
		temp.setExit(newExit, dir);

		return true;
	}

	private void resetComboBox(int d) {
		if (targets[d] == null)
			return;

		targets[d].setSelectedIndex(-1);
		// targets[d].setPopupVisible(true);
	}

	class directionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			for (int a = 0; a < 6; a++) {
				if (e.getSource() == directions[a])
					parent.moveView(a);
			}
		}
	}

	private void inform(String msg) {
		JOptionPane.showMessageDialog(null, msg, "Error:", JOptionPane.WARNING_MESSAGE);
	}

	class exitButton extends JButton {
		String[] icons = { "noexit.gif", "goes.gif", "links.gif" };
		ImageIcon[] images;
		int state;
		public final int NOEXIT = 0;
		public final int GOES = 1;
		public final int LINKS = 2;
		private ExitPanel parent;
		private int direction; // the direction this exit represents.
		private JComboBox target;

		public exitButton(ExitPanel e, int d, JComboBox t) {
			super(new ImageIcon(ResourceLoader.getURL("image/noexit.gif")));
			parent = e;
			direction = d;
			target = t;
			setMargin(new Insets(0, 0, 0, 0));
			images = new ImageIcon[3];
			images[NOEXIT] = new ImageIcon(ResourceLoader.getURL("image/" + icons[NOEXIT]));
			images[LINKS] = new ImageIcon(ResourceLoader.getURL("image/" + icons[LINKS]));
			images[GOES] = new ImageIcon(ResourceLoader.getURL("image/" + icons[GOES]));
			setState(NOEXIT);
			this.addActionListener(new exitLinkListener());
		}

		public void setState(int newState) {
			state = newState;
			setIcon(images[state]);
			if (newState == NOEXIT)
				target.setEnabled(true);
			else
				target.setEnabled(false);
		}

		public void moveState(int newState) {
			// process change.
			// 1. Can't change from no-exit if a vnum is not chosen.
			// 2. confirm change from or to one-way
			// 3. change to one-way -> delete other rooms exit
			// 4. change to two-way -> create other rooms exit
			// 5. change to no-exit -> delete both exits and unchoose vnum.
			if (newState == GOES) {
				if (state == LINKS) // can't happen ?
				{
					parent.deleteOpposingExit(direction);
				} else if (state == NOEXIT) {
					if (parent.createCurrentExit(direction) == false)
						return;
				} else
					return;
				target.setEnabled(false);
			} else if (newState == LINKS) {
				if (state == GOES) {
					if (parent.createOpposingExit(direction) == false)
						return;
				} else if (state == NOEXIT) // can't happen ?
				{
					if (parent.createCurrentExit(direction) == false || parent.createOpposingExit(direction) == false)
						return;
				} else
					return;
				target.setEnabled(false);
			} else if (newState == NOEXIT) {
				parent.deleteOpposingExit(direction);
				parent.deleteCurrentExit(direction);
				parent.resetComboBox(direction);
				target.setEnabled(true);
			} else // can't happen ?
			{
				System.out.println("Major error in ExitPanel.ExitButton.MoveState(int)");
				System.exit(0);
			}

			state = newState;
			setIcon(images[state]);
			parent.update();
		}

		public int getState() {
			return state;
		}

		class exitLinkListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				if (state == LINKS)
					moveState(NOEXIT);
				else
					moveState(getState() + 1);
			}
		}

	}
}
