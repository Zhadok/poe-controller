package org.zhadok.poe.controller.config.ui;

import java.awt.AWTEvent;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.apache.commons.lang3.NotImplementedException;
import org.zhadok.poe.controller.App;
import org.zhadok.poe.controller.config.pojo.ConfigAction;
import org.zhadok.poe.controller.mouse.MouseAction;
import org.zhadok.poe.controller.util.Loggable;

public class NextOutputMappingHandler implements KeyListener, Loggable, AWTEventListener {

	@Override
	public int getVerbosity() {
		return App.verbosity;
	}

	/**
	 * 
	 * @param parentFrame The parent to listen to for mouse events
	 */
	public NextOutputMappingHandler(JFrame parentFrame) {
		this.parentFrame = parentFrame;
	}

	private Consumer<ConfigAction> finishHandler;
	private JFrame parentFrame;

	public void setFinishHandler(Consumer<ConfigAction> finishHandler) {
		this.finishHandler = finishHandler;
	}

	@Override
	public void eventDispatched(AWTEvent event) {
		// Event and component that received that event
		MouseEvent mouseEvent = (MouseEvent) event;
		java.awt.Component c = mouseEvent.getComponent();
		
		// Ignoring mouse events from any other frame
		if (SwingUtilities.getWindowAncestor(c) == parentFrame) {
			if (event.getID() == MouseEvent.MOUSE_RELEASED) {
				// If button is cancel button don't fire event
				if (mouseEvent.getSource() instanceof JButton) {
					if (ConfigMappingUiTop.TEXT_CANCEL.equals(((JButton) mouseEvent.getSource()).getText())) {
						this.onFinish(null);
						return; 
					}
				}
				this.onMouseClicked(mouseEvent);
			}
		}
	}

	public void onMouseClicked(MouseEvent e) {
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

	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		
	}

	public void keyReleased(KeyEvent e) {
		String keyName = ConfigAction.mapKeyEventCodeToString(e.getKeyCode());
		log(1, "Detected key event: " + e.getKeyChar() + ", key code=" + e.getKeyCode() + ", found keyName=" + keyName);
		ConfigAction action = new ConfigAction(keyName, null, null, null);
		onFinish(action);
	}

	private void onFinish(ConfigAction action) {
		finishHandler.accept(action);
	}

}
