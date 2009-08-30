package com.goodworkalan.glob;

import com.goodworkalan.cassandra.CassandraException;
import com.goodworkalan.cassandra.Report;

/**
 * A general purpose exception that indicates that an error occurred in one of
 * the classes in the glob package.
 * 
 * 
 * @author Alan Gutierrez
 */
public class GlobException extends CassandraException {
    /** The serial version id. */
    private static final long serialVersionUID = 1L;
    
    /** Unable to copy a file. */
    public static final int COPY_FAILURE = 101;

    /**
     * Create a glob exception with the given error code and the given cause.
     * 
     * @param code
     *            The error code.
     * @param cause
     *            The cause.
     */
    public GlobException(int code, Throwable cause) {
        super(code, new Report(), cause);
    }
}
