package de.tu_darmstadt.gdi1.framework.exceptions;

/**
 * This Exception is thrown when we try to parse a level file or savegame
 * which has invalid level data.
 * 
 * @author jonas
 */
public class InvalidLevelDataException extends Exception {

	/** serial-uid. */
	private static final long serialVersionUID = -231724725741330928L;

	/**
	 * gives the message to the Exception-constructor.
	 * @param errorMessage
	 * 				describes the error which occurred
	 */
	public InvalidLevelDataException(final String errorMessage) {
		super(errorMessage);
	}

	/** @see Exception#Exception(String, Throwable) */
	public InvalidLevelDataException(String exceptionMessage, Throwable cause) {
		super(exceptionMessage, cause);
	}
}
