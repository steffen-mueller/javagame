package de.tu_darmstadt.gdi1.framework.interfaces;

/**
 * Provides an interface to a string translation dictionary.
 * The dictionary file format and storage method depends on the implementation.
 * {@link Dictionary} is the implementation provided by the framework.
 * @author Jan
 *
 */
public interface IDictionary {
	
	/**
	 * Transforms the given key into a language depending text.
	 * 
	 * @param key
	 * 		the key of a text which should be localized.
	 * 		May not be null.
	 * @return
	 * 		the localized text for the given key. 
	 * 		If no localized key is available for this key, the key himself will be returned.
	 * 		never null. 
	 * 		
	 * @throws NullPointerException
	 * 		if the given key is null.
	 */
	String getLocalizedText(final String key) throws NullPointerException;
	
	/**
	 * Transforms the given key and parameters into a language depending text.
	 * 
	 * @param key
	 * 		the key and parameters of a text which should be localized.
	 * 		may not be null.
	 * 		@see @link {@link MessageFormat} for format of text
	 * @return
	 * 		the localized text for the given key. 
	 * 		If no localized key is available for this key, the key himself will be returned.
	 * 		never null. 
	 * 		
	 * @throws NullPointerException
	 * 		if the given key is null.
	 */
	String getLocalizedText(final String[] key) throws NullPointerException;

}


