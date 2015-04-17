package co.uk.jpmorgan.report;


import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.testng.Assert;

import co.uk.jpmorgan.SeleniumBase;
import co.uk.jpmorgan.config.TestConfig;

public class MyReport extends SeleniumBase{
	
	static testReporter Report;
	protected static final Logger LOGGER = Logger.getLogger("REPORT");
	public static void startReporter(String reportname, String testrun)
	{
		Report = new testReporter(reportname);
		Report.addNewIteration(testrun);
	}
	
	public static void equals(Object exp,Object act,String message)
	{
		String testcaseName = Thread.currentThread().getStackTrace()[2].getMethodName();
		if(exp.toString().equals(act.toString()))
		{
			Report.reportPass(testcaseName, exp.toString(), act.toString(), message);
		}
		else
		{
			Report.reportFail(testcaseName, exp.toString(), act.toString(), message);
			captureScreen(testcaseName);
		}
	}
	public static void isTrue(boolean value, String Expected, String Actual, String message)
	{
	
		String testcaseName = Thread.currentThread().getStackTrace()[2].getMethodName();
		if(value)
		{
			t++;
			Report.reportPass(testcaseName, Expected, Actual, message);
		}
		else
		{	f++;
			Report.reportFail(testcaseName, Expected, Actual, message);
			
		}
	}
	public static void isTrue(boolean value,String message)
	{
	
		String testcaseName = Thread.currentThread().getStackTrace()[2].getMethodName();
		if(value)
		{
			t++;
			Report.reportPass(testcaseName,String.valueOf(value),"true", message);
		}
		else
		{	f++;
			Report.reportFail(testcaseName, String.valueOf(value), "true", message);
			
		}
	}
	public static void isTrue(boolean value)
	{
		String testcaseName = Thread.currentThread().getStackTrace()[2].getMethodName();
		
		if(value)
		{
			Report.reportPass(testcaseName,String.valueOf(value),"true", "");
			t=t+1;
		}
		else
		{
			Report.reportFail(testcaseName, String.valueOf(value), "true", "");
			captureScreen(testcaseName);
			f=f+1;
		}
	}
	public static void pass(String message)
	{
		String testcaseName = Thread.currentThread().getStackTrace()[2].getMethodName();
		t++;
		Report.reportPass(testcaseName,"","", message);
		
	}
	
	public static void fail(String message)
	{
		f=f+1;
		String testcaseName = Thread.currentThread().getStackTrace()[1].getMethodName();
		Report.reportFail(testcaseName,"","", message);
		captureScreen(testcaseName);
	}
	public static void failAndExit(String message)
	{
		f=f+1;
		String testcaseName = Thread.currentThread().getStackTrace()[3].getMethodName();
		Report.reportFailAndExitTest(testcaseName,"","", message);
		captureScreen(testcaseName);
		
	}
	
	public static void equals(Object exp,Object act)
	{
		String testcaseName = Thread.currentThread().getStackTrace()[2].getMethodName();
		if(exp.toString().equals(act.toString()))
		{
			t++;
			Report.reportPass(testcaseName, exp.toString(), act.toString(), "");
		}
		else
		{
			f++;
			Report.reportFail(testcaseName, exp.toString(), act.toString(), "");
			captureScreen(testcaseName);
		}
	}
	public static void warning(String message)
	{
		String testcaseName = Thread.currentThread().getStackTrace()[2].getMethodName();
		Report.reportInfo(testcaseName+message);
	}
	
	public static void Log(String message)
	{
		String testcaseName = Thread.currentThread().getStackTrace()[3].getMethodName();
		Report.reportDebug(testcaseName+message);
	}
	
	public static void Exception(String message)
	{
		String testcaseName = Thread.currentThread().getStackTrace()[2].getMethodName();
		Report.reportDebug(testcaseName+message);
		Assert.fail(message);
		captureScreen(testcaseName);
		
	}
	
	public static void closeReports()
	{
		Report.stopReporter();
	}
	public static void captureScreen(String message) {

	    String path,browser;
	    browser =  TestConfig.getBrowser();
	    
	    
	    try {
	        WebDriver augmentedDriver = new Augmenter().augment(driver);
	        File fileName;
	        File source = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
	        fileName = new File("./target/screenshots/"+browser+message+".png");
	        if(fileName.exists())
	        {
	        	fileName.delete();
	        }
	        FileUtils.copyFile(source,fileName); 
	    }
	    catch(IOException e) {
	        path = "Failed to capture screenshot: " + e.getMessage();}
	    }
	
	
	

}
