package de.tu_darmstadt.gdi1.framework.events;

import de.tu_darmstadt.gdi1.framework.exceptions.FrameworkError;
import de.tu_darmstadt.gdi1.framework.interfaces.IControllerEvent;

/**
 * This is a default implementation of {@link IControllerEvent}.
 * It provides storage for two integers and a string,
 * including getters, setters, and setting constructors.
 * It can be used to extend own events from it or for simple purposes.
 * <p>
 * A class inheriting from this class has to respect the limitations from {@link #clone()} - or has to override {@link #clone()} completely.
 * </p>
 * 
 * @see #clone()
 * @author Jan
 *
 */
public class DefaultControllerEvent implements IControllerEvent {

	private int int1;
	private int int2;
	private String str;
	
	public DefaultControllerEvent() {
	}

	public DefaultControllerEvent(final String str) {
		this.str = str;
	}

	public DefaultControllerEvent(final String str, final int int1) {
		this.str = str;
		this.int1 = int1;
	}

	public DefaultControllerEvent(final String str, final int int1, final int int2) {
		this.str = str;
		this.int1 = int1;
		this.int2 = int2;
	}

	public DefaultControllerEvent(final int int1) {
		this.int1 = int1;
	}

	public DefaultControllerEvent(final int int1, final int int2) {
		this.int1 = int1;
		this.int2 = int2;
	}

	/**
	 * Please see for general informations about clone: {@link Object#clone()}<br>
	 * <p>
	 * To create a new instance <code>this.getClass().newInstance();</code> will be used.<br>
	 * If you override this class you have to decide if you want to: override clone completely OR provide a constructor
	 * with no parameters.<br>
	 * If you did none of this you will get a {@link InstantiationException} or {@link IllegalAccessException} (boxed in a {@link FrameworkError}) if you
	 * call <code>clone()</code>.
	 * </p>
	 * 
	 * @return a new instance of this object, having all fields of {@link de.tu_darmstadt.gdi1.framework.events.DefaultControllerEvent} like this instance.<br>
	 *         If you "only" have a {@link de.tu_darmstadt.gdi1.framework.events.DefaultControllerEvent} you get a full deep copy of it.<br>
	 *         If you have extended the {@link de.tu_darmstadt.gdi1.framework.events.DefaultControllerEvent} you get the same class (if you provide a
	 *         constructor without arguments), but obviously you have to set copies of the fields you have extended by your own.
	 */
	public DefaultControllerEvent clone() {
		DefaultControllerEvent event;
		try {
			event = this.getClass().newInstance();
			event.setIntOne(this.getIntOne());
			event.setIntTwo(this.getIntTwo());
			event.setString(this.getString());
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
	

	public int getIntOne() {
		return int1;
	}


	public int getIntTwo() {
		return int2;
	}


	public String getString() {
		return str;
	}


	public void setIntOne(final int intOne) {
		int1 = intOne;
	}


	public void setIntTwo(final int intTwo) {
		int2 = intTwo;
	}


	public void setString(final String string) {
		str = string;
	}

}
