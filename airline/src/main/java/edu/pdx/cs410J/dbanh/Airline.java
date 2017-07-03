package edu.pdx.cs410J.dbanh;

import java.util.Collection;
import java.util.List;

import edu.pdx.cs410J.AbstractAirline;
import edu.pdx.cs410J.AbstractFlight;

public class Airline extends AbstractAirline {
	
	private List<AbstractFlight> flights;
	private String name;

@Override
public void addFlight(AbstractFlight arg0) {
	// TODO Auto-generated method stub
	flights.add(arg0);
	
}

@Override
public Collection getFlights() {
	// TODO Auto-generated method stub
	return flights;
}

@Override
public String getName() {
	// TODO Auto-generated method stub
	return name;
}

public void setName(String name) {
	name = name;
}
}
