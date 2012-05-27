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
public class Player extends GameElement
{
	private int playerID;
	
	public Player (int playerID)
	{
		this.playerID = playerID;
	}

	@Override
	public GameElement clone ()
	{
		return new Player(this.playerID);
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
		if (playerID == 1)
			return new ImageIcon("resource/images/player_white.png");
		else if (playerID == 2)
			return new ImageIcon("resource/images/player_red.png");
		else if (playerID == 3)
			return new ImageIcon("resource/images/player_black.png");
		else if (playerID == 4)
			return new ImageIcon("resource/images/player_blue.png");

		return new ImageIcon("resource/images/player_white.png");
	}

	@Override
	public String getDescription ()
	{
		return "Player";
	}
}
