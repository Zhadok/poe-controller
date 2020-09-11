package org.zhadok.poe.controller.config.ui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeListener;

import org.zhadok.poe.controller.config.pojo.Mapping;

public class MappingRow extends JPanel {

	private static final long serialVersionUID = -4115167338628202045L;
	
	private Mapping mapping; 
	
	private JButton buttonStartMappingInput;
	private JButton buttonStartMappingOutput; 
	private JButton buttonAssignMacroOutput; 
	private JButton buttonDeleteMapping;
	
	private JTextField textInput; 
	private JTextField textOutput; 
	
	public MappingRow(Mapping mapping, 
			ChangeListener startMappingInputListener,
			ActionListener startMappingOutputListener,
			ActionListener assignMacroListener, 
			ActionListener deleteListener) {
		super(new FlowLayout(FlowLayout.LEFT));
		this.mapping = mapping;
		
		buttonStartMappingInput = new JButton("Map input");
		buttonStartMappingInput.addChangeListener(startMappingInputListener);
		
		buttonStartMappingOutput = new JButton("Map output");
		buttonStartMappingOutput.addActionListener(startMappingOutputListener);
		
		buttonAssignMacroOutput = new JButton("Assign macro"); 
		buttonAssignMacroOutput.addActionListener(assignMacroListener);
		
		textInput = new JTextField();
		textInput.setEnabled(false);
		textInput.setDisabledTextColor(Color.BLACK);
		textInput.setBorder(new EmptyBorder(4, 4, 4, 4));
		
		textOutput = new JTextField();
		textOutput.setEnabled(false);
		textOutput.setDisabledTextColor(Color.BLACK);
		textOutput.setBorder(new EmptyBorder(4, 4, 4, 4));
		
		buttonDeleteMapping = new JButton("X");
		buttonDeleteMapping.setForeground(Color.RED);
		buttonDeleteMapping.addActionListener(deleteListener); 
		
		this.updateTexts();
		
		this.add(buttonStartMappingInput);
		this.add(buttonStartMappingOutput);
		// this.add(buttonAssignMacroOutput); 
		this.add(textInput);
		this.add(textOutput);
		this.add(buttonDeleteMapping);
	}
	
	public void updateTexts() {
		String mappingKeyString = mapping.getMappingKey() != null ? mapping.getMappingKey().toStringUI() : ""; 
		String actionString = mapping.getAction() != null ? mapping.getAction().toStringUI() : "";
		
		SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
		    	textInput.setText(mappingKeyString);
		    	textOutput.setText(actionString);
		    }
		});
		
	}
	
	public void setButtonsEnabled(boolean value) {
		buttonStartMappingInput.setEnabled(value);
		buttonStartMappingOutput.setEnabled(value);
		buttonDeleteMapping.setEnabled(value);
	}
	
	
}
