package de.tu_darmstadt.gdi1.bomberman.game.levels;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.tu_darmstadt.gdi1.bomberman.BombermanController;
import de.tu_darmstadt.gdi1.bomberman.game.elements.Floor;
import de.tu_darmstadt.gdi1.bomberman.game.elements.GameElement;
import de.tu_darmstadt.gdi1.bomberman.game.elements.Player;
import de.tu_darmstadt.gdi1.bomberman.game.elements.Stone;
import de.tu_darmstadt.gdi1.bomberman.game.elements.Wall;
import de.tu_darmstadt.gdi1.framework.exceptions.InvalidLevelDataException;
import de.tu_darmstadt.gdi1.framework.interfaces.IGameBoard;
import de.tu_darmstadt.gdi1.framework.interfaces.IGameData;
import de.tu_darmstadt.gdi1.framework.interfaces.ILevelInformationProvider;

/**
 * Der Level Information Provider ist der Leveldateiparser des Spiels. Er erhält
 * vom Framework vorverarbeitete Level Informationen und kann diese dann prüfen
 * und parsen.
 */
public class BombermanLevelInformationProvider implements ILevelInformationProvider<GameElement> {

    Logger logger = Logger.getLogger(BombermanController.class.getName());

    /**
     * Parst ein Zeichen aus der Datei und gibt die BoardElements für dieses
     * Zeichen zurück.
     *
     * @param c Das Zeichen.
     * @return
     * @throws InvalidLevelDataException
     */
    @Override
    public List<GameElement> getBoardElementsFor(char c) throws InvalidLevelDataException {
        // Liste zum stacken der Dinge auf einem Grid (Bsp.: Bodenelement + PowerUp + Stein)
        List<GameElement> l = new LinkedList<GameElement>();
        // logger.log(Level.INFO, "Char: " + c);

        // Allgemein: Boden
        l.add(new Floor());
        switch (c) {
            // unzerstörbare Wand
            case '#':
                l.add(new Wall());
                break;
            // zerstörbarer Stein
            case '*':
                l.add(new Stone());
                break;
            // Spieler 1 auf leerem Feld
            case '1':
                l.add(new Player(1));
                break;
            // Spieler 1 auf leerem Feld
            case '2':
                l.add(new Player(2));
                break;
            // Spieler 1 auf leerem Feld
            case '3':
                l.add(new Player(3));
                break;
            // Spieler 1 auf leerem Feld
            case '4':
                l.add(new Player(4));
                break;
        }

        return l;
    }

    /**
     * Der letzte Verarbeitungsschritt - setzt die über die anderen Funktionen
     * gewonnenen Informationen zu einem Gamedata Objekt zusammen.
     *
     * @param gameBoard
     * @param lineCount
     * @param nonBoardInformations
     * @return
     * @throws InvalidLevelDataException
     */
    @Override
    public IGameData<GameElement> verifyGameBoard(IGameBoard<GameElement> gameBoard, int lineCount, Map<Integer, String> nonBoardInformations) throws InvalidLevelDataException {
        BombermanLevelValidator validator = new BombermanLevelValidator(gameBoard);
        if (!validator.isGameBoardValid()) {
            throw new InvalidLevelDataException(validator.getErrors());
        }
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
        // Das am höchsten gelegene GameElement im Stack entscheidet über das zu parsende Zeichen
        return field.get(field.size() - 1).getParsingSymbol();
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
