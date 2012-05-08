package de.tu_darmstadt.gdi1.framework.interfaces;


import java.io.Serializable;
import java.util.Date;

import de.tu_darmstadt.gdi1.framework.model.GameData;
import de.tu_darmstadt.gdi1.framework.utils.level.Level;


/**
 * this data structure holds all important information of a game.
 * should/could be extended for some more informations about a concrete game.
 * used by the {@link Level} loading/saving system, implemented by @link {@link GameData}.
 * @author jonas
 * @param <E> The type of BoardElements used
 */
public interface IGameData<E extends IBoardElement> extends Serializable {
	
	/**
	 * get the StepManager of this game.
	 * 
	 * @return
	 * 		the {@link IStepManager}
	 */
	IStepManager<E> getStepManager();
	

	/**
	 * sets a new StepManager to this GameData.
	 * 
	 * @param aStepManager
	 * 		the StepManager to set
	 */
	void setStepManager(IStepManager<E> aStepManager);

	
	/**
	 * get the whole time played in ms. (inc played time before a pause)
	 * 
	 * @return played time in ms.
	 */
	long getTime();


	/**
	 * set the time played in ms.<br>
	 * notice: this will set the given time <b>and</b> start the timer.
	 * 
	 * @param newTime
	 *            played time in ms.
	 */
	void setTime(long newTime);

	/**
	 * get the player name.
	 * 
	 * @return get the player name. If none set null is returned.
	 */
	String getPlayername();

	/**
	 * set the name of the player.
	 * 
	 * @param name
	 *            playername to set.
	 */
	void setPlayername(String name);

	/**
	 * get file name of the original level.
	 * 
	 * @return filename of original level, never null.
	 */
	String getLevelFilename();

	/**
	 * set file name of original level.
	 * 
	 * @param name
	 *            the file name, may not null.
	 */
	void setLevelFilename(String name);

	/**
	 * get the number of steps used.
	 * 
	 * @return step count.
	 */
	long getStepCount();

	/**
	 * set a new value for the stepCounter.
	 * 
	 * @param nSteps
	 *            new value for steps num.
	 */
	void setStepCount(long nSteps);

	/**
	 * increments the stepCounter by one.
	 */
	void incStepCount();

	
	/**
	 * the date of start. 
	 * @return
	 * the date of start.
	 */
	Date getStartDate();
	
	/**
	 * starts the play timer. played time increases from now on.<br>
	 * will be ignored if the time has started already. 
	 */
	void startTimer();

	/**
	 * stop timer. Played time is constant until startTimer().
	 * will be ignored if the time has stopped already. 
	 */
	void stopTimer();

	/**
	 * @return
	 * 	true if the game is paused.
	 */
	boolean isPaused();

	/**
	 * whether the game is running.
	 * notice: a won/lost game is not running
	 * @return
	 * 	true if the game is running.
	 */
	boolean isRunning();

	/**
	 * whether the game is won.
	 * @return
	 * 	true if the game is won.
	 */
	boolean isWon();

	/**
	 * whether the game is lost.
	 * @return
	 * 	true if the game is lost.
	 */
	boolean isLost();
	
}
