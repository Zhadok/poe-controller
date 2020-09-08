package org.zhadok.poe.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zhadok.poe.controller.config.ConfigManager;
import org.zhadok.poe.controller.config.pojo.Config;
import org.zhadok.poe.controller.config.pojo.Mapping;
import org.zhadok.poe.controller.config.pojo.MappingKey;
import org.zhadok.poe.controller.util.Loggable;

import net.java.games.input.Event;


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
	 * Hat Switch 
	 * 
	 * Values are between 0 and 1
	 * 
	 * Identifiers are of class Component.Identifier 
	 */
	private Map<MappingKey, Mapping> buttonMappings; 
	
	
	public ControllerSettings() {
		this.init();
	}

	private void init() {
		this.buttonMappings = new HashMap<>(); 
		
		try {
			log(1, "Loading config from file: " + Constants.FILE_SETTINGS);
			Config config = ConfigManager.getInstance().getLoadedConfig(); 
			
			for (Mapping mapping : config.getMapping()) {
				MappingKey mappingKey = mapping.getMappingKey(); 
				buttonMappings.put(mappingKey, mapping); 
			}
			log(1, "Loaded config file."); 
			log(2, config.toString()); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private List<Mapping> getMappingsByNameAndId(MappingKey mappingKey) {
		return null; 
	}
	
	/**
	 * @param mappingKey Which combination of componentName, id and valueWhenPressed are we looking for?
	 * @param event Needed to properly map key "release" events
	 * @return
	 */
	public Mapping getMapping(MappingKey mappingKey, Event event) {
		// Problem are "Hat switch" type buttons, where one value being released leads to all buttons being released
		
		boolean isDigitalButton = mappingKey.isAnalog() == false; 
		boolean isPressed = event.getValue() > 0f; 
		// Return all mappings corresponding to {componentName, id} 
		// because at this point we don't know what the correct valueWhenPressed is
		
		return this.buttonMappings.get(mappingKey); 
	}

	@Override
	public int getVerbosity() {
		return App.verbosity;
	}
	
}
