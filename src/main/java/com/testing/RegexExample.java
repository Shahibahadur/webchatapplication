package com.testing;

public class RegexExample {
    public static void main(String[] args) {
        String input = "This is a sample [example] string with special characters like ( ) and $.";
System.out.println(input);
        // Without \Q and \E, escaping all special characters manually
        String patternWithoutQE = "This is a sample \\[example\\] string with special characters like \\( \\) and \\$.";        System.out.println(patternWithoutQE);
        System.out.println("Matches without \\Q and \\E: " + input.matches(patternWithoutQE)); // Output: true

        // With \Q and \E, no need to escape characters inside the quoted section
        String patternWithQE = "\\QThis is a sample [example] string with special characters like ( ) and $.\\E";
        System.out.println("Matches with \\Q and \\E: " + input.matches(patternWithQE)); // Output: true
        String clean_string = "hello\nWorld";
        clean_string= clean_string.replaceAll(clean_string, clean_string);
    }
}

