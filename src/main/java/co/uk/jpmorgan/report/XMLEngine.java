package co.uk.jpmorgan.report;



import mx4j.log.Logger;

import org.apache.xpath.XPathAPI;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;




import co.uk.jpmorgan.config.TestConfig;
import co.uk.jpmorgan.lib.Library;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * An XML engine used to report test results and metrics for each executed test iteration in a format that meets the 
 * base requirements of the function
 * 
 * 
 */
public class XMLEngine 
{
	/* object that holds the root of the XML file */
	private Element objRootElement = null;
	/* Boolean Value Keeps the Test Execution Log */
	 
	
	/* object that holds the document (entire XML) */
	private Document objDocument = null;
	
	/* Object that holds the current Iteration Node */
	private Element objCurrentTestNode = null;
	
	/* Object that holds the current Step Node */
	private Element objCurrentStep = null;
	
	/* Value and object required to store the generated XML File */
	private String objFilePath = null;
	
	/* set default log path  which can be overwritten with constructor */
	private String objDefaultOutPutDirectory = "target/qa-logs/";
	
	/* This is the name used for the default stylesheet which will be copied to QA logs directory */
	private String objDefaultStyleSheetName = "defaultStyleSheet.xsl";
	
	private String reportname;
	/**
	 * Constructor which automatically starts the XML engine and creates an output file in the default directory
	 * 
	 * @param strTestRunIdentifier		String - Name of test run
	 */
	public XMLEngine(String strTestRunIdentifier)
	{
		reportname = strTestRunIdentifier;
		startEngine(strTestRunIdentifier);
	}
	
	/**
	 * Constructor which automatically starts the XML engine and creates the output file 
	 * in a named output directory
	 * 
	 * @param strTestRunIdentifier		String - Name of test run
	 * @param strAbsolutePath			String - Absolute value of the output folder to write results to
	 */
	public XMLEngine(String strTestRunIdentifier, String strAbsolutePath)
	{
		startEngine(strTestRunIdentifier);
		objDefaultOutPutDirectory = strAbsolutePath;
	}

	/**
	 * Copies a stylesheet to the qa-logs directory to ensure our reports can be loaded and displayed correctly
	 *  
	 * @param strStyleSheetFileName	String - Name of the stylesheet to copy
	 */
	private void copyStylesheet(String strStyleSheetFileName)
	{
		try
		{	
			/* grab full URI path to resource and also where to output file to*/
			File inputFile = new File("src\\test\\resources\\"+objDefaultStyleSheetName);//ClassLoader.getSystemResource(strStyleSheetFileName).toURI().getPath());
			File outputFile = new File(objDefaultOutPutDirectory + "\\" + strStyleSheetFileName);
			    
			/* create filereader and filewriter */
			FileReader in = new FileReader(inputFile);
			FileWriter out = new FileWriter(outputFile);
			int counter;
			    
			    /* output file to qa-logs directory. */
			    while ((counter = in.read()) != -1){
			      out.write(counter);
			    }
			    
			    /* close filereader and filewriter. */
			    in.close();
			    out.close();
		   }
		   catch(IOException e)
		   {
			   System.out.println("report file not found");
			   e.printStackTrace();
		   }
//		   catch(URISyntaxException e)
//		   {
//			   e.printStackTrace();
//		   }
	}
	
	/**
	 * Initialises the target directory to ensure the logs are written to the correct location
	 * 
	 * @param strFileName		String - Name of file to write to including full path
	 */
	private void initQALogsFolder(String strFileName)
	{
		// If the directory does not exist, create it 
        File qaLogsDirectory = new File("target/qa-logs/");///target/qa-logs
        if (!qaLogsDirectory.exists())
        {	
        	qaLogsDirectory.mkdir();
        }
        
        // Now set the filePath value of the qaLogsDirectory
        objFilePath = qaLogsDirectory + "\\" + strFileName + ".XML";
   	}
	
	
	/**
	 * Creates an XML file where we can add relevant report information as required
	 * @exception ParserConfigurationException
	 * @exception FileNotFoundException
	 * @param strTestRunIdentifier String - i.e. TestPhase and WorkStream
	 */ 
	private void startEngine(String strTestRunIdentifier)
	{
		try 
		{
			/* create document builder */
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder documentBuilder;
			documentBuilder = documentBuilderFactory.newDocumentBuilder(); 
				
			/* Set the FilePath value and check if the report file already exists */
			initQALogsFolder(strTestRunIdentifier + "_Results");
			
			/* copy the default stylesheet to the qa-log directory */
			copyStylesheet(objDefaultStyleSheetName);
			
			File XMLfile = new File(objFilePath);
		    boolean exists = XMLfile.exists();
		    
		    if (true)
		    {			
		    	/* create rootElement Log with relevant attributes and add it to the document */
		    	objDocument = documentBuilder.newDocument();	   	
			    objRootElement = objDocument.createElement("Log");
   
			    //Add Processing Instructions for StyleSheet
			    Node Log = objDocument.getFirstChild();
			    Node pi = objDocument.createProcessingInstruction("xml-stylesheet", "type=\"text/xsl\" href=\"defaultStyleSheet.XSL\"");
			    				// 								   
			    objDocument.insertBefore(pi, Log);
			    
			    //Add Attributes to Log Element
			    objRootElement.setAttribute("TestRunID", strTestRunIdentifier);
			    objRootElement.setAttribute("Start", returnDate());
			    objRootElement.setAttribute("Finish", "");
			    objRootElement.setAttribute("Status", "PASS");
			    objRootElement.setAttribute("DebugStatus", "");
			    objRootElement.setAttribute("TotalNumberExecuted", "0");
			    objRootElement.setAttribute("TotalPass", "0");
			    objRootElement.setAttribute("TotalFail", "0");
			    objRootElement.setAttribute("TotalBlocks", "0");
			    objDocument.appendChild(objRootElement);
			    saveXMLFile();
		    }
		    else
		    {
		    	//load the XML file    		
		    		try 
		    		{
		    			//reload the XML file
						objDocument = documentBuilder.parse(XMLfile);
						objRootElement = (Element) objDocument.getElementsByTagName("Log").item(0);		
					} 
		    		catch (SAXException e) 
		    		{
						e.printStackTrace();
					} 
		    		catch (IOException e) 
		    		{
						e.printStackTrace();
					}
		    }
		} 
		catch (ParserConfigurationException e) 
		{
			e.printStackTrace();
		}
	}

