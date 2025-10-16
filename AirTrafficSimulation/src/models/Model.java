package models;

import java.awt.Graphics;

public abstract class Model {
	
	//Models have a 2D position and width. They can be drawn.
	
	protected double x;
	protected double y;
	protected int width;
	
	public Model(double x, double y, int width){
		this.x = x;
		this.y = y;
		this.width = width;
	}

	public abstract void paint(Graphics g, int screenWidth);
	
	static public boolean collide(Model m1, Model m2, int screenWidth) {
        double kDegPerPx = 180.0 / screenWidth;
        double rDeg = (m1.getWidth()/2.0 + m2.getWidth()/2.0) * kDegPerPx;

        double dx = (double) m1.getX() - m2.getX();
        double dy = (double) m1.getY() - m2.getY();
        return dx*dx + dy*dy <= rDeg*rDeg;
	}
	
	public static double distPointToSegment(
	        double px, double py, double x1, double y1, double x2, double y2) {

	    double dx = x2 - x1, dy = y2 - y1;
	    double len2 = dx*dx + dy*dy;
	    if (len2 == 0) return Math.hypot(px - x1, py - y1);

	    double t = ((px - x1)*dx + (py - y1)*dy) / len2;
	    if (t <= 0) return Math.hypot(px - x1, py - y1);
	    if (t >= 1) return Math.hypot(px - x2, py - y2);

	    double cross = (px - x1)*dy - (py - y1)*dx;
	    return Math.abs(cross) / Math.sqrt(len2);
	}
	
	public static boolean segmentsHit(
	        double x1,double y1,double x2,double y2,
	        double x3,double y3,double x4,double y4,
	        double eps) {
		
	    if (distPointToSegment(x1,y1,x3,y3,x4,y4) <= eps) return true;
	    if (distPointToSegment(x2,y2,x3,y3,x4,y4) <= eps) return true;
	    if (distPointToSegment(x3,y3,x1,y1,x2,y2) <= eps) return true;
	    if (distPointToSegment(x4,y4,x1,y1,x2,y2) <= eps) return true;
	    
	    double d1x = x2 - x1, d1y = y2 - y1, d2x = x4 - x3, d2y = y4 - y3;
	    double c1 = (x3 - x1)*d1y - (y3 - y1)*d1x;
	    double c2 = (x4 - x1)*d1y - (y4 - y1)*d1x;
	    double c3 = (x1 - x3)*d2y - (y1 - y3)*d2x;
	    double c4 = (x2 - x3)*d2y - (y2 - y3)*d2x;
	    return (c1 * c2 < 0) && (c3 * c4 < 0);
	}
	
	
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
}
	
