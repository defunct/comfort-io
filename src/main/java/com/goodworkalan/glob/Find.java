package com.goodworkalan.glob;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


/**
 * A utility class representing a query against the file system.
 * 
 * @author Alan Gutierrez
 */
public class Find {
    /** The list of filters to apply to each file found in a directory tree. */
    private final List<FindFilter> filters = new ArrayList<FindFilter>();
    
    /**
     * Create a new find query.
     */
    public Find() {
    }

    /**
     * Include files that match the given pattern.
     * 
     * @param pattern
     *            A glob pattern to match.
     */
    public Find include(String pattern) {
        final Glob glob = new Glob(pattern);
        filters.add(new FindFilter() {
            public boolean accept(File base, String path) {
                return glob.match(path);
            }
        });
        return this;
    }

    /**
     * Exclude the files that match the given pattern.
     * 
     * @param pattern
     *            A glob pattern to match.
     */
    public Find exclude(String pattern) {
        final Glob glob = new Glob(pattern);
        filters.add(new FindFilter() {
            public boolean accept(File base, String path) {
                return ! glob.match(path);
            }
        });
        return this;
    }

    /**
     * Test the find conditions against the files in the given directory.
     * 
     * @param depth
     *            The depth of this directory relative to the base directory.
     * @param base
     *            The base directory of the find.
     * @param dir
     *            The directory files to test.
     * @param matches
     *            The set of files that match the conditions.
     */
    private void find(int depth, File base, File dir, Set<String> matches) {
        for (File file : dir.listFiles()) {
            String relative = Files.relativize(base, file).toString();
            boolean found = true;
            for (int i = 0, stop = filters.size(); found && i < stop; i++) {
                found = filters.get(i).accept(base, relative);
            }
            if (found) {
                matches.add(relative);
            }
            if (file.isDirectory()) {
                find(depth + 1, base, file, matches);
            }
        }
    }
    
    /**
     * Recursively search the given directory for the files that match the
     * conditions of this query.
     * <p>
     * FIXME Almost certainly want to return file name instead of file.
     * 
     * @param directory
     *            The directory to search.
     * @return A set of file names that match the conditions of this query.
     */
    public Set<String> find(File directory) {
        Set<String> matches = new LinkedHashSet<String>();
        if (directory.isDirectory()) {
            find(1, directory, directory, matches);
        }
        return matches;
    }
}
