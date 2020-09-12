package org.zhadok.poe.controller.config.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeListener;

import org.zhadok.poe.controller.config.pojo.Mapping;

public class MappingRow extends JPanel {

	private static final long serialVersionUID = -4115167338628202045L;
	
	private Mapping mapping; 
	
	private JButton buttonStartMappingInput;
	private JButton buttonStartMappingOutput; 
	private JButton buttonDeleteMapping;
	
	private JLabel textInput; 
	private JLabel textOutput; 
	
	public MappingRow(Mapping mapping, 
			ChangeListener startMappingInputListener,
			ActionListener startMappingOutputListener,
			ActionListener deleteListener) {
		super(new FlowLayout(FlowLayout.LEFT));
		this.setBackground(ConfigMappingUI.COLOR_PANEL_BACKGROUND);
		this.mapping = mapping;
		
		int textInputWidth = 150; 
		int textOutputWidth = 250; 
		int textPadding = ConfigMappingUI.TEXT_PADDING; 
		
		buttonStartMappingInput = new JButtonWithIcon("/img/icon-controller.png", "Map input", textPadding);
		buttonStartMappingInput.addChangeListener(startMappingInputListener);
		
		buttonStartMappingOutput = new JButtonWithIcon("/img/icon-mouse-keyboard.png", "Map output", textPadding, 2);
		buttonStartMappingOutput.addActionListener(startMappingOutputListener);
		
		Border border = new LineBorder(Color.BLACK, 1, true); 
		Border borderTextPadding = new EmptyBorder(textPadding, textPadding, textPadding, textPadding);
		
		textInput = new JLabel(" "); 
		textInput.setBorder(new CompoundBorder(border, borderTextPadding));
		textInput.setBackground(Color.WHITE);
		textInput.setOpaque(true);
		textInput.setPreferredSize(new Dimension(textInputWidth, textInput.getFont().getSize() + 2*textPadding));
		
		textOutput = new JLabel(" "); 
		textOutput.setBorder(new CompoundBorder(border, borderTextPadding));
		textOutput.setBackground(Color.WHITE);
		textOutput.setOpaque(true);
		textOutput.setPreferredSize(new Dimension(textOutputWidth, textOutput.getFont().getSize() + 2*textPadding));
		
		buttonDeleteMapping = new JButtonWithIcon("/img/icon-delete.png", "", textPadding);
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
		String mappingKeyString = mapping.getMappingKey() != null ? mapping.getMappingKey().toStringUI() : " "; 
		String actionString = mapping.getAction() != null ? mapping.getAction().toStringUI() : " ";
		
		SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
		    	textInput.setText(mappingKeyString);
		    	textInput.revalidate();
		    	textInput.repaint();
		    	textOutput.setText(actionString);
		    	textOutput.revalidate();
		    	textOutput.repaint();
		    	revalidate();
		    	repaint(); 
		    }
		});
		
	}
	
	public void setButtonsEnabled(boolean value) {
		buttonStartMappingInput.setEnabled(value);
		buttonStartMappingOutput.setEnabled(value);
		buttonDeleteMapping.setEnabled(value);
	}
	
	
}
