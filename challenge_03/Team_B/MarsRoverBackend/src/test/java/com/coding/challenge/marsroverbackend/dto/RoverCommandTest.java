package com.coding.challenge.marsroverbackend.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoverCommandTest {

    @Test
    @DisplayName("Should create RoverCommand with builder")
    void shouldCreateRoverCommandWithBuilder() {
        RoverCommand command = RoverCommand.builder()
                .x(1)
                .y(2)
                .direction('N')
                .commands("LMLMLMLMM")
                .build();

        assertEquals(1, command.getX());
        assertEquals(2, command.getY());
        assertEquals('N', command.getDirection());
        assertEquals("LMLMLMLMM", command.getCommands());
    }

    @Test
    @DisplayName("Should create RoverCommand with all args constructor")
    void shouldCreateRoverCommandWithAllArgsConstructor() {
        RoverCommand command = new RoverCommand(3, 4, 'E', "MMRMMRMRRM");

        assertEquals(3, command.getX());
        assertEquals(4, command.getY());
        assertEquals('E', command.getDirection());
        assertEquals("MMRMMRMRRM", command.getCommands());
    }

    @Test
    @DisplayName("Should create RoverCommand with no args constructor")
    void shouldCreateRoverCommandWithNoArgsConstructor() {
        RoverCommand command = new RoverCommand();

        assertEquals(0, command.getX());
        assertEquals(0, command.getY());
        assertEquals('\u0000', command.getDirection());
        assertNull(command.getCommands());
    }

    @Test
    @DisplayName("Should set and get X coordinate")
    void shouldSetAndGetXCoordinate() {
        RoverCommand command = new RoverCommand();
        command.setX(5);

        assertEquals(5, command.getX());
    }

    @Test
    @DisplayName("Should set and get Y coordinate")
    void shouldSetAndGetYCoordinate() {
        RoverCommand command = new RoverCommand();
        command.setY(7);

        assertEquals(7, command.getY());
    }

    @Test
    @DisplayName("Should set and get direction")
    void shouldSetAndGetDirection() {
        RoverCommand command = new RoverCommand();
        command.setDirection('S');

        assertEquals('S', command.getDirection());
    }

    @Test
    @DisplayName("Should set and get commands")
    void shouldSetAndGetCommands() {
        RoverCommand command = new RoverCommand();
        command.setCommands("MMLMR");

        assertEquals("MMLMR", command.getCommands());
    }

    @Test
    @DisplayName("Should handle all valid directions")
    void shouldHandleAllValidDirections() {
        assertAll(
                () -> {
                    RoverCommand command = RoverCommand.builder().direction('N').build();
                    assertEquals('N', command.getDirection());
                },
                () -> {
                    RoverCommand command = RoverCommand.builder().direction('E').build();
                    assertEquals('E', command.getDirection());
                },
                () -> {
                    RoverCommand command = RoverCommand.builder().direction('S').build();
                    assertEquals('S', command.getDirection());
                },
                () -> {
                    RoverCommand command = RoverCommand.builder().direction('W').build();
                    assertEquals('W', command.getDirection());
                }
        );
    }

    @Test
    @DisplayName("Should handle empty commands string")
    void shouldHandleEmptyCommandsString() {
        RoverCommand command = RoverCommand.builder()
                .x(0).y(0).direction('N').commands("")
                .build();

        assertEquals("", command.getCommands());
    }
}
