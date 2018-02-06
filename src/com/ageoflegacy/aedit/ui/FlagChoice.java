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
package com.ageoflegacy.aedit.ui;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.event.ActionListener;

public class FlagChoice extends JPanel {
    private JCheckBox[] choices;
    private String[] flags; // labels
    private int[] fileData; // what gets written to file.
    private int arraySize; // how many
    private int columns = 5;
    private ActionListener myParent;

    /**
     * Base constructor, builds layout.
     */
    private FlagChoice(String title) {
        super(true);
        setLayout(new MigLayout());
        if (title != null) {
            add(new JLabel(title + ": "), "wrap");
        }
    }

    /**
     * Title shows up on border, c is size of names and data
     *
     * @param title Title for the border
     * @param names  array of names to set as labels
     * @param data  array of flag values each name represents
     * @param c     the size of names/data<BOLD>(SHOULD BE THE SAME!)</BOLD>
     */
    public FlagChoice(String title, String[] names, int[] data, int c, ActionListener parent) {
        this(title);
        flags = names;        // Assign the array of names.
        fileData = data;      // Assign the array of data.
        arraySize = c;            // Assign size of array.
        myParent = parent;
        setupLayout();
    }

    public FlagChoice(String title, String[] names, int[] data, int c, ActionListener parent, int cols) {
        this(title);
        columns = cols;
        flags = names;        // Assign the array of names.
        fileData = data;      // Assign the array of data.
        arraySize = c;            // Assign size of array.
        myParent = parent;
        setupLayout();
    }

    private void setupLayout() {
        // Start creating
        choices = new JCheckBox[arraySize];

        // Lay them out on the panel.
        for (int a = 0; a < arraySize; a++) {
            choices[a] = new JCheckBox(flags[a]);
            choices[a].setSelected(false); // Just in case.
            choices[a].addActionListener(myParent);
            if( a % columns == (columns -1) ) {
                add(choices[a] ,"wrap");
            } else {
                add(choices[a]);
            }
        }
    }

    public void setEnabled(boolean flag) {
        for (int a = 0; a < arraySize; a++) {
            choices[a].setEnabled(flag);
        }
    }

    /**
     * Set the flags, must provide a full bit set, not an bit flag.
     */
    public void setFlags(int newFlags) {
        if (newFlags < 0) {
            System.out.println("!STOP! tried setting invalid flag set in FlagChoice!");
            return;
        }

        // Go through each item, if it set in the new flags, set its
        // check box otherwise, uncheck.
        for (int temp = 0; temp < arraySize; temp++) {
            if ((fileData[temp] & newFlags) == fileData[temp])
                choices[temp].setSelected(true);
            else
                choices[temp].setSelected(false);
        }
        return;
    }

    /**
     * Set a flag must provide a bit flag, not a bit set.
     *
     * @Must provide boolean, true = add bit, false = remove bit
     */
    public void setBit(int newBit, boolean on) {
        for (int temp = 0; temp < arraySize; temp++) {
            if (fileData[temp] == newBit)
                choices[temp].setSelected(on);
        }
    }

    /**
     * Retrieves full bit set(flags).
     */
    public int getFlags() {
        int total = 0;
        for (int temp = 0; temp < arraySize; temp++) {
            if (choices[temp].isSelected())
                total += fileData[temp];
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
        }
        catch (IndexOutOfBoundsException e) {
            System.out.println("Attempt to set bad flag in FlagChoice!");
        }
    }
}
