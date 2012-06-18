package de.tu_darmstadt.gdi1.bomberman.gui;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import de.tu_darmstadt.gdi1.bomberman.BombermanController;
import de.tu_darmstadt.gdi1.bomberman.framework.AbstractBombermanController;
import de.tu_darmstadt.gdi1.bomberman.game.elements.GameElement;
import de.tu_darmstadt.gdi1.framework.interfaces.IBoard;
import de.tu_darmstadt.gdi1.framework.interfaces.IUserInterfaceEvent;
import de.tu_darmstadt.gdi1.framework.utils.Point;
import de.tu_darmstadt.gdi1.framework.view.UserInterface;


/**
 * Das User Interface. Nimmt zwar Benutzereingaben und so weiter entgegen, darf aber nix selbst -
 * reicht alles an den Controller weiter, der der einzige wirkliche soziale Kontakt des GUI ist.
 */
public class Gui extends UserInterface<GameElement> {

	protected AbstractBombermanController controller;
	private MainMenu mainMenu;

	protected ArrayList<Point> dirtyPoints = new ArrayList<Point>();

	Logger logger = Logger.getLogger(BombermanController.class.getName());

	public Gui (AbstractBombermanController ctr)
	{
		super();
		controller = ctr;

		setTitle("The amazing TU Darmstadt Bomberman!");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		// Creating Main Menu
		mainMenu = new MainMenu(this);
		mainMenu.setDefaultCloseOperation(HIDE_ON_CLOSE);
	}

	@Override
	protected void handleNonFrameworkEvents(IUserInterfaceEvent<GameElement> event) {
		if (!(event instanceof UIEvent))
		{
			// TODO: mourn around
			return;
		}

		UIEvent bmevent = (UIEvent) event;
		switch (bmevent.getType())
		{
			case QUIT_GAME:
				quitGameGUI();
				break;
			case MAIN_MENU:
				showMainMenu();
				break;
			case REDRAW:
				// This does nothing, it is just called to tell the framework, that the UI has work to do and needs repaint
				break;
		}
	}

	void quitGameGUI ()
	{
		System.out.println("Disposing GUI...");
		mainMenu.dispose();
		dispose();
	}

	public void showMainMenu ()
	{
		System.out.println("Displaying main menu...");
		mainMenu.setVisible(true);
	}

	/**
	 * Sets a coordinate of the game board dirty.
	 * @param p
	 */
	public void setCoordinateDirty (Point p) {
		System.out.println("Setting dirty "+p);
		if (!dirtyPoints.contains(p))
			dirtyPoints.add(p);
	}

	public synchronized void addDirtyPoints (ArrayList<Point> points) {
		for (Point p : points) {
			if (!dirtyPoints.contains(p))
				dirtyPoints.add(p);
		}
	}

	public synchronized void addDirtyPoint (Point point) {
		if (!dirtyPoints.contains(point))
			dirtyPoints.add(point);
	}

	public synchronized void redrawDirty (IBoard<GameElement> board) {
		if (board == null || dirtyPoints.isEmpty())
			return;

		for (Point p : dirtyPoints) {
			JLabel label = boardPanel.getLabelAt(board, p.getX(), p.getY());
			ImageIcon icon = this.getComponentForBoard(board, p);
			if (icon != null) {
				label.setIcon(icon);
			}
		}

		dirtyPoints.clear();
	}

	// PLAYER CONTROLS /////////////////////////////////////////////////////////////////////////////

	@Override
	protected void mouseClicked(int x, int y) {
		// We do not (yet) care about clicking.
	}


	@Override
	protected void keyboardKeyTyped(KeyEvent evt) {
		// We do not care about typing.
	}

	@Override
	protected void keyboardKeyPressed(KeyEvent e) {
		ControllerInputEvent evt = createInputEvent(e, ControllerInputEvent.state.PRESSED);
		if (evt != null) {
			controller.handleEvent(evt);
		}
	}

	@Override
	protected void keyboardKeyReleased(KeyEvent e) {
		ControllerInputEvent evt = createInputEvent(e, ControllerInputEvent.state.RELEASED);
		if (evt != null) {
			controller.handleEvent(evt);
		}
	}

