package org.zhadok.poe.controller.action.macro;

import java.awt.Robot;

import org.zhadok.poe.controller.action.ActionHandlerMacro;
import org.zhadok.poe.controller.config.pojo.ConfigMacro;

import net.java.games.input.Event;

public class MacroCancelMacro extends Macro {

	public MacroCancelMacro(ConfigMacro macro, Robot robot) {
		super(macro, robot);
	}

	@Override
	public void performMacro(Event event, ConfigMacro macro, ActionHandlerMacro actionHandlerMacro) {
		Macro.cancelCurrentMacro(); 	

		actionHandlerMacro.actionHandlerKey.releaseControl();
		actionHandlerMacro.actionHandlerKey.releaseAlt();
	}

}
