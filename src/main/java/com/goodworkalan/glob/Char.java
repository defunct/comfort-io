package com.goodworkalan.glob;

/**
 * Match a single character in a file pattern match.
 * 
 * @author Alan Gutierrez.
 * 
 */
final class Char extends Part {
    /** The single character to match. */
    private final char ch;

    /**
     * Create a single character match for a file pattern match.
     * 
     * @param ch
     *            The single character to match.
     */
    public Char(char ch) {
        this.ch = ch;
    }

    /**
     * Match the character at the given offset in the given string object.
     * 
     * @param array
     *            A string representing a file part.
     * @param offset
     *            A character offset into the string.
     * @return True if this character match matches the character at the given
     *         offset in the given string.
     */
    public boolean match(Object array, int offset) {
        return ch == ((String) array).charAt(offset);
    }

    /**
     * This char match is equal to the given object if it is also a char match
     * and the characters to match are equal.
     * 
     * @param object
     *            The object to test for equality.
     * @return True if this object is equal to the given object.
     */
    @Override
    public boolean equals(Object object) {
        if (object instanceof Char) {
            return ch == ((Char) object).ch;
        }
        return false;
    }
    
    /**
     * The hash code is the hash code of the character to match.
     * 
     * @return The hash code.
     */
    @Override
    public int hashCode() {
        return (int) ch;
    }
}
