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
public class Bomb extends GameElement
{
	private int ticksTillExplode;
	private int detonationRadius = 1;

	public Bomb (int ticksTillExplode)
	{
		this.ticksTillExplode = ticksTillExplode;
	}

	public int getTicksTillExplode ()
	{
		return ticksTillExplode;
	}

	public void setTicksTillExplode (int ticksTillExplode)
	{
		this.ticksTillExplode = ticksTillExplode;
	}

	public int getDetonationRadius ()
	{
		return detonationRadius;
	}

	public void setDetonationRadius (int detonationRadius)
	{
		this.detonationRadius = detonationRadius;
	}

	@Override
	public GameElement clone ()
	{
		return new Bomb(this.ticksTillExplode);
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
		if (ticksTillExplode < 40)
			return new ImageIcon("resource/images/bomb_attention.png");
		else
			return new ImageIcon("resource/images/bomb.png");
	}

	@Override
	public String getDescription ()
	{
		return "Bomb";
	}

	@Override
	public char getParsingSymbol ()
	{
		return ' ';
	}

	public void decreaseBombTicks()
	{
		setTicksTillExplode(getTicksTillExplode()-1);
	}

	
	public boolean isSolid() {
		return true;
	}

}
