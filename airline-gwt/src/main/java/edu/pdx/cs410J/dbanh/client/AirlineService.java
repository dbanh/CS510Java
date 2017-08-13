package edu.pdx.cs410J.dbanh.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * A GWT remote service that returns a dummy airline
 */
@RemoteServiceRelativePath("airline")
public interface AirlineService extends RemoteService {

  /**
   * Returns the current date and time on the server
   */
  Airline getAirline();

  /**
   * Always throws an undeclared exception so that we can see GWT handles it.
   */
  void throwUndeclaredException();

  /**
   * Always throws a declared exception so that we can see GWT handles it.
   */
  void throwDeclaredException() throws IllegalStateException;
  
  /**
   * Saves airline into server
   * @param Airline
   */
  void saveAirline(Airline airline) throws IllegalArgumentException;
  
  /**
   * Searches for airline on the server
   * @param airline
   * @param src
   * @param dest
 * @return 
 * @throws Throwable 
   */
  Airline searchFlights(String airline, String src, String dest) throws IllegalArgumentException, Throwable;
}
