package co.uk.jpmorgan.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ContactusPage {

WebDriver driver;
	
	public ContactusPage(WebDriver driver){
		this.driver = driver;
	}
	@FindBy(xpath="//div[@id='bodyContent']/p[1]/strong/a")
	private WebElement text;

	//Media Contacts
	public boolean verifyPage() {
		if(text.getText().equalsIgnoreCase("Media Contacts"))
		{
			return true;
		}
		return false;
	}
}
