package de.tu_darmstadt.gdi1.bomberman.gui;

import de.tu_darmstadt.gdi1.framework.events.DefaultControllerEvent;

/**
 * Ein ControllerEvent ist ein Event, das an den Controller gesendet und dort verarbeitet werden
 * kann. Die Quelle ist im Normalfall eine Benutzereingabe.
 * Jedes Event hat einen Type, der angibt, was das Event darstellt.
 */
public class ControllerEvent extends DefaultControllerEvent {
	// Die möglichen Arten von Events.
	public enum type {
		USER_QUIT,
		PLAYER_INPUT,
		CHANGE_SKIN
	};

	protected type myType;

	public ControllerEvent (type t) {
		myType = t;
	}

	public type getType () {
		return myType;
	}

	/**
	 * Factory für mehr Bequemlichkeit. Aktuell eher nutzlos.
	 * @param t
	 * @return
	 */
	static public ControllerEvent create (type t) {
		return new ControllerEvent(t);
	}
}
