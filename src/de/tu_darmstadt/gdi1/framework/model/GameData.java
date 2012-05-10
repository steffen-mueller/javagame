package de.tu_darmstadt.gdi1.framework.model;


import java.io.IOException;
import java.util.Date;

import de.tu_darmstadt.gdi1.framework.interfaces.IBoardElement;
import de.tu_darmstadt.gdi1.framework.interfaces.IGameData;
import de.tu_darmstadt.gdi1.framework.interfaces.IStepManager;


/**
 * container for all game information.
 * 
 * @author jonas
 */
public abstract class GameData<E extends IBoardElement> implements IGameData<E> {

	/** serial-uid. */
	private static final long serialVersionUID = -3100639094502187901L;

	/** current GameBoard. */
	private IStepManager<E> stepManager;
	
	/** name of player. */
	private String playername = null;
	
	/** number of steps the user did. */
	private long stepCounter = 0;
	
	/** date storing when the game was started or resumed. */
	private transient Date startDate = null;
	
	/** the filename of the original level is stored here. Never null. */
	private String filename = "";

	/** elapsed game time. */
	private transient long elapsedTime = 0;

	/**
	 * constructor for gamedata, will create a new stepmanager with the given size.
	 * @param width width of board
	 * @param height height of board
	 */
	public GameData(final int width, final int height) {
		this(new StepManager<E>(width, height));
	}

	/**
	 * Creates a new GameData object containing the given stepmanager.
	 * @param aStepManager
	 * 		the stepmanager of the game.
	 */
	public GameData(final IStepManager<E> aStepManager) {
		this.stepManager = aStepManager;
	}

	
//	/** constructor for serialization. */
//	protected GameData() {
//		super();
//	}

	/**
	 * {@inheritDoc}
	 */
	public IStepManager<E> getStepManager() {
		return stepManager;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setStepManager(final IStepManager<E> aStepManager) {
		this.stepManager = aStepManager;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getPlayername() {
		return playername;
	}

	/**
	 * {@inheritDoc}
	 */
	public long getStepCount() {
		return stepCounter;
	}

	/**
	 * {@inheritDoc}
	 */
	public void incStepCount() {
		stepCounter++;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setPlayername(final String name) {
		playername = name;
		if (playername != null) { 
			playername = playername.replace("\t", "");
			playername = playername.replace("\n", "");
			playername = playername.replace("\r", "");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void setStepCount(final long steps) {
		stepCounter = steps;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setTime(final long newTime) {
		elapsedTime = newTime;
		if (startDate != null) {
			startDate = new Date();
		}
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	public long getTime() {
		if (startDate == null) {
			return elapsedTime;
		} else {
			long currentRunTime = new Date().getTime() - this.startDate.getTime();
			long resultingTime = this.elapsedTime + currentRunTime;			
			return resultingTime;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getLevelFilename() {
		return filename;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setLevelFilename(final String name) {
		//we need to check this because we have to promise a not null value for the getter-method
		if (name == null) {
			throw new NullPointerException("New Levelfilename has to be different form null.");
		}
		filename = name;
	}

	/**
	 * {@inheritDoc}
	 */
	public void startTimer() {
		if (startDate == null) {
			startDate = new Date();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void stopTimer() {
		if (startDate != null) {
			long currentRunTime = new Date().getTime() - this.startDate.getTime();
			elapsedTime += currentRunTime;			
			startDate = null;
		}
	}


	
	/**
	 * allows to serialize a running game.
	 * @param stream stream to write
	 * @throws java.io.IOException writing object failed
	 */
	private void writeObject(final java.io.ObjectOutputStream stream) throws IOException {
		stream.defaultWriteObject();
		long time = getTime();
		stream.writeObject(time);
		if (startDate == null) {
			stream.writeObject(Boolean.FALSE);
		} else {
			stream.writeObject(Boolean.TRUE);
		}
	}
	
	/**
	 * allows to serialize a running game.
	 * @param stream stream to read from
	 * @throws java.io.IOException read failed
	 * @throws ClassNotFoundException can't cast to the desired Object
	 */
	private void readObject(final java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException {
		stream.defaultReadObject();
		elapsedTime = (Long) stream.readObject();
		Boolean createDate = (Boolean) stream.readObject();
		if (createDate) {
			startDate = new Date();
		} else {
			startDate = null;
		}
	}


	/**
	 * {@inheritDoc}
	 */
	public Date getStartDate() {
		return startDate;
	}

	public abstract boolean isLost();

	public abstract boolean isPaused();

	public abstract boolean isRunning();

	public abstract boolean isWon();
}
