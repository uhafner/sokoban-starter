package edu.hm.hafner.sokoban;

import java.util.List;

import static edu.hm.hafner.sokoban.Field.*;

/**
 * Reads and creates a Sokoban level in ASCII format.
 *
 * @author Ullrich Hafner
 */
// Wall                 #
// Player               @
// Player on target     +
// Treasure             $
// Treasure on target   *
// Target               .
// Floor                (Space)
// Comment              ::
public class SokobanReader {
    /**
     * Converts the array of strings to a Sokoban level.
     *
     * @param name
     *         the name of the level
     * @param lines
     *         the lines of the level
     *
     * @return the created model of the level
     */
    public SokobanGameModel read(final String name, final List<String> lines) {
        // FIXME: Implement reading of game levels from the list of lines
        SokobanGameModel sokobanGameModel = new SokobanGameModel(name);
        sokobanGameModel.setLevel(createDummyLevel());
        sokobanGameModel.setPlayer(new Point(0, 0));
        return sokobanGameModel;
    }

    private Field[][] createDummyLevel() {
        return new Field[][] {
                {WALL, WALL, WALL, WALL},
                {WALL, FLOOR, TARGET, WALL},
                {WALL, FLOOR, TARGET, WALL},
                {WALL, FLOOR, FLOOR, WALL},
                {WALL, WALL, WALL, WALL},
        };
    }
}
