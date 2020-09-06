package org.zhadok.poe.controller.config;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.zhadok.poe.controller.config.pojo.Config;

public class ConfigManagerTest {
	
	private ConfigManager classUnderTest; 
	private final File defaultSettingsFile = new File("src/main/resources/default_settings.json"); 
	
	@BeforeEach
	void setup() {
		classUnderTest = ConfigManager.getInstance(); 
	}	
	
	@Test
	void defaultConfigCanBeLoaded() {
		Config config = classUnderTest.loadConfigFromFile(defaultSettingsFile); 
		assertEquals(0, config.getMapping().size());
		assertNotNull(config.getControllerName()); 
	}
	
	@Test
	void saveAndLoadConfig() {
		Config defaultConfig = classUnderTest.loadConfigFromFile(defaultSettingsFile); 
		
		File tempConfigFile = Paths.get(System.getProperty("java.io.tmpdir"), "temp-poe-controller-settings.json").toFile(); 
		classUnderTest.saveConfig(defaultConfig, tempConfigFile);
		Config loadedConfig = classUnderTest.loadConfigFromFile(tempConfigFile); 
		assertEquals(0, loadedConfig.getMapping().size());
		assertNotNull(loadedConfig.getControllerName()); 
		
		tempConfigFile.delete(); 
	}
	
}





