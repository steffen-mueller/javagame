package de.tu_darmstadt.gdi1.framework.utils;

import java.io.Serializable;

/**
 * A simple container for a 2 dimensional coordinate.
 * 
 * @author jonas
 * 
 */
public class Point implements Serializable {

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = -1802901175140170953L;

	/**
	 * The x-axis part of the coordinate.
	 */
	private int x;

	/**
	 * The y-axis part of the coordiante.
	 */
	private int y;

	/**
	 * Creates a new Point Object with the given coordinates.
	 * 
	 * @param newX
	 *            the x-axis part of the coordinate
	 * @param newY
	 *            the y-axis part of the coordinate
	 */
	public Point(final int newX, final int newY) {
		super();
		this.x = newX;
		this.y = newY;
	}

	/**
	 * Returns a string representation of the point object.
	 * @return 
	 * 		the string representation
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("Point[");
		sb.append(x);
		sb.append('|');
		sb.append(y);
		sb.append(']');
		return sb.toString();
	}

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(final Object o) {
		Point otherPoint = (Point) o;
		return (otherPoint.getX() == this.x && otherPoint.getY() == this.y);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		int hash = 7;
		hash = 31 * hash + this.x;
		hash = 31 * hash + this.y;
		return hash;
	}
	
	/**
	 * Creates a <i>new</i> point based on the given with modified x-axis.
	 *   
	 * @param current
	 * 		the base for the new point
	 * @param modifyValue
	 * 		the value for modifying the value of the x-axis
	 * @return
	 * 		a new point, based on the given only with modified x-axis
	 * 
	 *  @see #modifyYAxis(de.tu_darmstadt.gdi1.framework.utils.Point, int)
	 */
	public static Point modifyXAxis(final Point current, final int modifyValue) {
		return new Point(current.getX() + modifyValue, current.getY());
	}
	
	/**
	 * Creates a <i>new</i> point based on the given with modified y-axis.
	 *   
	 * @param current
	 * 		the base for the new point
	 * @param modifyValue
	 * 		the value for modifying the value of the y-axis
	 * @return
	 * 		a new point, based on the given only with modified y-axis
	 * 
	 *  @see #modifyXAxis(de.tu_darmstadt.gdi1.framework.utils.Point, int)
	 */
	public static Point modifyYAxis(final Point current, final int modifyValue) {
		return new Point(current.getX(), current.getY() + modifyValue);
	}


}
