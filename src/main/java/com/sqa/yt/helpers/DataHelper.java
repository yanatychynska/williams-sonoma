package com.sqa.yt.helpers;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.regex.*;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

/**
 * DataHelper Class to handle reading data from different sources.
 *
 * @author Nepton, Jean-francois
 * @version 1.0.0
 * @since 1.0
 */
public class DataHelper {

	/**
	 * Method to read a database table and get data from it
	 *
	 * @param driverClassString
	 * @param databaseStringUrl
	 * @param username
	 * @param password
	 * @param tableName
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws DataTypesMismatchException
	 * @throws DataTypesCountException
	 * @throws DataTypesTypeException
	 */
	public static Object[][] evalDatabaseTable(String driverClassString,
			String databaseStringUrl, String username, String password, String tableName)
			throws ClassNotFoundException, SQLException, DataTypesMismatchException,
			DataTypesCountException, DataTypesTypeException {
		// Method calls overloaded method which sets no offset for col or row in
		// the case you wanted to offset your data retrieved based on a column
		// or row offset
		return evalDatabaseTable(driverClassString, databaseStringUrl, username, password,
				tableName, 0, 0, null);
	}

	/**
	 * Method to read database table with implementation for a row and column
	 * offset.
	 *
	 * @param driverClassString
	 * @param databaseStringUrl
	 * @param username
	 * @param password
	 * @param tableName
	 * @param rowOffset
	 * @param colOffset
	 * @param dataTypes
	 * @return
	 * @throws DataTypesMismatchException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws DataTypesCountException
	 * @throws DataTypesTypeException
	 */
	public static Object[][] evalDatabaseTable(String driverClassString,
			String databaseStringUrl, String username, String password, String tableName,
			int rowOffset, int colOffset, DataType[] dataTypes)
			throws DataTypesMismatchException, ClassNotFoundException, SQLException,
			DataTypesCountException, DataTypesTypeException {
		// 2D Array of Objects to hold data object to be returned
		Object[][] myData;
		// Collection to hold same data
		ArrayList<Object> myArrayData = new ArrayList<Object>();
		// Driver class check, (requires driver dependency)
		Class.forName(driverClassString);
		// Create Connection and set value to the static method getConnection
		// from DriverManager
		Connection dbconn =
				DriverManager.getConnection(databaseStringUrl, username, password);
		// Create Statement object and set its value to database connection by
		// calling createStatement instance method
		Statement stmt = dbconn.createStatement();
		// Create the ResultSet variable and set its value to the Statement
		// object's instance method executeQuery with a supplied SQL select
		// statement
		ResultSet rs = stmt.executeQuery("select * from " + tableName);
		// Create a int variable and set its value to the ResultSet object's
		// instance method getColumnCount() from getMetaData().
		// ResultSetMetaData rsmd = rs.getMetaData();
		int numOfColumns = rs.getMetaData().getColumnCount();
		// Check if any DataTypes are supplied
		if (dataTypes != null) {
			// Check that the number of DataTypes passed is equal to the number
			// columns in the database specified
			if (dataTypes.length != numOfColumns) {
				throw new DataTypesCountException();
			}
		}
		// Iteration count
		int curRow = 1;
		// Start and go on first row if it is present
		while (rs.next()) {
			// Checking for rowOffset
			if (curRow > rowOffset) {
				// Gather row data based on columns collecting
				Object[] rowData = new Object[numOfColumns - colOffset];
				for (int i = 0, j = colOffset; i < rowData.length; i++) {
					try {
						// Based on DataType go to correct case
						switch (dataTypes[i]) {
						// Convert to String if data from database data
						case STRING:
							rowData[i] = rs.getString(i + colOffset + 1);
							break;
						// Convert to int if data from database data
						case INT:
							rowData[i] = rs.getInt(i + colOffset + 1);
							break;
						// Convert to float if data from database data
						case FLOAT:
							rowData[i] = rs.getFloat(i + colOffset + 1);
							break;
						default:
							break;
						}
					} catch (Exception e) {
						// Throw exception if error occurs
						throw new DataTypesTypeException();
					}
				}
				// Add row data to collection
				myArrayData.add(rowData);
			}
			// Increase row count
			curRow++;
		}
		// Create array for first level based on items collected in collection
		myData = new Object[myArrayData.size()][];
		// Use a loop to iterate through array to add each row data as an array
		// (Creating the 2D Array needed)
		for (int i = 0; i < myData.length; i++) {
			// Adding the row data...
			myData[i] = (Object[]) myArrayData.get(i);
		}
		// myArrayData.toArray(myData);
		// Close all connections
		rs.close();
		stmt.close();
		dbconn.close();
		// Return data as arrays of the rows of arrays of columns data
		return myData;
	}

