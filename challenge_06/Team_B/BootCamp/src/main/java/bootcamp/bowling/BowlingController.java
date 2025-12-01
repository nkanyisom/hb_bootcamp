package bootcamp.bowling;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin // Allow frontend to access API

public class BowlingController {

    //private BowlingGame game = new BowlingGame();
    private Map<String, BowlingGame> players = new HashMap<>();


    @PostMapping("/new-game/{player}")
    public String newGame(@PathVariable String player) {
        players.put(player, new BowlingGame());
        return "New game started for " + player;
    }

    @PostMapping("/roll/{player}")
    public ResponseEntity<String> roll(@PathVariable String player, @RequestBody RollRequest request) {
        BowlingGame game = players.get(player);
        if (game == null) {
            return ResponseEntity.badRequest().body("Player not found");
        }

        try {
            game.roll(request.getPins());
            return ResponseEntity.ok("Roll accepted");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/score/{player}")
    public int getScore(@PathVariable String player) {
        BowlingGame game = players.get(player);
        return (game != null) ? game.score() : 0;
    }

    @GetMapping("/frames/{player}")
    public List<Frame> getFrames(@PathVariable String player) {
        BowlingGame game = players.get(player);
        return (game != null) ? game.getFrames() : new ArrayList<>();
    }

}


