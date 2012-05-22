package de.tu_darmstadt.gdi1.bomberman.game.levels;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.tu_darmstadt.gdi1.bomberman.BombermanController;
import de.tu_darmstadt.gdi1.bomberman.game.elements.GameElement;
import de.tu_darmstadt.gdi1.bomberman.game.elements.Player;
import de.tu_darmstadt.gdi1.bomberman.game.elements.Wall;
import de.tu_darmstadt.gdi1.framework.exceptions.InvalidLevelDataException;
import de.tu_darmstadt.gdi1.framework.interfaces.IGameBoard;
import de.tu_darmstadt.gdi1.framework.interfaces.IGameData;
import de.tu_darmstadt.gdi1.framework.interfaces.ILevelInformationProvider;

/**
 * Der Level Information Provider ist der Leveldateiparser des Spiels. Er erhält vom Framework
 * vorverarbeitete Level Informationen und kann diese dann prüfen und parsen.
 */
public class BombermanLevelInformationProvider implements ILevelInformationProvider<GameElement> {

	Logger logger = Logger.getLogger(BombermanController.class.getName());

	/**
	 * Parst ein Zeichen aus der Datei und gibt die BoardElements für dieses Zeichen zurück.
	 * @param c Das Zeichen.
	 * @return
	 * @throws InvalidLevelDataException
	 */
	@Override
	public List<GameElement> getBoardElementsFor(char c) throws InvalidLevelDataException {
		// Liste zum stacken der Dinge auf einem Grid (Bsp.: Bodenelement + PowerUp + Stein)
		List<GameElement> l = new LinkedList<GameElement>();
		logger.log(Level.INFO, "Char: "+c);

		switch (c) {
			case '#':
				l.add(new Wall());
				break;
			case '+':
				l.add(new Wall());
				l.add(new Player());
				break;
			default:
				l.add(new Wall());
		}

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
		return new BombermanGameData(gameBoard);
	}

	/**
	 *
	 * @param field
	 * @return
	 * @throws InvalidLevelDataException
	 */
	@Override
	public char parseField(List<GameElement> field) throws InvalidLevelDataException {
		for (GameElement element : field) {
			if (element instanceof Wall) {
				return '#';
			}
			else if (element instanceof Player)
			{
				return '+';
			}
		}
		return '#';
	}

	@Override
	public String getLinePrefixForNonBoardLines() {
		return "//";
	}

	@Override
	public String getNoneBoardInformationLineFor(IGameData<GameElement> gameData) throws InvalidLevelDataException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

}
