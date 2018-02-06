package com.ageoflegacy.aedit.model;
// George Frick (tenchi@s5games.net) AIM: Cod3rguy
// Size.java
// Area Editor Project, Spring 2002
//
// A Size class, non-mutable.
// www.s5games.net

public class Size {
    public final static int SIZE_XS = 0;
    public final static int SIZE_SMALL = 1;
    public final static int SIZE_MEDIUM = 2;
    public final static int SIZE_LARGE = 3;
    public final static int SIZE_XL = 4;
    public final static int MAX_SIZE = SIZE_XL;

    private int mySize = SIZE_MEDIUM;

    // construct using a size
    public Size(Size s) {
        mySize = s.getSize();
    }

    // construct using an int
    public Size(int size) {
        if (size > MAX_SIZE || size < 0)
            System.out.println("BAD SIZE!!!");
        else
            mySize = size;
    }

    // construct using a string
    public Size(String size) {
        String size2 = size.toLowerCase();
        if (size2.equals("extra small") || size.equals("XS"))
            mySize = SIZE_XS;
        else if (size2.equals("small") || size.equals("S"))
            mySize = SIZE_SMALL;
        else if (size2.equals("medium") || size.equals("M"))
            mySize = SIZE_MEDIUM;
        else if (size2.equals("large") || size.equals("L"))
            mySize = SIZE_LARGE;
        else if (size2.equals("extra large") || size.equals("XL"))
            mySize = SIZE_XL;
        else
            mySize = SIZE_MEDIUM;
    }

    // pre: two sizes
    // post: is this size larger than size two?
    public boolean isLargerThan(Size two) {
        if (two.getSize() > mySize)
            return false;
        else
            return true;
    }

    public String print() {
        return toString().toLowerCase();
    }

    // pre: size object
    // post: size as printable string
    public String toString() {
        switch (mySize) {
            case SIZE_XS:
                return "Extra Small";
            case SIZE_SMALL:
                return "Small";
            case SIZE_MEDIUM:
                return "Medium";
            case SIZE_LARGE:
                return "Large";
            case SIZE_XL:
                return "Extra Large";
            default:
                return "!SIZE CLASS!";
        }

    }

    // pre: size object
    // post: size as short printable string
    public String getShortName() {
        switch (mySize) {
            case SIZE_XS:
                return "XS";
            case SIZE_SMALL:
                return "S";
            case SIZE_MEDIUM:
                return "M";
            case SIZE_LARGE:
                return "L";
            case SIZE_XL:
                return "XL";
            default:
                return "!SIZE CLASS!";
        }
    }

    // pre: size object
    // post: size as integer
    public int getSize() {
        return mySize;
    }
}
