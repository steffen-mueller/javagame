/**
 * 
 */
package de.tu_darmstadt.gdi1.framework.view;


import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileFilter;

import de.tu_darmstadt.gdi1.framework.exceptions.FrameworkError;
import de.tu_darmstadt.gdi1.framework.exceptions.SoundFailedException;
import de.tu_darmstadt.gdi1.framework.exceptions.WrongXMLFormatException;
import de.tu_darmstadt.gdi1.framework.interfaces.IBoard;
import de.tu_darmstadt.gdi1.framework.interfaces.IBoardElement;
import de.tu_darmstadt.gdi1.framework.interfaces.IDictionary;
import de.tu_darmstadt.gdi1.framework.interfaces.IGameView;
import de.tu_darmstadt.gdi1.framework.interfaces.IMenuListener;
import de.tu_darmstadt.gdi1.framework.interfaces.IUserInterfaceEvent;
import de.tu_darmstadt.gdi1.framework.sound.SoundManager;
import de.tu_darmstadt.gdi1.framework.utils.MenuBuilder;
import de.tu_darmstadt.gdi1.framework.utils.Point;


/**
 * Abstract base class for your implementation of the games UserInterface.<br><br>
 * 
 * The UserInterface class is the receiver of the events sent by the controller, not your derived class. 
 * You hand in your events - whatever result they should produce - with the handleEvent() method. The 
 * UserInterface will check the content of the event for data the framework should show to the user (repaint 
 * the GameBoard and the InformationPanel, play sounds). After the framework has done it's work, the event is 
 * given over to your implementation via the handleNonFrameworkEvents() method. The framework will not change 
 * any data in your events.<br><br>
 * 
 * The UserInterface has some different constructors to match your needs, check the JavaDocs for details.<br><br>
 * 
 * The basic UserInterface window contians only the BoardPanel and - if you handed in the path to an xmlFile 
 * - the JMenuBar.  An {@link InformationPanel} is added by simply handing in an event that contains an infoMap 
 * entry. The Information map will be updated everytime you hand in an event containing an information map, you 
 * can remove the InformationPanel by submitting  an empty infoMap (empty as in infoMap = new LinkedHashMap(), 
 * infoMap = null won't change anything).<br><br>
 * 
 * You can put any data into the events, if a field that is interesting for the framework
 * is null, the framework won't change anything on the current state of that component.<br><br>
 * 
 * Your derived class needs to implement the abstract methods from this class. Your 
 * implementation will be informed about user input via this methods, it should react 
 * appropriate to the input and handle events over to the controller. The framework
 * will inform you about mouse clicks, key strokes, menu clicks and an closing request
 * by the user.<br><br>
 * 
 * If you don't want to use the default layout pass the constructor false for the defaultLayout
 * boolean. You'll need to layout the UserInterface window yourself. The components are aviable
 * via getter methods. The components (InformationPanel, BoardPanel) will still get updated 
 * as you would expect it.
 * 
 * To use your own pause screen pass an JPanel via the constructor. If set to the pause mode
 * the BoardPanel will change it's size to the PausePanel's size. If no pausePanel is used
 * the size of the board (and of the window) will stay the same in the pause mode.
 * 
 * The UserInterface should be closed like every other JFrame by calling the dispose() method.
 * 
 * @author f_m
 * @see MenuBuilder, GameBoard, SoundManager
 */
public abstract class UserInterface<E extends IBoardElement> extends JFrame implements IGameView<E>, IMenuListener {
	
	/** Serial UID. */
	private static final long serialVersionUID = -1598238269774055101L;
	protected BoardPanel<E> boardPanel;
	protected SoundManager soundManager;
	protected InformationPanel infoPanel;
	private boolean initialInvisible;
	private List<JMenuItem> menuItemList;
	private IDictionary dict;
	
	

