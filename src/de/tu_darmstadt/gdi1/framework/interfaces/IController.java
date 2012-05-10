package de.tu_darmstadt.gdi1.framework.interfaces;

import de.tu_darmstadt.gdi1.framework.controller.AbstractController;

/**
 * A interface for the abstract class of a Controller.
 * As a regular framework user, just extend {@link AbstractController}!
 * It is an implementation of this interface doing all the dirty work. 
 * 
 * <br> This Interface is what the {@link IUserInterface} see of the controller.
 * Add here only things the (G)UI needs to access!
 * <br><br>
 * A class which implements this interface should provide some kind of an event queue
 * and a separate EventWorker-thread.
 * The queue is filled via the handleEvent-Methods of this interface.
 * <br>
 * The "real" handling should be a call form the EventWorker(-thread) to a abstract method of the class implementing this interface.
 * <br>Notice: this abstract method is NOT defined here and should not be called from outside the controller!
 * 
 * @author jonas
 *
 */
public interface IController  {

	/**
	 * By calling this method the given event should be enqueued <u>at the end</u> of the controller's event queue.
	 * <br>With this method, the UI can inform the {@link de.tu_darmstadt.gdi1.framework.interfaces.IController} about an input of the user, for example.
	 * 
	 * <br><br>
	 * The controller decides how this is implemented.
	 * <small>
	 * (Depending on the implementation there could also be two queues.
	 * One for immediately events which is checked permanently and another for "normal" events which is only
	 * checked if the immediate event queue is empty.)</small>
	 *  
	 * @param event
	 * 		the event which should be placed in the handling queue, may not null.
	 * @throws NullPointerException thrown if the event is null
	 */
	void handleEvent (IControllerEvent event) throws NullPointerException;
	
	/**
	 * See {@link de.tu_darmstadt.gdi1.framework.interfaces.IController#handleEvent(IControllerEvent)}
	 * but this method should enqueue the given event so that it is executed with priority (before other events).
	 * <br><br>
	 * Priority events will always be processed before regular events.
	 * If you add a priority event while another priority event is queued,
	 * however, it is unspecified which priority event will be executed first.
	 * The controller implementation decides about that.
	 * <b>Use this only if you really need to.</b>
	 * 
	 * @param event
	 * 		the event which should be handled next, may not null.
	 * @throws NullPointerException thrown if the event is null
	 */
	void handleEventImmediately (IControllerEvent event) throws NullPointerException;
	
}
