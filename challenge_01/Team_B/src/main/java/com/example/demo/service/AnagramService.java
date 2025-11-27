package com.example.demo.service;

import com.example.demo.dto.AnagramCheckerRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;


@Slf4j
@Service
public class AnagramService {

    public boolean isAnagram(AnagramCheckerRequest anagramCheckerRequest) {
        var normalizedInput1 = anagramCheckerRequest.getInput1().toLowerCase().replaceAll("[^a-z0-9]", "");
        var normalizedInput2 = anagramCheckerRequest.getInput2().toLowerCase().replaceAll("[^a-z0-9]", "");

        if (normalizedInput1.length() != normalizedInput2.length()) {
            return false;
        }

        char[] input1Chars = normalizedInput1.toCharArray();
        char[] input2Chars = normalizedInput2.toCharArray();

        Arrays.sort(input1Chars);
        Arrays.sort(input2Chars);
        return Arrays.equals(input1Chars, input2Chars);
    }
}