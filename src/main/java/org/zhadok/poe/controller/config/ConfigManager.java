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
	
	public Config getConfig() {
		if (loadedConfig == null) {
			loadedConfig = this.loadConfig(); 
		}
		return loadedConfig; 
	}
	
	private Config loadConfig() {
		ObjectMapper objectMapper = new ObjectMapper();
		File fileSettings = Constants.FILE_SETTINGS.toFile(); 
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

	
	
}
