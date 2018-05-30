package net.njfc.respawnredux.client.util;

public class Rectangle {

	private Position min;
	private Position max;

	public Rectangle(Position min, Position max) {
		this.min = min;
		this.max = max;
	}

	public Position getMin() {
		return min;
	}

	public void setMin(Position min) {
		this.min = min;
	}

	public Position getMax() {
		return max;
	}

	public void setMax(Position max) {
		this.max = max;
	}

	/**
	 * Returns the rectangle width on the X axis
	 *
	 * @return double
	 */
	public double getWidth() {
		return Math.abs(min.x - max.x);
	}

	/**
	 * Returns the rectangle height on the Y axis
	 *
	 * @return double
	 */
	public double getHeight() {
		return Math.abs(min.y - max.y);
	}

	/**
	 * Returns true if the given Position is located within the bounds
	 * of this cuboid.
	 *
	 * @param position Position
	 * @return boolean
	 */
	public boolean contains(Position position) {
		double x = position.x;
		double y = position.y;

		double x1 = Math.min(min.x, max.x);
		double y1 = Math.min(min.y, max.y);
		double x2 = Math.max(min.x, max.x);
		double y2 = Math.max(min.y, max.y);

		return x >= x1 && x <= x2 && y >= y1 && y <= y2;
	}

	public boolean intersects(Rectangle r) {
		return this.min.x < r.max.x && this.max.x > r.min.x && this.min.y < r.max.y && this.max.y > r.min.y;
	}

	/**
	 * Returns the center of this rectangle
	 *
	 * @return Position
	 */
	public Position getCenter() {
		return new Position(
			Math.abs(min.x - max.x) / 2,
			Math.abs(min.y - max.y) / 2
		);
	}

	@Override
	public String toString() {
		return "Rectangle{" +
			"min=" + min +
			", max=" + max +
			", width=" + getWidth() +
			", height=" + getHeight() +
			'}';
	}

}
