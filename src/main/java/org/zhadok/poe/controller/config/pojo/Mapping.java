package org.zhadok.poe.controller.config.pojo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
	
	public void setButtonName(String buttonName) {
		this.buttonName = buttonName; 
	}
	
	public Float getButtonValue() {
		return this.buttonValue; 
	}
	
	public void setButtonValue(Float buttonValue) {
		this.buttonValue = buttonValue; 
	}
	
	/**
	 * Example component names: 
	 * Button 0
	 * Button 16
	 * Hat Switch - 0.25
	 * @return
	 */
	@JsonIgnore
	public String getComponentName() {
		return getComponentName(this.getButtonName(), this.getButtonValue()); 
	}
	
	@JsonIgnore
	public String getInputStringUI() {
		String result = getButtonName(); 
		if (buttonValue != null) {
			result += " (value=" + buttonValue + ")"; 
		}
		return result; 
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
	
	public void setAction(ConfigAction action) {
		this.action = action; 
	}
	
	
	public Mapping() {
		this(null, null, null, null); 
	}
	
	@JsonCreator
	public Mapping(@JsonProperty("buttonName") String buttonName, 
			@JsonProperty("buttonValue") Float buttonValue, 
			@JsonProperty("buttonDescription") String buttonDescription, 
			@JsonProperty("action") ConfigAction action) {
		this.buttonName = buttonName; 
		this.buttonValue = buttonValue; 
		this.buttonDescription = buttonDescription; 
		this.action = action; 
	}
	
	
	@Override
	public String toString() {
		String actionsString = this.getAction() != null ? ": " + this.getAction().toString() : ""; 
		return this.getButtonName() + " (" + this.getButtonDescription() + ")" + actionsString; 
	}

	

	
}
