// George Frick
// RomIO.java

package com.ageoflegacy.aedit.model;

import com.ageoflegacy.aedit.model.Area;

import java.io.*;

public abstract class RomIO {

    protected File inputFile;
    protected FileInputStream inStream;
    protected FileOutputStream outStream;
    protected DataInputStream dataStream;
    protected BufferedInputStream inbuf1;
    protected BufferedReader buf2;
    protected BufferedWriter outbuf;
    protected BufferedInputStream outbuf1;
    protected boolean open;
    protected Area area;

    public boolean openArea(boolean read) {
        try {
            if (read) {
                inStream = new FileInputStream(inputFile);
                buf2 = new BufferedReader(new InputStreamReader(inStream));
            } else {
                outStream = new FileOutputStream(inputFile);
                outbuf = new BufferedWriter(new OutputStreamWriter(outStream));
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("Bad filename!");
            return false;
        }
        return true;
    }

    public boolean isOpen() {
        return open;
    }

    public String getLine() {
        if (!open)
            return null;

        try {
            return buf2.readLine();
        }
        catch (Exception e) {
            return null;
        }
    }

    protected String readToTilde(BufferedReader in) {
        String temp = "error";

        try {
            temp = in.readLine() + " \n";
            while (temp.indexOf('~') == -1) {
                temp += in.readLine() + " \n";
            }
            temp = temp.substring(0, temp.indexOf('~'));
        }
        catch (Exception e) {
            System.out.println("Error reading, expecting tilde(~).");
        }

        return temp;
    }

    protected int readVnum(BufferedReader in) {
        try {
            String temp = in.readLine();

            if (temp.charAt(0) != '#')
                return -1;

            temp = temp.substring(1, temp.length());
            return Integer.parseInt(temp);
        }
        catch (Exception e) {
            System.out.println("Bad #vnum format!");
        }

        return -1;
    }

    protected int parseVnum(String in) {
        try {
            return Integer.parseInt(in.substring(1, in.length()));
        }
        catch (Exception e) {
            System.out.println("Bad #vnum format!");
        }

        return -1;
    }

    public static String oneArgument(String st) {
        int t1, t2;
        String s = st.trim();
        t1 = s.indexOf("'");
        if (t1 >= 0 && (t1 < s.indexOf(' ') || s.indexOf(' ') == -1)) {
            t1 = s.indexOf("'");
            t2 = s.indexOf("'", t1 + 1);
            // System.out.println("Caught(" + s + ") at: " + Integer.toString(t1) + " and: " + Integer.toString(t2));
            if (t2 >= 0)
                return s.substring(t1 + 1, t2);
        }

        if (s.indexOf(' ') < 0)
            return s;

        return s.substring(0, s.indexOf(' ', 0));
    }

    protected static String trimArgument(String s) {
        int t1, t2;
        t1 = s.indexOf("'");
        if (t1 >= 0 && t1 < s.indexOf(' ') && s.indexOf(' ') != -1) {
            t1 = s.indexOf("'");
            t2 = s.indexOf("'", t1 + 1);
            // System.out.println("Caught(2) at: " + Integer.toString(t1) + " and: " + Integer.toString(t2));
            if (t2 >= 0)
                return s.substring(t2 + 1).trim();
        }

        if (s.indexOf(' ') < 0)
            return s;

        return s.substring(s.indexOf(' ') + 1);
    }
}
