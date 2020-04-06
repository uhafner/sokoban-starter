package edu.hm.hafner.sokoban;

import org.junit.jupiter.api.Test;

import static edu.hm.hafner.sokoban.Field.*;
import static org.assertj.core.api.Assertions.*;

/**
 * Tests the class {@link SokobanGameModel}.
 *
 * @author Ullrich Hafner
 */
public class MovingSokobanTest extends SokobanTest {
    /** Verifies that a treasure can be moved. */
    @Test
    public void shouldMoveTreasureToTarget() {
        AbstractSokobanModel sokoban = createLevel();
        assertThatPlayerIsAt(sokoban, new Point(3, 4));
        assertThatTreasuresAreAt(sokoban, new Point(2, 4), new Point(4, 5));

        sokoban.moveLeft();
        assertThatPlayerIsAt(sokoban, new Point(3, 4));

        sokoban.moveDown();
        assertThatPlayerIsAt(sokoban, new Point(3, 5));

        sokoban.moveLeft();
        assertThatPlayerIsAt(sokoban, new Point(2, 5));

        sokoban.moveUp();
        assertThatPlayerIsAt(sokoban, new Point(2, 4));
        assertThatTreasuresAreAt(sokoban, new Point(2, 3), new Point(4, 5));

        sokoban.moveRight();
        sokoban.moveRight();
        sokoban.moveRight();
        sokoban.moveDown();
        sokoban.moveLeft();
        assertThatPlayerIsAt(sokoban, new Point(4, 5));
        assertThatTreasuresAreAt(sokoban, new Point(2, 3), new Point(3, 5));
    }

    private void assertThatPlayerIsAt(final AbstractSokobanModel sokoban, final Point player) {
        assertThat(sokoban.getPlayer().isEqualTo(player)).as("Player at wrong position").isTrue();
    }

    private void assertThatTreasuresAreAt(final AbstractSokobanModel sokoban, final Point... treasures) {
        assertThat(sokoban.getTreasures().size()).as("Wrong number of treasures").isEqualTo(treasures.length);
        for (Point treasure : treasures) {
            assertThat(sokoban.getTreasures().contains(treasure)).isTrue();
        }
    }

    private static AbstractSokobanModel createLevel() {
        Field[][] fields = {
                {BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND},
                {BACKGROUND, WALL, WALL, WALL, WALL, BACKGROUND, BACKGROUND, BACKGROUND},
                {BACKGROUND, WALL, FLOOR, TARGET, WALL, BACKGROUND, BACKGROUND, BACKGROUND},
                {BACKGROUND, WALL, FLOOR, FLOOR, WALL, WALL, WALL, BACKGROUND},
                {BACKGROUND, WALL, TARGET, FLOOR, FLOOR, FLOOR, WALL, BACKGROUND},
                {BACKGROUND, WALL, FLOOR, FLOOR, FLOOR, FLOOR, WALL, BACKGROUND},
                {BACKGROUND, WALL, FLOOR, FLOOR, WALL, WALL, WALL, BACKGROUND},
                {BACKGROUND, WALL, WALL, WALL, WALL, BACKGROUND, BACKGROUND, BACKGROUND},
                {BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND},
        };
        SokobanGameModel sokoban = new SokobanGameModel("Test");
        sokoban.setLevel(fields);
        sokoban.setPlayer(new Point(3, 4));
        sokoban.addTreasure(new Point(2, 4));
        sokoban.addTreasure(new Point(4, 5));
        sokoban.validate();
        return sokoban;
    }

    @Override
    protected SokobanGameModel createSokoban() {
        return new SokobanGameModel("Test");
    }
}
