package de.tu_darmstadt.gdi1.bomberman.game.levels;

import de.tu_darmstadt.gdi1.bomberman.game.elements.GameElement;
import de.tu_darmstadt.gdi1.framework.utils.level.LevelManager;

/**
 * Der LevelManager weiß alles über Bombermanlevels und wie sie zusammenhängen.
 * @author Steffen Müller
 */
public class BombermanLevelManager extends LevelManager<GameElement>  {
	public BombermanLevelManager ()
	{
		super(new BombermanLevelInformationProvider());
	}
}
