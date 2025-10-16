package models;

import java.awt.Color;
import java.awt.Graphics;

public class Airplane extends Model {
	
	//Airplane is a model that appears and moves on the map during simulations. 
	//It has a 2D location and is drawn as a blue circle.
	
	private double startX;
	private double startY;
	private double endX;
	private double endY;
	private int duration; //ms
	private int elapsed; //ms
	private Flight flight;
	private Airport departureAirport;
	private Airport destinationAirport;
	private boolean inTyphoon = false;
	private boolean inStorm = false;
	
	public Airplane(Flight flight) {
		super(flight.getDepartureAirport().getX(), flight.getDepartureAirport().getY(), 10);
		
		this.flight = flight;
		this.departureAirport = flight.getDepartureAirport();
		this.destinationAirport = flight.getDestinationAirport();
		this.startX = x;
		this.startY = y;
		this.endX = flight.getDestinationAirport().getX();
		this.endY = flight.getDestinationAirport().getY();
		this.duration = flight.getDuration() * 60000;
		this.elapsed = 0;
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
	
	public void hitTyphoon(Typhoon t, int screenWidth) {
		
		boolean hit = collide(this, t, screenWidth);

        if (hit && !inTyphoon) {
        	redirect(startX, startY);
        	
            duration = Math.max(1, elapsed);
            elapsed  = 0;

            inTyphoon = true;
        } 
        else if (!hit && inTyphoon)
            inTyphoon = false;
    }
	
	public void hitStorm(Storm s, int screenWidth) {
	    boolean hit = hitsStorm(this, s, screenWidth);

	    if (hit && !inStorm) {
	    	redirect(endX, endY);
	    	
	        int remaining = duration - elapsed;
	        int slowed    = (int) Math.round(remaining * 1.5);
	        elapsed = 0;
	        duration = Math.max(1, slowed);

	        inStorm = true;
	    } 
	    else if (!hit && inStorm) {
	        inStorm = false;
	    }
	}
	
	private static boolean hitsStorm(Airplane a, Storm s, int screenWidth) {
	    double tolDeg = (a.width / 2.0) * 180.0 / screenWidth;

	    double d = distPointToSegment(
	            a.x, a.y, s.getX(), s.getY(), s.getEndX(), s.getEndY());

	    return d <= tolDeg;
	}
	
	private void redirect(double newEndX, double newEndY) {
        startX = this.x;
        startY = this.y;
        endX   = newEndX;
        endY   = newEndY;
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
	
	@Override 
	public String toString() { 
		return flight.toString() + " Delay: " + flight.getDelay(); 
	} 
}


