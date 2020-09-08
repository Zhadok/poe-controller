package org.zhadok.poe.controller.config.pojo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Mapping {

	private MappingKey mappingKey; 
	private ConfigAction action; 
	
	public MappingKey getMappingKey() {
		return this.mappingKey; 
	}
	
	public void setMappingKey(MappingKey mappingKey) {
		this.mappingKey = mappingKey; 
	}
	
	public boolean hasMappingKey() {
		return this.mappingKey != null; 
	}
	
	public ConfigAction getAction() {
		return this.action; 
	}
	
	public void setAction(ConfigAction action) {
		this.action = action; 
	}
	
	public boolean hasAction() {
		return this.getAction() != null;
	}
	
	public Mapping() {
		this(null, null); 
	}
	
	@JsonCreator
	public Mapping(@JsonProperty("mappingKey") MappingKey mappingKey, 
			@JsonProperty("action") ConfigAction action) {
		this.mappingKey = mappingKey; 
		this.action = action; 
	}
	
	@Override
	public String toString() {
		String mappingKeyString = this.getMappingKey() != null ? this.getMappingKey().toString() : ""; 
		String actionString = this.hasAction() ? this.getAction().toString() : ""; 
		
		String result = mappingKeyString;
		if (this.hasMappingKey() && this.hasAction()) {
			result += ": "; 
		}
		result += actionString; 
		return result; 
	}
	
	

}
