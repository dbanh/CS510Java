package edu.pdx.cs410J.dbanh;

import com.google.common.annotations.VisibleForTesting;
import edu.pdx.cs410J.web.HttpRequestHelper;

import java.io.IOException;
import java.util.Map;

import org.slf4j.LoggerFactory;

import static java.net.HttpURLConnection.HTTP_OK;

/**
 * A helper class for accessing the rest client.  Note that this class provides
 * an example of how to make gets and posts to a URL.  You'll need to change it
 * to do something other than just send key/value pairs.
 */
public class AirlineRestClient extends HttpRequestHelper
{
    private static final String WEB_APP = "airline";
    private static final String SERVLET = "flights";
    
    final static org.slf4j.Logger logger = LoggerFactory.getLogger(AirlineServlet.class);

    private final String url;


    /**
     * Creates a client to the airline REST service running on the given host and port
     * @param hostName The name of the host
     * @param port The port
     */
    public AirlineRestClient( String hostName, int port )
    {
        this.url = String.format( "http://%s:%d/%s/%s", hostName, port, WEB_APP, SERVLET );
    }

    /**
     * Returns all keys and values from the server
     */
    public Map<String, String> getAllKeysAndValues() throws IOException {
      Response response = get(this.url);
      return Messages.parseKeyValueMap(response.getContent());
    }
    
    /**
     * Returns all flights from the server
     */
    public String getAllFlights() throws IOException {
      Response response = get(this.url);
//      return Messages.parseKeyValueMap(response.getContent());
      return response.getContent();
    }

    /**
     * Returns the value for the given key
     */
    public String getValue(String key) throws IOException {
      Response response = get(this.url, "key", key);
      throwExceptionIfNotOkayHttpStatus(response);
      String content = response.getContent();
      return Messages.parseKeyValuePair(content).getValue();
    }
    
    public String searchForFlights(String airlineName, String src, String dest) throws IOException {
        Response response = get(this.url, "name", airlineName, "src", src, "dest", dest);
        return response.getContent();
      }

    public void addKeyValuePair(String key, String value) throws IOException {
      Response response = postToMyURL("key", key, "value", value);
      throwExceptionIfNotOkayHttpStatus(response);
    }
    
    public String addAirlineAndFlight(String airlineName, String flightNumber, String src, String departTime, String dest, String arriveTime) throws IOException {
        Response response = postToMyURL("name", airlineName, "flightNumber", flightNumber, "src", src, "departTime", departTime, "dest", dest, "arriveTime", arriveTime);
        if(response.getCode() != HTTP_OK) {
        	return response.getContent();
        }
        return null;
      }

    @VisibleForTesting
    Response postToMyURL(String... keysAndValues) throws IOException {
      return post(this.url, keysAndValues);
    }

    public void removeAllMappings() throws IOException {
      Response response = delete(this.url);
      throwExceptionIfNotOkayHttpStatus(response);
    }

    private Response throwExceptionIfNotOkayHttpStatus(Response response) {
      int code = response.getCode();
      if (code != HTTP_OK) {
//        throw new AppointmentBookRestException(code);
    	  logger.debug("response content: " + response.getContent());
      }
      return response;
    }

    private class AppointmentBookRestException extends RuntimeException {
      public AppointmentBookRestException(int httpStatusCode) {
        super("Got an HTTP Status Code of " + httpStatusCode);
      }
    }
}
