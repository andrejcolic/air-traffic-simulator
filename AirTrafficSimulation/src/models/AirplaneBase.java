package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AirplaneBase {
	
	//Base that is used to manage a collection of airplanes.

	private ArrayList<Airplane> airplanes = new ArrayList<>();

    public void add(Airplane a) {       
    	if (a == null) return;
        airplanes.add(a);
    }

    public void remove(Airplane a) {
        airplanes.remove(a);
    }
    
    public void removeAll() {
    	airplanes.clear();
    }
    
    public List<Airplane> getAll(){
    	return Collections.unmodifiableList(new ArrayList<>(airplanes));
    }
    
    public void moveAirplanes(int step) {
    	for (Airplane a : airplanes)
    		a.moveAirplane(step);
    	for (int i = airplanes.size() - 1; i >= 0; i--) {
            Airplane a = airplanes.get(i);
            if (a.arrived()) airplanes.remove(i);
        }
    }
}
