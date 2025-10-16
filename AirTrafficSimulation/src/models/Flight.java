package models;
import java.time.LocalTime;

import exceptions.InvalidInputException;
public class Flight { 
	
	//Class that contains departure and destination airports as well
	//as well as the time of departure and the duration of the flight.
	
	private Airport departureAirport; 
	private Airport destinationAirport; 
	private LocalTime departureTime; //HH:mm 
	private int duration; //min
	private int delay; //min
	
	public Flight(Airport departureAirport, Airport destinationAirport, 
			LocalTime departureTime, int duration) {
		this.departureAirport = departureAirport; 
		this.destinationAirport = destinationAirport; 
		this.departureTime = departureTime; 
		this.duration = duration; 
	} 
	
	public LocalTime EstimatedTimeOfArrival() { 
		return departureTime.plusMinutes(duration); 
	}
	
	@Override 
	public String toString() { 
		StringBuilder sb = new StringBuilder(); 
		sb.append(departureAirport.getCode()).append(" ").append(departureTime).append(" -> "); 
		sb.append(destinationAirport.getCode()).append(" ").append(EstimatedTimeOfArrival()); 
		return sb.toString(); 
	} 
	
	public static void validateFlight(String departureAirportCode, String destinationAirportCode, 
			String departureTimeString, String durationString, AirportBase airportBase) throws InvalidInputException{
		
		if (departureAirportCode == null || destinationAirportCode == null || departureTimeString == null || 
				durationString == null || departureTimeString.trim().isEmpty() || durationString.trim().isEmpty()) {
			throw new InvalidInputException("All fields must be filled out.");
		}
		
		departureAirportCode = departureAirportCode.trim();
		destinationAirportCode = destinationAirportCode.trim();
		departureTimeString = departureTimeString.trim();
		durationString = durationString.trim();
		
		String errorMsg = "";
		boolean inputError = false;
		
		if (departureAirportCode.equals(destinationAirportCode)) {
			inputError = true;
			errorMsg += "Airports must be different";
		}
		
		if (airportBase.findByCode(departureAirportCode) == null) {
	        if (inputError)
	        	errorMsg += ", \n";
	        inputError = true;
	        errorMsg += "Departure airport code does not exist: " + departureAirportCode; 
	    }
		
	    if (airportBase.findByCode(destinationAirportCode) == null) {
	    	if (inputError)
	        	errorMsg += ", \n";
	    	inputError = true;
	        errorMsg += "Destination airport code does not exist: " + destinationAirportCode; 
	    }
		
		if(!departureTimeString.matches("^(?:[01]\\d|2[0-3]):[0-5]\\d$")) {
			if (inputError)
				errorMsg += ", \n";
			inputError = true;
			errorMsg += "Time format is invalid";
		}
		
		int durationInt;
		try {
			durationInt = Integer.parseInt(durationString);
		} 
		catch (NumberFormatException e) {
	    	if (inputError)
	    		errorMsg += ", \n";
	    	inputError = true;
			errorMsg += "Duration must be an integer.";
			throw new InvalidInputException(errorMsg);
		}
		
		if (durationInt <= 0) {
			if (inputError)
	    		errorMsg += ", \n";
			inputError = true;
			errorMsg += "Duration must be a positive number";
		}
		
		if (durationInt > 1440) {
			if (inputError)
				errorMsg += ", \n";
			inputError = true;
			errorMsg += "An airplane can't fly for more than 24 hours";
		}
		
		if (inputError) {
			errorMsg += ".";
			throw new InvalidInputException(errorMsg);
		}
	}
	
	//Getters and setters
	
	public Airport getDepartureAirport() {
		return departureAirport;
	}
	
	public Airport getDestinationAirport() {
		return destinationAirport;
	}
	
	public LocalTime getDepartureTime() {
		return departureTime;
	}
	
	public int getDepartureTimeMs() {
		return departureTime.getHour() * 3600 * 1000 + departureTime.getMinute() * 60 * 1000;
	}
	
	public int getDuration() {
		return duration;
	}
	
	public int getDelay() {
		return delay;
	}
	
	public void setDelay(int delay) {
		this.delay = delay;
	}
}