package de.tu_darmstadt.gdi1.bomberman.gui;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import de.tu_darmstadt.gdi1.bomberman.BombermanController;
import de.tu_darmstadt.gdi1.bomberman.framework.AbstractBombermanController;
import de.tu_darmstadt.gdi1.bomberman.game.elements.GameElement;
import de.tu_darmstadt.gdi1.bomberman.sound.SoundManagerFactory;
import de.tu_darmstadt.gdi1.framework.interfaces.IBoard;
import de.tu_darmstadt.gdi1.framework.interfaces.IUserInterfaceEvent;
import de.tu_darmstadt.gdi1.framework.utils.Point;
import de.tu_darmstadt.gdi1.framework.view.UserInterface;

/**
 * Das User Interface. Nimmt zwar Benutzereingaben und so weiter entgegen, darf
 * aber nix selbst - reicht alles an den Controller weiter, der der einzige
 * wirkliche soziale Kontakt des GUI ist.
 */
public class Gui extends UserInterface<GameElement> {

    private static Gui INSTANCE = null;
    protected AbstractBombermanController controller;
    private MainMenu mainMenu;
    private WinnerScreen winnerScreen;
    // Used for skin cycling
    int skinIndex = 0;
    protected ArrayList<Point> dirtyPoints = new ArrayList<Point>();
    Logger logger = Logger.getLogger(BombermanController.class.getName());

    private Gui(AbstractBombermanController ctr) {
        super();
        controller = ctr;

        setTitle("The amazing TU Darmstadt Bomberman!");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Creating Main Menu
        mainMenu = new MainMenu(this);
        mainMenu.setLocationRelativeTo(this);
        mainMenu.setDefaultCloseOperation(HIDE_ON_CLOSE);

        // Creating Winner Screen
        winnerScreen = new WinnerScreen(this);
        winnerScreen.setLocationRelativeTo(this);
        winnerScreen.setDefaultCloseOperation(HIDE_ON_CLOSE);
    }

    public static void createInstance(AbstractBombermanController ctr) {
        INSTANCE = new Gui(ctr);
    }

