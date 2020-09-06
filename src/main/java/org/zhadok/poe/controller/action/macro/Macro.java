package org.zhadok.poe.controller.action.macro;

import java.awt.Robot;

import org.zhadok.poe.controller.App;
import org.zhadok.poe.controller.action.ActionHandlerMacro;
import org.zhadok.poe.controller.config.ConfigManager;
import org.zhadok.poe.controller.config.pojo.Config;
import org.zhadok.poe.controller.config.pojo.ConfigMacro;
import org.zhadok.poe.controller.util.Loggable;

import net.java.games.input.Event;

public abstract class Macro implements Loggable {
	
	public int getVerbosity() {
		return App.verbosity;
	}
	
	public static final double gameScaling = 2; 
	protected ConfigMacro macro;
	protected Robot robot;

	protected Config config; 
	
	// persistent macros
	private static MacroCharacterMovement macroCharacterMovement;
	private static MacroMouseMovement macroMouseMovement;	
	
	// Needed so we can keep track and cancel it
	private static Thread currentMacroThread; 
	
	public Macro(ConfigMacro macro, Robot robot) {
		this.config = ConfigManager.getInstance().getLoadedConfig(); 
		this.macro = macro;
		this.robot = robot;
	}
	
	public Robot getRobot() {
		return this.robot; 
	}
	
	/**
	 * Starts a new thread with the macro
	 * 
	 * Macro should run in a new thread so that it can be interrupted
	 * @param event
	 * @param macro
	 * @param actionHandlerMacro
	 */
	public void startMacro(Event event, ConfigMacro macro, ActionHandlerMacro actionHandlerMacro) {
		
		Macro m = this; 
		if (this.shouldRunInSeparateThread() ) {
			Macro.currentMacroThread = new Thread(new Runnable() {
				@Override
				public void run() {
					m.performMacro(event, macro, actionHandlerMacro); 
				}
			}); 
			Macro.currentMacroThread.start(); 	
		}
		else {
			// Start macro in main thread
			m.performMacro(event, macro, actionHandlerMacro);
		}
	}
	
	public static void cancelCurrentMacro() {
		if (currentMacroThread != null && currentMacroThread.isInterrupted() == false) {
			currentMacroThread.interrupt(); 
		}
	}
	
	/**
	 * By default, run macros in main thread
	 * @return
	 */
	protected boolean shouldRunInSeparateThread() {
		return false; 
	}
	
	protected abstract void performMacro(Event event, ConfigMacro macro, ActionHandlerMacro actionHandlerMacro);
	
	public static void resetMacros() {
		macroCharacterMovement = null; 
		macroMouseMovement = null; 
	}
	
	public static Macro buildMacro(ConfigMacro macro, Robot robot) {
		if (App.verbosity >= 1) {
			//System.out.println("Executing: " + macro.toString());
		}
		MacroName macroName = macro.getName();
		switch(macroName) {
		case MacroExitProgram:
			return new MacroExitProgram(macro, robot);
		case MacroCharacterMovement:
			if(macroCharacterMovement == null) {
				macroCharacterMovement = new MacroCharacterMovement(macro, robot);
			}
			return macroCharacterMovement;
		case MacroMouseMovement:
			if (macroMouseMovement == null) {
				macroMouseMovement = new MacroMouseMovement(macro, robot);
			}
			return macroMouseMovement;
		default: 
			throw new RuntimeException("Unknown macro='" + macroName + "'");
		}
	}
	
	
	
	
}
