package org.zhadok.poe.controller.config.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class ConfigMappingUiTop extends JPanel {
	
	private static final long serialVersionUID = -6604990179117588277L;
	public static final String TEXT_CANCEL = "Cancel"; 
	
	private final JLabel labelListeningStatus;
	private final JButtonWithIcon buttonCancel; 
	
	public ConfigMappingUiTop() {
		super(); 
		this.setBackground(ConfigMappingUi.COLOR_PANEL_BACKGROUND);
		this.setLayout(new BorderLayout());
		int textPadding = ConfigMappingUi.TEXT_PADDING; 
		
		labelListeningStatus = new JLabel("Listening status");
		labelListeningStatus.setHorizontalAlignment(SwingConstants.CENTER);
		labelListeningStatus.setFont(ConfigMappingUi.FONT_BIG_BUTTONS);
		labelListeningStatus.setBorder(new EmptyBorder(textPadding, textPadding, textPadding, textPadding));
		this.add(labelListeningStatus, BorderLayout.CENTER);
		
		buttonCancel = new JButtonWithIcon("/img/icon-cancel.png", TEXT_CANCEL, ConfigMappingUi.FONT_BIG_BUTTONS, 
				textPadding, 1); 
		this.hideCancelButton();
		this.add(buttonCancel, BorderLayout.EAST); 
	}

	public void setStatusText(String text) {
		labelListeningStatus.setText(text);
	}
	
	public void hideCancelButton() {
		buttonCancel.removeAllActionListeners();
		buttonCancel.setVisible(false);
	}
	
	public void showCancelButton(ActionListener listener) {
		buttonCancel.addActionListener(listener);
		buttonCancel.setVisible(true);
	}
	
	

}
