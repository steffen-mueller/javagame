package de.tu_darmstadt.gdi1.framework.interfaces;

import de.tu_darmstadt.gdi1.framework.events.DefaultControllerEvent;



/**
 * Event-Class for events <b>to</b> a {@link IController}. <br>
 * Sent from a {@link IUserInterface} or from the {@link IController} himself.
 *  
 * <br>
 * <br>
 * This is implemented by {@link DefaultControllerEvent}, feel free to re-implement.
 * <br><br>
 * If you do not want to work with separate event classes,
 * feel free to use the string as an action identifier and/or
 * extend this class to contain such an identifier.
 *
 * @author jonas
 *
 */
public interface IControllerEvent extends Cloneable {
	
	/** {@inheritDoc} */
	IControllerEvent clone();

	/**
	 * sets a string related to the action.
	 * E.g. the path to load from
	 * @param aString
	 * 		the string to set
	 */
	void setString(String aString);
	
	/**
	 * sets a int related to the action.
	 * E.g. a part of a coordinate where the user clicked.
	 * @param intOne
	 * 		the int to set
	 */
	void setIntOne(int intOne);
	
	/**
	 * sets a int related to the action.
	 * E.g. a part of a coordinate where the user clicked.
	 * @param intTwo
	 * 		the int to set
	 */
	void setIntTwo(int intTwo);

	String getString();
	
	int getIntOne();
	
	int getIntTwo();
	
}
