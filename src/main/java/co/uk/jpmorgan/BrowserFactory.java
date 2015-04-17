package co.uk.jpmorgan;



import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.net.URL;
import org.openqa.selenium.*;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.Rotatable;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.android.AndroidDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.RemoteWebDriver;


import co.uk.jpmorgan.config.BrowserEnum;
import co.uk.jpmorgan.config.DeviceType;
import co.uk.jpmorgan.config.TestConfig;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.WebClient;

public class BrowserFactory {
		WebDriver driver;
		public static String ANDROID_USER_AGENT = "Mozilla/5.0 (Linux; U; Android 4.0.3; ko-kr; LG-L160L Build/IML74K) AppleWebkit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30";
		public static String IPHONE_USER_AGENT = "Mozilla/5.0 (iPhone; U; CPU iPhone OS 3_0 like Mac OS X; en-us) AppleWebKit/528.18 (KHTML, like Gecko) Version/4.0 Mobile/7A341 Safari/528.16";
		public static BrowserVersion ANDROID_VERSION = new BrowserVersion(
				"Netscape",
				"5.0 (Linux; U; Android 4.0.3; ko-kr; LG-L160L Build/IML74K) AppleWebkit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30",
				ANDROID_USER_AGENT, 4);
		public static BrowserVersion IPHONE_VERSION = new BrowserVersion(
				"Netscape",
				"5.0 (iPhone; U; CPU iPhone OS 3_0 like Mac OS X; en-us) AppleWebKit/528.18 (KHTML, like Gecko) Version/4.0 Mobile/7A341 Safari/528.16",
				IPHONE_USER_AGENT, 3);
		public WebDriver getDriver() throws MalformedURLException {

			return getDriver(TestConfig.getDevice(), TestConfig.getBrowser(), true);
		}

		public WebDriver getDriver(String device, String browser,
				boolean javascriptEnabled) throws MalformedURLException {
			DeviceType deviceType = DeviceType.valueOf(device.toUpperCase());
			BrowserEnum browserEnum = BrowserEnum.valueOf(browser.toUpperCase());
			switch (deviceType){
			case DESKTOP:
				switch(browserEnum){
				case CHROME:
					driver = getChromeDriver(null, javascriptEnabled);
					break;
				case FIREFOX:
					driver = getFirefoxDriver(null, javascriptEnabled);
					System.out.println("desktop");
					break;
				case FIREFOX_REMOTE:
					driver = getRemoteFirefox(null, javascriptEnabled);
	                break;
				case HTMLUNIT:
					driver = getHtmlUnitDriver(null, javascriptEnabled);
				case IE:
					driver = getInternetExplorer(null, javascriptEnabled); 
					break;
				}break;
			case ANDROID_DEVICE:
				driver = new AndroidDriver(new URL("http://localhost:8080/wd/hub"),
						DesiredCapabilities.android());
				///add exact url
				break;
			case ANDROID:
				switch(browserEnum){
				case CHROME:
					driver = getChromeDriver(ANDROID_USER_AGENT, javascriptEnabled);
					break;
				case FIREFOX:
					driver = getFirefoxDriver(ANDROID_USER_AGENT, javascriptEnabled);
					break;
				case FIREFOX_REMOTE:
					driver = getRemoteFirefox(ANDROID_USER_AGENT, javascriptEnabled);
	                break;
				case HTMLUNIT:
					driver = getHtmlUnitDriver(ANDROID_VERSION, javascriptEnabled);
					break;
				}break;
			case IPHONE:
				switch(browserEnum){
				case CHROME:
					driver = getChromeDriver(IPHONE_USER_AGENT, javascriptEnabled);
					break;
				case FIREFOX:
					driver = getFirefoxDriver(IPHONE_USER_AGENT, javascriptEnabled);
					break;
				case FIREFOX_REMOTE:
					driver = getRemoteFirefox(IPHONE_USER_AGENT, javascriptEnabled);
	                break;
				case HTMLUNIT:
					driver = getHtmlUnitDriver(IPHONE_VERSION, javascriptEnabled);
					break;
				}break;
			default:
					break;
				
			}
			
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			if (driver instanceof RemoteWebDriver) {
				((RemoteWebDriver) driver).setLogLevel(Level.INFO);
			}
			if(TestConfig.getDevice().equalsIgnoreCase("DESKTOP"))
			{ 
				driver.manage().window().maximize();
				return driver;
			}
			if(driver instanceof RemoteWebDriver && !(driver instanceof Rotatable) ){
	            Dimension dim = new Dimension(480,800);
	            driver.manage().window().setSize(dim);
	        }
			return driver;
		}
//			switch (deviceType) {
//			case ANDROID_DEVICE:
//				driver = new AndroidDriver(new URL("http://localhost:8080/wd/hub"),
//						DesiredCapabilities.android());
//				System.setProperty("browser", "native");
//				break;
//			case ANDROID:
//				switch (browserEnum) {
//				case CHROME:
//					driver = getChromeDriver(ANDROID_USER_AGENT, javascriptEnabled);
//					break;
//				case FIREFOX:
//					driver = getFirefoxDriver(ANDROID_USER_AGENT, javascriptEnabled);
//					break;
//	            case FIREFOX_REMOTE:
//	                driver = getRemoteFirefox(ANDROID_USER_AGENT, javascriptEnabled);
//	                break;
//	            case HTMLUNIT:
		//			driver = getHtmlUnitDriver(ANDROID_VERSION, javascriptEnabled);
//					break;
//				}
//				break;
//			case DESKTOP:
//				switch (browserEnum) {
//				case CHROME:
//					driver = getChromeDriver(null, javascriptEnabled);
//					break;
//				case FIREFOX:
		//			driver = getFirefoxDriver(null, javascriptEnabled);
//					break;
//	            case FIREFOX_REMOTE:
//	                driver = getRemoteFirefox(null, javascriptEnabled);
//	                break;
//	            case HTMLUNIT:
//					driver = getHtmlUnitDriver(null, javascriptEnabled);
//					break;
//				}
//				break;
//			case IPHONE:
//				switch (browserEnum) {
//				case CHROME:
//					driver = getChromeDriver(IPHONE_USER_AGENT, javascriptEnabled);
//					break;
//				case FIREFOX:
//					driver = getFirefoxDriver(IPHONE_USER_AGENT, javascriptEnabled);
//					break;
//	            case FIREFOX_REMOTE:
//	                driver = getRemoteFirefox(IPHONE_USER_AGENT, javascriptEnabled);
//	                break;
//				case HTMLUNIT:
//					driver = getHtmlUnitDriver(IPHONE_VERSION, javascriptEnabled);
//					break;
//				}
//				break;
//			default:
//				break;
//			}
		
