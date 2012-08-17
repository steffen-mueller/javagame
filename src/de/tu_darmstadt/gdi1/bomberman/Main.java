package de.tu_darmstadt.gdi1.bomberman;

import de.tu_darmstadt.gdi1.bomberman.BombermanController;
import de.tu_darmstadt.gdi1.bomberman.game.levels.BombermanLevelGenerator;

/**
 * Der Kickstarter des Spiels. Das hier ist die zu startende Klasse.
 */
public class Main {
	public static void main(String[] args) {
		new BombermanController();
	}
}
