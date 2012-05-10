package de.tu_darmstadt.gdi1.framework.interfaces;

import java.util.List;
import javax.swing.ImageIcon;

import de.tu_darmstadt.gdi1.framework.utils.Point;
import de.tu_darmstadt.gdi1.framework.view.BoardPanel;
import de.tu_darmstadt.gdi1.framework.view.UserInterface;


/**
 * If you want to use this framework you need to implement this interface.
 * Usually, it is wise to implement it in the view.
 * <br>
 * <br>
 * This interface allows the {@link BoardPanel} to ask a class (typically the view)
 * how a given combination of {@link IBoardElement}s on the {@link IBoard}
 * are to be drawn. Therefore, an instance of a class implementing this
 * will be passed to the {@link BoardPanel} in its constructor.
 * <br>
 * By default, the abstract {@link UserInterface} class implements this interface.
 * You may choose to use a separate class to implement this interface and pass
 * that to the {@link BoardPanel}, for example to allow different skins for board elements.
 * @author jonas
 * @param <E> The type of BoardElements used
 *
 */
public interface IGameView<E extends IBoardElement> extends IUserInterface<E> {
	
	 /**
	 * @param aBoard
	 * 		the board from which a field is to be mapped
	 * @param coordinates the coordinates of the field to be mapped.
	 * @return
	 * 		a Image representing the List of {@link IBoardElement}, ie. the image to be displayed for the field of the board
	 */
	 ImageIcon getComponentForBoard (IBoard<E> aBoard, Point coordinates);
	
	 /**
	  * Specifies, if the element list at a certain position should be repainted.
	  * The default implementation will repaint all element lists, which equal each other.
	  * 
	  * Overwrite this method for further repaint tweaks.
	  * 
	  * @param oldBoard			old gameboard
	  * @param newBoard			new gameboard
	  * @param coordinate		coordinate of element to paint
	  * @return
	  * 		true if the elements should be repainted
	  */
	 boolean shouldPaintElementsAtCoordinate (IBoard<E> oldBoard, IBoard<E> newBoard, Point coordinate);

	/**
	 * This method is used by the view to find out what should be displayed for a given field of the board.
	 * Every implementation of a UserInterface has to override this with a implementation appropriate for
	 * the given game. The framework user is free to choose a way to do this. One possible way would be
	 * to overlay transparent images (one for every IBoardElement in the list).
	 * @see #getComponentForBoard(IBoard, Point)
	 * @param aList
	 * 		the list of IBoardElements which should be mapped to a {@link java.awt.Image}. This represents the elements on one field of the board.
	 * @param coordinates the coordinates of the component. can be used if necessary or just ignored otherwise. 
	 * @return
	 * 		a Image representing the List of {@link IBoardElement}, ie. the image to be displayed for the field of the board
	 */
	public abstract ImageIcon getComponentForList (List<E> aList, Point coordinates);
}
