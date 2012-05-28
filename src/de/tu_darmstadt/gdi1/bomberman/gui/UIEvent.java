package de.tu_darmstadt.gdi1.bomberman.gui;

import de.tu_darmstadt.gdi1.bomberman.game.elements.GameElement;
import de.tu_darmstadt.gdi1.framework.events.DefaultUserInterfaceEvent;

/**
 * Ein UIEvent ist ein Event, das an die GUI gesendet und dort verarbeitet werden
 * kann. Die Quelle ist im Normalfall ein Ereignis im Controller oder im Spiel.
 * Jedes Event hat einen Type, der angibt, was das Event darstellt.
 */
public class UIEvent extends DefaultUserInterfaceEvent<GameElement> {

	public enum type {
		NEW_GAME,
		QUIT_GAME,
		DETONATE_BOMB,
		REDRAW
	}

	private type theType;

	public UIEvent (type t)
	{
		theType = t;
	}

	public type getType ()
	{
		return theType;
	}

	static public UIEvent create (type t) {
		return new UIEvent(t);
	}
}
