package de.tu_darmstadt.gdi1.framework.exceptions;

/**
 * A FrameworkError indicates that the framework has been brought to an abnormal
 * condition that should never happen under normal circumstances.
 * 
 * You should not try to catch this error but to avoid it. 
 * 
 * The getCause() and getMessage() methods should give you a hint what went wrong.
 * 
 * @author f_m
 */
public class FrameworkError extends Error {

	/**
	 * serial-uid.
	 */
	private static final long serialVersionUID = -1220844823396059365L;

	/**
	 * 
	 */
	public FrameworkError() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public FrameworkError(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public FrameworkError(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public FrameworkError(Throwable cause) {
		super(cause);
	}


}
