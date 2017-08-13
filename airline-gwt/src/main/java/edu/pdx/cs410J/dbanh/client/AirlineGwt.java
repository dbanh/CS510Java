package edu.pdx.cs410J.dbanh.client;

import com.google.common.annotations.VisibleForTesting;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.UmbrellaException;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import edu.pdx.cs410J.AirportNames;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A basic GWT class that makes sure that we can send an airline back from the server
 */
public class AirlineGwt implements EntryPoint {

  private final Alerter alerter;
  private final AirlineServiceAsync airlineService;
  private final Logger logger;
  static final String DATE_FORMAT_PATTERN = "MM/dd/yyyy hh:mm a";

  @VisibleForTesting
  Button showAirlineButton;
  
  @VisibleForTesting
  Button saveAirlineButton;

  @VisibleForTesting
  Button searchFlightsButton;

  @VisibleForTesting
  Button showDeclaredExceptionButton;

  @VisibleForTesting
  Button showClientSideExceptionButton;
  
  @VisibleForTesting
  TextArea airlinePrettyText = new TextArea();

  public AirlineGwt() {
    this(new Alerter() {
      @Override
      public void alert(String message) {
        Window.alert(message);
      }
    });
  }

  @VisibleForTesting
  AirlineGwt(Alerter alerter) {
    this.alerter = alerter;
    this.airlineService = GWT.create(AirlineService.class);
    this.logger = Logger.getLogger("airline");
    Logger.getLogger("").setLevel(Level.INFO);  // Quiet down the default logging
  }

  private void alertOnException(Throwable throwable) {
    Throwable unwrapped = unwrapUmbrellaException(throwable);
    StringBuilder sb = new StringBuilder();
    sb.append(unwrapped.toString());
    sb.append('\n');

    for (StackTraceElement element : unwrapped.getStackTrace()) {
      sb.append("  at ");
      sb.append(element.toString());
      sb.append('\n');
    }

    this.alerter.alert(sb.toString());
  }
  
  public void alertOnSuccess() {
	  StringBuilder sb = new StringBuilder();
	  sb.append("Flight successfully added");
	  this.alerter.alert(sb.toString());
  }
  
  public void alertOnFailureToSaveAirline() {
	  StringBuilder sb = new StringBuilder();
	  sb.append("Airline requested does not match airline saved on server");
	  this.alerter.alert(sb.toString());
  }
  
  public void alertOnFailureToSearchAirline() {
	  StringBuilder sb = new StringBuilder();
	  sb.append("No airline to search");
	  this.alerter.alert(sb.toString());
  }
  
  public void alertOnFailureToFindFlights() {
	  StringBuilder sb = new StringBuilder();
	  sb.append("No matching flights found");
	  this.alerter.alert(sb.toString());
  }
  
  public void alertOnFailureToSearchFlights() {
	  StringBuilder sb = new StringBuilder();
	  sb.append("Airline requested not found on server");
	  this.alerter.alert(sb.toString());
  }


  private Throwable unwrapUmbrellaException(Throwable throwable) {
    if (throwable instanceof UmbrellaException) {
      UmbrellaException umbrella = (UmbrellaException) throwable;
      if (umbrella.getCauses().size() == 1) {
        return unwrapUmbrellaException(umbrella.getCauses().iterator().next());
      }

    }

    return throwable;
  }

