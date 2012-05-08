package de.tu_darmstadt.gdi1.framework.interfaces;

import java.util.List;
import java.util.Map;

import de.tu_darmstadt.gdi1.framework.exceptions.InvalidLevelDataException;
import de.tu_darmstadt.gdi1.framework.model.GameBoard;
import de.tu_darmstadt.gdi1.framework.model.GameData;



/**
 * Interface for a connector between the generic level load and store part of the framework
 * (see {@link LevelInterface}) and a concrete game.<br>
 * The process of load or save a map/game is mostly generic.
 * The few pieces of information this process needs about a concrete
 * game are provided here.<br>
 * E.g. for the work with a {@link LevelManagerInterface} you have to implement this interface correctly.
 * 
 * @author jonas
 * @param <E> The type of BoardElements used
 * 
 */
public interface ILevelInformationProvider<E extends IBoardElement> {
	
    /**
     * Get the {@link IBoardElement}s for the specified area.<br>
     * "specified area" as one char, which could be loaded from a text-file.
     * @param c
     * 			the char
     * @return
     * 		copy of {@link IBoardElement}s.
     * @throws InvalidLevelDataException
     * 		if the given character is illegal.
	 * @see de.tu_darmstadt.gdi1.framework.interfaces.ILevelInformationProvider#parseField(java.util.List)
     */
	List<E> getBoardElementsFor (char c) throws InvalidLevelDataException;

	
	/**
	 * Creates a {@link IGameData}-instance for the given {@link IGameBoard}.<br>
	 * In this method the {@link IGameBoard} will be extended with all additional information from the file handed in as strings.
	 * Also the {@link IGameBoard} should be verified.<br>
	 *     
	 * @param gameBoard
	 * 			the board which should be verified and boxed into a {@link IGameData}-instance.
	 * @param lineCount
	 * 			the total number of lines in the file handed in
	 * @param nonBoardInformations
	 * 			every line of the loaded file which starts with {@link de.tu_darmstadt.gdi1.framework.interfaces.ILevelInformationProvider#getLinePrefixForNonBoardLines()} will be
	 * 			delivered here. Each entry in the map will consist of the line number as key and the whole line as a String as the value.
	 * 			Will be an empty map if there are no such lines in the file.<br>
	 * 			All lines will be <b>without</b> the prefix for non-board information (see {@link de.tu_darmstadt.gdi1.framework.interfaces.ILevelInformationProvider#getLinePrefixForNonBoardLines()}.
	 * @return
	 * 		the {@link IGameData} containing the given GameBoard and all information.
	 * @throws InvalidLevelDataException
	 * 			will be thrown if the given {@link GameBoard} or the additional information are not legal.
	 */
	IGameData<E> verifyGameBoard (IGameBoard<E> gameBoard, int lineCount, Map<Integer, String> nonBoardInformations) throws InvalidLevelDataException;
	
	
	/**
	 * looks what elements are on a board field and produces a char which
	 * represents them in a level file.
	 * @param field
	 * 			List of elements on the board field, as returned by {@link IBoard#getElements(int, int)}
	 * @return
	 * 			char for the level file
	 * @throws InvalidLevelDataException 
	 * 			thrown if the field hasn't a valid state in terms of
	 * 			ordinary level data
	 * @see de.tu_darmstadt.gdi1.framework.interfaces.ILevelInformationProvider#getBoardElementsFor(char)
	 */
	char parseField (final List<E> field) throws InvalidLevelDataException;
	

	/**
	 * Every line in the file which starts with this String does not contain normal board information.<br>
	 * Such lines will be given to {@link de.tu_darmstadt.gdi1.framework.interfaces.ILevelInformationProvider#verifyGameBoard(IGameBoard, String[])}<br>
	 * Must not start with {@link LevelInterface#SPACE}.<br>
	 * Additional information could be a time or step deadline, for example.
	 * Just information which are relevant for the game, but
	 * which are not directly connected to one field. 
	 * <p>
	 * Make sure that this prefix is no legal combination of chars which you expect in {@link de.tu_darmstadt.gdi1.framework.interfaces.ILevelInformationProvider#parseField(java.util.List)}.<br>
	 * If you fail to do so, board lines may be falsely identified as lines for additional information.
	 * </p>
	 * 
	 * @return
	 * 		the prefix of lines which does not contain board information, may not null or empty.
	 */
	String getLinePrefixForNonBoardLines ();
	
	/**
	 * 
	 * Extract every relevant information for a level-file from a {@link GameData} object as string.<br>
	 * This string has to be in such format that
	 * {@link de.tu_darmstadt.gdi1.framework.interfaces.ILevelInformationProvider#verifyGameBoard(IGameBoard, String[])} can work with it. <br>
	 * 
	 * 
	 * <p>
	 * Will be called when storing {@link GameData} as level.
	 * </p>
	 * 
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
	 *            the data to store.
	 * @return the informations of gameData as string - starting with
	 *         {@link de.tu_darmstadt.gdi1.framework.interfaces.ILevelInformationProvider#getLinePrefixForNonBoardLines()}
	 * @throws InvalidLevelDataException
	 *             if the given gameData object is not legal
	 * 
	 * @see LevelInterface#saveBoardAsLevel(IGameData)
	 * @see LevelManagerInterface#saveBoardAsLevel(java.io.File, IGameData)
	 * @see de.tu_darmstadt.gdi1.framework.interfaces.ILevelInformationProvider#verifyGameBoard(IGameBoard, String[])
	 * @see LevelManagerInterface#saveGame(java.io.File, IGameData)
	 */
	String getNoneBoardInformationLineFor (IGameData<E> gameData) throws InvalidLevelDataException;

}
