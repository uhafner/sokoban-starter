package edu.hm.hafner.sokoban;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import static edu.hm.hafner.sokoban.Field.*;
import static org.assertj.core.api.Assertions.*;

/**
 * Tests the classes {@link AbstractSokobanModel} and {@link SokobanGameModel}.
 *
 * @author Ullrich Hafner
 */
@SuppressWarnings({"NullAway", "ConstantConditions", "PMD.NullAssignment"})
class SokobanTest {
    /** Verifies that null values are not stored. */
    @Test
    void shouldThrowNpeIfLevelIsNull() {
        assertThatThrownBy(() -> {
            // Given
            AbstractSokobanModel sokoban = createSokoban();

            // When
            sokoban.setLevel(null);
        }).isInstanceOf(NullPointerException.class); // Then
    }

    /** Verifies that null values are not stored. */
    @Test
    void shouldThrowNpeIfFieldIsNull() {
        assertThatThrownBy(() -> {
            // Given
            AbstractSokobanModel sokoban = createSokoban();
            Field[][] level = createLevelWithOneTreasure();
            level[0][0] = null;

            // When
            sokoban.setLevel(level);
        }).isInstanceOf(NullPointerException.class); // Then
    }

    /** Verifies that null values are not stored. */
    @Test
    void shouldThrowNpeIfPlayerIsNull() {
        assertThatThrownBy(() -> {
            // Given
            AbstractSokobanModel sokoban = createSokobanWithOneTreasure();

            // When
            sokoban.setPlayer(null);
        }).isInstanceOf(NullPointerException.class); // Then
    }

    /** Verifies that null values are not stored. */
    @Test
    void shouldThrowNpeIfTreasureIsNull() {
        assertThatThrownBy(() -> {
            // Given
            AbstractSokobanModel sokoban = createSokobanWithOneTreasure();

            // When
            sokoban.addTreasure(null);
        }).isInstanceOf(NullPointerException.class); // Then
    }

    /** Verifies that the player is not placed on a wall. */
    @Test
    void shouldDetectInvalidWorldWithPlayerOnWall() {
        assertThatThrownBy(() -> {
            // Given
            AbstractSokobanModel sokoban = createSokobanWithOneTreasure();
            sokoban.setPlayer(new Point(0, 0));
            sokoban.addTreasure(new Point(1, 2));

            // When
            sokoban.validate();
        }).isInstanceOf(IllegalArgumentException.class); // Then
    }

    /** Verifies that no treasure is placed on a wall. */
    @Test
    void shouldDetectInvalidWorldWithTreasureOnWall() {
        assertThatThrownBy(() -> {
            // Given
            AbstractSokobanModel sokoban = createSokobanWithOneTreasure();
            sokoban.setPlayer(new Point(1, 1));
            sokoban.addTreasure(new Point(0, 0));

            // When
            sokoban.validate();
        }).isInstanceOf(IllegalArgumentException.class); // Then
    }

    /** Verifies that the player is not placed on a treasure. */
    @Test
    void shouldDetectInvalidWorldWithPlayerOnTreasure() {
        assertThatThrownBy(() -> {
            // Given
            AbstractSokobanModel sokoban = createSokobanWithOneTreasure();
            sokoban.setPlayer(new Point(1, 1));
            sokoban.addTreasure(new Point(1, 1));

            // When
            sokoban.validate();
        }).isInstanceOf(IllegalArgumentException.class); // Then
    }

    /** Verifies that the number of targets is equal to the number of treasures. */
    @Test
    void shouldDetectInvalidWorldThatHasNotEnoughTargets() {
        assertThatThrownBy(() -> {
            // Given
            AbstractSokobanModel sokoban = createSokobanWithOneTreasure();
            sokoban.setPlayer(new Point(2, 3));
            sokoban.addTreasure(new Point(3, 3));
            sokoban.addTreasure(new Point(3, 4));

            // When
            sokoban.validate();
        }).isInstanceOf(IllegalArgumentException.class); // Then
    }

    /** Verifies that the number of targets is equal to the number of treasures. */
    @Test
    void shouldDetectInvalidWorldThatHasNotEnoughTreasures() {
        assertThatThrownBy(() -> {
            // Given
            AbstractSokobanModel sokoban = createSokobanWithOneTreasure();
            sokoban.setPlayer(new Point(2, 3));

            // When
            sokoban.validate();
        }).isInstanceOf(IllegalArgumentException.class); // Then
    }

