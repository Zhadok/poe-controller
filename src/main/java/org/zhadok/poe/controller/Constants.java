package org.zhadok.poe.controller;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Constants {
	
	public static final int DEFAULT_VERBOSITY = 1;
	
	public static final Path DIR_JAVA_TEMP = Paths.get(System.getProperty("java.io.tmpdir"));
	
	public static final Path DIR_PROJECT = Paths.get("poe-controller-files");
	
	public static final Path DIR_LIB = DIR_PROJECT.resolve("lib");

	public static final Path FILE_SETTINGS = DIR_PROJECT.resolve("settings.json");
	
	public static final Path FILE_LOG = DIR_PROJECT.resolve("poe-controller.log"); 

}
