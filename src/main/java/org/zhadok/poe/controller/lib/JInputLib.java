package org.zhadok.poe.controller.lib;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.zhadok.poe.controller.App;
import org.zhadok.poe.controller.Constants;
import org.zhadok.poe.controller.util.Loggable;
import org.zhadok.poe.controller.util.Util;

public class JInputLib implements Loggable {
	
	/**
	 * Native files should be under src/main/resources/natives
	 */
	private final String[] nativeFiles = new String[] {
			"jinput-raw_64.dll",
			"jinput-dx8_64.dll",
			"jinput-wintab.dll",
			"libjinput-linux64.so",
			"libjinput-osx.jnilib"
	}; 
	
	@Override
	public int getVerbosity() {
		return App.verbosity;
	}
	
	/**
	 * Copy JInput native files there
	 * Set the directory as java.library.path
	 * @throws IOException 
	 * 
	 * https://stackoverflow.com/questions/1611357/how-to-make-a-jar-file-that-includes-dll-files
	 * 
	 */
	public void prepare() throws IOException {
		log(1, "Preparing JInput natives in directory: " + Constants.DIR_LIB);
		
		// If we are outside the jar they are in target/natives/
		// If we are within the jar they are in root dir
		for (String nativeFile : nativeFiles) {
			File fileOut = Constants.DIR_LIB.resolve(nativeFile).toFile(); 
			if (fileOut.exists() == false) {
				String resourcePath = "/natives/" + nativeFile;
				//System.out.println(getClass().getResource(resourcePath));
				log(1, "Writing file " + resourcePath); 
				
				Util.copyFileFromResource(resourcePath, fileOut);
			}
			else {
				log(1, "File already exists: " + fileOut.toPath()); 
			}
		}
	}

	public void checkLibFiles() {
		String javaLibraryPath = System.getProperty("java.library.path"); 
		
		log(1, "java.library.path: " + javaLibraryPath);
		
		// Check that each file exists via java.library.path
		for (String nativeFile : nativeFiles) {
			Path pathToNativeFile = Paths.get(javaLibraryPath, nativeFile); 
			File file = pathToNativeFile.toFile();
			log(1, "Native file exists on library path: " + file.exists() + " (" + pathToNativeFile.toString() + ")");
			
			System.loadLibrary(nativeFile); 
			log(1, "Library successfully loaded."); 
		}
	}
	
}
