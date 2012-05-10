package de.tu_darmstadt.gdi1.framework.interfaces;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import de.tu_darmstadt.gdi1.framework.exceptions.InvalidLevelDataException;
import de.tu_darmstadt.gdi1.framework.exceptions.NoNextLevelException;
import de.tu_darmstadt.gdi1.framework.model.GameData;


/**
 * Level Manager which gives you a next level and manages the order of the levels.<br>

 * A LevelManager works with a {@link LevelInterface}.<br>
 * The LevelManager will handle everything around files - where is the next file and what is the "next" file
 * and how to open a stream, etc.<br>
 * The {@link LevelInterface} has to deal with the content of this files.<br>
 * 
 * <p>
 * please see {@link de.tu_darmstadt.gdi1.framework.interfaces.LevelManagerInterface#getLevelNumber()} for information about expected file-names.
 * </p>
 * @author jonas
 * @param <E> The type of BoardElements used
 * 
 */
public interface LevelManagerInterface<E extends de.tu_darmstadt.gdi1.framework.interfaces.IBoardElement> {

	/**
	 * resets the level manager.
	 * 
	 * @param path
	 *            defines the path where you get the level files.
	 */
	void reset (File path);

	/**
	 * gives you the next level.<br>
	 * Will use {@link LevelInterface#loadLevel(String)} when a proper "next file" was found and read as string.<br>
	 * <br>
	 * Next in the meaning of "the next file of the current directory" and next of directory means:<br>
	 * The files of the current directory will be sorted alphabetically. Then the next to the current level will be
	 * searched.<br>
	 * <p>
	 * This method will use {@link LevelInterface#loadLevel(String)} as said above. <br>
	 * Please note that this means you get only level-files - not save-games - 
	 * even if you had loaded a save game. If you load a save game the "current directory" will be changed.<br>
	 * So if you load by hand a save game and call than this method - in the directory of the save game where only
	 * other save games exist (or nothing else (readable)) - this method will fire {@link NoNextLevelException}.
	 * </p>
	 * 
	 * 
	 * <p>
	 * Notice: The LevelManager only find files which are:
	 * <ul>
	 * <li>ending with <code>.txt</code>
	 * <li>not starting with <code>.</code>
	 * <li>not named <code>highscore.txt</code>
	 * </ul>
	 * </p>
	 * <p>
	 * Notice: if you want to get the level number (see {@link de.tu_darmstadt.gdi1.framework.interfaces.LevelManagerInterface#getLevelNumber()}) your level files
	 * should fit a special name format.
	 * </p>
	 * 
	 * @return the loaded {@link IGameData}
	 * 
	 * @throws NoNextLevelException
	 *             thrown if there is no next level
	 * @throws java.io.IOException
	 *             thrown if file could not be loaded
	 * @throws InvalidLevelDataException
	 *             thrown if the level file doesn't specify a valid level
	 */
	IGameData<E> loadNextLevel () throws NoNextLevelException, InvalidLevelDataException,
			IOException;

	/**
	 * gives you the current level again, for example if the user wants to restart.
	 * 
	 * <p> works obviously only for levels/save-games you load with this {@link de.tu_darmstadt.gdi1.framework.interfaces.LevelManagerInterface}.<br>
	 * </p> 
	 * 
	 * @return the loaded {@link IGameData}
	 * 
	 * @throws java.io.IOException
	 *             thrown if file could not be loaded
	 * @throws InvalidLevelDataException
	 *             thrown if the level file doesn't specify a valid level
	 */
	IGameData<E> loadCurrentAgain () throws InvalidLevelDataException, IOException;

	/**
	 * sets the path of the current level. This is used if a level is loaded externally.<br>
	 * If the path you hand in does not exists you will get a {@link java.io.FileNotFoundException}. In this case the "current" path will stay unchanged.
	 * 
	 * @param path
	 *            path to the current level.
	 * @throws java.io.FileNotFoundException
	 * 			if the given path does not exists.
	 */
	void setCurrentLevelPath (String path) throws FileNotFoundException;

