package de.tu_darmstadt.gdi1.bomberman.game.elements;

import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;

import de.tu_darmstadt.gdi1.framework.utils.Point;

/**
 * javadoc: Not yet commented
 *
 * @version $Id$
 */
public class Bomb extends GameElement
{
	private int ticksTillExplode;
	private int detonationRadius;
	private int explosionLastingTicks = 5;
	private int explosionLastingDamping = 1;

	protected Player player;

	public Bomb (Player player, int ticksTillExplode)
	{
		this.ticksTillExplode = ticksTillExplode;
		this.player = player;
		this.detonationRadius = player.getBombradius();
	}
	
	//public void setdetonationRadius(int newRadius){
	//	detonationRadius = newRadius;
	//}

	public int getTicksTillExplode ()
	{
		return ticksTillExplode;
	}

	public void setTicksTillExplode (int ticksTillExplode)
	{
		this.ticksTillExplode = ticksTillExplode;
	}

	//public int getDetonationRadius ()
	//{
	//	return detonationRadius;
	//}


	@Override
	public GameElement clone ()
	{
		return new Bomb(player, this.ticksTillExplode);
	}

	@Override
	public ImageIcon getImageIcon ()
	{
		if (ticksTillExplode < 20)
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

	public boolean isDirty () {
		return (ticksTillExplode == 19);
	}

	@Override
	public void destroy () {
		Bomb.super.destroy();
		gameData.removeBomb(this);
		player.removeBombOwnage(this);
	}

	// Explosion Management ////////////////////////////////////////////////////////////////////////

	/**
	 * Lässt diese Bombe sofort explodieren. Gibt eine Liste der Explosionsfelder auf der Karte zurück.
	 * @return
	 */
	public ArrayList<Explosion> explode (long tickCount) {
		ArrayList<Explosion> list = new ArrayList<Explosion>();

		// Create center of the explosion
		List<GameElement> present = gameBoard.getElements(x, y);
		Explosion center = new Explosion(Explosion.style.CENTER, tickCount + explosionLastingTicks + detonationRadius*explosionLastingDamping);
		center.setCoordinates(this.x, this.y);
		center.setGameBoard(gameBoard);
		center.setGameData(gameData);
		list.add(center);
		present.add(center);

		// Replace the elements of the center of the explosion
		gameBoard.setElements(x, y, present);

		// Remove myself
		destroy();

		// Spread the explosion in all 4 directions + center.
		list.addAll(propagateExplosion(new Point(x,y), new Point(0,0), Explosion.style.CENTER, 1, tickCount));
		list.addAll(propagateExplosion(new Point(x,y), new Point(-1,0), Explosion.style.HORIZONTAL, detonationRadius, tickCount));
		list.addAll(propagateExplosion(new Point(x,y), new Point(1,0), Explosion.style.HORIZONTAL, detonationRadius, tickCount));
		list.addAll(propagateExplosion(new Point(x,y), new Point(0,-1), Explosion.style.VERTICAL, detonationRadius, tickCount));
		list.addAll(propagateExplosion(new Point(x,y), new Point(0,1), Explosion.style.VERTICAL, detonationRadius, tickCount));

		return list;
	}

	protected ArrayList<Explosion> propagateExplosion (Point pos, Point dir, Explosion.style style, int range, long tickCount) {

		// We may return explosions from here
		ArrayList<Explosion> list = new ArrayList<Explosion>();

		// Get the new coordinate
		Point newpos = new Point(pos.getX()+dir.getX(), pos.getY()+dir.getY());

		// If we fell off the board, stop
		if (!gameBoard.checkCoordinates(newpos))
			return list;

		// Check the elements on the new coordinate
		boolean isStopped = false;
		ArrayList<GameElement> toRemove = new ArrayList<GameElement>();
		ArrayList<Bomb> toExplode = new ArrayList<Bomb>();
		List<GameElement> present = gameBoard.getElements(newpos.getX(), newpos.getY());
		for (GameElement e : present) {
			// Another bomb - also explode
			if (e instanceof Bomb) {
				toExplode.add((Bomb)e);
			}
			// Something solid - definitely stop propagation
			else if (e.isSolid()) {
				isStopped = true;
			}

			if (e.isDestroyable()) {
				toRemove.add(e);
			}
		}

		for (Bomb b: toExplode) {
			list.addAll(b.explode(tickCount));
		}

		// If we hit something solid and nothing could be destroyed, cancel here.
		if (isStopped && toRemove.isEmpty()) {
			return list;
		}

		// Create a new explosion
		Explosion ex = new Explosion(style, tickCount + explosionLastingTicks + range * explosionLastingDamping);
		ex.setCoordinates(newpos);
		ex.setGameBoard(gameBoard);
		ex.setGameData(gameData);
		list.add(ex);
		present.add(ex);
		gameBoard.setElements(newpos.getX(), newpos.getY(), present);

		// Remove things we bombed away
		for (GameElement e : toRemove) {
			e.destroy();
		}

		// Go on spreading if we still have range left and were not stopped
		if (!isStopped && range > 0)
			list.addAll(propagateExplosion(newpos, dir, style, range-1, tickCount));

		return list;
	}
}
