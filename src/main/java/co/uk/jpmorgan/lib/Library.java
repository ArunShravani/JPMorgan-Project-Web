package co.uk.jpmorgan.lib;



import static com.googlecode.charts4j.Color.BLACK;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.support.ui.Select;



import co.uk.jpmorgan.config.TestConfig;

import com.googlecode.charts4j.Color;
import com.googlecode.charts4j.GCharts;
import com.googlecode.charts4j.PieChart;
import com.googlecode.charts4j.Slice;




public class Library {
	WebDriver driver;
	public static String configlocation = "src/main/java/co/uk/rac/config/";
											
	public Library(WebDriver driver) {
		this.driver = driver;
	}

	public static Properties loadproperties(String filename)
	{    
		configlocation = "src/main/java/co/uk/rac/config/";
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
	
	public void timeout(int t)
	{
		
		driver.manage().timeouts().implicitlyWait(t, TimeUnit.SECONDS);
	}
	public void waitUntil(int time)
	{
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}

		
	}
	public void captureScreen(String message) {

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

	public  void captureScreen() {

	    String path,browser;
	    browser = TestConfig.getBrowser();
	    
	    
	    try {
	        WebDriver augmentedDriver = new Augmenter().augment(driver);
	        File fileName;
	        File source = ((TakesScreenshot)augmentedDriver).getScreenshotAs(OutputType.FILE);
	        fileName = new File("./target/screenshots/"+browser+Thread.currentThread().getStackTrace()[2].getMethodName()+".png");
	        FileUtils.copyFile(source,fileName); 
	    }
	    catch(IOException e) {
	        path = "Failed to capture screenshot: " + e.getMessage();
	    }


	}
	public static String findFolder(String folder)
	{
		  File actual = new File(".");
	        for( File f :findDirectories(actual) ){
	        	File[] f1 = findDirectories(f);
	        	for(File f2 : f1)
	        	{
	        		if(f2.getName().equals(folder))
	        		{
	        			return (f2.getAbsolutePath());
	        			
	        		}
//	        		else
//	        		{
//	        			
//	        			call();
//	        		}
	        		
	        	}
	           
	        }
	        return null;
	}
	public static File[] findDirectories(File root) { 
        return root.listFiles(new FileFilter() {
            public boolean accept(File f) {
                return f.isDirectory();
            }});
    }
	
	public static void sleep(int n) {
		try {
			Thread.sleep(n);
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
	}

	public static String piechart(int t , int f){
		Slice s1 = Slice.newSlice(t, Color.newColor("004411"), "Passed "+t, "PASS");
        Slice s2 = Slice.newSlice(f, Color.newColor("951800"), "Failed "+f, "FAILED");
        PieChart chart = GCharts.newPieChart(s1, s2);
        chart.setTitle("Build Health", BLACK, 16);
        chart.setSize(500, 200);
        chart.setThreeD(true);
        String url1 = chart.toURLString();
        //Logger.global.info(url1);
        //String expectedString =url; //"http://chart.apis.google.com/chart?cht=p3&chs=500x200&chts=000000,16&chd=e:TNTNTNGa&chtt=A+Better+Web&chco=CACACA,DF7417,951800,01A1DB&chdl=Apple|Mozilla|Google|Microsoft&chl=Safari|Firefox|Chrome|Internet+Explorer";
		return url1;
		
	}
	public static void addchart(String chart) {
		 try{
	            FileWriter fstream = new FileWriter("C:\\Users\\RACUAT\\workspace\\acceptance\\target\\qa-logs\\RAC_UAT_UKBD_Test_Results.html",true);
	            	            BufferedWriter fbw = new BufferedWriter(fstream);
	            fbw.write(TestConfig.reportaddFirst()+chart+TestConfig.reportaddLast());
	            fbw.newLine();
	            fbw.close();
	            System.out.println("Hi How are you");
	        }catch (Exception e) {
	            System.out.println("Error: " + e.getMessage());

	        }
		 sleep(1000);
		
	}
	}

