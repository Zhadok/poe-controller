package org.zhadok.poe.controller;

import java.util.ArrayList;
import java.util.List;

import org.zhadok.poe.controller.action.ControllerOutputMapper;
import org.zhadok.poe.controller.action.macro.Macro;
import org.zhadok.poe.controller.util.Loggable;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Event;
import net.java.games.input.EventQueue;

public class ControllerEventHandler implements Loggable {

	@Override
	public int getVerbosity() {
		return App.verbosity;
	}

	/**
	 * Poll interval (in milliseconds) for controller changes
	 */
	public final int POLL_CONTROLLER_INTERVAL_MS = 20; 

	private boolean isPolling = false;
	private List<ControllerEventListener> controllerEventListeners = new ArrayList<>(); 
	private TemporaryControllerEventListener temporaryEventListener = null; 	
	private long lastEventTimestamp = -1; 
	
	public void registerEventListener(ControllerEventListener listener) {
		this.controllerEventListeners.add(listener);
	}
	
	public void notifyControllerEventListeners(Event event) {
		controllerEventListeners.forEach((listener) -> listener.handleEvent(event));
	}
	
	/**
	 * Unregisters all listeners of instance ControllerOutputMapper and resets all macros
	 */
	public void resetControllerMappingListener() {
		this.controllerEventListeners.removeIf((listener) -> listener instanceof ControllerOutputMapper); 
		Macro.resetMacros();
		this.registerEventListener(new ControllerOutputMapper());
	}
	
	/**
	 * For the next n events, only this listener will be called
	 */
	public void registerTemporaryListener(TemporaryControllerEventListener listener) {
		this.temporaryEventListener = listener; 
	}	
	
	public void unregisterTemporaryListener() {
		this.temporaryEventListener = null;
	}
	
	public void startPolling() {
		if (this.isPolling == true) {
			return; 
		}
		
		this.isPolling = true;
		log(1, "Started polling for controller changes"); 
		while (isPolling == true) {
			/* Get the available controllers */
			Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
			
			if (controllers.length == 0) {
				System.out.println("Found no controllers.");
				System.exit(0);
			}

			for (int i = 0; i < controllers.length; i++) {
				/* Remember to poll each one */
				
				if (controllers[i].poll()) {
					/* Get the controllers event queue */
					EventQueue queue = controllers[i].getEventQueue();

					/* Create an event object for the underlying plugin to populate */
					Event event = new Event();

					/* For each object in the queue */
					while (queue.getNextEvent(event)) {
						this.handleEvent(event);
					}
				}
			}

			/*
			 * Sleep for 20 milliseconds, in here only so the example doesn't
			 * thrash the system.
			 */
			try {
				Thread.sleep(POLL_CONTROLLER_INTERVAL_MS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void stopPolling() {
		if (this.isPolling == true) {
			log(1, "Stopped polling for controller changes"); 
			this.isPolling = false;
		}
	}
	
	public boolean isEventMouseMovement(Event event) {
		return "net.java.games.input.RawMouse$Axis".equals(event.getComponent().getClass().getName()) || 
			   "net.java.games.input.RawMouse$Button".equals(event.getComponent().getClass().getName()); 
	}
	
	private void handleEvent(Event event) {
		if (isEventMouseMovement(event)) {
			return; 
		}
		
		if (getVerbosity() >= 3) {
			StringBuffer buffer = new StringBuffer();
			//buffer.append(event.getNanos()).append(", ");
			Component comp = event.getComponent();
			buffer.append(comp.getName());
			buffer.append(" (id=" + comp.getIdentifier() + ")");
			buffer.append(": "); 
			float value = event.getValue();
			
			/*
			 * Check the type of the component and display an
			 * appropriate value
			 */
			if (comp.isAnalog()) {
				buffer.append(value);
			} else {
				if (value > 0) {
					buffer.append("On (value=" + value + ")");
				} else {
					buffer.append("Off (value=" + value + ")");
				}
			}
			long timestamp = event.getNanos(); 
			
			long diffMS = (timestamp - lastEventTimestamp) / 1000000; 
			buffer.append(". ").append(diffMS).append("ms since last event"); 
			this.lastEventTimestamp = timestamp; 
			
			log(3, buffer.toString());
		}
		
		
		if (this.temporaryEventListener != null) {
			// If a single event listener is registered for next event (e.g. UI)
			// Only notify that listener
			this.temporaryEventListener.handleEvent(event);
			
			if (this.temporaryEventListener != null && this.temporaryEventListener.hasReceivedAllEvents()) {
				this.temporaryEventListener = null;
			}
		} else {
			this.notifyControllerEventListeners(event); 
		}
	}

	
	
	
}
