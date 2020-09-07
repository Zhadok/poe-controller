package org.zhadok.poe.controller.action.macro;

import java.awt.Dimension;
import java.awt.Robot;

import org.zhadok.poe.controller.action.ActionHandlerMacro;
import org.zhadok.poe.controller.action.ActionHandlerMouse;
import org.zhadok.poe.controller.config.ConfigManager;
import org.zhadok.poe.controller.config.pojo.ConfigAction;
import org.zhadok.poe.controller.config.pojo.ConfigCharacterMovement;
import org.zhadok.poe.controller.config.pojo.ConfigMacro;
import org.zhadok.poe.controller.util.Point;
import org.zhadok.poe.controller.util.Util;

import net.java.games.input.Event;

public class MacroCharacterMovement extends Macro {
	

	private final ConfigCharacterMovement settings;
	
	public final Point pointCharacterCenter;  // screen center - a bit of y offset
	
	/***
	 * How far should the mouse be from the center of the screen when using left stick to move?
	 * Calculate: X% of screen size
	 * 
	 * Must be a single variable so we get a circle, not an oval (since screen width and height are different)
	 * ==> This variable gives the radius of the circle
	 */
	public final int maxMouseDistanceToCenter_CharacterMovement; 

	public float stickXAxis = 0; 
	public float stickYAxis = 0; 
	
	public final ConfigAction[] actionsOnStickRelease;
	
	public MacroCharacterMovement(ConfigMacro macro, Robot robot) {
		super(macro, robot);
		
		this.settings = ConfigManager.getInstance().getLoadedConfig().getCharacterMovement();
		
		this.pointCharacterCenter = getPointCharacterCenter(settings); 
		this.maxMouseDistanceToCenter_CharacterMovement = getMaxMouseDistanceToCenter(settings);
		
		this.actionsOnStickRelease = this.settings.getActionsOnStickRelease(); 
		
		log(1, "Initialized with mouseDistanceToScreenCenter=" + this.maxMouseDistanceToCenter_CharacterMovement + ", robot=" + robot.hashCode()); 
		
	}
	
	public static Point getPointCharacterCenter(ConfigCharacterMovement settings) {
		Point pointScreenCenter = Util.getScreenCenter();
		return new Point(pointScreenCenter.x + settings.getMouseOffsetCharacterToScreenCenterX(), 
				pointScreenCenter.y + settings.getMouseOffsetCharacterToScreenCenterY()); 
	}
	
	public static int getMaxMouseDistanceToCenter(ConfigCharacterMovement settings) { 
		Dimension screenSize = Util.getScreenSize(); 
		return (int) (Math.min(screenSize.getWidth(), screenSize.getHeight()) * 
				settings.getMouseDistance_ScreenSizeMultiplier()); 
	}
	
	public void performMacro(Event event, ConfigMacro macro, ActionHandlerMacro actionHandlerMacro) {
		
		ActionHandlerMouse mouse = actionHandlerMacro.actionHandlerMouse;

		// Event consists only of EITHER x axis OR y axis value, so need to save both values
		if (macro.getParameter("axis").equals("x")) {
			this.stickXAxis = event.getValue();
		}
		else {
			this.stickYAxis = event.getValue();
		}
		
		mouse.mouseLeftPress();

		// X axis value between -1 and 1
		// Y axis value between -1 and 1
		// https://www.xarg.org/2017/07/how-to-map-a-square-to-a-circle/
		Point pointOnSquare = new Point(stickXAxis, stickYAxis); 
		Point pointOnCircle = pointOnSquare.mapFromCircleToSquare(); 
		int dx = (int) (pointOnCircle.x * this.maxMouseDistanceToCenter_CharacterMovement); 
		int dy = (int) (pointOnCircle.y * this.maxMouseDistanceToCenter_CharacterMovement); 
			
		int x = (int) this.pointCharacterCenter.x + dx;
		int y = (int) this.pointCharacterCenter.y + dy; 
		robot.mouseMove(x, y);
		log(2, "Change (" + dx + "/" + dy + ") from center (" + pointCharacterCenter.x + "/" + pointCharacterCenter.y + 
				") ==> to new point (" + x + "/" + y + "), changed axis=" + macro.getParameter("axis"));
		
		// If X axis 0 and Y axis 0 release mouse
		if (Math.abs(this.stickXAxis) < settings.getStickThreshold() &&
			Math.abs(this.stickYAxis) < settings.getStickThreshold()) {
				log(1, "Left stick released"); 
				mouse.mouseLeftRelease();
			}
	}
	
	@Override
	public String toString() {
		return super.toString(); 
	}
	
}
