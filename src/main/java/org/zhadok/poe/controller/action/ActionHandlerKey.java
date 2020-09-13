package org.zhadok.poe.controller.action;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.zhadok.poe.controller.config.pojo.ConfigAction;
import org.zhadok.poe.controller.config.pojo.Mapping;

import net.java.games.input.Event;

public class ActionHandlerKey extends ActionHandler {
	
	private Random random; 
	private Map<Integer, Integer> keysPressed; 
	
	public ActionHandlerKey(Robot robot) {
		super(robot);
		log(1, "Initializing ActionHandlerKey..."); 
		this.random = new Random(); 
		this.keysPressed = new HashMap<>(); 
	}


	@Override
	public void handleAction(Event event, Mapping mapping) {
		int keycode = mapping.getAction().getKeyEventCode(); 
		boolean isDigitalButton = mapping.getMappingKey().isAnalog() == false; 
		boolean isPressed = event.getValue() > 0f; 
		
		log(3, "Handling event with mapping=" + mapping.toString()); 
		
		if (isDigitalButton && isPressed) {
			log(2, "Pressing action=" + mapping.getAction().toString()); 
			keyPress(keycode);
		} else if (isDigitalButton && !isPressed) {
			log(2, "Releasing keycode=" + keycode); 
			keyRelease(keycode);
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
		keyPress(keyEventCode);
		robot.delay(getDelayAfterKeyPress());
		keyRelease(keyEventCode);
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
	        keyPress(keyCode);
	        robot.delay(getDelayAfterKeyPress());
	        keyRelease(keyCode);
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
		keyPress(KeyEvent.VK_CONTROL);
		keyPress(KeyEvent.VK_ALT);
		keyPress(KeyEvent.VK_TAB);
		keyRelease(KeyEvent.VK_TAB);
		keyRelease(KeyEvent.VK_ALT);
		keyRelease(KeyEvent.VK_CONTROL);
		
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
		keyPress(KeyEvent.VK_CONTROL);
		robot.delay(getDelayAfterKeyPress());
	}
	public void releaseControl() {
		log(2, "Releasing control..."); 
		keyRelease(KeyEvent.VK_CONTROL);
		robot.delay(getDelayAfterKeyPress());
	}
	
	public void pressAlt() {
		keyPress(KeyEvent.VK_ALT);
		robot.delay(getDelayAfterKeyPress());
	}
	public void releaseAlt() {
		keyRelease(KeyEvent.VK_ALT);
		robot.delay(getDelayAfterKeyPress());
	}

	public void pressShift() {
		keyPress(KeyEvent.VK_SHIFT); 
		robot.delay(getDelayAfterKeyPress());
	}
	public void releaseShift() {
		keyRelease(KeyEvent.VK_SHIFT); 
		robot.delay(getDelayAfterKeyPress());
	}

	private void keyPress(int keycode) {
		robot.keyPress(keycode);
		keysPressed.put(keycode, 0); 
	}
	private void keyRelease(int keycode) {
		robot.keyRelease(keycode);
		keysPressed.remove(keycode);
	}

	public void releaseAllKeys() {
		this.keysPressed.keySet().forEach(keycode -> {
			log(1, "Releasing key: " + keycode); 
			robot.keyRelease(keycode); 
		});
	}
	
}
