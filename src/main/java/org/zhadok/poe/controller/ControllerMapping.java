package org.zhadok.poe.controller;

import java.awt.AWTException;
import java.awt.Robot;

import org.zhadok.poe.controller.action.ActionHandlerKey;
import org.zhadok.poe.controller.action.ActionHandlerMacro;
import org.zhadok.poe.controller.action.ActionHandlerMouse;
import org.zhadok.poe.controller.config.pojo.Mapping;
import org.zhadok.poe.controller.config.pojo.MappingKey;
import org.zhadok.poe.controller.util.Loggable;

import net.java.games.input.Event;


public class ControllerMapping implements Loggable, ControllerEventListener {
	
	public int getVerbosity() {
		return App.verbosity;
	}
	
	public final Robot robot; 

	public final ControllerSettings settings; 
	
	public final ActionHandlerKey actionHandlerKey; 
	public final ActionHandlerMouse actionHandlerMouse; 
	public final ActionHandlerMacro actionHandlerMacro; 
	
	public ControllerMapping() {
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

	public void handleEvent(Event event) {
		
		// Hat switch = left group of 4 buttons, all share name. Only value is different (0.25, 0.5, 0.75, 1.0)
		MappingKey mappingKey = new MappingKey(event); 
		log(3, "Handling event with " + mappingKey.toString());
		
		Mapping mapping = settings.getMapping(mappingKey, event); 
		if (mapping != null) {
			if (mapping.getAction() == null) {
				log(1, "No config action assigned to mapping: " + mapping.toString()); 
				return;
			}
			this.handleEvent(event, mapping);
		}
		else {
			log(3, "No mapping found for " + mappingKey.toString()); 
		}
	}

	private void handleEvent(Event event, Mapping mapping) {
		if (mapping.getAction().hasKey()) {
			actionHandlerKey.handleAction(event, mapping);
		}
		else if (mapping.getAction().hasMouseAction()) {
			actionHandlerMouse.handleAction(event, mapping);
		}
		else if (mapping.getAction().hasMacro()) {
			actionHandlerMacro.handleAction(event, mapping);
		}
		else {
			throw new RuntimeException("Action has no assigned key, mouse or macro: " + mapping.getAction().toString()); 
		}
	}
	
	
}
