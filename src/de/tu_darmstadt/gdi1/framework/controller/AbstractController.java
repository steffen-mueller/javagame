package de.tu_darmstadt.gdi1.framework.controller;


import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import de.tu_darmstadt.gdi1.framework.exceptions.FrameworkError;
import de.tu_darmstadt.gdi1.framework.interfaces.IController;
import de.tu_darmstadt.gdi1.framework.interfaces.IControllerEvent;


/**
 * This is an abstract implementation of a controller.
 * If you just want to use the framework, override it, implementing
 * {@link #processEvent(IControllerEvent)} and leaving the rest alone.
 * This allows queueing events and handling them in a thread separate
 * from the view without need to implement such functions yourself.
 * <br><br>
 * 
 * This contains a thread which handles most of the actions inside the game.
 * It starts automatically, so you do not need to worry about it.
 * (Actually, {@link de.tu_darmstadt.gdi1.framework.controller.AbstractController} has the thread, see {@link de.tu_darmstadt.gdi1.framework.controller.AbstractController#getInnerThread()})
 * <br>
 * 
 * There are two event queues available, one for immediate (priority) and
 * one for normal events, that can be filled via {@link #handleEvent(IControllerEvent)}
 * and {@link #handleEventImmediately(IControllerEvent)}.<br>
 * 
 * A timer is also availible, allowing you to easily use timed events via
 * {@link #addTimer(IControllerEvent, boolean, boolean, long)}<br>
 * <br><br>
 * This controller queues priority events separately,
 * i.e. if you send priority event A, then send priority event B before
 * A has been processed, A will be processed first.
 * See {@link AbstractControllerAlternative} if you look for the opposite behavior.
 * 
 * @see IController
 * @author Jan
 * @author jonas
 *
 */
public abstract class AbstractController /*extends Thread*/ implements IController {
	
	
	/** normal "non-immediate" event queue. */
	private LinkedBlockingQueue<IControllerEvent> eventQueue = new LinkedBlockingQueue<IControllerEvent>();
	/**  event queue for immediate events. */
	private LinkedBlockingQueue<IControllerEvent> immediateEventQueue = new LinkedBlockingQueue<IControllerEvent>();
	
	/** timer for handling all events. */
	private Timer eventTimer = null; 

	/** Flag if the EventWorkerThread is listening for new events. */
	private final AtomicBoolean listenForEvents;
	
	/** Flag if the EventWorkerThread is sleeping.
	 * (..because both queues are empty). <br>
	 * If this one is true the EventWorkerThread is sleeping and needs a signal to wake up if you 
	 * insert something to a queue. */
	private final AtomicBoolean workerSleeps;
	
    /**
     * If you want to send (threadsafe-)signals you need a associated lock to guarantee the 
     * thread safety. This is this lock.
     * 
 	 * @see de.tu_darmstadt.gdi1.framework.controller.AbstractController#addEvent(java.util.concurrent.LinkedBlockingQueue, IControllerEvent)
	 * @see de.tu_darmstadt.gdi1.framework.controller.AbstractController#run()
 	 */
    private final ReentrantLock lockBoth;
    
    /**
     * This is the condition over which signals were sent to the worker.<br>
     * The worker waits for a signal if both queues were empty.
     * Signals were send every times there is a insert of a first element for one queue. 
     * 
 	 * @see de.tu_darmstadt.gdi1.framework.controller.AbstractController#addEvent(java.util.concurrent.LinkedBlockingQueue, IControllerEvent)
     */
    private final Condition notEmptyBoth;
    
    /**
     * The thread-object of this controller.
     */
	private final InnerThread innerThread;

	/**
	 * Creates an {@link de.tu_darmstadt.gdi1.framework.controller.AbstractController}. This Controller receives events and put them to a queue. This queue is only
	 * read by a own WorkerThread of this Controller. The WorkerThread will start automatically after this object is
	 * created.<br>
	 * If you want to stop this WorkerThread you could call {@link de.tu_darmstadt.gdi1.framework.controller.AbstractController#stopWorker()}, but be aware that a
	 * thread is <b>not</b> able to be restarted.<br>
	 * <br>
	 * Please notice: If you use this constructor you will have a <i><b>changed behavior on uncaught exceptions</b></i>.<br>
	 * An own (non default) {@link java.lang.Thread.UncaughtExceptionHandler} will be set which will kill the whole program with
	 * <code>System.exit(-3)</code>on an uncaught exception. <br>
	 * <br>
	 * Reminder: By default a Thread will print a StackTrace and die on an uncaught exception.<br>
	 * Without any effects to other threads. Means the other threads will not take care of this, but the instance (and
	 * the queues) of this Thread will be alive as long as someone (another threads) has a reference to it. <br>
	 * <small>Of course you could make all other threads to daemons (see {@link Thread#setDaemon(boolean)}).<br>
	 * In this case the VM will quit also if you use the default {@link java.lang.Thread.UncaughtExceptionHandler}.<br>
	 * But please be aware that a graphical Swing UserInterface is not the best/easiest place to try to set all threads
	 * to daemons.<br>
	 * </small> To prevent misunderstanding: the default (of Java/Threads) is <u>not</u> what you get if you use this
	 * constructor.
	 * 
	 * @see de.tu_darmstadt.gdi1.framework.controller.AbstractController#AbstractController(java.lang.Thread.UncaughtExceptionHandler)
	 */
	public AbstractController() {
		this(new DefaultUncaughtExceptionHandlerOfController());
	}

