package org.zhadok.poe.controller;
import java.awt.AWTException;
import java.io.IOException;

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

	public static int verbosity = 1; 
	public int getVerbosity() {
		return verbosity; 
	}
	
	private ControllerSettings settings; 
	private boolean running = true;
	
	private ControllerMapping controllerMapping; 
	
	public App() throws AWTException {
		this.controllerMapping = ControllerMapping.getInstance(); 
		this.settings = controllerMapping.settings; 
	}
	
	private void startPolling() {
		log(1, "Started polling for controller changes"); 
		while (running == true) {
			/* Get the available controllers */
			Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
			
			if (controllers.length == 0) {
				System.out.println("Found no controllers.");
				System.exit(0);
			}
			if (controllers.length > 1) {
				//System.out.println("Found " + controllers.length + " controllers");
			
				/*for (Controller controller : controllers) {
					System.out.println(controller.getName());
				}*/
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
				Thread.sleep(settings.POLL_CONTROLLER_INTERVAL_MS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
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
		
		controllerMapping.handleEvent(event);
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
		
		JInputLib lib = new JInputLib();
		lib.prepare(); 
		
		String version = Runtime.class.getPackage().getImplementationVersion();
		System.out.println("Running with Java version=" + version);
		App app = new App();
		app.startPolling(); 
	}
	
	
	
}
