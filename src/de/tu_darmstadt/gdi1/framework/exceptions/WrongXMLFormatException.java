/**
 * 
 */
package de.tu_darmstadt.gdi1.framework.exceptions;

/**
 * Class representing an exception that occurs if the given XML File
 * to build an menu is not well-formed.
 * 
 * @author f_m
 */
public class WrongXMLFormatException extends FrameworkException {

	/**
	 * @param message
	 */
	public WrongXMLFormatException(String message) {
		super(message);
	}

}
