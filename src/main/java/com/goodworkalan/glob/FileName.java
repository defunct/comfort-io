package com.goodworkalan.glob;

/**
 * Match a file name against a literal file name.
 *
 * @author Alan Gutierrez
 */
final class FileName extends Match {
    /** The file name to match. */
    private final String fileName;

    /**
     * Create a new file name match.
     * 
     * @param fileName
     *            The file name to match.
     */
    public FileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Match the part at the given offset into the given array of file parts
     * against the literal file name.
     * 
     * 
     * @param array
     *            An array of file path parts.
     * @param offset
     *            An offset into the array of file path parts.
     * @return True if the part matches the literal file name.
     */
    public boolean match(Object array, int offset) {
        return this.fileName.equals(((String[]) array)[offset]);
    }

    /**
     * This file name match is equal to the given object if it is also a file
     * name match and the file names to match are equal.
     * 
     * @param object
     *            The object to test for equality.
     * @return True if this object is equal to the given object.
     */
    @Override
    public boolean equals(Object object) {
        if (object instanceof FileName) {
            return fileName.equals(((FileName) object).fileName);
        }
        return false;
    }

    /**
     * The hash code of the file name match is the hash code of the file name to
     * match.
     * 
     * @return The hash code.
     */
    @Override
    public int hashCode() {
        return fileName.hashCode();
    }
}
