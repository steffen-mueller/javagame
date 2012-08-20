package de.tu_darmstadt.gdi1.bomberman.game.levels;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import de.tu_darmstadt.gdi1.bomberman.game.elements.GameElement;
import de.tu_darmstadt.gdi1.bomberman.game.elements.Player;
import de.tu_darmstadt.gdi1.framework.exceptions.InvalidLevelDataException;
import de.tu_darmstadt.gdi1.framework.exceptions.NoNextLevelException;
import de.tu_darmstadt.gdi1.framework.interfaces.IBoard;
import de.tu_darmstadt.gdi1.framework.interfaces.IGameData;
import de.tu_darmstadt.gdi1.framework.utils.level.LevelManager;

/**
 * Der LevelManager verwaltet ... Levels. Uha. Aktuell wird hier vor allem der
 * BombermanLevelInformationProvider an das Framework geklebt.
 */
public class BombermanLevelManager extends LevelManager<GameElement>  {
	HashMap<Integer,Player> players = new HashMap<Integer, Player>();
        boolean generate;
        final int BOARDSIZE = 12;
        final int NO_OF_PLAYER = 4;
	
	public BombermanLevelManager (boolean randomize)
	{
		super(new BombermanLevelInformationProvider());
		generate = randomize;
	}

	public BombermanLevelManager ()
	{
		super(new BombermanLevelInformationProvider());
		generate = false;
	}

	public HashMap<Integer,Player> getPlayers()
	{
		return players;
	}

	@Override
	public IGameData<GameElement> loadNextLevel () throws NoNextLevelException, InvalidLevelDataException, IOException
	{
        IGameData<GameElement> gd;
        if (generate){
            gd = super.loadLevel(BombermanLevelGenerator.generateGrid(BOARDSIZE, NO_OF_PLAYER));
        } else {
			gd = super.loadNextLevel();
        }
		IBoard<GameElement> gb = gd.getStepManager().getCurrentBoard();
		players = new HashMap<Integer,Player>();
		for (int x = 0; x < gb.getWidth(); x++) {
			for (int y = 0; y < gb.getHeight(); y++) {
				List<GameElement> list = gb.getElements(x,y);
				if (list.get(list.size()-1) instanceof Player) {
					Player player = (Player) list.get(list.size()-1);
					player.setCoordinates(x,y);
					players.put(player.getPlayerID(), player);
				}
			}
		}

		return gd;
	}

    @Override
    public IGameData<GameElement> loadCurrentAgain() throws InvalidLevelDataException, IOException {
        IGameData<GameElement> gd;
        if (generate) {
            gd = super.loadLevel(BombermanLevelGenerator.generateGrid(BOARDSIZE, NO_OF_PLAYER));
        } else {
            gd = super.loadCurrentAgain();
        }
        return gd;
    }
}
