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
			int number = Integer.valueOf(value);
			flight.setNumber(number);
			success = true;
		}
		
		if(key.equals("Source")) {
			flight.setSource(value);
			success = true;
		}
		
		if(key.equals("Departure time")) {
			String[] dateTime = value.split(" ");
			flight.setDepartureDate(dateTime[0]);
			flight.setDepartureTime(dateTime[1]);
			success = true;
		}
		
		if(key.equals("Destination")) {
			flight.setDestination(value);
			success = true;
		}
		
		if(key.equals("Arrival time")) {
			String[] dateTime = value.split(" ");
			flight.setArrivalDate(dateTime[0]);
			flight.setArrivalTime(dateTime[1]);
			success = true;
		}
		return success;
	}


	
}
