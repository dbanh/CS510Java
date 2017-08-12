package edu.pdx.cs410J.dbanh.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import edu.pdx.cs410J.dbanh.client.Airline;
import edu.pdx.cs410J.dbanh.client.Flight;
import edu.pdx.cs410J.dbanh.client.AirlineService;

/**
 * The server-side implementation of the Airline service
 */
public class AirlineServiceImpl extends RemoteServiceServlet implements AirlineService
{
	
  Airline airline;	
	
  @Override
  public Airline getAirline() {
    return airline;
  }

  @Override
  public void throwUndeclaredException() {
    throw new IllegalStateException("Expected undeclared exception");
  }

  @Override
  public void throwDeclaredException() throws IllegalStateException {
    throw new IllegalStateException("Expected declared exception");
  }

  /**
   * Log unhandled exceptions to standard error
   *
   * @param unhandled
   *        The exception that wasn't handled
   */
  @Override
  protected void doUnexpectedFailure(Throwable unhandled) {
    unhandled.printStackTrace(System.err);
    super.doUnexpectedFailure(unhandled);
  }

  @Override
  public void saveAirline(Airline newAirline) {
	if(airline == null) {
		airline = new Airline();
	}
		
	this.airline = newAirline;
  }
}
