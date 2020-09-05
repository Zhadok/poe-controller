package org.zhadok.poe.controller.config.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import org.zhadok.poe.controller.App;
import org.zhadok.poe.controller.ControllerEventListener;
import org.zhadok.poe.controller.util.Loggable;

import net.java.games.input.Event;

public class ConfigMappingUI implements Loggable, ControllerEventListener {

	@Override
	public int getVerbosity() {
		return App.verbosity;
	}
	
	private JFrame frame;
	private final App app; 

	/**
	 * Create the application.
	 */
	public ConfigMappingUI(App app) {
		this.app = app; 
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize() {
		ConfigMappingUI that = this; 
		
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JButton buttonStartListening = new JButton("Start listening");
		buttonStartListening.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				log(1, "Registering for next event..."); 
				app.registerForNextEvent((event) -> {
					log(1, "Received next event: " + event);
				});
				
			}
		});
		frame.getContentPane().add(buttonStartListening, BorderLayout.SOUTH);
		frame.setVisible(true);
	}

	@Override
	public void handleEvent(Event event) {
		//log(1, "Received event: " + event.toString()); 
	}

}
