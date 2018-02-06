package com.ageoflegacy.aedit.model;

import com.ageoflegacy.aedit.model.MudConstants;

public class MobTrigger {
    protected int type;
    protected int vnum;
    protected String phrase;

    public MobTrigger(int type, int vnum, String phrase) {
        this.type = type;
        this.vnum = vnum;
        this.phrase = phrase;
    }

    public int getType() {
        return type;
    }

    public int getVnum() {
        return vnum;
    }

    public String getPhrase() {
        return phrase;
    }

    public void setType(int nType) {
        if (nType <= 0)
            return;

        type = nType;
    }

    public void setVnum(int nVnum) {
        if (nVnum <= 0)
            return;

        vnum = nVnum;
    }

    public void setPhrase(String nPhrase) {
        phrase = nPhrase;
    }

    public String toString() {
        return sLookup(type) + " " + vnum + " " + phrase;
    }

    public String toFile() {
        return sLookup(type) + " " + vnum + " " + phrase;
    }

    public final static String sLookup(int t) {
        for (int a = 0; a < MudConstants.NUM_TRIGGERS; a++) {
            if (MudConstants.triggerFlags[a] == t)
                return MudConstants.triggerNames[a].toUpperCase();
        }

        return null;
    }

    public final static int iLookup(String t) {
        for (int a = 0; a < MudConstants.NUM_TRIGGERS; a++) {
            if (MudConstants.triggerNames[a].equalsIgnoreCase(t))
                return MudConstants.triggerFlags[a];
        }

        return 0;
    }
}