	/**
	 * Creates a {@link de.tu_darmstadt.gdi1.framework.controller.AbstractController}. This Controller receive events and put them to a queue. This queue is only
	 * read by a own WorkerThread of this Controller. The WorkerThread will starts automatically after this object is
	 * created.<br>
	 * If you want to stop this WorkerThread you could call {@link de.tu_darmstadt.gdi1.framework.controller.AbstractController#stopWorker()}, but be aware that a
	 * thread is <b>not</b> able to be restarted.
	 * 
	 * @param exceptionHandler
	 *            the {@link java.lang.Thread.UncaughtExceptionHandler} for this thread.<br>
	 *            use null if you want this thread to behavior like no UncaughtExceptionHandler was set.<br>
	 *            please see comments to this parameter (and the default behavior of {@link java.lang.Thread.UncaughtExceptionHandler}) in {@link de.tu_darmstadt.gdi1.framework.controller.AbstractController#AbstractController()}
	 * 
	 * @see Thread#getUncaughtExceptionHandler()
	 * @see de.tu_darmstadt.gdi1.framework.controller.AbstractController#AbstractController()
	 */
	public AbstractController(final UncaughtExceptionHandler exceptionHandler) {
    	super();
    	
    	innerThread = new InnerThread("EventQueueWorkerThread");

    	//only for signals, policy is unimportant
    	this.lockBoth = new ReentrantLock();
    	//the condition to send signals:
    	this.notEmptyBoth = lockBoth.newCondition();
    	
    	//Initial the thread should run...
    	this.listenForEvents = new AtomicBoolean(true);
    	//Initial the thread is not sleeping..
    	this.workerSleeps = new AtomicBoolean(false);

    	//set the requested behavior for uncaught exceptions.
    	// if null does not change the default one.
    	if (exceptionHandler != null) {
    		innerThread.setUncaughtExceptionHandler(exceptionHandler);
    	}
    	
    	//let the separate EventWorkerThread start
    	innerThread.start();
    }
	
    /** {@inheritDoc} */
    public void handleEvent(final IControllerEvent event) {
		addEvent(eventQueue, event);
	}
		
    /** {@inheritDoc} */
	public void handleEventImmediately(final IControllerEvent event) {
		addEvent(immediateEventQueue,  event);
	}

	/**
	 * Inserts the event into the queue.<br>
	 * If the queue is empty and/or the EventWorkerThread is marked as sleeping
	 * this method will send a signal to this EventWorkerThread.<br>
	 * 
	 * @param queue
	 * 		the queue where the event should be inserted, may not null.
	 * @param event
	 * 		the event which should be inserted, may not null.
	 */
	private void addEvent(final LinkedBlockingQueue<IControllerEvent> queue, final IControllerEvent event) {
		if (event == null) {
			throw new NullPointerException("null as event is not allowed.");
		}
		/*
		 * is the worker thread sleeping OR the queue is empty?
		 * (in the last case the worker thread maybe is preparing to go sleep)
		 * if yes remember that we have to wake up the worker thread
		 */
		boolean signalWorker = (workerSleeps.get() || queue.size() <= 0);
		
		
		/* if you want to make sure that a thread return after a specified time from putting a event into a queue
		 * the following code-line would be the right place to insert a timeout.
		 * E.g. instead of .offer(event) use .offer(event, timeout, unit).

		 * Be aware:
		 * 		if the want to make sure a thread to return after a specified time,
		 * 		you will lose the safety that every event will be inserted in a queue.
		 * 
		 * If the look accurate to the code, you should see that the insertion should 
		 * be very fast. If the queue (has a maximum capacity and) is full the event will be lost.
		 * If the queue has no maximum capacity there could no reason for needing much time for
		 * a insert. 
		 */  

		//offer the event to the thread safe queue.
		queue.offer(event);
				
		
		/*
		 * if we not detected already that we should wake up the worker thread we have to check:
		 * does the thread sleep now, or is the current offered event the only one in the queue
		 * (in the last case the worker thread maybe is preparing to go sleep)
		 * 
		 * do we need the last case (in both conditions)? maybe not?!
		 * if the worker is preparing to sleep he will check if both queues were empty -
		 * after setting workerSleeps=true.
		 */
		signalWorker = (signalWorker || workerSleeps.get() || queue.size() <= 1);
		if (signalWorker) {
			signalQueueWorker();
		}
	}

