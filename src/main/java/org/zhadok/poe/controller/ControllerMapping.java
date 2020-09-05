package org.zhadok.poe.controller;

import java.awt.AWTException;
import java.awt.Robot;

import org.zhadok.poe.controller.action.ActionHandlerKey;
import org.zhadok.poe.controller.action.ActionHandlerMacro;
import org.zhadok.poe.controller.action.ActionHandlerMouse;
import org.zhadok.poe.controller.config.pojo.ConfigAction;
import org.zhadok.poe.controller.config.pojo.Mapping;
import org.zhadok.poe.controller.util.Loggable;

import net.java.games.input.Component;
import net.java.games.input.Event;


public class ControllerMapping implements Loggable {
	
	public int getVerbosity() {
		return App.verbosity;
	}
	
	public final Robot robot; 

	public final ControllerSettings settings; 
	
	public final ActionHandlerKey actionHandlerKey; 
	public final ActionHandlerMouse actionHandlerMouse; 
	public final ActionHandlerMacro actionHandlerMacro; 
	
	private static ControllerMapping instance; 
	
	private ControllerMapping() {
		log(1, "Initializing ControllerMapping...");
		try {
			this.robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
			throw new RuntimeException("Unable to initialize java.awt.Robot");
		} 
		
		this.settings = new ControllerSettings(); 
		
		this.actionHandlerKey = new ActionHandlerKey(robot); 
		this.actionHandlerMouse = new ActionHandlerMouse(robot); 
		this.actionHandlerMacro = new ActionHandlerMacro(robot, this.actionHandlerKey, this.actionHandlerMouse); 
		
		//LogWatcher.getInstance().watchNonBlocking(LogWatcher.pathToPoELogDir);
		//LogWatcher.getInstance().addTestLines(); 
		
		//TradeUI.launchInNewThread();
		//TradeUI.launchAsNewJavaFX();
	}

	public static ControllerMapping getInstance() {
		if (instance == null) {
			instance = new ControllerMapping(); 
		}
		return instance; 
	}
	
	public void handleEvent(Event event) {
		Component comp = event.getComponent(); 

		// Hat switch = left group of 4 buttons, all share name. Only value is different (0.25, 0.5, 0.75, 1.0)
		String componentName = comp.getName().equals("Hat Switch") ? 
				Mapping.getComponentName("Hat Switch", event.getValue()) :
				comp.getName(); 
		log(3, "ComponentName: " + componentName);
		
		switch (comp.getName()) {
		
		default: 
			Mapping mapping = settings.getMapping(componentName); 
			if (mapping != null) {
				this.handleEvent(event, mapping.getAction());
			}
			else {
				//log(1, "Unknown component name: " + componentName); 
			}
			break; 
		}
	}

	public void handleEvent(Event event, ConfigAction action) {
		if (action == null) {
			return;
		}
		
		if (action.hasKey()) {
			actionHandlerKey.handleAction(event, action);
		}
		else if (action.hasMouseAction()) {
			actionHandlerMouse.handleAction(event, action);
		}
		else if (action.hasMacro()) {
			actionHandlerMacro.handleAction(event, action);
		}
	}
	
	
}
