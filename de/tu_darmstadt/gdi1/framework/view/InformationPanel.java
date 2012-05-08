/**
 * 
 */
package de.tu_darmstadt.gdi1.framework.view;


import java.awt.Dimension;
import java.awt.Font;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.tu_darmstadt.gdi1.framework.interfaces.IDictionary;
import de.tu_darmstadt.gdi1.framework.interfaces.IUserInterfaceEvent;

/**
 * An InformationPanel is a panel that shows information about
 * the current game state to the user. You can decide what should be
 * displayed, just sent an event to the UserInterface containing an
 * LinkedHashMap with the information to display.<br><br>
 * 
 * We use an LinkedHashMap, because this implementation of the Map 
 * Interface has an predictable iteration order. This lets the user 
 * decide in which sequence the informations should be displayed.
 * The first entity inserted into the map will be the first that
 * will be displayed.<br><br>
 * 
 * @see IUserInterfaceEvent
 * 
 * @author f_m
 */
public class InformationPanel extends JPanel {
	
	private LinkedHashMap<JLabel, JLabel> labelMap;
	private JPanel keyPanel, valuePanel;
	private IDictionary dictionary;
	
	private final static Font LABELFONT = new Font("DejaVu Sans", Font.BOLD, 13);
	
	/**
	 * Constructor for an new InformationPanel.
	 */
	public InformationPanel() {
		this(null);
	}
	
	/**
	 * Constructor for an new InformationPanel.
	 */
	public InformationPanel(IDictionary dict) {
		keyPanel = new JPanel();
		valuePanel = new JPanel();
		dictionary = dict;
		
		keyPanel.setLayout(new BoxLayout(keyPanel, BoxLayout.Y_AXIS));
		valuePanel.setLayout(new BoxLayout(valuePanel, BoxLayout.Y_AXIS));
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		Dimension filler = new Dimension(10, 1);
		
		add(keyPanel);
		add(new Box.Filler(filler, filler, filler));
		add(valuePanel);
	}


	/**
	 * (Re)paints the panel, updating the entries with the values
	 * from the map.
	 * 
	 * @param informationMap A LinkedHashMap of strings, the key should be 
	 * the name of the information, the value should be the actual 
	 * value of the information.
	 */
	public void paint(final LinkedHashMap<String, String[]> informationMap) {
		updateLabelMap(informationMap);
		resetPanel();
		
		for (Entry<JLabel, JLabel> entry : labelMap.entrySet()) {
			keyPanel.add(entry.getKey());
			valuePanel.add(entry.getValue());
		}
	}
	
	/**
	 * resets the panel, i.e. removes the components
	 */
	private void resetPanel() {
		keyPanel.removeAll();
		valuePanel.removeAll();
	}


	/**
	 * Creates an Map of <JLabel, JLabel> for the given Strings.
	 * 
	 * @param informationMap the Strings to use.
	 */
	private void updateLabelMap(final LinkedHashMap<String, String[]> informationMap) {
		labelMap = new LinkedHashMap<JLabel, JLabel>();
		for (Entry<String, String[]> entry : informationMap.entrySet()) {
			JLabel key;
			if(dictionary == null) {
				key = new JLabel(entry.getKey());
			} else {
				key = new JLabel(dictionary.getLocalizedText(entry.getKey()));
			}
			key.setFont(LABELFONT);
			key.setName(entry.getKey() + "_key");
			JLabel value;
			if(dictionary == null) {
				value = new JLabel(entry.getValue()[0]);
			} else {
				value = new JLabel(dictionary.getLocalizedText(entry.getValue()));
			}
			value.setName(entry.getKey() + "_value");
			value.setFont(LABELFONT);
			labelMap.put(key, value);
		}
	}
}