	/**
	 * this method sends a signal to the QueueWorker-Thread.<br>
	 * If it sleeps he will wake up.<br>
	 * Else it will observe any changes by its own.<br>
	 * Those changes could be a further event in a queue or a changed "{@link de.tu_darmstadt.gdi1.framework.controller.AbstractController#listenForEvents}"-state
	 */
	private void signalQueueWorker() {
		/*
		 * Respecting the fact that nowhere this lock is hold and by maximum two threads sending concurrently 
		 *  signals
		 * this signal sending should be done very fast.<br>
		 * Obviously the lock is held during sending a signal.<br>
		 * Also the EventWorkerThread holds this lock if he tries to fall asleep.
		 * If he do so he hold the lock only during checking a few conditions before executing 
		 * a await on the condition connected with the lock (to listen for signals.<br> 
		 * In other words: should also be very fast.
		 */
		lockBoth.lock();
		try {
			notEmptyBoth.signal();
		} finally {
			lockBoth.unlock();
		}
	}

	/**
	 * Get the thread of this controller.
	 * @return
	 * 		the thread of this controller
	 * @see de.tu_darmstadt.gdi1.framework.controller.AbstractController.InnerThread
	 */
	protected Thread getInnerThread() {
		return innerThread;
	}
	
	/**
	 * let the worker stop his work (after finishing the current event processing).<br>
	 * Be aware: there is no possibility to restart a thread.<br>
	 * Also all added timers will not be executed anymore.
	 */
	protected void stopWorker() {
		listenForEvents.set(false);
		//maybe the worker is sleeping, wake him up:
		signalQueueWorker();
		//also kill all timers:
		if (eventTimer != null) {
			eventTimer.cancel();
		}
	}
	
	/**
	 * The inner thread of the {@link de.tu_darmstadt.gdi1.framework.controller.AbstractController}.
	 * The advantage of having an inner class as thread is resulting simplicity of the {@link de.tu_darmstadt.gdi1.framework.controller.AbstractController}.
	 * Only a few methods but high functionality.
	 * If you want to modify the default thread behavior use {@link de.tu_darmstadt.gdi1.framework.controller.AbstractController#getInnerThread()} to do so.
	 * 
	 * @author jonas
	 *
	 */
	private class InnerThread extends Thread {
		
		/**
		 * creates an InnerThread with the given name.
		 * @param name
		 */
		public InnerThread(final String name) {
			super(name);
		}

		/**
		 * DO NOT CALL DIRECTLY! The thread starts itself!<br>
		 * 
		 * Controller thread "main" function.<br>
		 * This takes events from the queues and passes them to 
		 * {@link #processEvent(IControllerEvent)} for processing.
		 * Handles thread safety. 
		 * 
		 * <br>For implementation details, see code and comments in there.
		 * 
		 * @see Thread
		 * @see Thread#run()
		 * @see Thread#start()
		 */
		public void run() {
			
			IControllerEvent nextEventToHandle = null;
			initialize();

			while (listenForEvents.get()) {
				try {
					
					/* if nextEventToHandle is null, in the last while-round was nothing to do.
					 * if no one other thread is putting something in a queue (and held the putLock)
					 * and both queues are empty start to wait.. */
					if ((nextEventToHandle == null) && (lockBoth.tryLock())) {
						try {
							workerSleeps.set(true);
							/* important to check (with lock and sleep=true): were the queues really empty?
							 * 
							 */
							if (eventQueue.size() + immediateEventQueue.size() == 0) {
								/* at this point we can be sure that:
								 *  - the next inserting thread will send a signal
								 *  	[ -> obviously, because both queues are empty ]
								 *  - this signal of the inserting thread will be heard.
								 *  	[ -> we have already the lock which the other thread needs to send the signal ]   
								 */
								
								/*
								 * hearth of this method: the wait-part.
								 * this will block until a other thread calls notEmptyBoth.signal()
								 * or this thread is interrupted.
								 */
								notEmptyBoth.await();
							}
						} finally {
							lockBoth.unlock();
							workerSleeps.set(false);
						}
					}

					nextEventToHandle = immediateEventQueue.poll();
					if (nextEventToHandle != null) {
						processEvent(nextEventToHandle);
						continue;
					}

					nextEventToHandle = eventQueue.poll();
					if (nextEventToHandle != null) {
						processEvent(nextEventToHandle);
						continue;
					}

				} catch (InterruptedException e) {
					throw new FrameworkError("No one should interrupt the controller. The controller will wake up by its own if its sleeps and you stop it or add a new event.", e);
				}
			}
		}
	}


