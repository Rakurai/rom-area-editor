/* George Frick
 * AreaEditor.java
 * Area Editor Project, spring 2002
 *
 * This is the main class of the Area editor, it's purpose being to set
 * up the main gui and start the components up. The program is event
 * based.
 */

package com.ageoflegacy.aedit;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.ageoflegacy.aedit.io.FileParser;
import com.ageoflegacy.aedit.model.Model;
import com.ageoflegacy.aedit.model.RomJsonWriter;
import com.ageoflegacy.aedit.model.RomWriter;
import com.ageoflegacy.aedit.model.area.AreaHeader;
import com.ageoflegacy.aedit.ui.JsonFileFilter;
import com.ageoflegacy.aedit.ui.MudFileView;
import com.ageoflegacy.aedit.ui.RomFileFilter;
import com.ageoflegacy.aedit.ui.view.EditorView;
import com.ageoflegacy.aedit.ui.view.scriptView.ScriptView;

public class AreaEditorFrame extends JFrame {

	private static final long serialVersionUID = 5924318944902029357L;

	/* Layout */
	GridBagLayout myLayout;
	GridBagConstraints constraint;

	/* Tabbed Pane */
	JTabbedPane tabbed;
	com.ageoflegacy.aedit.ui.view.overView.OverView myOverView;
	com.ageoflegacy.aedit.ui.view.roomView.RoomView myRoomView;
	com.ageoflegacy.aedit.ui.view.mobView.MobView myMobView;
	com.ageoflegacy.aedit.ui.view.objectView.ObjectView myObjectView;
	ScriptView myScriptView;
	final static int TAB_COUNT = 5;

	/* The model */
	Model model;

	/* FILE MENU */
	JMenu fileMenu; // File:
	JMenuItem fileClose; // File->close
	JMenuItem fileSave; // File->save
	JMenuItem fileSaveAs; // File->save as
	JMenu recentFileMenu; // File->Recent:
	final static int MAX_RECENT_FILES = 10;
	public final static String PREFS_FILE = "prefs.txt";
	JMenu aboutMenu;
	JFileChooser fileChooser;

	/* preferences and data */
	Map<String, JMenuItem> recentFiles;

