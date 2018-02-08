// George Frick
// AreaEditor.java
// Area Editor Project, spring 2002
//
// This is the main class of the Area editor, it's purpose being to set
// up the main gui and start the components up. The program is event
// based.

package com.ageoflegacy.aedit;

import java.io.File;

import com.ageoflegacy.aedit.model.Model;

public class AreaEditor {
	// Main function
	public static void main(String[] args) {
		// initialize model
		try {
			Model model = new Model();
		
			if (args.length > 0) {
				model.loadArea(new File(args[0]));
			}
			else {
				// start GUI
				AreaEditorFrame ed = new AreaEditorFrame("Animud/Rom/Cynthe Area Editor", model);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
