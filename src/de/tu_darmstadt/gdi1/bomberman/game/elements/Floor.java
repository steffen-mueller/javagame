package de.tu_darmstadt.gdi1.bomberman.game.elements;

import javax.swing.ImageIcon;

/**
 * javadoc: Not yet commented
 *
 * @version $Id$
 */
public class Floor extends GameElement
{
	@Override
	public GameElement clone ()
	{
		return new Floor();
	}

	@Override
	public ImageIcon getImageIcon ()
	{
		return new ImageIcon("resource/images/floor.png");
	}

	@Override
	public String getDescription ()
	{
		return "Floor";
	}

	@Override
	public char getParsingSymbol ()
	{
		return ' ';
	}
}
