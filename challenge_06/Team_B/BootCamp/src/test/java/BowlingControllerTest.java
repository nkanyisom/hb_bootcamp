import bootcamp.bowling.BowlingController;
import bootcamp.bowling.BowlingGame;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BowlingController.class)
@ContextConfiguration(classes = {BowlingController.class, BowlingGame.class})
public class BowlingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Small helper class
    static class RollRequest {
        public int pins;
        public RollRequest(int pins) { this.pins = pins; }
        public int getPins() { return pins; }
    }

    // -------------------------------------------------------------
    // NEW GAME
    // -------------------------------------------------------------
    @Test
    void testNewGameCreatesPlayer() throws Exception {
        mockMvc.perform(post("/api/new-game/Alice"))
                .andExpect(status().isOk())
                .andExpect(content().string("New game started for Alice"));
    }


    // -------------------------------------------------------------
    // ROLL ENDPOINT
    // -------------------------------------------------------------
    @Test
    void testRollAccepted() throws Exception {
        mockMvc.perform(post("/api/new-game/Bob"));

        mockMvc.perform(post("/api/roll/Bob")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new RollRequest(5))))
                .andExpect(status().isOk())
                .andExpect(content().string("Roll accepted"));
    }

    @Test
    void testRollInvalidPlayer() throws Exception {
        mockMvc.perform(post("/api/roll/Unknown")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new RollRequest(5))))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Player not found"));
    }

    @Test
    void testRollInvalidPinsReturns400() throws Exception {
        mockMvc.perform(post("/api/new-game/Charlie"));

        mockMvc.perform(post("/api/roll/Charlie")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new RollRequest(20)))) // invalid
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Pins must be between 0 and 10")));
    }

    // -------------------------------------------------------------
    // GET SCORE
    // -------------------------------------------------------------
    @Test
    void testGetScoreForExistingPlayer() throws Exception {
        mockMvc.perform(post("/api/new-game/Dave"));

        mockMvc.perform(post("/api/roll/Dave")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new RollRequest(7))));
        mockMvc.perform(post("/api/roll/Dave")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new RollRequest(2))));

        mockMvc.perform(get("/api/score/Dave"))
                .andExpect(status().isOk())
                .andExpect(content().string("9"));
    }

    @Test
    void testGetScoreForUnknownPlayerReturnsZero() throws Exception {
        mockMvc.perform(get("/api/score/Nobody"))
                .andExpect(status().isOk())
                .andExpect(content().string("0"));
    }

    // -------------------------------------------------------------
    // GET FRAMES
    // -------------------------------------------------------------
    @Test
    void testGetFramesForExistingPlayer() throws Exception {
        mockMvc.perform(post("/api/new-game/Eve"));

        mockMvc.perform(post("/api/roll/Eve")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new RollRequest(10)))); // strike

        mockMvc.perform(get("/api/frames/Eve"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].rolls[0]", is("X")));  // first frame = strike
    }

    @Test
    void testGetFramesReturnsEmptyListForUnknownPlayer() throws Exception {
        mockMvc.perform(get("/api/frames/Unknown"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
