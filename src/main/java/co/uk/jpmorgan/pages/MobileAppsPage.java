package co.uk.jpmorgan.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class MobileAppsPage {
	
	WebDriver driver;
	
	public MobileAppsPage(){
		this.driver=driver;
	}

	@FindBy(xpath="//*[@id='bodyContent']/p[1]")
	private WebElement appsText;
	public boolean verifyPage() {
		
		if(appsText.getText().contains("Discover apps from J.P. Morgan that give clients access to their accounts from anywhere they can access the Internet, including iPhone® and iPad"))
		{	
			return true;
		}
		return false;
		
	}

}
