package co.uk.jpmorgan;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpUtils;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Rotatable;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import co.uk.jpmorgan.config.TestConfig;
import co.uk.jpmorgan.lib.Library;
import co.uk.jpmorgan.lib.MailSender;
import co.uk.jpmorgan.report.MyReport;



public class BaseAcceptance extends SeleniumBase{
	
	public static String chart;
	/* create Test object which will be used for reporting to Console */
    protected static final Logger LOGGER = Logger.getLogger("TEST");
	@BeforeClass
	public void BeforeTestsuit() throws MalformedURLException{
		super.getDriver();
		Message="Test started";
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		MyReport.startReporter("JpMorgan_Test","JPMorgan Test in "+TestConfig.getBrowser());
		LOGGER.info("Driver Initialisation done and Reporting service Started");
	}
	@AfterClass
	public void afterTestSuit(){
		MyReport.closeReports();
		driver.quit();
		email();
		LOGGER.info("Reporting service Stoped");
			}

	@BeforeMethod(alwaysRun=true)
	public void setupBase() throws MalformedURLException {
		driver.get("http://www.jpmorgan.com");
		LOGGER.info("Browser stared  "+TestConfig.getBrowser());
	}
	
	@AfterMethod(alwaysRun=true)
	public void teardownBase(ITestResult result) {
		try{
			Message = result.getMethod().getMethodName();
		if(!result.isSuccess())
		{
			MyReport.isTrue(result.isSuccess(), Message);
		}
		}catch(Exception e)
		{
			LOGGER.info("Error"+e.getMessage());
		}
		try{
			captureScreen(result.getMethod().getMethodName());
		}catch(Exception e)
		{
			LOGGER.fatal("screenshot not captured");
		}
		driver.manage().deleteAllCookies();
		LOGGER.info("All Cookies are deleted");
		Message="Test faild";
	}

	@AfterMethod(groups = "requiresMocks", alwaysRun=true)
	public void AllUsers() {
		
	}

    public void sleep(int timeout){
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    /**
     * Method to assert that branding elements are present in page
     * TODO: add more elements to BasePage class and make asserts more stringent
     */
    public void assertBaseElements(){
//        assertTrue(PageHeader.isDisplayed());
//        assertTrue(PageFooter.isDisplayed());
    }

    public static void waitForElementVisible(WebElement element){
        WebDriverWait wait = new WebDriverWait(driver, 15);
        final WebElement el = element;
        wait.until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return el.isDisplayed();
            }
        });
    }

    public void rotate(ScreenOrientation orientation){

        if(driver instanceof RemoteWebDriver && !(driver instanceof Rotatable) ){
            Dimension portrait = new Dimension(480,800);
            Dimension landscape = new Dimension(800,480);
            switch (orientation){
                case PORTRAIT:
                    driver.manage().window().setSize(portrait);
                    break;
                case LANDSCAPE:
                    driver.manage().window().setSize(landscape);
                    break;
            }
        } else if(driver instanceof Rotatable){
            ((Rotatable) driver).rotate(orientation);
            sleep(1000);
        }
    }

    public static boolean isElementPresent(WebElement element){
        try {
            element.isDisplayed();
            return true;
        } catch (NoSuchElementException e){
            
        }
        return false;
    }
    public static void email(){
		MailSender mailSender=new MailSender("itsarunp4u@gmail.com","Selenium Webdriver Execution Report","Hi this is a test mail");
	}
    public static void captureScreen(String message) {

	    String path,browser;
	    browser =  TestConfig.getBrowser();
	    
	    
	    try {
	        //WebDriver augmentedDriver = new Augmenter().augment(driver);
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
    
    

}