	/**
	 * Default constructor for an new UserInterface.<br>
	 * Default means: will use the default layout and will be shown automatically. Without any menu.<br>
	 * If you want to add a menu or change/add something you should use another constructor.
	 * <br>
	 * Notice: the frame will be set visible with the first event. Not until than it is known which size infopanel/boardpanel should have. 
	 */
	public UserInterface() {
		this(true, null, null);
	}
	
	/**
	 * Constructor for an new UserInterface.<br>
	 * Will create infopanel and board, will prepare the default behavior of the frame (e.g. report pressed keys, ask back before close, etc).
	 * <br>
	 * <br>
	 * Notice: if you use a own layout you have to use boardpanel/infopanel (see getters) of this class.
	 * If you want to use own boardpanel/infopanel/soundmanager you have to pass them to this class (see protected setters)
	 * to keep the default frame behavior like displaying a board of an event directly..
	 * Please notice that it is not possible to use none-default boardpanel/infopanel with the default layout. 
	 * if you do NOT want this behavior you should create your own frame at all. 
	 * <br>
	 * <br>
	 * Notice: the frame will be set visible with the first event. Not until than it is known which size infopanel/boardpanel should have. 
	 * @param defaultLayout if true will board and infopanel will lay out automatically.
	 * @param menuXmlPath the path to an menu-xml, or null if no menu should be displayed.
	 * @param pauseScreen the pause screen that should be used, or null for an empty pause screen.
	 */
	public UserInterface(final boolean defaultLayout, final String menuXmlPath, final JPanel pauseScreen) {
		this(defaultLayout, menuXmlPath, pauseScreen, null);
	}
	
	/**
	 * Constructor for an new UserInterface.<br>
	 * Will create infopanel and board, will prepare the default behavior of the frame (e.g. report pressed keys, ask back before close, etc).
	 * <br>
	 * <br>
	 * Notice: if you use a own layout you have to use boardpanel/infopanel (see getters) of this class.
	 * If you want to use own boardpanel/infopanel/soundmanager you have to pass them to this class (see protected setters)
	 * to keep the default frame behavior like displaying a board of an event directly..
	 * Please notice that it is not possible to use none-default boardpanel/infopanel with the default layout. 
	 * if you do NOT want this behavior you should create your own frame at all. 
	 * <br>
	 * <br>
	 * Notice: the frame will be set visible with the first event. Not until than it is known which size infopanel/boardpanel should have. 
	 * @param defaultLayout if true will board and infopanel will lay out automatically.
	 * @param menuXmlPath the path to an menu-xml, or null if no menu should be displayed.
	 * @param pauseScreen the pause screen that should be used, or null for an empty pause screen.
	 */
	public UserInterface(final boolean defaultLayout, final String menuXmlPath, final JPanel pauseScreen, final IDictionary dict) {
		this.dict = dict;
		
		if(pauseScreen == null) {
			boardPanel = new BoardPanel<E>(this);
		} else {
			boardPanel = new BoardPanel<E>(this, pauseScreen);
		}
		infoPanel = new InformationPanel(dict);
		soundManager = new SoundManager();
		
		/* we wait for the first event, with board and infomap before we show this frame
		 * to differ invisible frames (user wanted or initial) we have to set this variable. */  
		initialInvisible = true;
		
		if (defaultLayout) {
			setLayoutAndPanels();
		}

		addListenters();
		
        setLocationRelativeTo(null);
        
        if (menuXmlPath != null) {
        	addMenu(menuXmlPath);
        }
        pack();
	}


