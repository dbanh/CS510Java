package edu.pdx.cs410J.dbanh;

import edu.pdx.cs410J.AbstractFlight;

public class Flight extends AbstractFlight {
	
	private String source;
	private String destination;
	private String departureDate;
	private String departureTime;
	private String arrivalDate;
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
	  return new StringBuilder(departureDate).append(" ").append(departureTime).toString();
  }
  
  public void setDepartureTime(String departureTime) {
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
	  return new StringBuilder(arrivalDate).append(" ").append(arrivalTime).toString();

  }
  
  public void setArrivalTime(String arrivalTime) {
	  this.arrivalTime = arrivalTime;
  }

public String getDepartureDate() {
	return departureDate;
}

public void setDepartureDate(String departureDate) {
	this.departureDate = departureDate;
}

public String getArrivalDate() {
	return arrivalDate;
}

public void setArrivalDate(String arrivalDate) {
	this.arrivalDate = arrivalDate;
}
}
