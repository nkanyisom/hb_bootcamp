import bootcamp.bowling.BowlingGame;
import bootcamp.bowling.Frame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class BowlingGameTest {

    private BowlingGame game;

    @BeforeEach
    void setUp() {
        game = new BowlingGame();
    }

    private void rollMany(int n, int pins) {
        for (int i = 0; i < n; i++) {
            game.roll(pins);
        }
    }

    private void roll(int... rolls) {
        for (int pins : rolls) {
            game.roll(pins);
        }
    }

    // ------------------------------------------------------------
    // Scoring Tests
    // ------------------------------------------------------------

    @Test
    void testGutterGame() {
        rollMany(20, 0);
        assertEquals(0, game.score());
    }

    @Test
    void testAllOnes() {
        rollMany(20, 1);
        assertEquals(20, game.score());
    }

    @Test
    void testSingleSpare() {
        roll(5, 5, 3); // spare + 3 bonus
        rollMany(17, 0);
        assertEquals(16, game.score());
    }

    @Test
    void testSingleStrike() {
        roll(10, 3, 4);
        rollMany(16, 0);
        assertEquals(24, game.score());
    }

    @Test
    void testPerfectGame() {
        rollMany(12, 10);
        assertEquals(300, game.score());
    }

    // ------------------------------------------------------------
    // Validation Tests
    // ------------------------------------------------------------

    @Test
    void testIllegalRollNegative() {
        assertThrows(IllegalArgumentException.class, () -> game.roll(-1));
    }

    @Test
    void testIllegalRollGreaterThanTen() {
        assertThrows(IllegalArgumentException.class, () -> game.roll(11));
    }

    @Test
    void testFrameExceedsTenPins() {
        game.roll(6);
        assertThrows(IllegalArgumentException.class, () -> game.roll(5));
    }

    @Test
    void testAllowStrikeThenNormalRoll() {
        game.roll(10);
        assertDoesNotThrow(() -> game.roll(7));
    }

    @Test
    void testRollingAfterGameOverThrows() {
        rollMany(20, 1);
        assertTrue(game.isGameOver());
        assertThrows(IllegalArgumentException.class, () -> game.roll(1));
    }

    // ------------------------------------------------------------
    // 10th Frame Behavior
    // ------------------------------------------------------------

    @Test
    void test10thFrameOpenFrameEndsAt20Rolls() {
        rollMany(18, 0);
        roll(3, 4);
        assertTrue(game.isGameOver());
    }

    @Test
    void test10thFrameSpareAllowsExtraRoll() {
        rollMany(18, 0);
        roll(5, 5); // spare
        assertFalse(game.isGameOver());
        roll(7);
        assertTrue(game.isGameOver());
    }

    @Test
    void test10thFrameStrikeAllowsTwoExtraRolls() {
        rollMany(18, 0);
        roll(10);
        assertFalse(game.isGameOver());
        roll(3);
        roll(4);
        assertTrue(game.isGameOver());
    }

    // ------------------------------------------------------------
    // Frame List Tests
    // ------------------------------------------------------------

    @Test
    void testFramesReturn10Frames() {
        rollMany(20, 0);
        List<Frame> frames = game.getFrames();
        assertEquals(10, frames.size());
    }

    @Test
    void testFrameSymbolsStrike() {
        roll(10, 3, 4);
        rollMany(16, 0);
        List<Frame> frames = game.getFrames();

        assertEquals("X", frames.get(0).getRolls().get(0));
        assertEquals(17, frames.get(0).getScore()); // 10 + 3 + 4
    }

    @Test
    void testFrameSymbolsSpare() {
        roll(6, 4, 5);
        rollMany(17, 0);

        List<Frame> frames = game.getFrames();
        assertEquals("6", frames.get(0).getRolls().get(0));
        assertEquals("/", frames.get(0).getRolls().get(1));
        assertEquals(15, frames.get(0).getScore()); // 10 + 5 bonus
    }

    @Test
    void testFrameSymbolsOpenFrame() {
        roll(3, 4);
        rollMany(18, 0);
        List<Frame> frames = game.getFrames();

        assertEquals("3", frames.get(0).getRolls().get(0));
        assertEquals("4", frames.get(0).getRolls().get(1));
        assertEquals(7, frames.get(0).getScore());
    }
}
