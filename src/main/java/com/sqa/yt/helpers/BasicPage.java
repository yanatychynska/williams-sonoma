/**
 * File Name: BasicPage.java<br>
 * Tychynska, Yana<br>
 * Java Boot Camp Exercise<br>
 * Instructor: Jean-francois Nepton<br>
 * Created: Feb 4, 2017
 */
package com.sqa.yt.helpers;

import org.openqa.selenium.*;
import org.openqa.selenium.support.*;
import org.testng.log4testng.*;

/**
 * BasicPage //ADDD (description of class)
 * <p>
 * //ADDD (description of core fields)
 * <p>
 * //ADDD (description of core methods)
 *
 * @author Tychynska, Yana
 * @version 1.0.0
 * @since 1.0
 */
public class BasicPage {

	private WebDriver driver;

	private Logger log;

	public BasicPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
		this.getLog(Logger.getLogger(BasicPage.class));
	}

	/**
	 * @return
	 */
	public WebDriver getDriver() {
		return this.driver;
	}

	/**
	 * @return the log
	 */
	public Logger getLog() {
		return this.log;
	}

	/**
	 * @param log
	 *            the log to set
	 */
	public void getLog(Logger log) {
		this.log = log;
	}

	public boolean isElementPresent(By by) {
		return AutoBasics.isElementPresent(getDriver(), by);
	}

	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}

	public boolean takeScreenshot(String name) {
		return AutoBasics.takeScreenShot(getDriver(), name);
	}
}
