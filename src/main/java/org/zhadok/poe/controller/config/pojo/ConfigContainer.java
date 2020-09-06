package org.zhadok.poe.controller.config.pojo;

import org.zhadok.poe.controller.util.Util;


public class ConfigContainer {

	private Config[] configs; 

	public Config[] getConfigs() {
		return configs; 
	}
	
	public void setConfigs(Config[] configs) {
		this.configs = configs; 
	}
	
	@Override
	public String toString() {
		String result = Util.stringJoin("\n", this.getConfigs()); 
		return result;
	}
	
	
}
