package org.zhadok.poe.controller.action.macro;

import java.awt.Robot;

import org.zhadok.poe.controller.action.ActionHandlerMacro;
import org.zhadok.poe.controller.config.pojo.ConfigMacro;

import net.java.games.input.Event;

public class MacroExitProgram extends Macro {
	
	public MacroExitProgram(ConfigMacro macro, Robot robot) {
		super(macro, robot);
	}

	public void performMacro(Event event, ConfigMacro macro, ActionHandlerMacro actionHandlerMacro) {
		System.exit(0);
	}
	
}
