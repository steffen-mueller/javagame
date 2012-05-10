package de.tu_darmstadt.gdi1.framework.model;


import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import de.tu_darmstadt.gdi1.framework.exceptions.InvalidHighscoreDataException;
import de.tu_darmstadt.gdi1.framework.interfaces.IHighscoreEntry;
import de.tu_darmstadt.gdi1.framework.interfaces.IHighscoreStore;


/**
 * This class stores and manages the data for the highscore table.
 * All changes are automatically saved to the file specified in the constructor.
 * @author Jan
 *
 */
public class HighscoreStore implements IHighscoreStore, Serializable {
	
	private static final long serialVersionUID = 8068714653397570017L;

	/**
	 * The ordered highscore list
	 */
	private LinkedList<IHighscoreEntry> highscorelist = new LinkedList<IHighscoreEntry>();
	
	/**
	 * the column captions for the highscore table
	 */
	String[] titles = null;
	
	/**
	 * The maximum number of highscore entries that will be stored
	 */
	int maxCapacity = 20;
	
	/**
	 * the file that will be used to autosave the highscores
	 */
	File datafile = null;
	
	/**
	 * Creates a new HighscoreStore that uses the given file.
	 * If the file exists, the highscore table will be loaded,
	 * otherwise a new file will created later.
	 * @param filename the filename for persistent storage of the highscores.
	 * @throws java.io.IOException if the highscore file cannot be properly created/opened
	 * @throws InvalidHighscoreDataException if the data file is invalid
	 */
	public HighscoreStore(String filename) throws IOException, InvalidHighscoreDataException {

		datafile = new File(filename);
		if (datafile.isDirectory()) {
			if (filename.charAt(filename.length()-1) != '\\' &&
				filename.charAt(filename.length()-1) != '/' ) {
				filename += File.separator;
			}
			filename += "highscores.dat";
			datafile = new File(filename);
		}
		
		if (datafile.isFile()) {
			ObjectInputStream data = null;
			try {
				data = new ObjectInputStream(new FileInputStream(datafile));;
				maxCapacity = data.readInt();
				titles = (String[]) data.readObject();
				highscorelist = (LinkedList<IHighscoreEntry>) data.readObject();
			} catch (ClassNotFoundException e) {
				throw new InvalidHighscoreDataException(e);
			} catch (EOFException e) {
				// do nothing
			} catch (Exception e) { // if casting fails
				throw new InvalidHighscoreDataException(e);
			} finally {
				//the stream should be closed, also if a casting fails:
				if (data != null) {
					//caller has to handle IOExceptions of close
					// (IOExceptions of save he has to handle, too)
					data.close();
				}
			}
		}
		
		save(); // make sure file can be written, create if necessary
		// if something locks the file after construction, saving will
		// fail, printing a stacktrace.
	}

	/**
	 * Sets a new maximal number of highscores that can be stored.
	 * If more highscores are currenty stored, the ones exceeding the new limit
	 * will be discarded.
	 * 
	 * @param maxCapacity the new maximal capacity of the list
	 */
	public void setCapacity(int maxCapacity) {
		this.maxCapacity = maxCapacity;
		limitNumber();
		try {
			save();
		} catch (IOException e) {
			// Should never happen, constructor tested that save() works
			e.printStackTrace();
		}
	}

	/**
	 * Sets the column titles for the highscore table
	 * @param titles the column titles
	 */
	public void setTitles(String[] titles) {
		this.titles = titles;
		try {
			save();
		} catch (IOException e) {
			// Should never happen, constructor tested that save() works
			e.printStackTrace();
		}
	}
	
	/**
	 * deletes all entries
	 */
	public void clear() {
		highscorelist.clear();
		try {
			save();
		} catch (IOException e) {
			// Should never happen, constructor tested that save() works
			e.printStackTrace();
		}
	}
	
	/**
	 * Inserts a new highscore entry. The table will be automatically re-sorted.
	 * Entries exceeding the capacity will be discarded, this may cause the new
	 * entry not being included.
	 * @param entry the entry to add
	 */
	public void addEntry(IHighscoreEntry entry) {
		highscorelist.add(entry);
		Collections.sort(highscorelist);
		limitNumber();
		try {
			save();
		} catch (IOException e) {
			// Should never happen, constructor tested that save() works
			e.printStackTrace();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<IHighscoreEntry> getList() {
		return highscorelist;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String[] getTitles() {
		if ( titles == null ) {
			throw new RuntimeException("Requested titles before setting them");
		}
		return this.titles;
	}
	
	/**
	 * saves the highscore list to file
	 * @throws java.io.IOException if saving fails for whatever reason (like the file being locked)
	 */
	private void save() throws IOException {
		ObjectOutputStream data = null;
		try {
			data = new ObjectOutputStream(new FileOutputStream(datafile));
			data.writeInt(maxCapacity);
			data.writeObject(titles);
			data.writeObject(highscorelist);
		} finally {
			if (data != null) {
				data.close();
			}
		}
	}
	
	/**
	 * removes entries exceeding the limit
	 */
	private void limitNumber() {
		while (highscorelist.size() > maxCapacity )
			highscorelist.removeLast();
	}
	
	
}
