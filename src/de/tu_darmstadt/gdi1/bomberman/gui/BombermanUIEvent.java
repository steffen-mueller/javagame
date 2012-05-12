package de.tu_darmstadt.gdi1.bomberman.gui;

import de.tu_darmstadt.gdi1.bomberman.game.elements.GameElement;
import de.tu_darmstadt.gdi1.framework.events.DefaultUserInterfaceEvent;

/**
 *
 * @author Steffen Müller
 */
public class BombermanUIEvent extends DefaultUserInterfaceEvent<GameElement> {

	public enum type {
		NEW_GAME,
		QUIT_GAME
	};

	private type theType;

	public BombermanUIEvent (type t)
	{
		theType = t;
	}

	public type getType ()
	{
		return theType;
	}
	
	static public BombermanUIEvent create (type t) {
		return new BombermanUIEvent(t);
	}
}
