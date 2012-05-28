package de.tu_darmstadt.gdi1.bomberman.game.elements;

import javax.swing.ImageIcon;

import de.tu_darmstadt.gdi1.framework.interfaces.IBoardElement;

/**
 * Basisklasse für alle Boardelemente, also die Dinge, die auf dem Spielfeld sein können. Jede der
 * Bomberman Boardelement-Klassen muss hiervon erben.
 */
public abstract class GameElement implements IBoardElement {
	private static final long serialVersionUID = 4973814294335529190L;

	@Override
	public abstract GameElement clone();

	@Override
	public abstract boolean equals(final Object obj);
	
	public abstract ImageIcon getImageIcon ();

	public abstract String getDescription ();
	
	public abstract char getParsingSymbol();
}
