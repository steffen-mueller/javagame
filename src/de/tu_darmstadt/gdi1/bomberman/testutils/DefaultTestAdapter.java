package de.tu_darmstadt.gdi1.bomberman.testutils;

import java.io.File;
import java.util.List;

import de.tu_darmstadt.gdi1.bomberman.BombermanController;
import de.tu_darmstadt.gdi1.bomberman.game.BombermanGame;
import de.tu_darmstadt.gdi1.bomberman.game.elements.Bomb;
import de.tu_darmstadt.gdi1.bomberman.game.elements.Explosion;
import de.tu_darmstadt.gdi1.bomberman.game.elements.Floor;
import de.tu_darmstadt.gdi1.bomberman.game.elements.GameElement;
import de.tu_darmstadt.gdi1.bomberman.game.elements.Player;
import de.tu_darmstadt.gdi1.bomberman.game.elements.PowerUp;
import de.tu_darmstadt.gdi1.bomberman.game.elements.Stone;
import de.tu_darmstadt.gdi1.bomberman.game.elements.Wall;
import de.tu_darmstadt.gdi1.bomberman.game.levels.BombermanGameData;
import de.tu_darmstadt.gdi1.bomberman.game.levels.BombermanLevelManager;
import de.tu_darmstadt.gdi1.bomberman.gui.ControllerInputEvent;
import de.tu_darmstadt.gdi1.framework.interfaces.IBoard;
import de.tu_darmstadt.gdi1.framework.interfaces.IGameData;

/**
 * Der Testadapter wird von den Testfällen verwendet, um mit dem Spiel zu interagieren.
 * Der Adapter kapselt alle testbaren Aktivitäten und bildet sie auf die Spielimplementierung ab.
 * Die Tests arbeiten gegen das ITestAdapter Interface.
 */
public class DefaultTestAdapter implements ITestAdapter {

	BombermanController controller;
	BombermanGame game;
	BombermanLevelManager levelManager;

	ControllerInputEvent.button[] lastButtons;

	DefaultTestAdapter () {
		levelManager = new BombermanLevelManager();
		lastButtons = new ControllerInputEvent.button[4];
	}

	// LEVEL LOADING ///////////////////////////////////////////////////////////////////////////////

