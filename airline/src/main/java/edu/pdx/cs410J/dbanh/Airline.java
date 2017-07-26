package edu.pdx.cs410J.dbanh;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.pdx.cs410J.AbstractAirline;
import edu.pdx.cs410J.AbstractFlight;

public class Airline extends AbstractAirline {
	
	private List<AbstractFlight> flights = new ArrayList<AbstractFlight>();
	private String name;

	public Airline(String name) {
		this.name = name;
	}
	
@Override
public void addFlight(AbstractFlight arg0) {
	this.flights.add(arg0);
}

@Override
public Collection<AbstractFlight> getFlights() {
	// TODO Auto-generated method stub
	return flights;
}

@Override
public String getName() {
	// TODO Auto-generated method stub
	return name;
}

public void setName(String name) {
	this.name = name;
}

public void setFlights(List<Flight> flights) {
	List<AbstractFlight> abstractFlights = new ArrayList<AbstractFlight>(flights);
	this.flights = abstractFlights;
}

}
