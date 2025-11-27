package com.coding.challenge.marsroverbackend.service;

import com.coding.challenge.marsroverbackend.dto.MissionRequestDTO;
import com.coding.challenge.marsroverbackend.model.Plateau;
import com.coding.challenge.marsroverbackend.model.Rover;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MissionService {

    public List<String> moveRover(MissionRequestDTO request) {
        var plateau = Plateau.builder()
                .maxX(request.getGridX())
                .maxY(request.getGridY())
                .build();

        // to store final positions of rovers
        List<String> results = new ArrayList<>();
        request.getRovers().forEach(command -> {

            // to check if the rover is inside the position
            if (!plateau.isInside(command.getX(), command.getY())) {
                throw new IllegalArgumentException(
                        String.format("Rover starting position (%d,%d) is outside plateau (%d,%d).",
                                command.getX(), command.getY(), plateau.getMaxX(), plateau.getMaxY())
                );
            }

            // create rover and process commands
            var rover = Rover.builder()
                    .x(command.getX())
                    .y(command.getY())
                    .direction(command.getDirection())
                    .plateau(plateau)
                    .build();
            rover.processCommands(command.getCommands());
            results.add(rover.getPosition());
            log.info("Rover final position: {}", rover.getPosition());
        });
        return results;
    }
}
