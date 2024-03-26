import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Your implementations of various string searching algorithms.
 *
 * @author Madison Schlaff
 * @version 1.0
 *
 */
public class PatternMatching {

    /**
     * Knuth-Morris-Pratt (KMP) algorithm relies on the failure table (also
     * called failure function). Works better with small alphabets.
     *
     * Make sure to implement the buildFailureTable() method before implementing
     * this method.
     *
     * @param pattern    the pattern you are searching for in a body of text
     * @param text       the body of text where you search for pattern
     * @param comparator you MUST use this to check if characters are equal
     * @return list containing the starting index for each match found
     * @throws java.lang.IllegalArgumentException if the pattern is null or has
     *                                            length 0
     * @throws java.lang.IllegalArgumentException if text or comparator is null
     */
    public static List<Integer> kmp(CharSequence pattern, CharSequence text,
                                    CharacterComparator comparator) {
        if (pattern == null || pattern.length() == 0) {
            throw new IllegalArgumentException("Pattern is null or length 0.");
        } else if (text == null || comparator == null) {
            throw new IllegalArgumentException("Text or comparator is null.");
        }

        List<Integer> indexList = new ArrayList<>();

        if (pattern.length() > text.length()) {
            return indexList;
        }

        int[] table = buildFailureTable(pattern, comparator);
        int index = 0;
        int patternIndex = 0;

        while (index + pattern.length() <= text.length()) {
            while (patternIndex < pattern.length()
                    && comparator.compare(pattern.charAt(patternIndex),
                    text.charAt(index + patternIndex)) == 0) {
                patternIndex++;
            }

            if (patternIndex == 0) {
                index++;
            } else {
                if (patternIndex == pattern.length()) {
                    indexList.add(index);
                }
                index += patternIndex - table[patternIndex - 1];
                patternIndex = table[patternIndex - 1];
            }
        }
        return indexList;
    }

    /**
     * Builds failure table that will be used to run the Knuth-Morris-Pratt
     * (KMP) algorithm.
     *
     * The table built should be the length of the input pattern.
     *
     * Note that a given index i will contain the length of the largest prefix
     * of the pattern indices [0..i] that is also a suffix of the pattern
     * indices [1..i]. This means that index 0 of the returned table will always
     * be equal to 0
     *
     * Ex.
     * pattern:       a  b  a  b  a  c
     * failureTable: [0, 0, 1, 2, 3, 0]
     *
     * If the pattern is empty, return an empty array.
     *
     * @param pattern    a pattern you're building a failure table for
     * @param comparator you MUST use this to check if characters are equal
     * @return integer array holding your failure table
     * @throws java.lang.IllegalArgumentException if the pattern or comparator
     *                                            is null
     */
    public static int[] buildFailureTable(CharSequence pattern,
                                          CharacterComparator comparator) {
        if (comparator == null || pattern == null) {
            throw new IllegalArgumentException("Pattern or comparator is null.");
        }

        int[] table = new int[pattern.length()];

        if (pattern.length() != 0) {
            table[0] = 0;
        }

        int idx = 0;
        int jidx = 1;

        while (jidx < pattern.length()) {
            if (comparator.compare(pattern.charAt(jidx), pattern.charAt(idx)) == 0) {
                table[jidx++] = ++idx;
            } else {
                if (idx != 0) {
                    idx = table[idx - 1];
                } else {
                    table[jidx++] = idx;
                }
            }
        }
        return table;
    }

    /**
     * Boyer Moore algorithm that relies on last occurrence table. Works better
     * with large alphabets.
     *
     * Make sure to implement the buildLastTable() method before implementing
     * this method. Do NOT implement the Galil Rule in this method.
     *
     * Note: You may find the getOrDefault() method from Java's Map class
     * useful.
     *
     * @param pattern    the pattern you are searching for in a body of text
     * @param text       the body of text where you search for the pattern
     * @param comparator you MUST use this to check if characters are equal
     * @return list containing the starting index for each match found
     * @throws java.lang.IllegalArgumentException if the pattern is null or has
     *                                            length 0
     * @throws java.lang.IllegalArgumentException if text or comparator is null
     */
    public static List<Integer> boyerMoore(CharSequence pattern,
                                           CharSequence text,
                                           CharacterComparator comparator) {
        if (pattern == null || pattern.length() == 0) {
            throw new IllegalArgumentException("Pattern is null or length 0.");
        } else if (text == null || comparator == null) {
            throw new IllegalArgumentException("Text or comparator is null.");
        }

        List<Integer> indexList = new ArrayList<>();
        Map<Character, Integer> table = null;
        if (pattern.length() <= text.length()) {
            table = buildLastTable(pattern);
        }

        int index = 0;

        while (index + pattern.length() <= text.length()) {
            int patternIndex = pattern.length() - 1;

            while (patternIndex >= 0 && comparator.compare(
                    text.charAt(index + patternIndex),
                    pattern.charAt(patternIndex)) == 0) {
                patternIndex--;
            }
            if (patternIndex < 0) {
                indexList.add(index++);
            } else {
                char lastChar = text.charAt(index + pattern.length() - 1);
                int shift = table.getOrDefault(lastChar, -1);
                if (shift < patternIndex) {
                    index += patternIndex - shift;
                } else {
                    index += 1;
                }
            }
        }
        return indexList;
    }

