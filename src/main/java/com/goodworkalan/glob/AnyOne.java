package com.goodworkalan.glob;

/**
 * Match a single part of a file path.
 *
 * @author Alan Gutierrez
 */
final class AnyOne extends Part {
    /**
     * This any one match is equal to the given object if it is also an any one
     * match.
     * 
     * @param object
     *            The object to test for equality.
     * @return True if this object is equal to the given object.
     */
    @Override
    public boolean equals(Object object) {
        return object instanceof AnyOne;
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