  private void addWidgets(VerticalPanel panel) {
	
	final TextBox airlineName = new TextBox();
	airlineName.setName("airlineName");
	airlineName.getElement().setPropertyString("placeholder", "Airline name");
	
	final TextBox flightNumber = new TextBox();
	flightNumber.setName("flightNumber");
	flightNumber.getElement().setPropertyString("placeholder", "Flight number");
	
	final TextBox source = new TextBox();
	source.setName("source");
	source.getElement().setPropertyString("placeholder", "Departure airport");
	
	final TextBox departure = new TextBox();
	departure.setName("departure");
	departure.getElement().setPropertyString("placeholder", "Departure");
	
	//TODO: maybe turn this into three fields for date
	
	final TextBox destination = new TextBox();
	destination.setName("destination");
	destination.getElement().setPropertyString("placeholder", "Arrival airport");
	
	final TextBox arrival = new TextBox();
	arrival.setName("arrival");
	arrival.getElement().setPropertyString("placeholder", "Arrival");
	
	saveAirlineButton = new Button("Save airline");
	saveAirlineButton.addClickHandler(new ClickHandler() {
		@Override
		public void onClick(ClickEvent arg0) {
			List<String> errorList = validateAirline(airlineName.getText(), flightNumber.getText(), source.getText(), departure.getText(), destination.getText(), arrival.getText());
			
			if(errorList.size() == 0) {
				Airline airline = new Airline();
				airline.setName(airlineName.getText());
				Flight flight = new Flight();
				int flightNum = Integer.parseInt(flightNumber.getText());
				flight.setNumber(flightNum);
				flight.setSource(source.getText());
				flight.setDestination(destination.getText());
				
				Date departureDate = null;
				DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat(DATE_FORMAT_PATTERN);
				try {
					departureDate = dateTimeFormat.parse(departure.getText());
					flight.setDeparture(departureDate);
				} catch (IllegalArgumentException e) {
				}
				flight.setDepartureString(departure.getText());
				
				Date arrivalDate = null;
				try {
					arrivalDate = dateTimeFormat.parse(arrival.getText());
					flight.setArrival(arrivalDate);
				} catch (IllegalArgumentException e) {
				}
				flight.setArrivalString(arrival.getText());
				airline.addFlight(flight);
				saveAirline(airline);
			} else {
				StringBuilder sb = new StringBuilder();
		        for (String error : errorList) {
		          sb.append(error);
		          sb.append("\n");
		        }
		        alerter.alert(sb.toString());
				
			}
		}
	});
	  
    showAirlineButton = new Button("Display all flights for airline");
    showAirlineButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        showAirline();
      }
    });
    
	final TextBox airlineToSearch = new TextBox();
	airlineToSearch.setName("airlineToSearch");
	airlineToSearch.getElement().setPropertyString("placeholder", "Airline name");
	
	final TextBox sourceToSearch = new TextBox();
	sourceToSearch.setName("sourceToSearch");
	sourceToSearch.getElement().setPropertyString("placeholder", "Departure airport");
	
	final TextBox destinationToSearch = new TextBox();
	destinationToSearch.setName("destinationToSearch");
	destinationToSearch.getElement().setPropertyString("placeholder", "Destination airport");

	searchFlightsButton = new Button("Search for flights");
	searchFlightsButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        searchFlights(airlineToSearch.getText(), sourceToSearch.getText(), destinationToSearch.getText());
      }
    });

    showDeclaredExceptionButton = new Button("Show declared exception");
    showDeclaredExceptionButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        showDeclaredException();
      }
    });

    showClientSideExceptionButton= new Button("Show client-side exception");
    showClientSideExceptionButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        throwClientSideException();
      }
    });
    
    airlinePrettyText.setCharacterWidth(80);
    airlinePrettyText.setVisibleLines(20);
    

    panel.add(airlineName);
    panel.add(flightNumber);
    panel.add(source);
    panel.add(departure);
    panel.add(destination);
    panel.add(arrival);
    panel.add(saveAirlineButton);
    panel.add(showAirlineButton);
    panel.add(airlineToSearch);
    panel.add(sourceToSearch);
    panel.add(destinationToSearch);
    panel.add(searchFlightsButton);
    panel.add(airlinePrettyText);
    panel.add(showClientSideExceptionButton);
  }

  private void throwClientSideException() {
    logger.info("About to throw a client-side exception");
    throw new IllegalStateException("Expected exception on the client side");
  }

  private void showUndeclaredException() {
    logger.info("Calling throwUndeclaredException");
    airlineService.throwUndeclaredException(new AsyncCallback<Void>() {
      @Override
      public void onFailure(Throwable ex) {
        alertOnException(ex);
      }

      @Override
      public void onSuccess(Void aVoid) {
        alerter.alert("This shouldn't happen");
      }
    });
  }

  private void showDeclaredException() {
    logger.info("Calling throwDeclaredException");
    airlineService.throwDeclaredException(new AsyncCallback<Void>() {
      @Override
      public void onFailure(Throwable ex) {
        alertOnException(ex);
      }

      @Override
      public void onSuccess(Void aVoid) {
        alerter.alert("This shouldn't happen");
      }
    });
  }

  private void showAirline() {
    logger.info("Calling getAirline");
    airlineService.getAirline(new AsyncCallback<Airline>() {

      @Override
      public void onFailure(Throwable ex) {
        alertOnException(ex);
      }

      @Override
      public void onSuccess(Airline airline) {
        if (airline != null) {
			StringBuilder sb = new StringBuilder(airline.toString());
			sb.append("\n");
			Collection<Flight> flights = airline.getFlights();
			for (Flight flight : flights) {
				sb.append(flight);
				sb.append("\n");
			}
			alerter.alert(sb.toString());
		} else {
			alerter.alert("No airline to display");
		}
      }
    });
  }
  
  private void saveAirline(Airline airline) {
	  logger.info("Calling saveAirline");
	  airlineService.saveAirline(airline, new AsyncCallback<Airline>() {

		@Override
		public void onFailure(Throwable arg0) {
			alertOnFailureToSaveAirline();
		}

		@Override
		public void onSuccess(Airline arg0) {
			alertOnSuccess();
		}
	});
  }
  
  private void searchFlights(String airline, String src, String dest) {
	  logger.info("Calling searchFlights");
	  airlineService.searchFlights(airline, src, dest, new AsyncCallback<Airline>() {

		@Override
		public void onFailure(Throwable t) {
			if(t instanceof IllegalArgumentException) {
				alertOnFailureToSearchFlights();
			} else {
				alertOnFailureToSearchAirline();
			}
		}

		@Override
		public void onSuccess(Airline airline) {
			if(airline != null) {
				prettyPrintAirline(airline, airlinePrettyText);
			} else {
				alertOnFailureToFindFlights();
			}
		}
	
	  });
  }
  
  private void prettyPrintAirline(Airline airline, TextArea airlinePrettyText) {
	    PrettyPrinter pretty = new PrettyPrinter();
	    try {
	      pretty.dump(airline);

	    } catch (IOException e) {
	      alertOnException(e);
	    }

	    airlinePrettyText.setText(pretty.getPrettyText());
  }
  
  private List<String> validateAirline(String airlineName, String flightNumber, String src, String departure, String destination, String arrival) {
	  
		List<String> errorList = new ArrayList<String>();
		
		if(airlineName.isEmpty()) {
			errorList.add("ERROR: Airline name required");
		}
		
		if(!flightNumber.isEmpty()) {
	    	try {
	    		int number = Integer.valueOf(flightNumber);
	    	} catch (NumberFormatException e) {
	    		errorList.add("ERROR: Flight number is non-numeric");
	    	} 
		} else {
			errorList.add("ERROR: Flight number is required");
		}
		
		if(!src.isEmpty()) {
			if(!validateAirportCode(src)) {
				errorList.add("ERROR: Source airport code is not valid. Valid codes are 3 characters long, may only contains letters, and must be a real code.");
			}
		} else {
			errorList.add("ERROR: Departure airport is required");
		}
		
		if(!departure.isEmpty()) {
//			boolean validDate = validateDate(args[i]);
//			boolean validTime = validateTime(args[i+1]);
//			boolean validAmPm = validateAmPm(args[i+2]);
//			
//			if(validDate == false) {
//				errorList.add("ERROR: Departure date is not in the correct format. (Correct format: MM/DD/YYYY)");
//			}
//			if(validTime == false || validAmPm == false) {
//				errorList.add("ERROR: Departure time is not in the correct format. (Correct format: HH:MM AM/PM)");
//			}
			
//			else {
//				String sb = new StringBuilder(args[i]).append(" ").append(args[i+1]).append(" ").append(args[i+2]).toString();
				Date date = null;
				DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat(DATE_FORMAT_PATTERN);
				try {
					date = dateTimeFormat.parse(departure);
				} catch (IllegalArgumentException e) {
					errorList.add("ERROR: Depature date/time is not in the correct format. (Correct format: MM/DD/YYYY HH:MM AM/PM)");
				}
		} else {
			errorList.add("ERROR: Departure date/time is required");
		}
		
		if(!destination.isEmpty()) {
			if(!validateAirportCode(destination)) {
				errorList.add("ERROR: Destination airport code is not valid. Valid codes are 3 characters long, may only contains letters, and must be a real code.");
			}
		} else {
			errorList.add("ERROR: Destination airport required");
		}
		
		if(!arrival.isEmpty()) {
			Date date = null;
			DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat(DATE_FORMAT_PATTERN);
			try {
				date = dateTimeFormat.parse(arrival);
			} catch (IllegalArgumentException e) {
				errorList.add("ERROR: Arrival date/time is not in the correct format. (Correct format: MM/DD/YYYY HH:MM AM/PM)");
			}
		} else {
			errorList.add("ERROR: Arrival date/time is required");
		}
		
		return errorList;  
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

  @Override
  public void onModuleLoad() {
    setUpUncaughtExceptionHandler();

    // The UncaughtExceptionHandler won't catch exceptions during module load
    // So, you have to set up the UI after module load...
    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
      @Override
      public void execute() {
        setupUI();
      }
    });

  }

  private void setupUI() {
    RootPanel rootPanel = RootPanel.get();
    VerticalPanel panel = new VerticalPanel();
    rootPanel.add(panel);

    addWidgets(panel);
  }

  private void setUpUncaughtExceptionHandler() {
    GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
      @Override
      public void onUncaughtException(Throwable throwable) {
        alertOnException(throwable);
      }
    });
  }

  @VisibleForTesting
  interface Alerter {
    void alert(String message);
  }
}
