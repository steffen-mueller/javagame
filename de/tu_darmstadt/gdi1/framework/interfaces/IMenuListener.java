/**
 * 
 */
package de.tu_darmstadt.gdi1.framework.interfaces;

/**
 * This interface allows a class (typically the view) 
 * to receive notifications of menu clicks.
 * 
 * The class implementing this interface gets called by
 * the JMenuBar created by an MenuBuilder. 
 * 
 * @author f_m
 */
public interface IMenuListener {
	
	/**
	 * Method to get notified about menu clicks.
	 * 
	 * The menu bar calls this method if a menu item gets clicked.
	 * 
	 * @param itemName the name (attribute name in the 
	 * xml file) of the clicked menu item.
	 */
	void menuItemClicked(String itemName);
}
