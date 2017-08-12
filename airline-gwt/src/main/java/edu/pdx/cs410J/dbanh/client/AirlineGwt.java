package edu.pdx.cs410J.dbanh.client;

import com.google.common.annotations.VisibleForTesting;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.UmbrellaException;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A basic GWT class that makes sure that we can send an airline back from the server
 */
public class AirlineGwt implements EntryPoint {

  private final Alerter alerter;
  private final AirlineServiceAsync airlineService;
  private final Logger logger;

  @VisibleForTesting
  Button showAirlineButton;
  
  @VisibleForTesting
  Button saveAirlineButton;

  @VisibleForTesting
  Button showUndeclaredExceptionButton;

  @VisibleForTesting
  Button showDeclaredExceptionButton;

  @VisibleForTesting
  Button showClientSideExceptionButton;

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
			Airline airline = new Airline();
			airline.setName(airlineName.getText());
			Flight flight = new Flight();
			int flightNum = Integer.parseInt(flightNumber.getText());
			flight.setNumber(flightNum);
			flight.setSource(source.getText());
			flight.setDestination(destination.getText());
			flight.setDepartureString(departure.getText());
			flight.setArrivalString(arrival.getText());
			airline.addFlight(flight);
			saveAirline(airline);
		}
	});
	  
    showAirlineButton = new Button("Display all flights for airline");
    showAirlineButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        showAirline();
      }
    });

    showUndeclaredExceptionButton = new Button("Show undeclared exception");
    showUndeclaredExceptionButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        showUndeclaredException();
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

    panel.add(airlineName);
    panel.add(flightNumber);
    panel.add(source);
    panel.add(departure);
    panel.add(destination);
    panel.add(arrival);
    panel.add(saveAirlineButton);
    panel.add(showAirlineButton);
    panel.add(showUndeclaredExceptionButton);
    panel.add(showDeclaredExceptionButton);
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
        StringBuilder sb = new StringBuilder(airline.toString());
        Collection<Flight> flights = airline.getFlights();
        for (Flight flight : flights) {
          sb.append(flight);
          sb.append("\n");
        }
        alerter.alert(sb.toString());
      }
    });
  }
  
  private void saveAirline(Airline airline) {
	  logger.info("Calling saveAirline");
	  airlineService.saveAirline(airline, new AsyncCallback<Airline>() {

		@Override
		public void onFailure(Throwable arg0) {
			alertOnException(arg0);
		}

		@Override
		public void onSuccess(Airline arg0) {
			alertOnSuccess();
		}
	});
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
