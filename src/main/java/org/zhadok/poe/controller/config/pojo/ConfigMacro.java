package org.zhadok.poe.controller.config.pojo;

import java.util.HashMap;

import org.zhadok.poe.controller.action.macro.MacroName;


public class ConfigMacro {
	
	private MacroName name; 
	
	private String iconPath; 
	
	private HashMap<String,Object> parameters;
	

	public HashMap<String,Object> getParameters() {
		return this.parameters; 
	}
	public void setParameters(HashMap<String,Object> parameters) {
		this.parameters = parameters; 
	}
	
	public Object getParameter(String parameterName) {
		if (this.parameters == null)
			return null; 
		return this.parameters.get(parameterName);
	}

	public void setParameter(String parameterName, Object value) {
		if (this.parameters == null) {
			this.parameters = new HashMap<>(); 
		}
		this.parameters.put(parameterName, value); 
	}
	
	@Override
	public String toString() {
		return "Macro (" + this.getName() + ")" + ((this.getParameters() != null) ? ": " + this.getParameters() : ""); 
	}

	public MacroName getName() {
		return name;
	}
	
	public void setName(MacroName name) {
		this.name = name;
	}

	public String getIconPath() {
		return iconPath;
	}

	
}