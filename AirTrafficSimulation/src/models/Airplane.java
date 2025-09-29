package models;

import java.awt.Color;
import java.awt.Graphics;

public class Airplane extends Model {
	
	//Airplane is a model that appears on the map. 
	//It has a 2D location and is drawn as a blue circle.
	
	private double startX;
	private double startY;
	private double endX;
	private double endY;
	private int duration;
	private int elapsed;
	
	public Airplane(Flight f) {
		super(f.getDepartureAirport().getX(), f.getDepartureAirport().getY(), 10);
		
		startX = x;
		startY = y;
		endX = f.getDestinationAirport().getX();
		endY = f.getDestinationAirport().getY();
		duration = f.getDuration() * 60000;
		elapsed = 0;
	}

	public void moveAirplane(int timeStep) {
		elapsed += timeStep;
		if (elapsed > duration) elapsed = duration;
		
		double factor = (double) elapsed / (double) duration;
		
		x = startX + (endX - startX) * factor;
		y = startY + (endY - startY) * factor;
	}
	
	public boolean arrived() {
		return elapsed >= duration;
	}

	@Override
	public void paint(Graphics g, int screenWidth) {
		Color prevColor = g.getColor();
		g.setColor(Color.BLUE);
		
		int drawX = (int) Math.round((x + 90) * screenWidth / 180);
		int drawY = (int) Math.round((90 - y) * screenWidth / 180);
		g.fillOval(drawX - width/2, drawY - width/2, width, width);
		g.setColor(prevColor);
	}
}
