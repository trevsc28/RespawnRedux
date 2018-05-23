package net.njfc.respawnredux.client.util;

public class Collision {

	/**
	 * Returns true when the specified rectangle contains the specified position
	 *
	 * @param rect Rectangle
	 * @param pos Position
	 * @return boolean
	 */
	public static boolean squareCheck(Rectangle rect, Position pos) {
		return rect.contains(pos);
	}

	/**
	 * Returns the distance between the given positions
	 *
	 * @param origin Origin
	 * @param target Target
	 * @return Distance
	 */
	public static double distanceCheck(Position origin, Position target) {
		return Math.sqrt(Math.pow((origin.x - target.x), 2) + Math.pow((origin.y - target.y), 2));
	}

}
