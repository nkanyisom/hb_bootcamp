package com.coding.challenge.marsroverbackend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoverCommand {
    @Min(value = 0, message = "x coordinate must be non-negative")
    private int x;

    @Min(value = 0, message = "y coordinate must be non-negative")
    private int y;

    private char direction;

    @NotNull(message = "commands cannot be null")
    @Pattern(regexp = "^[LRM]*$", message = "commands must only contain L, R, or M")
    private String commands;
}
