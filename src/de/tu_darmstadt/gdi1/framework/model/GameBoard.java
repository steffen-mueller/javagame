package de.tu_darmstadt.gdi1.framework.model;


import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import de.tu_darmstadt.gdi1.framework.interfaces.IBoard;
import de.tu_darmstadt.gdi1.framework.interfaces.IBoardElement;
import de.tu_darmstadt.gdi1.framework.interfaces.IGameBoard;
import de.tu_darmstadt.gdi1.framework.utils.Point;


/**
 * Implaments a game board consisting of multiple fields.
 * Each field can have multiple game elements on it.
 * Consider using StepManager instead if you want undo/redo.
 * @author Jan
 * @param <E> The type of BoardElements used
 */
public class GameBoard<E extends IBoardElement> implements Cloneable, Serializable, IGameBoard<E> {
	
	private static final long serialVersionUID = 7516403135576231793L;
	
	/**
	 * Board width in fields.
	 */
	private int sizex;
	
	/**
	 * Board height in fields.
	 */
	private int sizey;
	
	/**
	 * Array representing the board, each entry is the list
	 * of game elements on one of the fields.
	 * Address via board[column][row].
	 */
	private List<E>[][] board;
	
	/**
	 * Creates an empty game board.
	 * @param sizex horizontal size of the board
	 * @param sizey vertical size of the board	 */
	@SuppressWarnings("unchecked")
	public GameBoard(final int sizex, final int sizey) {
		this.sizex = sizex;
		this.sizey = sizey;
		board = (LinkedList<E>[][]) new LinkedList[sizex][sizey];
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				board[i][j] = new LinkedList<E>();
			}
		}
	}
	
	/**
	 * Retrieves the list of elements on a given square.
	 * @param x column of the square
	 * @param y row of the square
	 * @return a list of elements on the given square
	 */
	public List<E> getElements(final int x, final int y) {
		return board[x][y];
	}
	
	/**
	 * Sets the list of elements on a given square.
	 * Trying to set to null will instead set an empty list.
	 * @param x column of the square
	 * @param y row of the square
	 * @param elements the list of elements to be set on the square
	 */
	public void setElements(final int x, final int y, final List<E> elements) {
		if (elements == null) {
			board[x][y] = new LinkedList<E>();
		} else {
			board[x][y] = elements;
		}
	}
	
	/**
	 * Creates a copy of the GameBoard.
	 * @return a new object with same content (game elements are cloned)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public GameBoard<E> clone() {
		GameBoard<E> theClone = new GameBoard<E>(sizex, sizey);
		for (int x = 0; x < board.length; x++) {
			for (int y = 0; y < board[x].length; y++) {
				LinkedList<E> listClone = new LinkedList<E>();
				for (IBoardElement element : board[x][y]) {
					listClone.add((E) element.clone());
				}
				theClone.setElements(x, y, listClone);
			}
		}
		return theClone;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean checkCoordinates(int x, int y) {
		if (!(x < sizex && x >= 0)) { return false; }
		if (!(y < sizey && y >= 0)) { return false; }
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean checkCoordinates(Point position) {
		return checkCoordinates(position.getX(), position.getY());
	}


	/**
	 * {@inheritDoc}
	 */
	public void setElements(final Point coord, final List<E> elementList) {
		setElements(coord.getX(), coord.getY(), elementList);
	}



	/**
	 * {@inheritDoc}
	 */
	public List<E> getElements(final Point coord) {
		return getElements(coord.getX(), coord.getY());
	}


	/**
	 * {@inheritDoc}
	 */
	public int getHeight() {
		return sizey;
	}


	/**
	 * {@inheritDoc}
	 */
	public int getWidth() {
		return sizex;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean equals(IBoard<E> other) {
		if (other == null) { return false; }
		if (getWidth() != other.getWidth()) { return false; }
		if (getHeight() != other.getHeight()) { return false; }
		for (int x = 0; x < sizex; x++) {
			for (int y = 0; y < sizey; y++) {
				if (!this.getElements(x, y).equals(other.getElements(x, y))) { return false; }
			}
		}
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int hashCode() {
		int i = 0;
		for (int x = 0; x < sizex; x++) {
			for (int y = 0; y < sizey; y++) {
				i = i ^ getElements(x, y).hashCode();
			}
		}		
		return i;
	}
}
