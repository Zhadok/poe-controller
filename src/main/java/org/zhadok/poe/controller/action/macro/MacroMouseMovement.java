package org.zhadok.poe.controller.action.macro;

import java.awt.Robot;

import org.zhadok.poe.controller.action.ActionHandlerMacro;
import org.zhadok.poe.controller.config.ConfigManager;
import org.zhadok.poe.controller.config.pojo.ConfigMacro;
import org.zhadok.poe.controller.config.pojo.ConfigMouseMovement;
import org.zhadok.poe.controller.mouse.MouseMoveThread;

import net.java.games.input.Event;

public class MacroMouseMovement extends Macro {

	public final MouseMoveThread mouseMoveThread;
	
	public float stickXAxis = 0; 
	public float stickYAxis = 0; 
	
	public MacroMouseMovement(ConfigMacro macro, Robot robot) {
		super(macro, robot);
		
		this.mouseMoveThread = new MouseMoveThread(this);
		this.startMouseMoveThread(); 
	}


	/**
	 * Starts the thread for moving the mouse:
	 * Needed for stick which moves the mouse.
	 * 
	 * Since we don't keep getting events (since the stick doesn't change), move the mouse a little more every 
	 * SETTINGS.POLL_RIGHT_STICK_INTERVAL_MS 
	 */
	public void startMouseMoveThread() {
		Thread t = new Thread(this.mouseMoveThread);
		t.start(); 
	}
	
	
	@Override
	public void performMacro(Event event, ConfigMacro macro, ActionHandlerMacro actionHandlerMacro) {

		// The MouseMoveThread continuously looks at these values and moves the mouse if above threshold
		if (macro.getParameter("axis").equals("x")) {
			this.stickXAxis = event.getValue();
		}
		else {
			this.stickYAxis = event.getValue();
		}
		
	}

}
