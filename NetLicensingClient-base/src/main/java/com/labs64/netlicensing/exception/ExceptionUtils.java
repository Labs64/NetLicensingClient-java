package com.labs64.netlicensing.exception;

/**
 * Helper class for implementing exception classes which are capable of holding cause exceptions.
 */
abstract class ExceptionUtils {

    /**
     * Build a message for the given base message and root cause.
     * 
     * @param message
     *            the base message
     * @param cause
     *            the root cause
     * @return the full exception message
     */
    public static String buildMessage(final String message, final Throwable cause) {
        if (cause != null) {
            StringBuilder sb = new StringBuilder();
            if (message != null) {
                sb.append(message).append("; ");
            }
            sb.append("cause exception is ").append(cause);
            return sb.toString();
        } else {
            return message;
        }
    }

}
