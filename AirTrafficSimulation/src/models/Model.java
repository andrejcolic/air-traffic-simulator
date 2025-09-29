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
	
	public abstract void paint(Graphics g, int screenWidth);
	
}
	
