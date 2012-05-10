package de.tu_darmstadt.gdi1.framework.events;


import java.util.LinkedHashMap;

import de.tu_darmstadt.gdi1.framework.exceptions.FrameworkError;
import de.tu_darmstadt.gdi1.framework.interfaces.IBoard;
import de.tu_darmstadt.gdi1.framework.interfaces.IBoardElement;
import de.tu_darmstadt.gdi1.framework.interfaces.IUserInterfaceEvent;

/**
 * Basic implementation of {@link IUserInterfaceEvent} 
 * @author Jan
 *
 */
public class DefaultUserInterfaceEvent<E extends IBoardElement> implements IUserInterfaceEvent<E> {

	/**
	 * the board to be displayed
	 */
	private IBoard<E> board;
	
	/**
	 * the information map to be displayed
	 */
	private LinkedHashMap<String, String[]> infomap;
	
	/**
	 * the name of the sound to be played
	 */
	private String sound;
	
	/**
	 * holds information whether pause mode should be enabled or disabled
	 */
	private Boolean pauseMode;
	
	/**
	 * holds information whether a complete repaint of the board is forced.
	 */
	private Boolean forcePaint;

	/**
	 * default constructor, initialize all values with null (or zero).
	 */
	public DefaultUserInterfaceEvent() {
		board = null;
		infomap = null;
		sound = null;
		forcePaint = false;
	}
	

	/** 
	 * try to create a new instance of the current class by using
	 *  <code>this.getClass().newInstance();</code>.
	 *  @return a copy with of this {@link DefaultControllerEvent} with same values.
	 *  or throws a RuntimeException if the instantiation (see above) does not work.
	 */
	public DefaultUserInterfaceEvent<E> clone() {
		DefaultUserInterfaceEvent<E> event;
		try {
			event = this.getClass().newInstance();
			event.setBoard(this.getBoard());
			event.setSound(this.getSound());
			event.setPauseMode(this.getPauseMode());
			event.setForceNewPaint(this.getForceNewPaint());
			event.setInformationMap(this.getInformationMap());
			return event;
		} catch (InstantiationException e) {
			e.printStackTrace();
			System.err.println("Event could not be cloned - you must override clone() or have a default constructor");
			throw new FrameworkError("Event could not be cloned - you must override clone() or have a default constructor", e);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			System.err.println("Event could not be cloned - you must override clone() or have a default constructor");
			throw new FrameworkError("Event could not be cloned - you must override clone() or have a default constructor", e);
		}
	}

	
	/**
	 * {@inheritDoc}
	 */
	public IBoard<E> getBoard() {
		return board;
	}




	/**
	 * {@inheritDoc}
	 */
	public LinkedHashMap<String, String[]> getInformationMap() {
		return infomap;
	}


	/**
	 * {@inheritDoc}
	 */
	public String getSound() {
		return sound;
	}


	/**
	 * {@inheritDoc}
	 */
	public void setBoard(IBoard<E> board) {
		this.board = board;
	}


	/**
	 * {@inheritDoc}
	 */
	public void setInformationMap(LinkedHashMap<String, String[]> informationMap) {
		infomap = informationMap;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setSound(String soundLabel) {
		sound = soundLabel;
	}

	/**
	 * {@inheritDoc}
	 */
	public Boolean getPauseMode() {
		return pauseMode;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setPauseMode(Boolean pauseMode) {
		this.pauseMode = pauseMode;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setForceNewPaint(boolean newPaint) {
		forcePaint = newPaint;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean getForceNewPaint() {
		return forcePaint;
	}

}
