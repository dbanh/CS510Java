package edu.pdx.cs410J.dbanh;

import java.text.DateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

import edu.pdx.cs410J.AbstractFlight;

public class Flight extends AbstractFlight implements Comparable<Flight> {
	
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

  @Override
  public int compareTo(Flight flight) {
	  int result = this.getSource().compareTo(flight.getSource());
	  if(result != 0) {
		  return result;
	  }
	  System.out.println("time compare: "+ this.getDeparture().compareTo(flight.getDeparture()));
	  
	  return this.getDeparture().compareTo(flight.getDeparture());
//	  if(this.getDeparture().compareTo(flight.getDeparture()) == 1) {
//		  System.out.println("this: " + this.getDepartureString());
//		  System.out.println("flight: " + flight.getDepartureString());
//		  System.out.println("After");
//		  return 1;
//	  }
//	  else if (this.getDeparture().compareTo(flight.getDeparture()) == -1) {
//		  System.out.println("this: " + this.getDepartureString());
//		  System.out.println("flight: " + flight.getDepartureString());
//		  System.out.println("Before");
//		  return -1;
//	  }
//	  else {
//		  return 0;
//	  }
  }

}
