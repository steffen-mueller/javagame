package de.tu_darmstadt.gdi1.bomberman.framework;

import de.tu_darmstadt.gdi1.framework.events.DefaultControllerEvent;

/**
 *
 */
public class ControllerEvent extends DefaultControllerEvent {
	public enum type {
		USER_QUIT
	};

	type myType;
	public ControllerEvent (type t) {
		myType = t;
	}

	public type getType ()
	{
		return myType;
	}

	static public ControllerEvent create (type t) {
		return new ControllerEvent(t);
	}
}
