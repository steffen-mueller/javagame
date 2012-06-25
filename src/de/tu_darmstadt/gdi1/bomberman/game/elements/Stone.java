package de.tu_darmstadt.gdi1.bomberman.game.elements;

import java.util.List;
import javax.swing.ImageIcon;

/**
 * javadoc: Not yet commented
 *
 * @version $Id$
 */
public class Stone extends GameElement
{
	@Override
	public GameElement clone ()
	{
		return new Stone();
	}

	@Override
	public ImageIcon getImageIcon ()
	{
		return new ImageIcon("resource/images/stone.png");
	}

	@Override
	public String getDescription ()
	{
		return "Stone";
	}

	@Override
	public char getParsingSymbol ()
	{
		return '*';
	}

	@Override
	public boolean isSolid() {
		return true;
	}

	@Override
	public boolean isDestroyable () {
		return true;
	}

	@Override
	public void destroy ()
	{
		super.destroy();
		List<GameElement> present = gameBoard.getElements(x, y);
		PowerUp powerUp = new PowerUp();
		present.add(powerUp);
		gameBoard.setElements(x, y, present);
		powerUp.setGameBoard(gameBoard);
	}
}
