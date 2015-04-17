package co.uk.jpmorgan;



import java.net.MalformedURLException;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import co.uk.jpmorgan.lib.Library;
import co.uk.jpmorgan.pages.ContactusPage;
import co.uk.jpmorgan.pages.LandingPage;
import co.uk.jpmorgan.pages.MobileAppsPage;


public class SeleniumBase {
	
	public static WebDriver driver;
	public LandingPage landingpage;
	public ContactusPage contactusPage;
	public MobileAppsPage mobileAppsPage;
	public static Library library;
	public static String Message;
	public static int t=0,f=0;
	public static boolean DebugSwitch = true;
	
	/* create reporter object which will be used for reporting to Console */
    protected static final Logger LOGGER = Logger.getLogger("TEST");
	public WebDriver getDriver() throws MalformedURLException{
                    driver = new BrowserFactory().getDriver();
		initPageObjects();
        return driver;
	}
	
	public WebDriver getDriver(String device, String browser, boolean javascriptEnabled) throws MalformedURLException{
		driver = new BrowserFactory().getDriver(device, browser, javascriptEnabled);
		initPageObjects();
		LOGGER.fatal("Page Objects are Initialised");
		return driver;
	}
	
	public void initPageObjects(){
		landingpage = PageFactory.initElements(driver, LandingPage.class);
		contactusPage = PageFactory.initElements(driver, ContactusPage.class);
		mobileAppsPage = PageFactory.initElements(driver, MobileAppsPage.class);
		library = new Library(driver);
	}
}

