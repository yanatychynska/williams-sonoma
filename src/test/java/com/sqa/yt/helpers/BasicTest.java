package com.sqa.yt.helpers;

import java.util.concurrent.*;

import org.apache.log4j.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.firefox.*;
import org.openqa.selenium.ie.*;
import org.openqa.selenium.safari.*;
import org.testng.annotations.*;

public class BasicTest extends Core {

	private String baseUrl;

	private WebDriver driver;

	private Logger log;

	public BasicTest(String baseUrl) {
		super();
		this.baseUrl = baseUrl;
		this.log = Logger.getLogger(BasicTest.class);
		this.log.info("Created BasicTest object through constructor");
	}

	public String baseUrl() {
		return this.baseUrl;
	}

	public WebDriver getdriver() {
		return this.driver;
	}

	public WebDriver getDriver() {
		return this.driver;
	}

	public Logger getLog() {
		return this.log;
	}

	public void setLog(Logger log) {
		this.log = log;
	}

	@BeforeMethod(groups = "chrome")
	public void setupChrome() {
		System.setProperty("webdriver.chrome.driver", "drivers/chromedriver");
		this.driver = new ChromeDriver();
		this.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		this.driver.get(this.baseUrl);
	}

	@BeforeMethod(groups = "firefox")
	public void setupFirefox() {
		this.driver = new FirefoxDriver();
		this.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		this.driver.get(this.baseUrl);
	}

	@BeforeMethod(enabled = false)
	public void setupIE() {
		System.setProperty("webdriver.ie.driver", "drivers/ie.exe");
		this.driver = new InternetExplorerDriver();
		this.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		this.driver.get(this.baseUrl);
	}

	@BeforeMethod(enabled = false)
	public void setupSafari() {
		this.driver = new SafariDriver();
		this.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		this.driver.get(this.baseUrl);
	}

	@AfterMethod(enabled = false)
	public void tearDown() {
		this.driver.quit();
	}
}
