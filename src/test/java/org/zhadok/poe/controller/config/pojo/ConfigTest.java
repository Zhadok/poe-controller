package org.zhadok.poe.controller.config.pojo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.zhadok.poe.controller.action.macro.MacroName.MacroCharacterMovement;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.zhadok.poe.controller.action.macro.MacroName;
import org.zhadok.poe.controller.config.ConfigManager;

public class ConfigTest {
	
	private Config classUnderTest; 
	
	@BeforeEach
	void setup() {
		this.classUnderTest = ConfigManager.getInstance().loadConfigFromFile(new File("src/main/resources/default_settings.json")); 
	}
	
	
	private static Stream<Arguments> provideArgumentsForSanityCheckConfig() {
	    return Stream.of(
	    		Arguments.of(Arrays.asList("Button 1", "Button 2", "Button 3"), false),
	    		Arguments.of(Arrays.asList("Button 1", "Button 1", "Button 3"), true)
	      );
	}
	
	@ParameterizedTest
	@MethodSource("provideArgumentsForSanityCheckConfig")
	void sanityCheckConfig(List<String> buttonNames, boolean expectException) {
		buttonNames.forEach((buttonName) -> {
			Mapping newMapping = new Mapping(); 
			newMapping.setButtonName(buttonName);
			classUnderTest.getMapping().add(newMapping) ;
		}); 
		
		if (expectException == true) {
			assertThrows(Exception.class, () -> classUnderTest.sanityCheckMapping()); 
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
	    		Arguments.of("X Axis", "Y Axis", "X Axis", "Y Axis", false),
	    		Arguments.of("Z Axis", "Z Rotation", "Z Axis", "Z Rotation", false),
	    		// XBox One controller (German)
	    		Arguments.of("X-Achse", "Y-Achse", "X-Achse", "Y-Achse", false),
	    		Arguments.of("X-Rotation", "Y-Rotation", "X-Rotation", "Y-Rotation", false),
	    		// Nacon
	    		// Left stick is same as Stadia
	    		Arguments.of("X Rotation", "Y Rotation", "X Rotation", "Y Rotation", false),
	    		
	    		// (Other language)
	    		
	    		// Invalid mappings
	    		Arguments.of("X Axis", "X Axis", null, null, true),
	    		Arguments.of("Custom Axis 1", "Custom Axis 2", null, null, true)
	    );
	}
	
	@ParameterizedTest
	@MethodSource("provideArgumentsForMapEventsToMovement")
	void mapEventsToMovement(String eventName1, String eventName2, 
			String mappingXButtonName, String mappingYButtonName, 
			boolean expectException) {
		
		Mapping mappingX = classUnderTest.addDefaultMovementMapping(MacroCharacterMovement, "x"); 
		Mapping mappingY = classUnderTest.addDefaultMovementMapping(MacroCharacterMovement, "y"); 
		
		if (expectException == false) {
			classUnderTest.mapStickEventsToMovement(mappingX, mappingY, eventName1, eventName2); 
			assertEquals(mappingXButtonName, mappingX.getButtonName()); 
			assertEquals(mappingYButtonName, mappingY.getButtonName()); 
			
			// Check mirror case
			classUnderTest.mapStickEventsToMovement(mappingX, mappingY, eventName2, eventName1); 
			assertEquals(mappingXButtonName, mappingX.getButtonName()); 
			assertEquals(mappingYButtonName, mappingY.getButtonName()); 
			
		} else {
			assertThrows(IllegalArgumentException.class, 
					() -> classUnderTest.mapStickEventsToMovement(mappingX, mappingY, eventName1, eventName2));
		}
		
		
	}

}
