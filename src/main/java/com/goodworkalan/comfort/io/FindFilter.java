package com.goodworkalan.comfort.io;

import java.io.File;

/**
 * Differs from Java FileName filter in that it file base is not the directory
 * that contains the file, but the base directory where the recursive find was
 * performed.
 * 
 * @author Alan Gutierrez
 */
public interface FindFilter {
    /**
     * Whether or not the filter matches the given search base directory and
     * relative file path.
     * 
     * @param base
     *            The base directory of the search.
     * @param filePath
     *            The relative path into directory.
     * @return True if the file filter matches the given file path.
     */
    public boolean accept(File base, String filePath);
}