    /** Verifies that the player has been set. */
    @Test
    void shouldDetectInvalidWorldThatHasNoPlayer() {
        assertThatThrownBy(() -> {
            // Given
            AbstractSokobanModel sokoban = createSokobanWithOneTreasure();
            sokoban.addTreasure(new Point(3, 4));

            // When
            sokoban.validate();
        }).isInstanceOf(IllegalArgumentException.class); // Then
    }

    /** Verifies that the field array is regular, i.e. the size of each line is the width. */
    @Test
    void shouldDetectIrregularArray() {
        assertThatThrownBy(() -> {
            // Given
            AbstractSokobanModel sokoban = createSokobanWithOneTreasure();
            sokoban.setLevel(createIrregularArray());
        }).isInstanceOf(IllegalArgumentException.class); // Then
    }

    /** Verifies that duplicate treasures are skipped. */
    @Test
    void shouldSkipDuplicateTreasure() {
        // Given
        AbstractSokobanModel sokoban = createSokobanWithOneTreasure();
        sokoban.setPlayer(new Point(1, 1));
        sokoban.addTreasure(new Point(1, 2));

        // When Then
        assertThatIllegalStateException().isThrownBy(() -> sokoban.addTreasure(new Point(1, 2)));
    }

    /** Verifies that setting a player outside of the visible level is correctly detected. */
    @Test
    void shouldDetectPlayerOutsideOfLevel() {
        shouldDetectPlayersOutsideOfField(
                new Point(-1, 0), new Point(0, -1), // Links oben
                new Point(2, 0), new Point(1, -1), // Rechts oben
                new Point(2, 1), new Point(1, 2),  // Rechts unten
                new Point(-1, 1), new Point(0, 2)   // Links unten
        );
    }

    private void shouldDetectPlayersOutsideOfField(final Point... outsidePlayers) {
        for (Point outsidePoint : outsidePlayers) {
            assertThatThrownBy(() -> {
                // Given
                AbstractSokobanModel sokoban = createSokobanWithOneTreasure();
                sokoban.setLevel(new Field[][] {
                        {WALL, WALL},
                        {WALL, TARGET},
                });
                sokoban.setPlayer(outsidePoint);
                // When
                sokoban.validate();
            }).isInstanceOf(IllegalArgumentException.class); // Then
        }
    }

    /** Verifies that setting a treasure outside of the visible level is correctly detected. */
    @Test
    void shouldDetectTreasureOutsideOfLevel() {
        shouldDetectTreasuresOutsideOfField(
                new Point(-1, 0), new Point(0, -1), // Links oben
                new Point(2, 0), new Point(1, -1), // Rechts oben
                new Point(2, 1), new Point(1, 2),  // Rechts unten
                new Point(-1, 1), new Point(0, 2)   // Links unten
        );
    }

    private void shouldDetectTreasuresOutsideOfField(final Point... outsidePlayers) {
        for (Point outsidePoint : outsidePlayers) {
            assertThatThrownBy(() -> {
                // Given
                AbstractSokobanModel sokoban = createSokobanWithOneTreasure();
                sokoban.setLevel(new Field[][] {
                        {WALL, WALL},
                        {WALL, TARGET},
                });
                sokoban.addTreasure(outsidePoint);
                // When
                sokoban.validate();
            }).isInstanceOf(IllegalArgumentException.class); // Then
        }
    }

    /** Verifies that a valid world with one treasure is correctly detected. */
    @Test
    void shouldValidateCorrectWorldWithOneTreasure() {
        // Given
        AbstractSokobanModel sokoban = createSokobanWithOneTreasure();
        sokoban.setPlayer(new Point(2, 3));
        sokoban.addTreasure(new Point(3, 4));

        // When
        sokoban.validate();
        boolean isSolved = sokoban.isSolved();

        // Then
        assertThat(isSolved).isFalse();
    }

