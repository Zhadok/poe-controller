package org.zhadok.poe.controller.action;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.Random;

import org.zhadok.poe.controller.config.pojo.ConfigAction;

import net.java.games.input.Event;

public class ActionHandlerKey extends ActionHandler {
	
	private Random random; 
	
	public ActionHandlerKey(Robot robot) {
		super(robot);
		this.random = new Random(); 
	}


	@Override
	public void handleAction(Event event, ConfigAction action) {
		
		int keycode = action.getKeyEventCode(); 
		boolean isDigitalButton = event.getComponent().isAnalog() == false;
		boolean isPressed = event.getValue() > 0f; 
		
		if (isDigitalButton && isPressed) {
			log(2, "Press keycode=" + keycode); 
			robot.keyPress(keycode);
		}
		if (isDigitalButton && !isPressed) {
			log(2, "Release keycode=" + keycode); 
			robot.keyRelease(keycode);
		}	
	}
	
	public int getRandom(double min, double max) {
		return (int) (random.nextInt((int) (max - min + 1)) + min);
	}
	

	public void pressAndReleaseKey(ConfigAction action) {
		this.pressAndReleaseKey(action.getKeyEventCode());
	}
	
	public void pressAndReleaseKey(int keyEventCode) {
		if (this.isInterrupted())
			return; 
		
		log(2, "Pressing and releasing keyEventCode: " + keyEventCode); 
		robot.keyPress(keyEventCode);
		robot.delay(getDelayAfterKeyPress());
		robot.keyRelease(keyEventCode);
		robot.delay(getDelayAfterKeyPress());
	}
	
	public void writeText(String text) {
		if (this.isInterrupted())
			return; 
		
		for (char c : text.toCharArray()) {
			if (c == '~') {
				writeTilde();
				continue; 
			}
			if (c == '/') {
				writeForwardSlash(); 
				continue; 
			}
			
	        int keyCode = KeyEvent.getExtendedKeyCodeForChar(c);
	        if (KeyEvent.CHAR_UNDEFINED == keyCode) {
	            throw new RuntimeException(
	                "Key code not found for character '" + c + "'");
	        }
	        robot.keyPress(keyCode);
	        robot.delay(getDelayAfterKeyPress());
	        robot.keyRelease(keyCode);
	        robot.delay(getDelayAfterKeyPress());
	    }
	}
	


	public void writeForwardSlash() {
		this.pressShift();
		this.pressAndReleaseKey(KeyEvent.VK_7);
		this.releaseShift();
	}


	public void writeTilde() {
		this.pressControl();
		this.pressAlt();
		this.pressAndReleaseKey(KeyEvent.VK_PLUS);
		this.releaseAlt();
		this.releaseControl();
	}
	
	public void altTab() {
		/*this.pressAlt();
		this.pressAndReleaseKey(KeyEvent.VK_TAB);
		this.releaseAlt();
		*/
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_ALT);
		robot.keyPress(KeyEvent.VK_TAB);
		robot.keyRelease(KeyEvent.VK_TAB);
		robot.keyRelease(KeyEvent.VK_ALT);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		
		this.pressAndReleaseKey(KeyEvent.VK_ENTER);
		
		System.out.println("Waiting...");
		robot.delay(2000);
		System.out.println("Done waiting."); 
	}
	
	public int getDelayAfterKeyPress() {
		return getRandom(25, 50); 
	}
	
	public void pressControl() {
		log(2, "Pressing control..."); 
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.delay(getDelayAfterKeyPress());
	}
	public void releaseControl() {
		log(2, "Releasing control..."); 
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.delay(getDelayAfterKeyPress());
	}
	
	public void pressAlt() {
		robot.keyPress(KeyEvent.VK_ALT);
		robot.delay(getDelayAfterKeyPress());
	}
	public void releaseAlt() {
		robot.keyRelease(KeyEvent.VK_ALT);
		robot.delay(getDelayAfterKeyPress());
	}

	public void pressShift() {
		robot.keyPress(KeyEvent.VK_SHIFT);
		robot.delay(getDelayAfterKeyPress());
	}
	public void releaseShift() {
		robot.keyRelease(KeyEvent.VK_SHIFT);
		robot.delay(getDelayAfterKeyPress());
	}
	
	
	
}
