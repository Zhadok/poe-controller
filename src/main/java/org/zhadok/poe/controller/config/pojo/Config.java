package org.zhadok.poe.controller.config.pojo;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Config {

	private String controllerName;
	
	private ConfigCharacterMovement characterMovement;
	
	private ConfigMouseMovement mouseMovement;

	private Mapping[] mapping; 
	
	public String getControllerName() {
		return this.controllerName; 
	}
	
	public ConfigCharacterMovement getCharacterMovement() {
		return this.characterMovement;
	}
	
	public ConfigMouseMovement getMouseMovement() {
		return this.mouseMovement;
	}
	
	public Mapping[] getMapping() {
		return mapping; 
	}
	
	@JsonCreator
	public Config(@JsonProperty("controllerName") String controllerName, 
			@JsonProperty("mapping") Mapping[] mapping) {
		this.controllerName = controllerName; 
		this.mapping = mapping; 
		
		this.sanityCheckMapping(this.mapping); 
	}
		
	/**
	 * Check for duplicate key names
	 * @param mapping
	 */
	private void sanityCheckMapping(Mapping[] mapping) {
		ArrayList<String> componentNames = new ArrayList<>(); 
		for (Mapping m : mapping) {
			
			if (componentNames.indexOf(m.getComponentName()) >= 0) {
				throw new RuntimeException("Duplicate mapping buttonName='" + m.getComponentName() + "'"); 
			}
			componentNames.add(m.getComponentName()); 
		}
	}
	
	@Override
	public String toString() {
		String characterMovementString = this.characterMovement.toString();
		String mouseMovementString = this.mouseMovement.toString(); 
		return "Configuration for controller '" + this.getControllerName() + "':\n" + 
				characterMovementString + "\n" +
				mouseMovementString; 
	}
	
}
