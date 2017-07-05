package edu.pdx.cs410J.dbanh;

import edu.pdx.cs410J.AbstractFlight;

public class Flight extends AbstractFlight {
	
	private String source;
	private String destination;
	private String departureTime;
	private String arrivalTime;
	private int number;
	
  @Override
  public int getNumber() {
    return number;
  }
  
  public void setNumber(int number) {
	  this.number = number;
  }

  @Override
  public String getSource() {
	  return source;
  }
  
  public void setSource(String source) {
	  this.source = source;
  }

  @Override
  public String getDepartureString() {
	  return departureTime;
  }
  
  public void setDepartureString(String departureTime) {
	  this.departureTime = departureTime;
  }

  @Override
  public String getDestination() {
	  return destination;
  }
  
  public void setDestination(String destination) {
	  this.destination = destination;
  }

  @Override
  public String getArrivalString() {
    return arrivalTime;
  }
  
  public void setArrivalString(String arrivalTime) {
	  this.arrivalTime = arrivalTime;
  }
}
