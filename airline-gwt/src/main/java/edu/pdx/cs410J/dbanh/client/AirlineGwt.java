package edu.pdx.cs410J.dbanh.client;

import com.google.common.annotations.VisibleForTesting;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.UmbrellaException;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DatePicker;

import edu.pdx.cs410J.AirportNames;

import java.io.IOException;
import java.util.ArrayList;
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
  Button helpMeButton;
  
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
  

  private void readMe() {
	  StringBuilder sb = new StringBuilder();

	  sb.append("This application allows the user to keep track of flights for an airline.\n"
	  		+ "The user can add flights to the server for an airline, search the server\n"
	  		+ "for flights, and view all flights for an airline. This application can only\n"
	  		+ "keep track of flights for one airline for each session.\n"
	  		+ "\nFlights can be added by entering the:\n"
	  		+ "1) Airline name\n2) Flight number\n3) Departure airport\n4) Arrival airport\n"
	  		+ "5) Departure time (hh:mm am/pm)\n6) Arrival time (hh:mm am/pm)\n7) Departure date\n8) Arrival date\n"
	  		+ "\nFlights can be searched by entering the:\n"
	  		+ "1) Airline name\n2) Departure airport\n3) Arrival airport\n"
	  		+ "\nFlights are sorted alphabetically by their departure airport followed by\n"
	  		+ "the departure time. Duplicate flights are also removed.");
	  
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
  
  private void addRightWidgets(VerticalPanel panel) {
	    airlinePrettyText.setCharacterWidth(60);
	    airlinePrettyText.setVisibleLines(30);
	    
	    Label textAreaLabel = new Label("Your results will display here:");
	    textAreaLabel.getElement().getStyle().setFontWeight(FontWeight.BOLD);
	    HorizontalPanel helpMePanel = new HorizontalPanel();
	    
	    helpMeButton = new Button("HELP! Click here for README");
	    helpMeButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				readMe();
			}
	    	
	    });
	    helpMePanel.add(helpMeButton);
	    helpMePanel.getElement().getStyle().setPaddingLeft(250, Unit.PX);
	    helpMePanel.getElement().getStyle().setPaddingTop(75, Unit.PX);
	    
	    panel.add(textAreaLabel);
	    panel.add(airlinePrettyText);
	    panel.add(helpMePanel);
  }

  private void addWidgets(VerticalPanel panel) {
	
	Label addFlight = new Label("ADD FLIGHT");
	addFlight.getElement().getStyle().setFontWeight(FontWeight.BOLD);
	addFlight.getElement().getStyle().setFontSize(14, Unit.PX);
	addFlight.getElement().getStyle().setPaddingBottom(10, Unit.PX);
	
	HorizontalPanel firstRow = new HorizontalPanel();
	final TextBox airlineName = new TextBox();
	airlineName.setName("airlineName");
	airlineName.getElement().setPropertyString("placeholder", "ex: Alaska");
	airlineName.getElement().getStyle().setPaddingRight(15, Unit.PX);
	Label airlineSectionLabel = new Label("Airline name: ");
	airlineSectionLabel.getElement().getStyle().setPaddingRight(3, Unit.PX);
	
	final TextBox flightNumber = new TextBox();
	flightNumber.setName("flightNumber");
	flightNumber.getElement().setPropertyString("placeholder", "ex: 42");
	Label flightNumberSectionLabel = new Label("Flight number: ");
	flightNumberSectionLabel.getElement().getStyle().setPaddingLeft(35, Unit.PX);
	firstRow.add(airlineSectionLabel);
	firstRow.add(airlineName);
	firstRow.getElement().getStyle().setPaddingLeft(10, Unit.PX);
	firstRow.getElement().getStyle().setPaddingBottom(2, Unit.PX);
	firstRow.getElement().getStyle().setPaddingRight(3, Unit.PX);
	firstRow.add(flightNumberSectionLabel);
	firstRow.add(flightNumber);
	
	HorizontalPanel secondRow = new HorizontalPanel();
	final TextBox source = new TextBox();
	source.setName("source");
	source.getElement().setPropertyString("placeholder", "ex: PDX");
	Label sourceSectionLabel = new Label("Departure airport: ");
	sourceSectionLabel.getElement().getStyle().setPaddingRight(3, Unit.PX);
	
	final TextBox destination = new TextBox();
	destination.setName("destination");
	destination.getElement().setPropertyString("placeholder", "ex: LAX");
	Label destinationSectionLabel = new Label("Arrival airport: ");
	destinationSectionLabel.getElement().getStyle().setPaddingLeft(20, Unit.PX);
	destinationSectionLabel.getElement().getStyle().setPaddingRight(3, Unit.PX);
	secondRow.add(sourceSectionLabel);
	secondRow.add(source);
	secondRow.add(destinationSectionLabel);
	secondRow.add(destination);
	secondRow.getElement().getStyle().setPaddingLeft(10, Unit.PX);
	
	HorizontalPanel thirdRow = new HorizontalPanel();
	final TextBox departureTimeHour = new TextBox();
	departureTimeHour.setName("departureTimeHour");
	departureTimeHour.getElement().setPropertyString("placeholder", "ex: 10");
	departureTimeHour.getElement().getStyle().setPaddingRight(3, Unit.PX);
	departureTimeHour.setVisibleLength(5);
	final TextBox departureTimeMinute = new TextBox();
	departureTimeMinute.setName("departureTimeMinute");
	departureTimeMinute.getElement().setPropertyString("placeholder", "ex: 30");
	departureTimeMinute.setVisibleLength(5);
	Label departureTimeLabel = new Label("Departure time: ");
	departureTimeLabel.getElement().getStyle().setPaddingLeft(10, Unit.PX);
	Label colon1 = new Label(":");
	colon1.getElement().getStyle().setFontWeight(FontWeight.BOLD);
	Label colon2 = new Label(":");
	colon2.getElement().getStyle().setFontWeight(FontWeight.BOLD);
	
	final TextBox arrivalTimeHour = new TextBox();
	arrivalTimeHour.setName("arrivalTimeHour");
	arrivalTimeHour.getElement().setPropertyString("placeholder", "ex: 10");
	arrivalTimeHour.getElement().getStyle().setPaddingRight(3, Unit.PX);
	arrivalTimeHour.setVisibleLength(5);
	final TextBox arrivalTimeMinute = new TextBox();
	arrivalTimeMinute.setName("departureTimeMinute");
	arrivalTimeMinute.getElement().setPropertyString("placeholder", "ex: 30");
	arrivalTimeMinute.setVisibleLength(5);
	Label arrivalTimeLabel = new Label("Arrival time: ");
	arrivalTimeLabel.getElement().getStyle().setPaddingLeft(34, Unit.PX);
	
	final ListBox amPmDeparture = new ListBox();
	amPmDeparture.addItem("AM");
	amPmDeparture.addItem("PM");
	
	final ListBox amPmArrival = new ListBox();
	amPmArrival.addItem("AM");
	amPmArrival.addItem("PM");
	
	thirdRow.add(departureTimeLabel);
	thirdRow.add(departureTimeHour);
	thirdRow.add(colon1);
	thirdRow.add(departureTimeMinute);
	thirdRow.add(amPmDeparture);
	thirdRow.add(arrivalTimeLabel);
	thirdRow.add(arrivalTimeHour);
	thirdRow.add(colon2);
	thirdRow.add(arrivalTimeMinute);
	thirdRow.add(amPmArrival);
	thirdRow.getElement().getStyle().setPaddingBottom(2, Unit.PX);
	
	HorizontalPanel fourthRow = new HorizontalPanel();
	final DatePicker departure = new DatePicker(); 
	departure.setCurrentMonth(new Date());
	final Label departureDateFromUI = new Label();
	departure.addValueChangeHandler(new ValueChangeHandler<Date>(){
		@Override
		public void onValueChange(ValueChangeEvent<Date> arg0) {
		      Date date = arg0.getValue();
		      String dateString=DateTimeFormat.getFormat("MM/dd/yyyy").format(date);
		      departureDateFromUI.setText(dateString);
		}
	  });
	Label departureSectionLabel = new Label("Departure date: ");
	departureSectionLabel.getElement().getStyle().setPaddingRight(3, Unit.PX);
	
	final DatePicker arrival = new DatePicker();
	arrival.setCurrentMonth(new Date());
	final Label arrivalDateFromUI = new Label();
	arrival.addValueChangeHandler(new ValueChangeHandler<Date>(){
		@Override
		public void onValueChange(ValueChangeEvent<Date> arg0) {
		      Date date = arg0.getValue();
		      String dateString=DateTimeFormat.getFormat("MM/dd/yyyy").format(date);
		      arrivalDateFromUI.setText(dateString);
		}
	  });
	Label arrivalSectionLabel = new Label("Arrival date: ");
	arrivalSectionLabel.getElement().getStyle().setPaddingRight(3, Unit.PX);
	arrivalSectionLabel.getElement().getStyle().setPaddingLeft(40, Unit.PX);

	fourthRow.add(departureSectionLabel);
	fourthRow.add(departure);
	fourthRow.add(arrivalSectionLabel);
	fourthRow.add(arrival);
	fourthRow.getElement().getStyle().setPaddingLeft(10, Unit.PX);
	
	
	saveAirlineButton = new Button("Add flight");
	saveAirlineButton.addClickHandler(new ClickHandler() {
		@Override
		public void onClick(ClickEvent arg0) {
			List<String> errorList = validateAirline(airlineName.getText(), flightNumber.getText(), source.getText(), departureDateFromUI.getText(), departureTimeHour.getText(),
					departureTimeMinute.getText(), destination.getText(), arrivalDateFromUI.getText(), arrivalTimeHour.getText(), arrivalTimeMinute.getText());
			
			if(errorList.size() == 0) {
				Airline airline = new Airline();
				airline.setName(airlineName.getText());
				Flight flight = new Flight();
				int flightNum = Integer.parseInt(flightNumber.getText());
				flight.setNumber(flightNum);
				flight.setSource(source.getText());
				flight.setDestination(destination.getText());
				
				StringBuilder departureDateSb = new StringBuilder();
				departureDateSb.append(departureDateFromUI.getText()).append(" ").append(departureTimeHour.getText()).append(":").append(departureTimeMinute.getText()).append(" ").append(amPmDeparture.getSelectedItemText());
				
				Date departureDate = null;
				DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat(DATE_FORMAT_PATTERN);
				try {
					departureDate = dateTimeFormat.parse(departureDateSb.toString());
					flight.setDeparture(departureDate);
				} catch (IllegalArgumentException e) {
				}
				flight.setDepartureString(departureDateSb.toString());

				StringBuilder arrivalDateSb = new StringBuilder();
				arrivalDateSb.append(arrivalDateFromUI.getText()).append(" ").append(arrivalTimeHour.getText()).append(":").append(arrivalTimeMinute.getText()).append(" ").append(amPmArrival.getSelectedItemText());
				
				Date arrivalDate = null;
				try {
					arrivalDate = dateTimeFormat.parse(arrivalDateSb.toString());
					flight.setArrival(arrivalDate);
				} catch (IllegalArgumentException e) {
				}
				flight.setArrivalString(arrivalDateSb.toString());
				
//				Date departureDate = null;
//				DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat(DATE_FORMAT_PATTERN);
//				try {
//					departureDate = dateTimeFormat.parse(departure.getValue());
//					flight.setDeparture(departure.getValue());
//				} catch (IllegalArgumentException e) {
//				}
//				flight.setDepartureString(departure.getValue()());
				
//				Date arrivalDate = null;
//				try {
//					arrivalDate = dateTimeFormat.parse(arrival.getText());
//					flight.setArrival(arrival.getValue());
//				} catch (IllegalArgumentException e) {
//				}
//				flight.setArrivalString(arrival.getText());
				
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
	
	HorizontalPanel fifthRow = new HorizontalPanel();
	fifthRow.add(saveAirlineButton);
	fifthRow.getElement().getStyle().setPaddingLeft(10, Unit.PX);
	  
    showAirlineButton = new Button("Display all flights for airline");
    showAirlineButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        showAirline();
      }
    });
    
	Label searchFlight = new Label("SEARCH FOR FLIGHTS");
	searchFlight.getElement().getStyle().setFontWeight(FontWeight.BOLD);
	searchFlight.getElement().getStyle().setFontSize(14, Unit.PX);
	searchFlight.getElement().getStyle().setPaddingBottom(10, Unit.PX);
	searchFlight.getElement().getStyle().setPaddingTop(30, Unit.PX);
	
	HorizontalPanel sixthRow = new HorizontalPanel();
	final TextBox airlineToSearch = new TextBox();
	airlineToSearch.setName("airlineToSearch");
	airlineToSearch.getElement().setPropertyString("placeholder", "ex: Alaska");
	airlineToSearch.getElement().getStyle().setPaddingRight(15, Unit.PX);
	Label airlineToSearchSectionLabel = new Label("Airline name: ");
	airlineToSearchSectionLabel.getElement().getStyle().setPaddingRight(3, Unit.PX);
	sixthRow.add(airlineToSearchSectionLabel);
	sixthRow.add(airlineToSearch);
	sixthRow.getElement().getStyle().setPaddingLeft(10, Unit.PX);
	
	HorizontalPanel seventhRow = new HorizontalPanel();
	final TextBox sourceToSearch = new TextBox();
	sourceToSearch.setName("sourceToSearch");
	sourceToSearch.getElement().setPropertyString("placeholder", "ex: PDX");
	Label sourceToSearchSectionLabel = new Label("Departure airport: ");
	sourceToSearchSectionLabel.getElement().getStyle().setPaddingRight(3, Unit.PX);
	
	final TextBox destinationToSearch = new TextBox();
	destinationToSearch.setName("destinationToSearch");
	destinationToSearch.getElement().setPropertyString("placeholder", "ex: LAX");
	Label destinationToSearchSectionLabel = new Label("Arrival airport: ");
	destinationToSearchSectionLabel.getElement().getStyle().setPaddingLeft(20, Unit.PX);
	destinationToSearchSectionLabel.getElement().getStyle().setPaddingRight(3, Unit.PX);
	seventhRow.add(sourceToSearchSectionLabel);
	seventhRow.add(sourceToSearch);
	seventhRow.add(destinationToSearchSectionLabel);
	seventhRow.add(destinationToSearch);
	seventhRow.getElement().getStyle().setPaddingLeft(10, Unit.PX);
	
//	final TextBox sourceToSearch = new TextBox();
//	sourceToSearch.setName("sourceToSearch");
//	sourceToSearch.getElement().setPropertyString("placeholder", "Departure airport");
//	
//	final TextBox destinationToSearch = new TextBox();
//	destinationToSearch.setName("destinationToSearch");
//	destinationToSearch.getElement().setPropertyString("placeholder", "Destination airport");

	searchFlightsButton = new Button("Search for flights");
	searchFlightsButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
			List<String> errorList = validateSearch(airlineToSearch.getText(), sourceToSearch.getText(), destinationToSearch.getText());
			
			if(errorList.size() == 0) {
				searchFlights(airlineToSearch.getText(), sourceToSearch.getText(), destinationToSearch.getText());
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
	
	HorizontalPanel eigthRow = new HorizontalPanel();
	eigthRow.add(searchFlightsButton);
	eigthRow.getElement().getStyle().setPaddingLeft(10, Unit.PX);
	
	HorizontalPanel ninthRow = new HorizontalPanel();
	Label returnAllFlights = new Label("SHOW ALL FLIGHTS");
	returnAllFlights.getElement().getStyle().setFontWeight(FontWeight.BOLD);
	returnAllFlights.getElement().getStyle().setFontSize(14, Unit.PX);
	returnAllFlights.getElement().getStyle().setPaddingBottom(10, Unit.PX);
	returnAllFlights.getElement().getStyle().setPaddingTop(30, Unit.PX);
	ninthRow.add(returnAllFlights);
	
	HorizontalPanel tenthRow = new HorizontalPanel();
	tenthRow.add(showAirlineButton);
	tenthRow.getElement().getStyle().setPaddingLeft(10, Unit.PX);
    
    panel.add(addFlight);
    panel.add(firstRow);
    panel.add(secondRow);
    panel.add(thirdRow);
    panel.add(fourthRow);
    panel.add(fifthRow);
    panel.add(searchFlight);
    panel.add(sixthRow);
    panel.add(seventhRow);
    panel.add(eigthRow);
    panel.add(ninthRow);
    panel.add(tenthRow);
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
//			StringBuilder sb = new StringBuilder(airline.toString());
//			sb.append("\n");
//			Collection<Flight> flights = airline.getFlights();
//			for (Flight flight : flights) {
//				sb.append(flight);
//				sb.append("\n");
//			}
//			alerter.alert(sb.toString());
        	prettyPrintAirline(airline, airlinePrettyText);
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
			airlinePrettyText.setText("");
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
				airlinePrettyText.setText("");
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
  
  private List<String> validateAirline(String airlineName, String flightNumber, String src, String departureDate, String departureHour, String departureMinute, String destination, String arrivalDate, String arrivalHour, String arrivalMinute) {
	  
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
		
		if(departureDate.isEmpty()) {
			errorList.add("ERROR: Departure date is required");
		}
		
		if(!departureHour.isEmpty()) {
			int hour;
			try {
				hour = Integer.parseInt(departureHour);
				if(hour > 12) {
					errorList.add("ERROR: Departure time, HOUR, must be 12 or smaller. ");
				}
			} catch (NumberFormatException e) {
				errorList.add("ERROR: Departure time, HOUR, must be a digit.");
			}

		} else {
			errorList.add("ERROR: Departure time, HOUR, is required");
		}
		
		if(!departureMinute.isEmpty()) {
			int minute;
			try {
				minute = Integer.parseInt(departureMinute);
				if(minute > 59) {
					errorList.add("ERROR: Departure time, MINUTE, must be 59 or smaller. ");
				}
			} catch (NumberFormatException e) {
				errorList.add("ERROR: Departure time, MINUTE, must be a digit.");
			}

		} else {
			errorList.add("ERROR: Departure time, MINUTE, is required");
		}
		
		if(!destination.isEmpty()) {
			if(!validateAirportCode(destination)) {
				errorList.add("ERROR: Destination airport code is not valid. Valid codes are 3 characters long, may only contains letters, and must be a real code.");
			}
		} else {
			errorList.add("ERROR: Destination airport required");
		}
		
		if(arrivalDate.isEmpty()) {
			errorList.add("ERROR: Arrival date is required");
		}
		
		if(!arrivalHour.isEmpty()) {
			int hour;
			try {
				hour = Integer.parseInt(arrivalHour);
				if(hour > 12) {
					errorList.add("ERROR: Arrival time, HOUR, must be 12 or smaller. ");
				}
			} catch (NumberFormatException e) {
				errorList.add("ERROR: Arrival time, HOUR, must be a digit.");
			}

		} else {
			errorList.add("ERROR: Departure time, HOUR, is required");
		}
		
		if(!arrivalMinute.isEmpty()) {
			int minute;
			try {
				minute = Integer.parseInt(arrivalMinute);
				if(minute > 59) {
					errorList.add("ERROR: Arrival time, MINUTE, must be 59 or smaller. ");
				}
			} catch (NumberFormatException e) {
				errorList.add("ERROR: Arrival time, MINUTE, must be a digit.");
			}

		} else {
			errorList.add("ERROR: Arrival time, MINUTE, is required");
		}
		
//		if(!arrival.isEmpty()) {
//			Date date = null;
//			DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat(DATE_FORMAT_PATTERN);
//			try {
//				date = dateTimeFormat.parse(arrival);
//			} catch (IllegalArgumentException e) {
//				errorList.add("ERROR: Arrival date/time is not in the correct format. (Correct format: MM/DD/YYYY HH:MM AM/PM)");
//			}
//		} else {
//			errorList.add("ERROR: Arrival date/time is required");
//		}
		
		return errorList;  
  }
  
  private List<String> validateSearch(String airlineName, String src, String destination) {
	  
		List<String> errorList = new ArrayList<String>();
		
		if(airlineName.isEmpty()) {
			errorList.add("ERROR: Airline name required");
		}
		
		if(!src.isEmpty()) {
			if(!validateAirportCode(src)) {
				errorList.add("ERROR: Source airport code is not valid. Valid codes are 3 characters long, may only contains letters, and must be a real code.");
			}
		} else {
			errorList.add("ERROR: Departure airport is required");
		}
		
		if(!destination.isEmpty()) {
			if(!validateAirportCode(destination)) {
				errorList.add("ERROR: Arrival airport code is not valid. Valid codes are 3 characters long, may only contains letters, and must be a real code.");
			}
		} else {
			errorList.add("ERROR: Arrival airport is required");
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
    VerticalPanel verticalPanelLeft = new VerticalPanel();
    addWidgets(verticalPanelLeft);
    
    VerticalPanel verticalPanelRight = new VerticalPanel();
    addRightWidgets(verticalPanelRight);
    verticalPanelRight.getElement().getStyle().setPaddingLeft(120, Unit.PX);
    
    HorizontalPanel horizontalPanel = new HorizontalPanel();
    horizontalPanel.add(verticalPanelLeft);
    horizontalPanel.add(verticalPanelRight);
    horizontalPanel.getElement().getStyle().setPaddingLeft(20, Unit.PX);
    
    Label header = new Label("AIRLINE APP");
    header.getElement().getStyle().setFontSize(30, Unit.PX);
    header.getElement().getStyle().setFontWeight(FontWeight.BOLDER);
    header.getElement().getStyle().setPaddingBottom(20, Unit.PX);
    header.getElement().getStyle().setPaddingLeft(20, Unit.PX);
    header.getElement().getStyle().setPaddingTop(20, Unit.PX);
    rootPanel.add(header);
    rootPanel.add(horizontalPanel);


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
