package edu.pdx.cs410J.dbanh;

import edu.pdx.cs410J.AbstractAirline;

/**
 * The main class for the CS410J airline Project
 */
public class Project1 {

  public static void main(String[] args) {
    Class c = AbstractAirline.class;  // Refer to one of Dave's classes so that we can be sure it is on the classpath
    
    if(args.length == 0) {
    	System.err.println("Missing command line arguments");
    } 
    
    else if (args.length == 6) {
    	Airline airline = new Airline();
    	Flight flight = new Flight();
		for (int i = 0; i < args.length; ++i) {
		      if (i == 0) {
		    	  airline.setName(args[i]);
		      }
		      else if (i == 1) {
		    	 flight.setFlightNumber(args[i]);
		      }
		      else if (i == 2) {
		    	  flight.setSource(args[i]);
		      }
		      else if (i == 3) {
		    	  flight.setDepartureString(args[i]);
		      }
		      else if (i == 4) {
		    	  flight.setDestination(args[i]);
		      }
		      else if (i == 5) {
		    	  flight.setArrivalString(args[i]);
		      }
		} 
		airline.addFlight(flight);
    }
    
    else {
    	System.err.println("Command line arguments incomplete. Please check arguments and try again.");
    }
    
    System.exit(1);
  }

}