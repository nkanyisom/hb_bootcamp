package com.coding.challenge.marsroverbackend.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoverTest {

    private Plateau plateau;

    @BeforeEach
    void setUp() {
        plateau = Plateau.builder()
                .maxX(5)
                .maxY(5)
                .build();
    }

    @Nested
    @DisplayName("Turn Left Tests")
    class TurnLeftTests {

        @Test
        @DisplayName("Should turn from North to West")
        void shouldTurnFromNorthToWest() {
            Rover rover = Rover.builder()
                    .x(1).y(2).direction('N').plateau(plateau)
                    .build();

            rover.processCommands("L");

            assertEquals("1 2 W", rover.getPosition());
        }

        @Test
        @DisplayName("Should turn from West to South")
        void shouldTurnFromWestToSouth() {
            Rover rover = Rover.builder()
                    .x(1).y(2).direction('W').plateau(plateau)
                    .build();

            rover.processCommands("L");

            assertEquals("1 2 S", rover.getPosition());
        }

        @Test
        @DisplayName("Should turn from South to East")
        void shouldTurnFromSouthToEast() {
            Rover rover = Rover.builder()
                    .x(1).y(2).direction('S').plateau(plateau)
                    .build();

            rover.processCommands("L");

            assertEquals("1 2 E", rover.getPosition());
        }

        @Test
        @DisplayName("Should turn from East to North")
        void shouldTurnFromEastToNorth() {
            Rover rover = Rover.builder()
                    .x(1).y(2).direction('E').plateau(plateau)
                    .build();

            rover.processCommands("L");

            assertEquals("1 2 N", rover.getPosition());
        }

        @Test
        @DisplayName("Should complete full rotation with 4 left turns")
        void shouldCompleteFullRotationWithFourLeftTurns() {
            Rover rover = Rover.builder()
                    .x(1).y(2).direction('N').plateau(plateau)
                    .build();

            rover.processCommands("LLLL");

            assertEquals("1 2 N", rover.getPosition());
        }
    }

    @Nested
    @DisplayName("Turn Right Tests")
    class TurnRightTests {

        @Test
        @DisplayName("Should turn from North to East")
        void shouldTurnFromNorthToEast() {
            Rover rover = Rover.builder()
                    .x(1).y(2).direction('N').plateau(plateau)
                    .build();

            rover.processCommands("R");

            assertEquals("1 2 E", rover.getPosition());
        }

        @Test
        @DisplayName("Should turn from East to South")
        void shouldTurnFromEastToSouth() {
            Rover rover = Rover.builder()
                    .x(1).y(2).direction('E').plateau(plateau)
                    .build();

            rover.processCommands("R");

            assertEquals("1 2 S", rover.getPosition());
        }

        @Test
        @DisplayName("Should turn from South to West")
        void shouldTurnFromSouthToWest() {
            Rover rover = Rover.builder()
                    .x(1).y(2).direction('S').plateau(plateau)
                    .build();

            rover.processCommands("R");

            assertEquals("1 2 W", rover.getPosition());
        }

        @Test
        @DisplayName("Should turn from West to North")
        void shouldTurnFromWestToNorth() {
            Rover rover = Rover.builder()
                    .x(1).y(2).direction('W').plateau(plateau)
                    .build();

            rover.processCommands("R");

            assertEquals("1 2 N", rover.getPosition());
        }

        @Test
        @DisplayName("Should complete full rotation with 4 right turns")
        void shouldCompleteFullRotationWithFourRightTurns() {
            Rover rover = Rover.builder()
                    .x(1).y(2).direction('N').plateau(plateau)
                    .build();

            rover.processCommands("RRRR");

            assertEquals("1 2 N", rover.getPosition());
        }
    }

    @Nested
    @DisplayName("Move Tests")
    class MoveTests {

        @Test
        @DisplayName("Should move North")
        void shouldMoveNorth() {
            Rover rover = Rover.builder()
                    .x(1).y(2).direction('N').plateau(plateau)
                    .build();

            rover.processCommands("M");

            assertEquals("1 3 N", rover.getPosition());
        }

        @Test
        @DisplayName("Should move East")
        void shouldMoveEast() {
            Rover rover = Rover.builder()
                    .x(1).y(2).direction('E').plateau(plateau)
                    .build();

            rover.processCommands("M");

            assertEquals("2 2 E", rover.getPosition());
        }

        @Test
        @DisplayName("Should move South")
        void shouldMoveSouth() {
            Rover rover = Rover.builder()
                    .x(1).y(2).direction('S').plateau(plateau)
                    .build();

            rover.processCommands("M");

            assertEquals("1 1 S", rover.getPosition());
        }

        @Test
        @DisplayName("Should move West")
        void shouldMoveWest() {
            Rover rover = Rover.builder()
                    .x(1).y(2).direction('W').plateau(plateau)
                    .build();

            rover.processCommands("M");

            assertEquals("0 2 W", rover.getPosition());
        }

        @Test
        @DisplayName("Should move multiple times")
        void shouldMoveMultipleTimes() {
            Rover rover = Rover.builder()
                    .x(0).y(0).direction('N').plateau(plateau)
                    .build();

            rover.processCommands("MMM");

            assertEquals("0 3 N", rover.getPosition());
        }
    }

    @Nested
    @DisplayName("Boundary Tests")
    class BoundaryTests {

        @Test
        @DisplayName("Should not move beyond North boundary")
        void shouldNotMoveBeyondNorthBoundary() {
            Rover rover = Rover.builder()
                    .x(2).y(5).direction('N').plateau(plateau)
                    .build();

            rover.processCommands("M");

            assertEquals("2 5 N", rover.getPosition());
        }

        @Test
        @DisplayName("Should not move beyond East boundary")
        void shouldNotMoveBeyondEastBoundary() {
            Rover rover = Rover.builder()
                    .x(5).y(2).direction('E').plateau(plateau)
                    .build();

            rover.processCommands("M");

            assertEquals("5 2 E", rover.getPosition());
        }

        @Test
        @DisplayName("Should not move beyond South boundary")
        void shouldNotMoveBeyondSouthBoundary() {
            Rover rover = Rover.builder()
                    .x(2).y(0).direction('S').plateau(plateau)
                    .build();

            rover.processCommands("M");

            assertEquals("2 0 S", rover.getPosition());
        }

        @Test
        @DisplayName("Should not move beyond West boundary")
        void shouldNotMoveBeyondWestBoundary() {
            Rover rover = Rover.builder()
                    .x(0).y(2).direction('W').plateau(plateau)
                    .build();

            rover.processCommands("M");

            assertEquals("0 2 W", rover.getPosition());
        }
    }

    @Nested
    @DisplayName("Complex Command Tests")
    class ComplexCommandTests {

        @Test
        @DisplayName("Should execute NASA example 1: 1 2 N with LMLMLMLMM")
        void shouldExecuteNasaExample1() {
            Rover rover = Rover.builder()
                    .x(1).y(2).direction('N').plateau(plateau)
                    .build();

            rover.processCommands("LMLMLMLMM");

            assertEquals("1 3 N", rover.getPosition());
        }

        @Test
        @DisplayName("Should execute NASA example 2: 3 3 E with MMRMMRMRRM")
        void shouldExecuteNasaExample2() {
            Rover rover = Rover.builder()
                    .x(3).y(3).direction('E').plateau(plateau)
                    .build();

            rover.processCommands("MMRMMRMRRM");

            assertEquals("5 1 E", rover.getPosition());
        }

        @Test
        @DisplayName("Should ignore unknown commands")
        void shouldIgnoreUnknownCommands() {
            Rover rover = Rover.builder()
                    .x(1).y(1).direction('N').plateau(plateau)
                    .build();

            rover.processCommands("MXYMZ");

            assertEquals("1 3 N", rover.getPosition());
        }

        @Test
        @DisplayName("Should handle empty commands")
        void shouldHandleEmptyCommands() {
            Rover rover = Rover.builder()
                    .x(1).y(2).direction('N').plateau(plateau)
                    .build();

            rover.processCommands("");

            assertEquals("1 2 N", rover.getPosition());
        }
    }

    @Test
    @DisplayName("Should return correct position format")
    void shouldReturnCorrectPositionFormat() {
        Rover rover = Rover.builder()
                .x(3).y(4).direction('E').plateau(plateau)
                .build();

        String position = rover.getPosition();

        assertEquals("3 4 E", position);
    }
}
