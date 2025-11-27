package anagram;

import java.util.Arrays;

public final class AnagramDetector {
    private AnagramDetector() {}

    private static String normalize(String s) {
        if (s == null) return "";
        return s.replaceAll("[^A-Za-z0-9]", "").toLowerCase();
    }

    public static boolean isAnagram(String a, String b) {
        var s = normalize(a);
        var t = normalize(b);
        if (s.length() != t.length()) {
            return false;
        }

        char[] string1 = s.toCharArray();
        char[] string2 = t.toCharArray();

        Arrays.sort(string1);
        Arrays.sort(string2);

       return Arrays.equals(string1, string2);
    }
}
