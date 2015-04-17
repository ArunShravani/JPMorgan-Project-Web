package co.uk.jpmorgan.config;



import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import co.uk.jpmorgan.lib.Library;

public class TestConfig {
	static Properties config;
	public static String browser;
	public static String getDevice() {
		config = loadproperties("TestConfig.properties");
		String device;
		device= config.getProperty("Device");
		return device;
	}

	public static String getBrowser() {
		browser=config.getProperty("Browser");
		System.out.println(browser);
		return browser;
	}

	public static String getSeleniumGridUrl() {
		
		return null;
	}
	public static String reportaddLast(){
		return config.getProperty("ChartLast");
	}
	public static String reportaddFirst(){
		return config.getProperty("ChartFirst");
	}
	public static  Properties loadproperties(String filename)
	{    
		String configlocation = "src/main/java/co/uk/jpmorgan/config/";
		Properties pro = new Properties();
	   try
		{
		  pro.load(new FileInputStream(configlocation+filename));
		}catch (IOException e)
		{
			System.out.println("file not found");
		}
		return pro;
	}
}
