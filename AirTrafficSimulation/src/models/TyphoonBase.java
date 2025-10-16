package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TyphoonBase {
	
	//A base that is used to store and manage a collection of typhoons.
	
	private ArrayList<Typhoon> typhoons = new ArrayList<>();

    public void add(Typhoon t) {    
    	if (t == null) return;
    	typhoons.add(t);
    }

    public void remove(Typhoon t) {
    	typhoons.remove(t);
    }
    
    public void removeAll() {
    	typhoons.clear();
    }
    
    public List<Typhoon> getAll(){
    	return Collections.unmodifiableList(new ArrayList<>(typhoons));
    }
}
