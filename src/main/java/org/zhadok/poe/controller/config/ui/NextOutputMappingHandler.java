package org.zhadok.poe.controller.config.ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.function.Consumer;

import org.apache.commons.lang3.NotImplementedException;
import org.zhadok.poe.controller.App;
import org.zhadok.poe.controller.config.pojo.ConfigAction;
import org.zhadok.poe.controller.mouse.MouseAction;
import org.zhadok.poe.controller.util.Loggable;

public class NextOutputMappingHandler implements KeyListener, MouseListener, Loggable {
	
	@Override
	public int getVerbosity() {
		return App.verbosity;
	}
	
	private Consumer<ConfigAction> finishHandler; 
	
	public void setFinishHandler(Consumer<ConfigAction> finishHandler) {
		this.finishHandler = finishHandler; 
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		log(1, "Detected mouse click: " + e.getButton());
		MouseAction mouseAction; 
		switch (e.getButton()) {
		case MouseEvent.BUTTON1: 
			mouseAction = MouseAction.LEFT_CLICK; 
			break; 
		case MouseEvent.BUTTON2: 
			mouseAction = MouseAction.MIDDLE_CLICK; 
			break; 
		case MouseEvent.BUTTON3: 
			mouseAction = MouseAction.RIGHT_CLICK;
			break; 
		default: 
			throw new NotImplementedException("Mouse with button " + e.getButton() + " not implemented."); 
		}
		ConfigAction action = new ConfigAction(null, mouseAction, null, null); 
		onFinish(action); 
	}

	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void keyTyped(KeyEvent e) {}

	public void keyPressed(KeyEvent e) {
		String keyName = ConfigAction.mapKeyEventCodeToString(e.getKeyCode()); 
		log(1, "Detected key event: " + e.getKeyChar() + ", key code=" + e.getKeyCode() + ", found keyName=" + keyName); 
		ConfigAction action = new ConfigAction(keyName, null, null, null); 
		onFinish(action); 
	}
	public void keyReleased(KeyEvent e) {}
	
	private void onFinish(ConfigAction action) {
		finishHandler.accept(action);
	}

	

	
}
