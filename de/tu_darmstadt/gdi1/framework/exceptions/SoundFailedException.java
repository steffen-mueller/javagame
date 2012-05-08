/**
 * 
 */
package de.tu_darmstadt.gdi1.framework.exceptions;

import javax.sound.sampled.LineUnavailableException;

/**
 * A framework exception that wraps a
 * {@link javax.sound.sampled.LineUnavailableException} to indicate that sound failed to play.
 * 
 * This exception only tells about a single attempt to play a sound, it doesn't
 * indicate any information about further attempts, but it is
 * very likely that, if one sound fails playing, other sounds might fail too.
 * 
 * @author Jan
 *
 */
public class SoundFailedException extends FrameworkException {

	/**
	 * the wrapped exception
	 */
	private LineUnavailableException e;
	
	/**
	 * Wraps the exception
	 * @param e exception to wrap
	 */
	public SoundFailedException(LineUnavailableException e) {
		super(e.getMessage(), e.getCause());
		this.e = e;
	}
	
	/**
	 * @return the original exception
	 */
	public LineUnavailableException getRealException() {
		return e;
	}

}
