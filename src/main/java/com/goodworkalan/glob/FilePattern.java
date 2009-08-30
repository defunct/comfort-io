package com.goodworkalan.glob;

/**
 * Match a file part with a wildcard pattern in a file glob.
 * 
 * @author Alan Gutierrez
 */
class FilePattern extends Part {
    /** The matches to apply against the chracters in the file part. */
    private final Part[] matches;

    /**
     * Create a file pattern match.
     * 
     * @param pattern
     *            The file pattern part.
     */
    public FilePattern(String pattern) {
        Part[] matches = new Part[pattern.length()];
        for (int i = 0; i < pattern.length(); i++) {
            char ch = pattern.charAt(i);
            switch (ch) {
            case '?':
                matches[i] = new AnyOne();
                break;
            case '*':
                matches[i] = new AnyOneOrMore();
                break;
            default:
                matches[i] = new Char(ch);
                break;
            }
        }
        this.matches = matches;
    }

    /**
     * Match the the string at the given offset in the given array of strings
     * representing a file path.
     * 
     * @param array
     *            An array of file path parts.
     * @param offset
     *            AN offset into the array of file path parts.
     * @return True if the path matches.
     */
    public boolean match(Object array, int offset) {
        String string = ((String[]) array)[offset];
        return descend(string, string.length(), 0, matches, 0);
    }
    
    /**
     * This object equals the given object if it is a file pattern
     * and all of the character matches are equal.
     * 
     * @param object
     *            The object to test for equality.
     * @return True if this object is equal to the given object.
     */
    @Override
    public boolean equals(Object object) {
        if (object instanceof FilePattern) {
            FilePattern filePattern = (FilePattern) object;
            if (matches.length != filePattern.matches.length) {
                return false;
            }
            for (int i = 0, stop = matches.length; i < stop; i++) {
                if (!matches[i].equals(filePattern.matches[i])) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * The hash code of the file pattern is derived from the hash codes of all
     * the character matches.
     * 
     * @return The hash code.
     */
    @Override
    public int hashCode() {
        int hashCode = 27;
        for (int i = 0, stop = matches.length; i < stop; i++) {
            hashCode = hashCode * 37 + matches[i].hashCode();
        }
        return hashCode;
    }
}
