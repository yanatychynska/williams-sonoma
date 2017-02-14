package com.sqa.yt.helpers;

import java.io.*;
import java.util.*;

import org.testng.*;
import org.testng.annotations.*;

public class PropertiesTest extends BasicTest {

	/**
	 *
	 */
	public PropertiesTest() {
		super("http://sqasolution.com");
	}

	@Test(dataProvider = "configData")
	public void addProperties(String key, String value) {
		addProp(key, value);
	}

	@DataProvider
	public Object[][] configData(String key, String value) {
		return new Object[][] { { "name", "Jeff" }, { "age", "34" },
				{ "address", "1234 Street" }, { "occupation", "engineer" },
				{ "company", "SQA Solutions" } };
	}

	@Test
	public void testPropertiesCreation() {
		Properties prop = new Properties();
		prop.setProperty("server-name", "yt-server");
		prop.setProperty("department-name", "engineering");
		prop.setProperty("name-server", "aa-server");
		prop.setProperty("ip-address", " 122.155.111.120");
		prop.setProperty("port", "9999");
		prop.setProperty("username", "anaana");
		prop.setProperty("password", "passs9876");
		AutoBasics.writeProps(prop, "my-config.properties");
	}

	@Test(enabled = false)
	public void testReadingAPropertieswithHelp() {
		// create the properties object
		String ip = AutoBasics.getProp("configs.properties", "ip-address");
		Assert.assertNotNull(ip, "104.135.101.120");
	}

	@Test(enabled = false)
	public void testReadingProperties() throws FileNotFoundException, IOException {
		// Create properties object
		Properties prop = new Properties();
		// Create an InputStream and set it to a new FileInputStream
		InputStream input = new FileInputStream("src/main/resources/config.properties");
		// Load the properties object with data from the properties file
		prop.load(input);
		// Read a property for ip-address;
		System.out.println("Config Properties");
		System.out.println("Server Name:" + prop.getProperty("name-server"));
		System.out.println("Department:" + prop.getProperty("department-name"));
		System.out.println("Port:" + prop.getProperty("port"));
		System.out.println("Ip Address:" + prop.getProperty("ip-address"));
		System.out.println("\tuser:" + prop.getProperty("username"));
		System.out.println("\tpass:" + prop.getProperty("password"));
	}

	@Test(enabled = false)
	public void testReadingPropertieswithHelp() {
		// create the properties object
		Properties prop = AutoBasics.readProps("configs.properties");
		Assert.assertNotNull(prop);
	}

	@Test(enabled = false)
	public void testWritingProperties() throws IOException {
		// Create the Properties object
		Properties prop = new Properties();
		// Set required properties
		prop.setProperty("department-name", "engineering");
		prop.setProperty("name-server", "aa-server");
		prop.setProperty("ip-address", " 122.155.111.120");
		prop.setProperty("port", "9999");
		prop.setProperty("username", "anaana");
		prop.setProperty("password", "passs9876");
		// save propertied file
		OutputStream output =
				new FileOutputStream("src/main/resources/dev-config.properties");
		// save properties file
		prop.store(output, "Development Properties");
	}
	// A) Create a static helper method which returns a properties object with a
	// file located in main resources (src/main/resources)
	// B) Create a static helper method which creates a file located in main
	// resources (src/main/resources) based on supplying properties object and
	// file name
}
