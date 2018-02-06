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

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;

public class MafiaMenuItem extends JMenuItem {

	public MafiaMenuItem(String name, int mnemonic, ImageIcon icon, ActionListener listener) {
		super(name);
		setMnemonic(mnemonic);
		setIcon(icon);
		addActionListener(listener);
	}
}
