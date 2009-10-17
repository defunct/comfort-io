package com.goodworkalan.glob;


/**
 * A match strategy a glob pattern.
 * 
 * @author Alan Gutierrez
 */
abstract class Part {
    /**
     * Test this match against the object found at the given offset into the
     * given array. This default implementation always returns true.
     * <p>
     * The array may be either an array of file path parts or else a String
     * object representing a part in a file path. The same logic is used for
     * both matching parts of the path, and for matching characters in a part of
     * the path.
     * <p>
     * Yes, this could conceivably be a parameterized type, but not easily. We'd
     * have to convert the array of file parts and the String to a common type
     * that could be parameterized, probably <code>java.util.List</code>.
     * 
     * @param array
     *            An indexed object.
     * @param offset
     *            An offset into the index.
     * @return True if this match matches the object at the given index in the
     *         given array.
     */
    public boolean match(Object array, int offset) {
        return true;
    }

    /**
     * Whether or not this match can match more than one part.
     * 
     * @return True if the match can match more than one part.
     */
    public boolean multiple() {
        return false;
    }

    /**
     * Descend the path matching the object at the given index against the given
     * array of the given length against the given array of matches starting at
     * the given match offset. The length parameter must be provided as a
     * separate parameter since the array may be either an array of strings or a
     * string. This method will recursively call itself while advancing the
     * indexes while the matches match the parts.
     * 
     * @param parts
     *            An array of parts to match.
     * @param length
     *            The length of the array of parts to match.
     * @param offset
     *            An offset into the array of parts to match.
     * @param matches
     *            An array of matches.
     * @param matchIndex
     *            The start index for the match array.
     * @return True if the parts are matched by the given matches.
     */
    public static boolean descend(Object parts, int length, int offset, Part[] matches, int matchIndex) {
        if (matches[matchIndex].multiple()) {
            int partsLength = length - offset;
            int matchesLength = matches.length - matchIndex;
            for (int i = matchIndex; i < matches.length; i++) {
                if (matches[i].multiple()) {
                    matchesLength--;
                }
            }
            for (int repeat = 0; repeat < (partsLength - matchesLength) + 2; repeat++) {
                if (match(parts, length, offset, matches, matchIndex, repeat)) {
                    return true;
                }
            }
            return false;
        }
        return match(parts, length, offset, matches, matchIndex, 1);
    }

    /**
     * Match the object at the given offset into the given array of the given
     * length against the match at the given match offset into the given match
     * array for the given number of repetitions.
     * 
     * @param parts
     *            An array of parts to match.
     * @param length
     *            The length of the array of parts to match.
     * @param partIndex
     *            An offset into the array of parts to match.
     * @param matches
     *            An array of matches.
     * @param matchIndex
     *            An offset into the array of matches.
     * @param repeat
     *            The number of times to repeat the match.
     * @return True if the array parts match the matches for the given number of
     *         repetitions.
     */
    private static boolean match(Object parts, int length, int partIndex, Part[] matches, int matchIndex, int repeat) {
        boolean matched = true;
        for (int i = 0; matched && i < repeat; i++) {
            if (!matches[matchIndex].match(parts, partIndex + i)) {
                matched = false;
            }
        }
        if (matched) {
            if (matchIndex + 1 == matches.length) {
                if (partIndex + repeat == length) {
                    return true;
                }
                return false;
            } else if (partIndex + repeat == length) {
                return false;
            }
            return descend(parts, length, partIndex + repeat, matches, matchIndex + 1);
        }
        return false;
    }

}
