package edu.pdx.cs410J.dbanh;

import edu.pdx.cs410J.InvokeMainTestCase;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * An integration test for the {@link Project1} main class.
 */
public class Project1IT extends InvokeMainTestCase {

    /**
     * Invokes the main method of {@link Project1} with the given arguments.
     */
    private MainMethodResult invokeMain(String... args) {
        return invokeMain( Project1.class, args );
    }

  /**
   * Tests that invoking the main method with no arguments issues an error
   */
  @Test
  public void testNoCommandLineArguments() {
    MainMethodResult result = invokeMain();
    assertThat(result.getExitCode(), equalTo(1));
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing command line arguments"));
  }
  
  /**
   * Tests that invoking the main method with not enough arguments issues an error
   */
  @Test
  public void testNotEnoughArguments() {
    MainMethodResult result = invokeMain("United", "42", "ABC");
    assertThat(result.getExitCode(), equalTo(1));
    assertThat(result.getTextWrittenToStandardError(), containsString("Command line arguments not valid"));
  }
  
  /**
   * Tests that invoking the main method with too many arguments issues an error
   */
  @Test
  public void testTooManyArguments() {
    MainMethodResult result = invokeMain("United", "42", "ABC", "01/01/2001", "1:11", "PDX", "03/03/2003", "3:33", "ABCD");
    assertThat(result.getExitCode(), equalTo(1));
    assertThat(result.getTextWrittenToStandardError(), containsString("Command line arguments not valid."));
  }

  /**
   * Tests that invoking the main method with -README prints out readme
   */
  @Test
  public void testReadme() {
    MainMethodResult result = invokeMain("-README", "United", "42", "ABC", "01/01/2001", "1:11", "PDX", "03/03/2003", "3:33");
    assertThat(result.getExitCode(), equalTo(1));
    assertThat(result.getTextWrittenToStandardOut(), containsString("README"));
    
    MainMethodResult result2 = invokeMain("-print", "-README", "United", "42", "ABC", "01/01/2001", "1:11", "PDX", "03/03/2003", "3:33");
    assertThat(result2.getExitCode(), equalTo(1));
    assertThat(result2.getTextWrittenToStandardOut(), containsString("README"));
  }
  
  /**
   * Tests that invoking the main method with -print prints out flight info
   */
  @Test
  public void testPrint() {
    MainMethodResult result = invokeMain("-print", "United", "42", "ABC", "01/01/2001", "1:11", "PDX", "03/03/2003", "3:33");
    assertThat(result.getExitCode(), equalTo(1));
    assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 42 departs ABC"));
  }
}