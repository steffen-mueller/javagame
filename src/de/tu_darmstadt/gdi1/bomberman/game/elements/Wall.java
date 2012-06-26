package de.tu_darmstadt.gdi1.bomberman.game.elements;

import javax.swing.ImageIcon;

/**
 * javadoc: Not yet commented
 *
 * @version $Id$
 */
public class Wall extends GameElement
{
    public static final String DESCRITION = "Wall";
	@Override
	public GameElement clone ()
	{
		return new Wall();
	}

	@Override
	public ImageIcon getImageIcon ()
	{
		return new ImageIcon("resource/images/wall.png");
	}

	@Override
	public String getDescription ()
	{
		return DESCRITION;
	}

	@Override
	public char getParsingSymbol ()
	{
		return '#';
	}

	public boolean isSolid() {
		return true;
	}
}
