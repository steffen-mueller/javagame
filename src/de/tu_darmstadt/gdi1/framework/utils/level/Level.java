package de.tu_darmstadt.gdi1.framework.utils.level;


import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.tu_darmstadt.gdi1.framework.exceptions.InvalidLevelDataException;
import de.tu_darmstadt.gdi1.framework.interfaces.IBoard;
import de.tu_darmstadt.gdi1.framework.interfaces.IBoardElement;
import de.tu_darmstadt.gdi1.framework.interfaces.IGameBoard;
import de.tu_darmstadt.gdi1.framework.interfaces.IGameData;
import de.tu_darmstadt.gdi1.framework.interfaces.ILevelInformationProvider;
import de.tu_darmstadt.gdi1.framework.interfaces.LevelInterface;
import de.tu_darmstadt.gdi1.framework.model.GameBoard;


/**
 * A implementation of {@link LevelInterface}.
 * 
 * This is an utility class without an internal state except the 
 * {@link ILevelInformationProvider} set in the constructor.
 * 
 * @author jonas
 * 
 * @see LevelInterface
 * @param <E> The type of BoardElements used
 */
public class Level<E extends IBoardElement> implements LevelInterface<E> {

	private final ILevelInformationProvider<E> levelInfoProvider;
	
	/**
	 * Constructs a new Level class.
	 * @param aLevelInfoProvider the LevelInformationProvider to be used to encode/decode levels etc.
	 */
	public Level(ILevelInformationProvider<E> aLevelInfoProvider) {
		super();
		
		checkifInfoProviderisLegal(aLevelInfoProvider);
		
		levelInfoProvider = aLevelInfoProvider;
	}
	
	private void checkifInfoProviderisLegal(final ILevelInformationProvider<E> infoProvider) {
		
		if (infoProvider == null) {
			throw new NullPointerException("null as ILevelInformationProvider is illegal");
		}
		
		final String isIllegal = "ILevelInformationProvider is not legal. ";
		
		String tmp = infoProvider.getLinePrefixForNonBoardLines();
		if (tmp == null) {
			throw new IllegalArgumentException(isIllegal + "ILevelInformationProvider.getLinePrefixForNoneBoardLines() have to return not null.");
		}
		if (tmp.length() < 1) {
			throw new IllegalArgumentException(isIllegal + "ILevelInformationProvider.getLinePrefixForNoneBoardLines() have to return a not empty string.");
		}
		if (tmp.charAt(0) == SPACE) {
			throw new IllegalArgumentException(isIllegal 
					+ "ILevelInformationProvider.getLinePrefixForNoneBoardLines() have to return a string with first char different from: '" + SPACE + "'");
		}
		
	}

