package de.tu_darmstadt.gdi1.framework.interfaces;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import de.tu_darmstadt.gdi1.framework.exceptions.NoLastStepException;
import de.tu_darmstadt.gdi1.framework.model.GameBoard;
import de.tu_darmstadt.gdi1.framework.model.StepManager;
import de.tu_darmstadt.gdi1.framework.utils.Point;

/**
 * 
 * A step manager is a extension or decorator for a normal {@link IGameBoard}.<br>
 * 
 * {@link de.tu_darmstadt.gdi1.framework.interfaces.IStepManager#reDo()} and {@link de.tu_darmstadt.gdi1.framework.interfaces.IStepManager#unDo()} allow undoing "steps". <br>
 * 
 * Steps are defined by calls to {@link #saveStep()}.
 * 
 * Note: a {@link de.tu_darmstadt.gdi1.framework.interfaces.IStepManager} has to be {@link java.io.Serializable} to have a nice
 * possibility to save/load the current game.
 * 
 * <br><br>
 * This interface is implemented by {@link StepManager}.
 * 
 * @author Jonas
 * @param <E> The type of BoardElements used
 * 
 */
public interface IStepManager<E extends IBoardElement> extends IGameBoard<E>, Serializable, Cloneable {

	/**
	 * The StepManager delegates this call to the underlying
	 * {@link GameBoard#setElements(int, int, java.util.LinkedList)}.
	 * Remember: Mark a step as finished with a call to
	 * {@link de.tu_darmstadt.gdi1.framework.interfaces.IStepManager#saveStep()}!
	 * 
	 * @param coord
	 *            the coordinate of the position
	 * @param elementList
	 *            the list of gameelements which shall be placed there, may not
	 *            be null.
	 */
	void setElements (final Point coord, final List<E> elementList);

	/**
	 * The game has to call this method after various setElements-calls to
	 * inform the StepManager that this step is finished.<br>
	 * Notice: One step in the game (e.g. move one field up) is most times
	 * related to more than one call of setElements.<br>
	 * 
	 * <p>
	 * With a call to this method you save the <i>current</i> state of the board.<br>
	 * So you may should call this method <i>before</i> you change something on your board.<br>
	 * If you save <i>after</i> you changed your board you have to call undo <b>twice</b> - because the first board on the undo-stack is equals to the current board.
	 * In this case the second one is the "last" board you want.<br>
	 * <br>
	 * So we recommend to call this method <i><b>before</b></i> you change something on the board, so that you always have the real-last-board on top of the stacks.
	 * </p>
	 * 
	 * {@link #undo()} will return to the last saved step. <br/>
	 * After you did a call to undo you can call {@link #redo()}. 
	 * 
 	 * Note: This clears the redo history.
	 * 
	 */
	void saveStep ();

	/**
	 * Returns to a board from the redo history,
	 * putting the current board back into the undo history.
	 * @throws NoLastStepException if there is no saved step to restore
	 */
	void redo () throws NoLastStepException;

	/**
	 * Returns to the last saved step.
	 * The current board gets added to the redo history.
	 * @throws NoLastStepException if there is no saved step to restore
	 */
	void undo () throws NoLastStepException;
	
	
	/**
	 * Will return the current board of this StepManager.<br>
	 * <p>
	 * Please notice: you get an clone of the real board.<br>
	 * If you want to change something on this board <b>always</b> use the {@link StepManager#setElements}-methods,
	 * NEVER perform changes on the result of this function.
	 * </p>
	 * <p>
	 * Use this Method if you want to publish the board -as read only- to other parts of the application, e.g. the view.
	 * </p>
	 * <p>
	 * The {@link StepManager} contains the whole histories and can become very large.<br>
	 * You should use this method, instead of giving the direct {@link StepManager}-reference to other objects, if they will clone
	 * or serialize what they get. Otherwise, they will be cloning/serializing the full histories.
	 * <b>The default BoardPanel, however, auto-detects {@link de.tu_darmstadt.gdi1.framework.interfaces.IStepManager} objects and extracts the board before cloning
	 * so you do <u>not</u> need to do it manually.</b> 
	 * </p>
	 * 
	 * @return the clone of the current board of this StepManager.
	 */
	IBoard<E> getCurrentBoard ();

}

