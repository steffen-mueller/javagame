package de.tu_darmstadt.gdi1.framework.model;


import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import de.tu_darmstadt.gdi1.framework.exceptions.NoLastStepException;
import de.tu_darmstadt.gdi1.framework.interfaces.IBoard;
import de.tu_darmstadt.gdi1.framework.interfaces.IBoardElement;
import de.tu_darmstadt.gdi1.framework.interfaces.IGameBoard;
import de.tu_darmstadt.gdi1.framework.interfaces.IStepManager;
import de.tu_darmstadt.gdi1.framework.utils.FrameworkUtils;
import de.tu_darmstadt.gdi1.framework.utils.Point;


/**
 * Stores undo/redo histories with game boards. Can be used like a game board.
 * Use saveStep() to store an undo step (i.e. probably you want to call that
 * once before (see {@link #saveStep()}) a set of all changes caused by one user action happes).
 * Use undo() or redo() do un- and redo.
 * @author Jan
 */
public class StepManager<E extends IBoardElement> implements Serializable, IStepManager<E>, Cloneable {

	private static final long serialVersionUID = 6270445872018457456L;

	/**
	 * The current game board managed by the StepManager.
	 */
	private IGameBoard<E> currentBoard;

	/** A stack containing the boards that can be undone,
	 * in correct order (next to undo on top).<br>
	 * The first element is always the element we add as last.
	 */
	private final Deque<IGameBoard<E>> undoHistory;

	/** A stack containing the boards that can be redone,
	 * in correct order (next to redo on top).<br>
	 * The first element is always the element we add as last.
	 */
	private final Deque<IGameBoard<E>> redoHistory;

	/** the default maximum (redo and undo) history size. */
	public static final int DEFAUL_MAX_HISTORY_SIZE = 20;
	/** the maximal size of the undo history. zero is unlimited. */
	private int maxUndoHistorySize = DEFAUL_MAX_HISTORY_SIZE;
	/** the maximal size of the undo history. zero is unlimited. */
	private int maxRedoHistorySize = DEFAUL_MAX_HISTORY_SIZE;

	/**
	 * Creates and initializes a step (undo/redo) manager
	 * containing an empty game board.
	 * <p> StepHistory (undo and redo) will be created with default maximum size, see {@link #setMaxRedoHistorySize(int)} and {@link #setMaxUndoHistorySize(int)}</p>
	 * @param sizex horizontal size of the managed board
	 * @param sizey vertical size of the managed board
	 */
	public StepManager(final int sizex, final int sizey) {
		this(new GameBoard<E>(sizex, sizey));
	}

	/**
	 * Creates a new StepManager using the given board.
	 * <p> StepHistory (undo and redo) will be created with default maximum size, see {@link #setMaxRedoHistorySize(int)} and {@link #setMaxUndoHistorySize(int)}</p>
	 * @param newBoard the board for the new StepManager to use
	 */
	public StepManager(IGameBoard<E> newBoard) {
		this(newBoard, new ArrayDeque<IGameBoard<E>>(), new ArrayDeque<IGameBoard<E>>());
	}

	/**
	 * Creates a new StepManager using the given board and histories.
	 * <p> StepHistory (undo and redo) will be created with default maximum size, see {@link #setMaxRedoHistorySize(int)} and {@link #setMaxUndoHistorySize(int)}</p>
	 * @param newBoard the board for the new StepManager to use
	 * @param undoHisto the undo history for the new StepManager to use
	 * @param redoHisto the redo history for the new StepManager to use
	 */
	protected StepManager(IGameBoard<E> newBoard, Deque<IGameBoard<E>> undoHisto, Deque<IGameBoard<E>> redoHisto) {
		currentBoard = newBoard;
		undoHistory = undoHisto;
		redoHistory = redoHisto;
	}

	/** {@inheritDoc} */
	public StepManager<E> clone() {
		return new StepManager<E>(
				this.currentBoard.clone(),
				FrameworkUtils.deepDequeClone(undoHistory),
				FrameworkUtils.deepDequeClone(redoHistory)
		);
	}

	/** {@inheritDoc} */
	public IBoard<E> getCurrentBoard() {
		return currentBoard.clone();
	}



	/**
	 * Retrieves the list of elements on a given square of the current board.
	 * @param x column of the square
	 * @param y row of the square
	 * @return a list of elements on the given square
	 */
	public List<E> getElements(final int x, final int y) {
		return currentBoard.getElements(x, y);
	}

