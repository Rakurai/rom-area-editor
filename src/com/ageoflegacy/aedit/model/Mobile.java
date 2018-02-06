// George Frick
// Mobile.java
// Area Editor Project, Spring 2002
//
// This is a class to hold a mobile's data. It restricts the setting
// of unallowed values.
package com.ageoflegacy.aedit.model;

import com.ageoflegacy.aedit.beans.Armor;
import com.ageoflegacy.aedit.beans.Dice;
import com.ageoflegacy.aedit.ui.view.mobView.MudShopView;

import java.util.Collection;
import java.util.Vector;

public class Mobile extends MudThing {
    protected Race race;
    protected String deathCry;
    protected int actFlags;
    protected int group;
    protected int hitRoll;
    protected Dice hitDice;
    protected Dice manaDice;
    protected Dice damageDice;
    protected String damageType;
    protected int offensiveFlags;
    protected int immunityFlags;
    protected int resistanceFlags;
    protected int vulnerabilityFlags;
    protected int startPosition;
    protected int defaultPosition;
    protected int sex;
    protected int wealth;
    protected int form;
    protected int parts;
    protected int maxInArea; // for resets
    protected String mySpec;
    protected MudShopView myShop;
    protected int[] equipment;
    protected Collection<MudObject> inventory;
    protected Collection<MobTrigger> triggers;

    public Mobile(int vnum, Area ar) {
        super(vnum);
        myarea = ar;
        hitDice = new Dice(1, 1, 1);
        manaDice = new Dice(1, 1, 1);
        damageDice = new Dice(1, 1, 1);
        deathCry = null;
        mySpec = null;
        actFlags = 0;
        group = 0;
        hitRoll = 10;
        damageType = "punch";
        offensiveFlags = 0;
        immunityFlags = 0;
        resistanceFlags = 0;
        vulnerabilityFlags = 0;
        startPosition = 8;
        defaultPosition = 8;
        sex = 0;
        wealth = 1;
        form = 0;
        parts = 0;
        triggers = new Vector<MobTrigger>();
        inventory = new Vector<MudObject>();
        equipment = new int[21];
        for (int a = 0; a < 21; a++)
            equipment[a] = -1;
        maxInArea = 0;
        race = RaceTable.defaultRace();
        myShop = null;
    }

    public Mobile(int vnum, Area ar, int copyFrom) {
        this(vnum, ar);

        // alright, now, get the mobile to copy from and
        // copy the information here...
        Mobile temp = ar.getMobile(copyFrom);
        if (temp == null)
            return;

        hitDice = new Dice(temp.getHitDice().getNumberOfDice(),
                temp.getHitDice().getNumberOfSides(),
                temp.getHitDice().getBonus());
        manaDice = new Dice(temp.getManaDice().getNumberOfDice(),
                temp.getManaDice().getNumberOfSides(),
                temp.getManaDice().getBonus());
        damageDice = new Dice(temp.getDamageDice().getNumberOfDice(),
                temp.getDamageDice().getNumberOfSides(),
                temp.getDamageDice().getBonus());
        if (temp.getDeathCry() != null)
            deathCry = temp.getDeathCry();

        if (temp.getSpecial() != null)
            mySpec = temp.getSpecial();

        actFlags = temp.getActFlags();
        group = temp.getGroup();
        hitRoll = temp.getHitRoll();
        race = temp.getRace();
        damageType = temp.getDamageType();
        offensiveFlags = temp.getOffensiveFlags();
        immunityFlags = temp.getImmunityFlags();
        resistanceFlags = temp.getResistanceFlags();
        vulnerabilityFlags = temp.getVulnerabilityFlags();
        startPosition = temp.getStartPosition();
        defaultPosition = temp.getDefaultPosition();
        sex = temp.getSex();
        wealth = temp.getWealth();
        form = temp.getForm();
        parts = temp.getParts();
        name = temp.getName();
        shortDesc = temp.getShortDesc();
        longDesc = temp.getLongDesc();
        description = temp.getDescription();
        size = temp.getSize();
        level = temp.getLevel();
        affectedBy = temp.getAffectedBy();
        alignment = temp.getAlignment();
        material = temp.getMaterial();
        for (Armor armor : Armor.values()) {
            setAC(armor, temp.getAC(armor));
        }
        //protected int[] armor;

        //protected Vector extraDescs;
    }

    public int getEquipment(int which) {
        if (which < 0 || which > 20)
            return -1;

        return equipment[which];
    }

    public void unEquip(int vnum) {
        for (int a = 0; a < 21; a++)
            if (equipment[a] == vnum)
                equipment[a] = -1;
    }

    public boolean isShop() {
        return myShop != null;

    }

    public void setShop(MudShopView s) {
        myShop = s;
    }

    public void addShop() {
        myShop = new MudShopView(vnum);
    }

    public void removeShop() {
        myShop = null;
    }

    public MudShopView getShop() {
        return myShop;
    }

    public String getSpecial() {
        return mySpec;
    }

    public Race getRace() {
        return race;
    }

