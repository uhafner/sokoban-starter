package edu.hm.hafner.sokoban.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import edu.hm.hafner.sokoban.model.HighScoreEntry.HighScoreEntryBuilder;
import edu.hm.hafner.util.Generated;

/**
 * An entry of a high score table.
 *
 * @author Ullrich Hafner
 */
@SuppressWarnings({"PMD.MissingStaticMethodInNonInstantiatableClass", "PMD.DataClass"})
@JsonDeserialize(builder = HighScoreEntryBuilder.class)
public final class HighScoreEntry {
    private final String playerName;
    private final String levelName;
    private final int numberOfMoves;
    private final int numberOfAttempts;
    private final Instant timestamp;
    private final List<Orientation> solution;

    private HighScoreEntry(final String levelName, final String playerName, final int numberOfMoves,
            final int numberOfAttempts, final List<Orientation> solution, final Instant timestamp) {
        this.levelName = levelName;
        this.playerName = playerName;
        this.numberOfMoves = numberOfMoves;
        this.numberOfAttempts = numberOfAttempts;
        this.solution = solution;
        this.timestamp = timestamp;
    }

    public String getLevelName() {
        return levelName;
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

    public List<Orientation> getSolution() {
        return solution;
    }

    public String getTimestamp() {
        return LocalDateTime.ofInstant(timestamp, ZoneId.of("UTC")).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    @JsonIgnore
    public Instant getInstant() {
        return timestamp;
    }

    @Override @Generated
    public String toString() {
        return new ToStringBuilder(this)
                .append("levelName", levelName)
                .append("playerName", playerName)
                .append("numberOfMoves", numberOfMoves)
                .append("numberOfAttempts", numberOfAttempts)
                .append("timestamp", timestamp)
                .append("solution", solution)
                .toString();
    }

    @Override @Generated
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HighScoreEntry that = (HighScoreEntry) o;
        return numberOfMoves == that.numberOfMoves
                && numberOfAttempts == that.numberOfAttempts
                && levelName.equals(that.levelName)
                && playerName.equals(that.playerName)
                && timestamp.equals(that.timestamp)
                && solution.equals(that.solution);
    }

    @Override @Generated
    public int hashCode() {
        return Objects.hash(levelName, playerName, numberOfMoves, numberOfAttempts, timestamp, solution);
    }

    /**
     * Builds {@link HighScoreEntry} instances.
     */
    @SuppressWarnings({"checkstyle:HiddenField", "checkstyle:MissingJavadocMethod", "PMD.AccessorClassGeneration", "ParameterHidesMemberVariable"})
    public static class HighScoreEntryBuilder {
        private String levelName = StringUtils.EMPTY;
        private String playerName = StringUtils.EMPTY;
        private int numberOfMoves;
        private int numberOfAttempts;
        private Instant timestamp = Instant.now();
        private List<Orientation> solution = new ArrayList<>();

        public HighScoreEntryBuilder withLevelName(final String levelName) {
            this.levelName = levelName;
            return this;
        }

        public HighScoreEntryBuilder withPlayerName(final String playerName) {
            this.playerName = playerName;
            return this;
        }

        public HighScoreEntryBuilder withNumberOfMoves(final int numberOfMoves) {
            this.numberOfMoves = numberOfMoves;
            return this;
        }

        public HighScoreEntryBuilder withNumberOfAttempts(final int numberOfAttempts) {
            this.numberOfAttempts = numberOfAttempts;
            return this;
        }

        public HighScoreEntryBuilder withTimestamp(final Instant timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public HighScoreEntryBuilder withSolution(final List<Orientation> solution) {
            this.solution = solution;
            return this;
        }

        public HighScoreEntry build() {
            return new HighScoreEntry(levelName, playerName, numberOfMoves, numberOfAttempts, solution, timestamp);
        }
    }
}