	/**
	 * Adds a new TestIteration element to the report and should be called when starting a new test. 
	 * @param strTestIdentifier	String - Name of the test to be executed.
	 */
	public void addNewIteration(String strTestIdentifier) 
	{	 	 
		 objCurrentTestNode = objDocument.createElement("TestIteration");
		 objCurrentTestNode.setAttribute("Start", returnDate());
		 objCurrentTestNode.setAttribute("Finish", "");
		 objCurrentTestNode.setAttribute("TestIdentifier", strTestIdentifier);
		 objCurrentTestNode.setAttribute("Status", "PASS");
		 objRootElement.appendChild(objCurrentTestNode);
	}
	
	/**
	 * Closes the test iteration and update the metrics. Should be called once the test has finished.
	 */
	public void closeIteration()
	{
		/* Update the currentTestNode with Finish Time and update it in the rootElement */
		objCurrentTestNode.setAttribute("Finish", returnDate());
		objRootElement.appendChild(objCurrentTestNode);
		  
		/* update metrics as soon as new Iteration is added */
		updateTestMetrics();
		saveXMLFile();
	}
	
	/**
	 * This method is used to add data to elements within a test step. It specifically generates
	 * CDDATA sections as the values added might have XML "unfriendly" characters.
	 * @param strElementName 	String - The node name for the value to be added.
	 * @param strElementValue 	String - The value to be added to the node.
	 * @exception TransformerException  
	 */ 
	private void addDataToElement(String strElementName, String strElementValue) 
	{
		try 
		{
			/* Create CDDATA Section and add it to the Element*/
			CDATASection cdStatus = objDocument.createCDATASection(strElementValue);
			Element node = (Element) XPathAPI.selectSingleNode(objCurrentStep, strElementName);
			node.appendChild(cdStatus);
		} 
		catch (TransformerException e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Reports a "Test Step" within a Test Iteration. It uses the addDataToElement method
	 * to add the values that are passed into the XML as CDDATA elements. It will also automatically update
	 * the TestIteration Status.
	 * @param strStatus 	String - The status of the step to be reported (e.g. PASS, FAIL, WARNING, INFO etc.)
	 * @param strStepName  String - Identifier for the individual step to be reported on.
	 * @param strExpected  String - Expected result for the step to be reported
	 * @param strActual 	String - Actual result for the step to be reported
	 * @param strDetails 	String - Details about the step or failure that can help reproduce the defect.
	 */ 
	public void report(String strStatus, String strStepName, String strExpected, String strActual, String strDetails) 
	{
		/* create a step node and set it in currentStep object. */
		objCurrentTestNode.appendChild(objDocument.createElement("Step"));
		objCurrentStep = (Element) objCurrentTestNode.getLastChild();
		 
		/* Add child nodes to step and populate them using the addDataToElement method */
		objCurrentStep.appendChild(objDocument.createElement("Status"));
		addDataToElement("Status", strStatus);
		 
		objCurrentStep.appendChild(objDocument.createElement("StepName"));
		addDataToElement("StepName", strStepName);
		 
		objCurrentStep.appendChild(objDocument.createElement("Expected"));
		addDataToElement("Expected", strExpected);
		
		objCurrentStep.appendChild(objDocument.createElement("Link"));
		addDataToElement("Link", "file:///"+Library.findFolder("screenshots")+"\\"+TestConfig.browser+strStepName+".png");
		
		objCurrentStep.appendChild(objDocument.createElement("Actual"));
		addDataToElement("Actual", strActual);
		 
		objCurrentStep.appendChild(objDocument.createElement("Details"));
		addDataToElement("Details", strDetails);
		 
		objCurrentStep.appendChild(objDocument.createElement("Time"));
		addDataToElement("Time", returnDate());
		 
		/* Only apply new status to TestIteration if it's a fail or if it's a block and existing status is not fail */
		if(!strStatus.equalsIgnoreCase("PASS")&&!strStatus.equalsIgnoreCase("WARNING")&&!strStatus.equalsIgnoreCase("INFO")&&!strStatus.equalsIgnoreCase("DEBUG")){
			if(strStatus.equalsIgnoreCase("FAIL") || strStatus.equalsIgnoreCase("BLOCK") && !objCurrentTestNode.getAttribute("Status").equalsIgnoreCase("FAIL")){
				objCurrentTestNode.setAttribute("Status", strStatus.toUpperCase());			 
			}
		}
	}
 
	/**
	 * Updates the test run metrics after each test and is called once a test has finished.
	 * @exception TransformerException
	 */ 
	private void updateTestMetrics() 
	{
		try 
		{
			/* Count all the TestIteration Elements and set the TotalNumberExecuted Value */
			int TotalNumberExecuted = objRootElement.getChildNodes().getLength();
			objRootElement.setAttribute("TotalNumberExecuted",String.valueOf(TotalNumberExecuted));
			
			/* Using Xpath count all the failure nodes and update the TotalFail value */
			NodeList FailureNodes = XPathAPI.selectNodeList(objRootElement, "//TestIteration[@Status='FAIL']");
			objRootElement.setAttribute("TotalFail",String.valueOf(FailureNodes.getLength()));
			 
			/* Using Xpath count all the pass nodes and update the TotalPass value */
			NodeList PassNodes = XPathAPI.selectNodeList(objRootElement, "//TestIteration[@Status='PASS']");
			objRootElement.setAttribute("TotalPass",String.valueOf(PassNodes.getLength()));	
			 
			/* Using Xpath count all the blocked nodes and update the TotalBlocks value */
			NodeList BlockNodes = XPathAPI.selectNodeList(objRootElement, "//TestIteration[@Status='BLOCK']");
			objRootElement.setAttribute("TotalBlocks",String.valueOf(BlockNodes.getLength()));
			
			/* check if any failures and update high level */
			if(FailureNodes.getLength()>0){
				objRootElement.setAttribute("Status", "FAIL");
			}
			/* If no failures but there are blocks update high level with BLOCKED */
			else if(BlockNodes.getLength()>0){
				objRootElement.setAttribute("Status", "BLOCKED");
			}
		} 
		catch (TransformerException e)
		{
			e.printStackTrace();
		}	 
	}
	
	/**
	 * Saves the XML file store in the document object using the private fileOutputStream object which holds the filepath.
	 * @exception TransformerConfigurationException
	 * @exception TransformerException
	 */ 	
	private void saveXMLFile()
	{
		try 
		{
			//Overwrite XML file with new & updated document source.
			FileOutputStream fileOutputStream = null;
			fileOutputStream = new FileOutputStream(new File(objFilePath));

			/* Using the fileOutputStream store the document object XML source to an XML file*/
			StreamResult streamResult = new StreamResult(fileOutputStream);
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(objDocument);
			transformer.transform(source, streamResult);
		} 
		catch (TransformerConfigurationException e) 
		{
			e.printStackTrace();
		} 
		catch (TransformerException e) 
		{
			e.printStackTrace();
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
	}
	
	/**
	 * Stops the XML engine and sets the 'Finish Time' whilst also updating any final metrics before 
	 * making a final save to the XML file
	 */ 
	public void stopEngine()
	{
		objRootElement.setAttribute("Finish", returnDate());
		updateTestMetrics();
		saveXMLFile();
		Library.sleep(1000);
		generateHtml();
	}

	/**
	 * Generate HTML Report with the same XML file name
	 * 
	 */
	
	private void generateHtml() {
		{
			try
		        {
		            TransformerFactory tFactory = TransformerFactory.newInstance();

		            Source xslDoc = new StreamSource(objDefaultOutPutDirectory+"\\defaultStyleSheet.xsl");
		            Source xmlDoc = new StreamSource(objDefaultOutPutDirectory+"\\"+reportname+"_Results.xml");
		            

		            String outputFileName = objDefaultOutPutDirectory+"\\"+reportname+"_Results.html";
		            OutputStream htmlFile = new FileOutputStream(outputFileName);
		            

		            Transformer transformer = tFactory.newTransformer(xslDoc);
		            transformer.transform(xmlDoc, new StreamResult(htmlFile));
		            
		        }
		        catch(Exception e)
		        {
		            e.printStackTrace();
		        }
		    }
		}		
	

	/**
	 * Gets a time stamp which is then added to the XML file to give an indication of when the the test started and finished.
	 * @return strReturnDate	String - A formated string of current time and date.
	 */
	private static String returnDate()
	{
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date).toString();
	}

} 