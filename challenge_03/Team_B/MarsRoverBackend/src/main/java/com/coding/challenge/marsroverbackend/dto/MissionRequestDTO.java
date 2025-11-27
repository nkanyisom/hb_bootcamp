package com.coding.challenge.marsroverbackend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MissionRequestDTO {
    @Min(value = 0, message = "gridX must be non-negative")
    private int gridX;

    @Min(value = 0, message = "gridY must be non-negative")
    private int gridY;

    @NotNull(message = "rovers list cannot be null")
    @Valid
    private List<RoverCommand> rovers;

}
