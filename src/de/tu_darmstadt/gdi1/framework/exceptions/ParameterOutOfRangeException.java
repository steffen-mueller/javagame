/**
 * 
 */
package de.tu_darmstadt.gdi1.framework.exceptions;

/**
 * One (or more) given parameters were out of the expected range. Check 
 * getMessage() for details.
 * 
 * @author f_m
 */
public class ParameterOutOfRangeException extends FrameworkException {

	/**
	 * @param message
	 */
	public ParameterOutOfRangeException(String message) {
		super(message);
	}
	
	public ParameterOutOfRangeException(String message, Throwable cause) {
		super(message, cause);
	}

}
