package com.coding.challenge.marsroverbackend.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlateauTest {

    private Plateau plateau;

    @BeforeEach
    void setUp() {
        plateau = Plateau.builder()
                .maxX(5)
                .maxY(5)
                .build();
    }

    @Nested
    @DisplayName("isInside Tests")
    class IsInsideTests {

        @Test
        @DisplayName("Should return true for position inside plateau")
        void shouldReturnTrueForPositionInsidePlateau() {
            assertTrue(plateau.isInside(3, 3));
        }

        @Test
        @DisplayName("Should return true for origin (0,0)")
        void shouldReturnTrueForOrigin() {
            assertTrue(plateau.isInside(0, 0));
        }

        @Test
        @DisplayName("Should return true for maximum boundary position")
        void shouldReturnTrueForMaxBoundary() {
            assertTrue(plateau.isInside(5, 5));
        }

        @Test
        @DisplayName("Should return true for edge positions")
        void shouldReturnTrueForEdgePositions() {
            assertAll(
                    () -> assertTrue(plateau.isInside(0, 3)),
                    () -> assertTrue(plateau.isInside(5, 3)),
                    () -> assertTrue(plateau.isInside(3, 0)),
                    () -> assertTrue(plateau.isInside(3, 5))
            );
        }

        @Test
        @DisplayName("Should return false for negative X")
        void shouldReturnFalseForNegativeX() {
            assertFalse(plateau.isInside(-1, 3));
        }

        @Test
        @DisplayName("Should return false for negative Y")
        void shouldReturnFalseForNegativeY() {
            assertFalse(plateau.isInside(3, -1));
        }

        @Test
        @DisplayName("Should return false for X beyond maximum")
        void shouldReturnFalseForXBeyondMax() {
            assertFalse(plateau.isInside(6, 3));
        }

        @Test
        @DisplayName("Should return false for Y beyond maximum")
        void shouldReturnFalseForYBeyondMax() {
            assertFalse(plateau.isInside(3, 6));
        }

        @Test
        @DisplayName("Should return false for both coordinates outside")
        void shouldReturnFalseForBothCoordinatesOutside() {
            assertFalse(plateau.isInside(-1, -1));
            assertFalse(plateau.isInside(6, 6));
        }
    }

    @Nested
    @DisplayName("Plateau Builder Tests")
    class PlateauBuilderTests {

        @Test
        @DisplayName("Should create plateau with specified dimensions")
        void shouldCreatePlateauWithSpecifiedDimensions() {
            Plateau customPlateau = Plateau.builder()
                    .maxX(10)
                    .maxY(8)
                    .build();

            assertEquals(10, customPlateau.getMaxX());
            assertEquals(8, customPlateau.getMaxY());
        }

        @Test
        @DisplayName("Should create plateau with zero dimensions")
        void shouldCreatePlateauWithZeroDimensions() {
            Plateau zeroPlateau = Plateau.builder()
                    .maxX(0)
                    .maxY(0)
                    .build();

            assertTrue(zeroPlateau.isInside(0, 0));
            assertFalse(zeroPlateau.isInside(1, 0));
            assertFalse(zeroPlateau.isInside(0, 1));
        }

        @Test
        @DisplayName("Should create rectangular plateau")
        void shouldCreateRectangularPlateau() {
            Plateau rectangularPlateau = Plateau.builder()
                    .maxX(10)
                    .maxY(3)
                    .build();

            assertTrue(rectangularPlateau.isInside(10, 3));
            assertFalse(rectangularPlateau.isInside(10, 4));
            assertFalse(rectangularPlateau.isInside(11, 3));
        }
    }

    @Nested
    @DisplayName("Getter and Setter Tests")
    class GetterSetterTests {

        @Test
        @DisplayName("Should get maxX correctly")
        void shouldGetMaxX() {
            assertEquals(5, plateau.getMaxX());
        }

        @Test
        @DisplayName("Should get maxY correctly")
        void shouldGetMaxY() {
            assertEquals(5, plateau.getMaxY());
        }

        @Test
        @DisplayName("Should set maxX correctly")
        void shouldSetMaxX() {
            plateau.setMaxX(10);
            assertEquals(10, plateau.getMaxX());
        }

        @Test
        @DisplayName("Should set maxY correctly")
        void shouldSetMaxY() {
            plateau.setMaxY(10);
            assertEquals(10, plateau.getMaxY());
        }
    }
}
