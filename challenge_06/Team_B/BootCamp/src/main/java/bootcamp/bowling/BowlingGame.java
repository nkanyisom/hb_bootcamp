package bootcamp.bowling;


import java.util.ArrayList;
import java.util.List;

public class BowlingGame {

    private final int[] rolls = new int[21];
    private int currentRoll = 0;


    public void roll(int pins) {

        if (isGameOver()) {
            throw new IllegalArgumentException("Game is over");
        }

        if (pins < 0 || pins > 10) {
            throw new IllegalArgumentException("Pins must be between 0 and 10");
        }

        // Frame validation: prevent total pins > 10 in a frame (except 10th frame)
        if (currentRoll % 2 == 1 && currentRoll < 18) {
            int previousRoll = rolls[currentRoll - 1];

            // Skip validation if previous roll was a strike
            if (previousRoll != 10 && previousRoll + pins > 10) {
                throw new IllegalArgumentException("Total pins in a frame cannot exceed 10");
            }
        }

        rolls[currentRoll++] = pins;
    }
        public int score() {
            int score = 0;
            int frameIndex = 0;

            for (int frame = 0; frame < 10; frame++) {
                if (isStrike(frameIndex)) {
                    score += 10 + strikeBonus(frameIndex);
                    frameIndex += 1;
                } else if (isSpare(frameIndex)) {
                    score += 10 + spareBonus(frameIndex);
                    frameIndex += 2;
                } else {
                    score += sumOfBallsInFrame(frameIndex);
                    frameIndex += 2;
                }
            }

            return score;
        }

    private boolean isStrike(int frameIndex) {
        return rolls[frameIndex] == 10;
    }

    private boolean isSpare(int frameIndex) {
        return rolls[frameIndex] + rolls[frameIndex + 1] == 10;
    }

    private int strikeBonus(int frameIndex) {
        return rolls[frameIndex + 1] + rolls[frameIndex + 2];
    }

    private int spareBonus(int frameIndex) {
        return rolls[frameIndex + 2];
    }

    private int sumOfBallsInFrame(int frameIndex) {
        return rolls[frameIndex] + rolls[frameIndex + 1];
    }

//    public boolean isGameOver() {
//        return currentRoll >= 20 || (currentRoll >= 18 && rolls[18] == 10);
//    }

    public boolean isGameOver() {
        if (currentRoll < 18) {
            return false; // Not yet in the 10th frame
        }

        int firstTenth = rolls[18];
        int secondTenth = rolls[19];

        // Strike in 10th frame
        if (firstTenth == 10) {
            return currentRoll >= 21;
        }

        // Spare in 10th frame
        if (firstTenth + secondTenth == 10) {
            return currentRoll >= 21;
        }

        // Open frame in 10th
        return currentRoll >= 20;
    }


    public List<Frame> getFrames() {
        List<Frame> frames = new ArrayList<>();
        int frameIndex = 0;

        for (int frame = 0; frame < 10; frame++) {
            Frame f = new Frame();
            List<String> rollSymbols = new ArrayList<>();

            if (rolls[frameIndex] == 10) {
                rollSymbols.add("X");
                f.setScore(10 + rolls[frameIndex + 1] + rolls[frameIndex + 2]);
                frameIndex += 1;
            }

            else {
                int first = rolls[frameIndex];
                int second = rolls[frameIndex + 1];
                rollSymbols.add(String.valueOf(first));

                if (first + second == 10) {
                    rollSymbols.add("/");
                    f.setScore(10 + rolls[frameIndex + 2]);
                } else {
                    rollSymbols.add(String.valueOf(second));
                    f.setScore(first + second);
                }

                frameIndex += 2;
            }

            f.setRolls(rollSymbols);
            frames.add(f);
        }

        return frames;
    }
    }






