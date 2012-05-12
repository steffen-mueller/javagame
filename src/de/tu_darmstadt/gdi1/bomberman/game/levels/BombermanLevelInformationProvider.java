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
 * Der Level Information Provider ist der Leveldateiparser des Spiels. Er erhält vom Framework
 * vorverarbeitete Level Informationen und kann diese dann prüfen und parsen.
 */
public class BombermanLevelInformationProvider implements ILevelInformationProvider<GameElement> {

	/**
	 * Parst ein Zeichen aus der Datei und gibt die BoardElements für dieses Zeichen zurück.
	 * @param c Das Zeichen.
	 * @return
	 * @throws InvalidLevelDataException
	 */
	@Override
	public List<GameElement> getBoardElementsFor(char c) throws InvalidLevelDataException {
		// TODO
		List<GameElement> l = new LinkedList<GameElement>();
		return l;
	}

	/**
	 * Der letzte Verarbeitungsschritt - setzt die über die anderen Funktionen gewonnenen
	 * Informationen zu einem Gamedata Objekt zusammen.
	 * @param gameBoard
	 * @param lineCount
	 * @param nonBoardInformations
	 * @return
	 * @throws InvalidLevelDataException
	 */
	@Override
	public IGameData<GameElement> verifyGameBoard(IGameBoard<GameElement> gameBoard, int lineCount, Map<Integer, String> nonBoardInformations) throws InvalidLevelDataException {
		// TODO
		return new BombermanGameData();
	}

	/**
	 *
	 * @param field
	 * @return
	 * @throws InvalidLevelDataException
	 */
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
