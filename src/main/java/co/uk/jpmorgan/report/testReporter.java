package co.uk.jpmorgan.report;





import org.apache.log4j.Logger;
import org.testng.Assert;

import co.uk.jpmorgan.SeleniumBase;








/**
 * Used to report test failures (with/without stopping the test), passes, info messages and debug logging as an example
 * 
 * it will report raw data to the Console/Logger as well as to our more structured XML Reporting Engines at the same time
 * 
 *
 */
public class testReporter 
{
      
	/* Placeholder XML Engine to report using our main Report format*/
	protected static XMLEngine XMLENGINE;
	
    /* create reporter object which will be used for reporting to Console */
    protected static final Logger LOGGER = Logger.getLogger("REPORT");

    /**
     * Constructor that starts a new XML Reporting engine for the test
     */
    public testReporter(String strTestName) 
	{
		XMLENGINE = new XMLEngine(strTestName);
	}

	/**
     * Adds a new Iteration node to the XML report. This marks the start of a new test 
     * 
     * @param strTestIdentifier 	String - The name of the test to be reported on.
     */
    public void addNewIteration(String strTestIdentifier)
    {
    	XMLENGINE.addNewIteration(strTestIdentifier);
    }
    
    /**
     * Closes an XML iteration node and will update the metrics, setting the end time of the iteration. 
     * This marks the end of a test iteration and is to be called during the "After" phase
     */
    public void closeIteration()
    {
    	XMLENGINE.closeIteration();
    }
    
    /**
     * Stop the reporter XML Engine and updates all metrics.
     */
    public void stopReporter()
    {
    	XMLENGINE.stopEngine();
    }
    
	/**
	 * Used to report PASSING test results via Assert.assertTrue and forces the user to specify various values to
	 * to the Log4J and to the XML Engine for standardised reporting. 
	 * 
	 * We have to add this as an assert-pass is not naturally reported to the console so positive information
	 * required by testers would otherwise be lost
	 * 
	 * @param strStepIdentifier 	String - A value that can help determine where the report was made or the specific check it relates to.
	 * @param strExpectedResult 	String - What did the tester expect to see or what was expected value.
	 * @param strActualResult 		String - What actually happened or what was actual value.
	 * @param strTestInformation 	String - Any information that can help the user when reporting the defect.
	 */
	public void reportPass(String strStepIdentifier, String strExpectedResult, String strActualResult, String strTestInformation)
	{
	  /* Create message to report in the specific format. */
	  String strMessage = "PASS - StepID:" + "[" + strStepIdentifier + "]| Expected: ["+ strExpectedResult +"]|Actual: ["+ strActualResult +"]| Info: " + "["+ strTestInformation +"]";
	  
	  /* Report the PASS to the XML reporting engine */
	  XMLENGINE.report("PASS", strStepIdentifier, strExpectedResult, strActualResult, strTestInformation);
	  
	  /* Report the PASS to the Console/Logger as an info message as there is no other way to report a pass for now. */
	  LOGGER.info(strMessage);
	  
	  /* Report pass through assert (which doesn't log anything!).*/
	  Assert.assertTrue(true, strMessage); 
	}

	/**
	 * Used to report FAILING test results via Assert.assertTrue(false) and forces the user to specify various values to
	 * to the Log4J and to the XML Engine for standardised reporting. 
	 * 
	 * Any failure will ignore any exceptions thrown by the assert so will allow the test to continue running
	 * 
	 * @param strStepIdentifier 	String - A value that can help determine where the report was made or the specific check it relates to.
	 * @param strExpectedResult 	String - What did the tester expect to see or what was expected value
	 * @param strActualResult 		String - What actually happened or what was actual value
	 * @param strTestInformation 	String - Any information that can help the user when reporting the defect
	 */
    public void reportFail(String strStepIdentifier, String strExpectedResult, String strActualResult, String strTestInformation)
    {
      /* Create message to report in the specific format. */
      String strMessage = "FAIL - StepID:" + "[" + strStepIdentifier + "]| Expected: ["+ strExpectedResult +"]|Actual: ["+ strActualResult +"]| Info: " + "["+ strTestInformation +"]";
      
      /* Report the FAIL to the XML reporting engine */
	  XMLENGINE.report("FAIL", strStepIdentifier, strExpectedResult, strActualResult, strTestInformation);
      
	  /* Report the FAIL to the Console/Logger */
      LOGGER.info(strMessage); 
      
      /* Report FAIL through an assert however do not exit the test */
        try
        {
            Assert.assertTrue(false, strMessage);
        } 
        catch (Error e)
        {
        }
    }	
	   
    /**
     * Used to report FAILING test results via Assert.fail and forces the user to specify various values to
	 * to the Log4J and to the XML Engine for standardised reporting. 
     * 
     * This method reports the failure and stops the test iteration, marking the TestNG test as FAILED
     * 
     * @param strStepIdentifier 	String - A value that can help determine where the report was made or the specific check it relates to.
     * @param strExpectedResult 	String - What did the tester expect to see or what was expected value
     * @param strActualResult 		String - What actually happened or what was actual value
     * @param strTestInformation 	String - Any information that can help the user when reporting the defect
     */
    public void reportFailAndExitTest(String strStepIdentifier, String strExpectedResult, String strActualResult, String strTestInformation)
    {
    	/* Create message to report in the specific format. */
        String strMessage = "FAIL - StepID:" + "[" + strStepIdentifier + "]| Expected: ["+ strExpectedResult +"]|Actual: ["+ strActualResult +"]| Info: " + "["+ strTestInformation +"]";
        
        /* Report the FAIL to the XML reporting engine */
  	    XMLENGINE.report("FAIL", strStepIdentifier, strExpectedResult, strActualResult, "Exiting Test: " + strTestInformation);
               
  	    /* Report FAIL through an assert */
        Assert.fail(strMessage);
    }

    /**
     * Reports a DEBUG type message to Log4J and to the XML Engine however only if the
     * framework's Debug Switch is equal to TRUE. 
     * 
     * Please use as this is very helpful in identify problems in our TestNG scripts
     * 
     * @param strDebugInformation 	String - Any information that can help the user to debug
     */
    public void reportDebug(String strDebugInformation)
    {
    	// Log the DEBUG message to the XML Engine and the LOGGER but only if debugging is enabled at
    	// framework level
    	if(SeleniumBase.DebugSwitch)
    	{
    		XMLENGINE.report("DEBUG", " ", " " , " ", strDebugInformation);    	

    	}
    }

    /**
     * Reports an INFO type message to Log4J and to the XML Engine 
     * 
     * This is especially useful to provide information that is useful for the tester but not related to an
     * actual test case
     * 
     * @param strINFOMesage		String - Any information that can help the tester
     */
    public void reportInfo(String strINFOMesage)
    {
   	   // Log the INFO message to the XML Engine and the LOGGER
   	   XMLENGINE.report("INFO", " ", " " , " ", strINFOMesage);    	
   	   LOGGER.info("INFO - Info: " + "["+ strINFOMesage +"]");
    }
}

