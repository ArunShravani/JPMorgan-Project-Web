package co.uk.jpmorgan;

import org.testng.annotations.Test;

import co.uk.jpmorgan.report.MyReport;

public class SmallTest extends BaseAcceptance{
	
	@Test
	public void contactTc1(){
		landingpage.selectContact();
		MyReport.isTrue(contactusPage.verifyPage(),"Contact Page Test");
	}

	@Test
	public void mobileAppPageTc2(){
		landingpage.selectMobileApps();
		MyReport.isTrue(mobileAppsPage.verifyPage(),"MobileAppsPage Page Test");
	}
}
