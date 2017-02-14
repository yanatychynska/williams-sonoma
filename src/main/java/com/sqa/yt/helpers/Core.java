/**
 * File Name: Core.java<br>
 * Tychynska, Yana<br>
 * Java Boot Camp Exercise<br>
 * Instructor: Jean-francois Nepton<br>
 * Created: Feb 11, 2017
 */
package com.sqa.yt.helpers;

/**
 * Core //ADDD (description of class)
 * <p>
 * //ADDD (description of core fields)
 * <p>
 * //ADDD (description of core methods)
 *
 * @author Tychynska, Yana
 * @version 1.0.0
 * @since 1.0
 */
public class Core {

	public boolean addProp(String key, String value) {
		return AutoBasics.addProperty(key, value);
	}

	public int getInt(String name) {
		return AutoBasics.getInt(name);
	}

	public String getPop(String name) {
		return AutoBasics.getProp(name);
	}
}
