package com.ageoflegacy.aedit.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.ageoflegacy.aedit.model.area.Area;
import com.ageoflegacy.aedit.model.table.DamTypeTable;
import com.ageoflegacy.aedit.model.table.LiquidTypeTable;
import com.ageoflegacy.aedit.model.table.RaceDataTable;
import com.ageoflegacy.aedit.model.table.SpellTypeTable;
import com.ageoflegacy.aedit.model.table.TypeTable;
import com.ageoflegacy.aedit.model.table.WeaponTypeTable;

public class Model {
	private Area area;
	private TypeTable damTypeTable;
	private TypeTable weaponTypeTable;
	private TypeTable liquidTypeTable;
	private TypeTable spellTypeTable;

	private RaceDataTable raceDataTable;

	public Model() throws IOException {
		area = new Area();

		raceDataTable = new RaceDataTable("data/race_table.csv");

		damTypeTable = new DamTypeTable("data/damtype.txt");
		weaponTypeTable = new WeaponTypeTable("data/wtype.txt");
		liquidTypeTable = new LiquidTypeTable("data/liquid.txt");
		spellTypeTable = new SpellTypeTable("data/spell.txt");
	}
	
	public Area getArea() {
		return area;
	}
	
	public void loadArea(File file) {
		RomLoader loader = new RomLoader(file);
		try {
			loader.readArea(this);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		area.transformResets();
	}

	public TypeTable getDamTypeTable() {
		return damTypeTable;
	}
	
	public TypeTable getWeaponTypeTable() {
		return weaponTypeTable;
	}

	public TypeTable getLiquidTypeTable() {
		return liquidTypeTable;
	}

	public TypeTable getSpellTypeTable() {
		return spellTypeTable;
	}
	
	public RaceDataTable getRaceDataTable() {
		return raceDataTable;
	}
}
