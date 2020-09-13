package org.zhadok.poe.controller.action;

import java.awt.AWTException;
import java.awt.Robot;
import java.util.List;

import org.zhadok.poe.controller.App;
import org.zhadok.poe.controller.ControllerEventListener;
import org.zhadok.poe.controller.config.pojo.Mapping;
import org.zhadok.poe.controller.config.pojo.MappingKey;
import org.zhadok.poe.controller.util.Loggable;

import net.java.games.input.Event;


public class ControllerOutputMapper implements Loggable, ControllerEventListener {
	
	public int getVerbosity() {
		return App.verbosity;
	}
	
	public final Robot robot; 

	public final ControllerOutputSettings settings; 
	
	public final ActionHandlerKey actionHandlerKey; 
	public final ActionHandlerMouse actionHandlerMouse; 
	public final ActionHandlerMacro actionHandlerMacro; 
	
	public ControllerOutputMapper() {
		log(1, "Initializing ControllerMapping...");
		try {
			this.robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
			throw new RuntimeException("Unable to initialize java.awt.Robot");
		} 
		
		this.settings = new ControllerOutputSettings(); 
		
		this.actionHandlerKey = new ActionHandlerKey(robot); 
		this.actionHandlerMouse = new ActionHandlerMouse(robot); 
		this.actionHandlerMacro = new ActionHandlerMacro(robot, this.actionHandlerKey, this.actionHandlerMouse); 
		
		//LogWatcher.getInstance().watchNonBlocking(LogWatcher.pathToPoELogDir);
		//LogWatcher.getInstance().addTestLines(); 
		
		//TradeUI.launchInNewThread();
		//TradeUI.launchAsNewJavaFX();
	}

	public void handleEvent(Event event) {
		MappingKey mappingKey = new MappingKey(event); 
		log(3, "Handling event with " + mappingKey.toString());
		
		List<Mapping> relevantMappings = settings.getRelevantMappings(mappingKey, event); 
		relevantMappings.forEach(mapping -> {
			if (mapping.hasAction() == false) {
				log(1, "No config action assigned to mapping: " + mapping.toString()); 
				return;
			}
			this.handleEvent(event, mapping);
		});
		if (relevantMappings.isEmpty()) {
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
