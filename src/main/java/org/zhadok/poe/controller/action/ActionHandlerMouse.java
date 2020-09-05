package org.zhadok.poe.controller.action;

import java.awt.Robot;
import java.awt.event.InputEvent;
import java.util.Random;

import org.zhadok.poe.controller.config.pojo.ConfigAction;

import net.java.games.input.Event;

public class ActionHandlerMouse extends ActionHandler {

	private boolean isMouseLeftClickPressed = false;
	private boolean isMouseRightClickPressed = false;
	
	private Random random; 
	boolean haveNextNextGaussian = false;
	double nextNextGaussian;  
	
	public ActionHandlerMouse(Robot robot) {
		super(robot);
		this.random = new Random(); 
	}

	
	@Override
	public void handleAction(Event event, ConfigAction action) {
		
		switch (action.getMouseAction()) {
		case LEFT_CLICK: 
			this.mouseLeftClick(); 
			break; 
		case LEFT_PRESS: 
			this.mouseLeftPress(); 
			break; 
		case LEFT_RELEASE:
			this.mouseLeftRelease();
			break; 
		case RIGHT_CLICK: 
			this.mouseRightClick(event); 
			break; 
		case RIGHT_PRESS:
			this.mouseRightPress(); 
			break; 
		case RIGHT_RELEASE:
			this.mouseRightRelease();
			break; 
		}
		
	}
	
	public int getRandom(double min, double max) {
		return (int) (random.nextInt((int) (max - min + 1)) + min);
	}
	
	/**
	 * Delay between mouse down and mouse release
	 * 
	 * http://instantclick.io/click-test says between 70ms and 90s
	 * @return
	 */
	public int getDelayMSMousePressAndRelease() {
		return getRandom(70, 90); 
	}
	
	
	@SuppressWarnings("deprecation")
	public void mouseLeftPress() {
		if (this.isMouseLeftClickPressed == false && !this.isInterrupted()) {
			this.isMouseLeftClickPressed = true;
			robot.mousePress(InputEvent.BUTTON1_MASK);
			log(2, "Mouse left press"); 
		}
	}
	@SuppressWarnings("deprecation")
	public void mouseLeftRelease() {
		if (this.isMouseLeftClickPressed == true && !this.isInterrupted()) {
			this.robot.mouseRelease(InputEvent.BUTTON1_MASK);
			this.isMouseLeftClickPressed = false; 
			log(2, "Mouse left release"); 
		}
	}
	
	public void mouseLeftClick() {
		// Release first
		//this.mouseLeftRelease(); 
		
		this.mouseLeftPress();		
		this.sleep(getDelayMSMousePressAndRelease());
		this.mouseLeftRelease(); 
	}
	
	
	@SuppressWarnings("deprecation")
	public void mouseRightClick() {
		if (this.isInterrupted())
			return; 
		
		
		robot.mousePress(InputEvent.BUTTON3_MASK);
		this.sleep(getDelayMSMousePressAndRelease());
		robot.mouseRelease(InputEvent.BUTTON3_MASK);
	}
	
	@SuppressWarnings("deprecation")
	public void mouseRightClick(Event event) {
		if (this.isInterrupted())
			return; 
		
		if (event.getValue() > 0f) {
			robot.mousePress(InputEvent.BUTTON3_MASK);
			this.sleep(getDelayMSMousePressAndRelease());
			robot.mouseRelease(InputEvent.BUTTON3_MASK);
		}
		else {
			
		}
	}
	
	@SuppressWarnings("deprecation")
	public void mouseRightPress() {
		if (this.isMouseRightClickPressed == false && !this.isInterrupted()) {
			this.isMouseRightClickPressed = true;
			robot.mousePress(InputEvent.BUTTON3_MASK);
			log(2, "Mouse right press"); 
		}
	}
	
	@SuppressWarnings("deprecation")
	public void mouseRightRelease() {
		if (this.isMouseRightClickPressed == true && !this.isInterrupted()) {
			this.isMouseRightClickPressed = false; 
			this.robot.mouseRelease(InputEvent.BUTTON3_MASK);
			log(2, "Mouse right release"); 
		}
	}

}
