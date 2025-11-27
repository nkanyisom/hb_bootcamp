package com.coding.challenge.marsroverbackend.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MissionRequestDTOTest {

    @Test
    @DisplayName("Should create MissionRequestDTO with builder")
    void shouldCreateMissionRequestDTOWithBuilder() {
        RoverCommand roverCommand = RoverCommand.builder()
                .x(1).y(2).direction('N').commands("M")
                .build();

        MissionRequestDTO request = MissionRequestDTO.builder()
                .gridX(5)
                .gridY(5)
                .rovers(Collections.singletonList(roverCommand))
                .build();

        assertEquals(5, request.getGridX());
        assertEquals(5, request.getGridY());
        assertEquals(1, request.getRovers().size());
    }

    @Test
    @DisplayName("Should create MissionRequestDTO with all args constructor")
    void shouldCreateMissionRequestDTOWithAllArgsConstructor() {
        RoverCommand roverCommand = RoverCommand.builder()
                .x(1).y(2).direction('N').commands("M")
                .build();

        List<RoverCommand> rovers = Collections.singletonList(roverCommand);
        MissionRequestDTO request = new MissionRequestDTO(10, 8, rovers);

        assertEquals(10, request.getGridX());
        assertEquals(8, request.getGridY());
        assertEquals(rovers, request.getRovers());
    }

    @Test
    @DisplayName("Should create MissionRequestDTO with no args constructor")
    void shouldCreateMissionRequestDTOWithNoArgsConstructor() {
        MissionRequestDTO request = new MissionRequestDTO();

        assertEquals(0, request.getGridX());
        assertEquals(0, request.getGridY());
        assertNull(request.getRovers());
    }

    @Test
    @DisplayName("Should set and get gridX")
    void shouldSetAndGetGridX() {
        MissionRequestDTO request = new MissionRequestDTO();
        request.setGridX(15);

        assertEquals(15, request.getGridX());
    }

    @Test
    @DisplayName("Should set and get gridY")
    void shouldSetAndGetGridY() {
        MissionRequestDTO request = new MissionRequestDTO();
        request.setGridY(20);

        assertEquals(20, request.getGridY());
    }

    @Test
    @DisplayName("Should set and get rovers list")
    void shouldSetAndGetRoversList() {
        MissionRequestDTO request = new MissionRequestDTO();

        RoverCommand rover1 = RoverCommand.builder()
                .x(1).y(2).direction('N').commands("M")
                .build();
        RoverCommand rover2 = RoverCommand.builder()
                .x(3).y(3).direction('E').commands("MM")
                .build();

        List<RoverCommand> rovers = Arrays.asList(rover1, rover2);
        request.setRovers(rovers);

        assertEquals(2, request.getRovers().size());
        assertEquals(rovers, request.getRovers());
    }

    @Test
    @DisplayName("Should handle empty rovers list")
    void shouldHandleEmptyRoversList() {
        MissionRequestDTO request = MissionRequestDTO.builder()
                .gridX(5)
                .gridY(5)
                .rovers(Collections.emptyList())
                .build();

        assertTrue(request.getRovers().isEmpty());
    }

    @Test
    @DisplayName("Should handle multiple rovers")
    void shouldHandleMultipleRovers() {
        RoverCommand rover1 = RoverCommand.builder()
                .x(1).y(2).direction('N').commands("LMLMLMLMM")
                .build();
        RoverCommand rover2 = RoverCommand.builder()
                .x(3).y(3).direction('E').commands("MMRMMRMRRM")
                .build();
        RoverCommand rover3 = RoverCommand.builder()
                .x(0).y(0).direction('S').commands("MMM")
                .build();

        MissionRequestDTO request = MissionRequestDTO.builder()
                .gridX(5)
                .gridY(5)
                .rovers(Arrays.asList(rover1, rover2, rover3))
                .build();

        assertEquals(3, request.getRovers().size());
        assertEquals(1, request.getRovers().get(0).getX());
        assertEquals(3, request.getRovers().get(1).getX());
        assertEquals(0, request.getRovers().get(2).getX());
    }

    @Test
    @DisplayName("Should create rectangular plateau configuration")
    void shouldCreateRectangularPlateauConfiguration() {
        MissionRequestDTO request = MissionRequestDTO.builder()
                .gridX(10)
                .gridY(5)
                .rovers(Collections.emptyList())
                .build();

        assertEquals(10, request.getGridX());
        assertEquals(5, request.getGridY());
    }
}
