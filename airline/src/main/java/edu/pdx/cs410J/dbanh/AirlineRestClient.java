package edu.pdx.cs410J.dbanh;

import com.google.common.annotations.VisibleForTesting;
import edu.pdx.cs410J.web.HttpRequestHelper;

import java.io.IOException;
import static java.net.HttpURLConnection.HTTP_OK;

/**
 * A helper class for accessing the rest client. The main class calls these methods to call the REST api
 */
public class AirlineRestClient extends HttpRequestHelper
{
    private static final String WEB_APP = "airline";
    private static final String SERVLET = "flights";
    
//    final static org.slf4j.Logger logger = LoggerFactory.getLogger(AirlineServlet.class);

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
     * Returns all flights from the server
     */
    public String getAllFlights() throws IOException {
      Response response = get(this.url);
      return response.getContent();
    }
    
    /**
     * Returns flights that match the source and destination airport
     * @param airlineName
     * @param src
     * @param dest
     * @return
     * @throws IOException
     */
    public String searchForFlights(String airlineName, String src, String dest) throws IOException {
        Response response = get(this.url, "name", airlineName, "src", src, "dest", dest);
        return response.getContent();
      }
    
    /**
     * Adds a flight to the airline in the servlet
     * @param airlineName
     * @param flightNumber
     * @param src
     * @param departTime
     * @param dest
     * @param arriveTime
     * @return
     * @throws IOException
     */
    public String addAirlineAndFlight(String airlineName, String flightNumber, String src, String departTime, String dest, String arriveTime) throws IOException {
        Response response = postToMyURL("name", airlineName, "flightNumber", flightNumber, "src", src, "departTime", departTime, "dest", dest, "arriveTime", arriveTime);
        if(response.getCode() != HTTP_OK) {
        	return response.getContent();
        }
        return null;
      }

    /**
     * Posts to the URL
     * @param keysAndValues
     * @return
     * @throws IOException
     */
    @VisibleForTesting
    Response postToMyURL(String... keysAndValues) throws IOException {
      return post(this.url, keysAndValues);
    }
}