	/**
	 * Mappt ein KeyEvent auf ein Inputevent. Die aktuell hart verdrahtete Tastenbelegung kann hier
	 * drin später aus einer dynamischen Map gelesen werden.
	 * Die Methode erzeugt ein Input Event, falls die betätigte Taste eine Bedeutung im Spiel hat.
	 * Falls nicht, wird null zurückgegeben.
	 * @param evt Das KeyEvent, das verarbeitet werden soll.
	 * @param state Ob er Button gedrückt ist oder losgelassen wurde.
	 * @return
	 */
	protected ControllerInputEvent createInputEvent (KeyEvent evt, ControllerInputEvent.state state) {
		int playerIdx = 0;
		ControllerInputEvent.button btn = ControllerInputEvent.button.NULL;
		switch (evt.getKeyCode()) {
			// PLAYER 1
			case (KeyEvent.VK_UP): // UP
				playerIdx = 1;
				btn = ControllerInputEvent.button.UP;
				break;
			case (KeyEvent.VK_DOWN): // DOWN
				playerIdx = 1;
				btn = ControllerInputEvent.button.DOWN;
				break;
			case (KeyEvent.VK_LEFT): // LEFT
				playerIdx = 1;
				btn = ControllerInputEvent.button.LEFT;
				break;
			case (KeyEvent.VK_RIGHT): // RIGHT
				playerIdx = 1;
				btn = ControllerInputEvent.button.RIGHT;
				break;
			case (KeyEvent.VK_SPACE): // BOMB BABY BOMB!
				playerIdx = 1;
				btn = ControllerInputEvent.button.BOMB;
				break;

			// PLAYER 2
			case (KeyEvent.VK_W): // UP
				playerIdx = 2;
				btn = ControllerInputEvent.button.UP;
				break;
			case (KeyEvent.VK_S): // DOWN
				playerIdx = 2;
				btn = ControllerInputEvent.button.DOWN;
				break;
			case (KeyEvent.VK_A): // LEFT
				playerIdx = 2;
				btn = ControllerInputEvent.button.LEFT;
				break;
			case (KeyEvent.VK_D): // RIGHT
				playerIdx = 2;
				btn = ControllerInputEvent.button.RIGHT;
				break;
			case (KeyEvent.VK_Q): // BOMB BABY BOMB!
				playerIdx = 2;
				btn = ControllerInputEvent.button.BOMB;
				break;

			// Menu Buttons
			case (KeyEvent.VK_M): // MAIN MENU
				playerIdx = 1;
				btn = ControllerInputEvent.button.MAIN_MENU;
				break;
		}

		if (playerIdx < 1)
			return null;

		return new ControllerInputEvent(playerIdx, btn, state);
	}

	// GAME MANAGEMENT /////////////////////////////////////////////////////////////////////////////

	@Override
	protected void closingRequested() {
		ControllerEvent evt = ControllerEvent.create(ControllerEvent.type.USER_QUIT);
		controller.handleEventImmediately(evt);
	}

	@Override
	public void menuItemClicked(String itemName) {
		throw new UnsupportedOperationException("Not supported yet.");
	}


	// GUI CELL RENDERER ///////////////////////////////////////////////////////////////////////////

	@Override
	public ImageIcon getComponentForList(List<GameElement> aList, Point coordinates) {
		// TODO
		ArrayList<ImageIcon> iconList = new ArrayList<ImageIcon>();
		ImageIcon ret = null;
		for (GameElement gameElement : aList) {
			iconList.add(gameElement.getImageIcon());
		}

		if (iconList.size() == 1) {
			return iconList.get(0);
		}
		else {
			return combineImages(iconList);
		}
	}

	private ImageIcon combineImages(ArrayList<ImageIcon> imageList)
	{
		BufferedImage bufferedImage = new BufferedImage(40, 40, BufferedImage.TYPE_INT_ARGB);
		Graphics temp = bufferedImage.createGraphics();
		for (ImageIcon imageIcon : imageList) {
			BufferedImage bi = new BufferedImage(imageIcon.getIconWidth(), imageIcon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics g = bi.createGraphics();
			g.drawImage(imageIcon.getImage(), 0, 0, null);
			g.dispose();
			temp.drawImage(bi, 0, 0, null);
		}
		temp.dispose();

		return new ImageIcon(bufferedImage);
	}
}
