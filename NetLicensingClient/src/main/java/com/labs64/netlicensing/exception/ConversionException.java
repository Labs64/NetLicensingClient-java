/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.labs64.netlicensing.exception;

/**
 * This exception class should be used when there's a problem during the conversion from one representation of object to
 * another (for example, during the transformation of an XML item to an entity).
 */
public class ConversionException extends NetLicensingException {

    private static final long serialVersionUID = -3798344733724547819L;

    /**
     * Construct a <code>ConversionException</code> with the specified detail message.
     * 
     * @param msg
     *            the detail message
     */
    public ConversionException(final String msg) {
        super(msg);
    }

    /**
     * Construct a <code>ConversionException</code> with the specified detail message and cause exception.
     * 
     * @param msg
     *            the detail message
     * @param cause
     *            the cause exception
     */
    public ConversionException(final String msg, final Throwable cause) {
        super(msg, cause);
    }

}
