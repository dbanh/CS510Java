package edu.pdx.cs410J.dbanh.client;

import edu.pdx.cs410J.AbstractAirline;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Airline extends AbstractAirline<Flight>
{
  /**
   * In order for GWT to serialize this class (so that it can be sent between
   * the client and the server), it must have a zero-argument constructor.
   */
  public Airline() {

  }

//  private Collection<Flight> flights = new ArrayList<>();
  private List<Flight> flights = new ArrayList<Flight>();
  private String name;

  @Override
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
	  this.name = name;
  }

  @Override
  public void addFlight(Flight flight) {
    this.flights.add(flight);
  }

  @Override
  public Collection<Flight> getFlights() {
    return this.flights;
  }
  
  public void setFlights(List<Flight> flights) {
	  this.flights = flights;
  }
}