	/**
	 * This method is called once when the own controller thread is starting.
	 * The implementation in the {@link de.tu_darmstadt.gdi1.framework.controller.AbstractController} is only a <b>stub</b>!
	 * Override this stub if you need to initialize something in the controller-thread.
	 */
	protected void initialize() {
		// stub to override
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
	 * <p> Notice too: if the controller is stopped (see {@link de.tu_darmstadt.gdi1.framework.controller.AbstractController#stopWorker()}
	 * this method will be consistent and will return a {@link de.tu_darmstadt.gdi1.framework.controller.AbstractController.GameTimer} - but this one is obviously <b>not</b> active.<br>
	 * If it would be active it would be senseless because all events this GameTimer would add to the queue of this controller would be unheard because the controller is stopped.
	 * </p>
	 * @param event
	 *            the event that should be triggered by the timer, will be cloned. may not null.
	 * @param priority
	 *            true adds the event to the beginning of the queue, false to the end
	 * @param repeating
	 *            true will trigger the event repeatedly
	 * @param interval
	 *            the time (in milliseconds) after which the event should be added to the queue or the interval between repetitions.
	 *            should not be negative.
	 * @return the created GameTimer. Use it to delete the set timer.
	 * @see java.util.Timer#schedule(java.util.TimerTask, long)
	 * @see java.util.Timer#schedule(java.util.TimerTask, long, long)
	 */
	public GameTimer addTimer(final IControllerEvent event, final boolean priority, final boolean repeating, final long interval) {
		if (event == null) {
			throw new NullPointerException("Null as event is not legal for AbstractController.addTimer");
		}
		GameTimer task = new GameTimer(event, priority);
		
		if (!listenForEvents.get()) {
			//we if we not listen to events anymore. 
			// we also do not add new timers.
			// to be consistent for callers:
			// just return the un-added timer.
			return task;
		}

		if (eventTimer == null) {
			createTimer();
		}
		if (repeating) {
			eventTimer.schedule(task, interval, interval);
		} else {
			eventTimer.schedule(task, interval);
		}
		return task;
	}
	
	/**
	 * creates a new Timer-Object, thread-safe because synchronized.
	 * Timer-Thread is a demon-thread.<br>
	 * if you want to get timer inputs make sure that your queueworker does not die - to keep the VM alive.
	 */
	private synchronized void createTimer() {
		if (eventTimer == null) {
			eventTimer = new Timer("Timer-AbstrController", true);
		}
	}
	
	/**
	 * Deletes a timer.
	 * You may also use the cancel() method of the GameTimer object
	 * returned by addTimer().
	 * @param timer timer to delete
	 */
	public void deleteTimer(final GameTimer timer) {
		timer.cancel();
	}
	
	/**
	 * Represents a scheduled timer.
	 * You may use its cancel() method to cancel/delete the timer.
	 * @author Jan
	 * @author jonas
	 */
	public class GameTimer extends TimerTask {
		
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
		protected GameTimer(final IControllerEvent aEvent, final boolean aPriority) {
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
	 * Creates and returns a new instance of {@link de.tu_darmstadt.gdi1.framework.controller.AbstractController.DefaultUncaughtExceptionHandlerOfController}.<br>
	 * This method is only a "short-cut" to avoid ugly external instantiations form internal classes.<br>
	 * 
	 * @return
	 * 		a new instance of the {@link de.tu_darmstadt.gdi1.framework.controller.AbstractController.DefaultUncaughtExceptionHandlerOfController}.
	 */
	protected UncaughtExceptionHandler getInstanceOfDefaultUncaughtExceptionHandlerOfController() {
		return new DefaultUncaughtExceptionHandlerOfController();
	}
	
	/**
	 * This is the default {@link java.lang.Thread.UncaughtExceptionHandler} of the {@link de.tu_darmstadt.gdi1.framework.controller.AbstractController}.<br>
	 * It will print the stack trace of the uncaught exception and than it let <i>the system hard exit</i>.<br>
	 * You can get an instance of this class with calling
	 * {@link de.tu_darmstadt.gdi1.framework.controller.AbstractController#getInstanceOfDefaultUncaughtExceptionHandlerOfController()}, if you want to use this
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
