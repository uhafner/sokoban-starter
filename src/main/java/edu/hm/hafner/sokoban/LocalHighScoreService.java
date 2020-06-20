package edu.hm.hafner.sokoban;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import edu.hm.hafner.sokoban.model.HighScoreEntry;
import edu.hm.hafner.sokoban.model.Orientation;

/**
 * Empty implementation just to make sure that the class compiles.
 *
 * @author Your Name
 */
public class LocalHighScoreService implements HighScoreService {
    @Override
    public void registerSolution(final String playerName, final String levelName, final int numberOfMoves,
            final int numberOfAttempts, final Collection<Orientation> solution) {
    }

    @Override
    public Collection<Orientation> getBestSolutionFor(final String levelName) {
        return Collections.emptyList();
    }

    @Override
    public List<HighScoreEntry> getBoard(final String levelName) {
        return Collections.emptyList();
    }

    @Override
    public void printBoard(final String levelName, final FormattedPrinter printer) {
    }

    @Override
    public void printScoresFor(final String player, final FormattedPrinter printer) {
    }

    @Override
    public void removeScoresFor(final String player) {
    }

    @Override
    public void clear() {
    }

    @Override
    public void setNumberOfEntriesPerLevel(final int numberOfEntries) {
    }

    @Override
    public void printBoards(final FormattedPrinter printer) {
    }
}
