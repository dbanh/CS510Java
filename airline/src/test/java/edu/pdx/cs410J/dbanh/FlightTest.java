package edu.pdx.cs410J.dbanh;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

/**
 * Unit tests for the {@link Flight} class.
 */
public class FlightTest {
  
  @Test
  public void getArrivalStringNeedsToBeImplemented() {
    Flight flight = new Flight();
    
	String sb = new StringBuilder("1/1/2017").append(" ").append("1:11").toString();
	SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm");
	Date date;
	try {
		date = formatter.parse(sb);
		flight.setArrival(date);
	} catch (ParseException e) {
		
	}

    assertThat(flight.getArrivalString(), is("01/01/2017 01:11 AM"));
  }

  @Test
  public void checkFlightNumberCreatedCorrectly() {
    Flight flight = new Flight();
    flight.setNumber(42);
    assertThat(flight.getNumber(), equalTo(42));
  }

  @Test
  public void forProject1ItIsOkayIfGetDepartureTimeReturnsNull() {
    Flight flight = new Flight();
    assertThat(flight.getDeparture(), is(nullValue()));
  }
  
  @Test
  public void checkAirlineAndFlightCreatedCorrectly() {
	  String name = "Alaska Airlines";
	  Airline airline = new Airline(name);
	  Flight flight = new Flight();

	  int number = 42;
	  flight.setNumber(number);
	  String source = "PDX";
	  flight.setSource(source);
	  String departureDate = "1/1/2017";
	  String departureTime = "1:11";

	  String departure = new StringBuilder(departureDate).append(" ").append(departureTime).append("AM").toString();
	  SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm");
	  Date departureDateTime;
	  try {
		  departureDateTime = formatter.parse(departure);
		  flight.setDeparture(departureDateTime);
	  } catch (ParseException e) {
	  }

	  String destination = "LAX";
	  flight.setDestination(destination);
	  String arrivalDate = "1/1/2017";
	  String arrivalTime = "3:11";
	  
	  String arrival = new StringBuilder(arrivalDate).append(" ").append(arrivalTime).append("AM").toString();
	  Date arrivalDateTime;
	  try {
		  arrivalDateTime = formatter.parse(arrival);
		  flight.setArrival(arrivalDateTime);
	  } catch (ParseException e) {
	  }
	  airline.addFlight(flight);
	  
	  assertThat(airline.getFlights(), is(notNullValue()));
	  assertThat(airline.getName(), is(name));
	  assertThat(flight.getNumber(), is(number));
	  assertThat(flight.getSource(), is(source));
	  assertThat(flight.getDepartureString(), is("01/01/2017 01:11 AM"));
	  assertThat(flight.getDestination(), is(destination));
	  assertThat(flight.getArrivalString(), is("01/01/2017 03:11 AM"));
  }
  
}
