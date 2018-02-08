package com.ageoflegacy.aedit.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.ageoflegacy.aedit.model.area.Area;
import com.ageoflegacy.aedit.model.table.DamTypeTable;
import com.ageoflegacy.aedit.model.table.RaceDataTable;

public class Model {
	private Area area;
	private DamTypeTable damTypeTable;
	private RaceDataTable raceDataTable;
	
	public Model() throws IOException {
		area = new Area();

		damTypeTable = new DamTypeTable("damtype.txt");
		raceDataTable = new RaceDataTable("race_table.csv");
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

	public DamTypeTable getDamTypeTable() {
		return damTypeTable;
	}

	public RaceDataTable getRaceDataTable() {
		return raceDataTable;
	}
}
