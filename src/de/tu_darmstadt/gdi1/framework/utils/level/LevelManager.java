package de.tu_darmstadt.gdi1.framework.utils.level;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.tu_darmstadt.gdi1.framework.exceptions.InvalidLevelDataException;
import de.tu_darmstadt.gdi1.framework.exceptions.NoNextLevelException;
import de.tu_darmstadt.gdi1.framework.interfaces.IBoardElement;
import de.tu_darmstadt.gdi1.framework.interfaces.IGameData;
import de.tu_darmstadt.gdi1.framework.interfaces.ILevelInformationProvider;
import de.tu_darmstadt.gdi1.framework.interfaces.LevelInterface;
import de.tu_darmstadt.gdi1.framework.interfaces.LevelManagerInterface;
import de.tu_darmstadt.gdi1.framework.utils.FileUtility;
import de.tu_darmstadt.gdi1.framework.utils.FrameworkUtils;



/**
 * Level Manager that implements LevelManagerInterface. It's used by controller to load new levels.<br>
 * 
 * 
 * @author jonas
 */
public class LevelManager<E extends IBoardElement> extends Level<E> implements LevelManagerInterface<E> {

	/** if no level dir is specified, use this one. */
	private static final String DEFAULT_LEVEL_DIR = "resource/levels/";

	/** store the current level file. never null. */
	private String lastLevelFile = "";

	/** store the currently used path. With a file-separator at the end. never null. */
	private File myPath;

	/** was the last loaded "level" a save game? <br>
	 * helper for {@link de.tu_darmstadt.gdi1.framework.utils.level.LevelManager#loadCurrentAgain()} */
	private boolean lastWasSaveGame;
	

	/**
	 * Will only call {@link de.tu_darmstadt.gdi1.framework.utils.level.LevelManager#LevelManager(ILevelInformationProvider, String)}.
	 * As default level directory {@link de.tu_darmstadt.gdi1.framework.utils.level.LevelManager#DEFAULT_LEVEL_DIR} will be used.
	 * @param aLevelInfoProvider
	 *            a provider for some game depending informations.
	 * @see de.tu_darmstadt.gdi1.framework.utils.level.LevelManager#LevelManager(ILevelInformationProvider, String)
	 */
	public LevelManager(final ILevelInformationProvider<E> aLevelInfoProvider) {
		this(aLevelInfoProvider, DEFAULT_LEVEL_DIR);
	}

	/**
	 * 
	 * @param aLevelInfoProvider
	 * 		a provider for some game depending informations.
	 * @param levelDir
	 * 		the directory where by defaul the levels should be loaded.
	 * @throws IllegalArgumentException
	 * 		if the given levelDir does not exists.
	 */
	public LevelManager(final ILevelInformationProvider<E> aLevelInfoProvider, final String levelDir) {
		super(aLevelInfoProvider);
		
		try {
			myPath = FrameworkUtils.loadFile(levelDir);
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("Your level directory does not exist: " + levelDir, e);
		}
	}

	
	/**
	 * return the file name of the last level.
	 * 
	 * @return file name of the last level
	 * @throws java.io.FileNotFoundException
	 *             thrown if the directory is empty
	 */
	private String getLastFileString() throws FileNotFoundException {
		if (lastLevelFile == "") {
			List<String> dirContent = Arrays.asList(myPath.list(new LevelFileFilter()));
			Collections.sort(dirContent);
			if (!dirContent.isEmpty()) {
				lastLevelFile = dirContent.get(0);
			} else {
				throw new FileNotFoundException("Current level directory does not contain any levels: " + myPath.getAbsolutePath());
			}

		}
		return lastLevelFile;
	}

