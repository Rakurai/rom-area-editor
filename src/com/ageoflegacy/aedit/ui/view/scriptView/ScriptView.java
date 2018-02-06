package com.ageoflegacy.aedit.ui.view.scriptView;

import com.ageoflegacy.aedit.ui.view.EditorView;
import com.ageoflegacy.aedit.model.Area;
import com.ageoflegacy.aedit.model.Room;
import com.ageoflegacy.aedit.model.MobileProgram;
import com.ageoflegacy.aedit.beans.scanning.SyntaxHighlighter;
import com.ageoflegacy.aedit.beans.scanning.Scanner;
import com.ageoflegacy.aedit.beans.scanning.LuaScanner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: Feb 21, 2009
 * Time: 12:36:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class ScriptView extends EditorView {

    private JComboBox vnumBox;
    private JButton newButton;
    private JButton deleteButton;
    private JButton backButton;
    private JButton nextButton;
    private Box buttonPanel;

    SyntaxHighlighter text;

    public ScriptView(Area d) {
        super(d);

        Scanner scanner = new LuaScanner();
        text = new SyntaxHighlighter(24, 80, scanner);
        JScrollPane scroller = new JScrollPane(text);
        scroller.setVerticalScrollBarPolicy(
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

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
                int vnum = data.getFreeScriptVnum();
                if (vnum == -1) {
                    inform("You are out of vnums!");
                    return;
                }
                MobileProgram temp = new MobileProgram(vnum);
                data.insert(temp);
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
                if (vnum <= data.getFirstMprogVnum())
                    return;

                int temp = vnum - 1;
                while (data.getScriptByVnum(temp) == null && temp >= data.getFirstMprogVnum())
                    temp--;

                if (temp >= data.getFirstMprogVnum())
                    vnum = temp;

                update();

            }
        });

        nextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (vnum >= data.getHighVnum())
                    return;

                int temp = vnum + 1;
                while (data.getScriptByVnum(temp) == null && temp <= data.getHighVnum())
                    temp++;

                if (temp <= data.getHighVnum())
                    vnum = temp;

                update();
            }
        });

        vnumBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MobileProgram temp = (MobileProgram) vnumBox.getSelectedItem();
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
        vnumBox = data.getVnumCombo("script");
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
        if (data.getMprogCount() > 0) {
            if( vnum <= 0 || data.getScriptByVnum(vnum) == null) {
                vnum = data.getFirstMprogVnum();
            }
            vnumBox.setSelectedItem(data.getScriptByVnum(vnum));
            MobileProgram temp = (MobileProgram) vnumBox.getSelectedItem();
            text.setText(temp.getProgram());
        } else {
            text.setText("");
        }
    }

}
