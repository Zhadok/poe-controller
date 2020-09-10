package org.zhadok.poe.controller.config.pojo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
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
		ArrayList<MappingKey> mappingKeys = new ArrayList<>(); 
		for (Mapping m : getMapping()) {
			if (mappingKeys.contains(m.getMappingKey())) {
				//throw new RuntimeException("Duplicate mapping with " + m.getMappingKey().toString()); 
			}
			mappingKeys.add(m.getMappingKey()); 
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

	public void mapStickEventsToMovement(Mapping mappingX, Mapping mappingY,
			Map<Axis, String> mapIdentifierToComponentName) {
		validateComponentNames(mapIdentifierToComponentName); 

		Set<Axis> axes = mapIdentifierToComponentName.keySet(); 

		// Combination of which axes go together, and which corresponds to X and Y
		// Nested list is: Name of X axis, name of Y axis
		List<List<Axis>> recognizedAxisSets = new ArrayList<>(); 
		
		// If event names are "X Axis" and "Y Axis" 
		recognizedAxisSets.add(Arrays.asList(Axis.X, Axis.Y)); 
		// If event names are "Z Axis" and "Z Rotation"
		recognizedAxisSets.add(Arrays.asList(Axis.Z, Axis.RZ)); 
		recognizedAxisSets.add(Arrays.asList(Axis.RX, Axis.RY)); 
		
		for (List<Axis> recognizedAxisSet : recognizedAxisSets) {
			if (recognizedAxisSet.containsAll(axes)) {
				
				mappingX.setMappingKey(new MappingKey(mapIdentifierToComponentName.get(recognizedAxisSet.get(0)), 
						recognizedAxisSet.get(0).toString(), null, true, null));
				mappingY.setMappingKey(new MappingKey(mapIdentifierToComponentName.get(recognizedAxisSet.get(1)), 
						recognizedAxisSet.get(1).toString(), null, true, null));
				return; 
			}
		}
		
		throw new IllegalArgumentException("No combination of mapping posssible for " + mapIdentifierToComponentName); 
	}

	private void validateComponentNames(Map<Axis, String> mapIdentifierToComponentName) {
		if (mapIdentifierToComponentName.keySet().size() != 2) {
			throw new IllegalArgumentException("Pass in exactly two Axes, argument is: " + mapIdentifierToComponentName); 
		}
		
		// Check that two component names are not the same
		Collection<String> values = mapIdentifierToComponentName.values(); 
		Iterator<String> iterator = values.iterator(); 
		String name1 = iterator.next(); 
		String name2 = iterator.next(); 
		if (name1.equals(name2)) {
			throw new IllegalArgumentException("Should receive two different component names, got " + name1); 
		}
	}

}





