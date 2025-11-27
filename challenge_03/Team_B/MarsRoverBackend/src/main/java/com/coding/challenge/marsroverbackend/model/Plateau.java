package com.coding.challenge.marsroverbackend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor

public class Plateau {
    private int maxX;
    private int maxY;

    //Checks rover's new position is still inside the plateau
    public boolean isInside(int x, int y) {
        return x >= 0 && y >= 0 && x <= maxX && y <= maxY;
    }
}
