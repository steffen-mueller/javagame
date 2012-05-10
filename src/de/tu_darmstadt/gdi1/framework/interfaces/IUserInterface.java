package de.tu_darmstadt.gdi1.framework.interfaces;

import de.tu_darmstadt.gdi1.framework.events.DefaultUserInterfaceEvent;
import de.tu_darmstadt.gdi1.framework.exceptions.ParameterOutOfRangeException;
import de.tu_darmstadt.gdi1.framework.exceptions.SoundFailedException;
import de.tu_darmstadt.gdi1.framework.view.UserInterface;

/**
 * A interface for a/the controller to communicate with the UI.
 * 
 * <br>
 * This Interface is implemented by the view and seen by the {@link IController}. <br>
 * Add here only things the Controller/Game-logic needs to know about the {@link de.tu_darmstadt.gdi1.framework.interfaces.IUserInterface}. <br>
 * In other words: just the one method {@link de.tu_darmstadt.gdi1.framework.interfaces.IUserInterface#handleEvent(IUserInterfaceEvent)} should be enough.
 * <br>
 * <br>
 * 
 * Implemented by {@link DefaultUserInterfaceEvent}.
 * 
 * @see {@link de.tu_darmstadt.gdi1.framework.interfaces.IUserInterface#handleEvent(IUserInterfaceEvent)}
 * @author jonas
 * @param <E> The type of BoardElements used
 */
public interface IUserInterface<E extends IBoardElement> {

	/**
	 * A method for the {@link IController} to inform the view about an event.
	 * The board of the event should be displayed automatically (if set),
	 * also the sound (if set) should be played.
	 * <br> 
	 * After this, the event should be given to a abstract method
	 * to allow the custom view to handle the remaining events.
	 * 
	 * <p>
	 * Please note that the work of this method will be done in the EDT-Thread.<br>
	 * So a call to this method will return only once all the work in the edt-thread is done.<br>
	 * For joining the edt-thread {@link javax.swing.SwingUtilities#invokeAndWait(Runnable)} is used.<br>
	 * Please see also {@link UserInterface#handleNonFrameworkEvents} for more informations.
	 * </p>
	 * 
	 * @param event
	 *            the event to handle
	 * @throws ParameterOutOfRangeException if the board to display is not valid.
	 * @throws SoundFailedException if playing a sound failed
	 * @see UserInterface#handleNonFrameworkEvents
	 */
	public void handleEvent (IUserInterfaceEvent<E> event) throws ParameterOutOfRangeException, SoundFailedException;

	/**
	 * Passes an event to {@link #handleEvent(IUserInterfaceEvent)} and eats exceptions.
	 * Stack traces are printed if exceptions are caught.
	 * @param event the event
	 */
	public void handleEventSilently (final IUserInterfaceEvent<E> event);

	
}
