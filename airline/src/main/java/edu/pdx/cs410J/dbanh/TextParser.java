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

public class TextParser implements AirlineParser {
	
	private String fileName;
	
	public TextParser(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public AbstractAirline parse() throws ParserException {
		
		AbstractAirline airline = null;
		Flight flight = new Flight();
		String airlineName;
		String line; //generic line for reading in
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		
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
						saveLine(flight, lineData[0].trim(), lineData[1].trim());
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
		} catch (IOException e) {
			System.err.println("ERROR: Problem reading from " + fileName);
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

	private void saveLine(Flight flight, String key, String value) {
		
		if(key.equals("Flight number")) {
			int number = Integer.valueOf(value);
			flight.setNumber(number);
		}
		
		if(key.equals("Source")) {
			flight.setSource(value);
		}
		
		if(key.equals("Departure time")) {
			String[] dateTime = value.split(" ");
			flight.setDepartureDate(dateTime[0]);
			flight.setDepartureTime(dateTime[1]);
		}
		
		if(key.equals("Destination")) {
			flight.setDestination(value);
		}
		
		if(key.equals("Arrival time")) {
			String[] dateTime = value.split(" ");
			flight.setArrivalDate(dateTime[0]);
			flight.setArrivalTime(dateTime[1]);
		}
	}


	
}
