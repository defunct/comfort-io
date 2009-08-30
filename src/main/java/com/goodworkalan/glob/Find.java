package com.goodworkalan.glob;

import java.io.File;
import java.util.ArrayList;

import java.util.List;


/**
 * A utility class representing a query against the file system.
 * 
 * @author Alan Gutierrez
 */
public class Find {
    private final List<File> path = new ArrayList<File>();
    
    public Find(String...directories) {
        for (String directory : directories) {
            path.add(new File(directory));
        }
    }
}
