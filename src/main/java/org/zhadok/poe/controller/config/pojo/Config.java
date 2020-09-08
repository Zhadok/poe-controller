package org.zhadok.poe.controller.config.pojo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.zhadok.poe.controller.action.macro.MacroName;
import org.zhadok.poe.controller.config.ConfigFactory;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import net.java.games.input.Component.Identifier.Axis;

public class Config {

	private String controllerName;
	
	private ConfigCharacterMovement characterMovement;
	
	private ConfigMouseMovement mouseMovement;

	private List<Mapping> mapping; 
	
	public String getControllerName() {
		return this.controllerName; 
	}
	
	public ConfigCharacterMovement getCharacterMovement() {
		return this.characterMovement;
	}
	
	public ConfigMouseMovement getMouseMovement() {
		return this.mouseMovement;
	}
	
	public List<Mapping> getMapping() {
		return mapping; 
	}
	

	
	@JsonCreator
	public Config(@JsonProperty("controllerName") String controllerName, 
			@JsonProperty("mapping") List<Mapping> mapping) {
		this.controllerName = controllerName; 
		this.mapping = mapping; 
	}
		
	/**
	 * Check for duplicate key names
	 * @param mapping
	 */
	public void sanityCheckMapping() {
		ArrayList<String> componentNames = new ArrayList<>(); 
		for (Mapping m : getMapping()) {
			
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
	
	
	/**
	 * If a mapping with the macro CharacterMovementMapping does not exist, it is added
	 * @param macroName MacroCharacterMovement or MacroMouseMovement
	 * @param axis "x" or "y"
	 * @return
	 */
	@JsonIgnore
	public Mapping getMovementMapping(MacroName macroName, String axis) {
		Mapping characterMovementMapping = getMapping().stream()
				.filter(mapping -> mapping.hasAction()) 
				.filter(mapping -> mapping.getAction().hasMacro())
				.filter(mapping -> macroName.equals(mapping.getAction().getMacro().getName()))
				.filter(mapping -> axis.equals(mapping.getAction().getMacro().getParameter("axis")))
				.findFirst().orElse(null); 
		
		return characterMovementMapping; 
	}
	
	
	/**
	 * Adds a default mapping for character movement. The mapping will have no input (buttonName) assigned
	 * @param macroName MacroCharacterMovement or MacroMouseMovement
	 * @param axis "x" or "y"
	 * @return
	 */
	@JsonIgnore
	public Mapping addDefaultMovementMapping(MacroName macroName, String axis) {
		if (getMovementMapping(macroName, axis) != null) {
			throw new IllegalArgumentException("Movement mapping for macro='" + macroName + " with axis='" + axis + "' already exists!"); 
		}
		Mapping movementMapping = ConfigFactory.buildDefaultMovementMapping(macroName, axis); 
		getMapping().add(movementMapping); 
		return movementMapping; 
	}

	/**
	 * Attempts to two event names to character movement "x" and "y" axis
	 * 
	 * Known event names: 
	 * "X Axis", "Y Axis", "Z Axis", "Z Rotation"
	 * 
	 * Use {@link mapStickEventsToMovementBetter: Check correct instance of, is analog, and get axis
	 * 
	 * @param mappingX
	 * @param mappingY
	 * @param eventName1
	 * @param eventName2
	 */
	@Deprecated
	public void mapStickEventsToMovement(Mapping mappingX, Mapping mappingY, String eventName1, String eventName2) {
		if (eventName1.equals(eventName2)) {
			throw new IllegalArgumentException("eventName1=" + eventName1 + " should not equal eventName2"); 
		}
		// If event names are "X Axis" and "Y Axis" 
		if (eventName1.toLowerCase().replaceAll("axis", "").contains("x") 
			|| eventName2.toLowerCase().replaceAll("axis", "").contains("y")) {
			
			mappingX.setButtonName(eventName1);
			mappingY.setButtonName(eventName2);
			return; 
		} 
		if (eventName2.toLowerCase().replaceAll("axis", "").contains("x") 
			|| eventName1.toLowerCase().replaceAll("axis", "").contains("y")) {
			
			mappingX.setButtonName(eventName2);
			mappingY.setButtonName(eventName1);
			return; 
		}
		
		// If event names are "Z Axis" and "Z Rotation"
		if (eventName2.toLowerCase().contains("rotation")) {
			mappingX.setButtonName(eventName1);
			mappingY.setButtonName(eventName2);
			return; 
		}
		if (eventName1.toLowerCase().contains("rotation")) {
			mappingX.setButtonName(eventName2);
			mappingY.setButtonName(eventName1);
			return; 
		}
		
		throw new IllegalArgumentException("No combination of mapping posssible for found event names '" + 
				eventName1 + "' and '" + eventName2 + "'"); 
	}
	
	public void mapStickEventsToMovement(Mapping mappingX, Mapping mappingY,
			Map<Axis, String> mapIdentifierToComponentName) {
		if (mapIdentifierToComponentName.keySet().size() != 2) {
			throw new IllegalArgumentException("Pass in exactly two Axes, argument is: " + mapIdentifierToComponentName); 
		}
		Set<Axis> axes = mapIdentifierToComponentName.keySet(); 
		// If event names are "X Axis" and "Y Axis" 
		if (axes.contains(Axis.X) && axes.contains(Axis.Y)) {
			mappingX.setButtonName(mapIdentifierToComponentName.get(Axis.X));
			mappingY.setButtonName(mapIdentifierToComponentName.get(Axis.Y));
			return; 
		}
		// If event names are "Z Axis" and "Z Rotation"
		if (axes.contains(Axis.Z) && axes.contains(Axis.RZ)) {
			mappingX.setButtonName(mapIdentifierToComponentName.get(Axis.Z));
			mappingY.setButtonName(mapIdentifierToComponentName.get(Axis.RZ));
			return; 
		}
		
		if (axes.contains(Axis.RX) && axes.contains(Axis.RY)) {
			mappingX.setButtonName(mapIdentifierToComponentName.get(Axis.RX));
			mappingY.setButtonName(mapIdentifierToComponentName.get(Axis.RY));
			return; 
		}
		
		throw new IllegalArgumentException("No combination of mapping posssible for " + mapIdentifierToComponentName); 
		
	}

}





