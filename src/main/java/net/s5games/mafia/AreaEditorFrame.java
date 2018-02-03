/* George Frick
 * AreaEditor.java
 * Area Editor Project, spring 2002
 *
 * This is the main class of the Area editor, it's purpose being to set
 * up the main gui and start the components up. The program is event
 * based.
 */

package net.s5games.mafia;

import net.s5games.mafia.model.*;
import net.s5games.mafia.ui.*;
import net.s5games.mafia.ui.view.scriptView.ScriptView;
import net.s5games.mafia.ui.view.EditorView;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.net.URL;
import java.util.*;

public class AreaEditorFrame extends JFrame {

	private static final long serialVersionUID = 5924318944902029357L;

    /* Layout */
    GridBagLayout myLayout;
    GridBagConstraints constraint;

    /* Tabbed Pane */
    JTabbedPane tabbed;
    net.s5games.mafia.ui.view.overView.OverView myOverView;
    net.s5games.mafia.ui.view.roomView.RoomView myRoomView;
    net.s5games.mafia.ui.view.mobView.MobView myMobView;
    net.s5games.mafia.ui.view.objectView.ObjectView myObjectView;
    ScriptView myScriptView;
    final static int TAB_COUNT = 5;

    /* The model */
    Area theArea;

    /* FILE MENU */
    JMenu fileMenu;        // File:
    JMenuItem fileClose;   // File->close
    JMenuItem fileSave;    // File->save
    JMenuItem fileSaveAs;  // File->save as 
    JMenu recentFileMenu;  // File->Recent:
    final static int MAX_RECENT_FILES = 10;
    public final static String PREFS_FILE = "prefs.txt";
    JMenu aboutMenu;
    JFileChooser fileChooser;

    /* preferences and data */
    Map<String,JMenuItem> recentFiles;

    public AreaEditorFrame(String title) {
        super(title);
        JPanel mainEditorPanel = new JPanel();
        mainEditorPanel.setPreferredSize(new Dimension(1280,1024));

        recentFiles = new HashMap<String,JMenuItem>();

        ClassLoader loader = ClassLoader.getSystemClassLoader();
        ImageIcon jMenuItemBullet = new ImageIcon(loader.getResource("image/bullet2.gif"));
        ImageIcon jMenuBullet = new ImageIcon(loader.getResource("image/bullet.gif"));
        ImageIcon jMenuAboutBullet = new ImageIcon(loader.getResource("image/bullet3.gif"));
        setJMenuBar(createFileMenu(jMenuBullet, jMenuItemBullet, jMenuAboutBullet));
        
        theArea = new Area();
        myOverView = new net.s5games.mafia.ui.view.overView.OverView(theArea);
        myRoomView = new net.s5games.mafia.ui.view.roomView.RoomView(theArea);
        myObjectView = new net.s5games.mafia.ui.view.objectView.ObjectView(theArea);
        myMobView = new net.s5games.mafia.ui.view.mobView.MobView(theArea);
        myScriptView = new ScriptView(theArea);
        myLayout = new GridBagLayout();
        constraint = new GridBagConstraints();
        mainEditorPanel.setLayout(myLayout);

        /************************************************************
         *	Create Contents				                          *
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
         *	Add everything to window		                      *
         ************************************************************/
        constraint.gridy = 1;
        constraint.gridx = 0;
        constraint.fill = GridBagConstraints.BOTH;
        constraint.weighty = 1;
        constraint.weightx = 1;
        myLayout.setConstraints(tabbed, constraint);
        mainEditorPanel.add(tabbed);

        /************************************************************
         *	Create/Setup the window			                      *
         ************************************************************/
        addWindowListener(new WindowEventHandler());
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        toggleTabs(false);

        getContentPane().add( mainEditorPanel );
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
        try {
            int count;
            String temp;
            RomLoader myLoader = new RomLoader(PREFS_FILE);
            if (!myLoader.isOpen()) {
                System.out.println("No preferences file, creating as needed.");
                return;
            }

            temp = myLoader.getLine();
            count = Integer.parseInt(temp);

            for (int a = 0; a < count; a++) {
                String recentFile = myLoader.getLine();
                addRecentFile(recentFile);
                System.out.println("Added recent file: " + recentFile);
            }

            System.out.println("...Complete.");
        }
        catch (Exception exc) {
            System.out.println("...Aborted, error in preference file.");
        }
    }

