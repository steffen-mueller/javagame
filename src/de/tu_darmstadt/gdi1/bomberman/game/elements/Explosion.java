package de.tu_darmstadt.gdi1.bomberman.game.elements;

import javax.swing.ImageIcon;

/**
 * javadoc: Not yet commented
 *
 * @version $Id$
 */
public class Explosion extends GameElement
{
	public enum style {
		CENTER,
		HORIZONTAL,
		VERTICAL
	};

	protected style myStyle;
	
	// The tick in which the explosion will disappear
	protected long	removalTick;

	public Explosion (style style, long removaltick) {
		myStyle = style;
		this.removalTick = removaltick;
	}

	@Override
	public GameElement clone ()
	{
		return new Explosion(myStyle, removalTick);
	}

	public boolean shouldBeRemoved (long tick) {
		return (removalTick <= tick);
	}

	@Override
	public void destroy () {
		Explosion.super.destroy();
		gameData.removeExplosion(this);
	}

	public boolean remove (long tick) {
		if (!shouldBeRemoved(tick))
			return false;

		destroy();
		return true;
	}

	@Override
	public ImageIcon getImageIcon () {
		switch (myStyle) {
			case CENTER: return new ImageIcon(skinPath+"explosion_center.png");
			case HORIZONTAL: return new ImageIcon(skinPath+"explosion_h.png");
			case VERTICAL: return new ImageIcon(skinPath+"explosion_v.png");
		}
		return null;
	}

	@Override
	public String getDescription ()
	{
		return "Explosion";
	}

	@Override
	public char getParsingSymbol ()
	{
		return ' ';
	}

	public boolean isSolid() {
		return false;
	}
}
