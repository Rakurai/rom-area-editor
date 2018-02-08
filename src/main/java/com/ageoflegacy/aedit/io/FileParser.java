package com.ageoflegacy.aedit.io;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.ageoflegacy.aedit.model.MudConstants;

public class FileParser {
	private BufferedInputStream stream;
	private int lineNumber;
	
	public FileParser(InputStream s) {
		stream = new BufferedInputStream(s);
		lineNumber = 1;
	}
	
	public int getLineNumber() {
		return lineNumber;
	}

	public boolean isEOF(char c) {
		return c == 0;
	}

	public boolean isWhitespace(char c) {
		if (c == ' ' || c == '\n' || c == '\r' || c == '\t')
			return true;
		
		return false;
	}
	
	public boolean isQuote(char c) {
		if (c == '\'' || c == '"')
			return true;
		
		return false;
	}
	
	public char readChar() throws IOException {
		if (stream.available() < 1)
			return 0;
		
		stream.mark(1);
		char c = (char)stream.read();
		
		if (c == '\n')
			lineNumber++;
		
		return c;
	}

	public void unreadChar(char c) throws IOException {
		if (isEOF(c))
			return;

		if (c == '\n')
			lineNumber--;

		stream.reset();
	}
	
	public void skipWhitespace() throws IOException {
		while (true) {
			char c = readChar();
			
			if (isEOF(c))
				break;

			if (isWhitespace(c))
				continue;

			unreadChar(c);
			return;
		}
	}
	
	// read the word until the next whitespace, understands quotes
	public String readWord() throws IOException {
		StringBuilder sb = new StringBuilder();
		char quote = 0;

		skipWhitespace();
		
		while (true) {
			char c = readChar();
			
			if (isEOF(c))
				break;
			
			if (quote != 0 && c == quote) // end no matter what
				break;
			
			if (isQuote(c)) {
				quote = c;
				continue; // don't write out the character
			}

			if (isWhitespace(c) && quote == 0) {
				unreadChar(c); // don't throw away newline just yet, for optional comments
				break;
			}
			
			sb.append(c);
		}
		
		return sb.toString().trim();
	}

	public int readInt() throws IOException {
		return Integer.parseInt(readWord());
	}

	public int readFlags() throws IOException {
		String word = readWord();
		return MudConstants.getBitInt(word);
	}
	
	public String readHeader() throws IOException {
		String word = readWord();
		
		if (word.charAt(0) != '#')
			throw new IOException("Expected '#', got " + word);
		
		return word.substring(1);
	}
	
	public int readVnum() throws IOException {
		return Integer.parseInt(readHeader());
	}
	
	// read until a character is encountered, throws away the character
	public String readTo(char chartype) throws IOException {
		// don't skip whitespace
		StringBuilder sb = new StringBuilder();
		
		while (true) {
			char c = readChar();
			
			if (isEOF(c))
				break;
			
			if (c == chartype)
				break;
			
			sb.append(c);
		}
		
		return sb.toString();
	}

	public String readString() throws IOException {
		skipWhitespace();
		return readTo('~');
	}
	
	public String readToEOL() throws IOException {
		return readTo('\n');
	}
}
