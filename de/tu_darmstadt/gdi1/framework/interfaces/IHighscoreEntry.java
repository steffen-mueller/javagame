package de.tu_darmstadt.gdi1.framework.interfaces;

import java.io.Serializable;

/**
 * This interface represents an entry for the highscore table.
 * A highscore entry can be compared for ordering and serialized for saving.
 * It consists of the column values, for example the date, name, and score.
 * 
 * You need to implement this class yourself, as the fields used differ widely
 * from game to game.
 * The column titles are to be set in the @link {@link IHighscoreStore} used by you.
 * 
 * @author Jan
 *
 */
public interface IHighscoreEntry extends Comparable<IHighscoreEntry>, Serializable {
	
	/**
	 * @return the values of the highscore entry (columns of the highscore table)
	 */
	public String[] getValues();
	
}
