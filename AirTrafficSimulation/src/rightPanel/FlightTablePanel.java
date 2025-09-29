package rightPanel;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.List;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.time.LocalTime;

import exceptions.InvalidInputException;
import guiCore.AirTrafficSimulator;
import models.Airport;
import models.Flight;

public class FlightTablePanel extends Panel {
	
	private AirTrafficSimulator owner;
	public Label title;
	Choice chooseAirport1;
	Choice chooseAirport2;
	TextField depTime;
	TextField duration;
    public Button addBtn;
    public TextArea status;
    public List flightList;
    public Button startSimulation;
    public Button pauseSimulation;
    public Button endSimulation;
    public Label simTimeLabel;

	
	public FlightTablePanel(AirTrafficSimulator owner) {
		super(new BorderLayout());
		this.owner = owner;
		
		title = new Label("Insert Flight Information:", Label.LEFT);
		chooseAirport1 = new Choice();
		chooseAirport1.setPreferredSize(new Dimension(50, 0));
		chooseAirport2 = new Choice();
		chooseAirport2.setPreferredSize(new Dimension(50, 0));
		depTime = new TextField(3);
		duration = new TextField(2);
        addBtn = new Button("Add");
        status = new TextArea("", 2, 32, TextArea.SCROLLBARS_NONE);
        status.setEditable(false);
        status.setFocusable(false);
        flightList = new List(18, false);
        startSimulation = new Button("Start");
        pauseSimulation = new Button("Pause");
        endSimulation = new Button("End");
        simTimeLabel = new Label("00:00");
        
        Panel header = new Panel(new GridLayout(0, 1, 0, 1));
        header.add(title);
        
        Panel form1 = new Panel(new FlowLayout(FlowLayout.LEFT, 3, 3));
        form1.add(new Label("Departure Airport:")); 
        form1.add(chooseAirport1);
        form1.add(new Label("Destination Airport:")); 
        form1.add(chooseAirport2);
        Panel form2 = new Panel(new FlowLayout(FlowLayout.CENTER, 3, 0));
        form2.add(new Label("Departure time:"));    
        form2.add(depTime);
        form2.add(new Label("Flight duration:"));    
        form2.add(duration);
        form2.add(addBtn);
        header.add(form1);
        header.add(form2);
        header.add(status);
        
        Panel footer = new Panel(new GridLayout(0, 1, 0, 1));
        Panel simulationHandler = new Panel(new FlowLayout(FlowLayout.CENTER, 3, 3));
        simulationHandler.add(new Label("Simulation:"));
        simulationHandler.add(startSimulation);
        simulationHandler.add(pauseSimulation);
        simulationHandler.add(endSimulation);
        simulationHandler.add(new Label("Time:"));
        simulationHandler.add(simTimeLabel);
        footer.add(simulationHandler);
        
        add(header, BorderLayout.NORTH);
        add(flightList, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);
        
        depTime.addActionListener((ae) -> duration.requestFocus());
        duration.addActionListener((ae) -> {
			try {
				addFlight();
			} catch(InvalidInputException e){
				setStatus(e.getMessage(), Color.RED);
				depTime.requestFocus();
			}
		});
	}
	
	public void addAirportChoice(Airport a) {
		chooseAirport1.add(a.getCode());
		chooseAirport2.add(a.getCode());
	}
	
	public void addFlight() throws InvalidInputException {
		
		String a1Code = chooseAirport1.getSelectedItem();
		String a2Code = chooseAirport2.getSelectedItem();
	    String depTimeText = depTime.getText().trim();
	    String durationText = duration.getText().trim();
	    
	    addFlight(a1Code, a2Code, depTimeText, durationText);
	    
	    depTime.setText("");
	    duration.setText("");
	}

	public void addFlight(String a1Code, String a2Code, String depTimeText, String durationText) throws InvalidInputException {
	    
	    Flight.validateFlight(a1Code, a2Code, depTimeText, durationText, owner.airportBase);
	    
	    int dur = Integer.parseInt(durationText);
	    
	    Flight f = new Flight(owner.airportBase.findByCode(a1Code), owner.airportBase.findByCode(a2Code),
	    		LocalTime.parse(depTimeText), dur);
	    
	    owner.flightBase.add(f);
	    setStatus("Added: " + f.toString(), Color.GREEN);
	    
	    refreshFlightList();
	}
	
	public void setStatus(String msg, Color c) {
		
		if(status != null) {
			status.setForeground(c);
			status.setText(msg);
			status.repaint();
		}
		
	}
	
	private void refreshFlightList() {
	    if (flightList == null) return;
	    flightList.removeAll();
	    for (Flight f : owner.flightBase.getAll()) {
	    	flightList.add(
	            f.toString()
	        );
	    }
	    if (flightList.getItemCount() > 0) {
	    	flightList.select(0);
	    }
	}
	
	public void setSimTime(int ms) {
		int min = ms / 60000;
		int h = min / 60;
		if (h >= 24)
			h -= 24;
		min = min % 60;
		if (simTimeLabel != null) simTimeLabel.setText(String.format("%02d:%02d", h, min));
	}
}
