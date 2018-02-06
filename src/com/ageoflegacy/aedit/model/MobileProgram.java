package com.ageoflegacy.aedit.model;

public class MobileProgram {
	int vnum;
	String data;

	public MobileProgram(int v) {
		vnum = v;
		data = "function mprog" + v + "(ch,mob,amt,args)\n-- Place script body here\nend\n";
	}

	public MobileProgram(int v, String d) {
		vnum = v;
		data = d;
		// System.out.println("New mobprog: " + toString() );
	}

	public String getProgram() {
		return data;
	}

	public void setProgram(String newData) {
		data = newData;
	}

	public int getVnum() {
		return vnum;
	}

	public void setVnum(int newV) {
		vnum = newV;
	}

	public String toString() {
		if (data.indexOf('\n') != -1)
			return Integer.toString(vnum) + ": " + data.substring(0, data.indexOf('\n'));
		else
			return Integer.toString(vnum) + ": " + data;
	}

	public String toFile() {
		String temp;
		temp = "#" + Integer.toString(vnum) + "\n" + data + "~\n";
		return temp;
	}
}
