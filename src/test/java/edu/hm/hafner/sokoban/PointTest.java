package edu.hm.hafner.sokoban;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests the class {@link Point}.
 */
class PointTest {
    /** Verifies that the coordinates of a new point are correctly set. */
    @Test
    void shouldCreatePointY() {
        // Given
        Point point = new Point(3, 4);

        // When and then
        assertThat(point.getY()).isEqualTo(4);
    }

    /** Verifies that the coordinates of a new point are correctly set. */
    @Test
    void shouldCreatePointX() {
        // Given
        Point point = new Point(3, 4);

        // When and then
        assertThat(point.getX()).isEqualTo(3);
    }

    /** Verifies that the equals operation correctly works. */
    @Test
    void shouldDetectEqualPoints() {
        // Given
        Point point = new Point(3, 4);

        // When and then
        assertThat(point).isEqualTo(new Point(3, 4));
    }

    /** Verifies that the equals operation correctly works. */
    @Test
    void shouldDetectNotEqualPointsX() {
        // Given
        Point point = new Point(3, 4);

        // When and then
        assertThat(point).isNotEqualTo(new Point(2, 4));
        assertThat(point).isNotEqualTo(new Point(4, 4));
    }

    /** Verifies that the equals operation correctly works. */
    @Test
    void shouldDetectNotEqualPointsY() {
        // Given
        Point point = new Point(3, 4);

        // When and then
        assertThat(point).isNotEqualTo(new Point(3, 5));
        assertThat(point).isNotEqualTo(new Point(3, 3));
    }

    /** Verifies that the equals operation correctly works. */
    @Test
    void shouldDetectNotEqualPointsNull() {
        // Given
        Point point = new Point(3, 4);

        // When and then
        assertThat(point).isNotEqualTo(null);
    }

    /** Verifies that the surrounding points are correctly computed and the SUT is not modified. */
    @Test
    void shouldCreateNeighborsL() {
        // Given
        Point point = new Point(3, 4);

        // When and then
        assertThat(point.moveLeft()).isEqualTo(new Point(2, 4));
        assertThat(point).isEqualTo(new Point(3, 4));
    }

    /** Verifies that the surrounding points are correctly computed and the SUT is not modified. */
    @Test
    void shouldCreateNeighborsR() {
        // Given
        Point point = new Point(3, 4);

        // When and then
        assertThat(point.moveRight()).isEqualTo(new Point(4, 4));
        assertThat(point).isEqualTo(new Point(3, 4));
    }

    /** Verifies that the surrounding points are correctly computed and the SUT is not modified. */
    @Test
    void shouldCreateNeighborsU() {
        // Given
        Point point = new Point(3, 4);

        // When and then
        assertThat(point.moveUp()).isEqualTo(new Point(3, 3));
        assertThat(point).isEqualTo(new Point(3, 4));
    }

    /** Verifies that the surrounding points are correctly computed and the SUT is not modified. */
    @Test
    void shouldCreateNeighborsD() {
        // Given
        Point point = new Point(3, 4);

        // When and then
        assertThat(point.moveDown()).isEqualTo(new Point(3, 5));
        assertThat(point).isEqualTo(new Point(3, 4));
    }
}
