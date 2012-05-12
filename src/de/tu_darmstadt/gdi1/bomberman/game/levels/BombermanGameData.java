package de.tu_darmstadt.gdi1.bomberman.game.levels;

import de.tu_darmstadt.gdi1.bomberman.game.elements.GameElement;
import de.tu_darmstadt.gdi1.framework.interfaces.IStepManager;
import de.tu_darmstadt.gdi1.framework.model.GameData;

/**
 * Der gesamte Zustand eines Bomberman Spiels.
 */
public class BombermanGameData extends GameData<GameElement> {

	public BombermanGameData () {
		// Do nothing here at the moment. Necessary nevertheless, otherwise we would not get
		// rid of the step manager.
		super(20,15);
	}

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
