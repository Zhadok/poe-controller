package org.zhadok.poe.controller.util;

import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

import org.zhadok.poe.controller.Constants;

public class Util {

	public static Point getScreenCenter() {
	    Dimension dimension = Util.getScreenSize(); 
	    int x = (int) (dimension.getWidth() / 2);
	    int y = (int) (dimension.getHeight() / 2);
	    return new Point(x, y); 
	}
	
	public static Dimension getScreenSize() {
		return Toolkit.getDefaultToolkit().getScreenSize();
	}
	
	public static Point getMouseLocation() {
		PointerInfo pointerInfo = MouseInfo.getPointerInfo(); 
		java.awt.Point location; 
		if (pointerInfo != null) {
			location = pointerInfo.getLocation();
		}
		else {
			Point screenCenter = Util.getScreenCenter(); 
			location = new java.awt.Point((int) screenCenter.x, (int) screenCenter.y); 
		}
		return new Point(location.x, location.y); 
	}
	
	public static String formatDouble(double number) {
		DecimalFormat df = new DecimalFormat("#.#");
		df.setRoundingMode(RoundingMode.FLOOR);
		return df.format(number);
	}
	
	
	public static String capitalize(String input) {
		return input.substring(0,1).toUpperCase() + input.substring(1); 
	}
	
	public static String stringJoin(String joiner, Object[] objects) {
		 return String.join(joiner, Arrays.stream(objects).map(obj->obj.toString()).toArray(String[]::new)); 
	}

	public static void resetClipboard() {
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		StringSelection stringSelection = new StringSelection("");
		clipboard.setContents(stringSelection, null);
	}
	
	public static String getClipboard() {
		String data = "";
		try {
			data = (String) Toolkit.getDefaultToolkit()
			        .getSystemClipboard().getData(DataFlavor.stringFlavor);
		} catch (HeadlessException | UnsupportedFlavorException | IOException e) {
			e.printStackTrace();
		} 
		return data; 
	}

	
	public static void ensureProjectDirExists() {
		File projectTempDir = Constants.DIR_PROJECT.toFile(); 
		if (projectTempDir.exists() == false) {
			System.out.println("Util::ensureProjectDirExists Creating directory " + projectTempDir);
			projectTempDir.mkdir();
		}
		File nativeLibsDir = Constants.DIR_LIB.toFile(); 
		if (nativeLibsDir.exists() == false) {
			System.out.println("Util::ensureProjectDirExists Creating directory " + nativeLibsDir);
			nativeLibsDir.mkdir(); 
		}
	}

	public static void copyFileFromResource(String resourcePath, File fileOut) throws IOException {
		InputStream resourceStream = Util.class.getResourceAsStream(resourcePath);
		if (resourceStream == null || fileOut == null) {
			throw new NullPointerException("Unable to get resource as stream, resourcePath='" + resourcePath + "', fileOut=" + fileOut); 
		}
		Files.copy(resourceStream, fileOut.toPath()); 		
	}
	
	public static boolean runningFromJar() {
		String protocol = Util.class.getResource("").getProtocol();
		return "jar".equals(protocol);  
	}

	public static List<String> getJavaRuntime() {
		return Arrays.asList("Java Virtual Machine specification version: " + System.getProperty("java.vm.specification.version"),
				"Java Virtual Machine specification vendor: " + System.getProperty("java.vm.specification.vendor"),
				"Java Virtual Machine specification name: " + System.getProperty("java.vm.specification.name"),
				"Java Virtual Machine implementation version: " + System.getProperty("java.vm.version"),
				"Java Virtual Machine implementation vendor: " + System.getProperty("java.vm.vendor"),
				"Java Virtual Machine implementation name: " + System.getProperty("java.vm.name"),
				"Java Virtual Machine 32 bit or 64 bit: " + System.getProperty("sun.arch.data.model")); 
	}
	
	/**
	 * https://stackoverflow.com/questions/2062020/how-can-i-tell-if-im-running-in-64-bit-jvm-or-32-bit-jvm-from-within-a-program
	 * @return
	 */
	public static boolean isJava32Bit() {
		try {
			int bits = Integer.parseInt(System.getProperty("sun.arch.data.model"));
			return bits == 32; 
		} catch (Exception e) {
			return false; 
		}
	}

}




