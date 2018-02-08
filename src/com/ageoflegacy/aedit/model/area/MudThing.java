// George Frick
// MudThing.java (abstract)
// Area Editor Project, Spring 2002
//
// This is an abstract class to represent an object, room, or mobile in
// the model editor. It may represent more also.
package com.ageoflegacy.aedit.model.area;

import com.ageoflegacy.aedit.beans.Armor;
import com.ageoflegacy.aedit.model.RomIO;
import com.ageoflegacy.aedit.model.Size;

import java.util.Map;
import java.util.TreeMap;

public abstract class MudThing {
	protected Area myarea;
	protected String name;
	protected String shortDesc;
	protected String longDesc;
	protected String description;
	protected Size size;
	public int vnum;
	protected int level;
	protected int affectedBy;
	protected int affectedBy2;
	protected int alignment;
	protected String material;
	protected int[] armor;
	protected Map<String, String> extraDescriptions;

	protected MudThing() {
		armor = new int[Armor.values().length];
		extraDescriptions = new TreeMap<String, String>();
	}

	protected MudThing(int newVnum) {
		this();
		vnum = newVnum;
		name = Integer.toString(vnum);
		shortDesc = name;
		longDesc = "A " + name;
		description = longDesc;
		level = 1;
		material = "nothing";
		size = new Size(1);
	}

	public Area getArea() {
		return myarea;
	}

	public void testPrint() {
		System.out.println("Vnum " + vnum + "- " + name + ", " + shortDesc);
	}

	public String getName() {
		return name;
	}

	public String getShortDesc() {
		return shortDesc;
	}

	public String getLongDesc() {
		return longDesc;
	}

	public String getDescription() {
		return description;
	}

	public String toString() {
		return Integer.toString(vnum) + " - " + getName();
	}

	public Size getSize() {
		return size;
	}

	public int getVnum() {
		return vnum;
	}

	public int getLevel() {
		return level;
	}

	public int getAffectedBy() {
		return affectedBy;
	}

	public int getAffectedBy2() {
		return affectedBy2;
	}

	public int getAlignment() {
		return alignment;
	}

	public int getAC(Armor which) {
		return armor[which.ordinal()];
	}

	public int[] getArmor() {
		return armor;
	}

	public String getMaterial() {
		return material;
	}

	public void setName(String newname) {
		if (newname.indexOf('{') != -1)
			return;

		name = newname;
	}

	public void setShortName(String newshort) {
		shortDesc = newshort;
	}

	public void setLongName(String newlong) {
		longDesc = newlong;
	}

	public void setDescription(String newdesc) {
		description = newdesc;
	}

	public void setSize(Size newsize) {
		size = newsize;
	}

	public void setVnum(int newvnum) {
		if (newvnum < myarea.getLowVnum() || newvnum > myarea.getHighVnum())
			return;

		vnum = newvnum;
	}

	public void setLevel(int newlevel) {
		if (newlevel < 1 || newlevel > 999)
			return;

		level = newlevel;
	}

	public void setAffectedBy(int newAffectedBy) {
		affectedBy = newAffectedBy;
	}

	public void setAffectedBy2(int newAffectedBy2) {
		affectedBy2 = newAffectedBy2;
	}

	public void setAlignment(int newAlignment) {
		if (newAlignment < -1000 || newAlignment > 1000)
			return;

		alignment = newAlignment;
	}

	public void setAC(Armor which, int newArmorClass) {
		armor[which.ordinal()] = newArmorClass;
	}

	public void setMaterial(String newMaterial) {
		material = RomIO.oneArgument(newMaterial);
	}

	public static String vnumString(int vnum) {
		if (vnum < 10)
			return "00000" + vnum;
		else if (vnum < 100)
			return "0000" + vnum;
		else if (vnum < 1000)
			return "000" + vnum;
		else if (vnum < 10000)
			return "00" + vnum;
		else if (vnum < 100000)
			return "0" + vnum;
		else
			return Integer.toString(vnum);
	}

	public Map<String, String> getExtraDescriptions() {
		return extraDescriptions;
	}

	public void setExtraDescriptions(Map<String, String> extraDescriptions) {
		this.extraDescriptions = extraDescriptions;
	}
}
