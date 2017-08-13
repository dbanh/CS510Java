package edu.pdx.cs410J.dbanh.server;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sun.tools.internal.ws.wsdl.document.jaxws.Exception;

import edu.pdx.cs410J.AbstractFlight;
import edu.pdx.cs410J.dbanh.client.Airline;
import edu.pdx.cs410J.dbanh.client.Flight;
import edu.pdx.cs410J.dbanh.client.AirlineService;

/**
 * The server-side implementation of the Airline service
 */
public class AirlineServiceImpl extends RemoteServiceServlet implements AirlineService
{
	
  Airline airline;	
	
  @Override
  public Airline getAirline() {
    return airline;
  }

  @Override
  public void throwUndeclaredException() {
    throw new IllegalStateException("Expected undeclared exception");
  }

  @Override
  public void throwDeclaredException() throws IllegalStateException {
    throw new IllegalStateException("Expected declared exception");
  }

  /**
   * Log unhandled exceptions to standard error
   *
   * @param unhandled
   *        The exception that wasn't handled
   */
  @Override
  protected void doUnexpectedFailure(Throwable unhandled) {
    unhandled.printStackTrace(System.err);
    super.doUnexpectedFailure(unhandled);
  }

  @Override
  public void saveAirline(Airline newAirline) throws IllegalArgumentException {
	if(airline == null) {
		airline = new Airline();
		airline.setName(newAirline.getName());
	} else if(!this.airline.getName().equals(newAirline.getName())) {	
		throw new IllegalArgumentException("Airline requested does not match saved airline");
	} 
	
	List<Flight> flights = new ArrayList<Flight>();
	flights = (List<Flight>) newAirline.getFlights();
	for(Flight flight : flights) {
		airline.addFlight(flight);
	}
  }

  @Override
  public Airline searchFlights(String airlineToSearch, String src, String dest) throws IllegalArgumentException, Throwable{
	  if(airline == null) {
		  throw new Throwable();
	  }
	  
	  List<Flight> matchingFlights = new ArrayList<Flight>();
	  
	  if (airline.getName().equals(airlineToSearch)) {
		List<Flight> flights = new ArrayList<Flight>();
		flights = (List<Flight>) airline.getFlights();

		for (Flight flight : flights) {
			if (flight.getSource().toUpperCase().equals(src.toUpperCase()) && flight.getDestination().toUpperCase().equals(dest.toUpperCase())) {
				matchingFlights.add(flight);
			}
		}
	  } else {
		  throw new IllegalArgumentException("No airline doesn't match");
	  }
		
	  if (matchingFlights.size() > 0) {
		  Airline matchingAirline = new Airline();
		  matchingAirline.setName(airlineToSearch);
		  matchingAirline.setFlights(matchingFlights);
		  return matchingAirline;
	  } 
	  
	  return null;
  }
}
