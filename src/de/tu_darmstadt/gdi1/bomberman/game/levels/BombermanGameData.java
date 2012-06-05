package de.tu_darmstadt.gdi1.bomberman.game.levels;

import de.tu_darmstadt.gdi1.bomberman.game.elements.GameElement;
import de.tu_darmstadt.gdi1.bomberman.game.elements.Player;
import de.tu_darmstadt.gdi1.framework.interfaces.IGameBoard;
import de.tu_darmstadt.gdi1.framework.model.GameData;
import de.tu_darmstadt.gdi1.framework.model.StepManager;
import de.tu_darmstadt.gdi1.framework.utils.Point;
import java.util.ArrayList;
import java.util.Collection;
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

	// Players /////////////////////////////////////////////////////////////////////////////////////

	public Collection<Player> getPlayers () {
		return players.values();
	}

	public Player getPlayer (int playerIdx) {
		return players.get(playerIdx);
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
