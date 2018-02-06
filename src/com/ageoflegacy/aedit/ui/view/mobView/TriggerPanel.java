package com.ageoflegacy.aedit.ui.view.mobView;

import com.ageoflegacy.aedit.model.MobTrigger;
import com.ageoflegacy.aedit.model.MudConstants;
import com.ageoflegacy.aedit.model.*;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class TriggerPanel extends JPanel {
    Area area;
    int vnum;
    Mobile mob;

    JComboBox typeBox;
    JTextField phraseField, mprogVnum;
    JList currentList;
    JButton addButton, removeButton, clearButton;

    public TriggerPanel(int vnum, Area data) {
        area = data;
        this.vnum = vnum;
        mob = data.getMobile(vnum);

        addButton = new JButton("Add/Update");
        removeButton = new JButton("Remove");
        clearButton = new JButton("New");
        typeBox = new JComboBox(MudConstants.triggerNames);
        phraseField = new JTextField(30);
        mprogVnum = new JTextField(6);
        currentList = new JList();

        typeBox.setBorder(new TitledBorder("Type"));
        mprogVnum.setBorder(new TitledBorder("Script"));
        phraseField.setBorder(new TitledBorder("Phrase/Chance/etc"));
        JScrollPane listPane = new JScrollPane(currentList);
        listPane.setBorder(new TitledBorder("Current Triggers"));

        setLayout(new MigLayout());
        add(typeBox);
        add(mprogVnum);
        add(phraseField, "wrap");
        add(listPane, "wrap, growx, span");
        add(addButton);
        add(removeButton);
        add(clearButton);

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MobTrigger temp;

                int mType = MudConstants.triggerFlags[typeBox.getSelectedIndex()];
                int mVnum = Integer.parseInt(mprogVnum.getText());
                String mPhrase = phraseField.getText();

                if (currentList.getSelectedValue() != null) {
                    temp = (MobTrigger) currentList.getSelectedValue();
                    temp.setType(mType);
                    temp.setVnum(mVnum);
                    temp.setPhrase(mPhrase);
                } else {
                    temp = new MobTrigger(mType, mVnum, mPhrase);
                    mob.addTrigger(temp);
                }
                clearData();
            }
        });

        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MobTrigger temp = (MobTrigger) currentList.getSelectedValue();

                if (temp != null) {
                    mob.removeTrigger(temp);
                    clearData();
                }
            }
        });

        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearData();
            }
        });

        currentList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                MobTrigger temp = (MobTrigger) currentList.getSelectedValue();

                if (temp != null) {
                    phraseField.setText(temp.getPhrase());
                    typeBox.setSelectedItem(MobTrigger.sLookup(temp.getType()));
                    mprogVnum.setText(Integer.toString(temp.getVnum()));
                }
            }
        });
        clearData();
    }

    private void clearData() {
        if (mob.getTriggers().size() == 0) {
            String[] str = new String[0];
            currentList.setListData(str);
        } else {
            currentList.setListData(mob.getTriggers().toArray(new MobTrigger[mob.getTriggers().size()]));
        }
        phraseField.setText("");
        typeBox.setSelectedIndex(0);
        mprogVnum.setText("");
    }
}
