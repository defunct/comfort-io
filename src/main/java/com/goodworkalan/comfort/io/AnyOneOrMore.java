package com.goodworkalan.comfort.io;

/**
 * Match any multiple file parts, a depth match.
 * 
 * @author Alan Gutierrez
 */
class AnyOneOrMore extends Part {
    /**
     * Return true indicating that this match can match multiple parts
     * of a path.
     * 
     * @return True indciating that this match can match multiple parts.
     */
    public boolean multiple() {
        return true;
    }
    
    /**
     * This any one match is equal to the given object if it is also an any one
     * or more match.
     * 
     * @param object
     *            The object to test for equality.
     * @return True if this object is equal to the given object.
     */
    @Override
    public boolean equals(Object object) {
        return object instanceof AnyOneOrMore;
    }
    
    /**
     * Hash code is always the same value.
     * 
     * @return The hash code.
     */
    @Override
    public int hashCode() {
        return 7;
    }
}
