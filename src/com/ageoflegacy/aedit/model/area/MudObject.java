// George Frick
// MudObject.java
// Area Editor Project, Spring 2002
package com.ageoflegacy.aedit.model.area;

import com.ageoflegacy.aedit.model.MudConstants;

import javax.swing.*;
import java.util.Vector;
import java.util.Collection;

public class MudObject extends MudThing {

	private int condition; //
	private int cost; //
	private int settings; // gem settings
	private String removeMessage; //
	private String wearMessage; //
	private int casts; //
	private int weight; //
	private int wearFlags; //
	private int extraFlags; //
	private int type; //
	private int[] iValues;
	private String[] sValues;
	protected Collection<ObjectAffect> affects;
	protected Collection<MudObject> contents;

	public MudObject(int vnum, Area ar) {
		super(vnum);
		this.vnum = vnum;
		myarea = ar;
		condition = 100;
		cost = 1;
		settings = 0;
		weight = 1;
		type = MudConstants.ITEM_TRASH;
		extraFlags = 0;
		wearFlags = 0;
		iValues = new int[5];
		sValues = new String[5];
		for (int a = 0; a < 5; a++) {
			iValues[a] = 0;
			sValues[a] = "0";
		}
		casts = 0;
		affects = new Vector<ObjectAffect>();
		contents = new Vector<MudObject>();
	}

	public MudObject(int vnum, Area ar, int copy) {
		this(vnum, ar);
		MudObject temp = ar.getObject(copy);
		if (temp == null)
			return;

		condition = temp.getCondition();
		cost = temp.getCost();
		weight = temp.getWeight();
		type = temp.getType();
		extraFlags = temp.getExtraFlags();
		wearFlags = temp.getWearFlags();
		for (int a = 0; a < 5; a++) {
			iValues[a] = temp.getiValue(a);
			sValues[a] = temp.getsValue(a);
		}
		name = temp.getName();
		shortDesc = temp.getShortDesc();
		longDesc = temp.getLongDesc();
		description = temp.getDescription();
		size = temp.getSize();
		level = temp.getLevel();
		material = temp.getMaterial();

		for (ObjectAffect af : temp.getAffects()) {
			ObjectAffect newAf = new ObjectAffect(af.getType(), af.getValue());
			addAffect(newAf);
		}
	}

	public Collection<ObjectAffect> getAffects() {
		return affects;
	}

	public void setAffects(Collection<ObjectAffect> affects) {
		this.affects = affects;
	}

	public void addObject(MudObject mAdd) {
		contents.add(mAdd);
	}

	public void removeObject(MudObject mRemove) {
		while (contents.contains(mRemove))
			contents.remove(mRemove);
	}

	public Collection<MudObject> getInventory() {
		return contents;
	}

	public boolean isContainer() {
		return (type == MudConstants.ITEM_CONTAINER);
	}

	public boolean isFull() {
		return !isContainer() || contents.size() >= iValues[0];

	}

	public int getCondition() {
		return condition;
	}

	public int getCost() {
		return cost;
	}

	public String getWearMessage() {
		return wearMessage;
	}

	public String getRemoveMessage() {
		return removeMessage;
	}

	public int getWeight() {
		return weight;
	}

	public int getType() {
		return type;
	}

	public int getWearFlags() {
		return wearFlags;
	}

	public int getExtraFlags() {
		return extraFlags;
	}

	public int getCasts() {
		return casts;
	}

	public String getsValue(int which) {
		if (which >= 0 && which <= 5)
			return sValues[which];
		else {
			System.out.println("Could not return object sValue, out of bounds.");
			return "invalid";
		}
	}

	public int getiValue(int which) {
		if (which >= 0 && which <= 5)
			return iValues[which];
		else {
			System.out.println("Could not return object iValue, out of bounds.");
			return -1;
		}
	}

	public int getiValue(int which, int minimum) {
		int temp = getiValue(which);

		if (temp < minimum) {
			setiValue(which, minimum);
			return minimum;
		}

		return temp;
	}

	public int getiValue(int which, int min, int max) {
		int temp = getiValue(which, min);

		if (temp > max) {
			setiValue(which, max);
			return max;
		}

		return temp;
	}

	public void addAffect(ObjectAffect nAffect) {
		affects.add(nAffect);
	}

	public void removeAffect(ObjectAffect nAffect) {
		affects.remove(nAffect);
	}

	public JList getAffectList() {
		return new JList(affects.toArray(new ObjectAffect[affects.size()]));
	}

	public void setCondition(int newCondition) {
		if (newCondition >= 0 && newCondition <= 100)
			condition = newCondition;
		else {
			System.out.println("Could not set illegal condition " + newCondition + ".");
			System.out.println("Must be between 0 and 100.");
		}

	}

	public void setCost(int newCost) {

		if (newCost >= 0)
			cost = newCost;

	}

	public void setNumSettings(int s) {
		settings = s;
	}

	/*
	 * setWearMessage/setRemoveMessage Must contain %s and %? somewhere in
	 * string. Add this checking before final release.
	 */
	public void setWearMessage(String newMessage) {
		wearMessage = newMessage;
	}

	public void setRemoveMessage(String newMessage) {
		removeMessage = newMessage;
	}

	public void setWeight(int newWeight) {
		weight = newWeight;
	}

	public void setType(final int newType) {
		if (MudConstants.isValidType(newType))
			type = newType;
		else {
			System.out.println("Could not set illegal type: " + Integer.toString(newType) + ".");
			System.out.println("See constant table for valid types.");
		}
	}

	public void setWearFlags(int newFlags) {
		wearFlags = newFlags;
	}

	public void removeWearFlag(int removeFlag) {
		wearFlags = wearFlags ^ removeFlag;
	}

	public void addWearFlag(int addFlag) {
		wearFlags = wearFlags | addFlag;
	}

	public void setExtraFlags(int newFlags) {
		extraFlags = newFlags;
	}

	public void removeExtraFlag(int removeFlag) {
		extraFlags = extraFlags ^ removeFlag;
	}

	public void addExtraFlag(int addFlag) {
		extraFlags = extraFlags | addFlag;
	}

	public void setCast(int newFlags) {
		casts = newFlags;
	}

	public void removeCast(int removeFlag) {
		casts = casts ^ removeFlag;
	}

	public void addCast(int addFlag) {
		casts = casts | addFlag;
	}

	public void setiValue(int which, int newValue) {
		if (newValue < -1) {
			System.out.println("Could not set object iValue, illegal value.");
			return;
		}

		if (which >= 0 && which <= 5)
			iValues[which] = newValue;
		else {
			System.out.println("Could not set object iValue, out of bounds.");
		}
	}

	public void setsValue(int which, String newValue) {
		if (newValue.startsWith("''"))
			return;

		if (which >= 0 && which <= 5)
			sValues[which] = newValue;
		else {
			System.out.println("Could not set object sValue, out of bounds.");
		}
	}

}
