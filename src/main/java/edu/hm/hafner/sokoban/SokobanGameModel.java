package edu.hm.hafner.sokoban;

/**
 * Represents the playable game field of Sokoban.
 *
 * @author Ullrich Hafner
 */
public class SokobanGameModel extends AbstractSokobanModel {
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
            setPlayer(point);
        }
    }

    @Override
    public void moveRight() {
        Point point = getPlayer().moveRight();
        if (point.getX() < getWidth()) {
            setPlayer(point);
        }
    }

    @Override
    public void moveUp() {
        // FIXME: implement correct movement
        Point point = getPlayer().moveUp();
        if (point.getY() >= 0) {
            setPlayer(point);
        }
    }

    @Override
    public void moveDown() {
        // FIXME: implement correct movement
        Point point = getPlayer().moveDown();
        if (point.getY() < getHeight()) {
            setPlayer(point);
        }
    }

    @Override
    public void reset() {
        // FIXME: reset model to initial setup
    }

    @Override
    protected void storeLevelState() {
        // FIXME: Store the initial level state
    }
}
