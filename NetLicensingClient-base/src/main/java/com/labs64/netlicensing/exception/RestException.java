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
