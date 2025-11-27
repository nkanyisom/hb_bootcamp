package main;

import anagram.AnagramDetector;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter first word/phrase:");
        String a = scanner.nextLine();
        System.out.println("Enter second word/phrase:");
        String b = scanner.nextLine();

        boolean result = AnagramDetector.isAnagram(a, b);
        if (result) {
            System.out.println(" They are anagrams!");
        } else {
            System.out.println(" They are NOT anagrams!");
        }
    }
}
