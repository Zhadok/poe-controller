package org.zhadok.poe.controller.action;

import java.awt.Robot;

import org.zhadok.poe.controller.App;
import org.zhadok.poe.controller.config.pojo.Mapping;
import org.zhadok.poe.controller.util.Loggable;

import net.java.games.input.Event;

public abstract class ActionHandler implements Loggable {

	protected final Robot robot; 
	private ThreadLocal<Boolean> isThreadInterrupted; 
	
	protected ActionHandler(Robot robot) {
		this.robot = robot; 
		this.isThreadInterrupted = new ThreadLocal<>(); 
		this.isThreadInterrupted.set(false);
	}

	@Override
	public int getVerbosity() {
		return App.verbosity;
	}
	
	public abstract void handleAction(Event event, Mapping mapping); 
	
	public void sleep(int ms) {
		if (this.isInterrupted())
			return; 
		
		// Either use robot.delay or Thread.sleep()
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			// Already interrupted, is ok
			log(1, "Interrupted sleep.");  
			//Thread.currentThread().interrupt(); // Can't call this, will have many exceptions from robot's mouseMove which has an autodelay in it
			this.interrupt(); 
		}
	}
	
	private void interrupt() {
		this.isThreadInterrupted = new ThreadLocal<>(); 
		this.isThreadInterrupted.set(true);
	}
	
	protected boolean isInterrupted() {
		if (this.isThreadInterrupted.get() == null) {
			return false; 
		}
		return this.isThreadInterrupted.get();
	}
	
}
