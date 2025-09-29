package centerPanel;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

import guiCore.AirTrafficSimulator;
import models.Airplane;
import models.Airport;

public class AirTrafficMap extends Canvas {
	
	private AirTrafficSimulator owner;
	private int squareWidth;
	private int rows = 18;
	
	private String selectedAirport = null;
	private boolean selected = false;
	private Timer blinkTimer;
	private boolean blink = false;
	
	
	public AirTrafficMap(AirTrafficSimulator owner) {
		
		this.owner = owner;	
		
		addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				handleClick(e.getPoint());
			}
		});
	}
	
	public int getRows() {
		return rows;
	}
	
	@Override
	public void paint(Graphics g) {
		//drawGridLines(g);
		drawAirports(g);
		drawAirplanes(g);
	}
	
	@Override 
	public void update(Graphics g) { 
		g.clearRect(0, 0, getWidth(), getHeight());
		paint(g); 
	}
	
	private int getDim() {
		int width = owner.centerPanel.getWidth();
		int height = owner.centerPanel.getHeight();
		int w = width / rows * rows;
		int h = height / rows * rows;
		return Math.max(w, h);
	}
	
	private void drawGridLines(Graphics g) {
		int dim = getDim();
		int step = dim / rows;
		for(int i = 0; i < dim; i += step) {
			Color prevColor = g.getColor();
			g.setColor(Color.LIGHT_GRAY);
			if(i == 9 * step)
				g.setColor(Color.GRAY);
			g.drawLine(0, i, dim - 1, i);
			g.drawLine(i, 0, i, dim - 1);
			g.setColor(prevColor);
		}	
	}
	
	public void drawAirports(Graphics g) {
		if(selectedAirport != null && !owner.visibleAirports.contains(selectedAirport)) {
			stopBlinking();
			selectedAirport = null;
			owner.timer.go();
			repaint();
		}
		int w = getWidth();

		for (Airport a : owner.airportBase.getAll()) {
		    if (!owner.visibleAirports.contains(a.getCode())) {
		        continue;
		    }
		    
		    if (a.getCode().equals(selectedAirport) && blink)
		    	a.paint(g, w, Color.RED);
		    	
		    else
		    	a.paint(g, w);
		}
	}
	
	public void drawAirplanes(Graphics g) {
		if (owner.sim == null)
			return;
		for(Airplane a : owner.sim.airplaneBase.getAll()) {
			a.paint(g, getWidth());
		}
	}
	
	private void handleClick(Point p) {
		String clicked = null;
		
		for (Airport a : owner.airportBase.getAll()) {
			if(!owner.visibleAirports.contains(a.getCode()))
				continue;
			
			Rectangle r = airplaneRectangle(a);
			if(r.contains(p)) {
				clicked = a.getCode();
				break;
			}
		}
		if (clicked == null)
			return;
		
		if(clicked.equals(selectedAirport)) {
			stopBlinking();
			selectedAirport = null;
			owner.timer.go();
			repaint();
		}
		else {
			selectedAirport = clicked;
			startBlinking();
			owner.timer.pause();
		}
			
	}
	
	private Rectangle airplaneRectangle(Airport a) {
		Point p = adaptCords(a);
		int width = a.getWidth();
		int x = p.x - width/2, y = p.y - width/2;
		return new Rectangle(x, y, width, width);
	}
	
	private Point adaptCords(Airport a) {
		int w = getWidth();
		int h = getHeight();
		double x = a.getX(), y = a.getY();
	    int newX = (int) Math.round((x + 90.0) / 180.0 * w);
	    int newY = (int) Math.round(h - (y + 90.0) / 180.0 * h);
		return new Point(newX, newY);
	}
	
	private void startBlinking() {
		stopBlinking();
		blinkTimer = new Timer(true);
		blinkTimer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				blink = !blink;
				repaint();
			}
		}, 0, 500);
		
	}
	
	private void stopBlinking() {
		if(blinkTimer != null) {
			blinkTimer.cancel();
			blinkTimer = null;
		}
		blink = false;
	}
}