    private void addRecentFile(String toAdd) {
        File temp = new File(toAdd);
        
        if (!temp.canRead()) {
            System.out.println("\nFile does not exist: " + toAdd);
            return;
        }

        // Already in list, skip
        for( String s : recentFiles.keySet()) {
            if( s.equalsIgnoreCase(toAdd)) {
                return;
            }
        }
        // List is max Size, remove first element.
        if( recentFiles.size() == MAX_RECENT_FILES) {
            String ftemp = recentFiles.keySet().iterator().next();
            JMenuItem fItem = recentFiles.remove(ftemp);
            recentFileMenu.remove(fItem);
        }
        // Ok, let's add it.
        JMenuItem newitem = new JMenuItem(new File(toAdd).getName());
        recentFiles.put(toAdd,newitem);
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
            megadance = new ImageIcon(loader.getResource("image/megadance.gif"));
            aboutPanel = new JPanel();
            JLabel msg = new JLabel("<HTML><BODY><BOLD><HR>Thank you for using the MAFIA model editor!</BOLD><BR> This editor was designed and created by <A HREF=mailto:'tenchi@s5games.net'>George Frick</A><P>GUI design by George Frick and Scott Emerson of <A HREF='http://www.s5games.net'>CaffeineGamez</A></P><HR></BODY></HTML><HTML><BODY><BOLD>Special thanks to all beta testers and players of Animud.</BOLD></BODY></HTML>");
            aboutPanel.add(msg);
            aboutPanel.add(new JLabel(megadance));

        }

        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(myparent, aboutPanel, "About the MAFIA editor",
                    JOptionPane.PLAIN_MESSAGE);
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
                openFile(fileChooser.getSelectedFile());
                fileSave.setEnabled(true);
                fileSaveAs.setEnabled(true);                  
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
                File ft = new File(new File(theArea.getFileName()).getCanonicalPath());
                fileChooser.setSelectedFile(ft);
            } catch (Exception fError) {
            }

            int selected = fileChooser.showSaveDialog(myparent.getContentPane());

            if (selected == JFileChooser.APPROVE_OPTION) {
                if( fileChooser.getFileFilter().getDescription().equals(JsonFileFilter.DESCRIPTION)) {
                    RomJsonWriter writer = new RomJsonWriter(fileChooser.getSelectedFile());
                    writer.writeArea(theArea);
                } else if( fileChooser.getFileFilter().getDescription().equals(RomFileFilter.DESCRIPTION)) {
                    RomWriter writer = new RomWriter(fileChooser.getSelectedFile());
                    writer.writeArea(theArea);
                }
                theArea.setPathName(fileChooser.getSelectedFile().getPath());
                fileSave.setEnabled(true);                  
                addRecentFile(fileChooser.getSelectedFile().getAbsolutePath());
            } else if (selected == JFileChooser.CANCEL_OPTION) {
                System.out.println("Canceled Save");
            }
        }
    }

    class saveListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String fileName1 = theArea.getPathName();
            if (fileName1 == null) {
                fileSave.setEnabled(false);              
                return;
            }
            RomWriter writer = new RomWriter(fileName1);
            writer.writeArea(theArea);
        }
    }

    private void openFile(File toOpen) {
        theArea.clear();
        RomLoader loader = new RomLoader(toOpen);
        theArea = loader.readArea(theArea);
        theArea.transformResets();
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
              openFile(new File(fullFileName));
        }
    }

    class closeListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            closeArea();
            tabbed.setSelectedIndex(0);
        }
    }

    private void closeArea() {
        theArea.clear();
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
            if( header != null ) {
                closeArea();
                theArea.setHeader(header);
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
        fileMenu.add(new MafiaMenuItem("New", 'N', jMenuItemIcon, new newListener())); // File-> New, Open, Convert, Quit
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