	/**
	 * adds the listeners to this frame to enable the default frame behavior.
	 * (<strike>e.g.</strike> ask before close, report pressed keys, <strike>etc</strike>)
	 */
	private void addListenters() {
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(final WindowEvent e) {
				closingRequested();
			}
			
		});
		
		addKeyListener(new KeyListener() {
			
			public void keyTyped(KeyEvent e) {
				keyboardKeyTyped(e);
			}
			
			public void keyReleased(KeyEvent e) {
				keyboardKeyReleased(e);
			}
			
			public void keyPressed(KeyEvent e) {
				keyboardKeyPressed(e);
			}
		});
	}

	/**
	 * Initializes the Layout and panels of the window.
	 * The panels (fields of this class) should be created before.
	 * Override this for a custom layout.
	 */
	protected void setLayoutAndPanels() {
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		
		boardPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		infoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		add(boardPanel);
		add(infoPanel);
		
		this.setResizable(false);
		
		try {
			//We set the Look&Feel to the system default.
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			// should never happen on the system default look&feel.
			// if it does, print stacktrace and ignore.
			e.printStackTrace();
		}
	}

	/**
	 * Adds an JMenuBar to the Window.
	 * 
	 * @param xmlpath 	the path to the xml describing
	 * 					the menu.
	 */
	public void addMenu(String xmlpath) {
		try {
			MenuBuilder.MenuBuilderResult res = MenuBuilder.buildMenu(new File(xmlpath), this, dict);
			menuItemList = res.getMenuItems();
			setJMenuBar(res.getMenuBar());
		} catch (WrongXMLFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.pack();
	}
	
	
	/**
	 * Returns the JMenuItem with the given name.
	 * 
	 * You need to cast it yourself to it's real class (e.g. JRadioButtonItem or
	 * JCheckBoxItem)
	 * 
	 * @param name the name of the item.
	 * @return the item or null if the item wasn't found or no menu was added to the menu
	 * through the MenuBuilder.
	 */
	public JMenuItem getMenuItem(String name) {
		if(menuItemList == null)
			return null;
		
		for (JMenuItem item : menuItemList) {
			if(item.getName().equals(name)) {
				return item;
			}
		}
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void handleEventSilently(final IUserInterfaceEvent<E> event) {
		try {
			handleEvent(event);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void handleEvent(final IUserInterfaceEvent<E> event) throws SoundFailedException {
		//don't need to start the sound from the edt...
		if (event.getSound() != null && this.soundManager != null) {
			soundManager.playSound(event.getSound());
		} 

		//but the rest of the frameworks tasks should be done on the edt.
		invokeRunnableOnEDT(new Runnable() {
			
			public void run() {
				handleEventOnEDT(event);
			}
			
		});

	}
	
	/**
	 * Handles the parts of the event that belong to the grafics and should
	 * be handled on the edt. 
	 * 
	 * WARNING: Call this Method from the EDT!
	 * 
	 * @param event the event to handle.
	 */
	private void handleEventOnEDT(final IUserInterfaceEvent<E> event) {
		/*
		 * we check for the elements wrapped in this event, that should be 
		 * handled by the framework, i.e. a board to display, a sound to play
		 * and the informations for updating the informationPanel.
		 */
		if (event.getPauseMode() != null) {
			boardPanel.setPauseMode(event.getPauseMode());
			requestFocusInWindow();
		}
		if (event.getBoard() != null && this.boardPanel != null) {
			if (!event.getForceNewPaint()) {
				// should be the default case
				boardPanel.paintBoard(event.getBoard());
			} else {
				// repaint forced
				boardPanel.paintBoard(event.getBoard(), true);
			}
		} 
		if (event.getInformationMap() != null && infoPanel != null) {
			infoPanel.paint(event.getInformationMap());
		}

		if (initialInvisible && !this.isVisible()) {
			/* 
			 * we wait for the first paint to set the board visible in order
			 * to avoid the window "jumping around" when it changes its size.
			 * 
			 * If we don't use the default layout, the window - this - shouldn't
			 * be set visible.
			 */
				setVisible(true);	
			initialInvisible = false;
		}
		
		pack();
		handleNonFrameworkEvents(event);
	}
	
	/**
	 * Invokes the given Runnable on the edt.
	 * @param r the Runnable.
	 */
	private void invokeRunnableOnEDT(final Runnable r) {
		try {
			SwingUtilities.invokeAndWait(r);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			// Vorschlag: Einen FrameworkError einfuehren.
			// Beachte: Error ist nicht gleich Exception
			// Dieser zeigt an das die "Kacke" am dampfen ist und das Framework falsch benutzt wird.
			// Diesen kann man hier weiter werfen um das Programm zu beenden.
			// Um etwas es debugbar zu halten kann man beim Error die konkrete Exception (ggf die Cause von InvocationTargetException) 
			// rein setzten,
			// Ein einfaches Print-Stacktrace ist, denke ich, eine zu lockere Handhabung.
			// weil alles was hier im Runnable ausgefuehrt wird muss(!) funktioniert.
			//
			// Wenn das Pauseboard nicht setzbar ist, das Board/Info-label nicht zeichenbar ist, etc
			//   muss nicht der Anwneder eine Fehlermeldung bekommen und weiter machen duerfen
			//   sondern das Programm sollte den geordneten Rueckzug antreten.
			//
			// Ist das Board "out of range" wurde es (beim level laden zB) nicht ausreichend geprueft.
			// 		also nicht das Problem vom Framework sondern ein Problem der Implementierung.
			//		daher koennte man sich meiner Meinung anch den haesslichen, aber durch aus kreativen, workaround sparen.
			// Fliegt einem etwas in dem Runnable um die Ohren hat man vorher beim implementieren (nicht benutzen!) einen schweren Fehler begangen.
			//
			// Natuerlich muss das angemessen in form von JavaDoc und doc festgehalten werden.
			// Der Framework-Nutzer sollte im Vorfeld wissen, dass er ein rechteckiges Board welches auf getWidth/Height angemessen antwortet
			//		liefern muss. (und nicht null sondern hoechstens eine leere liste als antwort auf getElements, und alles andere was die methode hier erwartet)
			//		Tut er es nicht nutzt er das Framework falsch und das Framework sollte sich verabschieden.
			// 		Exceptions werden dann wahrscheinlich nur unbehandelt geloggt - und dann wird sich gewundern warum das Framework in einem "illegalen"/undefinierten zustand ist.
			// Weil was passiert denn wenn das Board "out of range" ist? Ein paar Labels sind mit Bildern versehen - ein paar sind es nicht. 
			// 	ist das Board sichtbar?  
			// Grundsaetzlich koennte man sogar so drastisch sein und an dieser Stelle das UserInterface wegwerfen (dispose) 
			e.printStackTrace();
			dispose();
			throw new FrameworkError(e.getTargetException());
		}
	}
	
	/**
	 * This method is called by the framework after the framework tasks have
	 * been completed, i.e. playing sound, redrawing the BoardPanel and update
	 * the inforamtionPanel.<br><br>
	 * 
	 * With this method, you can handle your own event types, depending
	 * on your game.<br><br>
	 * 
	 * WARNING: This method is processed on the awt Event-Dispatching-Thread (EDT)
	 * to let you work on the gui. Do not start any long running tasks in here, it will 
	 * freeze the user interface untill this job is done. The controller is the much better
	 * place for such tasks. If you really need to start that task from here, check {@link javax.swing.SwingWorker}<br><br>
	 * 
	 * As this method is already running on the edt, you don't need to invoke it by hand via
	 * SwingUtilities. If you trie, you will get an exception.<br><br>
	 * 
	 * Speaking of Exceptions: You need to catch every Exception that might be thrown 
	 * in the methods that you are calling before handleNonFrameworkEvents returns, even
	 * a RuntimeException. If an Exception is thrown from this method, it will cause
	 * the framework to throw an FrameworkError and shut down the UserInterface as well
	 * as the Controller.
	 * 
	 * @param event the IUserInterfaceEvent
	 */
	protected abstract void handleNonFrameworkEvents(IUserInterfaceEvent<E> event);

	/**
	 * This method gets called when a user clicks on an icon. The enumeration
	 * starts from top left, with x as the columns and y as the rows.
	 * 
	 * @param x
	 *            the column
	 * @param y
	 *            the row
	 */
	protected abstract void mouseClicked(int x, int y);

	/**
	 * This method is called whenever an key has been typed. This method 
	 * should - in general - be the one you should use for your implementation.<br><br>
	 * 
	 * It will be called with the produced KeyEvent whenever the user "types"
	 * a unicode symbol, i.e. presses and releases the key. If the user keeps 
	 * holding the key (or key combination) constant KeyEvents will get fired
	 * to this method. <br><br>
	 * 
	 * You'll get only one event per unicode character, regardless of how much 
	 * physical keys were used to enter the character, e.g. an € character will
	 * be one KeyEvent, although the user needed to press two keys.<br><br>
	 * 
	 * WARNING: You won't be informed of KeyEvents that don't produce an unicode
	 * character, e.g. Strg or Alt won't produce an KeyEvent for this method.<br><br>
	 * 
	 * If you need to keep track of those keys or you need to be informed about the 
	 * beginning and end of the time the key is pressed, use the keyboardKeyPressed()
	 * and keyboardKeyReleased() methods. <br><br>
	 * 
	 * Check {@link java.awt.event.KeyEvent} for more detailed information.
	 * 
	 * @param evt
	 *            the KeyEvent
	 * 
	 * @see java.awt.event.KeyEvent
	 */
	protected abstract void keyboardKeyTyped(KeyEvent evt);
	
	/**
	 * This method is called whenever a key has been pressed down.<br><br>
	 * 
	 * Use this method to find out about keys that don't produce a character
	 * input (e.g. Strg or Alt) and to track the time a key is pressed.
	 * 
	 * @param e the KeyEvent
	 */
	protected abstract void keyboardKeyPressed(KeyEvent e);

	/**
	 * This method is called whenever a key has been released.<br><br>
	 * 
	 * Use this method to find out about keys that don't produce a character
	 * input (e.g. Strg or Alt) and to track the time a key is pressed.
	 * 
	 * @param e the KeyEvent
	 */
	protected abstract void keyboardKeyReleased(KeyEvent e);

	/**
	 * {@inheritDoc}
	 */
	public abstract void menuItemClicked(String itemName);
	
	
	/**
	 * This method is called whenever the user tries to close 
	 * the game window. You can either close the application 
	 * directly or delegate this job to the controller, to 
	 * check e.g. if there is a progress to be saved.
	 */
	protected abstract void closingRequested();

	
	/**
	 * Shows an open file dialog to the user and returns the 
	 * selected file. 
	 * 
	 * @param path A File representing the path where to start from.
	 * If null, the dialog will start from the users home dir.
	 * @param filters the file extensions that should be accepted. If 
	 * null all filetypes are accepted.
	 * @return the selected file or null if the user canceled the dialog.
	 */
	public File showOpenFileDialog(final File path, final String[] filters) {
		JFileChooser fileChooser = createFileChooser(path, filters);

		if (fileChooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
			//The user canceled the dialog - return null
			return null;
		}
		return fileChooser.getSelectedFile();
	}
	
	/**
	 * Shows an save file dialog to the user and returns the selected file.
	 * 
	 * @param path A File representing the path where to start from.
	 * If null, the dialog will start from the users home dir.
	 * @param filters the file extensions that should be accepted. If 
	 * null all filetypes are accepted.
	 * @return the selected file to save, null if the user canceled the dialog.
	 * WARNING: The file can but does not need to exist. 
	 */
	public File showSaveFileDialog(final File path, final String[] filters) {
		JFileChooser fileChooser = createFileChooser(path, filters);
		if (fileChooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) {
			//The user canceled the dialog - return null
			return null;
		}
		
		if (fileChooser.getFileFilter().accept(fileChooser.getSelectedFile())) {
			return fileChooser.getSelectedFile();
		} 
		
		return fixFileName(fileChooser);
	}

	/**
	 * Fixes a broken filename, i.e. so that it has the 
	 * right suffix and only one dot.
	 * @param fileChooser a JFileChooser with an selected and
	 * broken file.
	 * @return the File with a fixed filename.
	 */
	private File fixFileName(final JFileChooser fileChooser) {
		String fileName = fileChooser.getSelectedFile().getName();
		if (fileName.contains(".")) {
			/* the user entered an invalid file extension
			 * the new fileName should be the substring from zero 
			 * to (but without) the dot. */
			fileName = fileName.substring(0, fileName.indexOf("."));
		}
		
		return new File(fileChooser.getSelectedFile().getParent() 
				+  File.separator 
				+  fileName + "." 
				+   fileChooser.getFileFilter().getDescription());
	}
	
	/**
	 * Creates an new JFileChooser from the given arguments.
	 * 
	 * @param path the path to start from.
	 * @param filters the file extensions that should be allowed.
	 * @return the created JFileChooser
	 */
	private JFileChooser createFileChooser(final File path, final String[] filters) {
		JFileChooser fileChooser = new JFileChooser(path);
		fileChooser.setMultiSelectionEnabled(false);

		// set filters
		if (filters != null) {
			fileChooser.setAcceptAllFileFilterUsed(false);
			for (final String filter : filters) {
				fileChooser.addChoosableFileFilter(new FileFilter() {

					@Override
					public boolean accept(final File f) {
						return f.isDirectory() || f.getName().toLowerCase().endsWith(filter);
					}

					@Override
					public String getDescription() {
						return filter;
					}

				});
			}
		}
		
		return fileChooser;
	}
	
	/**
	 * Shows an JOptionPane to the user and asks him for an imput. You can use 
	 * this e.g. to ask the user to enter his name.
	 * 
	 * @param title The titlebar string.
	 * @param question the question that the user should answer.
	 * @return the String the user entered, null if he canceled.
	 */
	public String getStingInputFromUser(final String title, final String question) {
		return (String) JOptionPane.showInputDialog(null, question, title, JOptionPane.QUESTION_MESSAGE);
	}
	
	@Override
	public void dispose() {
		soundManager.closeSoundBank();
		super.dispose();
	}

	
	
	/**
	 * @return the panel
	 */
	protected BoardPanel<E> getBoardPanel() {
		return boardPanel;
	}

	/**
	 * @param aPanel the panel to set
	 */
	protected void setBoardPanel(final BoardPanel<E> aPanel) {
		this.boardPanel = aPanel;
	}

	/**
	 * @return the infoPanel
	 */
	protected InformationPanel getInfoPanel() {
		return infoPanel;
	}

	/**
	 * @param aInfoPanel the infoPanel to set
	 */
	protected void setInfoPanel(final InformationPanel aInfoPanel) {
		this.infoPanel = aInfoPanel;
	}
	
	/**
	 * Returns the SoundManager, for you to register and setup
	 * the sounds.<br>
	 * 
	 * You shouldn't play the sounds directly, use an IUserInterfaceEvent
	 * instead!<br>
	 * TODO May reduce visibility to protected to avoid problems at this point?<br>  
	 * 
	 * @return the soundManager.
	 */
	public SoundManager getSoundManager() {
		return soundManager;
	}

	/**
	 * @param aSoundManager the soundManager to set
	 */
	protected void setSoundManager(final SoundManager aSoundManager) {
		this.soundManager = aSoundManager;
	}

	/**
	 * {@inheritDoc}
	 */
	public ImageIcon getComponentForBoard(IBoard<E> aBoard, Point coordinates) {
		return getComponentForList(aBoard.getElements(coordinates), coordinates);
	}
	
	/**
	 * @param aList
	 * 		the list of IBoardElements which should be mapped to a {@link java.awt.Image}. This represents the elements on one field of the board.
	 * @param coordinates the coordinates of the component. can be used if necessary or just ignored otherwise. 
	 * @return
	 * 		a Image representing the List of {@link IBoardElement}, ie. the image to be displayed for the field of the board
	 */
	public abstract ImageIcon getComponentForList(List<E> aList, Point coordinates);
	
	/**
	 * {@inheritDoc}
	 */
	public boolean shouldPaintElementsAtCoordinate(IBoard<E> oldBoard, IBoard<E> newBoard, Point coordinate) {
		return !oldBoard.getElements(coordinate).equals(newBoard.getElements(coordinate));
	}
}
