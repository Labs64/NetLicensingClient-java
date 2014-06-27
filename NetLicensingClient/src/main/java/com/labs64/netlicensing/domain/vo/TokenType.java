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
package com.labs64.netlicensing.domain.vo;

/**
 * This enum defines available token types.
 */
public enum TokenType {

    DEFAULT,

    APIKEY,

    REGISTRATION,

    PASSWORDRESET,

    SHOP;

    public static TokenType parseString(final String token) {
        if (token != null) {
            for (final TokenType tokenType : TokenType.values()) {
                if (token.equalsIgnoreCase(tokenType.name())) {
                    return tokenType;
                }
            }
        }
        return null;
    }

}
