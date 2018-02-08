package com.ageoflegacy.aedit.io;

import java.io.InputStream;
import java.net.URL;

public class ResourceLoader {
	public static URL getURL(String filename) {
		return ResourceLoader.class.getClassLoader().getResource(filename);
	}

	public static InputStream getInputStream(String filename) {
		return ResourceLoader.class.getClassLoader().getResourceAsStream(filename);
	}
}
