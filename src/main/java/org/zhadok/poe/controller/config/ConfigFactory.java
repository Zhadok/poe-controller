package org.zhadok.poe.controller.config;

import org.zhadok.poe.controller.action.macro.MacroName;
import org.zhadok.poe.controller.config.pojo.ConfigAction;
import org.zhadok.poe.controller.config.pojo.ConfigMacro;
import org.zhadok.poe.controller.config.pojo.Mapping;

public class ConfigFactory {

	
	public static Mapping buildDefaultMovementMapping(MacroName macroName, String axis) {
		ConfigMacro macro = new ConfigMacro(); 
		macro.setName(macroName);
		macro.setParameter("axis", axis); 
		
		ConfigAction action = new ConfigAction(null, null, macro, null); 
		
		Mapping result = new Mapping(); 
		result.setAction(action);
		return result; 
	}
	
	
	
	
		
}
