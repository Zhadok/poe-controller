package org.zhadok.poe.controller;

import org.zhadok.poe.controller.util.TriConsumer;

import net.java.games.input.Event;

/**
 * A temporary controller event listener for a predetermined number of events.
 */
public class TemporaryControllerEventListener {

	private final int numberEventsToReceive; 
	
	private final boolean filterAnalogEvents; 
	
	private int numberEventsToBeSkipped; 
	private int numberEventsReceived = 0; 
	
	private final TriConsumer<Event, Integer, Boolean> eventConsumer;
	
	public TemporaryControllerEventListener(int numberEventsToBeSkipped, int numberEventsToReceive, boolean filterEventsAnalog, 
			TriConsumer<Event, Integer, Boolean> eventConsumer) {
		this.numberEventsToBeSkipped = numberEventsToBeSkipped; 
		this.numberEventsToReceive = numberEventsToReceive; 
		this.filterAnalogEvents = filterEventsAnalog; 
		this.eventConsumer = eventConsumer;
	}
	
	private void decrementNumberEventsToBeSkipped() {
		this.numberEventsToBeSkipped--; 
	}
	
	private boolean shouldEventBeSkipped() {
		return this.numberEventsToBeSkipped > 0; 
	}

	public int getNumberEventsToReceive() {
		return numberEventsToReceive;
	}

	public void incrementEventsReceived() {
		this.numberEventsReceived++; 
	}

	public boolean isFilterAnalogEvents() {
		return filterAnalogEvents;
	}
	

	/**
	 * 
	 * @param event
	 * @return Did the temporary event listener consume the event?
	 */
	public void handleEvent(Event event) {
		if (shouldEventBeSkipped()) {
			this.decrementNumberEventsToBeSkipped(); 
			return; 
		}
		if (isFilterAnalogEvents() == true && event.getComponent().isAnalog() == true) {
			return; 
		}
		this.incrementEventsReceived();
		eventConsumer.handle(event, numberEventsReceived, hasReceivedAllEvents());
	}
	
	public boolean hasReceivedAllEvents() {
		return this.numberEventsReceived >= this.numberEventsToReceive; 
	}
	
}
