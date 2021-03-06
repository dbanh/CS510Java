package edu.pdx.cs410J.dbanh;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
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
	
	public PrettyPrinter() {}

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
	
	public void prettyPrintToWeb(AbstractAirline airline, PrintWriter pw ){
		
		List<Flight> listFlightsForAirline = new ArrayList<Flight>();
		
		Iterator<AbstractFlight> iterator = airline.getFlights().iterator();
		while(iterator.hasNext()) {
			AbstractFlight airlineFlight = iterator.next();
			listFlightsForAirline.add((Flight) airlineFlight);
		}	
		Collections.sort(listFlightsForAirline);
		
		List<Flight> curatedFlights = removeDuplicates(listFlightsForAirline);
		

		pw.println("AIRLINE: " + airline.getName().toUpperCase());
		if(curatedFlights.size() > 1) {
			pw.println(curatedFlights.size() + " flights");
		}
		else {
			pw.println(curatedFlights.size() + " flight");
		}
		
		pw.println();
		
		for(AbstractFlight flight : curatedFlights) {
			
			pw.println("Flight number: " + flight.getNumber());
			pw.println("Source: " + flight.getSource());
			pw.println("Departure time: " + flight.getDepartureString());
			pw.println("Destination: " + flight.getDestination()); 
			pw.println("Arrival time: " + flight.getArrivalString());
			pw.println("Duration: " + Integer.toString(calculateDuration(flight.getDeparture(), flight.getArrival())) + " minutes");
			pw.println();
		}
		
	    pw.flush();
	}
	
	/**
	 * Removes duplicate flights in an airline. Compares the source, departure time, destination, and arrival time. 
	 * @param airline object
	 * @return airline object with duplicate flights removed
	 */
	private static List<Flight> removeDuplicates(List<Flight> flights) {
		
		for(int i = 0; i < flights.size()-1; ++i) {
			if(flights.get(i).getSource().equals(flights.get(i+1).getSource()) && 
					flights.get(i).getDepartureString().equals(flights.get(i+1).getDepartureString()) && 
					flights.get(i).getDestination().equals(flights.get(i+1).getDestination()) && 
					flights.get(i).getArrivalString().equals(flights.get(i+1).getArrivalString())) {
				flights.remove(i+1);
				--i;
			}
		}

		return flights;
	}
	
	private int calculateDuration(Date departure, Date arrival) {
		long diffInMillies = arrival.getTime() - departure.getTime();
		return (int) ((diffInMillies/1000)/60);
	}
	
}
