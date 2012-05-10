/**
 * 
 */
package de.tu_darmstadt.gdi1.framework.utils;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import de.tu_darmstadt.gdi1.framework.exceptions.WrongXMLFormatException;
import de.tu_darmstadt.gdi1.framework.interfaces.IDictionary;
import de.tu_darmstadt.gdi1.framework.interfaces.IMenuListener;


/**
 * A factory class to create a JMenuBar out of an XML file. 
 * 
 * The delivered xml file needs to have the following structure.<br><br>
 * 
 * The document should be enclosed by the menubar tag. Each menu, whether it is a top 
 * menu or an submenu, is created with the menu tag. There a three different entity types
 * that can be used: <br><br>
 * 
 * - <b>entity</b> - a normal text entity.<br>
 * - <b>checkbox_entity</b> - an entity that can be selected or unselected.<br>
 * - <b>radiobutton_entity</b> - 	an entity that belongs to an  surrounding radiobutton_group.
 * 	Only one of the entities in an radiobutton_group can be activated.<br><br>
 * 
 * <b>Attributes:</b><br><br>
 * 
 * <b>All</b><br>
 * label: The Label that should be placed on the menu/entity.<br>
 * name: the components name.<br><br>
 * 
 * <b>All entity types:</b><br>
 * accelerator: the accelerator code that should be used for this entity, check the syntax of {@link javax.swing.KeyStroke#getKeyStroke(String)}.<br><br>
 *
 * <b>checkbox_entity and radiobutton_entity</b><br>
 * selected: whether the entity is selected<br><br>
 * 
 * <b>Example:</b><br><br>
 * 
 * Here is an example xml file that shows how you can create an menu:<br>
 * <pre>
 *  <code>
 * &#60;menubar&#62;
 *
 * &#60;menu label="Menu 1" name="menuOne"&#62;
 *   &#60;entity label="Open test" name="openTest"/&#62;
 *   &#60;entity label="Save test" name="saveTest"/&#62;
 *   &#60;menu label="Submenu for Menu 1" name="menuTwo"&#62;
 *     &#60;entity label="Submenu Entity" name="subMenuEntity"/&#62;
 *   &#60;/menu&#62;
 *   &#60;separator name="separatorOne"/&#62;
 *   &#60;entity label="Enter Name" name="nameEntity"/&#62;
 * &#60;/menu&#62;
 *
 * &#60;menu label="Menu 2" name="menuTwo"&#62;
 *   &#60;entity label="A entity with accelerator" name="coolEntity" accelerator="alt X"/&#62;
 *   &#60;checkbox_entity label="A selected checkbox entity" selected="true" name="selectedCheckBox"/&#62;
 *   &#60;checkbox_entity label="And an unselected one" selected="false" name="unselectedCheckBox"/&#62;
 *   &#60;menu label="Submenu with Radiobuttons" name="radioMenu"&#62;
 *   &#60;radiobutton_group&#62;
 *     &#60;radiobutton_entity label="A radio button" name="radioButton1" selected="false"/&#62;
 *     &#60;radiobutton_entity label="A selected radio Button" name="radioButton2" selected="true"/&#62;
 *   &#60;/radiobutton_group&#62;
 *   &#60;/menu&#62;
 * &#60;/menu&#62;
 *
 * &#60;/menubar&#62;
 * </code>
 *</pre>
 * 
 * @author f_m
 */
public class MenuBuilder extends DefaultHandler {
	private static JMenuBar menuBar = null;
	private static Stack<JMenu> menuStack = null;
	private static ButtonGroup radioButtonGroup = null;
	private static IMenuListener menuListener= null;
	private static List<JMenuItem> menuItemList = null;
	private static IDictionary dict = null;
	
	//class only for static use.
	private MenuBuilder() {}
	
	/**
	 * Because there is no possibility to return more then one object, the result
	 * of the MenuBuilder is an object, that wraps the JMenuBar that you can put 
	 * into your JFrame and an List of the JMenuItmes that are used to let you easily
	 * access the JMenuItems.
	 * 
	 * @author f_m
	 */
	public class MenuBuilderResult {
		private JMenuBar menubar;
		private List<JMenuItem> menuItems;
		public MenuBuilderResult(JMenuBar menubar, List<JMenuItem> menuItems) {
			this.menubar = menubar;
			this.menuItems = menuItems;
		}
		
		/**
		 * @return the JMenuBar
		 */
		public JMenuBar getMenuBar() {
			return menubar;
		}
		
		/**
		 * @return a List of the used JMenuItems
		 */
		public List<JMenuItem> getMenuItems() {
			return menuItems;
		}
		
	}

