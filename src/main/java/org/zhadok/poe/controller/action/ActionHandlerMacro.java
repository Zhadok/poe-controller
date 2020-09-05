package org.zhadok.poe.controller.action;

import java.awt.Robot;

import org.zhadok.poe.controller.action.macro.Macro;
import org.zhadok.poe.controller.config.pojo.ConfigAction;

import net.java.games.input.Event;

public class ActionHandlerMacro extends ActionHandler {

	public final ActionHandlerKey actionHandlerKey;
	public final ActionHandlerMouse actionHandlerMouse; 
	
	public ActionHandlerMacro(Robot robot, ActionHandlerKey actionHandlerKey, ActionHandlerMouse actionHandlerMouse) {
		super(robot);
		this.actionHandlerKey = actionHandlerKey; 
		this.actionHandlerMouse = actionHandlerMouse; 
	}

	@Override
	public void handleAction(Event event, ConfigAction action) {
		
		if (event.getValue() != 0) {
			try {
				Macro macro = Macro.buildMacro(action.getMacro(), robot);
				macro.startMacro(event, action.getMacro(), this);
			}
			catch(Exception e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
	}

}
