package co.uk.jpmorgan.pages;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import co.uk.jpmorgan.report.MyReport;

public class LandingPage {

	WebDriver driver;
	
	public LandingPage(WebDriver driver){
		this.driver = driver;
	}
	@FindBy(xpath="//div[@id='utilSearch']/ul/li[4]/a/span")
	private WebElement Contact;
	@FindBy(xpath="//div[@id='utilSearch']/ul/li[5]/a/span")
	private WebElement mobileApp;
	public void selectContact() {
		try{
			Contact.click();
		}catch(NoSuchElementException e)
		{
			MyReport.fail("ContactUs Page link Missing"+e.getMessage());
		}
	}
	
	public void selectMobileApps() {
		
		try{
			mobileApp.click();
		}catch(NoSuchElementException e)
		{
			MyReport.fail("Mobile apps Page link Missing"+e.getMessage());
		}
	}
	
	
}
