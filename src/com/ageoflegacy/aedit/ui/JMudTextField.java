package com.ageoflegacy.aedit.ui;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

public class JMudTextField extends JTextField {
    public JMudTextField(int cols) {
        super(cols);
    }

    protected Document createDefaultModel() {
        return new TextOnlyDocument();
    }

    static class NumberOnlyDocument extends PlainDocument {
        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
            if (str == null)
                return;

            char[] myset = str.toCharArray();
            String result = "";

            for (int i = 0; i < myset.length; i++) {
                if ((myset[i] < '9' && myset[i] > '0') || (myset[i] == '-' && i == 0))
                    result += myset[i];
            }
            super.insertString(offs, new String(result), a);
        }
    }

    static class TextOnlyDocument extends PlainDocument {

        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
            if (str == null)
                return;

            char[] myset = str.toCharArray();
            String result = "";

            for (int i = 0; i < myset.length; i++) {
                if (myset[i] > '9' || myset[i] < '0')
                    result += myset[i];
            }
            super.insertString(offs, new String(result), a);
        }
    }
}
