package de.tu_darmstadt.gdi1.bomberman;

import de.tu_darmstadt.gdi1.bomberman.gui.ControllerEvent;
import de.tu_darmstadt.gdi1.bomberman.framework.AbstractBombermanController;
import de.tu_darmstadt.gdi1.bomberman.game.BombermanGame;
import de.tu_darmstadt.gdi1.bomberman.game.elements.GameElement;
import de.tu_darmstadt.gdi1.bomberman.game.levels.BombermanGameData;
import de.tu_darmstadt.gdi1.bomberman.game.levels.BombermanLevelManager;
import de.tu_darmstadt.gdi1.bomberman.gui.UIEvent;
import de.tu_darmstadt.gdi1.bomberman.gui.Gui;
import de.tu_darmstadt.gdi1.framework.exceptions.InvalidLevelDataException;
import de.tu_darmstadt.gdi1.framework.exceptions.NoNextLevelException;
import de.tu_darmstadt.gdi1.framework.exceptions.SoundFailedException;
import de.tu_darmstadt.gdi1.framework.interfaces.IControllerEvent;
import de.tu_darmstadt.gdi1.framework.interfaces.IGameData;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Der BombermanController ist unsere Implementierung eines AbstractBombermanControllers.
 * Der Controller kümmert sich um die Organisation des Projekts und den groben Zusammenbau:
 * - GUI erstellen
 * - Modell erstellen
 * - Alles miteinander bekannt machen, falls nötig
 */
public class BombermanController extends AbstractBombermanController {

	protected Gui gui;
	protected BombermanLevelManager levelManager;
	protected BombermanGame game;

	// INITIALISIERUNG /////////////////////////////////////////////////////////////////////////////

	/**
	 * Initialisiert nur ein paar Member. Der eigentlich interessante Part passiert in initialise().
	 */
	public BombermanController () {
		super();
		levelManager = new BombermanLevelManager();

		/*
		infoProvider = new SameGameLevelInformationProvider();
		theLevelManager = new SerializableLevelManager(infoProvider);
		infoProvider.setLevelManager(theLevelManager);
		generator = new Generator(new GeneratorSettings(), theLevelManager);
		 */
	}

	/**
	 * Erstellt die GUI.
	 */
	@Override
	protected void initialize() {

		// Erstellt ein GUI Objekt. Wenn die Konfigurationsdateien fehlen, exception.
		gui = new Gui(this);
		try {
			// Hole den nächsten Level (oder eben den ersten) ...
			IGameData<GameElement> gd = levelManager.loadNextLevel();
			if (gd == null || !(gd instanceof BombermanGameData))
				throw new InvalidLevelDataException("The game data passed from the level manager where either null or no instance of BombermanGameData.");

			// ... und starte ihn!
			startNewGame((BombermanGameData)gd);
		} catch (NoNextLevelException ex) {
			Logger.getLogger(BombermanController.class.getName()).log(Level.SEVERE, null, ex);
		} catch (InvalidLevelDataException ex) {
			Logger.getLogger(BombermanController.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(BombermanController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void startNewGame (BombermanGameData gamedata)
	{
		if (gamedata == null)
			throw new NullPointerException("Cannot initialize null gamedata.");

		game = new BombermanGame(gamedata, this);
		game.initialiseTickTimer();
	}

	// EVENT HANDLING //////////////////////////////////////////////////////////////////////////////

	@Override
	protected void processEvent(final IControllerEvent event) {
		if (event instanceof ControllerEvent)
			handleControllerEvent((ControllerEvent) event);
	}

	public void handleControllerEvent (ControllerEvent evt) {
		switch (evt.getType())
		{
			case USER_QUIT:
				this.stopWorker();
				sendEventToUI(UIEvent.create(UIEvent.type.QUIT_GAME));
				break;
		}
	}

	// UI COMMUNICATION ////////////////////////////////////////////////////////////////////////////

	public void sendEventToUI (UIEvent evt)
	{
		if (gui == null)
		{

			return;
		}

		try {
			gui.handleEvent(evt);
		} catch (SoundFailedException ex) {
			Logger.getLogger(BombermanController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
