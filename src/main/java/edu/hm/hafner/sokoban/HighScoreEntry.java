package edu.hm.hafner.sokoban;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang3.StringUtils;

/**
 * An entry of a high score table.
 *
 * @author Ullrich Hafner
 */
@SuppressWarnings({"PMD.MissingStaticMethodInNonInstantiatableClass", "PMD.DataClass"})
public class HighScoreEntry {
    private final String playerName;
    private final int numberOfMoves;
    private final int numberOfAttempts;
    private final Instant timestamp;
    private final Collection<Orientation> solution;

    private HighScoreEntry(final String playerName, final int numberOfMoves, final int numberOfAttempts,
            final Collection<Orientation> solution) {
        this.playerName = playerName;
        this.numberOfMoves = numberOfMoves;
        this.numberOfAttempts = numberOfAttempts;
        this.solution = solution;
        this.timestamp = Instant.now();
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getNumberOfMoves() {
        return numberOfMoves;
    }

    public int getNumberOfAttempts() {
        return numberOfAttempts;
    }

    public Collection<Orientation> getSolution() {
        return solution;
    }

    public String getTimestamp() {
        return LocalDateTime.ofInstant(timestamp, ZoneId.of("UTC")).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    /**
     * Builds {@link HighScoreEntry} instances.
     */
    @SuppressWarnings({"checkstyle:HiddenField", "PMD.AccessorClassGeneration", "ParameterHidesMemberVariable"})
    public static class HighScoreEntryBuilder {
        private String playerName = StringUtils.EMPTY;
        private int numberOfMoves;
        private int numberOfAttempts;
        private Collection<Orientation> solution = new ArrayList<>();

        HighScoreEntryBuilder withPlayerName(final String playerName) {
            this.playerName = playerName;
            return this;
        }

        HighScoreEntryBuilder withNumberOfMoves(final int numberOfMoves) {
            this.numberOfMoves = numberOfMoves;
            return this;
        }

        HighScoreEntryBuilder withNumberOfAttempts(final int numberOfAttempts) {
            this.numberOfAttempts = numberOfAttempts;
            return this;
        }

        HighScoreEntryBuilder withSolution(final Collection<Orientation> solution) {
            this.solution = solution;
            return this;
        }

        HighScoreEntry build() {
            return new HighScoreEntry(playerName, numberOfMoves, numberOfAttempts, solution);
        }
    }
}
