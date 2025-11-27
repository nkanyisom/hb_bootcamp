package com.coding.challenge.marsroverbackend.service;

import com.coding.challenge.marsroverbackend.dto.MissionRequestDTO;
import com.coding.challenge.marsroverbackend.dto.RoverCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MissionServiceTest {

    private MissionService missionService;

    @BeforeEach
    void setUp() {
        missionService = new MissionService();
    }

    @Nested
    @DisplayName("Single Rover Tests")
    class SingleRoverTests {

        @Test
        @DisplayName("Should process single rover command")
        void shouldProcessSingleRoverCommand() {
            RoverCommand roverCommand = RoverCommand.builder()
                    .x(1).y(2).direction('N').commands("LMLMLMLMM")
                    .build();

            MissionRequestDTO request = MissionRequestDTO.builder()
                    .gridX(5).gridY(5)
                    .rovers(Collections.singletonList(roverCommand))
                    .build();

            List<String> results = missionService.moveRover(request);

            assertEquals(1, results.size());
            assertEquals("1 3 N", results.get(0));
        }

        @Test
        @DisplayName("Should handle rover with no movement commands")
        void shouldHandleRoverWithNoMovementCommands() {
            RoverCommand roverCommand = RoverCommand.builder()
                    .x(2).y(3).direction('E').commands("")
                    .build();

            MissionRequestDTO request = MissionRequestDTO.builder()
                    .gridX(5).gridY(5)
                    .rovers(Collections.singletonList(roverCommand))
                    .build();

            List<String> results = missionService.moveRover(request);

            assertEquals(1, results.size());
            assertEquals("2 3 E", results.get(0));
        }

        @Test
        @DisplayName("Should handle rover with only turn commands")
        void shouldHandleRoverWithOnlyTurnCommands() {
            RoverCommand roverCommand = RoverCommand.builder()
                    .x(0).y(0).direction('N').commands("LLRR")
                    .build();

            MissionRequestDTO request = MissionRequestDTO.builder()
                    .gridX(5).gridY(5)
                    .rovers(Collections.singletonList(roverCommand))
                    .build();

            List<String> results = missionService.moveRover(request);

            assertEquals(1, results.size());
            assertEquals("0 0 N", results.get(0));
        }
    }

    @Nested
    @DisplayName("Multiple Rovers Tests")
    class MultipleRoversTests {

        @Test
        @DisplayName("Should process multiple rovers - NASA example")
        void shouldProcessMultipleRoversNasaExample() {
            RoverCommand rover1 = RoverCommand.builder()
                    .x(1).y(2).direction('N').commands("LMLMLMLMM")
                    .build();

            RoverCommand rover2 = RoverCommand.builder()
                    .x(3).y(3).direction('E').commands("MMRMMRMRRM")
                    .build();

            MissionRequestDTO request = MissionRequestDTO.builder()
                    .gridX(5).gridY(5)
                    .rovers(Arrays.asList(rover1, rover2))
                    .build();

            List<String> results = missionService.moveRover(request);

            assertEquals(2, results.size());
            assertEquals("1 3 N", results.get(0));
            assertEquals("5 1 E", results.get(1));
        }

        @Test
        @DisplayName("Should process three rovers")
        void shouldProcessThreeRovers() {
            RoverCommand rover1 = RoverCommand.builder()
                    .x(0).y(0).direction('N').commands("MM")
                    .build();

            RoverCommand rover2 = RoverCommand.builder()
                    .x(2).y(2).direction('E').commands("MM")
                    .build();

            RoverCommand rover3 = RoverCommand.builder()
                    .x(4).y(4).direction('S').commands("MM")
                    .build();

            MissionRequestDTO request = MissionRequestDTO.builder()
                    .gridX(5).gridY(5)
                    .rovers(Arrays.asList(rover1, rover2, rover3))
                    .build();

            List<String> results = missionService.moveRover(request);

            assertEquals(3, results.size());
            assertEquals("0 2 N", results.get(0));
            assertEquals("4 2 E", results.get(1));
            assertEquals("4 2 S", results.get(2));
        }

        @Test
        @DisplayName("Should return empty list for no rovers")
        void shouldReturnEmptyListForNoRovers() {
            MissionRequestDTO request = MissionRequestDTO.builder()
                    .gridX(5).gridY(5)
                    .rovers(Collections.emptyList())
                    .build();

            List<String> results = missionService.moveRover(request);

            assertTrue(results.isEmpty());
        }
    }

    @Nested
    @DisplayName("Validation Tests")
    class ValidationTests {

        @Test
        @DisplayName("Should throw exception when rover starts outside plateau - X too large")
        void shouldThrowExceptionWhenRoverStartsOutsidePlateauXTooLarge() {
            RoverCommand roverCommand = RoverCommand.builder()
                    .x(6).y(2).direction('N').commands("M")
                    .build();

            MissionRequestDTO request = MissionRequestDTO.builder()
                    .gridX(5).gridY(5)
                    .rovers(Collections.singletonList(roverCommand))
                    .build();

            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> missionService.moveRover(request)
            );

            assertTrue(exception.getMessage().contains("outside plateau"));
        }

        @Test
        @DisplayName("Should throw exception when rover starts outside plateau - Y too large")
        void shouldThrowExceptionWhenRoverStartsOutsidePlateauYTooLarge() {
            RoverCommand roverCommand = RoverCommand.builder()
                    .x(2).y(6).direction('N').commands("M")
                    .build();

            MissionRequestDTO request = MissionRequestDTO.builder()
                    .gridX(5).gridY(5)
                    .rovers(Collections.singletonList(roverCommand))
                    .build();

            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> missionService.moveRover(request)
            );

            assertTrue(exception.getMessage().contains("outside plateau"));
        }

        @Test
        @DisplayName("Should throw exception when rover starts at negative X")
        void shouldThrowExceptionWhenRoverStartsAtNegativeX() {
            RoverCommand roverCommand = RoverCommand.builder()
                    .x(-1).y(2).direction('N').commands("M")
                    .build();

            MissionRequestDTO request = MissionRequestDTO.builder()
                    .gridX(5).gridY(5)
                    .rovers(Collections.singletonList(roverCommand))
                    .build();

            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> missionService.moveRover(request)
            );

            assertTrue(exception.getMessage().contains("outside plateau"));
        }

        @Test
        @DisplayName("Should throw exception when rover starts at negative Y")
        void shouldThrowExceptionWhenRoverStartsAtNegativeY() {
            RoverCommand roverCommand = RoverCommand.builder()
                    .x(2).y(-1).direction('N').commands("M")
                    .build();

            MissionRequestDTO request = MissionRequestDTO.builder()
                    .gridX(5).gridY(5)
                    .rovers(Collections.singletonList(roverCommand))
                    .build();

            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> missionService.moveRover(request)
            );

            assertTrue(exception.getMessage().contains("outside plateau"));
        }

        @Test
        @DisplayName("Should allow rover at plateau boundary")
        void shouldAllowRoverAtPlateauBoundary() {
            RoverCommand roverCommand = RoverCommand.builder()
                    .x(5).y(5).direction('N').commands("RR")
                    .build();

            MissionRequestDTO request = MissionRequestDTO.builder()
                    .gridX(5).gridY(5)
                    .rovers(Collections.singletonList(roverCommand))
                    .build();

            List<String> results = missionService.moveRover(request);

            assertEquals(1, results.size());
            assertEquals("5 5 S", results.get(0));
        }
    }

    @Nested
    @DisplayName("Different Plateau Sizes Tests")
    class DifferentPlateauSizesTests {

        @Test
        @DisplayName("Should work with small plateau")
        void shouldWorkWithSmallPlateau() {
            RoverCommand roverCommand = RoverCommand.builder()
                    .x(0).y(0).direction('N').commands("MRM")
                    .build();

            MissionRequestDTO request = MissionRequestDTO.builder()
                    .gridX(1).gridY(1)
                    .rovers(Collections.singletonList(roverCommand))
                    .build();

            List<String> results = missionService.moveRover(request);

            assertEquals("1 1 E", results.get(0));
        }

        @Test
        @DisplayName("Should work with large plateau")
        void shouldWorkWithLargePlateau() {
            RoverCommand roverCommand = RoverCommand.builder()
                    .x(50).y(50).direction('E').commands("MMMMMMMMMM")
                    .build();

            MissionRequestDTO request = MissionRequestDTO.builder()
                    .gridX(100).gridY(100)
                    .rovers(Collections.singletonList(roverCommand))
                    .build();

            List<String> results = missionService.moveRover(request);

            assertEquals("60 50 E", results.get(0));
        }

        @Test
        @DisplayName("Should work with rectangular plateau")
        void shouldWorkWithRectangularPlateau() {
            RoverCommand roverCommand = RoverCommand.builder()
                    .x(0).y(0).direction('E').commands("MMMMMMMMMM")
                    .build();

            MissionRequestDTO request = MissionRequestDTO.builder()
                    .gridX(10).gridY(3)
                    .rovers(Collections.singletonList(roverCommand))
                    .build();

            List<String> results = missionService.moveRover(request);

            assertEquals("10 0 E", results.get(0));
        }
    }
}
