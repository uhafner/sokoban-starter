package edu.hm.hafner.sokoban;

import java.util.Collection;

/**
 * Controls the life cycle of a Sokoban level. Stores the moves for each attempt, records high scores, and so on.
 *
 * @author Ullrich Hafner
 */
public interface LevelInformationBoard {
    /**
     * Returns the name of the level. This name is used as ID to identify the high scores.
     *
     * @return the name
     */
    String getName();

    /**
     * Starts a new attempt to solve the specified level. If the level has been solved, the {@link #finishLevel()}
     * method needs to be invoked to record the total number of moves.
     *
     * @see #finishLevel()
     */
    void startNewAttempt();

    /**
     * Marks the current attempt as a solution of this level. Records the number of moves and evaluates whether the
     * number of moves are a new high score or not.
     *
     * @return the quality of the solution
     * @see #finishLevel()
     */
    AttemptResult finishLevel();

    /**
     * Returns whether at least one successful attempt has been recorded.
     *
     * @return {@code true} if at least one successful attempt has been recorded, {@code false} if the level has not
     *         been solved yet
     */
    boolean hasSuccessfulAttempt();

    /**
     * Returns the number of attempts that the current user played already. This number is the total of all successful
     * and unsuccessful attempts.
     *
     * @return the number of attempts
     */
    int getAttempts();

    /**
     * Returns the minimum number of moves for a successful solution of this level. This number may change if a better
     * solution will be added.
     *
     * @return the minimum number of moves so far
     * @see #finishLevel()
     */
    int getHighScore();

    /**
     * Returns the best solution so far. If there is no such solution registered yet, an {@link IllegalStateException}
     * will be thrown.
     *
     * @return the recorded moves of the best solution
     */
    Collection<Orientation> printHighScoreSolution();

    /**
     * Returns the current number of moves of the current attempt. The number of moves should be increased for each
     * movement operation, see {@link #recordMove(Orientation)}.
     *
     * @return the current number moves
     * @see #recordMove(Orientation)
     */
    int getMoves();

    /**
     * Records a player move of the specified orientation.
     *
     * @param orientation
     *         the orientation of the player
     */
    void recordMove(Orientation orientation);

    /**
     * Returns the recorded moves of the current attempt. These moved are not necessarily a solution yet.
     *
     * @return the recorded moves so far
     */
    Collection<Orientation> printMoves();

    /**
     * The result of a attempt.
     */
    enum AttemptResult {
        /** A new high score. */
        NEW_HIGH_SCORE,
        /** The same number of moves as the high score. */
        HIGH_SCORE_SET,
        /** A solution with too many moves. */
        NO_HIGH_SCORE
    }
}
