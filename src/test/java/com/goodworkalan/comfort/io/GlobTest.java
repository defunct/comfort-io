package com.goodworkalan.comfort.io;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.io.File;

import org.testng.annotations.Test;

import com.goodworkalan.comfort.io.AnyOne;
import com.goodworkalan.comfort.io.AnyOneOrMore;
import com.goodworkalan.comfort.io.Char;
import com.goodworkalan.comfort.io.FileName;
import com.goodworkalan.comfort.io.FilePattern;
import com.goodworkalan.comfort.io.Glob;

/**
 * Unit tests for the Glob class.
 * 
 * @author Alan Gutierrez
 */
public class GlobTest {
    /** Test the string constructor. */
    @Test
    public void constructor() {
        new Glob("");
    }

    /** Test many any single part. */
    @Test
    public void starGlob() {
        Glob glob = new Glob("*");
        assertTrue(glob.match(new File("hello")));
    }

    /** Test match exact. */
    @Test
    public void exactGlob() {
        Glob glob = new Glob("hello");
        assertTrue(glob.match(new File("hello")));
        assertFalse(glob.match(new File("world")));
    }

    /** Test literal match path. */
    @Test
    public void exactPathGlob() {
        Glob glob = new Glob("hello/world");
        assertTrue(glob.match(new File("hello/world")));
        assertFalse(glob.match(new File("world/hello")));
        assertFalse(glob.match(new File("hello/world/nurse")));
    }

    /** Test two match anys. */
    @Test
    public void starPathGlob() {
        Glob glob = new Glob("*/*");
        assertTrue(glob.match(new File("hello/world")));
        assertTrue(glob.match(new File("world/hello")));
        assertFalse(glob.match(new File("hello/world/nurse")));
    }

    /** Test match any parts multiple. */
    @Test
    public void depthGlob() {
        Glob glob = new Glob("**/hello");
        assertTrue(glob.match(new File("world/hello")));
        assertTrue(glob.match(new File("world/world/hello")));
    }

    /** Test a match any parts multiple with some trailing literals. */
    @Test
    public void depthExactGlob() {
        Glob glob = new Glob("**/hello/world");
        assertTrue(glob.match(new File("world/hello/world")));
        assertTrue(glob.match(new File("world/world/hello/world")));
        assertFalse(glob.match(new File("world/hello")));
        assertFalse(glob.match(new File("world/world/hello")));
    }

    /** Test two match any multiple conidtions. */
    @Test
    public void depthInMiddleGlob() {
        Glob glob = new Glob("**/hello/**/world");
        assertTrue(glob.match(new File("world/hello/foo/world")));
        assertTrue(glob.match(new File("world/hello/world")));
        assertTrue(glob.match(new File("world/world/hello/world")));
        assertFalse(glob.match(new File("world/hello")));
        assertFalse(glob.match(new File("world/world/hello")));
    }

    /** Test matching any file at any depth. */
    @Test
    public void matchAnything() {
        Glob glob = new Glob("**/*");
        assertTrue(glob.match(new File("world/hello/foo/world")));
        assertTrue(glob.match(new File("world/hello/world")));
        assertTrue(glob.match(new File("world/world/hello/world")));
        assertTrue(glob.match(new File("world/hello")));
        assertTrue(glob.match(new File("world/world/hello")));
    }

    /** Test file name wildcard matching. */
    @Test
    public void matchWildcard() {
        Glob glob = new Glob("he*o");
        assertTrue(glob.match(new File("heo")));
        assertTrue(glob.match(new File("hello")));
        assertFalse(glob.match(new File("world")));
    }

    /** Test the single character wildcard. */
    @Test
    public void matchWildchar() {
        Glob glob = new Glob("he?o");
        assertFalse(glob.match(new File("heo")));
        assertTrue(glob.match(new File("helo")));
        assertFalse(glob.match(new File("hello")));
        assertFalse(glob.match(new File("world")));
    }

    /** Test equality and hash codes of glob and parts. */
    @Test
    public void equality() {
        assertEquals(new AnyOne(), new AnyOne());
        assertFalse(new AnyOne().equals(new Object()));
        assertEquals(new AnyOneOrMore(), new AnyOneOrMore());
        assertFalse(new AnyOneOrMore().equals(new Object()));
        assertEquals(new Char('a'), new Char('a'));
        assertFalse(new Char('a').equals(new Char('b')));
        assertFalse(new Char('a').equals(new Object()));
        assertEquals(new FileName("a"), new FileName("a"));
        assertFalse(new FileName("a").equals(new FileName("b")));
        assertFalse(new FileName("a").equals(new Object()));
        assertFalse(new FilePattern("ab").equals(new Object()));
        assertFalse(new FilePattern("ab").equals(new FilePattern("ac")));
        assertFalse(new FilePattern("ab").equals(new FilePattern("a")));
        assertEquals(new FilePattern("a"), new FilePattern("a"));
        Glob glob = new Glob("hello/*/*.xml");
        assertEquals(glob, glob);
        assertEquals(glob, new Glob("hello/*/*.xml"));
        assertFalse(glob.equals("hello/world"));
        assertFalse(glob.equals( new Glob("hello/world")));
        assertFalse(glob.equals( new Glob("hello/world/*.xml")));
        assertEquals(new Glob("hello/*/*.xml").hashCode(), new Glob("hello/*/*.xml").hashCode());
    }
}