	/**
	 * loads the level from the specified file.<br>
	 * This method will open the file and read it - the conversion to a {@link GameData} object will be done by {@link LevelInterface#loadLevel(String)}.
	 * 
	 * <p>
	 * if you want to load a save game (including information about a player) please see {@link de.tu_darmstadt.gdi1.framework.interfaces.LevelManagerInterface#loadGame(java.io.File)}.
	 * </p>
	 * <p>
	 * What does "level" mean?<br>
	 * A level is defined here as storage of all relevant information of a map - without containing anything about the
	 * current player or the current game state. <br>
	 * e.g. if you want to store a race game as level you would store the length and appearance of the speedway but
	 * <i>not</i> where a car is or the elapsed time.
	 * </p>
	 * 
	 * @return the loaded {@link IGameData}
	 * 
	 * @param f
	 *            file to read the level data
	 * @throws InvalidLevelDataException
	 *             thrown if the file doesn't specify a correct level.
	 * @throws java.io.IOException
	 *             something went wrong when accessing the file
	 */
	IGameData<E> loadLevel (File f) throws InvalidLevelDataException, IOException;

	/**
	 * saves the current configuration on the board as
	 * a level: information about playername/steps/time/etc will be lost.
	 * the resulting save-file is in the same format as files for new levels. 
	 * 
	 * <p>
	 * What does "level" mean?<br>
	 * A level is defined here as storage of all relevant informations of a map - without containing anything from the
	 * current player or the current game state. <br>
	 * e.g. if you want to store a race game as level you would store the length and appearance of the speedway but
	 * <i>not</i> where a car is or the elapsed time.
	 * </p>
	 * 
	 * @param f
	 *            file to save to
	 * @param board
	 *            board that should be saved
	 * @throws java.io.IOException
	 *             something went wrong when accessing the file
	 * @throws InvalidLevelDataException
	 *             parsing the board failed
	 * @see {@link de.tu_darmstadt.gdi1.framework.interfaces.LevelManagerInterface#saveGame(java.io.File, IGameData)}
	 */
	void saveBoardAsLevel (File f, IGameData<E> board) throws IOException, InvalidLevelDataException;

	/**
	 * saves a game to a specified location.<br>
	 * the given game data instance will be serialized and written into the given file.<br>
	 * All informations about playername/played time/steps/etc will be kept.
	 * 
	 * @param f
	 *            location to save.
	 * @param game
	 *            game instance to save.
	 * @return false if the game couldn't be saved
	 */
	boolean saveGame (File f, IGameData<E> game);

	/**
	 * loads a savegame from the specified location.<br>
	 * <p>
	 * Will change the "current directory". (relevant for {@link de.tu_darmstadt.gdi1.framework.interfaces.LevelManagerInterface#loadNextLevel()}).
	 * </p>
	 * <p>
	 * If you load a saved game with this method you <b>will be able</b> to reload this saved game with {@link de.tu_darmstadt.gdi1.framework.interfaces.LevelManagerInterface#loadCurrentAgain()}.
	 * </p>
	 * 
	 * @return the loaded {@link IGameData}
	 * 
	 * @param f
	 *            location to load
	 * @throws java.io.IOException
	 *             something went wrong with file
	 * @throws InvalidLevelDataException
	 *             this is a corrupt save game
	 */
	IGameData<E> loadGame (File f) throws IOException, InvalidLevelDataException;

	/**
	 * returns the filename where the current level is loaded from.
	 * 
	 * @return filename of current level
	 */
	String getCurrentLevelName ();

	
	/**
	 * the path (directory) of the last loaded level.
	 * 
	 * @return
	 * 		the directory of the last loaded level.
	 */
	File getPath ();
	
	/**
	 * returns the number of the current level.<br>
	 * Notice:
	 * Your level-files should be named in the format:<br> 
	 * <tt>level_(\\d+)\\.txt</tt><br>
	 * Example: <tt>level_01.txt</tt> or <tt>level_55.txt</tt>.
	 * <br>If they have other names this method will return the (alphabetical) index from the current level file in the current directory.<br>
	 * Example:<br>
	 * Your current level file is named <tt>bTestLevel.lvl</tt> and in this directory were also the files
	 * <tt>otherTestLevel.lvl</tt>, <tt>aTestLevel.lvl</tt>, <tt>TestLevelWithAnotherEnding.tmp</tt> and <tt>Highscore.dat</tt>.<br>
	 * So your number will be 2, because only <tt>aTestLevel.lvl</tt> is before the current levelfile name. 
	 * 
	 * @return
	 * 	number of levels returned since reset.
	 * 
	 */
	int getLevelNumber ();
	

	/**
	 * A getter for the {@link LevelInterface} object which helps this {@link de.tu_darmstadt.gdi1.framework.interfaces.LevelManagerInterface} to deal with Strings.<br>
	 * 
	 * @return the Object which helps this {@link de.tu_darmstadt.gdi1.framework.interfaces.LevelManagerInterface} to deal with loaded Strings.
	 * @see LevelInterface
	 */
	LevelInterface<E> getLevel ();

}
