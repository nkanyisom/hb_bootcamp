package com.example.bowling.model;

import org.springframework.stereotype.Component;

@Component
public class BowlingGame {
    // Array size reduced as 10 frames * 2 rolls = 20 rolls max
    private final int[] rolls = new int[20];
    private int currentRoll = 0;
    private int currentFrame = 1;
    private static final int TOTAL_FRAMES = 10;

    public void roll(int pins) {
        if (isGameOver()) return;
        if (pins < 0 || pins > getPinsRemaining()) {
            throw new IllegalArgumentException("Invalid number of pins: " + pins + ". Only " + getPinsRemaining() + " pins remaining.");
        }

        rolls[currentRoll++] = pins;

        int rollsInFrame = currentRoll - getRollIndexForFrame(currentFrame);

        // Case 1: Strike on the first roll (Frame advances after one roll)
        if (rollsInFrame == 1 && pins == 10) {
            currentFrame++;
        }
        // Case 2: Second roll of the frame completed (Spare or Open Frame advances)
        else if (rollsInFrame == 2) {
            currentFrame++;
        }

        // Safety check for game end after the 10th frame
        if (currentFrame > TOTAL_FRAMES) {
            currentFrame = TOTAL_FRAMES + 1;
        }
    }

    // --- Scoring Logic (Simplified) ---

    public int score() {
        int score = 0;
        int rollIndex = 0;
        for (int frame = 1; frame <= TOTAL_FRAMES; frame++) {
            if (rollIndex >= currentRoll) break; // No more rolls made

            if (isStrike(rollIndex)) {
                score += 10 + strikeBonus(rollIndex);
                rollIndex += 1;
            } else if (isSpare(rollIndex)) {
                score += 10 + spareBonus(rollIndex);
                rollIndex += 2;
            } else {
                // Open frame or partial frame
                if (rollIndex + 1 >= currentRoll && currentFrame <= TOTAL_FRAMES) break;
                score += rolls[rollIndex] + rolls[rollIndex + 1];
                rollIndex += 2;
            }
        }
        return score;
    }

    // --- Helper Methods ---

    private boolean isStrike(int rollIndex) {
        if (rollIndex >= rolls.length || rollIndex >= currentRoll) return false;
        return rolls[rollIndex] == 10;
    }

    private boolean isSpare(int rollIndex) {
        if (rollIndex + 1 >= rolls.length || rollIndex + 1 >= currentRoll) return false;
        return rolls[rollIndex] + rolls[rollIndex + 1] == 10 && rolls[rollIndex] != 10;
    }

    private int strikeBonus(int rollIndex) {
        // Next two rolls are always at indices rollIndex + 1 and rollIndex + 2
        int r1 = rollIndex + 1 < currentRoll ? rolls[rollIndex + 1] : 0;
        int r2 = rollIndex + 2 < currentRoll ? rolls[rollIndex + 2] : 0;
        return r1 + r2;
    }

    private int spareBonus(int rollIndex) {
        // Next single roll is at index rollIndex + 2
        return rollIndex + 2 < currentRoll ? rolls[rollIndex + 2] : 0;
    }

    // --- Frame Indexing (Simplified) ---

    public int getRollIndexForFrame(int frame) {
        int rollIndex = 0;
        for (int f = 1; f < frame; f++) {
            if (rollIndex >= currentRoll) break;

            // In the simplified model, a frame is always 1 roll (strike) or 2 rolls (open/spare)
            if (isStrike(rollIndex)) {
                rollIndex += 1;
            } else {
                rollIndex += 2;
            }
        }
        return rollIndex;
    }

    public int getPinsRemaining() {
        if (isGameOver()) return 0;

        int frameStart = getRollIndexForFrame(currentFrame);
        int rollsInFrame = currentRoll - frameStart;

        if (rollsInFrame == 0) return 10; // First roll of any frame

        // Second roll: Pins remaining is 10 - first roll score
        if (rollsInFrame == 1 && rolls[frameStart] < 10) {
            return 10 - rolls[frameStart];
        }

        // Should not happen unless game logic error, but default for safety
        return 10;
    }

    // --- Getters ---

    public boolean isGameOver() {
        return currentFrame > TOTAL_FRAMES;
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public int[] getRolls() {
        return rolls;
    }

    // getFrameScore and getRollDisplay methods would also need minor simplification
    // to remove the specific 10th frame logic, but the core structure remains valid
    // based on the updated helper methods above.

    // ... (rest of the helper methods: getFrameScore, getRollDisplay, etc. from previous response)
    // Make sure they use the updated simplified logic for 10th frame (2 rolls max, no bonus checks).
    // The previous implementation is close; removing all logic that checks rollInFrame == 3 or looks for a bonus roll in frame 10 is the key.

    // As the full implementation is lengthy, I'll provide the simplified getRollDisplay for completeness:
    public String getRollDisplay(int frameNumber, int rollInFrame) {
        if (rollInFrame > 2) return ""; // MAX 2 ROLLS PER FRAME

        int rollIndex = getRollIndexForFrame(frameNumber);
        int index = rollIndex + rollInFrame - 1;

        if (index >= currentRoll) return "";

        int pins = rolls[index];

        if (rollInFrame == 1) {
            return pins == 10 ? "X" : (pins == 0 ? "-" : String.valueOf(pins));
        } else { // rollInFrame == 2
            int firstRoll = rolls[rollIndex];
            if (firstRoll == 10) return ""; // Strike on roll 1, roll 2 is skipped/empty
            if (firstRoll + pins == 10) {
                return "/";
            } else {
                return pins == 0 ? "-" : String.valueOf(pins);
            }
        }
    }

    public Integer getFrameScore(int frameNumber) {
        int rollIndex = 0;
        int cumulativeScore = 0;
        for (int f = 1; f <= TOTAL_FRAMES; f++) {
            if (rollIndex >= currentRoll) break;

            // Check if frame score can be calculated
            int rollsToAdvance = isStrike(rollIndex) ? 1 : 2;
            int requiredBonusRolls = 0;
            if (isStrike(rollIndex)) requiredBonusRolls = 2;
            else if (isSpare(rollIndex)) requiredBonusRolls = 1;

            if (rollIndex + rollsToAdvance + requiredBonusRolls > currentRoll) {
                if (f == frameNumber) return null;
            }

            int frameScore = 0;
            if (isStrike(rollIndex)) {
                frameScore = 10 + strikeBonus(rollIndex);
            } else if (isSpare(rollIndex)) {
                frameScore = 10 + spareBonus(rollIndex);
            } else {
                frameScore = rolls[rollIndex] + rolls[rollIndex + 1];
            }

            cumulativeScore += frameScore;

            if (f == frameNumber) {
                return cumulativeScore;
            }

            rollIndex += rollsToAdvance;
        }
        return null;
    }
}