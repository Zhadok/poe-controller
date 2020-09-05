package org.zhadok.poe.controller.util;

public class Point {
	
	public final int x; 
	public final int y; 
	
	public Point(int x, int y) {
		this.x = x; 
		this.y = y; 
	}
	
	@Override
	public String toString() {
		return "(" + x + "/" + y + ")";
	}

	public double distanceTo(Point target) {
		return Math.sqrt(
				Math.pow(target.x-this.x, 2) + 
				Math.pow(target.y-this.y, 2)
				);
	}
	
}
