package edu.pdx.cs410J.dbanh;

import edu.pdx.cs410J.AbstractFlight;
import edu.pdx.cs410J.AirportNames;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This servlet ultimately provides a REST API for working with an airline and flights. The servlet will post an airline and flight as well
 * as get all flights that match the provided source and destination airport. 
 */
public class AirlineServlet extends HttpServlet {
//  private final Map<String, String> data = new HashMap<>();
  private Airline airline;
//  final static org.slf4j.Logger logger = LoggerFactory.getLogger(AirlineServlet.class);
  
  public void init(ServletConfig servletConfig) throws ServletException {
	    airline = new Airline();
  }

  /**
   * Handles an HTTP GET request from a client returning all of the flights or by searching for flights that
   * depart from the src airport and arrives at the dest airport.
   */
  @Override
  protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
  {
      response.setContentType( "text/plain" );

      String uri = request.getRequestURI();
      String lastPart = uri.substring(uri.lastIndexOf('/') + 1, uri.length());

      if (lastPart.equals("flights")) {

    	if(airline.getName() == null) {
    		response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No saved airline to display");
    		return;
    	}
  		String airlineName = getParameter("name", request);
  		String src = getParameter("src", request);
  		String dest = getParameter("dest", request);
  		
  		if(airlineName == null && src == null && dest == null) {
  			writeFlights(response);
  		}
        
        else if(airlineName != null && src != null && dest != null){
  			searchForFlights(airlineName, src, dest, response);
  		} 
        
        else {
  			response.sendError(HttpServletResponse.SC_BAD_REQUEST,
  					"Missing search parameters. Airline name, source airport, & destination airport required.");
  			return;
  		}  
      }
      
      response.setStatus(HttpServletResponse.SC_OK);
  }


/**
   * Handles an HTTP POST request by storing the airline and flight
   */
  @Override
  protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
  {
      response.setContentType( "text/plain" );
      createAirline(request, response);
      
      response.setStatus(HttpServletResponse.SC_OK);
  }

  /**
   * Handles an HTTP DELETE request by removing all key/value pairs.  This
   * behavior is exposed for testing purposes only.  It's probably not
   * something that you'd want a real application to expose.
   */
  @Override
  protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      response.setContentType("text/plain");

      airline = null;

      PrintWriter pw = response.getWriter();
      pw.println(Messages.allMappingsDeleted());
      pw.flush();

