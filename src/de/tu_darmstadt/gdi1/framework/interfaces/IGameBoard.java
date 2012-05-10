package de.tu_darmstadt.gdi1.framework.interfaces;

import java.io.Serializable;
import java.util.List;

import de.tu_darmstadt.gdi1.framework.model.GameBoard;
import de.tu_darmstadt.gdi1.framework.model.StepManager;
import de.tu_darmstadt.gdi1.framework.utils.Point;


/**
 * The interface to anything representing a board of the game.
 * It is a extension with setter-methods of the {@link IBoard} interface.
 *
 * <br>
 * <br>
 * This is implemented by @link {@link GameBoard} and @link {@link StepManager}.
 * @see IBoard
 * @author jonas
 * @param <E> The type of BoardElements used
 */
public interface IGameBoard<E extends IBoardElement> extends IBoard<E>, Serializable {

	/**
	 * {@inheritDoc}
	 */
	IGameBoard<E> clone();
	

	/**
	 * Set the list of elements for a specified board field.
	 * 
	 * @param x
	 *            the x coordinate of the board field
	 * @param y
	 *            the y coordinate of the board field
	 * @param elementList
	 *            the list of BoardElements which shall be placed there, may not be null.
	 */
	void setElements(int x, int y, List<E> elementList);

	/**
	 * Set the list of elements for a specified board field.
	 * 
	 * @param coord
	 *            the coordinates of the board field
	 * @param elementList
	 *            the list of BoardElements which shall be placed there, may not be null.
	 */
	void setElements(Point coord, List<E> elementList);	
	
}
