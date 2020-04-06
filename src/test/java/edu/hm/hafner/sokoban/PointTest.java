package edu.hm.hafner.sokoban;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests the class {@link Point}.
 */
public class PointTest {
    /** Verifies that the coordinates of a new point are correctly set. */
    @Test
    public void shouldCreatePointY() {
        // Given
        Point point = new Point(3, 4);

        // When and then
        assertThat(point.getY()).isEqualTo(4);
    }

    /** Verifies that the coordinates of a new point are correctly set. */
    @Test
    public void shouldCreatePointX() {
        // Given
        Point point = new Point(3, 4);

        // When and then
        assertThat(point.getX()).isEqualTo(3);
    }

    /** Verifies that the equals operation correctly works. */
    @Test
    public void shouldDetectEqualPoints() {
        // Given
        Point point = new Point(3, 4);

        // When and then
        assertThat(point.isEqualTo(new Point(3, 4))).isTrue();
    }

    /** Verifies that the equals operation correctly works. */
    @Test
    public void shouldDetectNotEqualPointsX() {
        // Given
        Point point = new Point(3, 4);

        // When and then
        assertThat(point.isEqualTo(new Point(2, 4))).isFalse();
        assertThat(point.isEqualTo(new Point(4, 4))).isFalse();
    }

    /** Verifies that the equals operation correctly works. */
    @Test
    public void shouldDetectNotEqualPointsY() {
        // Given
        Point point = new Point(3, 4);

        // When and then
        assertThat(point.isEqualTo(new Point(3, 5))).isFalse();
        assertThat(point.isEqualTo(new Point(3, 3))).isFalse();

        assertThat(point.isEqualTo(null)).isFalse();
    }

    /** Verifies that the equals operation correctly works. */
    @Test
    public void shouldDetectNotEqualPointsNull() {
        // Given
        Point point = new Point(3, 4);

        // When and then
        assertThat(point.isEqualTo(null)).isFalse();
    }

    /** Verifies that the surrounding points are correctly computed and the SUT is not modified. */
    @Test
    public void shouldCreateNeighborsL() {
        // Given
        Point point = new Point(3, 4);

        // When and then
        assertThat(point.moveLeft().isEqualTo(new Point(2, 4))).isTrue();
        assertThat(point.isEqualTo(new Point(3, 4))).isTrue();
    }

    /** Verifies that the surrounding points are correctly computed and the SUT is not modified. */
    @Test
    public void shouldCreateNeighborsR() {
        // Given
        Point point = new Point(3, 4);

        // When and then
        assertThat(point.moveRight().isEqualTo(new Point(4, 4))).isTrue();
        assertThat(point.isEqualTo(new Point(3, 4))).isTrue();
    }

    /** Verifies that the surrounding points are correctly computed and the SUT is not modified. */
    @Test
    public void shouldCreateNeighborsU() {
        // Given
        Point point = new Point(3, 4);

        // When and then
        assertThat(point.moveUp().isEqualTo(new Point(3, 3))).isTrue();
        assertThat(point.isEqualTo(new Point(3, 4))).isTrue();
    }

    /** Verifies that the surrounding points are correctly computed and the SUT is not modified. */
    @Test
    public void shouldCreateNeighborsD() {
        // Given
        Point point = new Point(3, 4);

        // When and then
        assertThat(point.moveDown().isEqualTo(new Point(3, 5))).isTrue();
        assertThat(point.isEqualTo(new Point(3, 4))).isTrue();
    }
}
