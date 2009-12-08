package com.goodworkalan.comfort.io;

import com.goodworkalan.cassandra.CassandraException;
import com.goodworkalan.cassandra.Report;

/**
 * A general purpose exception that indicates that an error occurred in one of
 * the classes in the comfort.io package.
 * 
 * 
 * @author Alan Gutierrez
 */
public class ComfortIOException extends CassandraException {
    /** The serial version id. */
    private static final long serialVersionUID = 1L;
    
    /** Unable to copy a file. */
    public static final int COPY_FAILURE = 101;
    
    /** Unable to slurp a file. */
    public static final int SLURP_FAILURE = 102;

    /**
     * Create a glob exception with the given error code and the given cause.
     * 
     * @param code
     *            The error code.
     * @param cause
     *            The cause.
     */
    public ComfortIOException(int code, Throwable cause) {
        super(code, new Report(), cause);
    }
}
