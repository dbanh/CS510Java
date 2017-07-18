package edu.pdx.cs410J.dbanh;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.pdx.cs410J.AbstractAirline;
import edu.pdx.cs410J.AbstractFlight;
import edu.pdx.cs410J.AirlineDumper;

public class TextDumper implements AirlineDumper {
	
	private String fileName;
	
	public TextDumper(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public void dump(AbstractAirline arg0) throws IOException {
		
		BufferedWriter bufferedWriter = null;
		FileWriter fileWriter = null;
		
		
		try{
			fileWriter = new FileWriter(fileName);
			bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(arg0.getName().toUpperCase());
			bufferedWriter.newLine();
			bufferedWriter.newLine();
			
			List<AbstractFlight> flights = new ArrayList<AbstractFlight>(arg0.getFlights());
			for(AbstractFlight flight: flights) {
				bufferedWriter.write("Flight number - ");
				bufferedWriter.write(Integer.toString(flight.getNumber()));
				bufferedWriter.newLine();
				bufferedWriter.write("Source - ");
				bufferedWriter.write(flight.getSource());
				bufferedWriter.newLine();
				bufferedWriter.write("Departure time - ");
				bufferedWriter.write(flight.getDepartureString());
				bufferedWriter.newLine();
				bufferedWriter.write("Destination - ");
				bufferedWriter.write(flight.getDestination());
				bufferedWriter.newLine();
				bufferedWriter.write("Arrival time - ");
				bufferedWriter.write(flight.getArrivalString());
				bufferedWriter.newLine();
				bufferedWriter.newLine();
			}
			
			System.out.println("SUCCESS: Wrote airline out to file.");
		}
		catch(IOException e){
			System.err.println("ERROR: Writing to file failed.");
		}
		finally {
			try {
				bufferedWriter.close();
				fileWriter.close();
			}
			catch(IOException b) {
				System.err.println("ERROR: Problem closing file.");
			}
		}
	
		
	}
	
}