	/**
	 * Method to read an excel file in both the old format of excel and the
	 * newer one.
	 *
	 * @param fileLocation
	 * @param fileName
	 * @param hasLabels
	 * @return
	 * @throws InvalidExcelExtensionException
	 */
	public static Object[][] getExcelFileData(String fileLocation, String fileName,
			Boolean hasLabels) throws InvalidExcelExtensionException {
		// Use a variable to store the 2D Object;
		Object[][] resultsObject;
		// Separate the file name from the exchange
		String[] fileNameParts = fileName.split("[.]");
		// Extension becomes last part of the file after the period
		String extension = fileNameParts[fileNameParts.length - 1];
		// Collection of results which will be set to a collection of rows in
		// the excel file
		ArrayList<Object> results = null;
		// Check for the extension to be xlsx or newer Excel 2003+
		if (extension.equalsIgnoreCase("xlsx")) {
			// Call method to get results from a new type for excel document
			results = getNewExcelFileResults(fileLocation, fileName, hasLabels);
			// Check for the extension to be xls or old Excel -2003
		} else if (extension.equalsIgnoreCase("xls")) {
			// Call method to get results from a old type for excel document
			results = getOldExcelFileResults(fileLocation, fileName, hasLabels);
			// if extension is not one of these, through exception
		} else {
			throw new InvalidExcelExtensionException();
		}
		// Set the results object to an array of arrays with size set to the
		// amount of rows collected in results (the collection of rows)
		resultsObject = new Object[results.size()][];
		// Convert the results into the supplied array which is resultsObject
		results.toArray(resultsObject);
		// return the results object
		return resultsObject;
	}

	/**
	 * Overloaded method to read text file formatted in CSV style.
	 *
	 * @param fileName
	 * @return
	 */
	public static Object[][] getTextFileData(String fileName) {
		return getTextFileData("", fileName, TextFormat.CSV, false, null);
	}

	/**
	 * Overloaded method to read text file in various format styles.
	 *
	 * @param fileLocation
	 * @param fileName
	 * @param textFormat
	 * @return
	 */
	public static Object[][] getTextFileData(String fileLocation, String fileName,
			TextFormat textFormat) {
		return getTextFileData(fileLocation, fileName, textFormat, false, null);
	}

	/**
	 * Method to read text file in various format styles and also allows
	 * DataTypes to be specified
	 *
	 * @param fileLocation
	 * @param fileName
	 * @param textFormat
	 * @param hasLabels
	 * @param dataTypes
	 * @return
	 */
	public static Object[][] getTextFileData(String fileLocation, String fileName,
			TextFormat textFormat, Boolean hasLabels, DataType[] dataTypes) {
		Object[][] data;
		ArrayList<String> lines = openFileAndCollectData(fileLocation, fileName);
		switch (textFormat) {
		case CSV:
			data = parseCSVData(lines, hasLabels, dataTypes);
			break;
		case XML:
			data = parseXMLData(lines, hasLabels);
			break;
		case TAB:
			data = parseTabData(lines, hasLabels);
			break;
		case JSON:
			data = parseJSONData(lines, hasLabels);
			break;
		default:
			data = null;
			break;
		}
		return data;
	}

