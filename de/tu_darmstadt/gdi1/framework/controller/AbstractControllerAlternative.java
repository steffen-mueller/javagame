package de.tu_darmstadt.gdi1.framework.controller;


import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import de.tu_darmstadt.gdi1.framework.interfaces.IController;
import de.tu_darmstadt.gdi1.framework.interfaces.IControllerEvent;


/**
 * This is an EXPERIMENTAL abstract implementation of a controller.
 * If you just want to use the framework, override it, implementing
 * {@link #processEvent(IControllerEvent)} and leaving the rest alone.
 * This allows queueing events and handling them in a thread separate
 * from the view without need to implement such functions yourself.
 * <br><br>
 * 
 * This contains a thread which handles most of the actions inside the game.
 * It starts automatically, so you do not need to worry about it.
 * (Actually, {@link de.tu_darmstadt.gdi1.framework.controller.AbstractControllerAlternative} IS the thread.)
 * <br>
 *
 * This is an experimental alternative implementation of {@link AbstractController},
 * which uses a {@link java.util.concurrent.BlockingDeque} instead of doing all thread safety itself.
 * 
 * <br><br>
 * This controller adds priority events at the beginning of the queue,
 * i.e. if you send priority event A, then send priority event B before
 * A has been processed, B will be processed first.
 * See {@link AbstractController} if you look for the opposite behavior.
 * 
 * @see IController
 * @author Jan
 *
 */
public abstract class AbstractControllerAlternative extends Thread implements IController {

	/**
	 * Double-sided, thread safe event queue.
	 */
	private BlockingDeque<IControllerEvent> eventQueue = new LinkedBlockingDeque<IControllerEvent>(); 

	/** (daemon) timer for handling all events. */
	private Timer eventTimer = new Timer("TimerAbstractController", true); 


	/**
	 * Creates an {@link de.tu_darmstadt.gdi1.framework.controller.AbstractControllerAlternative}. This Controller receives events and put them to a queue. This queue is only
	 * read by a own WorkerThread of this Controller. The WorkerThread will start automatically after this object is
	 * created.<br>
	 * <br>
	 * Please notice: If you use this constructor you will have a <i><b>changed behavior on uncaught exceptions</b></i>.<br>
	 * An own (non default) {@link java.lang.Thread.UncaughtExceptionHandler} will be set which will kill the whole program with
	 * <code>System.exit(-3)</code>on an uncaught exception. <br>
	 * <br>
	 * Reminder: By default a Thread will print a StackTrace and die on an uncaught exception,<br>
	 * without any effects to other threads. This means the other threads will not take care of this, but the instance (and
	 * the queues) of this Thread will be alive as long as someone (another threads) has a reference to it. <br>
	 * <small>Of course you could set all other threads to daemons (see {@link Thread#setDaemon(boolean)}).<br>
	 * In this case the VM will quit also if you use the default {@link java.lang.Thread.UncaughtExceptionHandler}.<br>
	 * But please be aware that a graphical Swing UserInterface is not the best/easiest place to try to set all threads
	 * to daemons.<br>
	 * </small> To prevent misunderstanding: the default (of Java/Threads) is <u>not</u> what you get if you use this
	 * constructor.
	 * 
	 * @see de.tu_darmstadt.gdi1.framework.controller.AbstractControllerAlternative#AbstractController(java.lang.Thread.UncaughtExceptionHandler)
	 */
	public AbstractControllerAlternative() {
		this(new DefaultUncaughtExceptionHandlerOfController());
	}

	/**
	 * Creates a {@link de.tu_darmstadt.gdi1.framework.controller.AbstractControllerAlternative}. This Controller receive events and put them to a queue. This queue is only
	 * read by a own WorkerThread of this Controller. The WorkerThread will starts automatically after this object is
	 * created.<br>
	 * 
	 * @param exceptionHandler
	 *            the {@link java.lang.Thread.UncaughtExceptionHandler} for this thread.<br>
	 *            use null if you want this thread to behavior like no UncaughtExceptionHandler was set.<br>
	 *            please see comments to this parameter (and the default behavior of {@link java.lang.Thread.UncaughtExceptionHandler}) in {@link de.tu_darmstadt.gdi1.framework.controller.AbstractControllerAlternative#AbstractController()}
	 * 
	 * @see Thread#getUncaughtExceptionHandler()
	 * @see #AbstractControllerAlternative()
	 */
	public AbstractControllerAlternative(final UncaughtExceptionHandler exceptionHandler) {
    	super("ControllerWorkerThread");

    	//set the requested behavior for uncaught exceptions.
    	// if null does not change the default one.
    	if (exceptionHandler != null) {
        	this.setUncaughtExceptionHandler(exceptionHandler);
    	}
    	
    	// start the controller thread
    	this.start();
    }

	/**
	 * Adds an event to the end of the event queue for later processing.
	 * @param event the event to be added
	 */
    public void handleEvent(final IControllerEvent event) {
		eventQueue.addLast(event);
	}
		

	/**
	 * Adds an event to the beginning of the event queue for priority processing.
	 * @param event the event to be added
	 */
	public void handleEventImmediately(final IControllerEvent event) {
		eventQueue.addFirst(event);
	}

	
	/**
	 * Controller thread "main" function.
	 * DO NOT CALL DIRECTLY! Use start() instead.
	 * 
	 * <br>
	 * This takes events from the queues and passes them to 
	 * {@link #processEvent(IControllerEvent)} for processing.
	 * @see Thread
	 * @see Thread#run()
	 * @see Thread#start()
	 */
	public void run() {
		while (true) {
			try {
				processEvent(eventQueue.takeFirst());
			} catch (InterruptedException e) {
				// ignore interrupts
			}
		}
		
		
	}
	
