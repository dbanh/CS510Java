package edu.pdx.cs410J.dbanh.client;

import edu.pdx.cs410J.AbstractFlight;

import java.util.Date;
import com.google.gwt.i18n.client.DateTimeFormat;

public class Flight extends AbstractFlight implements Comparable<Flight>
{
	
  private String source;
  private String destination;
  private Date departure;
  private String departureString;
  private Date arrival;
  private String arrivalString;
  private int number;
  
  static final String DATE_FORMAT_PATTERN = "MM/dd/yyyy hh:mm a";
  
  /**
   * In order for GWT to serialize this class (so that it can be sent between
   * the client and the server), it must have a zero-argument constructor.
   */
  public Flight() {

  }

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
  public Date getDeparture() {
    return new Date();
  }
  
  public void setDeparture(Date departure) {
	  this.departure = departure;
  }

  public String getDepartureString() {
	  DateTimeFormat fmt = DateTimeFormat.getFormat("MM/dd/yyyy hh:mm a");
	  return fmt.format(departure);
  }

  public String getDestination() {
    return destination;
  }
  
  public void setDestination(String destination) {
	  this.destination = destination;
  }

  public Date getArrival() {
    return new Date();
  }
  
  public void setArrival(Date arrival) {
	  this.arrival = arrival;
  }

  public String getArrivalString() {
	  DateTimeFormat fmt = DateTimeFormat.getFormat("MM/dd/yyyy hh:mm a");
	  return fmt.format(arrival);
  }
  
  @Override
  public int compareTo(Flight flight) {
  	  int result = this.getSource().compareTo(flight.getSource());
  	  if(result != 0) {
  		  return result;
  	  }

  	  Date passedInDate = new Date();
  	  Date localDate = new Date();
		DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat(DATE_FORMAT_PATTERN);
		try {
			passedInDate = dateTimeFormat.parse(flight.getDepartureString());
			localDate = dateTimeFormat.parse(this.getDepartureString());
		} catch (IllegalArgumentException e) {
		}
	  
	  return localDate.compareTo(passedInDate);
  }

  	public void setDepartureString(String departureString) {
		this.departureString = departureString;
	}
	
	public void setArrivalString(String arrivalString) {
		this.arrivalString = arrivalString;
	}

}
