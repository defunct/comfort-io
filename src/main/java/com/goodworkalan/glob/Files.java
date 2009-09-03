package com.goodworkalan.glob;

import static com.goodworkalan.glob.GlobException.COPY_FAILURE;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Set;

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
            throw new GlobException(COPY_FAILURE, e)
                .put("source", source).put("destination", destination);
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
        return file.delete();
    }

    /**
     * Create a path from the given set of files.
     * 
     * @param path
     *            A set of files.
     */
    public final static String path(Set<File> path) {
        StringBuilder string = new StringBuilder();
        String separator = "";
        for (File file : path) {
            string.append(separator).append(file.toString());
            separator = File.pathSeparator;
        }
        return string.toString();
    }
}