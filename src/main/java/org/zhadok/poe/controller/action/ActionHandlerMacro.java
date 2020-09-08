package org.zhadok.poe.controller.action;

import java.awt.Robot;

import org.zhadok.poe.controller.action.macro.Macro;
import org.zhadok.poe.controller.config.pojo.Mapping;

import net.java.games.input.Event;

public class ActionHandlerMacro extends ActionHandler {

	public final ActionHandlerKey actionHandlerKey;
	public final ActionHandlerMouse actionHandlerMouse; 
	
	public ActionHandlerMacro(Robot robot, ActionHandlerKey actionHandlerKey, ActionHandlerMouse actionHandlerMouse) {
		super(robot);
		log(1, "Initializing ActionHandlerMacro..."); 
		this.actionHandlerKey = actionHandlerKey; 
		this.actionHandlerMouse = actionHandlerMouse; 
	}

	@Override
	public void handleAction(Event event, Mapping mapping) {
		log(3, "Handling event with mapping=" + mapping.toString()); 
		if (event.getValue() != 0) {
			try {
				Macro macro = Macro.buildMacro(mapping.getAction().getMacro(), robot);
				macro.startMacro(event, mapping.getAction().getMacro(), this);
			}
			catch(Exception e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
	}

}
