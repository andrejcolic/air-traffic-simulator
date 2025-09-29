package guiCore;

public class IdleTimer extends Thread {
	
	AirTrafficSimulator owner;
	private int s;
	private int limit;
	private boolean work;
	
	public IdleTimer(AirTrafficSimulator owner, int limit) {
		this.owner = owner;
		this.limit = limit;
	}
	
	public void run() {
		IdleWarningDialog dialog = new IdleWarningDialog(owner);
		try {
			while(!isInterrupted()) {
				
				synchronized (this) {
					while(!work) {
						wait();
					}
				}
				sleep(1000);
				s++;
				if(s >= limit - 5) {
					dialog.secondsLeft(limit - s);
				}
				if (s == limit - 5) {
					owner.setMaskKey(false);
			        dialog.setVisible(true);
				}
				if(s == limit) {
					owner.dispose();
					interrupt();
				}
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
		s = 0;
	}
	
	public synchronized int getSec() {
		return limit - s;
	}
	
	@Override
	public String toString() {
		return String.format("%02d:%02d", s/60, s%60);
	}
}
