package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FlightBase {

	private ArrayList<Flight> flights = new ArrayList<>();

    public void add(Flight f) {    
    	if (f == null) return;
        flights.add(f);
    }

    public void remove(Flight f) {
        flights.remove(f);
    }
    
    public void removeAll() {
    	flights.clear();
    }
    
    public List<Flight> getAll(){
    	return Collections.unmodifiableList(new ArrayList<>(flights));
    }
}