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
 * The exception class that should be used when the content of the response of NetLicensing service doesn't
 * meet expectations that we impose on it.
 */
public class WrongResponseFormatException extends BaseCheckedException {

    private static final long serialVersionUID = -2548284543777416222L;

    /**
     * Construct a <code>ConversionException</code> with the specified detail message.
     *
     * @param msg
     */
    public WrongResponseFormatException(final String msg) {
        super(msg);
    }

}