	public AreaEditorFrame(String title, Model model) {
		super(title);
		JPanel mainEditorPanel = new JPanel();
		mainEditorPanel.setPreferredSize(new Dimension(1280, 1024));

		this.model = model;
		recentFiles = new HashMap<String, JMenuItem>();

		ClassLoader loader = ClassLoader.getSystemClassLoader();
		
		ImageIcon jMenuItemBullet = new ImageIcon(loader.getResource("bullet2.gif"));
		ImageIcon jMenuBullet = new ImageIcon(loader.getResource("bullet.gif"));
		ImageIcon jMenuAboutBullet = new ImageIcon(loader.getResource("bullet3.gif"));
		setJMenuBar(createFileMenu(jMenuBullet, jMenuItemBullet, jMenuAboutBullet));

		myOverView = new com.ageoflegacy.aedit.ui.view.overView.OverView(model);
		myRoomView = new com.ageoflegacy.aedit.ui.view.roomView.RoomView(model);
		myObjectView = new com.ageoflegacy.aedit.ui.view.objectView.ObjectView(model);
		myMobView = new com.ageoflegacy.aedit.ui.view.mobView.MobView(model);
		myScriptView = new ScriptView(model);
		myLayout = new GridBagLayout();
		constraint = new GridBagConstraints();
		mainEditorPanel.setLayout(myLayout);

		/************************************************************
		 * Create Contents *
		 ************************************************************/
		tabbed = new JTabbedPane(JTabbedPane.TOP);
		tabbed.addTab("OverView", null, myOverView, "Overview of model stats.");
		tabbed.addTab("Rooms", null, myRoomView, "Room Editor.");
		tabbed.addTab("Mobs", null, myMobView, "Mob Editor.");
		tabbed.addTab("Objects", null, myObjectView, "Object Editor.");
		tabbed.addTab("Scripts", null, myScriptView, "Script Editor.");

		tabbed.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent evt) {
				update();
			}
		});

		/************************************************************
		 * Add everything to window *
		 ************************************************************/
		constraint.gridy = 1;
		constraint.gridx = 0;
		constraint.fill = GridBagConstraints.BOTH;
		constraint.weighty = 1;
		constraint.weightx = 1;
		myLayout.setConstraints(tabbed, constraint);
		mainEditorPanel.add(tabbed);

		/************************************************************
		 * Create/Setup the window *
		 ************************************************************/
		addWindowListener(new WindowEventHandler());
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		toggleTabs(false);

		getContentPane().add(mainEditorPanel);
		JFrame.setDefaultLookAndFeelDecorated(true);
		pack();
		validate();
		setVisible(true);

		setIconImage(jMenuBullet.getImage());
		requestFocus();
		readPreferences();
	}

	/**
	 * Select the current view and update it.
	 */
	public void update() {
		EditorView view = (EditorView) tabbed.getComponentAt(tabbed.getSelectedIndex());
		view.update();
	}

	private void toggleTabs(boolean enabled) {
		for (int loop = 0; loop < TAB_COUNT; loop++) {
			tabbed.setEnabledAt(loop, enabled);
		}
		fileClose.setEnabled(enabled);
		fileSave.setEnabled(enabled);
		fileSaveAs.setEnabled(enabled);
	}

	private void leaveEditor() {
		writePreferences();
		System.exit(0);
	}

	private void readPreferences() {
		System.out.print("Loading preferences...");
		FileInputStream stream;

		try {
			stream = new FileInputStream(PREFS_FILE);
			FileParser parser = new FileParser(stream);
	
			int count = parser.readInt();
	
			for (int a = 0; a < count; a++) {
				String recentFile = parser.readToEOL();
				addRecentFile(recentFile);
				System.out.println("Added recent file: " + recentFile);
			}

			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("...Complete.");
	}

	private void addRecentFile(String toAdd) {
		File temp = new File(toAdd);

		if (!temp.canRead()) {
			System.out.println("\nFile does not exist: " + toAdd);
			return;
		}

		// Already in list, skip
		for (String s : recentFiles.keySet()) {
			if (s.equalsIgnoreCase(toAdd)) {
				return;
			}
		}
		// List is max Size, remove first element.
		if (recentFiles.size() == MAX_RECENT_FILES) {
			String ftemp = recentFiles.keySet().iterator().next();
			JMenuItem fItem = recentFiles.remove(ftemp);
			recentFileMenu.remove(fItem);
		}
		// Ok, let's add it.
		JMenuItem newitem = new JMenuItem(new File(toAdd).getName());
		recentFiles.put(toAdd, newitem);
		recentFileMenu.add(newitem);
		newitem.addActionListener(new recentFilesListener(toAdd));
	}

	private void writePreferences() {
		RomWriter writer = new RomWriter(PREFS_FILE);

		if (!writer.isOpen())
			return;

		writer.romWrite(Integer.toString(recentFiles.size()) + "\n");
		for (String str : recentFiles.keySet()) {
			writer.romWrite(str + "\n");
		}

		writer.finish();
	}

	class quitListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			leaveEditor();
		}
	}

	class aboutListener implements ActionListener {
		JFrame myparent;
		ImageIcon megadance;
		JPanel aboutPanel;

		public aboutListener(JFrame p) {
			super();
			myparent = p;
			ClassLoader loader = ClassLoader.getSystemClassLoader();
			megadance = new ImageIcon(loader.getResource("megadance.gif"));
			aboutPanel = new JPanel();
			JLabel msg = new JLabel(
					"<HTML><BODY><BOLD><HR>Thank you for using the MAFIA model editor!</BOLD><BR> This editor was designed and created by <A HREF=mailto:'tenchi@s5games.net'>George Frick</A><P>GUI design by George Frick and Scott Emerson of <A HREF='http://www.s5games.net'>CaffeineGamez</A></P><HR></BODY></HTML><HTML><BODY><BOLD>Special thanks to all beta testers and players of Animud.</BOLD></BODY></HTML>");
			aboutPanel.add(msg);
			aboutPanel.add(new JLabel(megadance));

		}

		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(myparent, aboutPanel, "About the MAFIA editor", JOptionPane.PLAIN_MESSAGE);
		}
	}

	class WindowEventHandler extends WindowAdapter {
		public void windowClosing(WindowEvent evt) {
			leaveEditor();
		}
	}

	class openListener implements ActionListener {
		JFrame myparent;

		public openListener(JFrame p) {
			super();
			myparent = p;
		}

		public void actionPerformed(ActionEvent a) {
			fileChooser = new JFileChooser();
			fileChooser.setAcceptAllFileFilterUsed(false);
			fileChooser.addChoosableFileFilter(new RomFileFilter());
			fileChooser.setFileView(new MudFileView());
			int selected = fileChooser.showOpenDialog(myparent.getContentPane());

			if (selected == JFileChooser.APPROVE_OPTION) {
				try {
					openFile(fileChooser.getSelectedFile());
					fileSave.setEnabled(true);
					fileSaveAs.setEnabled(true);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (selected == JFileChooser.CANCEL_OPTION) {
				System.out.println("Canceled Load");
			}
		}
	}

	class saveAsListener implements ActionListener {
		JFrame myparent;

		public saveAsListener(JFrame p) {
			super();
			myparent = p;
		}

		public void actionPerformed(ActionEvent a) {
			fileChooser = new JFileChooser();
			fileChooser.setAcceptAllFileFilterUsed(false);
			fileChooser.addChoosableFileFilter(new RomFileFilter());
			fileChooser.addChoosableFileFilter(new JsonFileFilter());
			fileChooser.setFileView(new MudFileView());
			try {
				File ft = new File(new File(model.getArea().getFileName()).getCanonicalPath());
				fileChooser.setSelectedFile(ft);
			} catch (Exception fError) {
			}

			int selected = fileChooser.showSaveDialog(myparent.getContentPane());

			if (selected == JFileChooser.APPROVE_OPTION) {
				if (fileChooser.getFileFilter().getDescription().equals(JsonFileFilter.DESCRIPTION)) {
					RomJsonWriter writer = new RomJsonWriter(fileChooser.getSelectedFile());
					writer.writeArea(model.getArea());
				} else if (fileChooser.getFileFilter().getDescription().equals(RomFileFilter.DESCRIPTION)) {
					RomWriter writer = new RomWriter(fileChooser.getSelectedFile());
					writer.writeArea(model.getArea());
				}
				model.getArea().setPathName(fileChooser.getSelectedFile().getPath());
				fileSave.setEnabled(true);
				addRecentFile(fileChooser.getSelectedFile().getAbsolutePath());
			} else if (selected == JFileChooser.CANCEL_OPTION) {
				System.out.println("Canceled Save");
			}
		}
	}

	class saveListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String fileName1 = model.getArea().getPathName();
			if (fileName1 == null) {
				fileSave.setEnabled(false);
				return;
			}
			RomWriter writer = new RomWriter(fileName1);
			writer.writeArea(model.getArea());
		}
	}

	private void openFile(File toOpen) throws FileNotFoundException, IOException {
		model.loadArea(toOpen);

		update();
		toggleTabs(true);
		addRecentFile(toOpen.getAbsolutePath());
	}

	class recentFilesListener implements ActionListener {
		private String fullFileName;

		public recentFilesListener(String toAdd) {
			fullFileName = toAdd;
		}

		public void actionPerformed(ActionEvent e) {
			System.out.println("Opening recent file..." + fullFileName);
			try {
				openFile(new File(fullFileName));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	class closeListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			closeArea();
			tabbed.setSelectedIndex(0);
		}
	}

	private void closeArea() {
		model.getArea().clear();
		update();
		toggleTabs(false);
	}

	class newListener implements ActionListener {
		NewAreaPanel newAreaPanel;

		public newListener() {
			super();
			newAreaPanel = new NewAreaPanel();
		}

		public void actionPerformed(ActionEvent e) {
			AreaHeader header = newAreaPanel.getNewArea();
			if (header != null) {
				closeArea();
				model.getArea().setHeader(header);
				toggleTabs(true);
				update();
			}
		}
	}

	private JMenuBar createFileMenu(ImageIcon jMenuIcon, ImageIcon jMenuItemIcon, ImageIcon jMenuAboutIcon) {
		// Create a File menu and add it to the program.
		JMenuBar topBar = new JMenuBar();
		topBar.setBorder(new BevelBorder(BevelBorder.RAISED));

		// File Menu
		fileMenu = new JMenu("File", true);
		fileMenu.setIcon(jMenuIcon);
		fileMenu.setMnemonic('F');

		// Close, Save, Save As
		fileClose = new MafiaMenuItem("Close", 'C', jMenuItemIcon, new closeListener());
		fileSave = new MafiaMenuItem("Save", 'S', jMenuItemIcon, new saveListener());
		fileSaveAs = new MafiaMenuItem("Save As...", 'X', jMenuItemIcon, new saveAsListener(this));

		// recent files.
		recentFileMenu = new JMenu("Open Recent", true);
		recentFileMenu.setIcon(jMenuItemIcon);

		// Add them all to the File menu
		fileMenu.add(new MafiaMenuItem("New", 'N', jMenuItemIcon, new newListener())); // File->
																						// New,
																						// Open,
																						// Convert,
																						// Quit
		fileMenu.add(new MafiaMenuItem("Open", 'O', jMenuItemIcon, new openListener(this)));
		fileMenu.add(fileClose);
		fileMenu.add(fileSave);
		fileMenu.add(fileSaveAs);
		fileMenu.add(new MafiaMenuItem("Quit", 'Q', jMenuItemIcon, new quitListener()));
		fileMenu.addSeparator();
		fileMenu.add(recentFileMenu);
		// readPreferences();

		// About Menu
		aboutMenu = new JMenu("About");
		aboutMenu.setIcon(jMenuAboutIcon);
		aboutMenu.setMnemonic('A');
		aboutMenu.add(new MafiaMenuItem("About", 'B', jMenuItemIcon, new aboutListener(this)));

		// Add everything to layout
		topBar.add(fileMenu);
		topBar.add(aboutMenu);
		return topBar;
	}

}
