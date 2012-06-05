package de.tu_darmstadt.gdi1.bomberman.game;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import de.tu_darmstadt.gdi1.bomberman.BombermanController;
import de.tu_darmstadt.gdi1.bomberman.game.elements.Bomb;
import de.tu_darmstadt.gdi1.bomberman.game.elements.GameElement;
import de.tu_darmstadt.gdi1.bomberman.game.elements.Player;
import de.tu_darmstadt.gdi1.bomberman.game.levels.BombermanGameData;
import de.tu_darmstadt.gdi1.bomberman.gui.ControllerInputEvent;
import de.tu_darmstadt.gdi1.bomberman.gui.UIEvent;
import de.tu_darmstadt.gdi1.framework.interfaces.IBoard;
import de.tu_darmstadt.gdi1.framework.utils.Point;

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
	protected long tickCounter = 0;


	private ArrayList<Bomb> bombs = new ArrayList<Bomb>();

	public BombermanGame (BombermanGameData data, BombermanController ctr) {
		gameData = data;
		controller = ctr;

		//say hello to the gui, show the loaded level:
		sendEventToUI(UIEvent.type.NEW_GAME);
	}

	// BOMBING /////////////////////////////////////////////////////////////////////////////////////

	public void placeBomb (int playerIndex) throws Exception {
		if (playerIndex < 1 || playerIndex > 4)
			throw new Exception("Invalid playerIndex "+playerIndex);

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
		tickCounter++;

		// Keep on moving players that move due to pressed buttons
		continueMoving();
		detonateBombs();
		// TODO: removeExplosions();

		// Redraw what became dirty
		this.controller.redrawDirtyPoints();
	}

	public long getTickCount () {
		return tickCounter;
	}

	private void detonateBombs ()
	{
		for (Bomb bomb : bombs) {
			if (bomb.getTicksTillExplode() == 0) {
				UIEvent event = new UIEvent(UIEvent.type.DETONATE_BOMB);
				event.setForceNewPaint(true);
				controller.sendEventToUI(event);
				bombs.remove(bomb);
			}
			else {
				bomb.decreaseBombTicks();
			}
		}
	}

	/**
	 * Startet einen Timer, der die tick() Funktion alle tickRate Millisekunden ausführt.
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

	// Player movement /////////////////////////////////////////////////////////////////////////////

	public void startPlayerMove (int playerIdx, Player.direction dir) {
		Player pl = gameData.getPlayer(playerIdx);
		if (pl != null) {
			ArrayList<Point> dirtyPoints = new ArrayList<Point>();
			Point from = pl.getPoint();
			pl.setDirection(dir);
			if (pl.move(tickCounter)) {
				this.controller.addDirtyPoint(from);
				this.controller.addDirtyPoint(pl.getPoint());
				this.controller.redrawDirtyPoints();

			}
		}
	}

	public void stopPlayerMove (int playerIdx, Player.direction dir) {
		Player pl = gameData.getPlayer(playerIdx);
		if (pl != null && pl.getDirection() == dir) {
			pl.setDirection(Player.direction.NULL);
		}
	}

	public void continueMoving () {
		// Make each player move if he needs to
		for (Player pl : gameData.getPlayers()) {
			Point from = pl.getPoint();
			if (pl.move(tickCounter)) {
				this.controller.addDirtyPoint(from);
				this.controller.addDirtyPoint(pl.getPoint());
			}
		}
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
		controller.sendEventToUI(event);
	}

	/**
	 * Verarbeitet ein Input Event.
	 * @param event
	 */
	public void handleInputEvent(ControllerInputEvent event) throws Exception {
		if (event.getButton() == ControllerInputEvent.button.NULL)
			return;

		// Either place a bomb or move around.
		if (event.getButton() == ControllerInputEvent.button.BOMB) {
			this.placeBomb(event.getPlayerIndex());
		}
		else {
			// We know that it has to be a direction - map the direction from the ControllerInputEvent
			// to the Players directions (die Wege der losen Kopplung sind unergründlich - hässlich)
			Player.direction dir = Player.direction.NULL;
			switch (event.getButton()) {
				case UP: dir = Player.direction.UP; break;
				case DOWN: dir = Player.direction.DOWN; break;
				case LEFT: dir = Player.direction.LEFT; break;
				case RIGHT: dir = Player.direction.RIGHT; break;
			}

			if (event.getState() == ControllerInputEvent.state.PRESSED) {
				this.startPlayerMove(event.getPlayerIndex(), dir);
			}
			else {
				this.stopPlayerMove(event.getPlayerIndex(), dir);
			}
		}
	}
}
