package edu.hm.hafner.sokoban;

/**
 * A point represents a location in {@code (x,y)} coordinate space,
 * specified in integer precision. Instances of this class are immutable.
 *
 * @author Ullrich Hafner
 */
public class Point {
    private final int x;
    private final int y;
    private final String display;

    /**
     * Creates a new instance of {@code Point}.
     *
     * @param x x coordinate
     * @param y y coordinate
     */
    public Point(final int x, final int y) {
        this.x = x;
        this.y = y;
        display = String.format("(%d, %d)", x, y);
    }

    /**
     * Returns the x coordinate of this point.
     *
     * @return the x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y coordinate of this point.
     *
     * @return the y coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Checks if this point is equal to the specified other point.
     *
     * @param other the other point
     * @return {@code true} if this point is equal to the other point, {@code false} otherwise
     */
    public boolean isEqualTo(final Point other) {
        if (other == null) {
            return false;
        }
        return other.x == x && other.y == y;
    }

    /**
     * Returns the point left of this point.
     *
     * @return the point to the left
     */
    public Point moveLeft() {
        return new Point(x - 1, y);
    }

    /**
     * Returns the point right of this point.
     *
     * @return the point to the right
     */
    public Point moveRight() {
        return new Point(x + 1, y);
    }

    /**
     * Returns the point above this point.
     *
     * @return the point above
     */
    public Point moveUp() {
        return new Point(x, y - 1);
    }

    /**
     * Returns the point below of this point.
     *
     * @return the point below
     */
    public Point moveDown() {
        return new Point(x, y + 1);
    }

    @Override
    public String toString() {
        return display;
    }
}
