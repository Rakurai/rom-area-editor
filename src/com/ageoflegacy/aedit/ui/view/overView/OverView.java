/*
 * George Frick
 * OverView.java
 * Area Editor Project, Spring 2002
 *
 * This class displays the basic data of an model, it also provides
 * means to update this data, and revnum an model.
 */
package com.ageoflegacy.aedit.ui.view.overView;

import com.ageoflegacy.aedit.model.MudConstants;
import com.ageoflegacy.aedit.model.Area;
import com.ageoflegacy.aedit.ui.view.EditorView;
import com.ageoflegacy.aedit.ui.FlagChoice;
import com.ageoflegacy.aedit.ui.JMudNumberField;
import com.ageoflegacy.aedit.ui.JMudTextField;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.ageoflegacy.aedit.ui.view.roomView.LabeledField;
import net.miginfocom.swing.MigLayout;
import net.miginfocom.layout.CC;

public class OverView extends EditorView implements ActionListener {
    private JTextField fields[];
    private JPanel fieldPanels[];
    private FlagChoice areaFlagChoice;
    protected JButton edit, revnum;
    private String[] fieldTitles = {
        "File Name", "Area Name", "Builder", "Security", "Low Vnum", "High Vnum", "Resets in Area",
        "Exits Out of Area", "Rooms in Area", "Mobs in Area", "Objects in Area", "Object:Mobile:Room Ratio"
    };

    public OverView(Area ar) {
        super(ar);
        this.setLayout(new MigLayout("fillx"));

        JPanel overviewPanel = new JPanel();
        overviewPanel.setLayout(new MigLayout());
   
        edit = new JButton("Change Values");
        revnum = new JButton("ReVnum Area");
   
        overviewPanel.add(edit);
        overviewPanel.add(revnum, "wrap");

        fields = new JTextField[12];
        fieldPanels = new JPanel[12];

        for (int a = 0; a < 12; a++) {
            fields[a] = new JTextField(20);
            fields[a].setEditable(false);
            fieldPanels[a] = new LabeledField(fieldTitles[a], fields[a], true);
            if( a % 2 == 1 ) {
                overviewPanel.add( fieldPanels[a] , "wrap");
            } else {
                overviewPanel.add( fieldPanels[a] );
            }
        }

        areaFlagChoice = new FlagChoice("Area Flags", MudConstants.areaFlagNames,
                MudConstants.areaFlagData, MudConstants.areaFlagCount, this);
        overviewPanel.add(areaFlagChoice, "span");

        edit.setEnabled(false);
        revnum.setEnabled(false);
        areaFlagChoice.setEnabled(false);

        edit.addActionListener(new dataUpdate());
        revnum.addActionListener(new revnumUpdate());

CC componentConstraints = new CC();
componentConstraints.alignX("center").spanX();
        add(overviewPanel, componentConstraints);
        update();
    }

     // update the display from the data
    public void update() {
        fields[0].setText(data.getFileName());
        fields[1].setText(data.getAreaName());
        fields[2].setText(data.getBuilder());
        fields[3].setText(Integer.toString(data.getSecurity()));
        fields[4].setText(Integer.toString(data.getLowVnum()));
        fields[5].setText(Integer.toString(data.getHighVnum()));
        fields[6].setText(Integer.toString(data.getResetCount()));
        fields[7].setText(data.getExitFromAreaCount());
        fields[8].setText(Integer.toString(data.getRoomCount()));
        fields[9].setText(Integer.toString(data.getMobCount()));
        fields[10].setText(Integer.toString(data.getObjectCount()));
        fields[11].setText(data.getRatio());        
        areaFlagChoice.setFlags(data.getFlags());
        if (data.valid()) {
            edit.setEnabled(true);
            revnum.setEnabled(true);
            areaFlagChoice.setEnabled(true);
        } else {
            edit.setEnabled(false);
            revnum.setEnabled(false);
            areaFlagChoice.setEnabled(false);
        }
    }

     // NOT USED in this class!
    public void update(int v) {
        update();
    }

    // When model flags are adjusted, set to data
    public void actionPerformed(ActionEvent e) {
        data.setFlags(areaFlagChoice.getFlags());
        update();
    }

