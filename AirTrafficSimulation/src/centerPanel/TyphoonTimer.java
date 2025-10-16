package centerPanel;

import models.Typhoon;

public class TyphoonTimer extends Thread {
	
	Typhoon storm;
	private boolean work;
	AirTrafficMap owner;
	
	public TyphoonTimer(AirTrafficMap owner, Typhoon s) {
		this.owner = owner;
		this.storm = s;
	}
	
	public void run() {
		try {
			while(!isInterrupted()) {
				
				synchronized (this) {
					while(!work) {
						wait();
					}
				}
				sleep(200);
				storm.setWidth(storm.getWidth() + 2);
				owner.repaint();
			}
		} catch(InterruptedException e) {}
	}
	
	public synchronized void go() {
		work = true;
		notify();
	}
	
	public synchronized void pause() {
		work = false;
	}
	
	public boolean running() {
		return work;
	}
}
