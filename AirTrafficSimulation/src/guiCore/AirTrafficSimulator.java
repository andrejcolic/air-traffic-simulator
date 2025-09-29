package guiCore;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.MenuBar;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashSet;
import java.util.Set;

import centerPanel.AirTrafficMap;
import exceptions.InvalidInputException;
import leftPanel.AirportTablePanel;
import menu.FileMenu;
import models.Airport;
import models.AirportBase;
import models.FlightBase;
import rightPanel.FlightTablePanel;
import simulation.FlightScheduler;
import simulation.FlightSimulation;

public class AirTrafficSimulator extends Frame {
	
	public AirportBase airportBase = new AirportBase();
	public Set<String> visibleAirports = new HashSet<>();
	public FlightBase flightBase = new FlightBase();
	MenuBar menuBar = new MenuBar();
	FileMenu fileMenu = new FileMenu(this);
	public AirTrafficMap scene = new AirTrafficMap(this);
	public AirportTablePanel airportTablePanel = new AirportTablePanel(this);
	public FlightTablePanel flightTablePanel = new FlightTablePanel(this);
	public Panel centerPanel = new Panel();
	boolean maskKey = true;
	
	public IdleTimer timer = new IdleTimer(this, 60);
	
	public FlightSimulation sim;
	
	private void populateWindow() {
		
		menuBar.add(fileMenu);
		setMenuBar(menuBar);
		
		int dim = ((getWidth() / 2) / scene.getRows() * scene.getRows());
		scene.setPreferredSize(new Dimension(dim, dim));
		centerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		centerPanel.add(scene);
		add(centerPanel, BorderLayout.CENTER);
		add(airportTablePanel, BorderLayout.WEST);
		add(flightTablePanel, BorderLayout.EAST);
		
		timer.start();
		timer.go();
		
		long mask = AWTEvent.KEY_EVENT_MASK
                | AWTEvent.MOUSE_EVENT_MASK
                | AWTEvent.MOUSE_MOTION_EVENT_MASK
                | AWTEvent.MOUSE_WHEEL_EVENT_MASK;
		
		Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
            @Override
            public void eventDispatched(AWTEvent e) {
            	if(maskKey)
            		timer.reset();
            }
        }, mask);
		
		fileMenu.exit.addActionListener(e -> dispose());
		
		airportTablePanel.addBtn.addActionListener((ae) -> {
			try {
				airportTablePanel.addAirport();
			} catch(InvalidInputException e){
				airportTablePanel.setStatus(e.getMessage(), Color.RED);
				airportTablePanel.code.requestFocus();
			}
		});
		
		flightTablePanel.addBtn.addActionListener((ae) -> {
			try {
				flightTablePanel.addFlight();
			} catch(InvalidInputException e){
				flightTablePanel.setStatus(e.getMessage(), Color.RED);
			}
		});
		
		flightTablePanel.startSimulation.addActionListener((ae) -> {
			if(sim == null || !sim.isActive()) {
				sim = new FlightSimulation(this, FlightScheduler.getSchedule(flightBase));
				timer.pause();
				sim.start();
			}
		});
		
		flightTablePanel.pauseSimulation.addActionListener((ae) -> {
			if(sim != null && sim.isActive())
				sim.pause();
		});
		
		flightTablePanel.endSimulation.addActionListener((ae) -> {
			if(sim != null && sim.isActive()) {
				sim.reset();
				timer.go();
			}
		});
		
	}
	
	public void setMaskKey(boolean b) {
		maskKey = b;
	}
	
	
	
	
	public AirTrafficSimulator() {
		
		setBounds(0, 0, 1500, 700);
		setResizable(false);
		setTitle("Air Traffic Simulation");
		
		populateWindow();
		pack();
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (timer != null)
					timer.interrupt();
				if (sim != null)
					sim.reset();
				dispose();
			}
		});
		
		setVisible(true);
	}

	public static void main(String[] args) {
		new AirTrafficSimulator();
	}
	
}

