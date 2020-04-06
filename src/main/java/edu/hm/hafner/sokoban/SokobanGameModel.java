package edu.hm.hafner.sokoban;

/**
 * Represents the playable game field of Sokoban.
 *
 * @author Ullrich Hafner
 */
public class SokobanGameModel extends AbstractSokobanModel {
    private int moves;

    /**
     * Creates a new model with the specified name.
     *
     * @param name
     *         name of the model
     */
    public SokobanGameModel(final String name) {
        super(name);
    }

    /**
     * Moves the player to the left. If this is not possible, then nothing is done.
     */
    @Override
    public void moveLeft() {
        // FIXME: implement correct movement
        Point point = getPlayer().moveLeft();
        if (point.getX() >= 0) {
            moves++;
            setPlayer(point);
        }
    }

    /**
     * Moves the player to the right. If this is not possible, then nothing is done.
     */
    @Override
    public void moveRight() {
        // FIXME: implement correct movement
        Point point = getPlayer().moveRight();
        if (point.getX() < getWidth()) {
            moves++;
            setPlayer(point);
        }
    }

    /**
     * Moves the player up. If this is not possible, then nothing is done.
     */
    @Override
    public void moveUp() {
        // FIXME: implement correct movement
        Point point = getPlayer().moveUp();
        if (point.getY() >= 0) {
            moves++;
            setPlayer(point);
        }
    }

    /**
     * Moves the player down. If this is not possible, then nothing is done.
     */
    @Override
    public void moveDown() {
        // FIXME: implement correct movement
        Point point = getPlayer().moveDown();
        if (point.getY() < getHeight()) {
            moves++;
            setPlayer(point);
        }
    }

    @Override
    public String toString() {
        if (isSolved()) {
            return "Solved in " + moves + " moves!";
        }
        else {
            return moves + " moves...";
        }
    }
}