	/**
	 * this will be called by the event queue system to act upon an event.
	 * Needs to detect the type of the event and do all necessary actions.
	 * This will be the "center of the brain" of the final game.
	 * Has to be implemented by user of the framework.
	 * <br><br>
	 * Typically, you will want to detect what type of event you got,
	 * for example by calling {@link IControllerEvent#getClass()}, then
	 * handle the separate events, for example by computing what has to be done,
	 * changing the game board and sending events to the view.
	 * You may also want to send new events to the controller.
	 * @param event the event to work with
	 */
	protected abstract void processEvent(IControllerEvent event);
	
	/**
	 * Creates a timer which adds a clone of a event to the queue after a certain time has passed.<br>
	 * For additional informations see methods noticed below.<br>
	 * <p>Notice: the event will be cloned. So you have to take care that your Event does support this correctly. 
	 * </p> 
	 * @param event
	 *            the event that should be triggered by the timer, will be cloned. may not null.
	 * @param priority
	 *            true adds the event to the beginning of the queue, false to the end
	 * @param repeating
	 *            true will trigger the event repeatedly
	 * @param interval
	 *            the time after which the event should be added to the queue or the interval between repetitions.
	 *            should not be negative.
	 * @return the created GameTimer. Use it to delete the set timer by calling {@link de.tu_darmstadt.gdi1.framework.controller.AbstractControllerAlternative.GameTimer#cancel()}.
	 * @see java.util.Timer#schedule(java.util.TimerTask, long)
	 * @see java.util.Timer#schedule(java.util.TimerTask, long, long)
	 */
	public GameTimer addTimer(final IControllerEvent event, final boolean priority, final boolean repeating, final long interval) {
		if (event == null) {
			throw new NullPointerException("Null as event is not legal for AbstractController.addTimer");
		}
		GameTimer task = new GameTimer(event, priority);
		
		if (repeating) {
			eventTimer.schedule(task, interval, interval);
		} else {
			eventTimer.schedule(task, interval);
		}
		return task;
	}
	

	
	/**
	 * Represents a scheduled timer.
	 * You may use its cancel() method to cancel/delete the timer.
	 * @author Jan
	 * @author jonas
	 */
	public final class GameTimer extends TimerTask {
		
		/** the event which should be fired. */
		private IControllerEvent event;
		/** the priority with which this event should be fired. */
		private boolean priority;

		/**
		 * 
		 * @param aEvent
		 *            the event which should be fired.<br>
		 *            (notice: only a clone of the event given here will be fired) 
		 * @param aPriority
		 *            the priority with which this event should be fired.
		 */
		private GameTimer(final IControllerEvent aEvent, final boolean aPriority) {
			if (aEvent == null) {
				throw new NullPointerException("null as event is  not valid.");
			}
			this.event = aEvent;
			this.priority = aPriority;
		}

		/** {@inheritDoc} */
		public void run() {
			if (priority) {
				handleEventImmediately(event.clone());
			} else {
				handleEvent(event.clone());
			}
		}
	}
	


	/**
	 * Creates and returns a new instance of {@link de.tu_darmstadt.gdi1.framework.controller.AbstractControllerAlternative.DefaultUncaughtExceptionHandlerOfController}.<br>
	 * This method is only a "short-cut" to avoid ugly external instantiations form internal classes.<br>
	 * 
	 * @return
	 * 		a new instance of the {@link de.tu_darmstadt.gdi1.framework.controller.AbstractControllerAlternative.DefaultUncaughtExceptionHandlerOfController}.
	 */
	protected UncaughtExceptionHandler getInstanceOfDefaultUncaughtExceptionHandlerOfController() {
		return new DefaultUncaughtExceptionHandlerOfController();
	}
	
	/**
	 * This is the default {@link java.lang.Thread.UncaughtExceptionHandler} of the {@link de.tu_darmstadt.gdi1.framework.controller.AbstractControllerAlternative}.<br>
	 * It will print the stack trace of the uncaught exception and than it let <i>the system hard exit</i>.<br>
	 * You can get an instance of this class with calling
	 * {@link de.tu_darmstadt.gdi1.framework.controller.AbstractControllerAlternative#getInstanceOfDefaultUncaughtExceptionHandlerOfController()}, if you want to use this
	 * behavior for another thread. <br>
	 * <br>
	 * Notice: If you want this behavior for <u>all</u> threads you could set this ExceptionHandler via
	 * {@link Thread#setDefaultUncaughtExceptionHandler(java.lang.Thread.UncaughtExceptionHandler)} as default for all threads.
	 * 
	 * @author jonas
	 * 
	 */
	private static class DefaultUncaughtExceptionHandlerOfController implements UncaughtExceptionHandler {

		/** {@inheritDoc} */
		public void uncaughtException(final Thread t, final Throwable e) {
			System.err.println("Uncaught exception in thread \"" + t.getName() + "\". System will hard exit after printing stacktrace.");
			e.printStackTrace(System.err);
			System.exit(-3);
		}

	}
}
