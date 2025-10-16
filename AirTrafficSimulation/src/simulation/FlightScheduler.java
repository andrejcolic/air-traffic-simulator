package simulation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Airport;
import models.Flight;
import models.FlightBase;

public class FlightScheduler {
	
	private static final int gap = 10 * 60000;
	
	private FlightScheduler() {}
	
	public static List<DepartureInfo> getSchedule(FlightBase flights) {
		
	    Map<Airport, List<Flight>> airportFlights = new HashMap<>();
	    for(Flight f : flights.getAll()) {
	    	Airport a = f.getDepartureAirport();
	    	List<Flight> list = airportFlights.get(a);
	    	if (list == null) {
	    		list = new ArrayList<Flight>();
	    		airportFlights.put(a, list);
	    	}
	    	list.add(f);
	    }
	    
	    List<DepartureInfo> schedule = new ArrayList<>();
	    
	    for (Map.Entry<Airport, List<Flight>> e : airportFlights.entrySet()) {
            List<Flight> list = e.getValue();
            list.sort((a, b) -> Integer.compare(a.getDepartureTimeMs(), b.getDepartureTimeMs()));
            
            int last = -gap;
            for(Flight f : list) {
            	int planned = f.getDepartureTimeMs();
            	int allowed = last + gap;
            	
            	int departure = Math.max(planned, allowed);
            	f.setDelay((departure-planned)/60000);
            	schedule.add(new DepartureInfo(f, departure));
            	last = departure;
            }
	    }
	    schedule.sort((a, b) -> Integer.compare(a.departureMs, b.departureMs));
	    return schedule;
	}
}
