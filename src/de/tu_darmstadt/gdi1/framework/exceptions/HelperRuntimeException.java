package de.tu_darmstadt.gdi1.framework.exceptions;

/**
 * Helper Class to identify RuntimeExceptions from the framework.
 * For more information, check the comments in the handleEvent method.
 * @author f_m
 */
public class HelperRuntimeException extends RuntimeException {
	private ParameterOutOfRangeException e = null;

	/**  serial. */
	private static final long serialVersionUID = 6453828197021827136L;

	/**
	 * creates a new HelperRuntimeException, wrapping a ParameterOutOfRangeException.
	 * @param exception the exception to be wrapped
	 */
	public HelperRuntimeException(ParameterOutOfRangeException exception) {
		super("Hum, you shouldn't see this Exception. Nobody should. This is embarrassing, " +
				"go on...blame the author, you'll find out how to contact him...");
		e = exception;
	}
	
	/**
	 * @return the exception that was wrapped in the constructor
	 */
	public ParameterOutOfRangeException getRealException() {
		return e;
	}
	
}

