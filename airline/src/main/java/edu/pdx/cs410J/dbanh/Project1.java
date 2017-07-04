package edu.pdx.cs410J.dbanh;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import edu.pdx.cs410J.AbstractAirline;

/**
 * The main class for the CS410J airline Project
 */
public class Project1 {

	  public static void main(String[] args) {
	    Class c = AbstractAirline.class;  // Refer to one of Dave's classes so that we can be sure it is on the classpath
		List<String> errorList = new ArrayList<String>();
		Airline airline = new Airline();
		Flight flight = new Flight();
		
		if(args.length == 0) {
			System.err.println("Missing command line arguments");
		}
		
		else if(args[0].equals("-README")) {
			printReadMe();
		}
		
		else if(correctNumberOfArgs(args) == true) {
		    errorList = createAirlineAndFlight(args, airline, flight);
		
		    if(errorList.isEmpty()) {
		    	if(args[0].equals("-print") || args[1].equals("-print")) {
		    		Iterator<Flight> iterator = airline.getFlights().iterator();
		    		while(iterator.hasNext()) {
		    			System.out.println(iterator.next());
		    		}
		    	}
		    	
		    }
		    else {
		    	for(String error : errorList) {
		    		System.err.println(error);
		    	}
		    }
		}
		
		else {
			System.err.println("ERROR: Command line arguments not valid. Please check arguments and try again.");
			System.err.println("Arguments: " + args.length);
		    }
	    
	    System.exit(1);
	  }
  
	  /* INPUT: list of arguments from command line
	   * OUTPUT: true if there are 8 arguments, false if any other value
	   */
	 private static boolean correctNumberOfArgs(String [] args) {
		 int argsCount = 0;
		 
		 for(int i = 0; i < args.length; ++i) {
			 if(args[i].charAt(0) != '-') {
				 ++argsCount;
			 }
		 }
		 
		 if(argsCount == 8) {
			 return true;
		 }
		 else {
			 return false;
		 }
	 }

	 /* INPUT: list of arguments from command line, an Airline object, and Flight object
	  * OUTPUT: List of strings with any error messages from validation
	  * The method parses the argument list from command line, validates each argument according to their type, and saves the values in the airline and flight objects
	  * Arguments are expected in this order:
	  * 1 - -README or -print
	  * 2 - -README or -print
	  * 3 - Airline Name
	  * 4 - Flight number (numeric)
	  * 5 - Departure airport (3 character code)
	  * 6 - Departure date (MM/DD/YYYY)
	  * 7 - Departure time (HH:MM)
	  * 8 - Destination airport (3 character code)
	  * 9 - Arrival date (MM/DD/YYYY)
	  * 10 - Arrival time (HH:MM)
	  */
	private static List<String> createAirlineAndFlight(String[] args, Airline airline, Flight flight) {
	
		List<String> errorList = new ArrayList<String>();
		
		int startingPosition = args.length - 8; //accounts for any options included in the argument list
		
		for (int i = startingPosition; i < args.length; ++i) {
			
			if(i == startingPosition) {
		    	 airline.setName(args[i]);
			}
			else if(i == startingPosition+1) {
		    	try {
		    		int number = Integer.valueOf(args[i]);
		    		flight.setNumber(number);
		    	} catch (NumberFormatException e) {
		    		errorList.add("ERROR: Flight number is non-numeric");
		    	} 
			}
			else if( i == startingPosition+2) {
				if(args[i].length() == 3) {
					flight.setSource(args[i]);
				}
				else {
					errorList.add("ERROR: Source airport code is not 3 characters");
				}
			}
			else if(i == startingPosition+3) {
				String date = new StringBuilder(args[i]).append(" ").append(args[++i]).toString(); //concatenates date and time
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/DD/YYYY HH:MM");
				try {
					Date departureDate = simpleDateFormat.parse(date); //validates date/time is in correct format
					flight.setDepartureString(date); //but uses the string value for now
				} catch (ParseException e) {
					errorList.add("ERROR: Destination time is not in the correct format. (Correct format: MM/DD/YYYY HH:MM)");
				}
			}
			else if(i == startingPosition+5) {
				if(args[i].length() == 3) {
					flight.setDestination(args[i]);
				}
				else {
					errorList.add("ERROR: Destination airport code is not 3 characters");
				}
			}
			else if(i == startingPosition+6) {
				String date = new StringBuilder(args[i]).append(" ").append(args[++i]).toString(); //concatenates date and time
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/YYYY hh:mm");
				try {
					Date arrivalDate = simpleDateFormat.parse(date); //validates date/time is in correct format
					flight.setArrivalString(date); //but uses the string value for now
				} catch (ParseException e) {
					errorList.add("ERROR: Arrival time is not in the correct format. (Correct format: dd/MM/YYYY hh:mm)");
				}
			}
		} 
		airline.addFlight(flight);
		
		return errorList;
	}

	private static void printReadMe() {
		System.out.println("\n------------------README------------------\n");
		System.out.println("DENISE BANH\nCS510 | ADVANCED PROGRAMMING WITH JAVA");
		System.out.println("PROJECT 1: BEGINNING A JAVA APPLICATION\n");
		System.out.println("This application contains classes that will track an airline as well as flights for the airline.\n"
				+ "The Airline class consists of the airline's name and a List of Flight(s).\n"
				+ "The Flight class consists of the flight number, source location, departure time, destination location, and arrival time\n"
				+ "The application will read in from the command line the airline name, flight number, source location, departure date, departure time, destination, and arrival time\n"
				+ "(as arguments in that order)\n"
				+ "\nThe user also has the ability to invoke two options: -print or -README\n"
				+ "-print will print a description of the new flight\n"
				+ "-README will print a README for this project and exits without saving the flight(currently showing)\n"
				+ "These options can be specified -before- the arguments and only one can be specified at a time.\n"
				+ "\n----------------END README----------------\n");
	}

}