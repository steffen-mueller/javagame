package de.tu_darmstadt.gdi1.bomberman.game.levels;

import de.tu_darmstadt.gdi1.bomberman.game.elements.GameElement;
import de.tu_darmstadt.gdi1.bomberman.game.elements.Player;
import de.tu_darmstadt.gdi1.framework.interfaces.IGameBoard;
import de.tu_darmstadt.gdi1.framework.model.GameData;
import de.tu_darmstadt.gdi1.framework.model.StepManager;
import de.tu_darmstadt.gdi1.framework.utils.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Der gesamte Zustand eines Bomberman Spiels. Hierauf arbeitet die BombermanGame Klasse.
 */
public class BombermanGameData extends GameData<GameElement> {

	/**
	 * Contains all players the level contains.
	 */
	protected HashMap<Integer, Player> players = new HashMap<Integer, Player>();


	public BombermanGameData (IGameBoard<GameElement> gameBoard)
	{
		super(new StepManager<GameElement>(gameBoard));

		// Scan the game field for players and the like
		for (int x = 0; x < gameBoard.getWidth(); x++) {
			for (int y = 0; y < gameBoard.getHeight(); y++) {
				List<GameElement> elements = gameBoard.getElements(x, y);
				for (GameElement el : elements) {
					if (el instanceof Player) {
						Player pl = (Player) el;
						players.put(pl.getPlayerID(), pl);
						pl.setCoordinates(x, y);
						pl.setGameBoard(gameBoard);
					}
				}
			}
		}
	}

	// Gamedata Ticking - where the real magic happens /////////////////////////////////////////////

	/**
	 * Called each tick by the BombermanGame. This is where the state of the game may be
	 * manipulated.
	 * @param tickCount
	 * @return True if the game board needs to be repainted.
	 */
	public ArrayList<Point> tick (long tickCount) {
		ArrayList<Point> dirtyList = new ArrayList<Point>();

		// Make each player move if he needs to
		for (Player pl : players.values()) {
			pl.move(tickCount, dirtyList);
		}

		return dirtyList;
	}

	// Control interface ///////////////////////////////////////////////////////////////////////////

	/**
	 * Bewegt den Spieler in die angegebene Richtung. Der Tickcount ist die Nummer des aktuellen Ticks,
	 * wird für die Beschleunigung benötigt.
	 * Die Funktion gibt eine Liste von Koordinaten zurück, die durch die Bewegung dreckig geworden
	 * sind und neu gezeichnet werden müssen - das sind normalerweise zwei Felder oder keines.
	 * @param playerIdx
	 * @param dir
	 * @param tickCount
	 * @return
	 */
	public ArrayList<Point> movePlayer (int playerIdx, Player.direction dir, long tickCount) {
		ArrayList<Point> dirtyList = new ArrayList<Point>();

		Player pl = players.get(playerIdx);
		if (pl != null) {
			pl.setDirection(dir);
			pl.move(tickCount, dirtyList);
		}

		return dirtyList;
	}

	/**
	 * Lässt den Spieler aufhören, in die angegebene Richtung zu laufen. Der interessierte Leser
	 * fragt sich vielleicht, warum diese Checks nötig sind. Antwort: Spieler drücken gerne mal
	 * zwei Tasten gleichzeitig, wenn die Richtung dreht - Also "Oben" ist noch gedrückt, der Spieler
	 * drückt aber schon "Links" und lässt dann erst "Oben" los. Würden wir hier die Richtung nicht
	 * checken, würden wir mit dem Loslassen von "Oben" die Bewegung nach Links stoppen. Bam.
	 * @param playerIdx
	 * @param dir
	 */
	public void removePlayerDirection (int playerIdx, Player.direction dir) {
		Player pl = players.get(playerIdx);
		if (pl != null && pl.getDirection() == dir) {
			pl.setDirection(Player.direction.NULL);
		}
	}

	// Gamedata Interface //////////////////////////////////////////////////////////////////////////

	@Override
	public boolean isLost() {
		return false;
	}

	@Override
	public boolean isPaused() {
		return false;
	}

	@Override
	public boolean isRunning() {
		return true;
	}

	@Override
	public boolean isWon() {
		return false;
	}

}
