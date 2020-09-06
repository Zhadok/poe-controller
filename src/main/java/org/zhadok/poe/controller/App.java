package org.zhadok.poe.controller;
import java.awt.AWTException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.zhadok.poe.controller.action.macro.Macro;
import org.zhadok.poe.controller.config.ui.ConfigMappingUI;
import org.zhadok.poe.controller.lib.JInputLib;
import org.zhadok.poe.controller.util.Loggable;
import org.zhadok.poe.controller.util.Util;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Event;
import net.java.games.input.EventQueue;

/**
 * This class shows how to use the event queue system in JInput. It will show
 * how to get the controllers, how to get the event queue for a controller, and
 * how to read and process events from the queue.
 * 
 * @author Endolf
 */
public class App implements Loggable {

	public static int verbosity = Constants.DEFAULT_VERBOSITY; 
	public int getVerbosity() {
		return verbosity; 
	}
	
	private boolean running = false;
	
	/**
	 * Poll interval (in milliseconds) for controller changes
	 */
	public final int POLL_CONTROLLER_INTERVAL_MS = 20; 
	
	private List<ControllerEventListener> controllerEventListeners = new ArrayList<>(); 
	private ControllerEventListener nextEventListener = null; 	
	private int nEventsToBeSkipped = 0; 
	private int nEventsSingleListener = 0; 
	
	public App() {}
	
	public void registerEventListener(ControllerEventListener listener) {
		this.controllerEventListeners.add(listener);
	}
	
	/**
	 * Unregisters all listeners of instance ControllerMapping
	 */
	public void resetControllerMappingListener() {
		this.controllerEventListeners.removeIf((listener) -> listener instanceof ControllerMapping); 
		Macro.resetMacros();
		this.registerEventListener(new ControllerMapping());
	}
	
	/**
	 * For the next n events, only this listener will be called
	 * @param listener
	 */
	public void registerForNextEvents(int nEvents, ControllerEventListener listener) {
		this.nEventsSingleListener = nEvents; 
		this.nextEventListener = listener; 
	}	
	
	public void setEventsToBeSkipped(int eventsToBeSkipped) {
		this.nEventsToBeSkipped = eventsToBeSkipped; 
	}
	
	public void notifyControllerEventListeners(Event event) {
		controllerEventListeners.forEach((listener) -> listener.handleEvent(event));
	}
	
	
	public void startPolling() {
		if (this.running == true) {
			return; 
		}
		
		this.running = true;
		log(1, "Started polling for controller changes"); 
		while (running == true) {
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
		if (this.running == true) {
			log(1, "Stopped polling for controller changes"); 
			this.running = false;
		}
	}
	
	private long lastEventTimestamp = -1; 
	private void handleEvent(Event event) {
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
				if (value == 1.0f) {
					buffer.append("On (value=" + value + ")");
				} else {
					buffer.append("Off (value=" + value + ")");
				}
			}
			long timestamp = event.getNanos(); 
			
			long diffMS = (timestamp - lastEventTimestamp) / 1000000; 
			buffer.append(". ").append(diffMS).append("ms since last event"); 
			this.lastEventTimestamp = timestamp; 
			
			System.out.println(buffer.toString());
		}
		
		if (nEventsToBeSkipped > 0) {
			nEventsToBeSkipped--; 
			return; 
		}
		
		if (this.nextEventListener != null && nEventsSingleListener > 0) {
			// If a single event listener is registered for next event (e.g. UI)
			// Only notify that listener
			this.nextEventListener.handleEvent(event);
			this.nEventsSingleListener--; 
			
			if (nEventsSingleListener == 0) {
				this.nextEventListener = null; 
			}
		} else {
			this.notifyControllerEventListeners(event); 
		}
	}
	
	private ConfigMappingUI startConfigMappingUI() {
		ConfigMappingUI window = new ConfigMappingUI(this);
//		java.awt.EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					//window.initialize(); 
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
		return window; 
	}
	
	
	/**
	 * @param args
	 * @throws AWTException
	 * @throws IOException
	 */
	public static void main(String[] args) throws AWTException, IOException {
		// Setting java.library.path programmatically can't be used with jar files
		// This must be passed with -Djava.library.path=...
		// https://stackoverflow.com/questions/5419039/is-djava-library-path-equivalent-to-system-setpropertyjava-library-path/24988095#24988095
		// We will set it here so that the development process within IDEs works		
		System.setProperty("java.library.path", Constants.DIR_LIB.toString());
		Util.ensureProjectDirExists(); 
		Loggable.writeLogsToFile(Constants.FILE_LOG); 
		
		JInputLib lib = new JInputLib();
		lib.prepare(); 
		
		App app = new App();
		if (System.getProperty("verbosity") != null) {
			App.verbosity = Integer.valueOf(System.getProperty("verbosity")); 
			app.log(0, "Setting verbosity to " + verbosity);
		}
		app.resetControllerMappingListener();
		ConfigMappingUI window = app.startConfigMappingUI(); 
		app.registerEventListener(window);
		app.startPolling(); 
	}
	
	
	
	
	
}
