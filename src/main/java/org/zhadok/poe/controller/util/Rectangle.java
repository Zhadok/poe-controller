package org.zhadok.poe.controller.util;

public class Rectangle {

	public final int x1;
	public final int y1;
	public final int x2;
	public final int y2; 
	
	public Rectangle(int x1, int y1, int x2, int y2) {
//		if (x1 < x2 || y1 < y2) {
//			throw new RuntimeException("Error, attempt to create negative rectangle!"); 
//		}
		
		this.x1 = x1; 
		this.y1 = y1; 
		this.x2 = x2; 
		this.y2 = y2;
	}
	
	/**
	 * Attributes will be DIVIDED by scale
	 * @param scale
	 * @return
	 */
	public Rectangle applyScaling(double scale) {
		return new Rectangle(
			(int) (x1/scale),
			(int) (y1/scale),
			(int) (x2/scale),
			(int) (y2/scale)
		);
	}
	
	public Rectangle translateX(int dx) {
		return new Rectangle(x1 + dx, y1, x2 + dx, y2); 
	}
	public Rectangle translateY(int dy) {
		return new Rectangle(x1, y1 + dy, x2, y2 + dy); 
	}
	public Rectangle width(int width) {
		return new Rectangle(x1, y1, x1+width, y2); 
	}
	public Rectangle height(int height) {
		return new Rectangle(x1, y1, x2, y1+height); 
	}
	
	public int getWidth() {
		return x2 - x1; 
	}
	public int getHeight() {
		return y2 - y1; 
	}
	
	@Override
	public String toString() {
		return "Rectangle (" + x1 + "/" + y1 + "," + x2 + "/" + y2 + ")"; 
	}
	
}
