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
package com.labs64.netlicensing.util;

import com.labs64.netlicensing.exception.MalformedArgumentsException;

/**
 * Common utilities for precondition checking.
 */
public class CheckUtils {

    /**
     * Ensures that an object reference is not null.
     *
     * @param object
     *            object to check
     * @param msg
     *            exception message
     * @throws MalformedArgumentsException
     *             if object is null
     */
    private static void notNull(final Object object, final String msg) throws MalformedArgumentsException {
        if (object == null) {
            throw new MalformedArgumentsException(msg);
        }
    }

    /**
     * Ensures that a string is not null or empty.
     *
     * @param string
     *            string to check
     * @param msg
     *            exception message
     * @throws MalformedArgumentsException
     *             if string is null or empty
     */
    private static void notEmpty(final String string, final String msg) throws MalformedArgumentsException {
        if (string == null || string.length() == 0) {
            throw new MalformedArgumentsException(msg);
        }
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * @param parameter
     *            param to check
     * @param parameterName
     *            name of the parameter
     * @throws MalformedArgumentsException
     *             if parameter is null
     */
    public static void paramNotNull(final Object parameter, final String parameterName)
            throws MalformedArgumentsException {
        notNull(parameter, String.format("Parameter '%s' cannot be null", parameterName));
    }

    /**
     * Ensures that a string passed as a parameter to the calling method is not null or empty.
     *
     * @param parameter
     *            param to check
     * @param parameterName
     *            name of the parameter
     * @throws MalformedArgumentsException
     *             if parameter is null or empty
     */
    public static void paramNotEmpty(final String parameter, final String parameterName)
            throws MalformedArgumentsException {
        notEmpty(parameter, String.format("Parameter '%s' cannot be null or empty string", parameterName));
    }

}
