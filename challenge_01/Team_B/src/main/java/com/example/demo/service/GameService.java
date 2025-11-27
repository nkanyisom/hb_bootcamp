package com.example.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


@Slf4j
@Service
public class GameService {
    private final List<String> words = new ArrayList<>();
    private final Random rnd = new Random();

    public void load() throws Exception {

        try(InputStream in = getClass().getResourceAsStream("/words.txt")){
            if (in == null) throw new RuntimeException("words.txt not found");
            try (BufferedReader br = new BufferedReader(new InputStreamReader(in))){
                br.lines().map(String::trim).filter(s -> !s.isEmpty()).forEach(words::add);
            }
        }
        if (words.isEmpty()) throw new RuntimeException("words list empty");
    }

    public String getRandomWord(int minLen, int maxLen) throws Exception {
        load();
        List<String> filtered = new ArrayList<>();
        for(String word : words){
            if(word.length() >= minLen && word.length() <= maxLen)
                filtered.add(word);
        }
        return filtered.get(rnd.nextInt(filtered.size()));
    }

    public String scramble(String word) {
        List<Character> c = new ArrayList<>();
        for(char ch : word.toCharArray()) c.add(ch);
        Collections.shuffle(c);
        StringBuilder sb = new StringBuilder();
        for(char ch : c) sb.append(ch);

        if(sb.toString().equals(word)) return scramble(word);
        return sb.toString();
    }

    public boolean inValidWord(String word) {
        return words.contains(word.toLowerCase());
    }
}