package de.tu_darmstadt.gdi1.bomberman.game.levels;

import de.tu_darmstadt.gdi1.bomberman.game.elements.GameElement;
import de.tu_darmstadt.gdi1.framework.exceptions.InvalidLevelDataException;
import de.tu_darmstadt.gdi1.framework.interfaces.IGameBoard;
import de.tu_darmstadt.gdi1.framework.interfaces.IGameData;
import de.tu_darmstadt.gdi1.framework.interfaces.ILevelInformationProvider;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Der Level Information Provider erhält vorgeparste Level Informationen und kann diese dann
 * prüfen und parsen, bis ein brauchbarer bomberman Level rauskommt.
 */
public class BombermanLevelInformationProvider implements ILevelInformationProvider<GameElement> {

	@Override
	public List<GameElement> getBoardElementsFor(char c) throws InvalidLevelDataException {
		// TODO
		List<GameElement> l = new LinkedList<GameElement>();
		return l;
	}

	@Override
	public IGameData<GameElement> verifyGameBoard(IGameBoard<GameElement> gameBoard, int lineCount, Map<Integer, String> nonBoardInformations) throws InvalidLevelDataException {
		// TODO
		return new BombermanGameData();
	}

	@Override
	public char parseField(List<GameElement> field) throws InvalidLevelDataException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public String getLinePrefixForNonBoardLines() {
		// TODO: kann aber auch so bleiben?
		return "#";
	}

	@Override
	public String getNoneBoardInformationLineFor(IGameData<GameElement> gameData) throws InvalidLevelDataException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

}