	/**
	 * return the file string of the next level file.
	 * 
	 * @return file name of the next level file
	 * @throws NoNextLevelException
	 *             thrown if there is no next level file.
	 * @throws java.io.FileNotFoundException
	 *             thrown if directory is empty.
	 */
	private String getNextFileString() throws NoNextLevelException, FileNotFoundException {
		if (myPath == null) {
			throw new FileNotFoundException();
		}
		String[] fileList = myPath.list(new LevelFileFilter());
		if (fileList == null) {
			throw new FileNotFoundException();
		}
		List<String> dirContent = Arrays.asList(fileList);
		Collections.sort(dirContent);
		if (lastLevelFile == "" || lastWasSaveGame) {
			return getLastFileString();
		} else {
			int index = dirContent.indexOf(getLastFileString());
			if (index == -1) {
				throw new FileNotFoundException("File " + getLastFileString() + " not found.");
			}
			if (dirContent.size() <= index + 1) {
				//log.error(dirContent.size() + " files in dir and wanted to load the " + (index + 1));
				throw new NoNextLevelException();
			}
			return dirContent.get(index + 1);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public IGameData<E> loadCurrentAgain() throws InvalidLevelDataException, IOException {
		File f = FrameworkUtils.loadFile(myPath, getLastFileString());
		IGameData<E> gameData;
		if (lastWasSaveGame) {
			gameData = loadGame(f);
		} else {
			gameData = this.loadLevel(f);
		}
		//aehh? why is null possible?
		if (gameData != null) {
			gameData.setLevelFilename(f.toString());
		}
		return gameData;
	}

	/**
	 * {@inheritDoc}
	 */
	public IGameData<E> loadNextLevel() throws NoNextLevelException, InvalidLevelDataException, IOException {
		return this.loadNextLevel(0);
	}

	/**
	 * try as long as there are more files in the directory to load a next level.
	 * see {@link LevelManagerInterface#loadNextLevel()}
	 * @param number
	 *            a hook to avoid infinite loop, counts the number of attempts to load a next level.
	 * @return see {@link LevelManagerInterface#loadNextLevel()}
	 * @throws NoNextLevelException
	 *             see {@link LevelManagerInterface#loadNextLevel()}
	 * @throws InvalidLevelDataException
	 *             see {@link LevelManagerInterface#loadNextLevel()}
	 * @throws java.io.IOException
	 *             see {@link LevelManagerInterface#loadNextLevel()}
	 * @see {@link LevelManagerInterface#loadNextLevel()}
	 */
	private IGameData<E> loadNextLevel(final int number) throws NoNextLevelException, InvalidLevelDataException, IOException {
		if (number > myPath.list(new LevelFileFilter()).length) {
			throw new NoNextLevelException();
		}
		IGameData<E> gameData = null;
		lastLevelFile = getNextFileString();
		File f = FrameworkUtils.loadFile(myPath, lastLevelFile); //new File(myPath, lastLevelFile); 
		try {
			//log.info("Try to load level from " + f);
			gameData = super.loadLevel(FileUtility.readFile(f));
			gameData.setLevelFilename(f.toString());
		} catch (InvalidLevelDataException e) {
			//log.warn("Skipping an invalid level file");
			System.out.println("Warning: the file " + f.getCanonicalPath() + " does not contain a valid level. will be skipped.\nCause for skip: " + e.getMessage());
			gameData = loadNextLevel(number + 1);
		}
		return gameData;
	}

	/**
	 * {@inheritDoc}
	 */
	public void reset(final File path) {
		if (path == null) {
			throw new NullPointerException("Null as Path is illegal.");
		}
		if (!path.isDirectory()) {
			throw new IllegalArgumentException("Given path is not a directory");
		}
		myPath = path;
		lastLevelFile = "";
	}

	/**
	 * {@inheritDoc}
	 */
	public IGameData<E> loadLevel(final File f)
			throws InvalidLevelDataException, IOException {
		IGameData<E> gameData = super.loadLevel(FileUtility.readFile(f));
		gameData.setLevelFilename(f.toString());
		setCurrentLevelPath(f.toString());
		lastWasSaveGame = false;
		return gameData;
	}

	/**
	 * {@inheritDoc}
	 */
	public void saveBoardAsLevel(final File f, final IGameData<E> board) throws IOException, InvalidLevelDataException {
		FileUtility.writeFile(f, super.saveBoardAsLevel(board));

	}

	/**
	 * filter for a level file.
	 */
	private class LevelFileFilter implements FilenameFilter {

		/**
		 * {@inheritDoc}
		 */
		public boolean accept(final File f, final String s) {
			return s.toLowerCase().endsWith(".lvl") && !s.startsWith(".") && !s.equals("highscore.txt");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public String getCurrentLevelName() {

		return lastLevelFile;
	}

	/**
	 * {@inheritDoc}
	 */
	public File getPath() {
		return myPath;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getLevelNumber() {
		Pattern p = Pattern.compile("level_(\\d+)\\.txt");
		Matcher m = p.matcher(lastLevelFile);
		if (m.matches()) {
			return Integer.valueOf(m.group(1));
		} else {
			List<String> dirContent = Arrays.asList(myPath.list(new LevelFileFilter()));
			Collections.sort(dirContent);
			int num = 0;
			for (String s : dirContent) {
				num++;
				if (s.equals(lastLevelFile)) {
					break;
				}
			}
			return num;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public IGameData<E> loadGame(final File f) throws IOException, InvalidLevelDataException {
		FileInputStream is = new FileInputStream(f);
		IGameData<E> gameData = super.loadGame(is);
		setCurrentLevelPath(f.getPath());
		lastWasSaveGame = true;
		return gameData;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean saveGame(final File f, final IGameData<E> gameData) {
		try {
			FileOutputStream os = new FileOutputStream(f);
			super.saveGame(gameData, os);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void setCurrentLevelPath(final String path) throws FileNotFoundException {
		File f = FrameworkUtils.loadFile(path);
		reset(f.getParentFile());
		lastLevelFile = f.getName();
	}

	/**
	 * {@inheritDoc}
	 */
	public LevelInterface<E> getLevel() {
		return this;
	}
}
