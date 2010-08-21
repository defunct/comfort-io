package com.goodworkalan.comfort.io;

import java.io.File;
import java.util.StringTokenizer;

/**
 * A glob pattern for matching files. This glob implements the file pattern
 * made popular by Ant that is also used in Maven.
 * 
 * @author Alan Gutierrez
 */
public class Glob {
    /** The array of part matches. */
    private final Part[] matches;

    /**
     * Create a glob from the given glob pattern.
     * 
     * @param pattern
     *            The glob pattern.
     */
    public Glob(String pattern) {
        String[] parts = pattern.split("/");
        Part[] matches = new Part[parts.length];
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].equals("*")) {
                matches[i] = new AnyOne();
            } else if (parts[i].equals("**")) {
                matches[i] = new AnyOneOrMore();
            } else if (parts[i].indexOf('*') != -1
                    || parts[i].indexOf('?') != -1) {
                matches[i] = new FilePattern(parts[i]);
            } else {
                matches[i] = new FileName(parts[i]);
            }
        }
        this.matches = matches;
    }
    
    /**
     * Match against the given file.
     * 
     * @param file
     *            The file path to match.
     * @return True if this glob matches the given file.
     */
    public boolean match(File file) {
        return match(file.toString());
    }

    /**
     * Match against the given file name.
     * 
     * @param fileName
     *            The file path to match.
     * @return True if this glob matches the given file name.
     */
    public boolean match(String fileName) {
        StringTokenizer tokens = new StringTokenizer(fileName, File.separator);
        String[] parts = new String[tokens.countTokens()];
        int index = 0;
        while (tokens.hasMoreElements()) {
            parts[index++] = tokens.nextToken();
        }
        return Part.descend(parts, parts.length, 0, matches, 0);
    }

    /**
     * This object equals the given object if it is also a glob made from the
     * same glob pattern.
     * 
     * @param object
     *            The object to test for equality.
     * @return True if this object is equal to the given object.
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object instanceof Glob) {
            Glob glob = (Glob) object;
            if (matches.length != glob.matches.length) {
                return false;
            }
            for (int i = 0, stop = matches.length; i < stop; i++) {
                if (!matches[i].equals(glob.matches[i])) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * The hash code of the glob is derived from the hash codes of all the
     * path matches.
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
