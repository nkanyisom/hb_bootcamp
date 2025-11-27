package com.coding.challenge.marsroverbackend.controller;

import com.coding.challenge.marsroverbackend.dto.MissionRequestDTO;
import com.coding.challenge.marsroverbackend.service.MissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MissionController {

    // Service to handle mission logic
    private final MissionService missionService;

    // Endpoint to move rovers based on the mission request
    @PostMapping("move")
    public List<String> moveRovers(@Valid @RequestBody MissionRequestDTO request) {
        return missionService.moveRover(request);
    }

}
