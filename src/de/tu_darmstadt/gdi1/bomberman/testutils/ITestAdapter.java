package de.tu_darmstadt.gdi1.bomberman.testutils;

import de.tu_darmstadt.gdi1.bomberman.game.elements.Bomb;
import de.tu_darmstadt.gdi1.bomberman.game.elements.Player;

/**
 * Der Testadapter wird von den Testfällen verwendet, um mit dem Spiel zu interagieren.
 * Der Adapter kapselt alle testbaren Aktivitäten und bildet sie auf die Spielimplementierung ab.
 * Die Tests arbeiten gegen die Implementierung dieses Interface.
 */
public interface ITestAdapter {

	// ENUMS UND HILFSKLASSEN //////////////////////////////////////////////////////////////////////

	/**
	 * Die Himmelsrichtungen für das Laufen lassen.
	 */
	enum Direction {UP,DOWN,LEFT,RIGHT,BOMB,NULL};

	/**
	 * Die möglichen Elemente, die in FieldStatus zurück gegeben werden können.
	 */
	enum Element {PLAYER1, PLAYER2, PLAYER3, PLAYER4, FLOOR, BOMB, STONE, WALL, EXPLOSION, POWERUP};

	/**
	 * Parameter, die zurückgeliefert werden können.
	 * PLAYER_SPEED: wie viele Ticks benötigt ein Spieler, um ein Feld zurück zu legen? / Der Delay.
	 */
	enum Parameter {PLAYER_SPEED};

	/**
	 * Der Status eines einzelnen Spielfeldes, erheblich vereinfacht.
	 */
	public class FieldStatus {
		Element[] elements;

		public boolean contains (Element c) {
			for (int i = 0; i < elements.length; i++) {
				if (elements[i] == c)
					return true;
			}
			return false;
		}

		public void debugOut () {
			for (int i = 0; i < elements.length; i++) {
				System.out.println("... "+elements[i]);
			}
		}
	}

	// LEVEL LOADING ///////////////////////////////////////////////////////////////////////////////

	/**
	 * Lädt einen Level aus der angegebenen Datei.
	 * @param path
	 * @return Ob es geklappt hat.
	 */
	public boolean loadLevelFromFile (String path);

	/**
	 * Lädt einen Level aus dem übergebenen String, als wäre der String der Inhalt der Datei.
	 * Praktisch für Inline Levels.
	 * @param data Der Levelstring
	 * @return Ob es geklappt hat.
	 */
	public boolean loadLevelFromString (String data);

	// SIMULATING PLAYER CONTROLS //////////////////////////////////////////////////////////////////

	/**
	 * Äquivalent zum normalen Richtungs-Tastendruck. Gibt die Richtung an, in die sich der
	 * angegebene Spieler bewegen WILL in den kommenden Ticks. Wenn er damit aufhören soll, muss
	 * die Methode attemptStopPlayer aufgerufen werden.
	 * Die eigentliche Bewegung erfolgt erst zum nächsten Tick (vorher passiert nichts).
	 * @param playerIndex 1-4
	 * @param dir
	 */
	public void attemptMovePlayer (int playerIndex, Direction dir);

	/**
	 * Lässt den angegebenen Spieler stehen bleiben, falls er sich denn bewegte.
	 * @param playerIndex
	 */
	public void attemptStopPlayer (int playerIndex);

	/**
	 * Lässt den Spieler eine Bombe legen lassen WOLLEN. Äquivalent zum normalen Tastendruck.
	 * Die eigentliche Aktion erfolgt erst zum nächsten Tick (vorher passiert nichts).
	 * @param playerIndex
	 */
	public void attemptPlaceBomb (int playerIndex);

	// SIMULATING TIME /////////////////////////////////////////////////////////////////////////////

	/**
	 * Lässt die angegebene Anzahl Ticks vergehen.
	 * @param count
	 */
	public void tick (int count);

	/**
	 * Gibt Informationen über die Konfiguration zurück - bspw. wie schnell Spieler laufen können.
	 */
	public int getIntParameter (Parameter p);

	// FEEDBACK ////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Liefert ein zweidimensionales Array (erste Dimension die Breite des Spielfeldes, zweite die
	 * Höhe) von Feldinformationen. Der einzige Feedbackkanal für die Tests.
	 * @return
	 */
	public FieldStatus[][] getLevelStatus();
	
	/**
	 * Holt sich die Bombe auf der Spielfeldposition (b,c) zur�ck
	 */	
	public Bomb getBomb(int b, int c);
	
	/**
	 * Holt sich den Spieler auf der Spielfeldposition (b,c) zur�ck
	 */	
	public Player getPlayer(int b, int c);
	
	/**
	 * Gibt die aktuelle Karte alsStrig aus
	 */	
	public String maptoString();
	
	/**
	 * Gibt die abgelaufene Zeit zur�ck
	 */
	public long GetTime();

}
