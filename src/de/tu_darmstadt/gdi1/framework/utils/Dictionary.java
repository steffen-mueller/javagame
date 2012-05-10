/**
 * 
 */
package de.tu_darmstadt.gdi1.framework.utils;


import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import de.tu_darmstadt.gdi1.framework.interfaces.IDictionary;

/**
 * This allows easy translation of strings using a dictionary
 * in the form of a properties file.
 * 
 * Create a properties file called dictionary.properties in the
 * key=value format. You can use different file names, just keep
 * the .properties extension and give the filename without the extension
 * to the constructor. If you want to use the locale features,
 * just add the locale suffix to the file name ("dictionary_de.properties")
 * @author Jan
 *
 */
public class Dictionary implements IDictionary {
	
	/**
	 * RessourceBundle containing the translations.
	 */
	private ResourceBundle bundle = null;
	
	/**
	 * Creates a dictionary and loads strings without using a locale.
	 * @param dictName the name of the RessourceBundle to load
	 */
	public Dictionary(final String dictName) {
		try {
			bundle = ResourceBundle.getBundle(dictName);
		} catch (MissingResourceException e) {
			System.err.println("WARNING: Dictionary file not found, working without dictionary");
		}
	}

	/**
	 * Creates a dictionary and loads strings using a specific locale.
	 * @param dictName the name of the RessourceBundle to load 
	 * @param locale the locale to use
	 */
	public Dictionary(final String dictName, final Locale locale) {
		try {
			bundle = ResourceBundle.getBundle(dictName, locale);
		} catch (MissingResourceException e) {
			System.err.println("WARNING: Dictionary file not found, working without dictionary");
		}
	}
	
	/**
	 * Creates a dictionary and loads strings using the specified locale from the default dictionary file "dictionary".
	 * @param locale the locale to use
	 */
	public Dictionary(final Locale locale) {
		this("dictionary", locale);
	}

	/**
	 * Creates a dictionary and loads strings from the default dictionary file "dictionary".
	 */
	public Dictionary() {
		this("dictionary");
	}

	/**
	 * {@inheritDoc}
	 */
	public String getLocalizedText(final String key) {
		if (bundle != null && bundle.containsKey(key)) {
			return bundle.getString(key);
		} else {
			return key;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getLocalizedText(final String[] key) {
		if (key == null || key.length == 0) {
			return "";
		}
		if (bundle != null && bundle.containsKey(key[0])) {
			String tmp = bundle.getString(key[0]);
			Object params[] = new String[key.length - 1];
			System.arraycopy(key, 1, params, 0, key.length - 1);
			tmp = MessageFormat.format(tmp, params);
			return tmp;
		} else {
			return key[0];
		}
	}

}
