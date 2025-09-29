package simulation;

public class SimulationTimer extends Thread {
	
	private final int tick = 200;
	private final int scale = 600;
	private FlightSimulation sim;
	
	private int ms;
	private long refMs;
	private boolean work;
	
	public SimulationTimer(FlightSimulation sim) {
		this.sim = sim;
	}
	
	public void run() {
		try {
			while(!isInterrupted()) {
				
				synchronized (this) {
					while(!work) {
						wait();
						refMs = 0;
					}
				}
				
				long curr = System.currentTimeMillis();
				if (refMs == 0)
					refMs = curr;
				else {
					long passed = curr - refMs;
					int step = (int)(passed * scale);
					if(step > 0) {
						ms += step;
						if(ms > 86400000)
							ms = 86400000;
						sim.Tick(ms, step);
						refMs = curr;
					}
				}
				sleep(tick);
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
	
	public synchronized void reset() {
		ms = 0;
		refMs = 0;
	}
	
	public synchronized int getMs() {
		return ms;
	}
	
	public synchronized boolean isWorking() { 
		return work; 
	}	
}
