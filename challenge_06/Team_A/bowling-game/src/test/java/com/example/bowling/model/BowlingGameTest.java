package com.example.bowling.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the BowlingGame model logic with the simplified rules:
 * - Max 2 rolls per frame (including frame 10).
 * - Strike advances frame after 1 roll (F1-F10).
 * - Spare/Open advances frame after 2 rolls (F1-F10).
 * - Max 20 rolls total.
 */
public class BowlingGameTest {

    private BowlingGame game;

    @BeforeEach
    void setUp() {
        game = new BowlingGame();
    }

    /** Helper method to simulate rolling a specified number of times with the same pins. */
    private void rollMany(int rolls, int pins) {
        for (int i = 0; i < rolls; i++) {
            game.roll(pins);
        }
    }

    /** Helper method to simulate rolling a spare (pins1 + pins2 = 10). */
    private void rollSpare() {
        game.roll(5);
        game.roll(5);
    }

    /** Helper method to simulate rolling a strike (10 pins on first roll). */
    private void rollStrike() {
        game.roll(10);
    }

    // --- Core Scoring Tests (Simplified Rules) ---

    @Test
    void testGutterGame() {
        // 10 frames * 2 rolls = 20 rolls of 0 pins
        rollMany(20, 0);
        assertEquals(0, game.score(), "Gutter game score should be 0.");
        assertTrue(game.isGameOver(), "Game should be over after 20 rolls.");
    }

    @Test
    void testAllOnes() {
        // 20 rolls of 1 pin
        rollMany(20, 1);
        assertEquals(20, game.score(), "All ones score should be 20.");
        assertTrue(game.isGameOver());
    }

    @Test
    void testSingleSpare() {
        rollSpare(); // Frame 1 (10 + bonus)
        game.roll(3);  // Bonus roll
        rollMany(17, 0); // Remaining 8 frames + 1 roll of F2
        // F1: (5 + 5) + 3 = 13. F2: 3 + 0 = 3. Total: 16.
        assertEquals(16, game.score(), "Single spare with bonus of 3 should score 16.");
    }

    @Test
    void testSingleStrike() {
        rollStrike(); // Frame 1 (10 + 3 + 4 = 17)
        game.roll(3);  // Bonus roll 1
        game.roll(4);  // Bonus roll 2
        rollMany(16, 0); // Remaining 8 frames + 1 roll of F2
        // F1: 10 + 3 + 4 = 17. F2: 3 + 4 = 7. Total: 24.
        assertEquals(24, game.score(), "Single strike with bonuses 3, 4 should score 24.");
    }

    @Test
    void testPerfectGame_Simplified() {
        // 10 Strikes (10 rolls total)
        for (int i = 0; i < 10; i++) {
            rollStrike();
        }

        // F1: 10 + 10 + 10 = 30
        // F2 - F8 (7 frames): 7 * 30 = 210
        // F9: 10 + 10 + 0 = 20 (Bonus 2nd roll is missing as game ends after R10)
        // F10: 10 + 0 + 0 = 10 (No bonus rolls allowed)
        // Total: 8 * 30 + 20 + 10 = 270.
        // Let's re-verify the score calculation:
        // F1: 10 + (R2 10) + (R3 10) = 30
        // F9: 10 + (R10 10) + (R11 0) = 20
        // F10: 10 (R10) + 0 (R11) = 10. Wait, F10 uses 2 rolls.
        // Rolls: [10] [10] [10] [10] [10] [10] [10] [10] [10] [10]
        // F1 (R1): 10 + R2 + R3 = 30.
        // F9 (R9): 10 + R10 + R11(0) = 20. (Only 10 rolls made)
        // F10 (R10): 10. (Next roll R11 doesn't exist for bonus, and F10 only takes one roll if a strike).
        // Total score is 9 * 30 + 0 = 270.
        assertEquals(270, game.score(), "10 strikes (R1-R10) should score 270 in simplified mode.");
    }

    @Test
    void testAllSparesGame() {
        // 10 frames of 5|5 (20 rolls total)
        for (int i = 0; i < 10; i++) {
            rollSpare();
        }
        // Last roll is R20, which is the 2nd roll of F10. No bonus rolls after R20.
        // F1-F9: (5 + 5) + 5 (next roll) = 15. (9 * 15 = 135)
        // F10: (5 + 5) = 10.
        // Total: 135 + 10 = 145.
        assertEquals(145, game.score(), "All spares (5|5) should score 145 in simplified mode.");
    }

    // --- Frame and State Tests ---

    @Test
    void testFrameAdvancement_Strike() {
        assertEquals(1, game.getCurrentFrame());
        rollStrike(); // R1
        assertEquals(2, game.getCurrentFrame(), "Frame should advance after a strike.");
        rollStrike(); // R2
        assertEquals(3, game.getCurrentFrame(), "Frame should advance again after a strike.");
    }

    @Test
    void testFrameAdvancement_OpenFrame() {
        assertEquals(1, game.getCurrentFrame());
        game.roll(4); // R1
        assertEquals(1, game.getCurrentFrame(), "Frame should not advance after 1st roll of open frame.");
        game.roll(5); // R2
        assertEquals(2, game.getCurrentFrame(), "Frame should advance after 2nd roll of open frame.");
    }

    @Test
    void testFrameAdvancement_Spare() {
        assertEquals(1, game.getCurrentFrame());
        game.roll(7); // R1
        assertEquals(1, game.getCurrentFrame(), "Frame should not advance after 1st roll of spare.");
        game.roll(3); // R2 (Spare)
        assertEquals(2, game.getCurrentFrame(), "Frame should advance after a spare.");
    }

    @Test
    void testPinsRemaining() {
        game.roll(3);
        assertEquals(7, game.getPinsRemaining(), "Pins remaining should be 7 after rolling 3.");
        game.roll(5);
        assertEquals(10, game.getPinsRemaining(), "Pins remaining should reset to 10 after frame advances (F2 R1).");
        rollStrike();
        assertEquals(10, game.getPinsRemaining(), "Pins remaining should reset to 10 after strike (F3 R1).");
    }

    @Test
    void testGameEndsExactlyAfter20Rolls() {
        rollMany(19, 1);
        assertFalse(game.isGameOver(), "Game should not be over before the final roll.");
        game.roll(1);
        assertTrue(game.isGameOver(), "Game should be over exactly after the 20th roll.");

        // Attempt an extra roll
        assertThrows(IllegalArgumentException.class, () -> game.roll(1), "Roll after game over should throw exception.");
    }

    @Test
    void testGameEndsExactlyAfter10Strikes() {
        rollMany(10, 10);
        assertTrue(game.isGameOver(), "Game should be over after 10 strikes (10 rolls total).");

        // Ensure game is over even though only 10 rolls were made
        assertEquals(11, game.getCurrentFrame());
    }
}
