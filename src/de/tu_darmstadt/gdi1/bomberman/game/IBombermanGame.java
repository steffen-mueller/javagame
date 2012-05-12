package de.tu_darmstadt.gdi1.bomberman.game;

import de.tu_darmstadt.gdi1.bomberman.game.elements.GameElement;
import de.tu_darmstadt.gdi1.framework.interfaces.IBoard;

/**
 * Interface der BombermanGame Implementierung. Wird im Testadapter verwendet, damit andere
 * Implementierungen den gleichen Adapter verwenden können.
 */
public interface IBombermanGame {
	abstract public IBoard<GameElement> getBoard();

	abstract public void tick ();
}
