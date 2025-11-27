package com.coding.challenge.marsroverbackend.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.coding.challenge.marsroverbackend.dto.MissionRequestDTO;
import com.coding.challenge.marsroverbackend.dto.RoverCommand;
import com.coding.challenge.marsroverbackend.service.MissionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@WebMvcTest(MissionController.class)
class MissionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MissionService missionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should return rover positions for valid request")
    void shouldReturnRoverPositionsForValidRequest() throws Exception {
        RoverCommand roverCommand = RoverCommand.builder()
                .x(1).y(2).direction('N').commands("LMLMLMLMM")
                .build();

        MissionRequestDTO request = MissionRequestDTO.builder()
                .gridX(5).gridY(5)
                .rovers(Collections.singletonList(roverCommand))
                .build();

        when(missionService.moveRover(any(MissionRequestDTO.class)))
                .thenReturn(Collections.singletonList("1 3 N"));

        mockMvc.perform(post("/move")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0]").value("1 3 N"));
    }

    @Test
    @DisplayName("Should return multiple rover positions - NASA example")
    void shouldReturnMultipleRoverPositionsNasaExample() throws Exception {
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

        List<String> expectedResults = Arrays.asList("1 3 N", "5 1 E");
        when(missionService.moveRover(any(MissionRequestDTO.class)))
                .thenReturn(expectedResults);

        mockMvc.perform(post("/move")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0]").value("1 3 N"))
                .andExpect(jsonPath("$[1]").value("5 1 E"))
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("Should return empty list for no rovers")
    void shouldReturnEmptyListForNoRovers() throws Exception {
        MissionRequestDTO request = MissionRequestDTO.builder()
                .gridX(5).gridY(5)
                .rovers(Collections.emptyList())
                .build();

        when(missionService.moveRover(any(MissionRequestDTO.class)))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(post("/move")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("Should handle service exception")
    void shouldHandleServiceException() throws Exception {
        RoverCommand roverCommand = RoverCommand.builder()
                .x(10).y(10).direction('N').commands("M")
                .build();

        MissionRequestDTO request = MissionRequestDTO.builder()
                .gridX(5).gridY(5)
                .rovers(Collections.singletonList(roverCommand))
                .build();

        when(missionService.moveRover(any(MissionRequestDTO.class)))
                .thenThrow(new IllegalArgumentException("Rover starting position (10,10) is outside plateau (5,5)."));

        mockMvc.perform(post("/move")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Should accept valid JSON request body")
    void shouldAcceptValidJsonRequestBody() throws Exception {
        String jsonRequest = """
                {
                    "gridX": 5,
                    "gridY": 5,
                    "rovers": [
                        {
                            "x": 1,
                            "y": 2,
                            "direction": "N",
                            "commands": "LMLMLMLMM"
                        }
                    ]
                }
                """;

        when(missionService.moveRover(any(MissionRequestDTO.class)))
                .thenReturn(Collections.singletonList("1 3 N"));

        mockMvc.perform(post("/move")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());
    }
}
