/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.tu_darmstadt.gdi1.bomberman.gui;

/**
 *
 * @author Steffen Müller
 */
public class ControllerInputEvent extends ControllerEvent {

	/**
	 * Welche Funktion die gedrückte Taste hat
	 */
	public enum button {
		NULL,
		UP,
		DOWN,
		LEFT,
		RIGHT,
		BOMB,
		MAIN_MENU
	};

	/**
	 * Ob die Taste gedrückt oder losgelassen wurde
	 */
	public enum state {
		PRESSED,
		RELEASED
	}

	protected int playerIndex;
	protected button btn;
	protected state ste;

	public ControllerInputEvent (int playerIdx, button button, state state) {
		super(ControllerEvent.type.PLAYER_INPUT);

		playerIndex = playerIdx;
		btn = button;
		ste = state;
	}

	/**
	 * Gibt eine laufende Nummer des Spielers zurück, den das Event betrifft. Die Zahl liegt zwischen
	 * 1 und 4.
	 * @return INteger zwischen 1 und 4 (inklusive).
	 */
	public int getPlayerIndex () {
		return playerIndex;
	}

	/**
	 * GIbt den Button zurück, der gedrückt wurde. Besser gesagt: dessen Funktion, nicht den
	 * physikalischen Knopf.
	 * @return
	 */
	public button getButton () {
		return btn;
	}

	/**
	 * Gibt zurück, ob der Button gedrückt oder losgelassen wurde.
	 * @return
	 */
	public state getState () {
		return ste;
	}
}