    public static Gui getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException("create instance first");
        }
        return INSTANCE;
    }

    @Override
    protected void handleNonFrameworkEvents(IUserInterfaceEvent<GameElement> event) {
        if (!(event instanceof UIEvent)) {
            // TODO: mourn around
            return;
        }

        UIEvent bmevent = (UIEvent) event;
        switch (bmevent.getType()) {
            case QUIT_GAME:
                quitGameGUI();
                break;
            case MAIN_MENU:
	            Gui.getInstance().showMainMenu();
                break;
            case UPDATE_TIME:
                setTitle("The amazing TU Darmstadt Bomberman! [" + ((BombermanController) controller).getTimeInSeconds() + " sec.]");
                break;
            case REDRAW:
                // This does nothing, it is just called to tell the framework, that the UI has work to do and needs repaint
                break;
        }
    }

    void quitGameGUI() {
        System.out.println("Disposing GUI...");
        // Gui does not dispose while sound is playing.
        // So I found no better place for stopping the sound.
        SoundManagerFactory.getSoundManager().setLoop(SoundManagerFactory.SoundLabel.BACKGROUND, false);
        SoundManagerFactory.getSoundManager().stopSound(SoundManagerFactory.SoundLabel.BACKGROUND);
        mainMenu.dispose();
        winnerScreen.dispose();
        dispose();
    }

    public void showMainMenu() {
        System.out.println("Displaying main menu...");
        mainMenu.setVisible(true);
    }

    public void showWinnerScreen(String winnerText) {
        System.out.println("Displaying winner screen...");
        winnerScreen.updateWinnerText(winnerText);
        winnerScreen.setVisible(true);
    }

    /**
     * Sets a coordinate of the game board dirty.
     *
     * @param p
     */
    public void setCoordinateDirty(Point p) {
        System.out.println("Setting dirty " + p);
        if (!dirtyPoints.contains(p)) {
            dirtyPoints.add(p);
        }
    }

    public synchronized void addDirtyPoints(ArrayList<Point> points) {
        for (Point p : points) {
            if (!dirtyPoints.contains(p)) {
                dirtyPoints.add(p);
            }
        }
    }

    public synchronized void addDirtyPoint(Point point) {
        if (!dirtyPoints.contains(point)) {
            dirtyPoints.add(point);
        }
    }

    public synchronized void redrawDirty(IBoard<GameElement> board) {
        if (board == null || dirtyPoints.isEmpty()) {
            return;
        }

        for (Point p : dirtyPoints) {
            JLabel label = boardPanel.getLabelAt(board, p.getX(), p.getY());
            ImageIcon icon = this.getComponentForBoard(board, p);
            if (icon != null) {
                label.setIcon(icon);
            }
        }

        dirtyPoints.clear();
    }

    public synchronized void redrawFull(IBoard<GameElement> board) {
        if (board == null) {
            return;
        }

        boardPanel.paintBoard(board, true);
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
     * Mappt ein KeyEvent auf ein Inputevent. Die aktuell hart verdrahtete
     * Tastenbelegung kann hier drin später aus einer dynamischen Map gelesen
     * werden. Die Methode erzeugt ein Input Event, falls die betätigte Taste
     * eine Bedeutung im Spiel hat. Falls nicht, wird null zurückgegeben.
     *
     * @param evt Das KeyEvent, das verarbeitet werden soll.
     * @param state Ob er Button gedrückt ist oder losgelassen wurde.
     * @return
     */
    protected ControllerInputEvent createInputEvent(KeyEvent evt, ControllerInputEvent.state state) {
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
            case (KeyEvent.VK_CONTROL): // BOMB BABY BOMB!
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

            // PLAYER 3
            case (KeyEvent.VK_I): // UP
                playerIdx = 3;
                btn = ControllerInputEvent.button.UP;
                break;
            case (KeyEvent.VK_K): // DOWN
                playerIdx = 3;
                btn = ControllerInputEvent.button.DOWN;
                break;
            case (KeyEvent.VK_J): // LEFT
                playerIdx = 3;
                btn = ControllerInputEvent.button.LEFT;
                break;
            case (KeyEvent.VK_L): // RIGHT
                playerIdx = 3;
                btn = ControllerInputEvent.button.RIGHT;
                break;
            case (KeyEvent.VK_O): // BOMB BABY BOMB!
                playerIdx = 3;
                btn = ControllerInputEvent.button.BOMB;
                break;

            // PLAYER 4
            case (KeyEvent.VK_NUMPAD8): // UP
                playerIdx = 4;
                btn = ControllerInputEvent.button.UP;
                break;
            case (KeyEvent.VK_NUMPAD2): // DOWN
                playerIdx = 4;
                btn = ControllerInputEvent.button.DOWN;
                break;
            case (KeyEvent.VK_NUMPAD4): // LEFT
                playerIdx = 4;
                btn = ControllerInputEvent.button.LEFT;
                break;
            case (KeyEvent.VK_NUMPAD6): // RIGHT
                playerIdx = 4;
                btn = ControllerInputEvent.button.RIGHT;
                break;
            case (KeyEvent.VK_NUMPAD5): // BOMB BABY BOMB!
                playerIdx = 4;
                btn = ControllerInputEvent.button.BOMB;
                break;

            // Skin switching
            case (KeyEvent.VK_SPACE):
                if (state == ControllerInputEvent.state.PRESSED) {
                    switchSkin();
                }
                break;

            // DEBUG Buttons
            case (KeyEvent.VK_X): // Kill all Stones
                playerIdx = 1;
                btn = ControllerInputEvent.button.X_BUTTON;
                break;

            // Menu Buttons
            case (KeyEvent.VK_M): // MAIN MENU
                playerIdx = 1;
                btn = ControllerInputEvent.button.MAIN_MENU;
                break;
        }

        if (playerIdx < 1) {
            return null;
        }

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

    public void switchSkin() {
        // Receive all skin subdirectories
        File dir = new File("resource");
        FilenameFilter filter = new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return name.startsWith("images-");
            }
        };
        String[] children = dir.list(filter);

        // Rotate the skinindex and get the appropriate skin.
        skinIndex = (skinIndex + 1) % (children.length + 1);
        String skin = "images";
        if (skinIndex > 0) {
            skin = children[skinIndex - 1];
        }

        // Switch for game elements
        System.out.println("Switching to skin " + skin + " (idx " + skinIndex + " of " + children.length + ")");
        GameElement.setSkin(skin);

        // Force redraw
        ControllerEvent evt = ControllerEvent.create(ControllerEvent.type.CHANGE_SKIN);
        controller.handleEventImmediately(evt);
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
        } else {
            return combineImages(iconList);
        }
    }

    private ImageIcon combineImages(ArrayList<ImageIcon> imageList) {
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

    public void mainMenuClosed() {
        ControllerEvent evt = ControllerEvent.create(ControllerEvent.type.CONTINUE_GAME);
        controller.handleEventImmediately(evt);
    }
    
    public void winnerScreenClosed() {
        ControllerEvent evt = ControllerEvent.create(ControllerEvent.type.CONTINUE_GAME);
        controller.handleEventImmediately(evt);
    }

    public void restartGameButtonPressed() {
        ControllerEvent evt = ControllerEvent.create(ControllerEvent.type.RESTART_GAME);
        controller.handleEventImmediately(evt);
    }
    
    public void loadNextLevel(){
	    ControllerEvent evt = ControllerEvent.create(ControllerEvent.type.NEXT_LEVEL);
	    controller.handleEventImmediately(evt);
	    winnerScreen.dispose();
    }
}
