package edu.pdx.cs410J.dbanh;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Iterator;

/**
 * Unit tests for the {@link Flight} class.
 */
public class TextDumperTest {
	
  
  @Test
  public void getArrivalStringNeedsToBeImplemented() {
    Flight flight = new Flight();
    flight.setArrivalDate("1/1/2017");
    flight.setArrivalTime("1:11");
    assertThat(flight.getArrivalString(), is("1/1/2017 1:11"));
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
	  flight.setDepartureDate(departureDate);
	  String departureTime = "1:11";
	  flight.setDepartureTime(departureTime);
	  String destination = "LAX";
	  flight.setDestination(destination);
	  String arrivalDate = "1/1/2017";
	  flight.setArrivalDate(arrivalDate);
	  String arrivalTime = "3:11";
	  flight.setArrivalTime(arrivalTime);
	  airline.addFlight(flight);
	  
	  assertThat(airline.getFlights(), is(notNullValue()));
	  assertThat(airline.getName(), is(name));
	  assertThat(flight.getNumber(), is(number));
	  assertThat(flight.getSource(), is(source));
	  String departure = new StringBuilder(departureDate).append(" ").append(departureTime).toString();
	  assertThat(flight.getDepartureString(), is(departure));
	  assertThat(flight.getDestination(), is(destination));
	  String arrival = new StringBuilder(arrivalDate).append(" ").append(arrivalTime).toString();
	  assertThat(flight.getArrivalString(), is(arrival));
  }
  
}
