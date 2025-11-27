package com.example.demo.controller;

import com.example.demo.dto.GameStartResponse;
import com.example.demo.dto.AnagramCheckerRequest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.service.AnagramService;
import com.example.demo.service.GameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AnagramCheckerController {

    private final AnagramService anagramService;
    private final GameService gameService;

    @PostMapping(path = "/login", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<LoginResponse> checkAnagram(@RequestBody AnagramCheckerRequest anagramCheckerRequest) {
        var r = anagramService.isAnagram(anagramCheckerRequest);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setMessage(r ? "Valid" : "Invalid");
        return ResponseEntity.ok(loginResponse);
    }

    @GetMapping(path = "/game/start", produces = {"application/json"})
    public ResponseEntity<GameStartResponse> starGame() throws Exception {
        log.info("Anagram checker started");
        var word = gameService.getRandomWord(5,6);
        var scramble = gameService.scramble(word);
        return ResponseEntity.ok(GameStartResponse.builder().scrambled(scramble).length(scramble.length()).build());
    }

}