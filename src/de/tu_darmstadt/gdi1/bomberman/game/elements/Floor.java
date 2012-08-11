package de.tu_darmstadt.gdi1.bomberman.game.elements;

import javax.swing.ImageIcon;

/**
 * javadoc: Not yet commented
 *
 * @version $Id$
 */
public class Floor extends GameElement
{
    public static final String DESCRITION = "Floor";
	@Override
	public GameElement clone ()
	{
		return new Floor();
	}

	@Override
	public ImageIcon getImageIcon ()
	{
		return new ImageIcon(skinPath+"floor.png");
	}

	@Override
	public String getDescription ()
	{
		return DESCRITION;
	}

	@Override
	public char getParsingSymbol ()
	{
		return ' ';
	}
}
