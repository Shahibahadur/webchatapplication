package com.testing;

public class AdvancedPalindromeChecker {
	public static void main(String[] args) {
		String original = "A man , a plan, a canal, Panama]";
		String cleaned = original.replaceAll("[^a-zA-Z]", "");
	    System.out.println(cleaned);
	    //^  reprsent not these character , replace the other character
	    String regex = "[^\\[\\]a-zA-Z]";
	    //[] are used to define special character class, which is a special construct in rgex
	    // here \\ is used to to translate single back slash \ in the regular expression
	    //then single back slash in regular expression is then used to escape special 
	    //character character like [,],^
	    //if ^ is not at the beginning of character class treated as literal character
	    System.out.println(regex);

	    String unicodeString = "The unicode for Omega is: \u03A9";
	    System.out.println(unicodeString);
	    StringBuilder sb = new StringBuilder(cleaned);
	    sb.reverse();
	    System.out.println(sb.toString());
	    System.out.println("^");
	    
	}

}