	/**
	 * Overloaded method to read text file in various format styles and also
	 * allows DataTypes to be specified and also setting no labels.
	 *
	 * @param fileLocation
	 * @param fileName
	 * @param textFormat
	 * @param dataTypes
	 * @return
	 */
	public static Object[][] getTextFileData(String fileLocation, String fileName,
			TextFormat textFormat, DataType[] dataTypes) {
		return getTextFileData(fileLocation, fileName, textFormat, false, dataTypes);
	}

	/**
	 * @param hasLabels
	 * @param newExcelFormatFile
	 * @param results
	 * @param workbook
	 * @param sheet
	 * @return
	 */
	private static ArrayList<Object> collectExcelData(Boolean hasLabels,
			InputStream newExcelFormatFile, ArrayList<Object> results, Workbook workbook,
			Sheet sheet) {
		try {
			Iterator<Row> rowIterator = sheet.iterator();
			if (hasLabels) {
				rowIterator.next();
			}
			while (rowIterator.hasNext()) {
				ArrayList<Object> rowData = new ArrayList<Object>();
				Row row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_BOOLEAN:
						System.out.print(cell.getBooleanCellValue() + "\t\t\t");
						rowData.add(cell.getBooleanCellValue());
						break;
					case Cell.CELL_TYPE_NUMERIC:
						System.out.print(cell.getNumericCellValue() + "\t\t\t");
						rowData.add(cell.getNumericCellValue());
						break;
					case Cell.CELL_TYPE_STRING:
						System.out.print(cell.getStringCellValue() + "\t\t\t");
						rowData.add(cell.getStringCellValue());
						break;
					}
				}
				Object[] rowDataObject = new Object[rowData.size()];
				rowData.toArray(rowDataObject);
				results.add(rowDataObject);
				System.out.println("");
			}
			newExcelFormatFile.close();
			FileOutputStream out = new FileOutputStream(
					new File("src/main/resources/excel-output.xlsx"));
			workbook.write(out);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return results;
	}

	/**
	 * @param parameter
	 * @param dataType
	 * @return
	 * @throws BooleanFormatException
	 * @throws CharacterCountFormatException
	 */
	private static Object convertDataType(String parameter, DataType dataType)
			throws BooleanFormatException, CharacterCountFormatException {
		Object data = null;
		try {
			switch (dataType) {
			case STRING:
				data = parameter;
				break;
			case CHAR:
				if (parameter.length() > 1) {
					throw new CharacterCountFormatException();
				}
				data = parameter.charAt(0);
				break;
			case DOUBLE:
				data = Double.parseDouble(parameter);
			case FLOAT:
				data = Float.parseFloat(parameter);
			case INT:
				data = Integer.parseInt(parameter);
			case BOOLEAN:
				if (parameter.equalsIgnoreCase("true")
						| parameter.equalsIgnoreCase("false")) {
					data = Boolean.parseBoolean(parameter);
				} else {
					throw new BooleanFormatException();
				}
			default:
				break;
			}
		} catch (NumberFormatException | BooleanFormatException
				| CharacterCountFormatException e) {
			System.out.println(
					"Converting data.. to... " + dataType + "(" + parameter + ")");
		}
		return data;
	}

	/**
	 * Private method to get data from a new type of Excel file.
	 *
	 * @param fileLocation
	 * @param fileName
	 * @param hasLabels
	 * @return
	 * @throws IOException
	 */
	private static ArrayList<Object> getNewExcelFileResults(String fileLocation,
			String fileName, Boolean hasLabels) {
		ArrayList<Object> data = null;
		String fullFilePath = fileLocation + fileName;
		InputStream newExcelFormatFile;
		try {
			newExcelFormatFile = new FileInputStream(new File(fullFilePath));
			ArrayList<Object> results = new ArrayList<Object>();
			Workbook workbook = new XSSFWorkbook(newExcelFormatFile);
			Sheet sheet = workbook.getSheetAt(0);
			data = collectExcelData(hasLabels, newExcelFormatFile, results, workbook,
					sheet);
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found");
		} catch (IOException e) {
			System.out.println("File Read Error");
			e.printStackTrace();
		}
		return data;
	}

	/**
	 * Private method to get data from an old type of Excel file.
	 *
	 * @param fileLocation
	 * @param fileName
	 * @param hasLabels
	 * @return
	 */
	private static ArrayList<Object> getOldExcelFileResults(String fileLocation,
			String fileName, Boolean hasLabels) {
		ArrayList<Object> data = null;
		String fullFilePath = fileLocation + fileName;
		InputStream newExcelFormatFile;
		try {
			newExcelFormatFile = new FileInputStream(new File(fullFilePath));
			ArrayList<Object> results = new ArrayList<Object>();
			Workbook workbook = new HSSFWorkbook(newExcelFormatFile);
			Sheet sheet = workbook.getSheetAt(0);
			data = collectExcelData(hasLabels, newExcelFormatFile, results, workbook,
					sheet);
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found");
		} catch (IOException e) {
			System.out.println("File Read Error");
			e.printStackTrace();
		}
		return data;
	}

	/**
	 * Private method to open a text file and collect data lines as an ArrayList
	 * collection of lines.
	 *
	 * @param fileLocation
	 * @param fileName
	 * @return
	 */
	private static ArrayList<String> openFileAndCollectData(String fileLocation,
			String fileName) {
		String fullFilePath = fileLocation + fileName;
		ArrayList<String> dataLines = new ArrayList<String>();
		try {
			FileReader fileReader = new FileReader(fullFilePath);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line = bufferedReader.readLine();
			while (line != null) {
				dataLines.add(line);
				line = bufferedReader.readLine();
			}
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fullFilePath + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fullFilePath + "'");
		}
		return dataLines;
	}

	/**
	 * Private method to get parse data formatted in CSV style.
	 *
	 * @param lines
	 * @param hasLabels
	 * @param dataTypes
	 * @return
	 */
	private static Object[][] parseCSVData(ArrayList<String> lines, boolean hasLabels,
			DataType[] dataTypes) {
		ArrayList<Object> results = new ArrayList<Object>();
		if (hasLabels) {
			lines.remove(0);
		}
		String pattern = "(,*)([a-zA-Z0-9\\s-]+)(,*)";
		Pattern r = Pattern.compile(pattern);
		for (int i = 0; i < lines.size(); i++) {
			int curDataType = 0;
			ArrayList<Object> curMatches = new ArrayList<Object>();
			Matcher m = r.matcher(lines.get(i));
			while (m.find()) {
				if (dataTypes.length > 0) {
					try {
						curMatches.add(convertDataType(m.group(2).trim(),
								dataTypes[curDataType]));
					} catch (Exception e) {
						// System.out.println("DataTypes provided do not match
						// parsed data results.");
					}
				} else {
					curMatches.add(m.group(2).trim());
				}
				curDataType++;
			}
			Object[] resultsObj = new Object[curMatches.size()];
			curMatches.toArray(resultsObj);
			results.add(resultsObj);
		}
		Object[][] resultsObj = new Object[results.size()][];
		results.toArray(resultsObj);
		return resultsObj;
	}

	/**
	 * Private method to get parse data formatted in JSON style.
	 *
	 * @param lines
	 * @param hasLabels
	 * @return
	 */
	private static Object[][] parseJSONData(ArrayList<String> lines, Boolean hasLabels) {
		// TODO Create an implementation to handle JSON formatted documents
		return null;
	}

	/**
	 * Private method to get parse data formatted in Tab style.
	 *
	 * @param lines
	 * @param hasLabels
	 * @return
	 */
	private static Object[][] parseTabData(ArrayList<String> lines, Boolean hasLabels) {
		// TODO Create an implementation to handle Tab formatted documents
		return null;
	}

	/**
	 * Private method to get parse data formatted in XML style.
	 *
	 * @param lines
	 * @param hasLabels
	 * @return
	 */
	private static Object[][] parseXMLData(ArrayList<String> lines, Boolean hasLabels) {
		// TODO Create an implementation to handle XML formatted documents
		return null;
	}
}
