package org.zhadok.poe.controller.config.pojo;

public class ConfigMouseMovement {
	
	private double mouseMoveSensitivity;
	
	private int pollRightStickIntervalMS;
	
	public double getMouseMoveSensitivity() {
		return this.mouseMoveSensitivity;
	}

	public int getPollRightStickIntervalMS() {
		return pollRightStickIntervalMS;
	}
	
	@Override
	public String toString() {
		return "ConfigMouseMovement [mouseMoveSensitivity=" + mouseMoveSensitivity + ", pollRightStickIntervalMS="
				+ pollRightStickIntervalMS + "]";
	}


}
