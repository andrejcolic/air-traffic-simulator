package simulation;

import java.util.List;

import guiCore.AirTrafficSimulator;
import models.Airplane;
import models.AirplaneBase;

public class FlightSimulation {
	
	private AirTrafficSimulator owner;
	private List<DepartureInfo> schedule;
	public AirplaneBase airplaneBase;
	private SimulationTimer timer;
	private boolean active = false;
	private boolean paused = false;
	
	private int onQueue = 0;
	
	public FlightSimulation(AirTrafficSimulator owner, List<DepartureInfo> schedule) {
		this.owner = owner;
		this.schedule = schedule;
		this.airplaneBase = new AirplaneBase();
		this.timer = new SimulationTimer(this);
		this.timer.start();
	}
	
	public void start() {
		if(active) return;
		active = true;
		paused = false;
		airplaneBase.removeAll();
		onQueue = 0;
		timer.reset();
		timer.go();
	}
	
	public void pause() {
		if(!active) return;
		if(!paused)
			timer.pause();
		else
			timer.go();
		
		paused = !paused;
	}
	
	public void reset() {
		if(!active) return;
		pause();
		active = false;
		paused = false;
		airplaneBase.removeAll();
		onQueue = 0;
        timer.reset();
        timer.interrupt();
        owner.scene.repaint();
        owner.flightTablePanel.setSimTime(0);	
	}
	
	public boolean isActive() {
		return active;
	}
	
	void Tick(int ms, int step) {
		while (onQueue < schedule.size()) {
			DepartureInfo d = schedule.get(onQueue);
			if (d.departureMs <= ms) {
				airplaneBase.add(new Airplane(d.flight));
				onQueue++;
			}
			else
				break;
		}
		
		airplaneBase.moveAirplanes(step);
		
		owner.scene.repaint();
		owner.flightTablePanel.setSimTime(ms);	
	}
	
}
