package de.tu_darmstadt.gdi1.bomberman.game.levels;

import de.tu_darmstadt.gdi1.bomberman.game.elements.GameElement;
import de.tu_darmstadt.gdi1.framework.interfaces.IStepManager;
import de.tu_darmstadt.gdi1.framework.model.GameData;

/**
 * Der gesamte Zustand eines Bomberman Spiels. Hierauf arbeitet die BombermanGame Klasse.
 */
public class BombermanGameData extends GameData<GameElement> {

	public BombermanGameData () {
		// Wir werden den Stepmanager nicht los - also initialisieren.
		super(20,15);
	}

	// Gamedata Interface //////////////////////////////////////////////////////////////////////////

	@Override
	public boolean isLost() {
		return false;
	}

	@Override
	public boolean isPaused() {
		return false;
	}

	@Override
	public boolean isRunning() {
		return true;
	}

	@Override
	public boolean isWon() {
		return false;
	}

}