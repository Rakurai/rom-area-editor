package com.ageoflegacy.aedit.ui.view.roomView;

import com.ageoflegacy.aedit.model.Area;
import com.ageoflegacy.aedit.model.Mobile;
import com.ageoflegacy.aedit.model.MudObject;
import com.ageoflegacy.aedit.model.Room;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/*
 * George Frick
 */

public class RoomInPanel extends JPanel {

    Area area;
    int vnum;
    JLabel label1;
    JList myObjects;
    JList myMobiles;
    JComboBox mobList;
    JComboBox objList;
    JScrollPane mobPane, objPane;
    JButton addObjButton, removeObjButton;
    JButton addMobButton, removeMobButton;
    Room room;

    protected GridBagConstraints constraint;
    protected GridBagLayout layout;

    public RoomInPanel(int vnum, Area data) {
        area = data;
        this.vnum = vnum;
        constraint = new GridBagConstraints();
        layout = new GridBagLayout();
        this.setLayout(layout);

        room = data.getRoom(vnum);

        myObjects = new JList(room.getObjects().toArray(new MudObject[room.getObjects().size()]));
        myMobiles = new JList(room.getMobiles().toArray(new Mobile[room.getMobiles().size()]));
        objList = area.getVnumCombo("object");
        objList.setBorder(new TitledBorder("Object to add"));
        mobList = area.getVnumCombo("mob");
        mobList.setBorder(new TitledBorder("Mobile to add"));
        mobPane = new JScrollPane(myMobiles);
        mobPane.setBorder(new TitledBorder("Mobiles in room"));
        objPane = new JScrollPane(myObjects);
        objPane.setBorder(new TitledBorder("Objects in room"));

        addObjButton = new JButton("Add Object");
        removeObjButton = new JButton("Remove Object");
        addMobButton = new JButton("Add Mobile");
        removeMobButton = new JButton("Remove Mobile");

        JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayout(1, 2));
        panel1.add(addObjButton);
        panel1.add(removeObjButton);

        JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayout(1, 2));
        panel2.add(addMobButton);
        panel2.add(removeMobButton);

        if (room == null) {
            System.out.println("Bad room in reset menu!");
        }

        label1 = new JLabel("Setting Inventory of room " + room.toString());

        constraint.gridx = 0;
        constraint.gridy = 0;
        constraint.gridwidth = 2;
        constraint.fill = GridBagConstraints.BOTH;
        layout.setConstraints(label1, constraint);
        this.add(label1);

        constraint.gridx = 0;
        constraint.gridy = 1;
        constraint.gridwidth = 1;
        layout.setConstraints(mobPane, constraint);
        this.add(mobPane);

        constraint.gridx = 1;
        constraint.gridy = 1;
        constraint.gridwidth = 1;
        layout.setConstraints(objPane, constraint);
        this.add(objPane);

        constraint.gridx = 0;
        constraint.gridy = 2;
        layout.setConstraints(mobList, constraint);
        this.add(mobList);

        constraint.gridx = 1;
        constraint.gridy = 2;
        layout.setConstraints(objList, constraint);
        this.add(objList);

        constraint.gridx = 0;
        constraint.gridy = 3;
        layout.setConstraints(panel2, constraint);
        this.add(panel2);

        constraint.gridx = 1;
        constraint.gridy = 3;
        layout.setConstraints(panel1, constraint);
        this.add(panel1);


        addObjButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MudObject myObj = (MudObject) objList.getSelectedItem();
                room.addObject(myObj);
                myObjects.setListData(room.getObjects().toArray(new MudObject[room.getObjects().size()]));
            }
        });

        removeObjButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MudObject myObj = (MudObject) myObjects.getSelectedValue();
                if (myObj == null)
                    return;

                room.removeObject(myObj);
                myObjects.setListData(room.getObjects().toArray(new MudObject[room.getObjects().size()]));
            }
        });

        addMobButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Mobile myMob = (Mobile) mobList.getSelectedItem();
                room.addMobile(myMob);
                myMobiles.setListData(room.getMobiles().toArray(new Mobile[room.getMobiles().size()]));
            }
        });

        removeMobButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Mobile myMob = (Mobile) myMobiles.getSelectedValue();
                if (myMob == null)
                    return;

                room.removeMobile(myMob);
                myMobiles.setListData(room.getMobiles().toArray(new Mobile[room.getMobiles().size()]));
            }
        });

    }

}
