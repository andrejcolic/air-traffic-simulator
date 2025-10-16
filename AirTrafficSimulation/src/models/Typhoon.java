package models;

import java.awt.Color;
import java.awt.Graphics;

public class Typhoon extends Model {
	
//	Typhoon is a model that appears on the map. 
//	When an airplane hits a typhoon it starts returning back to its departure airport.
//	It is summoned by holding the left click button on spaces not occupied by other models.
//	Its size is determined by how much the left click button was held.
//	It can be removed by clicking it again.
//	It has a 2D location and is drawn as a red circle.

	public Typhoon(double x, double y, int width) {
		super(x, y, width);
	}

	@Override
	public void paint(Graphics g, int screenWidth) {
		Color prevColor = g.getColor();
		g.setColor(Color.RED);
		
		int drawX = (int) Math.round((x + 90) * screenWidth / 180);
		int drawY = (int) Math.round((90 - y) * screenWidth / 180);
		g.fillOval(drawX - width/2, drawY - width/2, width, width);
		g.setColor(prevColor);
	}
}
