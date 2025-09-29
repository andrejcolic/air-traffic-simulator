package simulation;

import models.Flight;

public class DepartureInfo {
	public final Flight flight;
    public final int departureMs;

    public DepartureInfo(Flight flight, int departure) {
        this.flight = flight;
        this.departureMs  = departure;
    }
}