    // confirms entry to the model data text fields
    public boolean checkBasicData(JTextField[] fields) {
        int errornum = -1;
        int c = 0, d = 0, lowv = 0, highv = 0;
        if (fields[0].getText().length() != 0 && (fields[0].getText().length() < 7 || !(fields[0].getText().endsWith(".are")))) {
            JOptionPane.showMessageDialog(null, "File name must be in the form XXX.are!!", "Couldn't Create New Area!", JOptionPane.ERROR_MESSAGE);
            System.out.println("Couldn't create new!(b)");
            return false;
        }
        if (fields[1].getText().length() != 0 && fields[1].getText().length() < 6) {
            JOptionPane.showMessageDialog(null, "Area name must be 6 characters or more!", "Couldn't Create New Area!", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (fields[2].getText().length() != 0 && fields[2].getText().length() < 4) {
            JOptionPane.showMessageDialog(null, "Builder name must be 4 characters or more!", "Couldn't Create New Area!", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            if (fields[3].getText().length() != 0) {
                errornum = 1;
                c = Integer.parseInt(fields[3].getText());
                if (c < 1 || c > 9) {
                    JOptionPane.showMessageDialog(null, "Security must be a # from 1 to 9.", "Couldn't Create New Area!", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            if (fields[4].getText().length() != 0 && fields[5].getText().length() != 0) {
                errornum = 2;
                lowv = Integer.parseInt(fields[4].getText());
                errornum = 3;
                highv = Integer.parseInt(fields[5].getText());
                if (lowv + 24 >= highv) {
                    JOptionPane.showMessageDialog(null, "High vnum must be GREATER than low vnum by at least 25!!", "Couldn't Create New Area!", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                if (lowv <= 0 || highv <= 0) {
                    JOptionPane.showMessageDialog(null, "High and Low vnum must be greater than 0!!", "Couldn't Create New Area!", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                int temp = (highv - lowv) + 1;
                if (temp < data.getRoomCount() || temp < data.getObjectCount() || temp < data.getMobCount()) {
                    JOptionPane.showMessageDialog(null, "Vnum range must be great enough to hold all objects, mobiles and rooms.", "Too few vnums!", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
        }
        catch (Exception exc) {
            switch (errornum) {
                case 1:
                    JOptionPane.showMessageDialog(null, "Security must be a NUMBER from 1 to 9.", "Couldn't Create New Area!", JOptionPane.ERROR_MESSAGE);
                    break;
                case 2:
                    JOptionPane.showMessageDialog(null, "Low vnum must be a NUMBER!!", "Couldn't Create New Area!", JOptionPane.ERROR_MESSAGE);
                    break;
                case 3:
                    JOptionPane.showMessageDialog(null, "High vnum must be a NUMBER!!", "Couldn't Create New Area!", JOptionPane.ERROR_MESSAGE);
                    break;
                default:
                    System.out.println("ACK!");
                    break;
            }
            return false;
        }
        return true;
    }

    /**
     * listens for revnum button, brings up window, then revnums
     */
    class revnumUpdate implements ActionListener {
        GridBagConstraints constraint;
        GridBagLayout layout;
        JPanel temp;
        JLabel oldLabel, newLabel;
        JTextField oldField, newField;

        public revnumUpdate() {
            oldLabel = new JLabel("Current Starting Vnum");
            newLabel = new JLabel("New Starting Vnum");
            oldField = new JTextField(7);
            newField = new JMudNumberField(7);
            oldField.setEnabled(false);

            temp = new JPanel();
            layout = new GridBagLayout();
            constraint = new GridBagConstraints();
            //temp.setSize(new Dimension(300,200));
            // temp.setPreferredSize(new Dimension(500,300));
            temp.setLayout(layout);
            constraint.insets = new Insets(3, 3, 3, 3);
            constraint.gridwidth = 1;
            constraint.gridheight = 1;
            constraint.gridy = 0;
            constraint.gridx = 0;
            JLabel l1 = new JLabel("Change an Area vnum range");
            l1.setBorder(new BevelBorder(BevelBorder.RAISED));
            layout.setConstraints(l1, constraint);
            temp.add(l1);
            constraint.gridy = 1;
            layout.setConstraints(oldLabel, constraint);
            temp.add(oldLabel);
            constraint.gridy = 2;
            layout.setConstraints(oldField, constraint);
            temp.add(oldField);
            constraint.gridy = 3;
            layout.setConstraints(newLabel, constraint);
            temp.add(newLabel);
            constraint.gridy = 4;
            layout.setConstraints(newField, constraint);
            temp.add(newField);
        }

        public void actionPerformed(ActionEvent e) {
            int choice = -1;
            int errornum = -1;
            Object[] options = {"Revnum", "Cancel"};

            oldField.setText(Integer.toString(data.getLowVnum()));
            do {
                errornum = -1;
                newField.setText("");
                choice = JOptionPane.showOptionDialog(null, temp, "Revnum an model",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                        null, options, options[0]);

                if (choice == 0) {
                    if (newField.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, "You must provide a new starting vnum.", "Empty field!", JOptionPane.ERROR_MESSAGE);
                        errornum = 1;
                        continue;
                    }
                    try {
                        int temp = Integer.parseInt(newField.getText());
                        if (temp <= 0) {
                            JOptionPane.showMessageDialog(null, "New starting vnum must be greater >= 1.", "new vnum less than 1!", JOptionPane.ERROR_MESSAGE);
                            errornum = 1;
                            continue;
                        }
                        data.reVnum(temp);
                        update(temp);
                        return;
                    }
                    catch (Exception exc) {
                        JOptionPane.showMessageDialog(null, "New vnum must be a number.", "Not a number!", JOptionPane.ERROR_MESSAGE);
                        errornum = 1;
                        continue;
                    }

                } else  // canceled new.
                    return;
            }
            while (choice == 0 && errornum == 1);

            JOptionPane.showMessageDialog(null, "You must revnum mob program text by hand!", "Revnum Warning!", JOptionPane.WARNING_MESSAGE);
            update();
        }
    }

    class dataUpdate implements ActionListener {
        JLabel[] labels;
        JTextField[] fields;
        GridBagConstraints constraint;
        GridBagLayout layout;
        JPanel temp;

        public dataUpdate() {
            labels = new JLabel[6];
            fields = new JTextField[6];
            for (int a = 0; a < 3; a++) {
                fields[a] = new JMudTextField(20);
            }
            fields[3] = new JMudNumberField(20);
            fields[4] = new JTextField(20);
            fields[5] = new JMudNumberField(20);
            temp = new JPanel();
            layout = new GridBagLayout();
            constraint = new GridBagConstraints();
            temp.setSize(new Dimension(300, 200));
            temp.setPreferredSize(new Dimension(500, 300));
            temp.setLayout(layout);
            constraint.insets = new Insets(3, 3, 3, 3);
            constraint.gridwidth = 2;
            constraint.gridheight = 1;
            constraint.gridy = 0;
            constraint.gridx = 0;
            JLabel l1 = new JLabel(
                    "You may adjust the model's header data:");
            layout.setConstraints(l1, constraint);
            temp.add(l1);
            constraint.gridwidth = 1;
            constraint.gridheight = 1;
            labels[0] = new JLabel("File Name : ", JLabel.RIGHT);
            labels[1] = new JLabel("Area Name :", JLabel.RIGHT);
            labels[2] = new JLabel("Builder   :", JLabel.RIGHT);
            labels[3] = new JLabel("Security  :", JLabel.RIGHT);
            labels[4] = new JLabel("Lower Vnum:", JLabel.RIGHT);
            labels[5] = new JLabel("High Vnum :", JLabel.RIGHT);
            for (int a = 0; a < 6; a++) {
                constraint.gridx = 0;
                constraint.gridy = a + 3;
                layout.setConstraints(labels[a], constraint);
                temp.add(labels[a]);
                constraint.gridx = 1;
                layout.setConstraints(fields[a], constraint);
                temp.add(fields[a]);
            }
            fields[0].setText(data.getFileName());
            fields[1].setText(data.getAreaName());
            fields[2].setText(data.getBuilder());
            fields[3].setText(Integer.toString(data.getSecurity()));
            fields[4].setEnabled(false);
            fields[4].setText("Use REVNUM to change starting vnum.");
            fields[5].setText(Integer.toString(data.getHighVnum()));
            fields[4].setEnabled(false);
        }

        public void actionPerformed(ActionEvent e) {
            int choice = -1;
            int errornum = -1;
            Object[] options = {"OK", "CANCEL"};

            fields[0].setText(data.getFileName());
            fields[1].setText(data.getAreaName());
            fields[2].setText(data.getBuilder());
            fields[3].setText(Integer.toString(data.getSecurity()));
            fields[4].setText("Use REVNUM to change starting vnum.");
            fields[5].setText(Integer.toString(data.getHighVnum()));
            do {
                errornum = -1;
                choice = JOptionPane.showOptionDialog(null, temp, "Basic Area Data",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                        null, options, options[0]);
                fields[4].setText(Integer.toString(data.getLowVnum()));
                if (choice == 0) {
                    for (int a = 0; a < 6; a++) {
                        if (fields[a].getText().equals("")) {
                            JOptionPane.showMessageDialog(null, "-All- fields must be entered!", "All fields mus be entered!", JOptionPane.ERROR_MESSAGE);
                            errornum = 1;
                            break;
                        }
                    }
                } else  // canceled new.
                    return;
            }
            while (choice == 0 && (errornum == 1 || checkBasicData(fields) == false));

            data.setFileName(fields[0].getText());
            data.setAreaName(fields[1].getText());
            data.setBuilder(fields[2].getText());
            data.setSecurity(Integer.parseInt(fields[3].getText()));
            data.setVnumRange(Integer.parseInt(fields[4].getText()), Integer.parseInt(fields[5].getText()));
            update();

        }
    }
}
