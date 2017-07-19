package edu.pdx.cs410J.dbanh;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.pdx.cs410J.AbstractAirline;
import edu.pdx.cs410J.AbstractFlight;
import edu.pdx.cs410J.AirlineDumper;
import edu.pdx.cs410J.AirlineParser;
import edu.pdx.cs410J.ParserException;

/**
 * This class takes in a file name (via constructor) where the class should read in. The sole 
 * purpose of this class is to read airline and flight information from a file and save it into an airline object. 
 * @author Denise
 *
 */
public class TextParser implements AirlineParser {
	
	private String fileName;
	
	/**
	 * Constructor for the TextParser class. It takes in the name of the file the parser will parse from
	 * @param fileName
	 */
	public TextParser(String fileName) {
		this.fileName = fileName;
	}


	/**
	 * This method will read in data from a file and save it into an airline object. The file is read in line by line,
	 * and the file should be in this format:
	 * <BEGINNING OF FILE>
	 * AIRLINE NAME
	 * 
	 * Flight number - 
	 * Source - 
	 * Departure time - 
	 * Destination - 
	 * Arrival Time -
	 * <END OF FILE>
	 * 
	 * The airline name and flights are each separated by a newline
	 * @return airline object
	 */
	@Override
	public AbstractAirline parse() throws ParserException {
		
		AbstractAirline airline = null;
		Flight flight = new Flight();
		String airlineName;
		String line; //generic line for reading in
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		boolean successLineSave;
		
		try {
			fileReader = new FileReader(fileName);
			bufferedReader = new BufferedReader(fileReader);		
			
			//read in the airline name
			airlineName = bufferedReader.readLine().trim();
			line = bufferedReader.readLine().trim(); //account for empty line after airline name in text file
			
			if(!airlineName.isEmpty()){
				airline = new Airline(airlineName);
				
				line = bufferedReader.readLine().trim();
				

				while (line != null) {
					
					//assume that the file is properly formatted
					//this chunk should represent a flight
					while(!line.isEmpty()) {
						String[] lineData = line.split("-");
						successLineSave = saveLine(flight, lineData[0].trim(), lineData[1].trim());
						if (!successLineSave) {
							System.err.println("ERROR: Problem reading from " + fileName);
						}
						line = bufferedReader.readLine().trim();
					}
					
					airline.addFlight(flight);
					flight = new Flight();
					if(line != null){
						line = bufferedReader.readLine(); //account for empty line after each flight
					}
				}
				
			}
			
		} catch (FileNotFoundException e) {
			System.err.println("ERROR: " + fileName + " not found.");
			return null;
		} catch (IOException e) {
			System.err.println("ERROR: Problem reading from " + fileName);
			return null;
		} catch (Exception e) {
			System.err.println("ERROR: Problem reading from " + fileName);
			return null;
		} finally {
			try {
				bufferedReader.close();
				fileReader.close();
			}
			catch(IOException b) {
				System.err.println("ERROR: Problem closing file.");
			}
		}
		
		return airline;
	}

	
	/**
	 * This method will take in a flight object to save data, a key (the label for the flight data such as "Flight number")
	 * and the data itself. It will check to see what the key is and save the flight data into the proper field.
	 * @param flight object
	 * @param key 
	 * @param value
	 * @return returns true if the line was successfully saved, false otherwise
	 */
	private boolean saveLine(Flight flight, String key, String value) {
		boolean success = false;
		
		if(key.equals("Flight number")) {
			try {
				int number = Integer.valueOf(value);
				flight.setNumber(number);
			} catch (NumberFormatException e) {
				return false;
			}
			success = true;
		}
		
		if(key.equals("Source")) {
			if(validateAirportCode(value)) {
				flight.setSource(value);
				success = true;
			}
			else {
				return false;
			}
		}
		
		if(key.equals("Departure time")) {
			String[] dateTime = value.split(" ");
			if(validateDate(dateTime[0]) && validateTime(dateTime[1])) {
				flight.setDepartureDate(dateTime[0]);
				flight.setDepartureTime(dateTime[1]);
				success = true;
			}
			else {
				return false;
			}
		}
		
		if(key.equals("Destination")) {
			if (validateAirportCode(value)) {
				flight.setDestination(value);
				success = true;
			}
			else {
				return false;
			}
		}
		
		if(key.equals("Arrival time")) {
			String[] dateTime = value.split(" ");
			if(validateDate(dateTime[0]) && validateTime(dateTime[1])) {
				flight.setArrivalDate(dateTime[0]);
				flight.setArrivalTime(dateTime[1]);
				success = true;
			}
			else {
				return false;
			}
		}
		return success;
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
		return true; 
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

	
}