    /**
     * Builds last occurrence table that will be used to run the Boyer Moore
     * algorithm.
     *
     * Note that each char x will have an entry at table.get(x).
     * Each entry should be the last index of x where x is a particular
     * character in your pattern.
     * If x is not in the pattern, then the table will not contain the key x,
     * and you will have to check for that in your Boyer Moore implementation.
     *
     * Ex. pattern = octocat
     *
     * table.get(o) = 3
     * table.get(c) = 4
     * table.get(t) = 6
     * table.get(a) = 5
     * table.get(everything else) = null, which you will interpret in
     * Boyer-Moore as -1
     *
     * If the pattern is empty, return an empty map.
     *
     * @param pattern a pattern you are building last table for
     * @return a Map with keys of all of the characters in the pattern mapping
     * to their last occurrence in the pattern
     * @throws java.lang.IllegalArgumentException if the pattern is null
     */
    public static Map<Character, Integer> buildLastTable(CharSequence pattern) {
        if (pattern == null) {
            throw new IllegalArgumentException("Pattern is null.");
        }

        Map<Character, Integer> table = new HashMap<>();

        for (int i = 0; i < pattern.length(); i++) {
            table.put(pattern.charAt(i), i);
        }
        return table;
    }

    /**
     * Prime base used for Rabin-Karp hashing.
     * DO NOT EDIT!
     */
    private static final int BASE = 113;

    /**
     * Runs the Rabin-Karp algorithm. This algorithms generates hashes for the
     * pattern and compares this hash to substrings of the text before doing
     * character by character comparisons.
     *
     * When the hashes are equal and you do character comparisons, compare
     * starting from the beginning of the pattern to the end, not from the end
     * to the beginning.
     *
     * You must use the Rabin-Karp Rolling Hash for this implementation. The
     * formula for it is:
     *
     * sum of: c * BASE ^ (pattern.length - 1 - i)
     *   c is the integer value of the current character, and
     *   i is the index of the character
     *
     * We recommend building the hash for the pattern and the first m characters
     * of the text by starting at index (m - 1) to efficiently exponentiate the
     * BASE. This allows you to avoid using Math.pow().
     *
     * Note that if you were dealing with very large numbers here, your hash
     * will likely overflow; you will not need to handle this case.
     * You may assume that all powers and calculations CAN be done without
     * overflow. However, be careful with how you carry out your calculations.
     * For example, if BASE^(m - 1) is a number that fits into an int, it's
     * possible for BASE^m will overflow. So, you would not want to do
     * BASE^m / BASE to calculate BASE^(m - 1).
     *
     * Ex. Hashing "bunn" as a substring of "bunny" with base 113
     * = (b * 113 ^ 3) + (u * 113 ^ 2) + (n * 113 ^ 1) + (n * 113 ^ 0)
     * = (98 * 113 ^ 3) + (117 * 113 ^ 2) + (110 * 113 ^ 1) + (110 * 113 ^ 0)
     * = 142910419
     *
     * Another key point of this algorithm is that updating the hash from
     * one substring to the next substring must be O(1). To update the hash,
     * subtract the oldChar times BASE raised to the length - 1, multiply by
     * BASE, and add the newChar as shown by this formula:
     * (oldHash - oldChar * BASE ^ (pattern.length - 1)) * BASE + newChar
     *
     * Ex. Shifting from "bunn" to "unny" in "bunny" with base 113
     * hash("unny") = (hash("bunn") - b * 113 ^ 3) * 113 + y
     *              = (142910419 - 98 * 113 ^ 3) * 113 + 121
     *              = 170236090
     *
     * Keep in mind that calculating exponents is not O(1) in general, so you'll
     * need to keep track of what BASE^(m - 1) is for updating the hash.
     *
     * Do NOT use Math.pow() in this method.
     * Do NOT implement your own version of Math.pow().
     *
     * @param pattern    a string you're searching for in a body of text
     * @param text       the body of text where you search for pattern
     * @param comparator you MUST use this to check if characters are equal
     * @return list containing the starting index for each match found
     * @throws java.lang.IllegalArgumentException if the pattern is null or has
     *                                            length 0
     * @throws java.lang.IllegalArgumentException if text or comparator is null
     */
    public static List<Integer> rabinKarp(CharSequence pattern,
                                          CharSequence text,
                                          CharacterComparator comparator) {
        if (pattern == null || pattern.length() == 0 || text == null || comparator == null) {
            throw new IllegalArgumentException("Pattern, text, or comparator is null or length is 0.");
        }

        List<Integer> indexList = new ArrayList<>();
        int patLen = pattern.length();
        int textLen = text.length();

        if (patLen > textLen) {
            return indexList;
        }

        long hash1 = hash(pattern);
        long hash2 = hash(text.subSequence(0, patLen));

        long runningPower = 1;
        for (int i = 0; i < patLen - 1; i++) {
            runningPower = (runningPower * BASE) % Integer.MAX_VALUE;
        }

        for (int i = 0; i <= textLen - patLen; i++) {
            if (hash1 == hash2 && equal(pattern, text, i, comparator)) {
                indexList.add(i);
            }
            if (i < textLen - patLen) {
                hash2 = (hash2 - (text.charAt(i) * runningPower) % Integer.MAX_VALUE) * BASE + text.charAt(i + patLen);
                hash2 %= Integer.MAX_VALUE;
                if (hash2 < 0) {
                    hash2 += Integer.MAX_VALUE;
                }
            }
        }
        return indexList;
    }

