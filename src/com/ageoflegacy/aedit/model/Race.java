package com.ageoflegacy.aedit.model;

public class Race {
    String name;
    boolean isPcRace;
    String movementMsg;
    int actFlags;
    int affectedByFlags;
    int affectedBy2Flags;
    int offensiveFlags;
    int immunityFlags;
    int resistanceFlags;
    int vulnerableFlags;
    int formFlags;
    int partsFlags;

    public Race(String n) {
        name = n;
        isPcRace = false;
        movementMsg = "-walks-";
        actFlags = 0;
        affectedByFlags = 0;
        affectedBy2Flags = 0;
        offensiveFlags = 0;
        immunityFlags = 0;
        resistanceFlags = 0;
        vulnerableFlags = 0;
        formFlags = 0;
        partsFlags = 0;
    }

    public void setPcRace(boolean ispcrace) {
        isPcRace = ispcrace;
    }

    public void setMovementMessage(String newMsg) {
        movementMsg = newMsg;
    }

    public void setActFlags(int newAct) {
        actFlags = newAct;
    }

    public void setAffectedByFlags(int newAct) {
        affectedByFlags = newAct;
    }

    public void setAffectedBy2Flags(int newAct) {
        affectedBy2Flags = newAct;
    }

    public void setOffensiveFlags(int newAct) {
        offensiveFlags = newAct;
    }

    public void setImmunityFlags(int newAct) {
        immunityFlags = newAct;
    }

    public void setResistanceFlags(int newAct) {
        resistanceFlags = newAct;
    }

    public void setVulnerableFlags(int newAct) {
        vulnerableFlags = newAct;
    }

    public void setFormFlags(int newAct) {
        formFlags = newAct;
    }

    public void setPartsFlags(int newAct) {
        partsFlags = newAct;
    }

    public boolean isPcRace() {
        return isPcRace;
    }

    public String getMovementMessage() {
        return movementMsg;
    }

    public int getActFlags() {
        return actFlags;
    }

    public int getAffectedByFlags() {
        return affectedByFlags;
    }

    public int getAffectedBy2Flags() {
        return affectedBy2Flags;
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

    public int getVulnerableFlags() {
        return vulnerableFlags;
    }

    public int getForm() {
        return formFlags;
    }

    public int getParts() {
        return partsFlags;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return name;
    }

    public void PrintToPrompt() {
        System.out.println("Race: " + name);
        System.out.println("--------------------------");
        System.out.print("Is PC race? ");
        if (isPcRace())
            System.out.println("Yes");
        else
            System.out.println("No");
        System.out.println("Movement Message: " + getMovementMessage());
        System.out.println("Act Flags       : " + Integer.toString(getActFlags()));
        System.out.println("Affected Flags  : " + Integer.toString(getAffectedByFlags()));
        System.out.println("Affected2 Flags : " + Integer.toString(getAffectedBy2Flags()));
        System.out.println("Offensive Flags : " + Integer.toString(getOffensiveFlags()));
        System.out.println("Immunity Flags  : " + Integer.toString(getImmunityFlags()));
        System.out.println("Resistance Flags: " + Integer.toString(getResistanceFlags()));
        System.out.println("Vulnerable Flags: " + Integer.toString(getVulnerableFlags()));
        System.out.println("Form Flags      : " + Integer.toString(getForm()));
        System.out.println("Parts Flags     : " + Integer.toString(getParts()));
    }
}
