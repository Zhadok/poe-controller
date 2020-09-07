package org.zhadok.poe.controller.config.pojo;

public class ConfigMouseMovement {
	
	private double mouseMoveSensitivity;
	private double stickThreshold; 
	
	private int pollRightStickIntervalMS;
	
	public double getMouseMoveSensitivity() {
		return this.mouseMoveSensitivity;
	}
	
	public void setMouseMoveSensitivity(double value) {
		this.mouseMoveSensitivity = value; 
	}

	public int getPollRightStickIntervalMS() {
		return pollRightStickIntervalMS;
	}
	
	public double getStickThreshold() {
		return stickThreshold;
	}

	public void setStickThreshold(double stickThreshold) {
		this.stickThreshold = stickThreshold;
	}

	@Override
	public String toString() {
		return "ConfigMouseMovement [mouseMoveSensitivity=" + mouseMoveSensitivity + ", stickThreshold="
				+ stickThreshold + ", pollRightStickIntervalMS=" + pollRightStickIntervalMS + "]";
	}
	
}
