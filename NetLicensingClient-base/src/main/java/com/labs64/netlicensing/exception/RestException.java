package com.labs64.netlicensing.exception;

/**
 * The Class RestException can be used in cases where no checked exception can be thrown (e.g. 3pp interfaces
 * implementation).
 */
public class RestException extends BaseCheckedException {

    private static final long serialVersionUID = -3863879794574523844L;

    /**
     * Construct a <code>RestException</code> with the specified detail message.
     *
     * @param msg the detail message
     */
    public RestException(final String msg) {
        super(msg);
    }

    /**
     * Construct a <code>RestException</code> with the specified detail message and cause exception.
     *
     * @param msg   the detail message
     * @param cause the cause exception
     */
    public RestException(final String msg, final Throwable cause) {
        super(msg, cause);
    }

}
