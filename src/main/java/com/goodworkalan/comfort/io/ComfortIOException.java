package com.goodworkalan.comfort.io;

import java.util.ResourceBundle;

/**
 * A general purpose exception that indicates that an error occurred in one of
 * the classes in the comfort.io package.
 * 
 * 
 * @author Alan Gutierrez
 */
public class ComfortIOException extends RuntimeException {
    /** The serial version id. */
    private static final long serialVersionUID = 1L;
    
    /** Unable to copy a file. */
    public static final int COPY_FAILURE = 101;
    
    /** Unable to slurp a file. */
    public static final int SLURP_FAILURE = 102;
    
    /** The error code. */
    private final int code;
    
    /** The detail message format arguments. */
    private final Object[] arguments;

    /**
     * Create a glob exception with the given error code and the given cause.
     * 
     * @param code
     *            The error code.
     * @param cause
     *            The cause.
     */
    public ComfortIOException(int code, Throwable cause, Object...arguments) {
        super(null, cause);
        this.code = code;
        this.arguments = arguments;
    }
    
    /**
     * Get the error code.
     * 
     * @return The error code.
     */
    public int getCode() {
        return code;
    }
    
    /**
     * Returns the detail message string of this error.
     * 
     * @return The detail message string of this error.
     */
    @Override
    public String getMessage() {
        ResourceBundle bundle = ResourceBundle.getBundle(getClass().getPackage().getName() + ".mix.exceptions");
        return String.format(bundle.getString(Integer.toString(code)), arguments);
    }
}
