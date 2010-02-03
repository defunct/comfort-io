package com.goodworkalan.comfort.io;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import org.testng.annotations.Test;

import com.goodworkalan.comfort.io.ComfortIOException;
import com.goodworkalan.comfort.io.Files;

/**
 * Unit tests for the Files utility methods.
 *
 * @author Alan Gutierrez
 */
public class FilesTest {
    /** Test the constructor for the sake of coverage. */
    @Test
    public void constructor() {
        new Files();
    }

    /** Test a failed file deletion in a sub directory. */
    @Test
    public void deleteFailure() {
        File undeletable = mock(File.class);
        when(undeletable.isDirectory()).thenReturn(false);
        when(undeletable.delete()).thenReturn(false);
        when(undeletable.exists()).thenReturn(true);
        File file = mock(File.class);
        when(file.isDirectory()).thenReturn(true);
        when(file.listFiles()).thenReturn(new File[] { undeletable });
        assertFalse(Files.delete(file));
    }
    
    /** Test recursive deletion. */
    @Test
    public void delete() {
        new File("target/junk/path").mkdirs();
        Files.delete(new File("target/junk"));
    }
    
    /** Test a file copy. */
    @Test
    public void copy() {
        File junk = new File("target/junk");
        junk.mkdirs();
        File copy = new File(junk, "FileTest.java");
        Files.copy(new File("src/test/java/com/goodworkalan/comfort/io/FindTest.java"), copy);
        assertTrue(copy.exists());
        copy.delete();
        junk.delete();
    }
    
    /** Test a file copy I/O exception. */
    @Test(expectedExceptions=ComfortIOException.class)
    public void copyFailure() {
        try {
            Files.copy(new File("src/test/java/com/goodworkalan/glob/Foo.java"), new File("target/junk"));
        } catch (ComfortIOException e) {
            assertEquals(e.getMessage(), "Unable to copy file from (src/test/java/com/goodworkalan/glob/Foo.java) to (target/junk).");
            assertEquals(e.getCode(), ComfortIOException.COPY_FAILURE);
            throw e;
        }
    }
    
    /** Test path creation. */
    @Test
    public void path() {
        Set<File> path = new LinkedHashSet<File>();
        path.add(new File("foo/bar"));
        path.add(new File("baz"));
        assertEquals(Files.path(path), "foo/bar:baz");
    }
    
    /** Test directory based file. */
    @Test
    public void directoryFile() throws IOException {
        File directory = new File(".").getCanonicalFile();
        File file = null;
        file = new File(directory, "dir");
        file = new File(file, "file.txt");
        assertEquals(file, Files.file(directory, "dir", "file.txt"));
    }
}
