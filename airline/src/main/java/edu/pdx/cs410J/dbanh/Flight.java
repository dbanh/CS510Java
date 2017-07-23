package edu.pdx.cs410J.dbanh;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import edu.pdx.cs410J.AbstractFlight;

public class Flight extends AbstractFlight {
	
	private String source;
	private String destination;
	private Date departure;
	private Date arrival;
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
	  Locale currentLocale = new Locale("en_US");
	  DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, currentLocale);
	  return formatter.format(departure);
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
	  Locale currentLocale = new Locale("en_US");
	  DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, currentLocale);
	  return formatter.format(arrival);

  }
  
  public Date getDeparture() {
	return departure;
  }
	
  public void setDeparture(Date departure) {
	this.departure = departure;
  }
	
  public Date getArriva() {
	return arrival;
  }
  
  public void setArrival(Date arrival) {
	  this.arrival = arrival;
  }

}
