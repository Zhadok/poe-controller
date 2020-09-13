package org.zhadok.poe.controller.action;

import java.awt.Robot;
import java.awt.event.InputEvent;
import java.util.Random;

import org.apache.commons.lang3.NotImplementedException;
import org.zhadok.poe.controller.config.pojo.Mapping;

import net.java.games.input.Event;

public class ActionHandlerMouse extends ActionHandler {

	private boolean isMouseRightPressed = false;
	private boolean isMouseMiddlePressed = false; 
	
	private Random random; 
	boolean haveNextNextGaussian = false;
	double nextNextGaussian;  
	
	public ActionHandlerMouse(Robot robot) {
		super(robot);
		log(1, "Initializing ActionHandlerMouse..."); 
		this.random = new Random(); 
	}

	
	@Override
	public void handleAction(Event event, Mapping mapping) {
		log(3, "Handling event with mapping=" + mapping.toString()); 
		
		switch (mapping.getAction().getMouseAction()) {
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
		case MIDDLE_CLICK: 
			this.mouseMiddleClick(); 
		default: 
			throw new NotImplementedException("Mouse action '" + mapping.getAction().getMouseAction() + "' not implemented!"); 
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
	
	public void mouseLeftPress() {
		if (!this.isInterrupted()) {
			robot.mousePress(InputEvent.BUTTON1_MASK);
			log(2, "Mouse left press"); 
		}
	}
	public void mouseLeftRelease() {
		if (!this.isInterrupted()) {
			this.robot.mouseRelease(InputEvent.BUTTON1_MASK);
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
	
	
	public void mouseRightClick() {
		if (this.isInterrupted())
			return; 
		
		robot.mousePress(InputEvent.BUTTON3_MASK);
		this.sleep(getDelayMSMousePressAndRelease());
		robot.mouseRelease(InputEvent.BUTTON3_MASK);
	}
	
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
	
	public void mouseRightPress() {
		if (this.isMouseRightPressed == false && !this.isInterrupted()) {
			this.isMouseRightPressed = true;
			robot.mousePress(InputEvent.BUTTON3_MASK);
			log(2, "Mouse right press"); 
		}
	}
	
	public void mouseRightRelease() {
		if (this.isMouseRightPressed == true && !this.isInterrupted()) {
			this.isMouseRightPressed = false; 
			this.robot.mouseRelease(InputEvent.BUTTON3_MASK);
			log(2, "Mouse right release"); 
		}
	}

	
	private void mouseMiddleClick() {
		this.mouseMiddlePress();		
		this.sleep(getDelayMSMousePressAndRelease());
		this.mouseMiddleRelease(); 
	}
	
	private void mouseMiddlePress() {
		if (this.isMouseMiddlePressed == false && !this.isInterrupted()) {
			this.isMouseMiddlePressed = true;
			robot.mousePress(InputEvent.BUTTON2_MASK);
			log(2, "Mouse middle press"); 
		}
	}
	
	private void mouseMiddleRelease() {
		if (this.isMouseMiddlePressed == true && !this.isInterrupted()) {
			this.isMouseMiddlePressed = false; 
			this.robot.mouseRelease(InputEvent.BUTTON2_MASK);
			log(2, "Mouse middle release"); 
		}
	}
	
}
