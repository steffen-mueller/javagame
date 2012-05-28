/*
 * Copyright (c) 2008-2010 by teambits GmbH, Darmstadt, Germany (http://www.teambits.de). All rights reserved.
 * This is CONFIDENTIAL code. Use is subject to license terms.
 */
package de.tu_darmstadt.gdi1.bomberman.game.elements;

import javax.swing.ImageIcon;

/**
 * javadoc: Not yet commented
 *
 * @version $Id$
 */
public class Wall extends GameElement
{
	@Override
	public GameElement clone ()
	{
		return new Wall();
	}

	@Override
	public boolean equals (Object obj)
	{
		// todo: Not yet implemented
		return false;
	}

	@Override
	public ImageIcon getImageIcon ()
	{
		return new ImageIcon("resource/images/wall.png");
	}

	@Override
	public String getDescription ()
	{
		return "Wall";
	}

	@Override
	public char getParsingSymbol ()
	{
		return '#';
	}
}
