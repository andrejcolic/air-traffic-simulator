package centerPanel;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import guiCore.AirTrafficSimulator;
import guiCore.InfoDialog;
import models.Airplane;
import models.Airport;
import models.Model;
import models.Storm;
import models.Typhoon;

public class AirTrafficMap extends Canvas {
	
	private AirTrafficSimulator owner;
	private int rows = 18;
	
	private String selectedAirport = null;
	private Timer blinkTimer;
	private boolean blink = false;
	private TyphoonTimer holdTimer;
	private BufferedImage backBuffer = null;
    
    
    double startX = -1;
    double startY = -1;
	
	public AirTrafficMap(AirTrafficSimulator owner) {
		
		this.owner = owner;	
		
		addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON1)
					handleLeftClick(e.getPoint());
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				handlePress(e);
			}
			
			
			@Override
			public void mouseReleased(MouseEvent e) {
			    if (e.getButton() == MouseEvent.BUTTON1) {
			        if (holdTimer != null) holdTimer.interrupt();
			    }
			    if (e.getButton() == MouseEvent.BUTTON3) {
			        endStorm(e.getPoint());
			    }
			}
		});
	}
	
	public int getRows() {
		return rows;
	}
	
	private void ensureBackBuffer() {
	    int w = getWidth();
	    if (backBuffer == null) {
	        backBuffer = new BufferedImage(w, w, BufferedImage.TYPE_INT_ARGB);
	    }
	}
	
	@Override
	public void paint(Graphics g) {
		int w = getWidth();
	    ensureBackBuffer();
	    if (backBuffer == null) return;
	    Graphics bg = backBuffer.getGraphics();
	    bg.setColor(getBackground());
	    bg.fillRect(0, 0, w, w);

	    //drawGridLines(bg);
	    drawAirports(bg);
	    drawAirplanes(bg);
	    drawTyphoons(bg);
	    drawStorms(bg);

	    g.drawImage(backBuffer, 0, 0, this);
	    bg.dispose();
	}

	
	@Override 
	public void update(Graphics g) { 
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
	
	public void drawTyphoons(Graphics g) {
		for(Typhoon t : owner.typhoonBase.getAll()) {
			t.paint(g, getWidth());
		}
	}
	
	public void drawStorms(Graphics g) {
		for(Storm s : owner.stormBase.getAll()) {
			s.paint(g, getWidth());
		}
	}
	
	private void handleLeftClick(Point p) {
		clickAirport(p);
		clickAirplane(p);
	}
	
	void clickAirport(Point p) {
		String clickedCode = airportAt(p);
		
		if (clickedCode == null)
			return;
		
		if(clickedCode.equals(selectedAirport)) {
			stopBlinking();
			selectedAirport = null;
			owner.timer.go();
			repaint();
		}
		else {
			selectedAirport = clickedCode;
			startBlinking();
			owner.timer.pause();
		}
	}
	
	String airportAt(Point p) {
		String code = null;
		for (Airport a : owner.airportBase.getAll()) {
			if(!owner.visibleAirports.contains(a.getCode()))
				continue;
			
			Rectangle r = modelRectangle(a);
			if(r.contains(p)) {
				code = a.getCode();
				break;
			}
		}
		return code;
		
	}
	
	void clickAirplane(Point p) {
		Airplane a = airplaneAt(p);
		if(a != null) {
			InfoDialog.showInfoDialog(owner, "Flight info", a.toString());
		}
	}
	
	Airplane airplaneAt(Point p) {
		if(owner.sim == null)
			return null;
		for(Airplane a : owner.sim.airplaneBase.getAll()) {
			Rectangle r = modelRectangle(a);
			if(r.contains(p)) {
				return a;
			}
		}
		return null;
	}
	
	void clickTyphoon(Point p) {
	    Typhoon t = typhoonAt(p);
	    if (t != null) {
	        if (holdTimer != null) { holdTimer.interrupt(); holdTimer = null; }
	        owner.typhoonBase.remove(t);
	        repaint();
	    }
	}
	
	void pressTyphoon(Point p) {
		int w = getWidth();
		double x = (double) (((p.x + 0.5) / w) * 180.0 - 90.0);
		double y = (double) ((1.0 - (p.y + 0.5) / w) * 180.0 - 90.0);
		for(Typhoon t : owner.typhoonBase.getAll()) {
			Rectangle r = modelRectangle(t);
			if(r.contains(p)) {
				return;
			}
		}
		Typhoon t = new Typhoon(x, y, 5);
		owner.typhoonBase.add(t);
		holdTimer = new TyphoonTimer(this, t);
		holdTimer.start();
		holdTimer.go();
		repaint();
	}	
	
	Typhoon typhoonAt(Point p) {
		for(Typhoon t : owner.typhoonBase.getAll()) {
			Rectangle r = modelRectangle(t);
			if(r.contains(p))
				return t;
		}
		return null;
	}
	
	void startStorm(Point p) {
		int w = getWidth();
		startX = (double) (((p.x + 0.5) / w) * 180.0 - 90.0);
		startY = (double) ((1.0 - (p.y + 0.5) / w) * 180.0 - 90.0);
	}
	
	void endStorm(Point p) {
		if(startX == -1)
			return;
		int w = getWidth();
		double endX = (double) (((p.x + 0.5) / w) * 180.0 - 90.0);
		double endY = (double) ((1.0 - (p.y + 0.5) / w) * 180.0 - 90.0);
		double epsDeg = 3.0 * 180.0 / Math.max(w, 1);

		ArrayList<Storm> stormsHit = new ArrayList<>();
	    for (Storm s : owner.stormBase.getAll()) {
	        if (Storm.segmentsHit(startX, startY, endX, endY, s.getX(), s.getY(), s.getEndX(), s.getEndY(), epsDeg)) {
	            stormsHit.add(s);
	        }
	    }
	    if(stormsHit.size() > 0) {
	    	for(Storm s : stormsHit) {
	    		owner.stormBase.remove(s);
	    	}
	    	repaint();
	    	startX = -1; startY = -1;
	    	return;
	    }
		Storm s = new Storm(startX, startY, endX, endY);
		startX = -1; startY = -1;
		owner.stormBase.add(s);
		repaint();
	}
	
	void handlePress(MouseEvent e) {
	    Point p = e.getPoint();

	    if (e.getButton() == MouseEvent.BUTTON1) {
	        Typhoon typhoon = typhoonAt(p);
	        if (typhoon != null) {
	            owner.typhoonBase.remove(typhoon);
	            repaint();
	            return;
	        }
	        if (airportAt(p) == null && airplaneAt(p) == null) {
	            pressTyphoon(p);
	        }
	        return;
	    }

	    if (e.getButton() == MouseEvent.BUTTON3) {
	        if (airportAt(p) == null && airplaneAt(p) == null) {
	            startStorm(p);
	        }
	    }
	}
	
	private Rectangle modelRectangle(Model m) {
		Point p = getCords(m);
		int width = m.getWidth();
		int x = p.x - width/2, y = p.y - width/2;
		return new Rectangle(x, y, width, width);
	}
	
	private Point getCords(Model a) {
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
