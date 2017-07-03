package edu.pdx.cs410J.dbanh;

import edu.pdx.cs410J.AbstractFlight;

public class Flight extends AbstractFlight {
	
	private String source;
	private String destination;
	private String departureTime;
	private String arrivalTime;
	private String flightNumber;
	
  @Override
  public int getNumber() {
    return 42;
  }

  @Override
  public String getSource() {
	  return source;
//    throw new UnsupportedOperationException("This method is not implemented yet");
  }
  
  public void setSource(String source) {
	  this.source = source;
  }

  @Override
  public String getDepartureString() {
	  return departureTime;
//    throw new UnsupportedOperationException("This method is not implemented yet");
  }
  
  public void setDepartureString(String departureTime) {
	  this.departureTime = departureTime;
  }

  @Override
  public String getDestination() {
	  return destination;
//    throw new UnsupportedOperationException("This method is not implemented yet");
  }
  
  public void setDestination(String destination) {
	  this.destination = destination;
  }

  @Override
  public String getArrivalString() {
	  return arrivalTime;
//    throw new UnsupportedOperationException("This method is not implemented yet");
  }
  
  public void setArrivalString(String arrivalTime) {
	  this.arrivalTime = arrivalTime;
  }

  public String getFlightNumber() {
	return flightNumber;
  }

  public void setFlightNumber(String flightNumber) {
	this.flightNumber = flightNumber;
  }
}
