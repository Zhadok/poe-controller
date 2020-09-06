package org.zhadok.poe.controller.mouse;

import java.awt.Robot;

import org.zhadok.poe.controller.App;
import org.zhadok.poe.controller.action.macro.MacroMouseMovement;
import org.zhadok.poe.controller.config.ConfigManager;
import org.zhadok.poe.controller.config.pojo.ConfigMouseMovement;
import org.zhadok.poe.controller.util.Loggable;
import org.zhadok.poe.controller.util.Point;
import org.zhadok.poe.controller.util.Util;

/**
 * Problem: Using right stick to move mouse
 * Mouse only moves when new event comes in 
 * ==> Need another thread to check if right stick is pressed and if yes move the mouse
 */
public class MouseMoveThread implements Loggable, Runnable {
	
	public int getVerbosity() {
		return App.verbosity; 
	}
	
	private final ConfigMouseMovement settings;
	
	private final MacroMouseMovement macroMouseMovement;
	private final Robot robot;

	private boolean isRunning; 
	private boolean isStickReleased = true; 
	
	public MouseMoveThread(MacroMouseMovement macroMouseMovement) {
		this.macroMouseMovement = macroMouseMovement;
		this.robot = macroMouseMovement.getRobot(); 
		
		this.settings = ConfigManager.getInstance().getLoadedConfig().getMouseMovement();
	}

	@Override
	public void run() {
		log(1, "Started running MouseMoveThread with robot=" + robot.hashCode()); 
		this.isRunning = true; 
		while (this.isRunning) {
			
			if (this.isStickAboveThreshold()) {
				if (this.isStickReleased == true) {
					this.isStickReleased = false; 
					log(1, "Stick for mouse moving in use."); 
				}
				this.moveMouse();
			}
			else {
				if (this.isStickReleased == false) {
					log(1, "Stick for mouse moving released."); 
					this.isStickReleased = true;
				}
			}
			
			try {
				Thread.sleep(settings.getPollRightStickIntervalMS());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	

	private boolean isStickAboveThreshold() {
		return Math.abs(macroMouseMovement.stickXAxis) > macroMouseMovement.stickThreshold &&
				Math.abs(macroMouseMovement.stickYAxis) > macroMouseMovement.stickThreshold; 
	}
	
	private double transformStickAxis(double stickAxis) {
		//int sign = (stickAxis >= 0) ? +1 : -1;
		//return sign * stickAxis*stickAxis;
		return stickAxis; 
	}
	
	private double getStickXAxis() {
		return transformStickAxis(macroMouseMovement.stickXAxis);
	}
	private double getStickYAxis() {
		return transformStickAxis(macroMouseMovement.stickYAxis);
	}
	
	private void moveMouse() {
		Point currentLocation = Util.getMouseLocation();
		int dx = (int) (getStickXAxis() * settings.getMouseMoveSensitivity()); 
		int dy = (int) (getStickYAxis() * settings.getMouseMoveSensitivity()); 
		robot.mouseMove(currentLocation.x + dx, currentLocation.y + dy);
	}
	
	
}