	/**
	 * 	/**
	 * Builds and returns and swing JMenu out of the given xml file.
	 * 
	 * @param file
	 *            	the XML-File describing the menu
	 * @param menuListener
	 * 				the class that should be informed about clicked
	 * 				menu items.
	 * @param dict  the IDictonary that should be used to localize the menu
	 * @return A MenuBuilderResult, containing the JMenubar and a List of all used JMenuItems
	 * @throws java.io.IOException if the file couldn't be read successfully
	 * @throws WrongXMLFormatException if the xml file isn't properly formated
	 */
	public static MenuBuilderResult buildMenu(final File file, final IMenuListener menuListener, IDictionary dict) throws IOException, WrongXMLFormatException {
		MenuBuilder builder = new MenuBuilder();
		MenuBuilder.menuItemList = new ArrayList<JMenuItem>();
		MenuBuilder.menuListener = menuListener;
		MenuBuilder.dict = dict;
		try {
			SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
			saxParser.parse(file, builder);
		} catch (ParserConfigurationException e) {
			throw new WrongXMLFormatException(e.getMessage());
		} catch (SAXException e) {
			throw new WrongXMLFormatException(e.getMessage());
		} catch (IOException e) {
			throw e;
		}
		
		return builder.new MenuBuilderResult(menuBar, menuItemList);
	}

	@Override
	public void startDocument() {
		menuBar = new JMenuBar();
		menuStack = new Stack<JMenu>();
	}
	
	@Override
	public void startElement(final String namespaceURI, final String localName, final String qName, final Attributes attrs) {
		KeyStroke acc = checkAccelerator(attrs);
			
		if (qName.equals("menu")) {
			JMenu menu;
			if (dict == null) {
				menu = new JMenu(attrs.getValue("label"));
			} else {
				menu = new JMenu(dict.getLocalizedText(attrs.getValue("label")));
			}

			if (acc != null)
				menu.setMnemonic(acc.getKeyCode());
			menuStack.push(menu);
		} else if (qName.equals("entity")) {
			final JMenuItem item;
			if (dict == null) {
				item = new JMenuItem(attrs.getValue("label"));
			} else {
				item = new JMenuItem(dict.getLocalizedText(attrs.getValue("label")));
			}
			item.setName(attrs.getValue("name"));
			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					menuListener.menuItemClicked(item.getName());
				}	
			});
			if (acc != null)
				item.setAccelerator(acc);
			menuStack.peek().add(item);
			menuItemList.add(item);
		} else if (qName.equals("checkbox_entity")) {
			boolean selected = false;
			if (attrs.getValue("selected") != null && attrs.getValue("selected").equals("true"))
				selected = true;
			final JCheckBoxMenuItem item;
			if (dict == null) {
				item = new JCheckBoxMenuItem(attrs.getValue("label"));
			} else {
				item = new JCheckBoxMenuItem(dict.getLocalizedText(attrs.getValue("label")));
			}
			/*
			 * doesn't work, if you hand over the selected state via constructor, if you do so, the whole
			 * menu is demolished...don't ask me...
			 */
			item.setSelected(selected);
			item.setName(attrs.getValue("name"));
			if (acc != null)
				item.setAccelerator(acc);
			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					menuListener.menuItemClicked(item.getName());
				}	
			});
			menuStack.peek().add(item);
			menuItemList.add(item);
		} else if (qName.equals("separator")) {
			menuStack.peek().add(new JSeparator());
		} else if (qName.equals("radiobutton_group")) {
			radioButtonGroup = new ButtonGroup();
		} else if (qName.equals("radiobutton_entity")) {
			final JRadioButtonMenuItem item;
			if(dict == null) {
				item = new JRadioButtonMenuItem(attrs.getValue("label"));
			} else {
				item = new JRadioButtonMenuItem(dict.getLocalizedText(attrs.getValue("label")));
			}
			boolean selected = false;
			if (attrs.getValue("selected") != null && attrs.getValue("selected").equals("true"))
				selected = true;
			item.setSelected(selected);
			item.setName(attrs.getValue("name"));
			if (acc != null)
				item.setAccelerator(acc);
			if (radioButtonGroup != null)
				radioButtonGroup.add(item);
			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					menuListener.menuItemClicked(item.getName());
				}	
			});
			menuStack.peek().add(item);
			menuItemList.add(item);
		}
	}


	/**
	 * Checks whether the attributes contain an accelerator for 
	 * this entity. If though, it will be returned, otherwise null.
	 * 
	 * The accelerator must be formated like requested by 
	 * KeyStroke.getKeyStroke(String). 
	 * 
	 * @param attrs
	 * @return the KeyStroke for the accelerator if one could be parsed,
	 * 			otherwise null.
	 * @see javax.swing.KeyStroke#getKeyStroke(String)
	 */
	private KeyStroke checkAccelerator(Attributes attrs) {
		return KeyStroke.getKeyStroke(attrs.getValue("accelerator"));
	}

	@Override
	public void endElement(String namespaceURI, String localName, String qName)
			 {
		if (qName.equals("menu")) {
			JMenu menu = menuStack.pop();
			if (menuStack.isEmpty()){
				menuBar.add(menu);
			} else {
				menuStack.peek().add(menu);
			}
		} else if (qName.equals("radiobutton_group")) {
			radioButtonGroup = null;
		}
	}
}
