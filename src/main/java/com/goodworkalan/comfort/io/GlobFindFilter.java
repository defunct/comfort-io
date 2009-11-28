package com.goodworkalan.comfort.io;

import java.util.ArrayList;
import java.util.List;

/**
 * A find filter that has a public list of globs. Used by find to
 * build a list of globs to include or exclude.
 * 
 * @author Alan Gutierrez
 */
abstract class GlobFindFilter implements FindFilter {
    /** The list of globs. */
    public final List<Glob> globs = new ArrayList<Glob>();
}
