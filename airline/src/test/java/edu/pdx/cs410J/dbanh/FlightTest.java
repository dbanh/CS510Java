package edu.pdx.cs410J.dbanh;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Iterator;

/**
 * Unit tests for the {@link Flight} class.
 */
public class FlightTest {
  
  @Test
  public void getArrivalStringNeedsToBeImplemented() {
    Flight flight = new Flight();
    flight.setArrivalString("1/1/2017 1:11");
    flight.getArrivalString();
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
	  Airline airline = new Airline();
	  Flight flight = new Flight();
	  
	  String name = "Alaska Airlines";
	  airline.setName(name);
	  int number = 42;
	  flight.setNumber(number);
	  String source = "PDX";
	  flight.setSource(source);
	  String departure = "1/1/2017 1:11";
	  flight.setDepartureString(departure);
	  String destination = "LAX";
	  flight.setDestination(destination);
	  String arrival = "1/1/2017 3:11";
	  flight.setArrivalString(arrival);
	  airline.addFlight(flight);
	  
	  assertThat(airline.getFlights(), is(notNullValue()));
	  assertThat(airline.getName(), is(name));
	  assertThat(flight.getNumber(), is(number));
	  assertThat(flight.getSource(), is(source));
	  assertThat(flight.getDepartureString(), is(departure));
	  assertThat(flight.getDestination(), is(destination));
	  assertThat(flight.getArrivalString(), is(arrival));
  }
  
}
