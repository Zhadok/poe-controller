package org.zhadok.poe.controller.config;

import java.io.File;
import java.io.IOException;

import org.zhadok.poe.controller.App;
import org.zhadok.poe.controller.Constants;
import org.zhadok.poe.controller.config.pojo.Config;
import org.zhadok.poe.controller.config.pojo.ConfigContainer;
import org.zhadok.poe.controller.util.Loggable;
import org.zhadok.poe.controller.util.Util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class ConfigManager implements Loggable {

	@Override
	public int getVerbosity() {
		return App.verbosity;
	}
	
	private static ConfigManager instance = new ConfigManager(); 
	
	private Config loadedConfig; 
	
	private ConfigManager() {}
	
	public static ConfigManager getInstance() {
		return instance; 
	}
	
	public Config getLoadedConfig() {
		if (loadedConfig == null) {
			loadedConfig = this.loadConfigFromFile(); 
		}
		return loadedConfig; 
	}
	
	public void resetLoadedConfig() {
		this.loadedConfig = null;
	}
	
	public Config loadConfigFromFile() {
		return this.loadConfigFromFile(Constants.FILE_SETTINGS.toFile()); 
	}
	
	public Config loadConfigFromFile(File fileSettings) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			if (fileSettings.exists() == false) {
				this.generateDefaultConfig(fileSettings);
			}
			
			ConfigContainer result = objectMapper.readValue(fileSettings, ConfigContainer.class);
			return result.getConfigs()[0]; 
		}
		catch (Exception e) {
			log(1, "Error writing or loading settings file.");
			e.printStackTrace();
			System.exit(0);
			return null; 
		}
	}
	
	private void generateDefaultConfig(File fileOut) throws IOException {
		log(1, "Writing default settings file to " + fileOut.toPath()); 
		Util.copyFileFromResource("/default_settings.json", fileOut);
	}
	
	public void saveConfig(Config config) {
		this.saveConfig(config, Constants.FILE_SETTINGS.toFile());		
	}
	
	public void saveConfig(Config config, File file) {
		log(1, "Writing config file to " + file); 
		ConfigContainer configContainer = new ConfigContainer(); 
		configContainer.setConfigs(new Config[] {config});
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		try {
			objectMapper.writeValue(file, configContainer);
			log(1, "Config file saved."); 
		} catch (Exception e) {
			log(1, "Error saving config file!"); 
			e.printStackTrace();
			throw new RuntimeException("Error saving config file!"); 
		}
	}
	
	
	
}
