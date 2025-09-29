package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AirportBase {
	
	//Base that is used to manage a collection of airports.
	
	private Map<String, Airport> aMap = new LinkedHashMap<>();
	
	public void add(Airport a) {
		if (a == null) return;
        String code = a.getCode();
        aMap.put(code, a);
    }
	
	public Airport findByCode(String code) {
		return aMap.get(code);
	}
	
	public List<Airport> getAll() {
		return Collections.unmodifiableList(new ArrayList<>(aMap.values()));
    }
}
