package de.tu_darmstadt.gdi1.framework.interfaces;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import de.tu_darmstadt.gdi1.framework.exceptions.InvalidLevelDataException;
import de.tu_darmstadt.gdi1.framework.model.GameBoard;
import de.tu_darmstadt.gdi1.framework.utils.level.Level;

/**
 * defines the functions of the level loader and saver. LevelInterfaces just parses and creates the String for savegames
 * but does not do the actual file storing.<br>
 * The key-feature of this class is to load levels, i.e. 
 * to convert a multi-line string to one {@link IGameData} object.
 * To get some game-depending information you have to hand in an {@link ILevelInformationProvider}.<br>
 * Another feature is the reverse way: storing an {@link IGameData}-object as multi-line string.
 * 
 *<br>  
 * <p>As a simplification you can also load/save savegames (means: serialization of a {@link IGameData}-object) here, too.<br>
 *  For this streams will be used instead of strings - but you have to open/close them externally.
 *  </p> 
 *  
 *  <p>
 *  Please use a {@link LevelManagerInterface} if you do not want to take care about file handling.
 *  </p>
 * 
 * @see {@link de.tu_darmstadt.gdi1.framework.interfaces.LevelInterface#saveGame(IGameData, java.io.OutputStream)}
 * @see  {@link de.tu_darmstadt.gdi1.framework.interfaces.LevelInterface#loadGame(java.io.InputStream)
 * @see {@link de.tu_darmstadt.gdi1.framework.interfaces.LevelInterface#loadLevel(String)
 * @see {@link de.tu_darmstadt.gdi1.framework.interfaces.LevelInterface#saveBoardAsLevel(IGameData)
 * @param <E> The type of BoardElements used
 * @author jonas
 */
public interface LevelInterface<E extends IBoardElement> {

	
	/** a special char for the storage process. <br>
	 * <ul>
	 * <li>this char will not stored at the end of a line.
	 * <li>lines only containing this char will be skipped
	 * <li>during loading, lines will be filled up with this char until all have the same length.
	 * <li>
	 * </ul>*/
	char SPACE = ' ';
	/**
	 * a regular expression which matches multiple occurrences of the special char.
	 * @see Level#SPACE */
	String SPACE_AS_REGEX = "\\s*";
	

	/**	
	 * Load a level from a string.<br>
	 *
	 * this string should contain multiple lines.<br>
	 * Each line represents one row of the board.<br>
	 * the resulting board will have the width of the largest row of the string.
	 * smaller rows will be filled at the end with {@link de.tu_darmstadt.gdi1.framework.interfaces.LevelInterface#SPACE}.<br>
	 * empty lines (only containing {@link de.tu_darmstadt.gdi1.framework.interfaces.LevelInterface#SPACE} tested with {@link de.tu_darmstadt.gdi1.framework.interfaces.LevelInterface#SPACE_AS_REGEX}) will be ignored.<br>
	 * for every character {@link ILevelInformationProvider#getBoardElementsFor(char)} will be called.<br>
	 * the result will be set as element of the {@link GameBoard} at this coordinate.<br>
	 * 
	 * <p>Notice: the counting starts at the left top,
	 * so the element at the left <i>bottom</i> will have as coordinates 0|<i>y</i> where y stands for the maximal height.<br>
	 * </p>
	 * <p>If a line starts with {@link ILevelInformationProvider#getLinePrefixForNonBoardLines()} this line will be skipped first.
	 * at the end all such lines are given (as array, without prefix) to {@link ILevelInformationProvider#verifyGameBoard(IGameBoard, String[])}.<br>
	 * </p>
	 * 
	 * @param data
	 *            the actual level as string
	 * @return the level as GameDataInterface object
	 * @throws InvalidLevelDataException
	 *             thrown if data is malformed
	 */
	IGameData<E> loadLevel (String data) throws InvalidLevelDataException;

	/**
	 * Writes the game to the OutputStream o via serialization.<br>
	 * Notice: this method will open an {@link java.io.ObjectOutputStream} to use {@link java.io.ObjectInputStream#writeObject()}
	 * 
	 * @param game
	 *            the IGameData object which to convert
	 * @param o
	 *            the output stream
	 * @throws java.io.IOException
	 *             something went wrong concerning outputstream
	 */
	void saveGame (IGameData<E> game, OutputStream o) throws IOException;

	/**
	 * Loads a {@link IGameData}-object from an inputstream is via deserialization.<br>
	 * Notice: this method will open an {@link java.io.ObjectInputStream} to use {@link java.io.ObjectInputStream#readObject()}<br>
	 * the loaded {@link IGameData}-instance will be returned. <br>
	 * 
	 * <p>
	 * <strong>Warning:</strong>
	 * This Method returns a IGameData<strong><u>&lt;E&gt;</u></strong> - but only a plain <code>Object</code> can be read from the file.<br>
	 * If the read Object does not implement {@link IGameData} a {@link InvalidLevelDataException} will be thrown.<br>
	 * We can <i>not</i> check if the read Object is also of the generic Type <code>&lt;E&gt;</code> because generic informations are erased at runtime.<br>
	 * You have to check this by your own.
	 * </p>
	 * 
	 * @param is
	 *            the input stream to read from
	 * @return an instance of a game saved in is
	 * @throws java.io.IOException
	 *             something went wrong concerning inputstream
	 * @throws InvalidLevelDataException
	 *             thrown if this is a corrupt save game.
	 *             e.g. if the stored object is not a extension form the the expected type {@link IGameData}
	 */
	IGameData<E> loadGame (InputStream is) throws IOException, InvalidLevelDataException;

	/**
	 * Converts the current state on board to a string which can be parsed as a new level.<br>
	 * All information about the player and the play (until call of the method) will be lost.
	 * 
	 * <p>
	 * If you want to create a save game to be able to reload it later without any loss of informations, please see
	 * {@link de.tu_darmstadt.gdi1.framework.interfaces.LevelInterface#saveGame(IGameData, java.io.OutputStream)}.
	 * </p>
	 * 
	 * <p>
	 * What does "level" mean?<br>
	 * A level is defined here as storage of all relevant informations of a map - without containing anything from the
	 * current player or the current game state. <br>
	 * e.g. if you want to store a race game as level you would store the length and appearance of the speedway but
	 * <i>not</i> where a car is or the elapsed time.
	 * </p>
	 * 
	 * @param gameData
	 *            the IGameData which shall be parsed
	 * @return a string which represents the current configuration on the board as a level
	 * @throws InvalidLevelDataException
	 *             if the current configuration on board is not a valid configuration for a level file
	 */
	String saveBoardAsLevel (IGameData<E> gameData) throws InvalidLevelDataException;

}
