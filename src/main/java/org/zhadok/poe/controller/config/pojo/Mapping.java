package org.zhadok.poe.controller.config.pojo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Mapping {

	private String buttonName; 

	/**
	 * Optional: Only trigger if buttonName AND buttonValue matches
	 *
	 * Is null if not passed. 
	 */
	private Float buttonValue; 
	
	private String buttonDescription; 
	
	private ConfigAction action; 
	
	public String getButtonName() {
		return this.buttonName;  
	}
	
	public Float getButtonValue() {
		return this.buttonValue; 
	}
	
	/**
	 * Example component names: 
	 * Button 0
	 * Button 16
	 * Hat Switch - 0.25
	 * @return
	 */
	public String getComponentName() {
		return getComponentName(this.getButtonName(), this.getButtonValue()); 
	}
	
	public static String getComponentName(String buttonName, Float buttonValue) {
		return buttonName + (buttonValue != null ? " - " + buttonValue : ""); 
	}
	
	public String getButtonDescription() {
		return this.buttonDescription; 
	}
	
	public ConfigAction getAction() {
		return this.action; 
	}
	
	public Mapping(String buttonName) {
		this(buttonName, null, null, null); 
	}
	
	@JsonCreator
	public Mapping(@JsonProperty("buttonName") String buttonName, 
			@JsonProperty("buttonValue") Float buttonValue, 
			@JsonProperty("buttonDescription") String buttonDescription, 
			@JsonProperty("actions") ConfigAction action) {
		this.buttonName = buttonName; 
		this.buttonValue = buttonValue; 
		this.buttonDescription = buttonDescription; 
		this.action = action; 
	}
	
	
	@Override
	public String toString() {
		String actionsString = this.getAction().toString(); 
		return this.getButtonName() + " (" + this.getButtonDescription() + "): " + actionsString; 
	}
	
}
