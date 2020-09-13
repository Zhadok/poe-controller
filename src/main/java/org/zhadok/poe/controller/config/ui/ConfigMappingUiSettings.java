package org.zhadok.poe.controller.config.ui;

import java.util.prefs.Preferences;

import javax.swing.JFrame;

import org.zhadok.poe.controller.App;
import org.zhadok.poe.controller.util.Loggable;
import org.zhadok.poe.controller.util.Util;

public class ConfigMappingUiSettings implements Loggable {
	
	@Override
	public int getVerbosity() {
		return App.verbosity;
	}
	
	private final Preferences preferences;
	
	private final static String KEY_UI_WIDTH = "uiWidth";
	private final static String KEY_UI_HEIGHT = "uiHeight"; 
	private final static String KEY_UI_X = "uiX"; 
	private final static String KEY_UI_Y = "uiY"; 
	private final static String KEY_UI_EXTENDED_STATE = "uiExtendedState"; 
	
	
	public ConfigMappingUiSettings() {
		this.preferences = Preferences.userRoot().node(this.getClass().getName());
		log(1, "Loaded from preferences: width=" + getUiWidth() + ", height=" + 
				getUiHeight() + ", x=" + getUiHeight() + ", y=" + getUiY() + ", extended state=" + 
				getUiExtendedState()); 
		
	}
	
	public void setUiWidth(int uiWidth) {
		this.preferences.putInt(KEY_UI_WIDTH, uiWidth);
	}
	public int getUiWidth() {
		return preferences.getInt(KEY_UI_WIDTH, getDefaultUiWidth()); 
	}
	public void setUiHeight(int uiHeight) {
		this.preferences.putInt(KEY_UI_HEIGHT, uiHeight);
	}
	public int getUiHeight() {
		return preferences.getInt(KEY_UI_HEIGHT, getDefaultUiHeight()); 
	}
	public void setUiX(int uiX) {
		this.preferences.putInt(KEY_UI_X, uiX);
	}
	public int getUiX() {
		return preferences.getInt(KEY_UI_X, getDefaultUiX()); 
	}
	public void setUiY(int uiY) {
		this.preferences.putInt(KEY_UI_Y, uiY);
	}
	public int getUiY() {
		return preferences.getInt(KEY_UI_Y, getDefaultUiY()); 
	}
	public void setUiExtendedState(int value) {
		this.preferences.putInt(KEY_UI_EXTENDED_STATE, value);
	}
	public int getUiExtendedState() {
		return preferences.getInt(KEY_UI_EXTENDED_STATE, JFrame.MAXIMIZED_BOTH); 
	}
	
	
	private int getDefaultUiWidth() {
		return (int) (0.90 * Util.getScreenSize().width); 
	}
	private int getDefaultUiHeight() {
		return (int) (0.90 * Util.getScreenSize().height);
	}
	private int getDefaultUiX() {
		int paddingX = (Util.getScreenSize().width - getDefaultUiWidth()) / 2; 
		return paddingX; 
	}
	private int getDefaultUiY() {
		int paddingY = (Util.getScreenSize().height - getDefaultUiHeight()) / 2; 
		return paddingY; 
	}

}
