package org.zhadok.poe.controller.config.pojo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import net.java.games.input.Event;

/**
 * Used to identify the exact method of input being used on the controller, for example
 * a button or a joystick
 */
public class MappingKey {
	
	/**
	 * Example:
	 * Hat switch
	 * Axis X
	 * Button 1
	 */
	private final String componentName;
	
	/**
	 * Example: 
	 * 'pov' for hat switch
	 * 'x' for joysticks
	 * '1' for Button 1
	 */
	private final String id; 
	private final Float valueWhenPressed; 
	private final boolean analog;
	
	/**
	 * Optional field, not used for mapping
	 */
	private String buttonDescription; 

	@JsonCreator
	public MappingKey(
			@JsonProperty("componentName") String componentName, 
			@JsonProperty("id") String id, 
			@JsonProperty("valueWhenPressed") Float valueWhenPressed, 
			@JsonProperty("analog") boolean analog,
			@JsonProperty("buttonDescription") String buttonDescription) {
		this.componentName = componentName;
		this.id = id;
		this.valueWhenPressed = valueWhenPressed;
		this.analog = analog; 
		this.buttonDescription = buttonDescription;
	}
	
	public MappingKey(Event inputEvent) {
		this(inputEvent.getComponent().getName(), 
			 inputEvent.getComponent().getIdentifier().toString(), 
			 // Value should only be recorded if digital button, not analog (e.g. joystick)
			 inputEvent.getComponent().isAnalog() ? null : inputEvent.getValue(), 
			 inputEvent.getComponent().isAnalog(),
			 null);
	}

	public String getId() {
		return id;
	}

	public String getComponentName() {
		return componentName;
	}

	public boolean isAnalog() {
		return analog;
	}

	public Float getValueWhenPressed() {
		return valueWhenPressed;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((componentName == null) ? 0 : componentName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + (analog ? 1231 : 1237);
		result = prime * result + ((valueWhenPressed == null) ? 0 : valueWhenPressed.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MappingKey other = (MappingKey) obj;
		if (componentName == null) {
			if (other.componentName != null)
				return false;
		} else if (!componentName.equals(other.componentName))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (analog != other.analog)
			return false;
		if (valueWhenPressed == null) {
			if (other.valueWhenPressed != null)
				return false;
		} else if (!valueWhenPressed.equals(other.valueWhenPressed))
			return false;
		return true;
	}

	public String getButtonDescription() {
		return buttonDescription;
	}

	public void setButtonDescription(String buttonDescription) {
		this.buttonDescription = buttonDescription;
	}

	@Override
	public String toString() {
		return "MappingKey [componentName='" + componentName + "', id='" + id + "', valueWhenPressed=" + valueWhenPressed
				+ ", isAnalog=" + analog + "]";
	}
	
	@JsonIgnore
	public String toStringUI() {
		String result = this.getComponentName(); 
		if (this.getValueWhenPressed() != null) {
			result += " (value=" + this.getValueWhenPressed() + ")"; 
		}
		return result; 
	}

	
}
