/**
 * File Name: AutoBasics.java<br>
 * Tychynska, Yana<br>
 * Java Boot Camp Exercise<br>
 * Instructor: Jean-francois Nepton<br>
 * Created: Feb 4, 2017
 */
package com.sqa.yt.helpers;

import java.io.*;
import java.util.*;
import java.util.NoSuchElementException;

import org.apache.commons.io.*;
import org.openqa.selenium.*;

/**
 * AutoBasics //ADDD (description of class)
 * <p>
 * //ADDD (description of core fields)
 * <p>
 * //ADDD (description of core methods)
 *
 * @author Tychynska, Yana
 * @version 1.0.0
 * @since 1.0
 */
public class AutoBasics {

	private static final String DEFAULT_CONFIG_FILE_NAME = null;

	/**
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean addProperty(String key, String value) {
		Properties prop = readProps("src/main/resources/" + DEFAULT_CONFIG_FILE_NAME);
		prop.setProperty(key, value);
		return writeProps(prop, DEFAULT_CONFIG_FILE_NAME);
	}

	public static int getInt(String key) {
		return getInt(DEFAULT_CONFIG_FILE_NAME, key);
	}

	/**
	 * @param string
	 * @param string2
	 * @return
	 */
	public static String getProp(String key) {
		return getProp(DEFAULT_CONFIG_FILE_NAME, key);
	}

	public static String getProp(String fileName, String key) {
		Properties prop = readProps(fileName);
		String keyValue = prop.getProperty(key);
		return keyValue;
	}

	public static boolean isElementPresent(WebDriver driver, By by) {
		try {
			driver.findElement(by);
		} catch (NoSuchElementException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static Properties readProps(String fileName) {
		// Create properties object
		Properties prop = new Properties();
		try {
			// Create an InputStream and set it to a new FileInputStream
			InputStream input =
					new FileInputStream("src/main/resources/config.properties");
			// Load the properties object with data from the properties file
			prop.load(input);
		} catch (FileNotFoundException e) {
			System.err
					.println("File" + fileName + "was not found in src/main/resources.");
			return null;
		} catch (IOException e) {
			System.out.println("File" + fileName + "encounters error while reading.");
			return null;
		}
		return prop;
	}

	public static boolean takeScreenShot(WebDriver driver, String fileName) {
		File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(src, new File("screenshots/" + fileName + ".png"));
		} catch (IOException e) {
			// greeting Auto-generated catch block
			return false;
		}
		return true;
	}

	public static boolean writeProps(Properties prop, String fileName) {
		try {
			FileOutputStream output =
					new FileOutputStream("src/main/resources/" + fileName);
			prop.store(output, "Config Properties");
		} catch (FileNotFoundException e) {
			System.err
					.println("File" + fileName + "was not found in src/main/resources.");
			return false;
		} catch (IOException e) {
			System.out.println("File" + fileName + "encounters error while reading.");
			return false;
		}
		return true;
	}

	private static int getInt(String fileName, String key) {
		String input = getProp(fileName, key);
		int result = 0;
		try {
			result = Integer.parseInt(input);
		} catch (NumberFormatException e) {
			System.err.println("Issue converting to int value:" + input);
		}
		return result;
	}
}
