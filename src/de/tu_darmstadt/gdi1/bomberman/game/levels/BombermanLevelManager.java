package de.tu_darmstadt.gdi1.bomberman.game.levels;

import de.tu_darmstadt.gdi1.bomberman.game.elements.GameElement;
import de.tu_darmstadt.gdi1.framework.utils.level.LevelManager;

/**
 * Der LevelManager verwaltet ... Levels. Uha. Aktuell wird hier vor allem der
 * BombermanLevelInformationProvider an das Framework geklebt.
 */
public class BombermanLevelManager extends LevelManager<GameElement>  {
	public BombermanLevelManager ()
	{
		super(new BombermanLevelInformationProvider());
	}
}
