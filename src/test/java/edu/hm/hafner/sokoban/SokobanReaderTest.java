package edu.hm.hafner.sokoban;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.Test;

import static edu.hm.hafner.sokoban.Field.*;
import static java.util.Arrays.*;
import static org.assertj.core.api.Assertions.*;

/**
 * Tests the class {@link SokobanReader}.
 *
 * @author Ullrich Hafner
 */
public class SokobanReaderTest {
    /** Verifies that the "chaos.sok" level is correctly read. */
    @Test
    public void shouldReadChaosLevel() {
        List<String> lines = read("/chaos.sok");

        AbstractSokobanModel sokoban = new SokobanReader().read("Chaos", lines);

        Field[][] expected = {
                {BACKGROUND, BACKGROUND, WALL, WALL, WALL, WALL, WALL},
                {WALL, WALL, WALL, FLOOR, FLOOR, TARGET, WALL},
                {WALL, FLOOR, FLOOR, FLOOR, WALL, TARGET, WALL},
                {WALL, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, WALL},
                {WALL, TARGET, FLOOR, FLOOR, WALL, FLOOR, WALL},
                {WALL, FLOOR, FLOOR, FLOOR, FLOOR, TARGET, WALL},
                {WALL, WALL, WALL, WALL, WALL, WALL, WALL},
        };
        assertThatFieldIsCorrect(sokoban, expected);
        assertThatPlayerIsAt(sokoban, new Point(3, 1));
        assertThatTreasuresAreAt(sokoban, new Point(2, 2), new Point(3, 3), new Point(4, 3), new Point(4, 5));
    }

    private List<String> read(final String fileName) {
        try {
            return Files.readAllLines(Paths.get(SokobanReaderTest.class.getResource(fileName).toURI()));
        }
        catch (IOException | URISyntaxException exception) {
            throw new AssertionError(exception);
        }
    }

    /** Verifies that the "minicosmos.sok" level is correctly read. */
    @Test
    public void shouldReadMiniCosmosLevel() {
        List<String> lines = read("/minicosmos.sok");

        AbstractSokobanModel sokoban = new SokobanReader().read("Mini Cosmos", lines);

        Field[][] expected = {
                {BACKGROUND, BACKGROUND, WALL, WALL, WALL, WALL, WALL, BACKGROUND},
                {WALL, WALL, WALL, FLOOR, FLOOR, FLOOR, WALL, BACKGROUND},
                {WALL, FLOOR, FLOOR, FLOOR, WALL, FLOOR, WALL, WALL},
                {WALL, FLOOR, WALL, FLOOR, FLOOR, TARGET, FLOOR, WALL},
                {WALL, FLOOR, FLOOR, FLOOR, FLOOR, WALL, FLOOR, WALL},
                {WALL, WALL, FLOOR, WALL, FLOOR, FLOOR, FLOOR, WALL},
                {BACKGROUND, WALL, FLOOR, FLOOR, FLOOR, WALL, WALL, WALL},
                {BACKGROUND, WALL, WALL, WALL, WALL, WALL, BACKGROUND, BACKGROUND},
        };
        assertThatFieldIsCorrect(sokoban, expected);
        assertThatPlayerIsAt(sokoban, new Point(2, 6));
        assertThatTreasuresAreAt(sokoban, new Point(2, 2));
    }

    /** Verifies that a level with different line lengths is correctly read. */
    @Test
    public void shouldReadAsymmetricLevel() {
        SokobanReader converter = new SokobanReader();
        String[] inputLevel = {
                "####",
                "# .#",
                "#  ###",
                "#*@  #",
                "#  $ #",
                "#  ###",
                "####"
        };

        AbstractSokobanModel sokoban = converter.read("Test", asList(inputLevel));

        Field[][] expected = {
                {WALL, WALL, WALL, WALL, BACKGROUND, BACKGROUND},
                {WALL, FLOOR, TARGET, WALL, BACKGROUND, BACKGROUND},
                {WALL, FLOOR, FLOOR, WALL, WALL, WALL},
                {WALL, TARGET, FLOOR, FLOOR, FLOOR, WALL},
                {WALL, FLOOR, FLOOR, FLOOR, FLOOR, WALL},
                {WALL, FLOOR, FLOOR, WALL, WALL, WALL},
                {WALL, WALL, WALL, WALL, BACKGROUND, BACKGROUND},
        };
        assertThatFieldIsCorrect(sokoban, expected);
        assertThatPlayerIsAt(sokoban, new Point(2, 3));
        assertThatTreasuresAreAt(sokoban, new Point(1, 3), new Point(3, 4));
    }

    /** Verifies that a rectangular level with a player on a target is correctly read. */
    @Test
    public void shouldReadRectangleLevel() {
        SokobanReader converter = new SokobanReader();
        String[] fields = {
                "######",
                "#    #",
                "# #  #",
                "# $* #",
                "# +* #",
                "#    #",
                "######"
        };

        AbstractSokobanModel sokoban = converter.read("Test", asList(fields));

        Field[][] expected = {
                {WALL, WALL, WALL, WALL, WALL, WALL},
                {WALL, FLOOR, FLOOR, FLOOR, FLOOR, WALL},
                {WALL, FLOOR, WALL, FLOOR, FLOOR, WALL},
                {WALL, FLOOR, FLOOR, TARGET, FLOOR, WALL},
                {WALL, FLOOR, TARGET, TARGET, FLOOR, WALL},
                {WALL, FLOOR, FLOOR, FLOOR, FLOOR, WALL},
                {WALL, WALL, WALL, WALL, WALL, WALL},
        };
        assertThatFieldIsCorrect(sokoban, expected);
        assertThatPlayerIsAt(sokoban, new Point(2, 4));
        assertThatTreasuresAreAt(sokoban, new Point(2, 3), new Point(3, 3), new Point(3, 4));
    }

    private void assertThatTreasuresAreAt(final AbstractSokobanModel sokoban, final Point... treasures) {
        assertThat(sokoban.getTreasures().size()).isEqualTo(treasures.length);
        for (Point treasure : treasures) {
            assertThat(sokoban.getTreasures().contains(treasure)).isTrue();
        }
    }

    private void assertThatPlayerIsAt(final AbstractSokobanModel sokoban, final Point player) {
        assertThat(sokoban.getPlayer().isEqualTo(player)).isTrue();
    }

    private void assertThatFieldIsCorrect(final AbstractSokobanModel sokoban, final Field[][] expected) {
        for (int y = 0; y < sokoban.getHeight(); y++) {
            for (int x = 0; x < sokoban.getWidth(); x++) {
                assertThat(sokoban.getField(new Point(x, y)))
                        .as("Field (%d, %d)", x, y)
                        .isEqualTo(expected[y][x]);
            }
        }
    }
}
