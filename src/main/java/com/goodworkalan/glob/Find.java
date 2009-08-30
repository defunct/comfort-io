package com.goodworkalan.glob;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;


/**
 * A utility class representing a query against the file system.
 * 
 * @author Alan Gutierrez
 */
public class Find {
    /** The list of filters to apply to each file found in a directory tree. */
    private final List<FileFilter> filters = new ArrayList<FileFilter>();
    
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
        filters.add(new FileFilter() {
            public boolean accept(File file) {
                return glob.match(file);
            }
        });
        return this;
    }

    /**
     * Test the find conditions against the files in the given directory.
     * 
     * @param depth
     *            The depth of this directory relative to the base directory.
     * @param dir
     *            The directory files to test.
     * @param matches
     *            The set of files that match the conditions.
     */
    private void find(int depth, File dir, Set<File> matches) {
        for (File file : dir.listFiles()) {
            boolean found = true;
            for (int i = 0, stop = filters.size(); found && i < stop; i++) {
                found = filters.get(i).accept(file);
            }
            if (found) {
                StringTokenizer parts = new StringTokenizer(file.getAbsolutePath(), File.separator);
                for (int i = 0, stop = parts.countTokens() - depth; i < stop; i++) {
                    parts.nextToken();
                }
                StringBuilder fileName = new StringBuilder();
                String separator = "";
                while (parts.hasMoreTokens()) {
                    fileName.append(separator).append(parts.nextToken());
                    separator = File.separator;
                }
                matches.add(new File(fileName.toString()));
            }
            if (file.isDirectory()) {
                find(depth + 1, file, matches);
            }
        }
    }
    
    /**
     * Recursively search the given directory for the files that match the
     * conditions of this query.
     * 
     * @param directory
     *            The directory to search.
     * @return A set of files that match the conditions of this query.
     */
    public Set<File> find(File directory) {
        Set<File> matches = new LinkedHashSet<File>();
        if (directory.isDirectory()) {
            find(1, directory, matches);
        }
        return matches;
    }
}
