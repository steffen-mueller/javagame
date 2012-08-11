package de.tu_darmstadt.gdi1.bomberman;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.tu_darmstadt.gdi1.bomberman.framework.AbstractBombermanController;
import de.tu_darmstadt.gdi1.bomberman.game.BombermanGame;
import de.tu_darmstadt.gdi1.bomberman.game.elements.GameElement;
import de.tu_darmstadt.gdi1.bomberman.game.elements.Player;
import de.tu_darmstadt.gdi1.bomberman.game.levels.BombermanGameData;
import de.tu_darmstadt.gdi1.bomberman.game.levels.BombermanLevelManager;
import de.tu_darmstadt.gdi1.bomberman.gui.ControllerEvent;
import de.tu_darmstadt.gdi1.bomberman.gui.ControllerInputEvent;
import de.tu_darmstadt.gdi1.bomberman.gui.Gui;
import de.tu_darmstadt.gdi1.bomberman.gui.UIEvent;
import de.tu_darmstadt.gdi1.bomberman.sound.SoundManagerFactory;
import de.tu_darmstadt.gdi1.framework.exceptions.InvalidLevelDataException;
import de.tu_darmstadt.gdi1.framework.exceptions.NoNextLevelException;
import de.tu_darmstadt.gdi1.framework.exceptions.ParameterOutOfRangeException;
import de.tu_darmstadt.gdi1.framework.exceptions.SoundFailedException;
import de.tu_darmstadt.gdi1.framework.interfaces.IControllerEvent;
import de.tu_darmstadt.gdi1.framework.interfaces.IGameData;
import de.tu_darmstadt.gdi1.framework.utils.FrameworkUtils;
import de.tu_darmstadt.gdi1.framework.utils.Point;
import java.util.ArrayList;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Der BombermanController ist unsere Implementierung eines
 * AbstractBombermanControllers. Der Controller kümmert sich um die Organisation
 * des Projekts und den groben Zusammenbau: - GUI erstellen - Modell erstellen -
 * Alles miteinander bekannt machen, falls nötig
 */
public class BombermanController extends AbstractBombermanController {

    protected Gui gui;
    protected BombermanLevelManager levelManager;
    protected BombermanGame game;
    protected boolean testMode = false;
    public HashMap<Integer, Player> players;
    Logger logger = Logger.getLogger(BombermanController.class.getName());

    // INITIALISIERUNG /////////////////////////////////////////////////////////////////////////////
    /**
     * Initialisiert nur ein paar Member. Der eigentlich interessante Part
     * passiert in initialise().
     */
    public BombermanController() {
        super();
        levelManager = new BombermanLevelManager();

        /*
         * infoProvider = new SameGameLevelInformationProvider();
         * theLevelManager = new SerializableLevelManager(infoProvider);
         * infoProvider.setLevelManager(theLevelManager); generator = new
         * Generator(new GeneratorSettings(), theLevelManager);
         */
    }

    /**
     * Startet den Controller im Testmodus, der die Eventqueue und GUI nicht
     * startet.
     *
     * @param testmode
     */
    public BombermanController(boolean testmode) {
        levelManager = new BombermanLevelManager();
        testMode = testmode;
    }

