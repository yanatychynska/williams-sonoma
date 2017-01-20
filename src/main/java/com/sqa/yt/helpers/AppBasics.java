/**
 * File Name: AppBasic.java<br>
 * Tychynska, Yana<br>
 * Java Boot Camp Exercise<br>
 * Instructor: Jean-francois Nepton<br>
 * Created: Jan 14, 2017
 */
package com.sqa.yt.helpers;

import java.util.*;

public class AppBasics {

	static Scanner scanner = new Scanner(System.in);

	public static void farewellUser(String userName, String appName) {
		System.out.println("Thank you for using the " + appName + "application");
		System.out.println("Good Bye " + userName);
	}

	public static String greetUserAndGetName(String appName) {
		System.out.println("Hello, Welcome to the " + appName + "App.");
		System.out.print("Could I please get your name?");
		return scanner.nextLine();
	}

	public static String requestInfoFromUser(String question) {
		String response;
		System.out.println(question + "");
		response = scanner.nextLine();
		return response;
	}

	public static int requestIntFromUser(String question) {
		return requestIntFromUser(question, 0, 0);
	}

	public static int requestIntFromUser(String question, int min, int max) {
		String input;
		int result = 0;
		boolean isNotValid = true;
		while (isNotValid) {
			try {
				System.out.println(question + "");
				input = scanner.nextLine();
				result = Integer.parseInt(input);
				if (min != 0 && max != 0) {
					if (result <= max && result >= min) {
						isNotValid = false;
					} else {
						throw new NumberNotInRangeException();
					}
				}
				isNotValid = false;
			} catch (NumberFormatException e) {
				// TODO Give an error message
				System.out.println("You have not entered a correct number value");
			} catch (NumberNotInRangeException e) {
				System.out.println("You have not entered a number within the range of" + min + "and" + max + ".");
			}
		}
		return result;
	}
}