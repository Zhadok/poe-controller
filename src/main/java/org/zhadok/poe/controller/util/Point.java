package org.zhadok.poe.controller.util;

public class Point {
	
	public final double x; 
	public final double y; 
	
	public Point(double x, double y) {
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

	/**
	 * Assuming the result is a point on the unit circle
	 * https://www.xarg.org/2017/07/how-to-map-a-square-to-a-circle/
	 * @return
	 */
	public Point mapFromCircleToSquare() {
		double xOnCircle = x * Math.sqrt(1 - y * y / 2); 
		double yOnCircle = y * Math.sqrt(1 - x * x / 2);
		return new Point(xOnCircle, yOnCircle);
	}
	
}
