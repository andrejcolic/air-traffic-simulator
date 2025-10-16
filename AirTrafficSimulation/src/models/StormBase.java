package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StormBase {
	
	//A base that is used to store and manage a collection of storms.
	
	private ArrayList<Storm> storms = new ArrayList<>();

    public void add(Storm s) {    
    	if (s == null) return;
    	storms.add(s);
    }

    public void remove(Storm s) {
    	storms.remove(s);
    }
    
    public void removeAll() {
    	storms.clear();
    }
    
    public List<Storm> getAll(){
    	return Collections.unmodifiableList(new ArrayList<>(storms));
    }
}
