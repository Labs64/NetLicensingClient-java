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

import org.apache.commons.lang3.StringUtils;

/**
 * NetLicensing supported currencies.
 */
public enum Currency {

    NONE(""),

    EUR("EUR");

    private final String value;

    /**
     * Instantiates a new currency.
     *
     * @param currency
     *            currency value
     */
    Currency(final String currency) {
        value = currency;
    }

    /**
     * Get enum value.
     *
     * @return enum value
     */
    public String value() {
        return value;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return value;
    }

    /**
     * Parse currency value to {@link Currency} enum.
     *
     * @param value
     *            currency value
     * @return {@link Currency} enum object or throws {@link IllegalArgumentException} if no corresponding
     *         {@link Currency} enum object found
     */
    public static Currency parseValue(final String value) {
        for (final Currency currency : Currency.values()) {
            if (currency.value.equalsIgnoreCase(value)) {
                return currency;
            }
        }
        if (value != null && StringUtils.isBlank(value)) {
            return NONE;
        }
        throw new IllegalArgumentException(value);
    }

    /**
     * Parse currency value to {@link Currency} enum, nothrow version.
     *
     * @param value
     *            licenseType value as string
     * @return {@link Currency} enum object or {@code null} if argument doesn't match any of the enum values
     */
    public static Currency parseValueSafe(final String value) {
        try {
            return parseValue(value);
        } catch (final IllegalArgumentException e) {
            return null;
        }
    }

}
