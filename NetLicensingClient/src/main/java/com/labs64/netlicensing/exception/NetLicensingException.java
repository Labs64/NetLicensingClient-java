/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.labs64.netlicensing.exception;

/**
 * Base class for checked exceptions with a root cause.
 */
public abstract class NetLicensingException extends Exception {

    private static final long serialVersionUID = -1101213597571076128L;

    /**
     * Construct a <code>NetLicensingException</code> with the specified detail message.
     * 
     * @param msg
     *            the detail message
     */
    public NetLicensingException(final String msg) {
        super(msg);
    }

    /**
     * Construct a <code>NetLicensingException</code> with the specified detail message and cause exception.
     * 
     * @param msg
     *            the detail message
     * @param cause
     *            the cause exception
     */
    public NetLicensingException(final String msg, final Throwable cause) {
        super(msg, cause);
    }

    /**
     * Return the detail message, including the message from the cause exception if there is one.
     * 
     * @return error detail message
     */
    @Override
    public String getMessage() {
        return ExceptionUtils.buildMessage(super.getMessage(), getCause());
    }

    /**
     * Retrieve the innermost cause of this exception, if any.
     * 
     * @return the innermost exception, or <code>null</code> if none
     */
    public Throwable getRootCause() {
        Throwable rootCause = null;
        Throwable cause = getCause();
        while ((cause != null) && (cause != rootCause)) {
            rootCause = cause;
            cause = cause.getCause();
        }
        return rootCause;
    }

    /**
     * Retrieve the most specific cause of this exception, that is, either the innermost cause (root cause) or this
     * exception itself.
     * <p>
     * Differs from {@link #getRootCause()} in that it falls back to the present exception if there is no root cause.
     * 
     * @return the most specific cause (never <code>null</code>)
     */
    public Throwable getMostSpecificCause() {
        final Throwable rootCause = getRootCause();
        if (rootCause == null) {
            return this;
        } else {
            return rootCause;
        }
    }

    /**
     * Check whether this exception contains an exception of the given type: either it is of the given class itself or
     * it contains a cause cause of the given type.
     * 
     * @param exType
     *            the exception type to look for
     * @return whether there is a cause exception of the specified type
     */
    public boolean contains(final Class<Throwable> exType) {
        if (exType == null) {
            return false;
        }
        if (exType.isInstance(this)) {
            return true;
        }
        Throwable cause = getCause();
        if (cause == this) {
            return false;
        }
        if (cause instanceof NetLicensingException) {
            return ((NetLicensingException) cause).contains(exType);
        } else {
            while (cause != null) {
                if (exType.isInstance(cause)) {
                    return true;
                }
                if (cause.getCause() == cause) {
                    break;
                }
                cause = cause.getCause();
            }
            return false;
        }
    }

}