      response.setStatus(HttpServletResponse.SC_OK);

  }

  /**
   * Returns the value of the HTTP request parameter with the given name.
   *
   * @return <code>null</code> if the value of the parameter is
   *         <code>null</code> or is the empty string
   */
  private String getParameter(String name, HttpServletRequest request) {
    String value = request.getParameter(name);
    if (value == null || "".equals(value)) {
      return null;

    } else {
      return value;
    }
  }
  
  /**
   * This method checks the parameters of the service call to make sure the parameters are valid
   * @param airline
   * @param flightNumber
   * @param src
   * @param departTime
   * @param dest
   * @param arriveTime
   * @param response
   * @return
   * @throws IOException
   */
  private boolean checkParameters(String airline, String flightNumber, String src, String departTime, String dest, String arriveTime, HttpServletResponse response) throws IOException {
	  boolean allParameters = true;
	  if(airline == null) {
	      response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing airline name");
	      allParameters =  false;
	  }
	  if(flightNumber == null) {
		  response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing flight number");
		  allParameters =  false;
	  }
	  if(src == null) {
		  response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing airport source");
		  allParameters =  false;
	  }
	  if(departTime == null) {
		  response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing departure time");
		  allParameters =  false;
	  }
	  if(dest == null) {
		  response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing airport destination");
		  allParameters =  false;
	  }
	  if(arriveTime == null) {
		  response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing arrival time");
		  allParameters =  false;
	  }
	  
	  return allParameters;
  }
  
  /**
   * Creates an airline if an airline does not already exists and adds flights to it
   * @param request
   * @param response
   * @throws IOException
   */
  private void createAirline(HttpServletRequest request, HttpServletResponse response) throws IOException {
	    String airlineName = getParameter("name", request);
    	String flightNumber = getParameter("flightNumber", request);
    	String src = getParameter("src", request);
    	String departTime = getParameter("departTime", request);
    	String dest = getParameter("dest", request);
    	String arriveTime = getParameter("arriveTime", request);
    	
    	//If there are issues with the parameters
    	if(!checkParameters(airlineName, flightNumber, src, departTime, dest, arriveTime, response)) {
  	      return;
    	}

	   	    
	    if((airline.getName() != null) 
	    		&& (!airline.getName().equals(airlineName))) {
		      response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Airline name does not match saved airline. Saved airline: " + airline.getName() + "  Requested airline: " + airlineName);
		      return;
	    }

	    airline.setName(airlineName);

    	Flight flight = new Flight();;

 	    try {
 	    	flight.setNumber(Integer.parseInt(flightNumber));
 	    } catch(NumberFormatException ex) {
 	        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad flight number: " + flightNumber);
 	        return;
 	    }
 	    
 	    if(validateAirportCode(src)) {
 	    	flight.setSource(src);
 	    } else {
 	        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad source: " + src);
 	        return;
 	    }
 	    
 	    String departure = validateDateTime(departTime);

 	    if(departure != null) {
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
			Date date;
			try {
				date = formatter.parse(departTime);
				flight.setDeparture(date);
			} catch (ParseException e) {
	 	        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad departure time: " + departTime);
	 	        return;
			}
 	    } else {
 	        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad departure time: " + departTime);
 	        return;
 	    }
 	    
 	   if(validateAirportCode(dest)) {
 	    	flight.setDestination(dest);
 	    } else {
 	        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad source: " + dest);
 	        return;
 	    }
 	   
 	    String arrival = validateDateTime(arriveTime); 

 	    if(arrival != null) {
			SimpleDateFormat formatterArrival = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
			Date dateArrival;
			try {
				dateArrival = formatterArrival.parse(arriveTime);
				flight.setArrival(dateArrival);
			} catch (ParseException e) {
	 	        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad departure time: " + arriveTime);
	 	        return;
			}
 	    } else {
 	        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad departure time: " + arriveTime);
 	        return;
 	    }

 	    airline.addFlight(flight);

	}
  
  /**
   * This method validates the date and time according to MM/dd/yyyy hh:mm a and returns a string with the formatted date time
   * @param dateTime
   * @return
   */
  	private static String validateDateTime(String dateTime) {

  		String[] dateTimeSplit = dateTime.split(" ");
 	    boolean validDate = validateDate(dateTimeSplit[0]);
		boolean validTime = validateTime(dateTimeSplit[1]);
		boolean validAmPm = validateAmPm(dateTimeSplit[2]);
		String sb;
		
		if(validDate && validTime && validAmPm) {
			sb = new StringBuilder(dateTimeSplit[0]).append(" ").append(dateTimeSplit[1]).append(" ").append(dateTimeSplit[2]).toString();
		}
		else {
			return null;
		}
		return sb;
  	}
  
	/**
	 * This methods checks to see is the argument following time is correctly "am" or "pm"
	 * @param amPm
	 * @return true if "am" or "pm", false otherwise
	 */
	private static boolean validateAmPm(String amPm) {
		if(amPm.toLowerCase().equals("am") || amPm.toLowerCase().equals("pm")) {
			return true;
		}
		return false;
	}


	/**
	 * This method checks to see if the airport code entered is a valid code. It is valid if it is exactly 3 characters long and only contains letters.
	 * This method does not check that the airport code is a "real" airport code. 
	 * @param airportCode
	 * @return true if the airport code passed in is valid, false otherwise
	 */
	private static boolean validateAirportCode(String airportCode) {
		if(airportCode.length() != 3) {
			return false;
		}
		if(airportCode.matches(".*\\d+.*")) {
			return false;
		}
		if(AirportNames.getName(airportCode.toUpperCase()) != null) {
			return true;
		}
		return false; 
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
  		if(hour > 12) {
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
		
		if(dateFields[2].length() != 4 && dateFields[2].length() != 2) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * writes all flights prettily
	 * @param response
	 * @throws IOException
	 */
	 private void writeFlights(HttpServletResponse response) throws IOException {
	      PrintWriter pw = response.getWriter();
	      PrettyPrinter prettyPrinter = new PrettyPrinter();
	      prettyPrinter.prettyPrintToWeb(airline, pw);

	      response.setStatus( HttpServletResponse.SC_OK );
	 }
	 

	 /**
	  * Searches for flights based on the airport name, source airport, and destination airport. The method will also pretty print the flights.
	  * @param airlineName
	  * @param src
	  * @param dest
	  * @param response
	  * @throws IOException
	  */
	  private void searchForFlights(String airlineName, String src, String dest, HttpServletResponse response) throws IOException {
		
		  if(airline.getName() == null) {
		      response.sendError(HttpServletResponse.SC_BAD_REQUEST, "There is no saved airline.");
		      return;
		  }
		  if (airline.getName().equals(airlineName)) {
			List<AbstractFlight> flights = new ArrayList<AbstractFlight>();
			flights = (List<AbstractFlight>) airline.getFlights();
			List<AbstractFlight> matchingFlights = new ArrayList<AbstractFlight>();
			for (AbstractFlight flight : flights) {
				if (flight.getSource().toUpperCase().equals(src.toUpperCase()) && flight.getDestination().toUpperCase().equals(dest.toUpperCase())) {
					matchingFlights.add(flight);
				}
			}
			if (matchingFlights.size() > 0) {
				PrintWriter pw = response.getWriter();
				PrettyPrinter prettyPrinter = new PrettyPrinter();
				Airline matchingAirlineFlights = new Airline();
				matchingAirlineFlights.setName(airlineName);
				matchingAirlineFlights.setAbstractFlights(matchingFlights);

				prettyPrinter.prettyPrintToWeb(matchingAirlineFlights, pw);

				response.setStatus(HttpServletResponse.SC_OK);
			} else {
			      response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Found no flights that match parameters. Source: " + src + "  Destination: " + dest);
			      return;
			}
		  }
		  else {
		      response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Saved airline does not match airline in search request. Saved airline: " + airline.getName() + "  Searching for: +" + airlineName);
		      return;
		  }
		
	  }
}