	/**
	 * {@inheritDoc}
	 */
	public IGameData<E> loadLevel(final String data) throws InvalidLevelDataException {
		if (data == null || data.trim().length() == 0) {
			throw new InvalidLevelDataException("Empty (after trim) or null level string");
		}
		ArrayList<String> tmpStorage = new ArrayList<String>();
		Collections.addAll(tmpStorage, data.split("\n"));

		// skip empty or nirvana lines
		while (tmpStorage.get(0).matches(SPACE_AS_REGEX)) {
			tmpStorage.remove(0);
		}
		while (tmpStorage.get(tmpStorage.size() - 1).matches(SPACE_AS_REGEX)) {
			tmpStorage.remove(tmpStorage.size() - 1);
		}
		
		int width = 0;
		//the number of lines in the file
		int lineNumbers = tmpStorage.size();
		//the height of the board (if additional informations were contained this should be smaller than line-numbers, after check)
		int height = lineNumbers;
		
		// iterate through lines to get the maximum width
		// and remove NoneBoard-lines form height
		for (String line : tmpStorage) { 
			if (line.startsWith(levelInfoProvider.getLinePrefixForNonBoardLines())) {
				//important: decrease height on NoneBoard(Info)-line:
				height--;
			} else {
				width = Math.max(width, line.length());
			}
		}
		
		IGameBoard<E> gameBoard = new GameBoard<E>(width, height);
		
		Map<Integer, String> additionalInformations = new HashMap<Integer, String>();
		for (int i = 0; i < lineNumbers; i++) {
			
			if (tmpStorage.get(i).startsWith(levelInfoProvider.getLinePrefixForNonBoardLines())) {
				//cut out the prefix of the line and store it:
				//tmpStorage.get(i).length() - 
				String infoLine = tmpStorage.get(i).substring(levelInfoProvider.getLinePrefixForNonBoardLines().length()).trim();
				additionalInformations.put(i, infoLine);
				continue;
			}
			for (int j = 0; j < width; j++) {
				List<E> list;
				if (j >= tmpStorage.get(i).length()) {
					list = levelInfoProvider.getBoardElementsFor(SPACE);
				} else {
					list = levelInfoProvider.getBoardElementsFor(tmpStorage.get(i).charAt(j));
					if (null == list) {
						throw new InvalidLevelDataException(tmpStorage.get(i).charAt(j) + " is an invalid character in a level file. Occurs at colum " + i + " and row " + j);
					}
				}
				gameBoard.setElements(j, i - additionalInformations.size(), list);
			}
		}
		
		try {
			IGameData<E> gameData;
			
			gameData =  levelInfoProvider.verifyGameBoard(gameBoard, lineNumbers, additionalInformations);
			
			return gameData;
			
		} catch (InvalidLevelDataException e) {
			throw new InvalidLevelDataException("ILevelInformationProvider has thrown InvalidLevelDataException: " + e.getMessage(), e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public IGameData<E> loadGame(final InputStream is) throws IOException, InvalidLevelDataException {
		ObjectInputStream objectInput = new ObjectInputStream(is);
		try {
			Object readObject = objectInput.readObject();
			if (readObject instanceof IGameData) {
				IGameData<E> gameData = (IGameData) readObject; 
				return gameData;
			} else {
				throw new InvalidLevelDataException("The loaded Object does not implement IGameData.");
			}
			// all these exceptions inherited from IOException...
			// } catch (InvalidClassException e) {
			// } catch (StreamCorruptedException e) {
			// } catch (OptionalDataException e) {
			// } catch (IOException e) {
			// throw new InvalidLevelDataException("Save game could not be loaded", e);
		} catch (ClassNotFoundException e) { //does not inherit form IOException, have to be caught
			throw new InvalidLevelDataException("Save game could not be loaded", e);
		}

	}

	/**
	 * {@inheritDoc}
	 */
	public String saveBoardAsLevel(final IGameData<E> gameData) throws InvalidLevelDataException {
		
		IBoard<E> board = gameData.getStepManager();
		StringBuffer line = null;
		StringBuffer result = new StringBuffer();
		for (int y = 0; y < board.getHeight(); y++) {
			line = new StringBuffer();
			for (int x = 0; x < board.getWidth(); x++) {
				try {
					line.append(levelInfoProvider.parseField(board.getElements(x, y)));
				} catch (InvalidLevelDataException e) {
					throw new InvalidLevelDataException("Could not store the field at " + x + "|" + y + " cause of invalid level data", e);
				}
			}

			while (line.length() > 0 && line.charAt(line.length() - 1) == SPACE) {
				line.deleteCharAt(line.length() - 1);
			}
			result.append(line);
			result.append("\n");
		}
		String tmp = levelInfoProvider.getNoneBoardInformationLineFor(gameData);
		if ((tmp != null) && (tmp.length() > 0)) {
			if (!tmp.startsWith(levelInfoProvider.getLinePrefixForNonBoardLines())) {
				result.append(levelInfoProvider.getLinePrefixForNonBoardLines());
			} 
			result.append(tmp);
		}
		return result.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	public void saveGame(final IGameData<E> gameData, final OutputStream o) throws IOException {
		ObjectOutputStream objectStream = new ObjectOutputStream(o);
		objectStream.writeObject(gameData);
		objectStream.close();
	}
}
