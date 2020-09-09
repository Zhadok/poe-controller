package org.zhadok.poe.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
	
	/**
	 * Returns all mapping keys with matching componentName and Id
	 * @param otherMappingKey
	 * @return
	 */
	private List<MappingKey> getMappingKeysByNameAndId(MappingKey otherMappingKey) {
		
		return this.buttonMappings.keySet().stream()
			.filter(mappingKey -> mappingKey != null) 
			.filter(mappingKey -> mappingKey.getComponentName() != null && mappingKey.getId() != null) 
			.filter(mappingKey -> mappingKey.getComponentName().equals(otherMappingKey.getComponentName()) && 
								  mappingKey.getId().equals(otherMappingKey.getId()))
			.collect(Collectors.toList());
	}
	
	/**
	 * Returns a list of mappings which are relevant to the mappingKey and event
	 * 
	 * 
	 * @param mappingKey Which combination of componentName, id and valueWhenPressed are we looking for?
	 * @param event Needed to properly map key "release" events
	 * @return
	 */
	public List<Mapping> getRelevantMappings(MappingKey mappingKey, Event event) {
		boolean isDigitalButton = mappingKey.isAnalog() == false; 
		boolean isPressed = event.getValue() > 0f; 
		
		if (isDigitalButton == true && isPressed == false) {
			// Problem are "Hat switch" type buttons, where one value being released has to lead to all buttons being released
			// Return all mappings corresponding to {componentName, id} on button release
			// when mapped to a ConfigAction with a key
			// because at this point we don't know what the correct valueWhenPressed is
			// And then release all those keys in ActionHandlerKey
			return getMappingKeysByNameAndId(mappingKey).stream()
					.map(mappingKeyForKeyRelease -> this.buttonMappings.get(mappingKeyForKeyRelease))
					.filter(mapping -> mapping.hasAction() && mapping.getAction().hasKey()) 
					.collect(Collectors.toList()); 
		}
		else if (this.buttonMappings.get(mappingKey) != null) {
			return Arrays.asList(this.buttonMappings.get(mappingKey)); 
		}
		else {
			return new ArrayList<>(); 
		}
	}

	@Override
	public int getVerbosity() {
		return App.verbosity;
	}
	
}
