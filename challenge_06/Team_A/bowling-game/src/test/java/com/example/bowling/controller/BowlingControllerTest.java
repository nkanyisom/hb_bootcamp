package com.example.bowling.controller;

import com.example.bowling.model.BowlingGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.mockito.Mockito;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.contains;

@WebMvcTest(BowlingController.class)
public class BowlingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BowlingController controller;

    // Although BowlingGame is instantiated internally, we can use a mock/spy
    // or rely on the real behavior for simple state checks. For complex switching
    // tests, we'll spy on the internal instances created by the controller.

    /** Reset controller state before each test due to stateful nature. */
    @BeforeEach
    void setup() {
        // Use the controller method to ensure clean state
        controller.resetAllPlayers();
    }

    @Test
    void testAddPlayers_FCFS_Order() throws Exception {
        // 1. Add Player Alice
        mockMvc.perform(get("/").param("player", "Alice"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("players", contains("Alice")));

        // 2. Add Player Bob (Bob is added after Alice, FCFS)
        mockMvc.perform(get("/").param("player", "Bob"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("players", contains("Alice", "Bob")));

        // 3. Adding Alice again does not change the order or count
        mockMvc.perform(get("/").param("player", "Alice"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("players", contains("Alice", "Bob")));
    }

    @Test
    void testPlayerGamesInitializedAndDisplayed() throws Exception {
        mockMvc.perform(get("/").param("player", "Charly"));

        // Verify controller state
        assertTrue(controller.playerGames.containsKey("Charly"));
        assertEquals(1, controller.playerGames.size());

        // Verify model attributes for the active player
        mockMvc.perform(get("/?player=Charly"))
                .andExpect(model().attribute("player", "Charly"))
                .andExpect(model().attributeExists("game"))
                .andExpect(model().attribute("currentFrame", 1));
    }

    // --- Turn Switching Logic Tests (Requires game state control) ---

    private void prepareSpiedGame(String player, int currentFrame, int pinsRemaining) {
        BowlingGame originalGame = controller.playerGames.get(player);
        if (originalGame == null) {
            // Ensure player exists and get the new instance
            controller.playerGames.put(player, new BowlingGame());
            originalGame = controller.playerGames.get(player);
        }

        // Create a spy and replace the object in the map
        BowlingGame spiedGame = Mockito.spy(originalGame);
        controller.playerGames.put(player, spiedGame);

        // Mock methods needed for turn switching test
        Mockito.when(spiedGame.getCurrentFrame()).thenReturn(currentFrame);
        Mockito.when(spiedGame.getPinsRemaining()).thenReturn(pinsRemaining);
    }

    @Test
    void testTurnSwitchesAfterFrameAdvancement() throws Exception {
        // Setup P1 and P2. P1 starts.
        mockMvc.perform(get("/").param("player", "P1")); // FCFS order: [P1]
        mockMvc.perform(get("/").param("player", "P2")); // FCFS order: [P1, P2]

        // 1. P1's turn (Frame 1). Mock P1's game to advance frame after roll.
        prepareSpiedGame("P1", 1, 10);
        BowlingGame p1GameSpy = controller.playerGames.get("P1");

        // Mock game state just before roll: F1
        Mockito.when(p1GameSpy.getCurrentFrame()).thenReturn(1);

        // Mock game state after roll: F2 (signaling frame advancement)
        Mockito.doAnswer(invocation -> {
            // Simulate frame advancement in the spy
            Mockito.when(p1GameSpy.getCurrentFrame()).thenReturn(2);
            return null;
        }).when(p1GameSpy).roll(Mockito.anyInt());

        // P1 rolls - Frame advances (1 -> 2), turn must switch to P2
        mockMvc.perform(post("/roll").param("player", "P1").param("pins", "9"))
                .andExpect(redirectedUrl("/?player=P2"));

        // 2. P2's turn (Frame 1). Mock P2's game to advance frame after roll.
        prepareSpiedGame("P2", 1, 10);
        BowlingGame p2GameSpy = controller.playerGames.get("P2");

        // Mock game state just before roll: F1
        Mockito.when(p2GameSpy.getCurrentFrame()).thenReturn(1);

        // Mock game state after roll: F2 (signaling frame advancement)
        Mockito.doAnswer(invocation -> {
            Mockito.when(p2GameSpy.getCurrentFrame()).thenReturn(2);
            return null;
        }).when(p2GameSpy).roll(Mockito.anyInt());

        // P2 rolls - Frame advances (1 -> 2), turn must switch back to P1
        mockMvc.perform(post("/roll").param("player", "P2").param("pins", "10"))
                .andExpect(redirectedUrl("/?player=P1"));
    }

    @Test
    void testTurnDoesNotSwitchIfFrameNotAdvanced() throws Exception {
        // Setup Solo Player "S"
        mockMvc.perform(get("/").param("player", "S"));

        // 1. S's turn (Frame 1). Mock S's game NOT to advance frame after roll 1.
        prepareSpiedGame("S", 1, 10);
        BowlingGame sGameSpy = controller.playerGames.get("S");

        // Mock game state to ensure it stays in F1 after roll
        Mockito.when(sGameSpy.getCurrentFrame()).thenReturn(1);
        Mockito.doAnswer(invocation -> {
            // Only pinsRemaining changes after roll, frame stays at 1
            Mockito.when(sGameSpy.getPinsRemaining()).thenReturn(5);
            return null;
        }).when(sGameSpy).roll(5);

        // S rolls 5 (R1) - Frame does NOT advance (1 -> 1), turn stays S
        mockMvc.perform(post("/roll").param("player", "S").param("pins", "5"))
                .andExpect(redirectedUrl("/?player=S")); // Stays S's turn

        // Verify current player is still S
        mockMvc.perform(get("/"))
                .andExpect(model().attribute("currentPlayerTurn", "S"));
    }

    // --- Error Handling and Integrity Tests ---

    @Test
    void testRollOutOfTurn() throws Exception {
        // Setup X and Y. X starts.
        mockMvc.perform(get("/").param("player", "X"));
        mockMvc.perform(get("/").param("player", "Y"));

        // Y attempts to roll when it's X's turn
        mockMvc.perform(post("/roll").param("player", "Y").param("pins", "5"))
                .andExpect(redirectedUrlPattern("/?player=X*error=It+is+not+your+turn*"));
    }

    @Test
    void testInvalidPinsRollRedirectsWithError() throws Exception {
        // Setup Player Z.
        mockMvc.perform(get("/").param("player", "Z"));

        // Mock the game to throw an IllegalArgumentException for invalid pins
        prepareSpiedGame("Z", 1, 10);
        BowlingGame zGameSpy = controller.playerGames.get("Z");

        Mockito.doThrow(new IllegalArgumentException("Invalid number of pins: 11. Only 10 pins remaining."))
                .when(zGameSpy).roll(11);

        // Z attempts to roll invalid pins (should throw and redirect)
        mockMvc.perform(post("/roll").param("player", "Z").param("pins", "11"))
                .andExpect(redirectedUrlPattern("/?player=Z*error=Invalid+number+of+pins*"));

        // Turn should remain with Z after the error
        mockMvc.perform(get("/"))
                .andExpect(model().attribute("currentPlayerTurn", "Z"));
    }

    @Test
    void testRollWhenGameOverRedirectsWithError() throws Exception {
        // Setup Player G.
        mockMvc.perform(get("/").param("player", "G"));

        // Mock the game to be over
        prepareSpiedGame("G", 11, 0); // Frame 11 implies game over
        BowlingGame gGameSpy = controller.playerGames.get("G");

        Mockito.when(gGameSpy.isGameOver()).thenReturn(true);

        // G attempts to roll when game is over
        mockMvc.perform(post("/roll").param("player", "G").param("pins", "5"))
                .andExpect(redirectedUrlPattern("/?player=G*error=Game+is+over+for+this+player*"));
    }

    // --- Utility Methods Tests ---

    @Test
    void testReset() throws Exception {
        mockMvc.perform(get("/").param("player", "R"))
                .andExpect(status().isOk());

        // R rolls a strike (Game state changes)
        controller.playerGames.get("R").roll(10);
        assertEquals(2, controller.playerGames.get("R").getCurrentFrame());

        // Reset R's game
        mockMvc.perform(post("/reset").param("player", "R"))
                .andExpect(redirectedUrl("/?player=R"));

        // Verify R's game is back to Frame 1
        mockMvc.perform(get("/?player=R"))
                .andExpect(model().attribute("currentFrame", 1));
    }

    @Test
    void testDeletePlayer_CurrentPlayer() throws Exception {
        // Setup A, B. A starts.
        mockMvc.perform(get("/").param("player", "A"));
        mockMvc.perform(get("/").param("player", "B"));

        // Delete A (current player)
        mockMvc.perform(post("/delete").param("player", "A"))
                .andExpect(redirectedUrl("/?player=B")); // Turn should go to B

        // Verify internal state: Order is [B], current index 0
        assertEquals(1, controller.playerOrder.size());
        assertEquals("B", controller.playerOrder.get(0));
        assertEquals(0, controller.currentPlayerIndex);
    }

    @Test
    void testDeletePlayer_MiddlePlayer() throws Exception {
        // Setup A, B, C. A starts.
        mockMvc.perform(get("/").param("player", "A"));
        mockMvc.perform(get("/").param("player", "B"));
        mockMvc.perform(get("/").param("player", "C"));

        // Delete B (middle player). A keeps the turn.
        mockMvc.perform(post("/delete").param("player", "B"))
                .andExpect(redirectedUrl("/?player=A")); // A keeps turn since index was adjusted

        // Verify internal state: Order is [A, C], current index 0
        assertEquals(2, controller.playerOrder.size());
        assertEquals("A", controller.playerOrder.get(0));
        assertEquals("C", controller.playerOrder.get(1));
        assertEquals(0, controller.currentPlayerIndex);
    }

    @Test
    void testResetAllPlayers() throws Exception {
        mockMvc.perform(get("/").param("player", "P1"));
        mockMvc.perform(get("/").param("player", "P2"));

        // Verify 2 players exist
        assertEquals(2, controller.playerGames.size());

        mockMvc.perform(post("/resetAll"))
                .andExpect(redirectedUrl("/"));

        // Verify all state is cleared
        assertEquals(0, controller.playerGames.size());
        assertTrue(controller.playerOrder.isEmpty());
        assertEquals(0, controller.currentPlayerIndex);
    }
}