    /**
     * Helper function to check if text = pattern
     *
     * @param pattern pattern looking for
     * @param text string searching in
     * @param startIndex starting index
     * @param comparator equality comparator
     * @return true if pattern = text, false otherwise
     */
    private static boolean equal(CharSequence pattern, CharSequence text,
                                 int startIndex,
                                 CharacterComparator comparator) {
        for (int i = 0; i < pattern.length(); i++) {
            if (comparator.compare(pattern.charAt(i), text.charAt(startIndex + i)) != 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Helper function to find hash value
     *
     * @param text string to be hashed
     * @return hash value
     */
    private static long hash(CharSequence text) {
        long temp = 0;
        long runningPower = 1;

        for (int i = 0; i < text.length(); i++) {
            temp = (temp * BASE + text.charAt(i)) % Integer.MAX_VALUE;
            if (i < text.length() - 1) {
                runningPower = (runningPower * BASE) % Integer.MAX_VALUE;
            }
        }
        return temp;
    }

    /**
     * This method is OPTIONAL and for extra credit only.
     *
     * The Galil Rule is an addition to Boyer Moore that optimizes how we shift the pattern
     * after a full match. Please read Extra Credit: Galil Rule section in the homework pdf for details.
     *
     * Make sure to implement the buildLastTable() method and buildFailureTable() method
     * before implementing this method.
     *
     * @param pattern    the pattern you are searching for in a body of text
     * @param text       the body of text where you search for the pattern
     * @param comparator you MUST use this to check if characters are equal
     * @return list containing the starting index for each match found
     * @throws java.lang.IllegalArgumentException if the pattern is null or has
     *                                            length 0
     * @throws java.lang.IllegalArgumentException if text or comparator is null
     */
    public static List<Integer> boyerMooreGalilRule(CharSequence pattern,
                                          CharSequence text,
                                          CharacterComparator comparator) {
        if (pattern == null || pattern.length() == 0 || text == null || comparator == null) {
            throw new IllegalArgumentException("Pattern, text, or comparator is null or length is 0.");
        }

        List<Integer> indexList = new ArrayList<>();
        Map<Character, Integer> lastTable = buildLastTable(pattern);
        int[] failureTable = buildFailureTable(pattern, comparator);
        int index = 0;

        while (index + pattern.length() <= text.length()) {
            int patternIndex = pattern.length() - 1;

            while (patternIndex >= 0 && comparator.compare(
                    text.charAt(index + patternIndex),
                    pattern.charAt(patternIndex)) == 0) {
                patternIndex--;
            }

            if (patternIndex < 0) {
                indexList.add(index++);
            } else {
                char lastChar = text.charAt(index + patternIndex);
                int lastTableShift = lastTable.getOrDefault(lastChar, -1);
                int failureTableShift = failureTable[patternIndex];
                index += Math.max(lastTableShift, failureTableShift);
            }
        }
        return indexList;
    }
}
