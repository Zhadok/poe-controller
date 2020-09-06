package org.zhadok.poe.controller;

import java.util.HashMap;

import org.zhadok.poe.controller.config.ConfigManager;
import org.zhadok.poe.controller.config.pojo.Config;
import org.zhadok.poe.controller.config.pojo.Mapping;
import org.zhadok.poe.controller.util.Loggable;


public class ControllerSettings implements Loggable {
	
	/**
	 * Poll interval (in ms) for right stick (mouse movement)
	 */
	
	
	public final int POLL_LEFT_CLICK_INTERVAL_MS = 100;
	
	
	/**
	 * HashMap for finding which Mapping (and actions) belong to which component name
	 * 
	 * Example component names: 
	 * Button 0
	 * Button 16
	 * Hat Switch - 0.25
	 */
	private HashMap<String, Mapping> buttonMappings; 
	
	
	
	public ControllerSettings() {
		this.init();
	}

	private void init() {
		this.buttonMappings = new HashMap<>(); 
		
		try {
			log(1, "Loading config from file: " + Constants.FILE_SETTINGS);
			Config config = ConfigManager.getInstance().getLoadedConfig(); 
			
			for (Mapping m : config.getMapping()) {
				String componentName = m.getComponentName(); 
				buttonMappings.put(componentName, m); 
			}
			log(1, "Loaded config file."); 
			log(2, config.toString()); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Mapping getMapping(String componentName) {
		return this.buttonMappings.get(componentName); 
	}

	@Override
	public int getVerbosity() {
		return App.verbosity;
	}
	
}