	/**
	 * Sets the list of elements on a given square of the current board.
	 *
	 * Note: This clears the redo history.
	 *
	 * @param x column of the square
	 * @param y row of the square
	 * @param elements the list of elements to be set on the square
	 */
	public void setElements(final int x, final int y, final List<E> elements) {
		currentBoard.setElements(x, y, elements);
		redoHistory.clear();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setElements(final Point coords, final List<E> elements) {
		setElements(coords.getX(), coords.getY(), elements);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<E> getElements(final Point coords) {
		return getElements(coords.getX(), coords.getY());
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean checkCoordinates(final int x, final int y) {
		return currentBoard.checkCoordinates(x, y);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean checkCoordinates(final Point point) {
		return checkCoordinates(point.getX(), point.getY());
	}



	/**
	 * {@inheritDoc}
	 */
	public int getHeight() {
		return currentBoard.getHeight();
	}

	/**
	 * {@inheritDoc}
	 */
	public int getWidth() {
		return currentBoard.getWidth();
	}

	/**
	 * {@inheritDoc}
	 */
	public void saveStep() {
		undoHistory.addFirst(currentBoard.clone());
		while ((undoHistory.size() > maxUndoHistorySize) && (maxUndoHistorySize > 0)) {
			undoHistory.removeLast();
		}
		redoHistory.clear();
	}

	/**
	 * {@inheritDoc}
	 */
	public void undo() throws NoLastStepException {
		if (this.undoPossible() <= 0) {
			throw new NoLastStepException();
		}
		/*
		 * we perform an undo.
		 * Means we put the current board into the redo(!)-stack
		 * and get the last one form the undo-stack.
		 */

		redoHistory.addFirst(currentBoard);
		//zero as maxUndoHistorySize means unlimited history
		while ((redoHistory.size() > maxRedoHistorySize) && (maxRedoHistorySize > 0)) {
			redoHistory.removeLast();
		}
		currentBoard = undoHistory.removeFirst();
	}

	/**
	 * {@inheritDoc}
	 */
	public void redo() throws NoLastStepException {
		if (this.redoPossible() <= 0) {
			throw new NoLastStepException();
		}
		/*
		 * we perform an redo.
		 * Means we put the current board into the undo(!)-stack
		 * and get the last one form the redo-stack.
		 */

		undoHistory.addFirst(currentBoard);
		//zero as maxRedoHistorySize means unlimited history
		while ((undoHistory.size() > maxUndoHistorySize) && (maxUndoHistorySize > 0)) {
			undoHistory.removeLast();
		}

		currentBoard = redoHistory.removeFirst();
	}

	/**
	 * Undoes multiple steps.
	 * @param count number of steps to undo
	 * @throws NoLastStepException if there are not enough steps to restore
	 */
	public void undo(final int count) throws NoLastStepException {
		for (int i = 0; i < count; i++) {
			undo();
		}
	}

	/**
	 * Redoes multiple steps.
	 * @param count number of steps to undo
	 * @throws NoLastStepException if there are not enough steps to restore
	 */
	public void redo(final int count) throws NoLastStepException {
		for (int i = 0; i < count; i++) {
			redo();
		}
	}

	/**
	 * @return the amount of steps that can be undone
	 */
	public int undoPossible() {
		return undoHistory.size();
	}

	/**
	 * @return the amount of steps that can be redone
	 */
	public int redoPossible() {
		return redoHistory.size();
	}

	/**
	 * Calls the {@link IBoard#equals(IBoard)} method of the current board.
	 * The history is ignored!
	 */
	public boolean equals(IBoard<E> other) {
		return this.currentBoard.equals(other);
	}

	/**
	 * {@inheritDoc}
	 */
	public int hashCode() {
		return this.currentBoard.hashCode();
	}

	/**
	 * @return the maxUndoHistorySize<br>
	 * zero is unlimited history.
	 */
	public int getMaxUndoHistorySize() {
		return maxUndoHistorySize;
	}

	/**
	 * @param newMaxUndoHistorySize the maxUndoHistorySize to set<br>
	 * set zero to have unlimited history
	 *
	 */
	public void setMaxUndoHistorySize(final int newMaxUndoHistorySize) {
		this.maxUndoHistorySize = newMaxUndoHistorySize;
	}

	/**
	 * @return the maxRedoHistorySize<br>
	 * zero is unlimited history.
	 */
	public int getMaxRedoHistorySize() {
		return maxRedoHistorySize;
	}

	/**
	 * @param newMaxRedoHistorySize the maxRedoHistorySize to set<br>
	 * set zero to have unlimited history
	 */
	public void setMaxRedoHistorySize(final int newMaxRedoHistorySize) {
		this.maxRedoHistorySize = newMaxRedoHistorySize;
	}

}

