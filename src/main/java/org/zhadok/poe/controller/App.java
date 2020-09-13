package org.zhadok.poe.controller;
import java.awt.AWTException;
import java.io.File;
import java.io.IOException;

import org.zhadok.poe.controller.config.ui.ConfigMappingUI;
import org.zhadok.poe.controller.lib.JInputLib;
import org.zhadok.poe.controller.util.Loggable;
import org.zhadok.poe.controller.util.Util;

/**
 * This class shows how to use the event queue system in JInput. It will show
 * how to get the controllers, how to get the event queue for a controller, and
 * how to read and process events from the queue.
 * 
 * @author Endolf
 */
public class App implements Loggable {

	/**
	 * Setting the path to native files: 
	 * https://github.com/jinput/jinput/issues/42
	 */
	static {
		String libraryPath = new File("poe-controller-files/lib").getAbsolutePath(); 
		System.out.println("Setting net.java.games.input.librarypath=" + libraryPath);
		System.setProperty("net.java.games.input.librarypath", libraryPath);
	}
	
	public static int verbosity = Constants.DEFAULT_VERBOSITY; 
//	private static boolean running = false;
	
	public int getVerbosity() {
		return verbosity; 
	}
	
	
	private static ConfigMappingUI startConfigMappingUI(ControllerEventHandler eventHandler) {
		ConfigMappingUI window = new ConfigMappingUI(eventHandler);
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window.initialize(); 
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		return window; 
	}
	
	/**
	 * @param args
	 * @throws AWTException
	 * @throws IOException
	 */
	public static void main(String[] args) throws AWTException, IOException {
		Util.ensureProjectDirExists(); 
		Loggable.writeLogsToFile(Constants.FILE_LOG); 
		
		Util.getJavaRuntime().forEach(detail -> System.out.println(detail)); 
		
		JInputLib lib = new JInputLib();
		lib.prepare(); 

		ControllerEventHandler eventHandler = new ControllerEventHandler(); 
		if (System.getProperty("verbosity") != null) {
			App.verbosity = Integer.valueOf(System.getProperty("verbosity")); 
			eventHandler.log(0, "Setting verbosity to " + verbosity);
		}
		else {
			eventHandler.log(0, "Setting verbosity to " + verbosity + " (default value)");
		}
		eventHandler.resetControllerMappingListener();
		ConfigMappingUI window = startConfigMappingUI(eventHandler); 
		eventHandler.registerEventListener(window);
		
		if (Util.isJava32Bit()) {
			eventHandler.log(1, "WARNING: You appear to be using 32-bit Java. If the program crashes please try " + 
					   "uninstalling 32-bit Java and installing 64-bit Java."); 
		}
		
		eventHandler.startPolling(); 
	}

	
	
}
