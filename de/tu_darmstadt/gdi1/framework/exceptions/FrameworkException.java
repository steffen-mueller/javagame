/**
 * 
 */
package de.tu_darmstadt.gdi1.framework.exceptions;

/**
 * @author f_m
 * This is the base class for all exceptions thrown by the framework
 */
public class FrameworkException extends Exception {

	/**
	 * 
	 */
	public FrameworkException() {
	}

	/**
	 * @param message a description of the exception.
	 */
	public FrameworkException(String message) {
		super(message);
	}

	public FrameworkException(String message, Throwable cause) {
		super(message, cause);
	}


}
