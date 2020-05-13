package edu.hm.hafner.sokoban;

import java.util.Collection;
import java.util.Formatter;
import java.util.List;

import com.google.errorprone.annotations.FormatMethod;

/**
 * Manages the high scores of Sokoban levels.
 *
 * @author Ullrich Hafner
 */
public interface HighScoreService {
    /**
     * Registers a new solution for the specified level.
     *
     * @param playerName
     *         the name of the player
     * @param levelName
     *         the name of the Level
     * @param numberOfMoves
     *         the number of moves of the solution
     * @param numberOfAttempts
     *         the number of attempts so far
     * @param solution
     *         the solution
     */
    void registerSolution(String playerName, String levelName, int numberOfMoves,
            int numberOfAttempts, Collection<Orientation> solution);

    /**
     * Clears the all scores.
     */
    void clear();

    /**
     * Sets the maximum number of persisted {@link HighScoreEntry} instances that are stored per level. Otherwise all
     * solutions will be stored.
     *
     * @param numberOfEntries
     *         the maximum number of entries
     */
    void setNumberOfEntriesPerLevel(int numberOfEntries);

    /**
     * Removes all scores for the specified player. All level files will be removed.
     *
     * @param player
     *         the player to remove the scores for
     */
    void removeScoresFor(String player);

    /**
     * Returns the best solution for the specified level.
     *
     * @param levelName
     *         the level to get the solution for
     *
     * @return the recorded moves of the best solution for the specified level
     */
    Collection<Orientation> getBestSolutionFor(String levelName);

    /**
     * Returns the high score board for the specified level. The board is sorted by the following properties:
     * <ol>
     *     <li>Number of moves (ascending)</li>
     *     <li>Number of attempts (ascending)</li>
     *     <li>Name of player (ascending)</li>
     * </ol>
     * The board will show only the configured number of entries, see {@link #setNumberOfEntriesPerLevel(int)}.
     *
     * @param levelName
     *         the level to print the board for
     *
     * @return the sorted entries
     * @see #setNumberOfEntriesPerLevel(int)
     */
    List<HighScoreEntry> getBoard(String levelName);

    /**
     * Prints the high score board for the specified level.
     *
     * @param levelName
     *         the level to print the board for
     * @param printer
     *         the printer to use
     */
    void printBoard(String levelName, FormattedPrinter printer);

    /**
     * Prints all high score boards for all levels.
     *
     * @param printer
     *         the printer to use
     */
    void printBoards(FormattedPrinter printer);

    /**
     * Prints all scores for the specified player.
     *
     * @param player
     *         the player to print the scores for
     * @param printer
     *         the printer to use
     */
    void printScoresFor(String player, FormattedPrinter printer);

    /**
     * Prints a line of the board.
     */
    interface FormattedPrinter {
        /**
         * Prints the specified message. Use this method to print a formatted string.
         *
         * @param format
         *         A format string, see {@link Formatter} for details
         * @param args
         *         Arguments referenced by the format specifiers in the format string.  If there are more arguments than
         *         format specifiers, the extra arguments are ignored. The number of arguments is variable and may be
         *         zero.
         */
        @FormatMethod
        void print(String format, Object... args);
    }

    /**
     * A simple implementation of {@link FormattedPrinter} the prints the messages to {@code System.out}.
     */
    class SystemOutPrinter implements FormattedPrinter {
        @Override
        public void print(final String format, final Object... args) {
            System.out.format(format, args);
            System.out.println();
        }
    }
}