    /** Verifies that a valid world with two treasures is correctly detected. */
    @Test
    void shouldValidateCorrectWorldWithTwoTreasures() {
        // Given
        AbstractSokobanModel sokoban = createSokobanWithTwoTreasures();
        sokoban.setPlayer(new Point(1, 1));
        sokoban.addTreasure(new Point(1, 2));
        sokoban.addTreasure(new Point(1, 3));

        // When
        sokoban.validate();
        boolean isSolved = sokoban.isSolved();

        // Then
        assertThat(isSolved).isFalse();
    }

    /** Verifies that a level has been solved. */
    @Test
    void shouldDetectSolvedWorldWithOneTreasure() {
        // Given
        AbstractSokobanModel sokoban = createSokobanWithOneTreasure();
        sokoban.setPlayer(new Point(2, 3));
        sokoban.addTreasure(new Point(2, 1));

        // When
        sokoban.validate();
        boolean isSolved = sokoban.isSolved();

        // Then
        assertThat(isSolved).isTrue();

        // Then
        assertThat(sokoban.isSolved()).isTrue();
    }

    /** Verifies that a level has been solved. */
    @Test
    void shouldDetectSolvedWorldWithTwoTreasures() {
        // Given
        AbstractSokobanModel sokoban = createSokobanWithTwoTreasures();
        sokoban.setPlayer(new Point(1, 1));
        sokoban.addTreasure(new Point(2, 1));
        sokoban.addTreasure(new Point(2, 2));

        // When
        sokoban.validate();
        boolean isSolved = sokoban.isSolved();

        // Then
        assertThat(isSolved).isTrue();
    }

    /** Verifies that a level will be copied internally. */
    @Test
    void shouldCopyLevel() {
        // Given
        Field[][] level = createLevelWithOneTreasure();
        AbstractSokobanModel sokoban = createSokoban();
        sokoban.setLevel(level);
        sokoban.setPlayer(new Point(2, 3));
        sokoban.addTreasure(new Point(2, 1));

        // When
        fillArrayWithTarget(level);

        // When
        sokoban.validate();
        // Then
        assertThat(sokoban.isSolved()).isTrue();
    }

    protected AbstractSokobanModel createSokobanWithOneTreasure() {
        AbstractSokobanModel sokoban = createSokoban();
        sokoban.setLevel(createLevelWithOneTreasure());
        return sokoban;

    }

    protected AbstractSokobanModel createSokoban() {
        return new SokobanGameModel("Test");
    }

    protected AbstractSokobanModel createSokobanWithTwoTreasures() {
        AbstractSokobanModel sokoban = createSokoban();
        sokoban.setLevel(createLevelWithTwoTreasures());
        return sokoban;
    }

    @SuppressWarnings("PMD.UseVarargs")
    private void fillArrayWithTarget(final Field[][] level) {
        for (Field[] fields : level) {
            Arrays.fill(fields, TARGET);
        }
    }

    private Field[][] createIrregularArray() {
        return new Field[][] {
                {WALL, WALL, WALL, WALL},
                {WALL, FLOOR, TARGET, WALL},
                {WALL, FLOOR, FLOOR, WALL, WALL, WALL},
                {WALL, FLOOR, FLOOR, FLOOR, FLOOR, WALL},
                {WALL, FLOOR, FLOOR, FLOOR, FLOOR, WALL},
                {WALL, FLOOR, FLOOR, WALL},
                {WALL, WALL, WALL, WALL},
        };
    }

    private Field[][] createLevelWithOneTreasure() {
        return new Field[][] {
                {WALL, WALL, WALL, WALL, BACKGROUND, BACKGROUND},
                {WALL, FLOOR, TARGET, WALL, BACKGROUND, BACKGROUND},
                {WALL, FLOOR, FLOOR, WALL, WALL, WALL},
                {WALL, FLOOR, FLOOR, FLOOR, FLOOR, WALL},
                {WALL, FLOOR, FLOOR, FLOOR, FLOOR, WALL},
                {WALL, FLOOR, FLOOR, WALL, BACKGROUND, BACKGROUND},
                {WALL, WALL, WALL, WALL, BACKGROUND, BACKGROUND},
        };
    }

    private Field[][] createLevelWithTwoTreasures() {
        return new Field[][] {
                {WALL, WALL, WALL, WALL},
                {WALL, FLOOR, TARGET, WALL},
                {WALL, FLOOR, TARGET, WALL},
                {WALL, FLOOR, FLOOR, WALL},
                {WALL, WALL, WALL, WALL},
        };
    }
}