	/**
	 * Lädt einen Level aus der angegebenen Datei.
	 * @param path
	 * @return Ob es geklappt hat.
	 */
	@Override
	public boolean loadLevelFromFile (String path) {
		File f = new File(path);
		if (!f.exists())
			return false;

		try {
			IGameData<GameElement> gd = levelManager.loadLevel(f);
			initGame((BombermanGameData)gd);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	/**
	 * Lädt einen Level aus dem übergebenen String, als wäre der String der Inhalt der Datei.
	 * Praktisch für Inline Levels.
	 * @param data Der Levelstring
	 * @return Ob es geklappt hat.
	 */
	@Override
	public boolean loadLevelFromString (String data) {
		try {
			IGameData<GameElement> gd = levelManager.loadLevel(data);
			initGame((BombermanGameData)gd);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	protected void initGame (BombermanGameData gd) {
		if (gd == null)
			throw new NullPointerException("Cannot initialize null gamedata.");

		controller = new BombermanController(true);
		game = new BombermanGame(gd, controller);
		controller.setGame(game);
	}

	// SIMULATING PLAYER CONTROLS //////////////////////////////////////////////////////////////////

	/**
	 * Äquivalent zum normalen Richtungs-Tastendruck. Gibt die Richtung an, in die sich der
	 * angegebene Spieler bewegen WILL in den kommenden Ticks. Wenn er damit aufhören soll, muss
	 * die Methode attemptStopPlayer aufgerufen werden.
	 * Die eigentliche Bewegung erfolgt erst zum nächsten Tick (vorher passiert nichts).
	 * @param playerIndex
	 * @param dir
	 */
	@Override
	public void attemptMovePlayer (int playerIndex, Direction dir) {

		ControllerInputEvent.button btn = ControllerInputEvent.button.NULL;
		switch (dir) {
			case UP: btn = ControllerInputEvent.button.UP; break;
			case DOWN: btn = ControllerInputEvent.button.DOWN; break;
			case LEFT: btn = ControllerInputEvent.button.LEFT; break;
			case RIGHT: btn = ControllerInputEvent.button.RIGHT; break;
			case BOMB: btn = ControllerInputEvent.button.BOMB; break;
		}

		ControllerInputEvent ev = new ControllerInputEvent(playerIndex, btn, ControllerInputEvent.state.PRESSED);
		controller.testProcessEvent(ev);
		lastButtons[playerIndex-1] = btn;
	}

	/**
	 * Lässt den angegebenen Spieler stehen bleiben, falls er sich denn bewegte.
	 * @param playerIndex
	 */
	public void attemptStopPlayer (int playerIndex) {
		if (lastButtons[playerIndex-1] == ControllerInputEvent.button.NULL)
			return;

		ControllerInputEvent ev = new ControllerInputEvent(playerIndex, lastButtons[playerIndex-1], ControllerInputEvent.state.RELEASED);
		controller.testProcessEvent(ev);
		lastButtons[playerIndex-1] = ControllerInputEvent.button.NULL;
	}

	/**
	 * Lässt den Spieler eine Bombe legen lassen WOLLEN. Äquivalent zum normalen Tastendruck.
	 * Die eigentliche Aktion erfolgt erst zum nächsten Tick (vorher passiert nichts).
	 * @param playerIndex
	 */
	@Override
	public void attemptPlaceBomb (int playerIndex) {
		ControllerInputEvent ev = new ControllerInputEvent(playerIndex, ControllerInputEvent.button.BOMB, ControllerInputEvent.state.PRESSED);
		controller.testProcessEvent(ev);
	}

	// SIMULATING TIME /////////////////////////////////////////////////////////////////////////////

	/**
	 * Lässt die angegebene Anzahl Ticks vergehen.
	 * @param count
	 */
	@Override
	public void tick (int count) {
		for (int i = 0; i < count; i++)
			game.tick();
	}

	// FEEDBACK ////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Liefert ein zweidimensionales Array (erste Dimension die Breite des Spielfeldes, zweite die
	 * Höhe) von Feldinformationen. Der einzige Feedbackkanal für die Tests.
	 * @return
	 */
	@Override
	public FieldStatus[][] getLevelStatus() {
		IBoard<GameElement> board = game.getBoard();
		FieldStatus[][] fs = new FieldStatus[board.getWidth()][board.getHeight()];

		for (int x = 0; x < board.getWidth(); x++) {
			for (int y = 0; y < board.getHeight(); y++) {
				List<GameElement> el = board.getElements(x, y);

				if (el == null)
					continue;

				Element[] ls = new Element[el.size()];
				for (int i = 0; i < el.size(); i++)
				{
					if (el.get(i) instanceof Player) {
						Player p = (Player)el.get(i);
						switch(p.getPlayerID()) {
							case 1: ls[i] = Element.PLAYER1; break;
							case 2: ls[i] = Element.PLAYER2; break;
							case 3: ls[i] = Element.PLAYER3; break;
							case 4: ls[i] = Element.PLAYER4; break;
						}
					}
					else if (el.get(i) instanceof Bomb)
						ls[i] = Element.BOMB;
					else if (el.get(i) instanceof Floor)
						ls[i] = Element.FLOOR;
					else if (el.get(i) instanceof Stone)
						ls[i] = Element.STONE;
					else if (el.get(i) instanceof Wall)
						ls[i] = Element.WALL;
					else if (el.get(i) instanceof Explosion)
						ls[i] = Element.EXPLOSION;
					else if (el.get(i) instanceof PowerUp)
						ls[i] = Element.POWERUP;
					else
						System.out.println("Missed class: "+el.get(i).getClass());
				}

				fs[x][y] = new FieldStatus();
				fs[x][y].elements = ls;
			}
		}

		return fs;
	}

	/**
	 * Gibt Informationen über die Konfiguration zurück - bspw. wie schnell Spieler laufen können.
	 */
	@Override
	public int getIntParameter (Parameter p) {
		if (p == Parameter.PLAYER_SPEED)
			return 5;
		return -1;
	}
	
	/**
	 * Get Element a at Bomb (b,c)
	 */
public Bomb getBomb(int b, int c){
		
		for (int i = 0; i < game.getBoard().getElements(b, c).size(); i++) {
			if (game.getBoard().getElements(b,c).get(i) instanceof Bomb){
				return ((Bomb) game.getBoard().getElements(b,c).get(i));
			}
		}	 
		return null;	
	}

/**
 * Get Element a at player (b,c)
 */
public Player getPlayer(int b, int c){
	
	for (int i = 0; i < game.getBoard().getElements(b, c).size(); i++) {
		if (game.getBoard().getElements(b,c).get(i) instanceof Player){
			return ((Player) game.getBoard().getElements(b,c).get(i));
		}
	}	 
	return null;	
}

/**
 * Gibt die aktuelle Karte als String aus
 */	 
public String maptoString(){
	return "blub";
}

/**
 * Gibt die abgelaufene Zeit zurueck
 */
public long GetTime() {
	return game.getTimeInSeconds();
}
}
