package com.example.bowling.controller;

import com.example.bowling.model.BowlingGame;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class BowlingController {
    final Map<String, BowlingGame> playerGames = new HashMap<>();
    final List<String> playerOrder = new ArrayList<>();
    int currentPlayerIndex = 0;

    @GetMapping("/")
    public String home(@RequestParam(value = "player", required = false) String requestedPlayer, Model model,
                       @RequestParam(value = "error", required = false) String error) {

        String activePlayer = null;

        if (requestedPlayer != null && !requestedPlayer.isBlank()) {
            playerGames.computeIfAbsent(requestedPlayer, p -> {
                if (!playerOrder.contains(p)) {
                    playerOrder.add(p);
                }
                return new BowlingGame();
            });
            activePlayer = requestedPlayer;
        }

        String currentPlayerTurn = null;
        if (!playerOrder.isEmpty()) {
            currentPlayerTurn = playerOrder.get(currentPlayerIndex);
        }

        if (activePlayer == null && currentPlayerTurn != null) {
            activePlayer = currentPlayerTurn;
        }

        model.addAttribute("players", playerOrder);
        model.addAttribute("currentPlayerTurn", currentPlayerTurn);

        if (activePlayer != null) {
            BowlingGame game = playerGames.get(activePlayer);
            model.addAttribute("player", activePlayer);
            model.addAttribute("game", game);
            model.addAttribute("pinsRemaining", game.getPinsRemaining());
            model.addAttribute("isGameOver", game.isGameOver());
            model.addAttribute("currentFrame", game.getCurrentFrame());
        }

        model.addAttribute("playerGames", playerGames);
        model.addAttribute("frameNumbers", IntStream.rangeClosed(1, 10).boxed().collect(Collectors.toList()));

        if (error != null) {
            model.addAttribute("error", error);
        }

        return "index";
    }

    @PostMapping("/roll")
    public String roll(@RequestParam("player") String player, @RequestParam("pins") int pins) {
        if (player == null || player.isBlank()) return "redirect:/";

        BowlingGame game = playerGames.get(player);
        if (game == null) return "redirect:/?error=Player+not+found";

        if (playerOrder.isEmpty() || !playerOrder.get(currentPlayerIndex).equals(player)) {
            String errorMsg = "It+is+not+your+turn.+Current+player+is+" + playerOrder.get(currentPlayerIndex);
            return "redirect:/?player=" + playerOrder.get(currentPlayerIndex) + "&error=" + errorMsg;
        }

        if (game.isGameOver()) return "redirect:/?player=" + player + "&error=Game+is+over+for+this+player";

        int frameBeforeRoll = game.getCurrentFrame();

        try {
            game.roll(pins);
        } catch (IllegalArgumentException e) {
            return "redirect:/?player=" + player + "&error=" + e.getMessage().replace(" ", "+");
        }

        boolean switchPlayer = false;

        // Player switch logic: Switch only if the frame advanced OR the game is over
        // This implicitly enforces: 1 roll (strike) or 2 rolls (open/spare) then switch.
        if (game.isGameOver() || game.getCurrentFrame() > frameBeforeRoll) {
            switchPlayer = true;
        }

        String nextPlayerName = player;

        if (switchPlayer) {
            if (playerOrder.size() > 1) {
                currentPlayerIndex = (currentPlayerIndex + 1) % playerOrder.size();
            }
        }
        // Redirect to the player whose turn it *is* now (even if it's the same player after a reset/delete)
        nextPlayerName = playerOrder.get(currentPlayerIndex);

        return "redirect:/?player=" + nextPlayerName;
    }

    // Reset, Delete, ResetAll methods remain the same as they correctly handle FCFS order list.

    @PostMapping("/reset")
    public String reset(@RequestParam("player") String player) {
        if (playerGames.containsKey(player)) {
            playerGames.put(player, new BowlingGame());
        }
        return "redirect:/?player=" + player;
    }

    @PostMapping("/delete")
    public String deletePlayer(@RequestParam String player) {
        playerGames.remove(player);
        int oldIndex = playerOrder.indexOf(player);
        playerOrder.remove(player);

        if (playerOrder.isEmpty()) {
            currentPlayerIndex = 0;
            return "redirect:/";
        }

        if (oldIndex <= currentPlayerIndex) {
            currentPlayerIndex = currentPlayerIndex % playerOrder.size();
        }

        String nextPlayer = playerOrder.get(currentPlayerIndex);
        return "redirect:/?player=" + nextPlayer;
    }

    @PostMapping("/resetAll")
    public String resetAllPlayers() {
        playerGames.clear();
        playerOrder.clear();
        currentPlayerIndex = 0;

        return "redirect:/";
    }
}