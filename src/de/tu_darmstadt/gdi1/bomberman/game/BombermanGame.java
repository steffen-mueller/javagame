package de.tu_darmstadt.gdi1.bomberman.game;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import de.tu_darmstadt.gdi1.bomberman.BombermanController;
import de.tu_darmstadt.gdi1.bomberman.game.elements.GameElement;
import de.tu_darmstadt.gdi1.bomberman.game.levels.BombermanGameData;
import de.tu_darmstadt.gdi1.bomberman.gui.UIEvent;
import de.tu_darmstadt.gdi1.framework.interfaces.IBoard;

/**
 * Das eigentliche Bomberman Spiel. Diese Klasse enthält die Spiellogik. Unsere Implementierung des
 * IBombermanGame Interfaces. Das Interface normiert die Game Klasse gegenüber dem Testadapter, so
 * dass andere Implementierungen potentiell unseren Test Adapter verwenden könnten.
 *
 * WICHTIG WICHTIG: diese Klasse hat keinen State! Sie weiß nichts über das Spiel selbst. Für alles
 * muss sie die gameData Instanz fragen.
 */
public class BombermanGame implements IBombermanGame {
	protected BombermanGameData gameData;
	protected BombermanController controller;

	Logger logger = Logger.getLogger(BombermanGame.class.getName());

	public static long tickRate = 50;
	protected Timer tickTimer;

	public BombermanGame (BombermanGameData data, BombermanController ctr) {
		gameData = data;
		controller = ctr;

		//say hello to the gui, show the loaded level:
		sendEventToUI(UIEvent.type.NEW_GAME);
	}

	// Ticking /////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Diese Funktion wird für jedes "Frame" aufgerufen. Per "initialiseTickTimer" Funktion kann
	 * dies zeitgesteuert ausgeführt und gestartet werden. Die Testfälle führen die tick() Funktion
	 * aber schneller aus, um zeitabhängige Tests direkt durcharbeiten zu können.
	 *
	 * Im "Echtzeitmodus" per Timer kommt der tick 20x in der Sekunde - 20 Ticks sind also eine Sekunde.
	 */
	@Override
	public void tick ()
	{
		//logger.log(Level.INFO, "Tick tock!");
	}

	/**
	 * Startet einen Timer, der die Tick Funktion alle tickRate Millisekunden ausführt.
	 */
	public void initialiseTickTimer ()
	{
		if (tickTimer != null)
			return;

		// TickTimer anschmeißen. Hail to the closure: die run() Methode in der anonymen TimerTask
		// Klasse kennt die tick() Funktion des BombermanGame Objekts.
		tickTimer = new Timer();
		tickTimer.scheduleAtFixedRate(new TimerTask(){
			@Override
			public void run() {
				tick();
			}
		}, tickRate, tickRate);
	}

	/**
	 * Stoppt den mit initialiseTickTimer gestarteten Timer wieder.
	 */
	public void disposeTickTimer ()
	{
		tickTimer.cancel();
		tickTimer = null;
	}

	// Gamedata Getter/Setter //////////////////////////////////////////////////////////////////////

	/**
	 * Gibt das aktuelle Spielfeld IBoard zurück.
	 */
	@Override
	public IBoard<GameElement> getBoard() {
		return this.gameData.getStepManager().getCurrentBoard();
	}

	// Event Management ////////////////////////////////////////////////////////////////////////////

	/**
	 * Versendet ein Event an das User Interface. Der Controller wird als Zwischenhändler verwendet,
	 * da sich Game Object und GUI besser nicht direkt kennen dürfen.
	 * @param type
	 */
	private void sendEventToUI(UIEvent.type type) {
		UIEvent event = new UIEvent(type);
		event.setBoard(getBoard());
		//event.setInformationMap(infoMapManager.getInfoMap());
		controller.sendEventToUI(event);
	}
}