    /**
     * Erstellt die GUI.
     */
    @Override
    protected void initialize() {
        if (testMode == true) {
            return;
        }

        // Erstellt ein GUI Objekt. Wenn die Konfigurationsdateien fehlen, exception.
        gui = new Gui(this);
        try {
            // Hole den nächsten Level (oder eben den ersten) ...
            IGameData<GameElement> gd = levelManager.loadNextLevel();
            players = levelManager.getPlayers();
            if (gd == null || !(gd instanceof BombermanGameData)) {
                throw new InvalidLevelDataException("The game data passed from the level manager where either null or no instance of BombermanGameData.");
            }

            // ... und starte ihn!
            startNewGame((BombermanGameData) gd);
            logger.log(Level.INFO, "Loaded/active LevelFile: " + gd.getLevelFilename());
        } catch (NoNextLevelException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (InvalidLevelDataException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }

        try {
            // register sounds
            SoundManagerFactory.getSoundManager().registerSound(SoundManagerFactory.SoundLabel.BACKGROUND,
                    FrameworkUtils.loadFile(SoundManagerFactory.SoundPath.BACKGROUND));
            SoundManagerFactory.getSoundManager().registerSound(SoundManagerFactory.SoundLabel.BOMB,
                    FrameworkUtils.loadFile(SoundManagerFactory.SoundPath.BOMB));
            SoundManagerFactory.getSoundManager().registerSound(SoundManagerFactory.SoundLabel.DIE,
                    FrameworkUtils.loadFile(SoundManagerFactory.SoundPath.DIE));
            SoundManagerFactory.getSoundManager().registerSound(SoundManagerFactory.SoundLabel.GAME_END,
                    FrameworkUtils.loadFile(SoundManagerFactory.SoundPath.GAME_END));
            SoundManagerFactory.getSoundManager().registerSound(SoundManagerFactory.SoundLabel.POWER_UP,
                    FrameworkUtils.loadFile(SoundManagerFactory.SoundPath.POWER_UP));
            SoundManagerFactory.getSoundManager().registerSound(SoundManagerFactory.SoundLabel.WALK,
                    FrameworkUtils.loadFile(SoundManagerFactory.SoundPath.WALK));
            // start background sound
            SoundManagerFactory.getSoundManager().setLoop(SoundManagerFactory.SoundLabel.BACKGROUND, true);
            SoundManagerFactory.playWithoutAnnoyingExceptions(SoundManagerFactory.SoundLabel.BACKGROUND);
        } catch (ParameterOutOfRangeException ex) {
            Logger.getLogger(BombermanGame.class.getName()).log(Level.SEVERE, "Error while setting up the sounds", ex);
        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(BombermanGame.class.getName()).log(Level.SEVERE, "Error while setting up the sounds", ex);
        } catch (IOException ex) {
            Logger.getLogger(BombermanGame.class.getName()).log(Level.SEVERE, "Error while setting up the sounds", ex);
        }
    }

    private void startNewGame(BombermanGameData gamedata) {
        if (gamedata == null) {
            throw new NullPointerException("Cannot initialize null gamedata.");
        }

        game = new BombermanGame(gamedata, this);
        game.initialiseTickTimer();
    }

    public void setGame(BombermanGame game) {
        this.game = game;
    }

    public void addDirtyPoints(ArrayList<Point> points) {
        if (gui != null) {
            gui.addDirtyPoints(points);
        }
    }

    public void addDirtyPoint(Point point) {
        if (gui != null) {
            gui.addDirtyPoint(point);
        }
    }

    public void redrawDirtyPoints() {
        if (gui != null) {
            gui.redrawDirty(game.getBoard());
        }
    }

    // EVENT HANDLING //////////////////////////////////////////////////////////////////////////////
    @Override
    protected void processEvent(final IControllerEvent event) {
        try {
            if (event instanceof ControllerInputEvent) {
                game.handleInputEvent((ControllerInputEvent) event);
            } else if (event instanceof ControllerEvent) {
                handleControllerEvent((ControllerEvent) event);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void handleControllerEvent(ControllerEvent evt) {
        switch (evt.getType()) {
            case USER_QUIT:
                this.stopWorker();
                game.disposeTickTimer();
                sendEventToUI(UIEvent.create(UIEvent.type.QUIT_GAME));
                break;
			case CHANGE_SKIN:
				this.gui.redrawFull(game.getBoard());
				break;
        }
    }

    /**
     * Ruft processEvent synchron und direkt mit dem übergebenen Event auf und
     * umgeht dabei die Eventqueue des AbstractControllers, da diese im
     * Testszenario Probleme macht. !!! NUR IN TESTS AUFRUFEN, NICHT IM ECHTEN
     * CODE !!!
     *
     * @param event
     */
    public void testProcessEvent(final IControllerEvent event) {
        processEvent(event);
    }

    // UI COMMUNICATION ////////////////////////////////////////////////////////////////////////////
    public void sendEventToUI(UIEvent evt) {
        if (gui == null) {
            return;
        }

        try {
            gui.handleEvent(evt);
        } catch (SoundFailedException ex) {
            Logger.getLogger(BombermanController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
