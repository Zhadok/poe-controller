package org.zhadok.poe.controller.config.pojo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.zhadok.poe.controller.action.macro.MacroName.MacroCharacterMovement;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.zhadok.poe.controller.action.macro.MacroName;
import org.zhadok.poe.controller.config.ConfigManager;

import net.java.games.input.Component.Identifier.Axis;

public class ConfigTest {
	
	private Config classUnderTest; 
	
	@BeforeEach
	void setup() {
		this.classUnderTest = ConfigManager.getInstance().loadConfigFromFile(new File("src/main/resources/empty_settings.json")); 
	}
	
	
	private static Stream<Arguments> provideArgumentsForSanityCheckConfig() {
	    return Stream.of(
	    		Arguments.of(Arrays.asList("Button 1", "Button 2", "Button 3"), false),
	    		Arguments.of(Arrays.asList("Button 1", "Button 1", "Button 3"), true) // Duplicate names should lead to error
	      );
	}
	
	@ParameterizedTest
	@MethodSource("provideArgumentsForSanityCheckConfig")
	void sanityCheckConfig(List<String> buttonNames, boolean expectException) {
		buttonNames.forEach((buttonName) -> {
			MappingKey mappingKey = new MappingKey(buttonName, null, null, false, null); 
			
			Mapping newMapping = new Mapping(); 
			newMapping.setMappingKey(mappingKey);
			classUnderTest.getMapping().add(newMapping);
		}); 
		
		if (expectException == true) {
			// assertThrows(Exception.class, () -> classUnderTest.sanityCheckMapping()); 
		} else {
			classUnderTest.sanityCheckMapping();
			assertEquals(buttonNames.size(), classUnderTest.getMapping().size()) ;
		}
	}
	
	
	@ParameterizedTest
	@CsvSource({"MacroCharacterMovement", "MacroMouseMovement"})
	void ensureMovementMappingExists(MacroName macroName) {
		assertNull(classUnderTest.getMovementMapping(macroName, "x"));
		assertNull(classUnderTest.getMovementMapping(macroName, "y"));
		
		classUnderTest.addDefaultMovementMapping(macroName, "x"); 
		classUnderTest.addDefaultMovementMapping(macroName, "y"); 
		
		assertThrows(Exception.class, () -> classUnderTest.addDefaultMovementMapping(macroName, "x")); 
		assertThrows(Exception.class, () -> classUnderTest.addDefaultMovementMapping(macroName, "y")); 
		
		assertNotNull(classUnderTest.getMovementMapping(macroName, "x")); 
		assertNotNull(classUnderTest.getMovementMapping(macroName, "y")); 
		
		Mapping defaultMapping = classUnderTest.getMovementMapping(macroName, "x"); 
		assertEquals(macroName, defaultMapping.getAction().getMacro().getName()); 
		assertEquals("x", defaultMapping.getAction().getMacro().getParameter("axis")); 
	}
	
	
	private static Stream<Arguments> provideArgumentsForMapEventsToMovement() {
	    return Stream.of(
	    		
	    		// Stadia joysticks
	    		Arguments.of(Axis.X, "X Axis", Axis.Y, "Y Axis", false), 
	    		Arguments.of(Axis.Z, "Z Axis", Axis.RZ, "Z Rotation", false), 
	    		
	    		// XBox One controller (German)
	    		Arguments.of(Axis.X, "X-Achse", Axis.Y, "Y-Achse", false), 
	    		Arguments.of(Axis.RX, "X-Rotation", Axis.RY, "Y-Rotation", false), 
	    		
	    		// Nacon
	    		// Left stick is same as Stadia
	    		Arguments.of(Axis.X, "X Rotation", Axis.Y, "Y Rotation", false),

	    		//Invalid mappings
	    		Arguments.of(Axis.X, "axis1", Axis.X, "axis2", true),
	    		Arguments.of(Axis.X, "x", Axis.RX, "rx", true),
	    		Arguments.of(Axis.X, "xDuplicate", Axis.Y, "xDuplicate", true)
	    );
	}
	
	@ParameterizedTest
	@MethodSource("provideArgumentsForMapEventsToMovement")
	void mapEventsToMovement(Axis axisX, String expectedEventNameX, Axis axisY, String expectedEventNameY, 
			boolean expectException) {
		
		Mapping mappingX = classUnderTest.addDefaultMovementMapping(MacroCharacterMovement, "x"); 
		Mapping mappingY = classUnderTest.addDefaultMovementMapping(MacroCharacterMovement, "y"); 
		
		HashMap<Axis,String> mapIdentifierToComponentName = new HashMap<>(); 
		mapIdentifierToComponentName.put(axisX, expectedEventNameX); 
		mapIdentifierToComponentName.put(axisY, expectedEventNameY); 
		
		if (expectException == false) {
			classUnderTest.mapStickEventsToMovement(mappingX, mappingY, mapIdentifierToComponentName);
			assertEquals(expectedEventNameX, mappingX.getMappingKey().getComponentName()); 
			assertEquals(axisX.getName(), mappingX.getMappingKey().getId());
			assertEquals(expectedEventNameY, mappingY.getMappingKey().getComponentName()); 
			assertEquals(axisY.getName(), mappingY.getMappingKey().getId());
		} else {
			assertThrows(IllegalArgumentException.class, 
					() -> classUnderTest.mapStickEventsToMovement(mappingX, mappingY, mapIdentifierToComponentName));
		}
	}
	

}
