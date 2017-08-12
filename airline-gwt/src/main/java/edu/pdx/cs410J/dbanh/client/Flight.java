package edu.pdx.cs410J.dbanh.client;

import edu.pdx.cs410J.AbstractFlight;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Flight extends AbstractFlight implements Comparable<Flight>
{
  /**
   * In order for GWT to serialize this class (so that it can be sent between
   * the client and the server), it must have a zero-argument constructor.
   */
  public Flight() {

  }
  private String source;
	private String destination;
	private Date departure;
	private Date arrival;
	private int number;
//	final org.slf4j.Logger logger = LoggerFactory.getLogger(Flight.class);
	  
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
	  this.source = source.toUpperCase();
}

@Override
public String getDepartureString() {
	  Locale currentLocale = new Locale("en_US");
	  DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, currentLocale);
	  return formatter.format(this.departure);
}

@Override
public String getDestination() {
	  return destination;
}

public void setDestination(String destination) {
	  this.destination = destination.toUpperCase();
}

@Override
public String getArrivalString() {
	  Locale currentLocale = new Locale("en_US");
	  DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, currentLocale);
	  return formatter.format(this.arrival);
} 

public Date getDeparture() {
	return departure;
}
	
public void setDeparture(Date departure) {
	this.departure = departure;
}
	
public Date getArrival() {
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
	  Locale currentLocale = new Locale("en_US");
	  DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, currentLocale);
	  String formattedDeparture = formatter.format(flight.departure);
	  SimpleDateFormat simpleFormatter = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
	  Date date;

	  try {
		date = formatter.parse(formattedDeparture);
	  } catch (ParseException e) {
		return 0;
	  }
	  
	  return this.getDeparture().compareTo(date);
}

}
