package de.tu_darmstadt.gdi1.framework.interfaces;

import de.tu_darmstadt.gdi1.framework.exceptions.ParameterOutOfRangeException;


/**
 * 
 * The interface for something displaying an {@link IBoard}.
 * Typically, this will be a JPanel.
 * <br>
 * To be able to map a Collection of {@link IBoardElement} to a graphical representation,
 * a class implementing this interface should know an instance of {@link IGameView} to call
 * @link IGameView#getComponentForList(java.util.List, utils.Point) on it.
 * <br>
 * <br>
 * The framework class BoardPanel implements this interface.
 *  
 * @author jonas
 * @param <E> The type of BoardElements used
 */
public interface IBoardPanel<E extends IBoardElement> {

	/**
	 * Display the given board.
	 * @param aBoard
	 * 		the board to display
	 * @throws ParameterOutOfRangeException if the ImageIcon to set is null or the images doesn't have the same size.
	 */
	void paintBoard(IBoard<E> aBoard) throws ParameterOutOfRangeException;
	
}
