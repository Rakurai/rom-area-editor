package com.ageoflegacy.aedit.model.area;

public class MobileProgram {
	String data;

	public MobileProgram() {
	}

	public MobileProgram(String d) {
		data = d;
	}

	public String getProgram() {
		return data;
	}

	public void setProgram(String newData) {
		data = newData;
	}

	public String toString() {
		return data;
	}
}
