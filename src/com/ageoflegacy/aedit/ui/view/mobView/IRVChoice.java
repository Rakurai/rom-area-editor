/**
 * @author George Frick
 * FlagChoice.java
 * Area Editor Project, Fall 2002
 *
 * This is a class that allows you to create a bordered and titled panel
 * with a custom set of check boxes.
 * This class uses constants from
 * MudConstants.java for its letter/bit value matches.
 * The bit set can be retrieved as an integer.
 * 1. The name of each check box and its associated bit value(bit flag)
 *    is provided during creation of the check box.
 * 2. The title for the border is provided during creation
 * 3. The number of check boxes is provided during creation.
 * 4. All values can be set/retrieved by accessors.
 */
package com.ageoflegacy.aedit.ui.view.mobView;

import com.ageoflegacy.aedit.ui.SpringUtilities;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class IRVChoice extends JPanel {
    SpringLayout layout;
    private JCheckBox[] choices;
    private JCheckBox[] choices2;
    private JCheckBox[] choices3;
    private String[] flags; // labels
    private int[] fileData; // what gets written to file.
    private int arraySize; // how many
    private int arrayWidth = 3;
    private int columns = 5;
    private ActionListener myParent;

    /**
     * Base constructor, builds layout.
     */
    private IRVChoice(String title) {
        super(true);
        if (title != null)
            setBorder(new TitledBorder(title));
        layout = new SpringLayout();
        setLayout(layout);
    }

    /**
     * Title shows up on border, c is size of names and data
     *
     * @param title Title for the border
     * @param name  array of names to set as labels
     * @param data  array of flag values each name represents
     * @param c     the size of names/data<BOLD>(SHOULD BE THE SAME!)</BOLD>
     */
    public IRVChoice(String title, String[] names, int[] data1, int c, ActionListener parent) {
        this(title);
        flags = names;        // Assign the array of names.
        fileData = data1;      // Assign the array of data.
        arraySize = c;            // Assign size of array.
        myParent = parent;
        setupLayout();
    }

    public IRVChoice(String title, String[] names, int[] data, int c, ActionListener parent, int cols) {
        this(title);
        columns = cols;
        flags = names;        // Assign the array of names.
        fileData = data;      // Assign the array of data.
        arraySize = c;            // Assign size of array.
        myParent = parent;
        setupLayout();
    }

    private void setupLayout() {
        try {
            // Start creating
            choices = new JCheckBox[arraySize];
            choices2 = new JCheckBox[arraySize];
            choices3 = new JCheckBox[arraySize];

            // Lay them out on the panel.
            for (int a = 0; a < arraySize; a++) {
                choices[a] = new JCheckBox();
                choices2[a] = new JCheckBox();
                choices3[a] = new JCheckBox();
                JPanel choicePanel = new JPanel();
                choicePanel.setLayout(new SpringLayout());
                choicePanel.add(choices[a]);
                choicePanel.add(choices2[a]);
                choicePanel.add(choices3[a]);
                choicePanel.add(new JLabel(flags[a]));
                SpringUtilities.makeCompactGrid(choicePanel, 1,
                        choicePanel.getComponentCount(), 0, 0, 0, 0);

                choices[a].setSelected(false); // Just in case.

                add(choicePanel);
                choices[a].addActionListener(new flagListener());
                choices[a].addActionListener(myParent);
                choices2[a].addActionListener(new flagListener());
                choices2[a].addActionListener(myParent);
                choices3[a].addActionListener(new flagListener());
                choices3[a].addActionListener(myParent);
            }
            int temp = arraySize % columns;
            while (temp > 0) {
                this.add(new JLabel(""));
                temp--;
            }
            SpringUtilities.makeCompactGrid(this, getComponentCount() / columns, columns, 1, 0, 1, 0);

        }
        catch (IndexOutOfBoundsException e) {
            System.out.println("Could not create flagchoice.");
            System.exit(0);
        }

    }

    public void setEnabled(boolean flag) {
        for (int a = 0; a < arraySize; a++) {
            choices[a].setEnabled(flag);
            choices2[a].setEnabled(flag);
            choices3[a].setEnabled(flag);
        }
    }

    /**
     * Set the flags, must provide a full bit set, not an bit flag.
     */
    public void setFlags(int newFlags, int which) {
        if (newFlags < 0) {
            System.out.println("!STOP! tried setting invalid flag set in FlagChoice!");
            return;
        }

        if (which == 0) {
            for (int a = 0; a < arraySize; a++) {
                if ((newFlags & fileData[a]) == fileData[a]) {
                    choices[a].setSelected(true);
                    choices2[a].setSelected(false);
                    choices3[a].setSelected(false);
                } else {
                    choices[a].setSelected(false);
                }
            }
        } else if (which == 1) {
            for (int a = 0; a < arraySize; a++) {
                if ((newFlags & fileData[a]) == fileData[a]) {
                    choices2[a].setSelected(true);
                    choices3[a].setSelected(false);
                    choices[a].setSelected(false);
                } else
                    choices2[a].setSelected(false);
            }
        } else if (which == 2) {
            for (int a = 0; a < arraySize; a++) {
                if ((newFlags & fileData[a]) == fileData[a]) {
                    choices3[a].setSelected(true);
                    choices2[a].setSelected(false);
                    choices[a].setSelected(false);
                } else
                    choices3[a].setSelected(false);
            }
        }

        return;
    }

    /**
     * Set a flag must provide a bit flag, not a bit set.
     *
     * @Must provide boolean, true = add bit, false = remove bit
     */
    public void setBit(int newBit, boolean on, int which) {
        if (which == 0) {
            for (int a = 0; a < arraySize; a++) {
                if (newBit == fileData[a]) {
                    choices[a].setSelected(on);
                    choices2[a].setSelected(!on);
                    choices3[a].setSelected(!on);
                }
            }
        } else if (which == 1) {
            for (int a = 0; a < arraySize; a++) {
                if (newBit == fileData[a]) {
                    choices2[a].setSelected(on);
                    choices3[a].setSelected(!on);
                    choices[a].setSelected(!on);
                }
            }
        } else if (which == 2) {
            for (int a = 0; a < arraySize; a++) {
                if (newBit == fileData[a]) {
                    choices3[a].setSelected(on);
                    choices2[a].setSelected(!on);
                    choices[a].setSelected(!on);
                }
            }
        }

    }

    /**
     * Retrieves full bit set(flags).
     */
    public int getFlags(int which) {
        int total = 0;

        if (which == 0) {
            for (int a = 0; a < arraySize; a++) {
                if (choices[a].isSelected())
                    total += fileData[a];
            }
        } else if (which == 1) {
            for (int a = 0; a < arraySize; a++) {
                if (choices2[a].isSelected())
                    total += fileData[a];
            }
        } else if (which == 2) {
            for (int a = 0; a < arraySize; a++) {
                if (choices3[a].isSelected())
                    total += fileData[a];
            }
        }

        return total;
    }

    /**
     * Given an integer bit flags, looks up the matching item
     * in the classes array of flags.
     */
    private int miniLookup(int flag) {
        for (int temp = 0; temp < arraySize; temp++) {
            if (fileData[temp] == flag)
                return temp;
        }

        return -1;
    }

    /**
     * Disable a certain check box in the layout.
     * - flag is an integer bit flag.
     */
    public void disableFlag(int flag) {
        try {
            choices[miniLookup(flag)].setEnabled(false);
            choices2[miniLookup(flag)].setEnabled(false);
            choices3[miniLookup(flag)].setEnabled(false);
        }
        catch (IndexOutOfBoundsException e) {
            System.out.println("Attempt to set bad flag in FlagChoice!");
        }
    }

    /**
     * Enable a certain check box in the layout.
     * - flag is an integer bit flag.
     */
    public void enableFlag(int flag) {
        try {
            choices[miniLookup(flag)].setEnabled(true);
            choices2[miniLookup(flag)].setEnabled(true);
            choices3[miniLookup(flag)].setEnabled(true);
        }
        catch (IndexOutOfBoundsException e) {
            System.out.println("Attempt to set bad flag in FlagChoice!");
        }
    }

    // Empty for now, can fill as needed.
    class flagListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            for (int temp = 0; temp < arraySize; temp++) {
                if (choices[temp] == (JCheckBox) e.getSource()) {
                    choices[temp].setSelected(true);
                    choices2[temp].setSelected(false);
                    choices3[temp].setSelected(false);
                } else if (choices2[temp] == (JCheckBox) e.getSource()) {
                    choices[temp].setSelected(false);
                    choices2[temp].setSelected(true);
                    choices3[temp].setSelected(false);
                } else if (choices3[temp] == (JCheckBox) e.getSource()) {
                    choices[temp].setSelected(false);
                    choices2[temp].setSelected(false);
                    choices3[temp].setSelected(true);
                }

            }
            // prints current to prompt.
            // System.out.println(Integer.toString(getFlags()) );
            // System.out.println(MudConstants.getBitString(getFlags()) );
            // System.out.println(Integer.toString(MudConstants.getBitInt(MudConstants.getBitString(getFlags())) ));
        }
    }

}
