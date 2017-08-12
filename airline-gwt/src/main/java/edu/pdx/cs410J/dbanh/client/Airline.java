package edu.pdx.cs410J.dbanh.client;

import edu.pdx.cs410J.AbstractAirline;
import edu.pdx.cs410J.AbstractFlight;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class Airline extends AbstractAirline<Flight>
{
  /**
   * In order for GWT to serialize this class (so that it can be sent between
   * the client and the server), it must have a zero-argument constructor.
   */
  public Airline() {

  }

    private List<Flight> flights = new ArrayList<Flight>();
	private String name;
	
	@Override
	public void addFlight(Flight flight) {
		this.flights.add(flight);
	}
	
	@Override
	public Collection<Flight> getFlights() {
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
//		List<AbstractFlight> abstractFlights = new ArrayList<AbstractFlight>(flights);
		this.flights = flights;
	}
	
	public void setAbstractFlights(List<AbstractFlight> abstractFlights) {
		List<Flight> flights = new ArrayList<Flight>();
		
		Iterator<AbstractFlight> iterator = abstractFlights.iterator();
		while(iterator.hasNext()) {
			AbstractFlight airlineFlight = iterator.next();
			flights.add((Flight) airlineFlight);
		}	
		
		this.flights = flights;
	}



}
