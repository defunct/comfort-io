package com.goodworkalan.glob;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.util.LinkedHashSet;
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
        Files.copy(new File("src/test/java/com/goodworkalan/glob/FindTest.java"), copy);
        assertTrue(copy.exists());
        copy.delete();
        junk.delete();
    }
    
    /** Test a file copy I/O exception. */
    @Test
    public void copyFailure() {
        try {
            Files.copy(new File("src/test/java/com/goodworkalan/glob/Foo.java"), new File("target/junk"));
        } catch (GlobException e) {
            assertEquals(e.getCode(), GlobException.COPY_FAILURE);
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
}
