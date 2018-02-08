package com.ageoflegacy.aedit.ui.view.scriptView;

import com.ageoflegacy.aedit.ui.view.EditorView;
import com.ageoflegacy.aedit.model.Model;
import com.ageoflegacy.aedit.model.Room;
import com.ageoflegacy.aedit.model.area.Area;
import com.ageoflegacy.aedit.model.area.Mobile;
import com.ageoflegacy.aedit.model.area.MobileProgram;
import com.ageoflegacy.aedit.beans.scanning.SyntaxHighlighter;
import com.ageoflegacy.aedit.beans.scanning.Scanner;
import com.ageoflegacy.aedit.beans.scanning.LuaScanner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA. User: Administrator Date: Feb 21, 2009 Time:
 * 12:36:01 PM To change this template use File | Settings | File Templates.
 */
public class ScriptView extends EditorView {

	private JComboBox vnumBox;
	private JButton newButton;
	private JButton deleteButton;
	private JButton backButton;
	private JButton nextButton;
	private Box buttonPanel;

	SyntaxHighlighter text;

	public ScriptView(Model m) {
		super(m);

//		Scanner scanner = new LuaScanner();
//		text = new SyntaxHighlighter(24, 80, scanner);
		JScrollPane scroller = new JScrollPane(text);
		scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		createNav();
		setLayout(new BorderLayout());
		add(buttonPanel, BorderLayout.NORTH);
		add(scroller, BorderLayout.CENTER);

		addListeners();
	}

	private void addListeners() {
		newButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Get the lowest available room vnum
				int vnum = model.getArea().getFreeScriptVnum();
				if (vnum == -1) {
					inform("You are out of vnums!");
					return;
				}
				MobileProgram temp = new MobileProgram();
				model.getArea().insert(temp);
				update(vnum);
			}
		});

		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteScript();
			}
		});

		backButton.addActionListener(new ActionListener() {
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

		vnumBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Mobile temp = (Mobile) vnumBox.getSelectedItem();
				if (temp == null)
					return;

				vnum = temp.getVnum();
				update();
			}
		});
	}

	private void deleteScript() {
	}

	private void createNav() {
		vnumBox = model.getArea().getVnumCombo("mob");
		newButton = new JButton("New");
		deleteButton = new JButton("Delete");
		backButton = new JButton("Back");
		nextButton = new JButton("Next");
		buttonPanel = Box.createHorizontalBox();
		buttonPanel.add(vnumBox);
		buttonPanel.add(backButton);
		buttonPanel.add(nextButton);
		buttonPanel.add(newButton);
		buttonPanel.add(deleteButton);
	}

	public void update(int newVnum) {
		vnum = newVnum;
		update();
	}

	public void update() {
		if (model.getArea().getMobCount() > 0) {
			if (vnum <= 0 || model.getArea().getMobile(vnum) == null) {
				vnum = model.getArea().getFirstMobVnum();
			}
			vnumBox.setSelectedItem(model.getArea().getMobile(vnum));
			MobileProgram temp = ((Mobile) vnumBox.getSelectedItem()).getMobProg();
			text.setText(temp.getProgram());
		} else {
			text.setText("");
		}
	}

}
