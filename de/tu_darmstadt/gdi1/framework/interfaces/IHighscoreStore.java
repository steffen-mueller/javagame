package de.tu_darmstadt.gdi1.framework.interfaces;


import java.util.List;

/**
 * This is an interface allowing the view to read the column captions
 * and values from a class storing a highscore list.
 * @author Jan
 *
 */
public interface IHighscoreStore {
	/**
	 * Gets the highscore list.
	 * @return the ordered highscore list
	 */
	public List<IHighscoreEntry> getList();
	
	/**
	 * The highscore entries consist of values for the highscore table columns.
	 * This returns the column captions. 
	 * 
	 * @return the column titles
	 */
	public String[] getTitles();
	
	/**
	 * Sets the column titles for the highscore table
	 * @param titles the column titles
	 */
	public void setTitles(String[] titles);

	
	/**
	 * Sets a new maximal number of highscores that can be stored.
	 * If more highscores are currenty stored, the ones exceeding the new limit
	 * will be discarded.
	 * 
	 * @param maxCapacity the new maximal capacity of the list
	 */
	public void setCapacity(int maxCapacity);
	
	/**
	 * Inserts a new highscore entry. The table will be automatically re-sorted.
	 * Entries exceeding the capacity will be discarded, this may cause the new
	 * entry not being included.
	 * @param entry the entry to add
	 */
	public void addEntry(IHighscoreEntry entry);



}