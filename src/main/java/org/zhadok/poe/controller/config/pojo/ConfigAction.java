package org.zhadok.poe.controller.config.pojo;

import java.awt.event.KeyEvent;
import java.lang.reflect.Field;

import org.zhadok.poe.controller.mouse.MouseAction;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ConfigAction {

	/**
	 * Used for keyboard keys
	 */
	private String key; 
	
	/**
	 * Used for mouse actions (left click, left press, left release)
	 */
	private MouseAction mouse; 
	
	/**
	 * Used for more complex actions (character movement, mouse movement)
	 */
	private ConfigMacro macro; 
	
	private String description; 
	
	@JsonIgnore
	private int keyEventCode; 
	
	
	public String getKey() {
		return this.key; 
	}
	public boolean hasKey() {
		return this.key != null; 
	}
	
	public MouseAction getMouseAction() {
		return this.mouse; 
	}
	public boolean hasMouseAction() {
		return this.mouse != null; 
	}
	
	public ConfigMacro getMacro() {
		return this.macro; 
	}
	public boolean hasMacro() {
		return this.macro != null;
	}
	
	
	public String getDescription() {	
		return this.description; 
	}
	
	public int getKeyEventCode() {
		return this.keyEventCode; 
	}
	
	public ConfigAction(String targetKey) {
		this(targetKey, null, null, null); 
	}
	
	@JsonCreator
	public ConfigAction(@JsonProperty("key") String targetKey,
			@JsonProperty("mouse") MouseAction mouse, 
			@JsonProperty("macro") ConfigMacro macro, 
			@JsonProperty("description") String description) {
		this.key = targetKey; 
		this.mouse = mouse; 
		this.macro = macro; 
		this.description = description; 
		
		
		int nParams = (targetKey==null ? 0 : 1) + 
				(mouse==null ? 0 : 1) + 
				(macro==null ? 0 : 1); 
		if (nParams != 1) {
			throw new RuntimeException("Please pass exactly one of key, mouse or macro (description='" + this.description + "'), nParams=" + nParams); 
		}
		
		
		
		if (targetKey != null) {
			try {
				this.keyEventCode = this.mapStringToKeyEventCode(this.key); 
			}
			catch (Exception e) {
				throw new RuntimeException("No key event code found for key='" + this.key + "' and description='" + this.description + "'"); 
			}
		}
	
	}
	
	public int mapStringToKeyEventCode(String key) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		String fieldName = "VK_" + key; 
		Field field = KeyEvent.class.getField(fieldName);    
		// field.setAccessible(true);
		int result = (int) field.get(null);
		return result; 
	}
	
	
	
	@Override
	public String toString() {
		if (this.hasKey()) {
			return "'" + this.getKey() + "' (" + this.getKeyEventCode() + ")"; 
		}
		else if (this.hasMouseAction()) {
			return this.getMouseAction().toString(); 
		}
		else {
			return this.getMacro().toString(); 
		}
		
	}
	
}
