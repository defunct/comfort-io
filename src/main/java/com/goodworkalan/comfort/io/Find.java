package com.goodworkalan.comfort.io;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * A utility class representing a query against the file system.
 * 
 * @author Alan Gutierrez
 */
public class Find {
    /** The list of filters to apply to each file found in a directory tree. */
    private final Map<String, FindFilter> filters = new HashMap<String, FindFilter>();

    /**
     * Create a new find query.
     */
    public Find() {
    }

    /**
     * Return true if this find has filters. Sometimes an operation may with so
     * specify default filters if not otherwise specified.
     * 
     * @return True if this file search has filters.
     */
    public boolean hasFilters() {
        return !filters.isEmpty();
    }

    /**
     * Include files that match the given pattern.
     * 
     * @param pattern
     *            A glob pattern to match.
     * @return This <code>Find</code> object in order to continue specifying
     *         criteria.
     */
    public Find include(String pattern) {
        GlobFindFilter globs = (GlobFindFilter) filters.get("include");
        if (globs == null) {
            globs = new GlobFindFilter() {
                public boolean accept(File base, String path) {
                    for (Glob glob : globs) {
                        if (glob.match(path)) {
                            return true;
                        }
                    }
                    return false;
                }
            };
            filters.put("include", globs);
        }
        globs.globs.add(new Glob(pattern));
        return this;
    }

    /**
     * Exclude the files that match the given pattern.
     * 
     * @param pattern
     *            A glob pattern to match.
     * @return This <code>Find</code> object in order to continue specifying
     *         criteria.
     */
    public Find exclude(String pattern) {
        GlobFindFilter globs = (GlobFindFilter) filters.get("exclude");
        if (globs == null) {
            globs = new GlobFindFilter() {
                public boolean accept(File base, String path) {
                    for (Glob glob : globs) {
                        if (glob.match(path)) {
                            return false;
                        }
                    }
                    return true;
                }
            };
            filters.put("exclude", globs);
        }
        globs.globs.add(new Glob(pattern));
        return this;
    }

    /**
     * Include only files that are <em>normal</em> files according to
     * <code>File.isFile</code>. A files is <em>normal</em> if it is not a
     * directory and satisfies additional system specific criteria. Files
     * created in Java that are not directories are considered <em>normal</em>
     * files.
     * 
     * @return This <code>Find</code> object in order to continue specifying
     *         criteria.
     */
    public Find filesOnly() {
        filters.put("file", new FindFilter() {
            public boolean accept(File base, String filePath) {
                return new File(base, filePath).isFile();
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
            for (FindFilter filter : filters.values()) {
                found = filter.accept(base, relative);
                if (!found) {
                    break;
                }
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