    public String getDeathCry() {
        return deathCry;
    }

    public int getActFlags() {
        return actFlags;
    }

    public int getGroup() {
        return group;
    }

    public int getHitRoll() {
        return hitRoll;
    }

    public Dice getHitDice() {
        return hitDice;
    }

    public Dice getManaDice() {
        return manaDice;
    }

    public Dice getDamageDice() {
        return damageDice;
    }

    public String getDamageType() {
        return damageType;
    }

    public int getOffensiveFlags() {
        return offensiveFlags;
    }

    public int getImmunityFlags() {
        return immunityFlags;
    }

    public int getResistanceFlags() {
        return resistanceFlags;
    }

    public int getVulnerabilityFlags() {
        return vulnerabilityFlags;
    }

    public int getStartPosition() {
        return startPosition;
    }

    public int getDefaultPosition() {
        return defaultPosition;
    }

    public int getSex() {
        return sex;
    }

    public int getWealth() {
        return wealth;
    }

    public int getForm() {
        return form;
    }

    public int getParts() {
        return parts;
    }

    public Collection<MobTrigger> getTriggers() {
        return triggers;
    }

    public void setSpecial(String spec) {
        mySpec = spec;
    }

    public void setRace(Race newRace) {
        if (race == newRace)
            return;

        race = newRace;
        setActFlags(actFlags | race.getActFlags());
        setAffectedBy(affectedBy | race.getAffectedByFlags());
        setOffensiveFlags(offensiveFlags | race.getOffensiveFlags());
        setImmunityFlags(immunityFlags | race.getImmunityFlags());
        setResistanceFlags(resistanceFlags | race.getResistanceFlags());
        setVulnerabilityFlags(vulnerabilityFlags | race.getVulnerableFlags());
        setForm(form | race.getForm());
        setParts(form | race.getParts());
    }

    public void setDeathCry(String newDeathCry) {
        deathCry = newDeathCry;
    }

    public void setActFlags(int newActFlags) {
        actFlags = newActFlags;
    }

    public void setGroup(int newGroup) {
        if (newGroup != 0 && (newGroup < myarea.getLowVnum() || newGroup > myarea.getHighVnum()))
            return;

        group = newGroup;
    }

    public void setHitRoll(int newHitRoll) {
        if (newHitRoll < 1)
            return;

        hitRoll = newHitRoll;
    }

    public void setHitDice(Dice newHitDice) {
        hitDice = newHitDice;
    }

    public void setManaDice(Dice newManaDice) {
        manaDice = newManaDice;
    }

    public void setDamageDice(Dice newDamageDice) {
        damageDice = newDamageDice;
    }

    public void setDamageType(String newDamageType) {
        damageType = newDamageType;
    }

    public void setOffensiveFlags(int newOffensiveFlags) {
        offensiveFlags = newOffensiveFlags;
    }

    public void setImmunityFlags(int newImmunityFlags) {
        immunityFlags = newImmunityFlags;
    }

    public void setResistanceFlags(int newResistanceFlags) {
        resistanceFlags = newResistanceFlags;
    }

    public void setVulnerabilityFlags(int newVulnerabilityFlags) {
        vulnerabilityFlags = newVulnerabilityFlags;
    }

    public void setStartPosition(int newStartPosition) {
        startPosition = newStartPosition;
    }

    public void setDefaultPosition(int newDefaultPosition) {
        defaultPosition = newDefaultPosition;
    }

    public void setSex(int newSex) {
        sex = newSex;
    }

    public void setWealth(int newWealth) {
        if (newWealth < 0)
            return;

        if (newWealth > (getLevel() * 15))
            wealth = getLevel() * 15;
        else
            wealth = newWealth;
    }

    public void setForm(int newForm) {
        form = newForm;
    }

    public void setParts(int newParts) {
        parts = newParts;
    }

    public void equipMobile(int pos, int vnum) {
        equipment[pos] = vnum;
    }

    public void giveToMobile(MudObject arg) {
        inventory.add(arg);
    }

    public void takeFromMobile(MudObject arg) {
        while (inventory.contains(arg))
            inventory.remove(arg);
    }

    public Collection<MudObject> getInventory() {
        return inventory;
    }

    // every time this function is called, the max will be increased
    public void upMax() {
        maxInArea++;
    }

    public void downMax() {
        maxInArea--;
    }

    public int getMax() {
        return myarea.maxMob(vnum);
    }

    public String toString() {
        if (getShop() == null)
            return Integer.toString(vnum) + " " + getShortDesc();
        else
            return Integer.toString(vnum) + " " + getShortDesc() +
                    "   [ShopKeeper]";
    }

    public void addTrigger(MobTrigger trig) {
        triggers.add(trig);
    }

    public void removeTrigger(MobTrigger trig) {
        if (triggers.contains(trig))
            triggers.remove(trig);
    }

    public void deleteTriggers(int vnum) {
        for (MobTrigger trigger : triggers) {
            if (trigger.getVnum() == vnum) {
                triggers.remove(trigger);
            }
        }
    }
}
