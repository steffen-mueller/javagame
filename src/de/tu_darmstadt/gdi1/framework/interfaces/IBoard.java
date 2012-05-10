package de.tu_darmstadt.gdi1.framework.interfaces;

import java.util.List;

import de.tu_darmstadt.gdi1.framework.utils.Point;


/**
 * This is the interface implemented by game board that allows a {@link IBoardPanel}
 * to get all information about a Board which is required to paint it.<br>
 * <br>
 * This interface is extended by IGameBoard and is implemented by GameBoad and StepManager.
 * 
 * @author jonas
 * 
 * @param <E> The type of BoardElements used
 *
 */
public interface IBoard<E extends IBoardElement> extends Cloneable {

	
	/** creates an independent copy of the board, which references copies of the elements.
	 * @return a copy of the board
	 */
	IBoard<E> clone(); 
	
	/**
	 * Get the elements at the given field of the board.
	 * 
	 * @param x
	 *            the x coordinate of the field
	 * @param y
	 *            the y coordinate of the field
	 * @return a list of BoardElements at the field
	 */
	List<E> getElements(int x, int y);

	/**
	 * Get the elements at the given field of the board.
	 * 
	 * @param coord
	 *            the coordinates of the field
	 * @return the list of BoardElements on the given field of the board
	 */
	List<E> getElements(Point coord);
	
	/**
	 * Get the width of the board.
	 * 
	 * @return dimension on the x-axis
	 */
	int getWidth();

	/**
	 * Get the height of the board.
	 * 
	 * @return dimension on the y-axis
	 */
	int getHeight();
	
	/**
	 * Compares this board to another.
	 * Warning: If ANY of your elements does not support equals(), you will not get very useful results.
	 * Remember that there is still the default Object.equals(Object), so if you mess up parameters,
	 * you will always get "false" unless you compare an object to itself.
	 * 
	 * @param other the other board
	 * @return true if the boards are equal, i.e. same size and fields have equal lists
	 */
	boolean equals(IBoard<E> other);
	
	/**
	 * checks if the given coordinates are valid or out of bound.
	 * 
	 * @param x
	 *            x-coordinate
	 * @param y
	 *            y-coordinate
	 * @return true if x and y are valid coordinates, otherwise false
	 */
	boolean checkCoordinates(int x, int y);

	/**
	 * checks if the given coordinates are valid or out of bound.
	 * 
	 * @param position
	 *            coordinates to check
	 * @return true if the position has valid coordinates, otherwise false
	 */
	boolean checkCoordinates(Point position);
	
}

