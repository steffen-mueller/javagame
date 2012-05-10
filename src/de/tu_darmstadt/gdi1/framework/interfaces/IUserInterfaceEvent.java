package de.tu_darmstadt.gdi1.framework.interfaces;

import java.util.LinkedHashMap;

import de.tu_darmstadt.gdi1.framework.sound.SoundManager;
import de.tu_darmstadt.gdi1.framework.view.InformationPanel;


/**
 * Event-Class for events <b>to</b> an {@link IUserInterface}.
 * <br>Sent from a {@link IController} to inform the {@link IUserInterface} about something.
 * <br>E.g. to repaint a changed {@link IBoard} or to play a sound.
 *  
 * <br>
 * <br>
 * <table border=1><tr><td>
 * An implementation of this interface should be part of the framework.<br>
 * </td></tr></table>
 * @param <E> The type of BoardElements used
 * @author jonas
 */
public interface IUserInterfaceEvent<E extends IBoardElement> extends Cloneable {
	
	/**
	 * @return the board to be drawn
	 */
	IBoard<E> getBoard();
	
	/**
	 * Sets the board to be drawn.
	 * @param board the board to be drawn
	 */
	void setBoard(IBoard<E> board);
	
	/**
	 * Force a <strong>complete</strong> repaint of the board.<br>
	 * If you set this flag, the framework will refresh its panel
	 * and paint every single element. The default case is, that you
	 * won't set this flag due to performance issues because it is
	 * much faster to only draw the elements on the panel which have 
	 * changed than to iterate over every element and paint it.<br><br>
	 * Note: there may be certain cases in which it makes sense to
	 * set this flag. These include a changed skin (the identified object
	 * has stayed the same but the icon associated with it is a new one)
	 * and others.
	 * @param newPaint true if the panel shall be forced to draw every element.
	 */
	void setForceNewPaint(final boolean newPaint);
	
	/**
	 * Returns whether the board shall be painted from scratch.
	 * @return true if the board shall be painted from scratch.
	 */
	boolean getForceNewPaint();

	/**
	 * Sets a sound to be played.
	 * @param aSoundLabel the name of the sound, as registered with the {@link SoundManager}
	 */
	void setSound(String aSoundLabel);
	
	/**
	 * @return the name of the sound to be played
	 */
	String getSound();

	/**
	 * Sets the information map to be displayed.
	 * @see InformationPanel
	 * @param informationMap information map to be displayed
	 */
	void setInformationMap(LinkedHashMap<String, String[]> informationMap);
	
	/**
	 * @return InformationMap to be displayed by the view 
	 */
	LinkedHashMap<String, String[]> getInformationMap();
	
	/**
	 * Set the pause mode for the board panel. 
	 * @param pauseMode Null don't change pause mode, true for pause, false for normal mode
	 */
	void setPauseMode(Boolean pauseMode);
	
	/**
	 * @return null if the pause mode should not be changed, true/false to activate/deactivate pause mode 
	 */
	Boolean getPauseMode();

}
