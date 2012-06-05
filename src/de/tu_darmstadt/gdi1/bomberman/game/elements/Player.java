package de.tu_darmstadt.gdi1.bomberman.game.elements;

import de.tu_darmstadt.gdi1.framework.interfaces.IGameBoard;
import de.tu_darmstadt.gdi1.framework.model.GameBoard;
import de.tu_darmstadt.gdi1.framework.utils.Point;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;

/**
 * javadoc: Not yet commented
 *
 * @version $Id$
 */
public class Player extends GameElement
{
	/**
	 * In welche Richtung bewegt sich der Spieler aktuell?
	 */
	public enum direction {
		NULL,
		UP,
		DOWN,
		LEFT,
		RIGHT
	};

	private direction dir = direction.NULL;
	private int playerID;
	private long nextMoveAllowedTick = 0;
	private long moveDelay = 4;
	private int x;
	private int y;
	private IGameBoard<GameElement> gameBoard = null;

	public Player (int playerID)
	{
		this.playerID = playerID;
	}

	// GETTER SETTER PARTAY ////////////////////////////////////////////////////////////////////////

	public int getPlayerID ()
	{
		return playerID;
	}

	public void setGameBoard (IGameBoard<GameElement> gb) {
		gameBoard = gb;
	}

	public direction getDirection () {
		return dir;
	}

	public void setDirection (direction d) {
		dir = d;
	}

	// LOGIC ///////////////////////////////////////////////////////////////////////////////////////

	public boolean move (long ticknumber) {
		// Prüfen: darf ich mich überhaupt wieder bewegen?
		if (dir == direction.NULL || ticknumber < nextMoveAllowedTick)
			return false;

		// Die Richtung in Koordinaten übersetzen
		int newX = this.x, newY = this.y;
		switch (dir) {
			case UP: newY -= 1; break;
			case DOWN: newY += 1; break;
			case LEFT: newX -= 1; break;
			case RIGHT: newX += 1; break;
		}

		// Check that we do not walk off the board
		if (!gameBoard.checkCoordinates(newX, newY))
			return false;

		// Get who is already where we want to go
		List<GameElement> present = gameBoard.getElements(this.x, this.y);
		List<GameElement> target = gameBoard.getElements(newX, newY);

		// Collision detection baby!
		for (GameElement t : target) {
			if (t.isSolid())
				return false;
		}

		// Perform the move
		System.out.println("Player "+getPlayerID()+" moves to ("+newX+","+newY+")");
		present.remove(this);
		gameBoard.setElements(x, y, present);
		target.add(this);
		gameBoard.setElements(newX, newY, target);
		this.x = newX;
		this.y = newY;

		nextMoveAllowedTick = ticknumber + moveDelay;
		return true;
	}

	// STAMMDATEN: PARSING, ICON, CLONING ... //////////////////////////////////////////////////////

	@Override
	public GameElement clone ()
	{
		return new Player(this.playerID);
	}

	@Override
	public boolean equals (Object obj)
	{
		if (obj instanceof Player) {
			if (((Player) obj).playerID == this.playerID) {
				return true;
			}
		}
		return false;
	}

	@Override
	public ImageIcon getImageIcon ()
	{
		if (playerID == 1)
			return new ImageIcon("resource/images/player_white.png");
		else if (playerID == 2)
			return new ImageIcon("resource/images/player_red.png");
		else if (playerID == 3)
			return new ImageIcon("resource/images/player_black.png");
		else
			return new ImageIcon("resource/images/player_blue.png");
	}

	@Override
	public String getDescription ()
	{
		if (playerID == 1)
			return "Player White";
		else if (playerID == 2)
			return "Player Red";
		else if (playerID == 3)
			return "Player Black";
		else
			return "Player Blue";
	}

	@Override
	public char getParsingSymbol ()
	{
		if (playerID == 1)
			return '1';
		else if (playerID == 2)
			return '2';
		else if (playerID == 3)
			return '3';
		else
			return '4';
	}

	public void setCoordinates (int x, int y)
	{
		this.x = x;
		this.y = y;
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
}
