package org.zhadok.poe.controller.config.pojo;

public class ConfigCharacterMovement {
	
	/**
	 * How many pixels (x axis) should the mouse be offset for character movement? 
	 * This should be the distance from the middle of the screen to the middle of the character
	 */
	private int mouseOffsetCharacterToScreenCenterX;
	/**
	 * How many pixels (y axis) should the mouse be offset for character movement? 
	 * This should be the distance from the middle of the screen to the middle of the character
	 */
	private int mouseOffsetCharacterToScreenCenterY;
	
	private double mouseDistance_ScreenSizeMultiplier;
	
	private double stickThreshold;
	
	private ConfigAction[] actionsOnStickRelease; 
	
	public int getMouseOffsetCharacterToScreenCenterX() {
		return mouseOffsetCharacterToScreenCenterX;
	}

	public int getMouseOffsetCharacterToScreenCenterY() {
		return mouseOffsetCharacterToScreenCenterY;
	}

	public double getMouseDistance_ScreenSizeMultiplier() {
		return mouseDistance_ScreenSizeMultiplier;
	}

	public double getStickThreshold() {
		return stickThreshold;
	}

	public ConfigAction[] getActionsOnStickRelease() {
		return actionsOnStickRelease; 
	}
	
	@Override
	public String toString() {
		return "ConfigCharacterMovement [mouseOffsetCharacterToScreenCenterX=" + mouseOffsetCharacterToScreenCenterX
				+ ", mouseOffsetCharacterToScreenCenterY=" + mouseOffsetCharacterToScreenCenterY
				+ ", mouseDistance_ScreenSizeMultiplier=" + mouseDistance_ScreenSizeMultiplier + ", stickThreshold="
				+ stickThreshold + "]";
	}

	
	
}
