package edu.pdx.cs410J.dbanh;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.pdx.cs410J.AbstractAirline;
import edu.pdx.cs410J.AbstractFlight;
import edu.pdx.cs410J.AirlineDumper;


/**
 * This class takes in a file name (via constructor) and an airline object (via the dump or prettyPrintToSysOut method). The sole 
 * purpose of this class is to write out an airline object and its flights to a file or standard out, prettily.
 * @author Denise
 *
 */
public class PrettyPrinter implements AirlineDumper {
	
	private String fileName;
	
	/**
	 * The constructor takes in the name of the file where the airline/flight data will be saved
	 * @param fileName
	 */
	
	public PrettyPrinter(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * This method will take in an airline object and writes the airline name, and all flights out to a file. 
	 * The format will be:
	 * <BEGINNING OF FILE>
	 * AIRLINE: AIRLINE NAME
	 * ___ flight[s]
	 * 
	 * Flight number: 
	 * Source:
	 * Departure time: 
	 * Destination:
	 * Arrival Time:
	 * Duration:
	 * <END OF FILE>
	 * 
	 * The airline name and flights are each separated by a newline
	 * @param an airline object
	 */
	@Override
	public void dump(AbstractAirline arg0) throws IOException {
		
		BufferedWriter bufferedWriter = null;
		FileWriter fileWriter = null;
		
		
		try{
			fileWriter = new FileWriter(fileName);
			bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write("AIRLINE: ");
			bufferedWriter.write(arg0.getName().toUpperCase());
			bufferedWriter.newLine();

			
			List<AbstractFlight> flights = new ArrayList<AbstractFlight>(arg0.getFlights());
			bufferedWriter.write(flights.size() + " flight");
			if(flights.size() > 1) {
				bufferedWriter.write("s");
			}
			bufferedWriter.newLine();
			bufferedWriter.newLine();
			
			for(AbstractFlight flight: flights) {
				
				bufferedWriter.write("Flight number: ");
				bufferedWriter.write(Integer.toString(flight.getNumber()));
				bufferedWriter.newLine();
				bufferedWriter.write("Source: ");
				bufferedWriter.write(flight.getSource());
				bufferedWriter.newLine();
				bufferedWriter.write("Departure time: ");
				bufferedWriter.write(flight.getDepartureString());
				bufferedWriter.newLine();
				bufferedWriter.write("Destination: ");
				bufferedWriter.write(flight.getDestination());
				bufferedWriter.newLine();
				bufferedWriter.write("Arrival time: ");
				bufferedWriter.write(flight.getArrivalString());
				bufferedWriter.newLine();
				bufferedWriter.write("Duration: ");
				bufferedWriter.write(Integer.toString(calculateDuration(flight.getDeparture(), flight.getArrival())));
				bufferedWriter.write(" minutes");
				bufferedWriter.newLine();
				bufferedWriter.newLine();
			}
		}
		catch(IOException e){
			System.err.println("ERROR: Writing to pretty file failed.");
		}
		finally {
			try {
				bufferedWriter.close();
				fileWriter.close();
			}
			catch(IOException b) {
				System.err.println("ERROR: Problem closing pretty file.");
			}
		}
	
		
	}

	public void prettyPrintToStandardOut(AbstractAirline airline) {
		System.out.println("AIRLINE: " + airline.getName().toUpperCase());
		List<AbstractFlight> flights = new ArrayList<AbstractFlight>(airline.getFlights());
		if(flights.size() > 1) {
			System.out.println(flights.size() + " flights");
		}
		else {
			System.out.println(flights.size() + " flight");
		}
		
		System.out.println();
		
		for(AbstractFlight flight : flights) {
			
			System.out.println("Flight number: " + flight.getNumber());
			System.out.println("Source: " + flight.getSource());
			System.out.println("Departure time: " + flight.getDepartureString());
			System.out.println("Destination: " + flight.getDestination()); 
			System.out.println("Arrival time: " + flight.getArrivalString());
			System.out.println("Duration: " + Integer.toString(calculateDuration(flight.getDeparture(), flight.getArrival())) + " minutes");
			System.out.println();
		}
	}
	
	private int calculateDuration(Date departure, Date arrival) {
		long diffInMillies = arrival.getTime() - departure.getTime();
		return (int) ((diffInMillies/1000)/60);
	}
	
}
