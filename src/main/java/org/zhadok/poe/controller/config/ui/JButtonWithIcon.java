package org.zhadok.poe.controller.config.ui;

import java.awt.Font;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import org.zhadok.poe.controller.App;
import org.zhadok.poe.controller.util.Loggable;

public class JButtonWithIcon extends JButton implements Loggable {

	private static final long serialVersionUID = 847547223141384303L;

	
	
	public JButtonWithIcon(String iconResourceUrl, String text, int textPadding) {
		this(iconResourceUrl, text, textPadding, 1); 
	}

	/**
	 * 
	 * @param iconResourceUrl
	 * @param text
	 * @param textPadding
	 * @param widthMultiplier
	 */
	public JButtonWithIcon(String iconResourceUrl, String text, int textPadding, double widthMultiplier) {
		this (iconResourceUrl, text, null, textPadding, widthMultiplier); 
	}
	
	/**
	 * 
	 * @param iconResourceUrl
	 * @param text
	 * @param textPadding
	 * @param widthMultiplier
	 */
	public JButtonWithIcon(String iconResourceUrl, String text, Font font,  int textPadding, double widthMultiplier) {
		super(text); 
		
		if (font != null) {
			this.setFont(font);
		}
		
		try {
			int size = getFont().getSize() + 1*textPadding; 
			ImageIcon imageIcon = new ScaledImageIcon(iconResourceUrl, (int) widthMultiplier*size, size); 
			this.setIcon(imageIcon);
			this.setDisabledIcon(imageIcon);
//			this.setHorizontalAlignment(SwingConstants.LEFT); 
			setIconTextGap(10);
		}
		catch (Exception e) {
			log(1, "Unable to set icon for button: " + e.getMessage()) ;
		}
	}

	@Override
	public int getVerbosity() {
		return App.verbosity;
	}
	
	public void removeAllActionListeners() {
		Arrays.stream(this.getActionListeners()).forEach(listener -> {
			this.removeActionListener(listener); 
		});
	}
	
}
