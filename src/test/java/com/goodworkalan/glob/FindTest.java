package com.goodworkalan.glob;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.util.Set;

import org.testng.annotations.Test;

/**
 * Unit tests for the Find class.
 *
 * @author Alan Gutierrez
 */
public class FindTest {
    /** Test find when given a file instead of a directory. */
    @Test
    public void fileBase() {
        assertEquals(new Find().find(new File("README")).size(), 0);
    }

    /** Test the default constructor. */
    @Test
    public void findAll() {
        Set<String> files = new Find().find(new File("src/test/java"));
        assertTrue(files.contains("com"));
        assertTrue(files.contains("com/goodworkalan"));
        assertTrue(files.contains("com/goodworkalan/glob"));
        assertTrue(files.contains("com/goodworkalan/glob/FindTest.java"));
    }
    
    /** Test the include condition. */
    @Test
    public void include() {
        Set<String> files = new Find().include("**/*.java").find(new File("src/test/java"));
        assertFalse(files.contains("com"));
        assertFalse(files.contains("com/goodworkalan"));
        assertFalse(files.contains("com/goodworkalan/glob"));
        assertTrue(files.contains("com/goodworkalan/glob/FindTest.java"));
    }
    
    /** Test the exclude condition. */
    @Test
    public void exclude() {
        Set<String> files = new Find().include("**/*.java").exclude("**/FilesTest.java").find(new File("src/test/java"));
        assertFalse(files.contains("com"));
        assertFalse(files.contains("com/goodworkalan"));
        assertFalse(files.contains("com/goodworkalan/glob"));
        assertFalse(files.contains("com/goodworkalan/glob/FilesTest.java"));
        assertTrue(files.contains("com/goodworkalan/glob/FindTest.java"));
    }
}
