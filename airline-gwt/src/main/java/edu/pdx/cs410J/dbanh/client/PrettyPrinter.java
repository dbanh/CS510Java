package edu.pdx.cs410J.dbanh.client;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.i18n.client.DateTimeFormat;

import edu.pdx.cs410J.AbstractAirline;
import edu.pdx.cs410J.AbstractFlight;
import edu.pdx.cs410J.AirlineDumper;


/**
 * This class takes in a file name (via constructor) and an airline object (via the dump or prettyPrintToSysOut method). The sole 
 * purpose of this class is to write out an airline object and its flights to a file or standard out, prettily.
 * @author Denise
 *
 */
public class PrettyPrinter /*implements AirlineDumper<Airline>*/ {
	
	private StringBuilder prettyText = new StringBuilder();

	public PrettyPrinter() {}

//	@Override
	public void dump(Airline airline) throws IOException {
		
		List<Flight> listFlightsForAirline = new ArrayList<Flight>();
		
		Iterator<Flight> iterator = airline.getFlights().iterator();
		while(iterator.hasNext()) {
			AbstractFlight airlineFlight = iterator.next();
			listFlightsForAirline.add((Flight) airlineFlight);
		}	
		Collections.sort(listFlightsForAirline);
		
		Collections.sort(listFlightsForAirline, new Comparator<Flight>() {

	        public int compare(Flight flight1, Flight flight2) {
	        	 int result = flight1.getSource().compareTo(flight2.getSource());
	         	  if(result != 0) {
	         		  return result;
	         	  }
	       	  
	       	  return flight1.getDeparture().compareTo(flight2.getDeparture());
	        }
	});
		
		List<Flight> curatedFlights = removeDuplicates(listFlightsForAirline);
		

		prettyText.append("AIRLINE: " + airline.getName().toUpperCase());
		prettyText.append("\n");
		if(curatedFlights.size() > 1) {
			prettyText.append(curatedFlights.size() + " flights");
		}
		else {
			prettyText.append(curatedFlights.size() + " flight");
		}
		
		prettyText.append("\n\n");
		
		for(Flight flight : curatedFlights) {
			
			prettyText.append("Flight number: " + flight.getNumber() + "\n");
			prettyText.append("Source: " + flight.getSource() + "\n");
			prettyText.append("Departure time: " + flight.getDepartureString() + "\n");
			prettyText.append("Destination: " + flight.getDestination() + "\n"); 
			prettyText.append("Arrival time: " + flight.getArrivalString() + "\n");
			prettyText.append("Duration: " + calculateDuration(flight.getDeparture(), flight.getArrival()) + " minutes\n");
			prettyText.append("\n");
		}
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
	
	public String getPrettyText() {
		return prettyText.toString();
	}
	
}
