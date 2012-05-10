package de.tu_darmstadt.gdi1.framework.interfaces;

import java.io.Serializable;

/**
 * This interface represents a board element (like "grassland", "switch", "crate", "player", "enemy").
 * Multiple board elements can be on a field of the board.
 * 
 * An implementation of this interface is <b>not</b> part of the framework, as this is highly game-specific.
 * 
 * You probably want to create a base "board element" class implementing this interface, extend all your
 * board elements from it and use that class as the parameter for the parametrized classes of the framework. 
 * @author Jan
 *
 */
public interface IBoardElement extends Cloneable, Serializable {


	/**
	 * This has to return a new game element with identical properties.
	 * This must be implemented correctly in order for StepManager to work!
	 * If you mess it up, you will be hunting down those weird bugs for a long time!
	 * @return an <b>independent</b> copy of the game element
	 */
	public IBoardElement clone();
}
