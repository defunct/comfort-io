package com.goodworkalan.comfort.io;

import static com.goodworkalan.comfort.io.ComfortIOException.READ_FAILURE;
import static com.goodworkalan.comfort.io.ComfortIOException.WRITE_FAILURE;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.testng.annotations.Test;

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
        assertFalse(Files.unlink(file));
    }
    
    /** Test recursive deletion. */
    @Test
    public void delete() {
        new File("target/junk/path").mkdirs();
        Files.unlink(new File("target/junk"));
        Files.unlink(new File("target/junk"));
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
    
    /** Test creation of a file name through catenation. */
    @Test
    public void fileName() {
        File file = new File("dir");
        file = new File(file, "file.txt");
        assertEquals(Files.file("dir", "file.txt"), file.toString());
    }
    
    /** Test slurp lines. */
    @Test
    public void slurp() {
        List<String> slurped = Files.slurp(new File("src/test/readable/lines.txt"));
        assertEquals(slurped.get(1), "b");
    }
    
    /** Test I/O exceptions during a slurp. */
    @Test
    public void splurpException() {
        try {
            Files.slurp(new File("target/missing.txt"));
        } catch (ComfortIOException e) {
            assertEquals(e.getCode(), READ_FAILURE);
            System.out.println(e.getMessage());
        }
    }
    
    /** Test pour lines. */
    @Test
    public void pour() throws IOException {
        List<String> lines = Arrays.asList("a", "b", "c");
        File read = new File("target/lines.txt");
        try {
            Files.pour(new File("target/lines.txt"), lines);
            BufferedReader readers = new BufferedReader(new FileReader("target/lines.txt"));
            assertEquals(readers.readLine(), "a");
            assertEquals(readers.readLine(), "b");
            assertEquals(readers.readLine(), "c");
            assertNull(readers.readLine());
        } finally {
            read.delete();
        }
    }    
    
    /** Test I/O exceptions during a pour. */
    @Test
    public void pourException() {
        try {
            Files.pour(new File("target"), Arrays.asList("a"));
        } catch (ComfortIOException e) {
            assertEquals(e.getCode(), WRITE_FAILURE);
            System.out.println(e.getMessage());
        }
    }
    
    /** Test direct method. */
    @Test
    public void direct() {
        assertEquals(Files.direct(new File("/a/./c")), new File("/a/c"));
        assertEquals(Files.direct(new File("a")), new File("a"));
        assertEquals(Files.direct(new File("a/.")), new File("a"));
        assertEquals(Files.direct(new File("././a/.")), new File("a"));
        assertEquals(Files.direct(new File("a/c/../b")), new File("a/b"));
    }

    /** Test relative parent directory with no parent directory. */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void parentNoParent() {
        Files.direct(new File("a/.."));
    }
    
    /** Test relative parent directory with no self directory. */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void parentNoSelf() {
        Files.direct(new File(".."));
    }

    /** Test relative self directory with no self directory. */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void selfNoSelf() {
        Files.direct(new File("."));
    }
    
    /** Test relativize for a file that is not the child of the base. */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void badRelativize() {
        Files.relativize(new File("/a/b"), new File("/c/b"));
    }
}
