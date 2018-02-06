/**
 * George Frick
 * AreaHeader.java
 * Area Editor Project, spring 2002
 *
 * @author gfrick
 * 12/19/15
 * This is an AreaHeader bean.
 */
package com.ageoflegacy.aedit.model;

public class AreaHeader {

	private String fileName;
	private String pathName;
	private String areaName;
	private String builder;
	private int lowVnum;
	private int highVnum;
	private int security;
	private int flags;

	public AreaHeader() {
		this.reset();
	}

	public void reset() {
		lowVnum = 0;
		highVnum = 0;
		fileName = "newarea.are";
		pathName = null;
		areaName = "NewArea";
		builder = "Mafia";
		flags = 0;
		security = 0;
	}

	public String getAreaName() {
		return areaName;
	}

	public String getBuilder() {
		return builder;
	}

	public String getFileName() {
		return fileName;
	}

	public String getPathName() {
		return pathName;
	}

	public int getSecurity() {
		return security;
	}

	public int getLowVnum() {
		return lowVnum;
	}

	public int getHighVnum() {
		return highVnum;
	}

	public int getFlags() {
		return flags;
	}

	public void setBuilder(String newbuilder) {
		builder = newbuilder;
	}

	public void setAreaName(String newname) {
		areaName = newname;
	}

	public void setFileName(String newname) {
		fileName = newname;
	}

	public void setPathName(String newpath) {
		pathName = newpath;
	}

	public void setVnumRange(int newLowVnum, int newHighVnum) {
		if (newHighVnum < newLowVnum) {
			setVnumRange(newHighVnum, newLowVnum);
			return;
		}

		lowVnum = newLowVnum;
		highVnum = newHighVnum;
	}

	public void setSecurity(int newSecurity) {
		if (newSecurity >= 1 && newSecurity <= 9) {
			security = newSecurity;
		}
	}

	public void setFlags(int newFlags) {
		flags = newFlags;
	}
}
