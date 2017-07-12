package edu.pdx.cs410J.dbanh;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.pdx.cs410J.AbstractAirline;

/**
 * The main class for the CS410J airline Project
 * 
 * Creates an airline and flight (which is added to the airline)
 * If the -README option is invoked, the program will print out the README but will not run the rest of the program.
 * The program will validate command line arguments according to the criteria specified and will display a list of errors if any validation errors occur.
 * The program will not print the airline/flight details entered by the user (if the user enters valid arguments) unless the -print option is invoked.
 * Options (-README and -print) MUST come before airline/flight arguments.
 */
public class Project1 {

	  public static void main(String[] args) {
	    Class c = AbstractAirline.class;  // Refer to one of Dave's classes so that we can be sure it is on the classpath
		List<String> errorList = new ArrayList<String>();
		Airline airline = new Airline();
		Flight flight = new Flight();
		
		if(args.length == 0) {
			System.err.println("ERROR: Missing command line arguments");
		}
		
		else if(containsReadMe(args) == true) {
			printReadMe();
		}
		
		else if(correctNumberOfArgs(args) == true) {
		    errorList = createAirlineAndFlight(args, airline, flight);
		
		    if(errorList.isEmpty()) {
		    	if(args[0].toLowerCase().equals("-print") || args[1].toLowerCase().equals("-print")) {
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
		    }

	    System.exit(1);
	  }
	  
	  /**
	   * Method checks to see if -README was invoked as one of the options. Options will only be accepted before the airline/flight arguments. The method 
	   * will immediately return false once an argument without a '-' is encountered (this assumes that airline/flight arguments will not begin with a '-').
	   * @param arguments from command line
	   * @return true if one of the arguments classified as "options" is -README, otherwise return false
	   */
	  
	  private static boolean containsReadMe(String [] args) {
		  for(int i = 0; i < args.length; ++i) {
			  if(args[i].toLowerCase().equals("-readme")) {
				  return true;
			  }
			  if(args[i].charAt(0) != '-') {
				  return false;
			  }
		  }
		  
		  return false;
	  }
  
	  /**
	   * Method checks to see if the number of arguments from the command line is 8 (the number of arguments necessary to create and airline and flight). 
	   * Arguments that are classified as "options" (-print and -README) will not be counted as part of the 8 arguments. But the method will verify that options
	   * appear before airline/flight arguments. This method assumes that any argument that begins with a '-' is an option and not an airline/flight argument.
	   * @param arguments from the command line (includes "options" and "arguments"
	   * @return true if the number of arguments in 8 (not including options), false if there are more or less than 8 arguments (not including options)
	   */
	 private static boolean correctNumberOfArgs(String [] args) {
		 int argsCount = 0;
		 int optionsCount = 0;

		 for(int i = 0; i < args.length; ++i) {
			 if(args[i].charAt(0) != '-') {
				 ++argsCount;
			 }
			 else {
				 //verify options are either -print or -README
				 if(args[i].equals("-print") || args[i].equals("-README")) {
					 ++optionsCount;
					 
					 //if options are not listed before airline/flight arguments, return failed validation
					 //i would be equal to optionsCount if options are before arguments
					 if(i > argsCount && i > optionsCount) {
						 return false;
					 }
				 }
				 //if argument is not -print or -README, assume they are a part of the airline/flight arguments
				 else {
					 ++argsCount;
				 }
			 }
		 }
		 
		 if(argsCount == 8) {
			 return true;
		 }
		 else {
			 return false;
		 }
	 }

	 /**
	  * This method parses the list from the command line, ignoring any "options" (-print or -README). This method assumes that there is the correct number
	  * of arguments necessary to save an airline and flight. The method performs validation on any argument with specific formats (see list below). Any validation
	  * errors will be added to an error list and returned to the caller. Arguments are expected in this order and format:
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
	  * @param arguments from command line
	  * @param Airline object
	  * @param Flight object that will be added to the Airline object
	  * @return list of validation errors (as Strings)
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
				boolean valid = validateDate(args[i]);
				if(valid == false) {
					errorList.add("ERROR: Destination date is not in the correct format. (Correct format: MM/DD/YYYY)");
				}
				else {
					flight.setDepartureDate(args[i]);
				}
			}
			else if(i == startingPosition+4) {
				boolean valid = validateTime(args[i]);
				if(valid == false) {
					errorList.add("ERROR: Destination time is not in the correct format. (Correct format: HH:MM)");
				}
				else {
					flight.setDepartureTime(args[i]);
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
				boolean valid = validateDate(args[i]);
				if(valid == false) {
					errorList.add("ERROR: Arrival date is not in the correct format. (Correct format: MM/DD/YYYY)");
				}
				else {
					flight.setArrivalDate(args[i]);
				}
			}
			else if(i == startingPosition+7) {
				boolean valid = validateTime(args[i]);
				if(valid == false) {
					errorList.add("ERROR: Arrival time is not in the correct format. (Correct format: HH:MM)");
				}
				else {
					flight.setArrivalTime(args[i]);
				}
			}
		} 
		airline.addFlight(flight);
		
		return errorList;
	}

	/**
	 * This method validates the time string according to "HH:MM" format. The validation will accept single-digit hours but not single-digit minutes. For instance:
	 * 1:11 is accepted
	 * 12:12 is accepted
	 * 2:2 is not accepted
	 * 2 is not accepted
	 * @param time string
	 * @return true if time matches "HH:MM" format, false if it does not
	 */
	private static boolean validateTime(String time) {
		String[] timeFields = time.split(":");
		int hour;
		int minutes;
		
		if(timeFields.length != 2)
		{
			return false;
		}
		
		//validating the hours (HH)
		try {
    		hour = Integer.valueOf(timeFields[0]);
    		if(hour > 24) {
    			return false;
    		}
    	} catch (NumberFormatException e) {
    		return false;
    	}
		
		//validating the minutes (MM)
		try {
    		minutes = Integer.valueOf(timeFields[1]);
    		if(minutes > 60) {
    			return false;
    		}
    		
    		if(minutes < 10) {
    			//will not accept single-digit minutes. Must be two-digit, such as 02.
    			if(timeFields[1].length() < 2) {
    				return false; 
    			}
    		}
    	} catch (NumberFormatException e) {
    		return false;
    	}
			
		return true;
		
	}

	/**
	 * This method validates the date string according to "MM/DD/YYYY" format. The validation will accept single-digit month and day values but not years less
	 * than 4 digits. The method does not accept month values greater than 12 or day values greater than 31. For example: 
	 * Accepted dates: 1/1/2001; 01/01/2001; 01/1/2001; 1/01/2001
	 * Not accepted dates: 13/1/2001; 1/1/01; 1/32/2001
	 * @param date string
	 * @return true if date string matches MM/DD/YYYY format
	 */
	private static boolean validateDate(String date) {
		
		String[] dateFields = date.split("/");
		int month;
		int day;
		
		if(dateFields.length != 3) {
			return false;
		}
		
		try {
    		month = Integer.valueOf(dateFields[0]);
    	} catch (NumberFormatException e) {
    		return false;
    	}
		
		if(month > 12) {
			return false;
		}
		
		try {
    		day = Integer.valueOf(dateFields[1]);
    	} catch (NumberFormatException e) {
    		return false;
    	}
		
		if(day > 31) {
			return false;
		}
		
		if(dateFields[2].length() != 4) {
			return false;
		}
		
		return true;
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
				+ "-README will print a README for this project and exits without saving the flight (currently viewing README)\n"
				+ "These options can be specified -before- the arguments\n"
				+ "\n----------------END README----------------\n");
	}

}