package com.goodworkalan.comfort.io;

import static com.goodworkalan.comfort.io.ComfortIOException.COPY_FAILURE;
import static com.goodworkalan.comfort.io.ComfortIOException.SLURP_FAILURE;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Utility methods for files and directories.
 *
 * @author Alan Gutierrez
 */
public class Files {
    /**
     * Copy the given source file to the given destination file.
     * 
     * @param source
     *            The source file.
     * @param destination
     *            The destination file.
     * @exception GlobException
     *                If an I/O error occours.
     */
    public final static void copy(File source, File destination) {
        try {
            FileChannel srcChannel = new FileInputStream(source).getChannel();
            FileChannel dstChannel = new FileOutputStream(destination).getChannel();

            dstChannel.transferFrom(srcChannel, 0, srcChannel.size());

            srcChannel.close();
            dstChannel.close();
        } catch (IOException e) {
            throw new ComfortIOException(COPY_FAILURE, e, source, destination);
        }
    }

    /**
     * Delete a file or recursively delete a directory.
     * 
     * @param file
     *            The file or directory to delete.
     * @return True if the file or directory was successfully deleted.
     */
    public final static boolean delete(File file) {
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                if (!delete(child)) {
                    return false;
                }
            }
        }
        return file.delete() || ! file.exists();
    }

    /**
     * Create a file search path from the given set of files.
     * 
     * @param path
     *            A set of files.
     */
    public final static String path(Collection<File> path) {
        StringBuilder string = new StringBuilder();
        String separator = "";
        for (File file : path) {
            string.append(separator).append(file.toString());
            separator = File.pathSeparator;
        }
        return string.toString();
    }

    /**
     * Create a file path from the given list of file names.
     * 
     * @param names
     *            A list of file named to catenate into a file path.
     * @return A file path using the system file separator.
     */
    public final static String file(String...names) {
        StringBuilder string = new StringBuilder();
        String separator = "";
        for (String name : names) {
            string.append(separator).append(name);
            separator = File.separator;
        }
        return string.toString();
    }

    /**
     * Create a file path from the given list of file names using the given
     * directory as a root.
     * 
     * @param directory
     *            The directory root.
     * @param names
     *            A list of file named to catenate into a file path.
     * @return A file path using the system file separator.
     */
    public final static File file(File directory, String...names) {
        StringBuilder string = new StringBuilder();
        String separator = "";
        for (String name : names) {
            string.append(separator).append(name);
            separator = File.separator;
        }
        return new File(directory, string.toString());
    }

    /**
     * Slurp an entire file in to an array list of lines in the file. Line
     * endings are removed.
     * 
     * @param file
     *            The file to slurp.
     * @return A list of the lines in the file.
     * @exception ComfortIOException
     *                For any I/O error.
     */
    public final static List<String> slurp(File file) {
        String line;
        List<String> lines = new ArrayList<String>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            throw new ComfortIOException(SLURP_FAILURE, e, file);
        }
        return lines;
    }
    
    public final static File relativize(File base, File file) {
        LinkedList<File> parts = new LinkedList<File>();
        File iterator = file;
        while (!iterator.equals(base)) {
            parts.addFirst(iterator);
            iterator = iterator.getParentFile();
            if (iterator == null) {
                throw new IllegalArgumentException();
            }
        }
        File relative = new File(parts.removeFirst().getName());
        while (!parts.isEmpty()) {
            relative = new File(relative, parts.removeFirst().getName());
        }
        return relative;
    }
}
