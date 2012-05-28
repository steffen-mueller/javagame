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
	private int x;
	private int y;

	public Player (int playerID)
	{
		this.playerID = playerID;
	}

	public int getPlayerID ()
	{
		return playerID;
	}

	@Override
	public GameElement clone ()
	{
		return new Player(this.playerID);
	}

	@Override
	public boolean equals (Object obj)
	{
		if (obj instanceof Player) {
			if (((Player) obj).playerID == this.playerID) {
				return true;
			}
		}
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
		else
			return new ImageIcon("resource/images/player_blue.png");
	}

	@Override
	public String getDescription ()
	{
		if (playerID == 1)
			return "Player White";
		else if (playerID == 2)
			return "Player Red";
		else if (playerID == 3)
			return "Player Black";
		else
			return "Player Blue";
	}

	@Override
	public char getParsingSymbol ()
	{
		if (playerID == 1)
			return '1';
		else if (playerID == 2)
			return '2';
		else if (playerID == 3)
			return '3';
		else
			return '4';
	}

	public void setCoordinates (int x, int y)
	{
		this.x = x;
		this.y = y;
	}
}
