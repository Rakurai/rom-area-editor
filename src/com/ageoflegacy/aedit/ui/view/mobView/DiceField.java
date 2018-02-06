package com.ageoflegacy.aedit.ui.view.mobView;

import com.ageoflegacy.aedit.beans.Dice;
import com.ageoflegacy.aedit.ui.view.mobView.DiceForm;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * Displays a Dice as a set of three text fields representing the number of dice, the number of sides on each dice
 * and a bonus to add to the roll. The bonus may be negative or 0.
 */
public class DiceField extends JPanel implements FocusListener {

    private JTextField number, sided, bonus;
    private String name;
    private com.ageoflegacy.aedit.ui.view.mobView.DiceForm target;
    private Dice currentValue;

    /**
     * Creat the Dicefield
     *
     * @param name   An alias to name this dice
     * @param target The target DiceForm to call on changes.
     */
    public DiceField(String name, DiceForm target) {
        this.name = name;
        this.target = target;
        setLayout(new GridLayout(1,1));
        Box box = Box.createHorizontalBox();

        number = new JTextField(3);
        sided = new JTextField(3);
        bonus = new JTextField(4);
        JLabel sideLabel = new JLabel("D");
        JLabel bonusLabel = new JLabel("+");

        number.addFocusListener(this);
        sided.addFocusListener(this);
        bonus.addFocusListener(this);

        box.add(number);
        box.add(sideLabel);
        box.add(sided);
        box.add(bonusLabel);
        box.add(bonus);
        add(box);
        setBorder(new TitledBorder(this.name + " Dice"));
    }

    /**
     * Retrieve the current values as a Dice
     *
     * @return Current Dice value
     */
    public Dice getValue() {
        int a, b, c;
        a = Integer.parseInt(number.getText());
        b = Integer.parseInt(sided.getText());
        c = Integer.parseInt(bonus.getText());
        return new Dice(a, b, c);
    }

    /**
     * Set the current value of the field
     *
     * @param dice dice to set fields to
     */
    public void setValue(Dice dice) {
        number.setText(Integer.toString(dice.getNumberOfDice()));
        sided.setText(Integer.toString(dice.getNumberOfSides()));
        bonus.setText(Integer.toString(dice.getBonus()));
    }

    /**
     * Enable or disable all fields.
     *
     * @param enabled true to enable, false to disable
     */
    public void setEnabled(boolean enabled) {
        number.setEnabled(enabled);
        sided.setEnabled(enabled);
        bonus.setEnabled(enabled);
    }

    /**
     * Called when any of the JTextFields gain focus. Copy the current Dice for later
     *
     * @param event FocusEvent
     */
    public void focusGained(FocusEvent event) {

        if (!(event.getComponent() instanceof JTextField))
            return;

        JTextField field = (JTextField) event.getComponent();

        if (field != number && field != sided && field != bonus)
            return;

        currentValue = new Dice(Integer.parseInt(number.getText()),
                Integer.parseInt(sided.getText()),
                Integer.parseInt(bonus.getText()));

    }

    /**
     * Called when any of the JTextFields lose focus. Compare new dice value and update as needed.
     *
     * @param event FocusEvent
     */
    public void focusLost(FocusEvent event) {

        if (!(event.getComponent() instanceof JTextField))
            return;

        JTextField field = (JTextField) event.getComponent();

        if (field != number && field != sided && field != bonus)
            return;

        if (currentValue == null)
            return;

        int newNumber;
        int newSided;
        int newBonus;

        try {
            newNumber = Integer.parseInt(number.getText());
            newSided = Integer.parseInt(sided.getText());
            newBonus = Integer.parseInt(bonus.getText());
        } catch (Exception ex) {
            number.setText(Integer.toString(currentValue.getNumberOfDice()));
            sided.setText(Integer.toString(currentValue.getNumberOfSides()));
            bonus.setText(Integer.toString(currentValue.getBonus()));
            currentValue = null;
            return;
        }

        if (newNumber == currentValue.getNumberOfDice()
                && newSided == currentValue.getNumberOfSides()
                && newBonus == currentValue.getBonus())
            return;

        target.setDice(name, new Dice(newNumber, newSided, newBonus));
        currentValue = null;
    }
}
