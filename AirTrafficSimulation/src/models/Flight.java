package models;
import java.time.LocalTime;

import exceptions.InvalidInputException;
public class Flight { 
	
	//Class that contains departure and destination airports as well
	//as well as the time of departure and the duration of the flight.
	
	private Airport a1; 
	private Airport a2; 
	private LocalTime departure; 
	int duration; 
	
	public Flight(Airport a1, Airport a2, LocalTime dep, int dur) {
		this.a1 = a1; 
		this.a2 = a2; 
		this.departure = dep; 
		this.duration = dur; 
	} 
	
	public LocalTime ETA() { 
		return departure.plusMinutes(duration); 
	}
	
	public Airport getDepartureAirport() {
		return a1;
	}
	
	public Airport getDestinationAirport() {
		return a2;
	}
	
	public LocalTime getDepTime() {
		return departure;
	}
	
	public int getDepMs() {
		return departure.getHour() * 3600 * 1000 + departure.getMinute() * 60 * 1000;
	}
	
	public int getDuration() {
		return duration;
	}
	
	@Override 
	public String toString() { 
		StringBuilder sb = new StringBuilder(); 
		sb.append(a1.getCode()).append(" ").append(departure).append(" -> "); 
		sb.append(a2.getCode()).append(" ").append(ETA()); 
		return sb.toString(); 
	} 
	
	public static void validateFlight(String a1Code, String a2Code, 
			String depTimeText, String durationText, AirportBase airportBase) throws InvalidInputException{
		
		if (a1Code == null || a2Code == null || depTimeText == null || durationText == null
		        || depTimeText.trim().isEmpty() || durationText.trim().isEmpty()) {
			throw new InvalidInputException("All fields must be filled out.");
		}
		
		a1Code = a1Code.trim();
	    a2Code = a2Code.trim();
	    depTimeText = depTimeText.trim();
	    durationText = durationText.trim();
		
		String errorMsg = "";
		boolean typeError = false;
		
		if (a1Code.equals(a2Code)) {
			typeError = true;
			errorMsg += "Airports must be different";
		}
		
		if (airportBase.findByCode(a1Code) == null) {
	        if (typeError)
	        	errorMsg += ", \n";
	        typeError = true;
	        errorMsg += "Departure airport code does not exist: " + a1Code; 
	    }
		
	    if (airportBase.findByCode(a2Code) == null) {
	    	if (typeError)
	        	errorMsg += ", \n";
	        typeError = true;
	        errorMsg += "Destination airport code does not exist: " + a2Code; 
	    }
		
		if(!depTimeText.matches("^(?:[01]\\d|2[0-3]):[0-5]\\d$")) {
			if (typeError)
				errorMsg += ", \n";
			typeError = true;
			errorMsg += "Time format is invalid";
		}
		
		int durationInt;
		try {
			durationInt = Integer.parseInt(durationText);
		} catch (NumberFormatException e) {
	    	if (typeError)
	    		errorMsg += ", \n";
	    	typeError = true;
			errorMsg += "Duration must be an integer.";
			throw new InvalidInputException(errorMsg);
		}
		
		if (durationInt <= 0) {
			if (typeError)
	    		errorMsg += ", \n";
			typeError = true;
			errorMsg += "Duration must be a positive number";
		}
		
		if (durationInt > 1440) {
			if (typeError)
				errorMsg += ", \n";
			typeError = true;
			errorMsg += "An airplane can't fly for more than 24 hours";
		}
		
		if (typeError) {
			errorMsg += ".";
			throw new InvalidInputException(errorMsg);
		}
	}
}