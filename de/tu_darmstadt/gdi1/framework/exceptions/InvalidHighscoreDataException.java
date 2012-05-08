package de.tu_darmstadt.gdi1.framework.exceptions;

/**
 * This Exception is thrown when we try to load a highscore list
 * with damaged or incompatible data
 * 
 * @author jan
 */
public class InvalidHighscoreDataException extends Exception {
	
	public InvalidHighscoreDataException(Throwable e) {
		super(e);
	}

}
