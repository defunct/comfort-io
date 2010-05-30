package com.goodworkalan.comfort.io;

import static com.goodworkalan.comfort.io.ComfortIOException.COPY_FAILURE;
import static com.goodworkalan.comfort.io.ComfortIOException.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
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
     *                If an I/O error occurs.
     */
    public final static void copy(File source, File destination) {
        try {
            FileChannel srcChannel = new FileInputStream(source).getChannel();
            try {
                FileChannel dstChannel = new FileOutputStream(destination).getChannel();
                try {
                    dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
                } finally {
                    dstChannel.close();
                }
            } finally {
                srcChannel.close();
            }
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
    public final static boolean unlink(File file) {
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                if (!unlink(child)) {
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
            try {
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
            } finally {
                reader.close();
            }
        } catch (IOException e) {
            throw new ComfortIOException(READ_FAILURE, e, file);
        }
        return lines;
    }

    /**
     * Write the given list of lines to file using the default system line
     * separator obtained from <code>System.getProperty("line.separator")</code>
     * 
     * @param file
     *            The file to write to.
     * @param lines
     *            The lines to write.
     * @exception ComfortIOException
     *                To wrap an I/O exception if the file cannot be written.
     */
    public final static void pour(File file, Collection<?> lines) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            try {
                for (Object string : lines) {
                    writer.write(string.toString());
                    writer.write("\n");
                }
            } finally {
                writer.close();
            }
        } catch (IOException e) {
            throw new ComfortIOException(WRITE_FAILURE, e, file);
        }
    }

    /**
     * Create a direct representation of the file without any dotted relative
     * references to parent directories or to the current directory. The
     * resulting file will have no double dotted ".." parent directory parts,
     * nor any single "." current directory parts. However, only the current
     * path Parent and current directory references are
     * <p>
     * This method differs from <code>File.getCannonicalPath()</code> in that it
     * does not resolve symbolic links and therefore cannot raise an I/O
     * exception. It does not resolve files relative to the current working
     * directory. The method only considers the path information present in the
     * given file. It does not add any additional context from the file system.
     * <p>
     * This method will work both relative and absolute files. Relative files
     * create a new relative reference with the parent and current directory
     * references decided by the path parts available in the relative path.
     * Because the current working directory is not considered when resolving a
     * path, a relative file path that references directories above the first
     * part of the relative file results in an
     * <code>IllegalArgumentException</code>. The path <code>a/b/../c</code>
     * resolves to <code>a/c</code>, but the path <code>a/../b/c</code> throws
     * an exception since the parent of <code>a</code> is not available in the
     * relative path.
     * <p>
     * If parent references go beyond the root of an absolute path an
     * <code>IllegalArgumentException</code> is raised.
     * 
     * @param file
     *            The file to make direct.
     * @return A file whose dotted parent and current directory references have
     *         been resolved.
     * @exception IllegalArgumentException
     *                If the parent or current directory references navigate
     *                above the first part of a relative path or the root of an
     *                absolute path.
     */
    public final static File direct(File file) {
        File root = null;
        LinkedList<String> parts = new LinkedList<String>();
        while (file != null) {
            if (file.getName().equals("..")) {
                file = file.getParentFile();
                if (file == null) {
                    throw new IllegalArgumentException();
                }
                file = file.getParentFile();
                if (file == null) {
                    throw new IllegalArgumentException();
                }
            } else if (file.getName().equals(".")) {
                file = file.getParentFile();
            } else {
                parts.addFirst(file.getName());
                root = file;
                file = file.getParentFile();
            }
        }
        
        if (parts.isEmpty()) {
            throw new IllegalArgumentException();
        }

        if (root.getParentFile() != null) {
            root = new File(root.getName());
        }

        parts.removeFirst();

        return file(root, parts.toArray(new String[parts.size()]));
    }

    /**
     * Create a new relative file from the given file by removing the given file
     * base. No attempt is made to resolve dotted parent directory or current
     * directory references in the file or the base.
     * 
     * @param base
     *            The based directory to remove.
     * @param file
     *            The file to relativize.
     * @return The relativized file.
     * @exception IllegalArgumentException
     *                If the relativized file is not a child of the base file.
     */
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
