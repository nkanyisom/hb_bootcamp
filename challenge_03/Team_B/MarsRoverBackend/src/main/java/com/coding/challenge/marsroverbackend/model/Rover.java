package com.coding.challenge.marsroverbackend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
public class Rover {
    private int x;
    private int y;
    private char direction;
    private Plateau plateau;

    //Method to process a string of commands
    public void processCommands(String commands) {
        for (char c : commands.toCharArray()) {
            switch (c){
                case 'L':
                    turnLeft();
                    break;
                case 'R':
                    turnRight();
                    break;
                case 'M':
                    move();
                    break;
                default:
                    break;
            }
        }
    }

    //Method for turning right
    private void turnLeft() {
        switch (direction) {
            case 'N':
                direction = 'W';
                break;
            case 'W':
                direction = 'S';
                break;
            case 'S':
                direction ='E';
                break;
            case 'E':
                direction = 'N';
                break;
            default:
                break;
        }
    }

    //Method for turning right
    private void turnRight() {
        switch (direction) {
            case 'N':
                direction = 'E';
                break;
            case 'E':
                direction = 'S';
                break;
            case 'S':
                direction = 'W';
                break;
            case 'W':
                direction = 'N';
                break;
            default:
                break;
        }
    }

    //Method for moving forward
    private void move() {
        int newX = x;
        int newY = y;

        switch (direction) {
            case 'N':
                newY++;
                break;
            case 'E':
                newX++;
                break;
            case 'S':
                newY--;
                break;
            case 'W':
                newX--;
                break;
            default:
                break;
        }
        //Check if the new position is inside the plateau
        if (plateau.isInside(newX, newY)) {
            x = newX;
            y = newY;
        } else {
            log.info("Rover cannot move outside the plateau: attempted move to ({}, {}) from ({}, {})",
                    newX, newY, x, y);
        }
    }

    //Method to get the current position of the rover
    public String getPosition() {
        return x + " " + y + " " + direction;
    }
}
