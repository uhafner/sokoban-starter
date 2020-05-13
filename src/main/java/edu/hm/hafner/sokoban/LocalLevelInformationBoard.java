package edu.hm.hafner.sokoban;

import java.util.Collection;
import java.util.Collections;

import org.apache.commons.lang3.StringUtils;

/**
 * Empty implementation just to make sure that the class compiles.
 *
 * @author Your Name
 */
public class LocalLevelInformationBoard implements LevelInformationBoard {
    /**
     * Creates a new information board for the specified level.
     *
     * @param level
     *         the level
     */
    public LocalLevelInformationBoard(final String level) {
    }

    @Override
    public String getName() {
        return StringUtils.EMPTY;
    }

    @Override
    public boolean hasSuccessfulAttempt() {
        return false;
    }

    @Override
    public int getHighScore() {
        return 0;
    }

    @Override
    public Collection<Orientation> printHighScoreSolution() {
        return Collections.emptyList();
    }

    @Override
    public String toString() {
        return StringUtils.EMPTY;
    }

    @Override
    public void startNewAttempt() {
    }

    @Override
    public int getAttempts() {
        return 0;
    }

    @Override
    public void recordMove(final Orientation orientation) {
    }

    @Override
    public AttemptResult finishLevel() {
        return AttemptResult.NEW_HIGH_SCORE;
    }

    @Override
    public int getMoves() {
        return 0;
    }

    @Override
    public Collection<Orientation> printMoves() {
        return Collections.emptyList();
    }
}