		private WebDriver getHtmlUnitDriver(BrowserVersion browserVersion,
				boolean javascriptEnabled) {
			// browserVersion = getVersionFor(emulation);
			// log.info("Browser Version used: " + emulation);
			if (null != browserVersion) {
				driver = new HtmlUnitDriver(browserVersion) {
					@Override
					protected WebClient modifyWebClient(WebClient client) {
						client.setCssErrorHandler(new SilentCssErrorHandler());
						return client;
					}
				};
			} else {
				driver = new HtmlUnitDriver() {
					@Override
					protected WebClient modifyWebClient(WebClient client) {
						client.setCssErrorHandler(new SilentCssErrorHandler());
						return client;
					}
				};
			}
			((HtmlUnitDriver) driver).setJavascriptEnabled(javascriptEnabled);
			return driver;

		}

		private WebDriver getFirefoxDriver(String userAgent,
				boolean javascriptEnabled) {
			return new FirefoxDriver(
					getFirefoxProfile(userAgent, javascriptEnabled));
		}

		private FirefoxProfile getFirefoxProfile(String userAgent,
				boolean javascriptEnabled) {
			FirefoxProfile profile = new FirefoxProfile();
			profile.setPreference("javascript.enabled", javascriptEnabled);
	        if (null != userAgent) {
				profile.setPreference("general.useragent.override", userAgent);
			}
			return profile;
		}

		private WebDriver getChromeDriver(String userAgent,
				boolean javascriptEnabled) {
			System.setProperty("webdriver.chrome.driver",
					"src/test/resources/driver/Chrome/chromedriver.exe");
			return new ChromeDriver(getChromeOptions(userAgent, javascriptEnabled));
		}

		private ChromeOptions getChromeOptions(String userAgent,
				boolean javascriptEnabled) {
			ChromeOptions opts = new ChromeOptions();

			if (null != userAgent) {
				opts.addArguments("user-agent=" + userAgent);
			}
			if (!javascriptEnabled) {
				opts.addArguments("disable-javascript");
			}
			return opts;
		}
		private WebDriver getInternetExplorer(String UserAgent,
				boolean javascriptEnabled) {
			System.setProperty("webdriver.ie.driver","src/test/resources/driver/IE/IEDriverServer.exe");
			return driver = new InternetExplorerDriver();
			
			}

	    private WebDriver getRemoteFirefox(String userAgent, boolean javascriptEnabled) {
	        try {
	            //log.info("Remote Firefox invoked");
	        	return new ScreenShotRemoteWebDriver(new URL((String) TestConfig.getSeleniumGridUrl()), getRemoteFirefoxCapabilities(userAgent, javascriptEnabled));
	        } catch (Exception e) {
	            throw new IllegalStateException("not able to get remote firefox", e);
	        }
	    }

	    private DesiredCapabilities getRemoteFirefoxCapabilities(String userAgent, boolean javascriptEnabled) {
	        DesiredCapabilities firefox = DesiredCapabilities.firefox();
	        firefox.setVersion("12.0");
	        firefox.setPlatform(Platform.LINUX);
	        firefox.setJavascriptEnabled(javascriptEnabled);
	        firefox.setCapability("maxInstances", "1");
	        firefox.setCapability("firefox_binary", "/usr/bin/firefox");
	        //if (null != userAgent) {
	        //    firefox.setPreference("general.useragent.override", userAgent);
	        //}
	        return firefox;
	    }

	    public static class ScreenShotRemoteWebDriver extends RemoteWebDriver implements TakesScreenshot {
	        public ScreenShotRemoteWebDriver(URL url, DesiredCapabilities dc) {
	            super(url, dc);
	        }

	        public <X> X getScreenshotAs(OutputType<X> target)
	                throws WebDriverException {
	            if ((Boolean) getCapabilities().getCapability(CapabilityType.TAKES_SCREENSHOT)) {
	                return target.convertFromBase64Png(execute(DriverCommand.SCREENSHOT).getValue().toString());
	            }
	            return null;
	        }
	    }

	}


