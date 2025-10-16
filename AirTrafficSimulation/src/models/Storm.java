package models;

import java.awt.Color;
import java.awt.Graphics;

public class Storm extends Model {
	
//	Storm is a model that appears on the map. 
//	When an airplane hits a storm it slows down for the rest of the flight.
//	It has a line shape and is summoned by pressing the right click button at
//  its start point and releasing it at its end point.
//	Two storms can't overlap.
//  A storm can be removed by trying to draw another storm over it.
//	It has a 2D location and is drawn as a purple line.
	
	private double endX;
	private double endY;

	public Storm(double startX, double startY, double endX, double endY) {
		super(startX, startY, 0);
		this.endX = endX;
		this.endY = endY;
	}
		
	@Override
	public void paint(Graphics g, int screenWidth) {
		Color prevColor = g.getColor();
		g.setColor(Color.MAGENTA);
		
		int drawX1 = (int) Math.round((x + 90) * screenWidth / 180);
		int drawY1 = (int) Math.round((90 - y) * screenWidth / 180);
		int drawX2 = (int) Math.round((endX + 90) * screenWidth / 180);
		int drawY2 = (int) Math.round((90 - endY) * screenWidth / 180);
		g.drawLine(drawX1, drawY1, drawX2, drawY2);
		g.setColor(prevColor);
	}
	
	public double getEndX() {
		return endX;
	}

	public void setEndX(double endX) {
		this.endX = endX;
	}

	public double getEndY() {
		return endY;
	}
	
	public void setEndY(double endY) {
		this.endY = endY;
	}
}
