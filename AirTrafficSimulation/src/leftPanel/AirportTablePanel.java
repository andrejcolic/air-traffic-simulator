package leftPanel;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;

import exceptions.InvalidInputException;
import guiCore.AirTrafficSimulator;
import models.Airport;

public class AirportTablePanel extends Panel {
	
	private AirTrafficSimulator owner;
	public Label title;
    public TextField code;
    public TextField name;
    public TextField xCord;
    public TextField yCord;
    public Button addBtn;
    public Label status;
    public AirportCheckboxList airportList;
    
	public AirportTablePanel(AirTrafficSimulator owner) {
		super(new BorderLayout());
		this.owner = owner;
		
		title = new Label("Insert Airport information:", Label.LEFT);
		code = new TextField(1);
        name = new TextField(12);
        xCord = new TextField(1);
        yCord = new TextField(1);
        addBtn = new Button("Add");
        status = new Label(" ");
        airportList = new AirportCheckboxList(owner);
        
        Panel header = new Panel(new GridLayout(0, 1, 0, 1));
        header.add(title);
        
        Panel form1 = new Panel(new FlowLayout(FlowLayout.CENTER, 3, 3));
        form1.add(new Label("Code:")); 
        form1.add(code);
        form1.add(new Label("Name:")); 
        form1.add(name);
        Panel form2 = new Panel(new FlowLayout(FlowLayout.CENTER, 3, 0));
        form2.add(new Label("X:"));    
        form2.add(xCord);
        form2.add(new Label("Y:"));    
        form2.add(yCord);
        form2.add(addBtn);
        header.add(form1);
        header.add(form2);
        header.add(status);
        
        add(header, BorderLayout.NORTH);
        add(airportList, BorderLayout.CENTER);
      
        code.addActionListener((ae) -> name.requestFocus());
        name.addActionListener((ae) -> xCord.requestFocus());
        xCord.addActionListener((ae) -> yCord.requestFocus());
        yCord.addActionListener((ae) -> {
			try {
				addAirport();
			} catch(InvalidInputException e){
				setStatus(e.getMessage(), Color.RED);
				code.requestFocus();
			}
		});
	}
	
	public void addAirport() throws InvalidInputException {
		
		String codeText = code.getText().trim().toUpperCase();
	    String nameText = name.getText().trim();
	    String xCordText = xCord.getText().trim();
	    String yCordText = yCord.getText().trim();
	    
	    addAirport(codeText, nameText, xCordText, yCordText);
	    
	    code.setText("");
	    name.setText("");
	    xCord.setText("");
	    yCord.setText("");
	    code.requestFocus();
	}
	
	public void addAirport(String codeText, String nameText, String xCordText, String yCordText) throws InvalidInputException{
		Airport.validateAirport(codeText, nameText, xCordText, yCordText, owner.airportBase);
		
		float x = Float.parseFloat(xCordText);
	    float y = Float.parseFloat(yCordText);
	    
	    Airport a = new Airport(codeText, nameText, x, y);
	    owner.airportBase.add(a);
	    owner.visibleAirports.add(codeText);
	    airportList.refreshList();
	    if(owner.scene != null) owner.scene.repaint();
	    owner.flightTablePanel.addAirportChoice(a);
	    setStatus("Added: " + a.toString(), Color.GREEN);
	}
	
	public void setStatus(String msg, Color c) {
		
		if(status != null) {
			status.setForeground(c);
			status.setText(msg);
			status.repaint();
		}
		
	}
}
