package com.ageoflegacy.aedit.ui.view.mobView;

import com.ageoflegacy.aedit.beans.Armor;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * Holds text fields to represent armor types (@see Armor). Defaults to 100.
 * Will not allow blank field or non numeric value.
 * Only creates an event on actual change.
 */
public class ArmorField extends JPanel implements FocusListener {

    ArmorForm target;
    private JTextField[] fields;
    // Hold which has focus and it's starting value to compare when it loses focus.
    private Armor selected;
    private String value;

    /**
     * Create the JTextfields and put them a bordered, titled box.
     *
     * @param target The ArmorForm this field is managing
     */
    public ArmorField(com.ageoflegacy.aedit.ui.view.mobView.ArmorForm target) {
        this.target = target;
        setLayout(new GridLayout(1, 1));
        Box box = Box.createHorizontalBox();

        fields = new JTextField[Armor.values().length]; 
        String defaultArmor = "100";
        for (Armor at : Armor.values()) {
            fields[at.ordinal()] = new JTextField(4);   
            fields[at.ordinal()].setText(defaultArmor);
            fields[at.ordinal()].addFocusListener(this);
            box.add(fields[at.ordinal()]);
        }

        add(box);
        setEnabled(true);
        StringBuffer buf = new StringBuffer("AC: ");
        boolean first = true;
        for (Armor at : Armor.values()) {
            if (!first)
                buf.append("/");
            else
                first = false;

            buf.append(at.name());
        }
        setBorder(new TitledBorder(buf.toString()));
    }

    /**
     * Called when one of the JTextFields gains focus
     *
     * @param event FocusEvent
     */
    public void focusGained(FocusEvent event) {
        if (!(event.getComponent() instanceof JTextField))
            return;

        JTextField field = (JTextField) event.getComponent();

        for (Armor at : Armor.values()) {
            if (field == fields[at.ordinal()]) {
                selected = at;
                value = field.getText();
            }
        }
    }

    /**
     * Called when one of the JTextFields loses focus, reverts change if box is blanked out
     * or changed to a non-number. Discards the event if the value did not change.
     *
     * @param event FocusEvent
     */
    public void focusLost(FocusEvent event) {
        if (!(event.getComponent() instanceof JTextField))
            return;

        JTextField field = (JTextField) event.getComponent();

        if (selected == null || field != fields[selected.ordinal()])
            return;

        if (field.getText().equals(value))
            return;

        if (field.getText().length() == 0)
            field.setText(value);

        int iValue = 0;
        try {
            iValue = Integer.parseInt(field.getText());
        } catch (Exception ex) {
            field.setText(value);
        }

        target.setArmor(selected, iValue);
        selected = null; // lost focus.
        value = null;
    }

    /**
     * Set a value for one of the armor fields
     *
     * @param type  Which Armor Type Field to set.
     * @param value the new value.
     */
    public void setValue(Armor type, int value) {
        fields[type.ordinal()].setText(Integer.toString(value));
    }

    /**
     * Get the value for one of the armor fields
     *
     * @param type Which Armor Type Field to set.
     * @return the value of field -type-
     */
    public int getValue(Armor type) {
        return Integer.parseInt(fields[type.ordinal()].getText());
    }

    /**
     * Turn the fields on or off, can only do them all, no single selection.
     *
     * @param val true to enable, false to disable.
     */
    public void setEnabled(boolean val) {
        for (Armor at : Armor.values()) {
            fields[at.ordinal()].setEnabled(val);
        }
    }
}
