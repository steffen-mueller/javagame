package de.tu_darmstadt.gdi1.bomberman.game.elements;

import de.tu_darmstadt.gdi1.bomberman.game.levels.BombermanGameData;
import javax.swing.ImageIcon;

import de.tu_darmstadt.gdi1.framework.interfaces.IBoardElement;
import de.tu_darmstadt.gdi1.framework.interfaces.IGameBoard;
import de.tu_darmstadt.gdi1.framework.utils.Point;
import java.util.List;

/**
 * Basisklasse für alle Boardelemente, also die Dinge, die auf dem Spielfeld sein können. Jede der
 * Bomberman Boardelement-Klassen muss hiervon erben.
 */
public abstract class GameElement implements IBoardElement {
	private static final long serialVersionUID = 4973814294335529190L;

	@Override
	public abstract GameElement clone();

	protected IGameBoard<GameElement> gameBoard = null;
	protected BombermanGameData gameData = null;

	protected int x;
	protected int y;

	public void setGameBoard (IGameBoard<GameElement> gb) {
		gameBoard = gb;
	}

	public void setGameData (BombermanGameData data) {
		gameData = data;
	}

	public void setCoordinates (int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	public void setCoordinates (Point p) {
		this.x = p.getX();
		this.y = p.getY();
	}

	public int getX () {
		return x;
	}

	public int getY () {
		return y;
	}

	public Point getPoint () {
		return new Point(x,y);
	}

	// Behavior ////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Returns whether players are blocked by this element, e.g. cannot pass.
	 * @return
	 */
	public boolean isSolid() {
		return false;
	}

	/**
	 * Returns whether this element can be destroyed with bombs.
	 * @return
	 */
	public boolean isDestroyable () {
		return false;
	}

	public void destroy () {
		List<GameElement> present = gameBoard.getElements(x, y);
		present.remove(this);
		gameBoard.setElements(x, y, present);
	}

	// Appearance //////////////////////////////////////////////////////////////////////////////////

	public abstract ImageIcon getImageIcon ();

	public abstract String getDescription ();
	
	public abstract char getParsingSymbol();
}
