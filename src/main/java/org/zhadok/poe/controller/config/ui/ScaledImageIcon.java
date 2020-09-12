package org.zhadok.poe.controller.config.ui;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.ImageIcon;

public class ScaledImageIcon extends ImageIcon {

	private static final long serialVersionUID = -3445045960167904749L;
	private final int width;
	private final int height;

	/**
	 * 
	 * @param url
	 * @param size Width and height
	 */
	public ScaledImageIcon(String resourceUrl, int size) {
		this(resourceUrl, size, size);
	}

	public ScaledImageIcon(String resourceUrl, int width, int height) {
		super(ScaledImageIcon.class.getResource(resourceUrl));
		this.width = width;
		this.height = height;
	}

	public int getIconWidth() {
		return width;
	}

	public int getIconHeight() {
		return height;
	}

	public void paintIcon(Component c, Graphics g, int x, int y) {
		g.drawImage(getImage(), x, y, getIconWidth(), getIconHeight(), c);
	}
}
